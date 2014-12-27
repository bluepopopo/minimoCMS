package com.minimocms.test.web;

import static com.minimocms.Minimo.page;

/**
 * Created by MattUpstairs on 27/12/2014.
 */
public class TestData {
    public static void create(){
        page("home");
        page("about");


        page("home").list("mylist1").add("Item-1");
        page("home").list("mylist1").add("Item-2");
    }
}
