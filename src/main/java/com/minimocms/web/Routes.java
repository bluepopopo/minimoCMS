package com.minimocms.web;

import com.minimocms.Minimo;
import com.minimocms.utils.Velocity;
import spark.ModelAndView;
import spark.servlet.SparkApplication;

import java.util.HashMap;
import java.util.Map;

import static com.minimocms.Minimo.page;
import static com.minimocms.Minimo.pages;
import static spark.Spark.*;

/**
 * Created by MattUpstairs on 26/12/2014.
 */
public class Routes implements SparkApplication {
    @Override
    public void init() {

        staticFileLocation("/minimo"); // Static files

        before("/minimo/*", (req,resp) -> {
            if(req.session().attribute("username")==null)
                resp.redirect("/login");
        });

        before("/minimo", (req, resp) -> {
            resp.redirect("/minimo/dash");
        });

        get("/minimo/dash", (req, resp) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("pages",pages());

            return new ModelAndView(model, "/minimo/assets/vms/index.vm");
        }, Velocity.engine);


        get("/minimo/page/:name", (req, resp) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("pages",pages());
            model.put("page",page(req.params("name")));

            return new ModelAndView(model, "/minimo/assets/vms/page.vm");
        }, Velocity.engine);
        post("/minimo/page/:name", (req, resp) -> {
            Minimo.save(req.params("name"),req);
            resp.redirect("/minimo/page/" + req.params("name"));
            return "";
        });


        get("/login", (req,resp) -> {
            Map<String, Object> model = new HashMap<>();

            return new ModelAndView(model, "/minimo/assets/vms/login.vm");
        }, Velocity.engine);
        
        post("/login", (req, resp) -> {
            String user = req.queryParams("username");
            String pass = req.queryParams("password");
            if (user.equals("root") && pass.equals("welcome1")) {
                req.session().attribute("username", user);
                resp.redirect("/minimo");
            } else {
                resp.redirect("/login");
            }
            return "";
        });


    }

}
