package com.minimocms.web;

import com.minimocms.Minimo;
import com.minimocms.data.MoId;
import com.minimocms.type.GenericContent;
import com.minimocms.type.MoList;
import com.minimocms.type.MoPage;
import com.minimocms.type.MoUser;
import com.minimocms.utils.PasswordHash;
import com.minimocms.utils.Velocity;
import spark.ModelAndView;
import spark.servlet.SparkApplication;

import java.util.HashMap;
import java.util.Map;

import static com.minimocms.Minimo.*;
import static spark.Spark.*;

public class Routes implements SparkApplication {
    @Override
    public void init() {

        staticFileLocation("/assets"); // Static files

        get("/minimofile/:fileid",(req,resp)->{
            resp.raw().getOutputStream().write(file(new MoId(req.params("fileid"))));
            return "";
        });

        get("/mologout",(req,resp)->{
            req.session().removeAttribute("user");
            resp.redirect("/");
            return "";
        });
        get("/minimo/page/:name", (req, resp) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("pages",pages(req));
            model.put("page",page(req, req.params("name")));

            return new ModelAndView(model, "/assets/minimoassets/vms/page.vm");
        }, Velocity.engine);

        post("/minimo/page/:name", (req, resp) -> {
            Minimo.save(req.params("name"),req);
//            JsonUtil.println(req.queryParams());
            resp.redirect("/minimo/page/" + req.params("name"));
            return "";
        });

        post("/minimo/page/:name/addListElement", (req,resp) -> {
            MoPage page = page(req, req.params("name"));
            MoList ls = (MoList)page.find(req.queryParams("listid"));
            String path = page.findPath(req.queryParams("listid"));

            System.out.println("adding list element"+req.queryParams("listid"));

            Map<String, Object> model = new HashMap<>();
            model.put("pages",pages(req));
            model.put("id",ls.id());
            model.put("path",path);
            GenericContent c = ls.add();
            model.put("c", c);

            persistPages(req);

            return new ModelAndView(model,"/assets/minimoassets/vms/render/mo-list-element.vm");
        },Velocity.engine);

        before("/minimo/*", (req,resp) -> {
            if(req.session().attribute("user")==null) {
                if(users().size()>0)
                    resp.redirect("/mologin");
                else
                    resp.redirect("/mo-create-user");
                halt();
            }
        });

        before("/minimo", (req, resp) -> {
            resp.redirect("/minimo/dash");
        });

        get("/mo-create-user", (req,resp)->{
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model,"/assets/minimoassets/vms/create-user.vm");
        }, Velocity.engine);

        post("/mo-create-user", (req,resp)->{
            if(users().size()>0){
                resp.redirect("/mologin");
            } else{
                MoUser user = new MoUser(req.queryParams("username"), PasswordHash.createHash(req.queryParams("password")));
                persistUser(user);
                req.session().attribute("user",user);
                resp.redirect("/minimo");
            }
            return "";
        });

        get("/minimo/dash", (req, resp) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("pages",pages(req));

            return new ModelAndView(model, "/assets/minimoassets/vms/index.vm");
        }, Velocity.engine);




        get("/mologin", (req,resp) -> {
            Map<String, Object> model = new HashMap<>();

            return new ModelAndView(model, "/assets/minimoassets/vms/login.vm");
        }, Velocity.engine);
        
        post("/mologin", (req, resp) -> {
            String username = req.queryParams("username");
            String pass = req.queryParams("password");

            MoUser user = user(username);

            if (user!=null&&PasswordHash.validatePassword(pass, user.getPassHash())) {
                req.session().attribute("user", user);
                resp.redirect("/minimo");
            } else {
                resp.redirect("/mologin");
            }
            return "";
        });


    }

}
