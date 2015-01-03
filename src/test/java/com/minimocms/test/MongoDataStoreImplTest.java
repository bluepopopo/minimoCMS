package com.minimocms.test;

import com.minimocms.data.mongodb.MongoDataStoreImpl;
import com.minimocms.test.web.TestData;
import com.minimocms.type.MoPage;
import com.minimocms.utils.JsonUtil;

public class MongoDataStoreImplTest {
    static MongoDataStoreImpl store = new MongoDataStoreImpl("test");
    public static void main(String args[]){

        TestData.create();

//        testFile();
        testPages();
    }

    private static void testPages() {
//        store.savePage(page("home"));
        for(MoPage p:store.pages().values()){
            System.out.println("page1:"+p);
        }

        System.out.println("page2:"+store.page("home"));
    }

    private static void testFile() {
        String id = store.saveFile(new byte[]{'a','s','d','f'});
        System.out.println("moid:"+id);

        for(String i : store.fileIds()){
            System.out.println("all ids:"+i);
        }

        byte[] b = store.file(id);
        System.out.println("bytes:"+ JsonUtil.toJson(b));

    }
}
