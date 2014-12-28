package com.minimocms.test.web;

import com.minimocms.type.MoDoc;
import com.minimocms.type.MoFileItem;
import com.minimocms.type.MoTextAreaItem;
import com.minimocms.type.MoTextItem;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

import static com.minimocms.Minimo.page;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public class TestData {
    public static void create(){
        page("home");
        page("about");

        page("home").document("mydoc1").build(doc->{
            doc.addItem(new MoTextItem("mytext1"),
                    txt -> txt.text("This is a test text"));
            doc.addItem(new MoFileItem("myfile"), it->{
                try {
                    it.file(IOUtils.toByteArray(new TestData().getClass().getResourceAsStream("/assets/minimoassets/images/cross.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            doc.addItem(new MoTextAreaItem("mytext1"),
                    txt -> txt.text("This is a test text"));
        });

        page("home").list("mylist1", MoDoc.class).buildTemplate(new MoDoc("list-template"), doc->{
            doc.addItem(new MoTextItem("mytext2"),
                    txt -> txt.text("This is a test text"));
            doc.addItem(new MoFileItem("myfile"), it->{

                try {
                    it.file(IOUtils.toByteArray(new TestData().getClass().getResourceAsStream("/assets/minimoassets/images/cross.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            doc.addItem(new MoTextAreaItem("mytext1"),
                    txt -> txt.text("This is a test text"));
        });

        page("home").list("mylist1").add();
        page("home").list("mylist1").add();
    }
}
