package com.minimocms.web;

import com.minimocms.Minimo;
import com.minimocms.type.GenericContent;
import com.minimocms.type.MoList;
import com.minimocms.type.MoPage;
import com.minimocms.type.MoUser;
import com.minimocms.utils.*;
import org.apache.commons.lang.StringEscapeUtils;
import spark.ModelAndView;
import spark.servlet.SparkApplication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.minimocms.Minimo.*;
import static com.minimocms.utils.URLDecoder.decodeUTF8;
import static spark.Spark.*;

public class Routes implements SparkApplication {

    Map<String,MoUser> restSessions = Collections.synchronizedMap(new LRUMap<String, MoUser>(1000));

    @Override
    public void init() {

        staticFileLocation("/assets"); // Static files

        get("/morest/pages",(req,resp)->{
            resp.type("application/json");
            return pages(req);
        }, new JsonTransformer());

        get("/morest/page/:name",(req,resp)->{
            resp.type("application/json");
            return page(req,decodeUTF8(req.params("name")));
        }, new JsonTransformer());

        post("/morest/pages",(req,resp)->{
            resp.type("application/json");
            boolean success = Minimo.store().setPagesFromJson(req,req.queryParams("pages"));
            Map<String,String> ret = new HashMap<String,String>();
            if(success)ret.put("result","success");
            else ret.put("result","fail");
            return ret;
        }, new JsonTransformer());

        post("/morest/page/:name",(req,resp)->{
            resp.type("application/json");
            boolean success = Minimo.store().setPageFromJson(req,req.queryParams("page"));
            Map<String,String> ret = new HashMap<String,String>();
            if(success)ret.put("result","success");
            else ret.put("result","fail");
            return ret;
        }, new JsonTransformer());

        get("/morest/files",(req,resp)->{
            resp.type("application/json");
            return Minimo.files(req);
        }, new JsonTransformer());

        get("/morest/file/:fileid",(req,resp)->{
            resp.raw().getOutputStream().write(file(decodeUTF8(req.params("fileid"))));
            return "";
        }, new JsonTransformer());

        before("/morest/*",(req,resp)->{
            if(restSessions.get(req.queryParams("token"))==null){
                halt(401,"Unauthorized, you must login to get a valid token");
            }
        });

        get("/morestlogin",(req,resp)->{
            resp.type("application/json");
            String username = req.queryParams("username");
            String pass = req.queryParams("password");
            MoUser user = user(username);
            Map<String,String> ret = new HashMap<String,String>();

            if (user!=null&&PasswordHash.validatePassword(pass, user.getPassHash())) {
                restSessions.put(req.session().id(),user);
                ret.put("token",req.session().id());
                ret.put("result","success");
                return ret;
            } else {
                ret.put("result","fail");
                return ret;
            }
        }, new JsonTransformer());

        post("/morestlogin",(req,resp)->{
            resp.type("application/json");
            String username = req.queryParams("username");
            String pass = req.queryParams("password");
            MoUser user = user(username);
            Map<String,String> ret = new HashMap<String,String>();

            if (user!=null&&PasswordHash.validatePassword(pass, user.getPassHash())) {
                restSessions.put(req.session().id(),user);
                ret.put("token",req.session().id());
                ret.put("authentication","success");
                return ret;
            } else {
                ret.put("authentication","fail");
                return ret;
            }
        }, new JsonTransformer());


        get("/minimo/get-json",(req,resp)->{
            resp.type("application/json");
            return JsonUtil.toJson(pages(req));
        });

        get("/minimo/upload-json",(req,resp)->{
            Map<String, Object> model = new HashMap<>();
            model.put("upload",req.queryParams("upload"));
            model.put("pages",pages(req));
            return new ModelAndView(model, "/assets/minimoassets/vms/upload-json.vm");
        }, Velocity.engine);

        post("/minimo/upload-json",(req,resp)->{
            boolean success = Minimo.store().setPagesFromJson(req,req.queryParams("json"));
            if(success){
                resp.redirect("/minimo/upload-json?upload=success");
            } else {
                resp.redirect("/minimo/upload-json?upload=fail");
            }
            return "";
        });

        get("/minimofile/:fileid",(req,resp)->{
            resp.raw().getOutputStream().write(file(decodeUTF8(req.params("fileid"))));
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
            model.put("page",page(req, decodeUTF8(req.params("name"))));
            model.put("success",StringEscapeUtils.escapeHtml(req.queryParams("success")));
            model.put("fail",StringEscapeUtils.escapeHtml(req.queryParams("error")));

            return new ModelAndView(model, "/assets/minimoassets/vms/page.vm");
        }, Velocity.engine);

        get("/minimo/delete/:name", (req, resp) -> {
            Minimo.store().deletePage(decodeUTF8(req.params("name")));
            Minimo.rollbackPages();
            resp.redirect("/minimo");
            halt();
            return "";
        });

        post("/minimo/page/:name", (req, resp) -> {
            boolean success = Minimo.save(decodeUTF8(req.params("name")),req);
//            JsonUtil.println(req.queryParams());
            if(success)
                resp.redirect("/minimo/page/" + decodeUTF8(req.params("name")+"?success=Your page was saved successfully"));
            else
                resp.redirect("/minimo/page/" + decodeUTF8(req.params("name")+"?error=Your page could not be saved"));
            halt();
            return "";
        });

        post("/minimo/page/:name/addListElement", (req,resp) -> {
            MoPage page = page(req, decodeUTF8(decodeUTF8(req.params("name"))));
            MoList ls = (MoList)page.find(req.queryParams("listid"));
            String path = page.findPath(req.queryParams("listid"));

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
            resp.redirect("/minimo/page/"+pages().iterator().next().name());
            halt();
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
        
        get("/minimo/copy/:frompage",(req,resp)->{
            String frompage = decodeUTF8(req.params("frompage"));
            String topage = decodeUTF8(req.queryParams("topage"));

            if(Minimo.store().existsPage(topage)){
                resp.redirect("/minimo/page/"+frompage+"?error=Unable to copy: page already exists");
                halt();
            } else {
                MoPage page = page(frompage).copy();
                page.name(topage);

                store().persistPage(page);
                Minimo.rollbackPages();

                resp.redirect("/minimo/page/"+topage+"?success=Copied page");
                halt();
            }
            return "";
        });

//        get("/minimo/dash", (req, resp) -> {
//            Map<String, Object> model = new HashMap<>();
//
//            model.put("pages",pages(req));
//
//            return new ModelAndView(model, "/assets/minimoassets/vms/index.vm");
//        }, Velocity.engine);




        get("/mologin", (req,resp) -> {
            Map<String, Object> model = new HashMap<>();
            if(req.queryParams("message")!=null)
                model.put("message",StringEscapeUtils.escapeHtml(req.queryParams("message")));
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
