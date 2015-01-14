package com.minimocms.test.web;

import com.minimocms.type.MoCheckboxItem;
import com.minimocms.type.MoDoc;
import com.minimocms.type.MoTextAreaItem;
import com.minimocms.type.MoTextItem;

import static com.minimocms.Minimo.page;

public class TestData {
    public static void create(){
        page("home");
        page("about");

        page("home").url("/:page-name");

        page("home").document("mydoc1").build(doc->{

            doc.item(new MoCheckboxItem("chk"));

            doc.item(new MoTextItem("mytext1"),
                    txt -> txt.setValue("This is a test text"));
//            doc.item(new MoImageItem("myfile"), it->{
//                try {
//                    it.file(IOUtils.toByteArray(new TestData().getClass().getResourceAsStream("/assets/minimoassets/images/cross.png")));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
            doc.item(new MoTextAreaItem("mytext1"),
                    txt -> txt.setValue("This is a test text"));
        });

        page("home").list("mylist1").buildTemplate(new MoDoc("list-template"), doc->{
            doc.item(new MoTextItem("mytext2"),
                    txt -> txt.setValue("This is a test text"));
//            doc.item(new MoImageItem("myfile"), it->{
//
//                it.file(new ResourceUtil().getFileBytes("/assets/minimoassets/images/cross.png"));
//            });

            doc.item(new MoTextAreaItem("mytext1"),
                    txt -> txt.setValue("This is a test text"));
        });

        page("home").list("mylist1").add();
        page("home").list("mylist1").add();
    }
}
