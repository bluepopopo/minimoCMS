package com.minimocms.data.mysql;

import com.jcabi.jdbc.*;
import com.minimocms.utils.JsonUtil;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.util.JSON;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;

/**
 * Created by Matt on 16/02/2015.
 */
public class MysqlStore {

    final static String SERVER="localhost";

    String dbName;

    public MysqlStore(String dbName){
        this.dbName=dbName;
    }

    public void insert(String collectionName, String name, String o) {

        try {
            jdbc().query(connection(),"insert into " + collectionName + " (name,data) values (?,?)", new MapListHandler(), name,o);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public Collection<String> names(String collectionName){

        try {
            jdbc().query(connection(),"select name from " + collectionName + "", new ResultSetHandler<List<String>>() {
                @Override
                public List<String> handle(ResultSet rs) throws SQLException {
                    List<String> names = new ArrayList<String>();
                    while(rs.next()){
                        names.add(rs.getString("name"));
                    }
                    return names;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return new ArrayList<>();
    }

    public String select(String collectionName,String name){


        try {
            jdbc().query(connection(),"select data from " + collectionName + " where name=?", new ResultSetHandler<String>() {
                @Override
                public String handle(ResultSet resultSet) throws SQLException {
                    resultSet.next();
                    return resultSet.getString("data");
                }
            }, name);

//            return
//                    jdbc().query(connection(),)
//                            .set(name)
//                            .select((r,s)->{
//                                String ret = new SingleOutcome<String>(String.class).handle(r,s);
//                                r.close();
//                                s.close();
//                                return ret;
//                            });
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return "";
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


    public static Outcome<List<Map<String,Object>>> RESULT_MAP = new Outcome<List<Map<String,Object>>>() {
        @Override
        public List<Map<String,Object>> handle(ResultSet rs, Statement statement) throws SQLException {
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
            rs.close();
            statement.close();
            return list;
        }
    };

    public QueryRunner jdbc() throws SQLException{
        return new QueryRunner();
    }



    Connection conn;
    private Connection connection() throws SQLException{

        try {
            conn = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private Connection getConnection() throws SQLException {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
//        connectionProps.put("initialSize", "10");
//        connectionProps.put("maxActive", "100");
//        connectionProps.put("maxIdle", "50");
//        connectionProps.put("minIdle", "10");
//        connectionProps.put("loginTimeout", "10");
//        connectionProps.put("socketTimeout", "200");

        conn = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/"+dbName,
                connectionProps);

        return conn;
    }

    public void delete(String collectionName,String name) {
        try {
            jdbc().query(connection(),"delete from "+collectionName+" where name=?", new MapListHandler(), name);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void close(){
        try {
            DbUtils.close(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertOrUpdate(String collectionName, String name, String o) {
        if(names(collectionName).stream().anyMatch(n->n.equals(name))){
            update(collectionName,name,o);
        } else {
            insert(collectionName,name,o);
        }
    }

    private void update(String collectionName, String name, String o) {
        try {
            jdbc().query(connection(),"update " + collectionName + " set data=? where name=?", new MapListHandler(), name,o);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}
