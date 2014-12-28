package com.minimocms.web;

import com.minimocms.Minimo;
import com.minimocms.type.GenericContent;
import com.minimocms.type.MoList;
import com.minimocms.type.MoPage;
import com.minimocms.type.MoUser;
import com.minimocms.utils.JsonUtil;
import com.minimocms.utils.PasswordHash;
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

        get("/minimo/page/:name", (req, resp) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("pages",pages());
            model.put("page",page(req.params("name")));

            return new ModelAndView(model, "/minimo/assets/vms/page.vm");
        }, Velocity.engine);

        post("/minimo/page/:name", (req, resp) -> {
            Minimo.save(req.params("name"),req);
//            JsonUtil.println(req.queryParams());
            resp.redirect("/minimo/page/" + req.params("name"));
            return "";
        });

        post("/minimo/page/:name/addListElement", (req,resp) -> {
            MoPage page = page(req.params("name"));
            MoList ls = (MoList)page.find(req.queryParams("listid"));
            String path = page.findPath(req.queryParams("listid"));

            System.out.println("adding list element"+req.queryParams("listid"));

            Map<String, Object> model = new HashMap<>();
            model.put("pages",pages());
            model.put("id",ls.id());
            model.put("path",path);
            GenericContent c = ls.add();
            model.put("c", c);
            ls.removeChildById(c.id());
            return new ModelAndView(model,"/minimo/assets/vms/render/mo-list-element.vm");
        },Velocity.engine);

        before("/minimo/*", (req,resp) -> {
            if(req.session().attribute("user")==null) {
                if(Minimo.store().users().size()>0)
                    resp.redirect("/login");
                else
                    resp.redirect("/create-user");
            }
        });

        get("/create-user", (req,resp)->{
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model,"/minimo/assets/vms/create-user.vm");
        }, Velocity.engine);

        post("/create-user", (req,resp)->{
            if(Minimo.store().users().size()>0){
                resp.redirect("/login");
            } else{
                MoUser user = new MoUser(req.queryParams("username"), PasswordHash.createHash(req.queryParams("password")));
                Minimo.store().saveUser(user);
                req.session().attribute("user",user);
                resp.redirect("/minimo");
            }
            return "";
        });
        before("/minimo", (req, resp) -> {
            resp.redirect("/minimo/dash");
        });

        get("/minimo/dash", (req, resp) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("pages",pages());

            return new ModelAndView(model, "/minimo/assets/vms/index.vm");
        }, Velocity.engine);




        get("/login", (req,resp) -> {
            Map<String, Object> model = new HashMap<>();

            return new ModelAndView(model, "/minimo/assets/vms/login.vm");
        }, Velocity.engine);
        
        post("/login", (req, resp) -> {
            String username = req.queryParams("username");
            String pass = req.queryParams("password");

            MoUser user = Minimo.store().user(username);

            if (user!=null&&PasswordHash.validatePassword(pass, user.getPassHash())) {
                req.session().attribute("user", user);
                System.out.println("validated");
                resp.redirect("/minimo");
            } else {
                System.out.println("invalid");
                System.out.println(JsonUtil.toJson(user));
                resp.redirect("/login");
            }
            return "";
        });


    }

}
