package com.minimocms.data.mysql;

import com.jcabi.aspects.Cacheable;
import com.jcabi.jdbc.ColumnOutcome;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.Outcome;
import com.jcabi.jdbc.SingleOutcome;
import com.minimocms.Minimo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by Matt on 16/02/2015.
 */
public class MysqlStore {

    // Use the following to allow large files
    // set global max_allowed_packet=10485760;


    final static String SERVER="localhost";

    String dbName;

    public MysqlStore(String dbName){
        this.dbName=dbName;
    }



    public Long insert(String collectionName, String name, byte[] blob) {
        try {
            return sql("insert into " + collectionName + " (name,data) values (?,?)")
                    .set(name)
                    .set(blob)
                    .insert(Outcome.LAST_INSERT_ID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    public Collection<String> names(String collectionName){

        try {
            return sql("select name from " + collectionName)
                    .select(new ColumnOutcome<String>(String.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return new ArrayList<>();
    }

    public String select(String collectionName,String name){


        try {
            return sql("select data from "+collectionName+" where name=?")
                    .set(name)
                    .select(new SingleOutcome<String>(String.class));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public byte[] selectBinary(String collectionName,String name){


        try {
            return sql("select data from "+collectionName+" where name=?")
                    .set(name)
                    .select(new SingleOutcome<byte[]>(byte[].class));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static final String userName="root";
    private static final String password="root";



//    public JdbcSession sql(String stmt){
//        try{
//            return jdbc().sql(stmt);
//        } catch(SQLException e){
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public static Outcome<ResultSet> RESULTS = new Outcome<ResultSet>() {
//        @Override
//        public ResultSet handle(ResultSet resultSet, Statement statement) throws SQLException {
//            return resultSet;
//        }
//    };

//    public static Outcome<List<Map<String,Object>>> RESULT_MAP = new Outcome<List<Map<String,Object>>>() {
//        @Override
//        public List<Map<String,Object>> handle(ResultSet rs, Statement statement) throws SQLException {
//            ResultSetMetaData md = rs.getMetaData();
//            int columns = md.getColumnCount();
//            ArrayList list = new ArrayList();
//            while (rs.next()){
//                HashMap row = new HashMap();
//                for(int i=1; i<=columns; ++i){
//                    row.put(md.getColumnName(i),rs.getObject(i));
//                }
//                list.add(row);
//            }
//            return list;
//        }
//    };

    public static Outcome<List<Map<String,Object>>> RESULT_MAP = (ResultSet rs, Statement statement) -> {
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            ArrayList list = new ArrayList();
            while (rs.next()){
                HashMap row = new HashMap();
                for(int i=1; i<=columns; ++i){
                    row.put(md.getColumnName(i),rs.getObject(i));
                }
                list.add(row);
            }
            return list;
        };

    public JdbcSession jdbc() throws SQLException{
        return new JdbcSession(source());
    }

    public JdbcSession sql(String stmt) throws SQLException {
        return jdbc().sql(stmt).prepare( s -> s.setQueryTimeout(10000));
    }

    DataSource ds;

    private DataSource source() {
        if(ds!=null)return ds;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mariadb://localhost:3306/" + dbName + "?initialTimeout=60000&connectTimeout=60000&socketTimeout=60000");
        config.setUsername("root");
        config.setPassword("root");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("dataSourceClassName","org.mariadb.jdbc.Driver");

        HikariDataSource hds = new HikariDataSource(config);

        hds.setMinimumIdle(1);
        if(Minimo.maxPoolSize >0)hds.setMaximumPoolSize(Minimo.maxPoolSize);

        ds=hds;
        return ds;
    }

    public void delete(String collectionName,String name) {
        try {
            sql("delete from " + collectionName + " where name=?")
                    .set(name)
                    .update(Outcome.VOID);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    

    public void insertOrUpdate(String collectionName, String name, byte[] o) {
        if(names(collectionName).stream().anyMatch(n->n.equals(name))){
            update(collectionName,name,o);
        } else {
            insert(collectionName,name,o);
        }
    }

    private void update(String collectionName, String name, byte[] blob) {
        
        try {
            sql("update " + collectionName + " set data=? where name=?")
                    .set(blob)
                    .set(name)
                    .update(Outcome.VOID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
