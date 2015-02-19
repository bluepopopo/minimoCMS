package com.minimocms.utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import spark.ModelAndView;
import spark.TemplateEngine;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

public class MyVelocityEngine extends TemplateEngine {

    private final VelocityEngine velocityEngine;

    /**
     * Constructor
     */
    public MyVelocityEngine() {
        Properties properties = new Properties();
//        properties.setProperty("resource.loader", "class");
//        properties.setProperty(
//                "class.resource.loader.class",
//                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");


        if(System.getProperty("minimo.assetPath")!=null){
            properties.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
            properties.setProperty("file.resource.loader.path", System.getProperty("minimo.assetPath"));
            properties.setProperty("file.resource.loader.class", org.apache.velocity.runtime.resource.loader.FileResourceLoader.class.getName());
            properties.setProperty("file.resource.loader.cache", "false");

        } else {

            properties.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            properties.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        }

        properties.setProperty("eventhandler.include.class", "org.apache.velocity.app.event.implement.IncludeRelativePath");
        velocityEngine = new org.apache.velocity.app.VelocityEngine(properties);
    }

    /**
     * Constructor
     *
     * @param velocityEngine The velocity engine, must not be null.
     */
    public MyVelocityEngine(VelocityEngine velocityEngine) {
        if (velocityEngine == null) {
            throw new IllegalArgumentException("velocityEngine must not be null");
        }
        this.velocityEngine = velocityEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String render(ModelAndView modelAndView) {
        Template template = velocityEngine.getTemplate(modelAndView.getViewName());
        Object model = modelAndView.getModel();
        if (model instanceof Map) {
            Map<?, ?> modelMap = (Map<?, ?>) model;
            VelocityContext context = new VelocityContext(modelMap);
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } else {
            throw new IllegalArgumentException("modelAndView must be of type java.util.Map");
        }
    }

}