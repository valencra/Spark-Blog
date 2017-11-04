package com.teamtreehouse.blog;

import com.teamtreehouse.blog.dao.BlogDAO;
import com.teamtreehouse.blog.dao.BlogDAOImpl;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class Blog {
    public static void main(String[] args) {
        staticFileLocation("/public");
        BlogDAO blogDAO = new BlogDAOImpl();

        // home page
        get("/", (req, res) -> {
            return new ModelAndView(null, "index.hbs");
        }, new HandlebarsTemplateEngine());
    }
}
