package com.minimocms.test.web;

import com.minimocms.type.MoDoc;
import com.minimocms.type.TextItem;

import static com.minimocms.Minimo.page;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public class TestStructure {
    public static void create(){
        page("home");
        page("about");

        page("home").document("mydoc1").build(doc->{
            doc.addItem(new TextItem("mytext1"),
                    txt -> txt.text("This is a test text"));
        });

        page("home").list("mylist1", MoDoc.class).buildTemplate(new MoDoc("list-template"), doc->{
            doc.addItem(new TextItem("mytext2"),
                    txt -> txt.text("This is a test text"));
        });
    }
}
