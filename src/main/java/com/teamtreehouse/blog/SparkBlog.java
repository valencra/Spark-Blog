package com.teamtreehouse.blog;

import com.sun.tools.internal.xjc.model.Model;
import com.teamtreehouse.blog.dao.BlogDAO;
import com.teamtreehouse.blog.dao.BlogDAOImpl;
import com.teamtreehouse.blog.model.Entry;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class SparkBlog {
    private static final String FLASH_MESSAGE_KEY = "flash_message";

    public static void main(String[] args) {
        // initializations
        staticFileLocation("/public");
        BlogDAO blogDAO = new BlogDAOImpl();
        generateInitialEntries(blogDAO);
        String adminPassword = "admin";

        // save password cookie as request attribute, if it exists
        before((req, res) -> {
            if (req.cookie("password") != null) {
                req.attribute("password", req.cookie("password"));
            }
        });

        before("/add", (req, res) -> {
            if (req.attribute("password") == null) {
                setFlashMessage(req, "Administrator privileges required. Please provide password.");
                res.redirect("/password");
            }
        });

        before("/edit", (req, res) -> {
            if (req.attribute("password") == null) {
                setFlashMessage(req, "Administrator privileges required. Please provide password.");
                res.redirect("/password");
            }
        });

        // blog home
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("flashMessage", captureFlashMessage(req));
            model.put("entries", blogDAO.findAllEntries());
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        // entry details
        get("/entries/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("entry", blogDAO.findEntryBySlug(req.params("slug")));
            return new ModelAndView(model, "detail.hbs");
        }, new HandlebarsTemplateEngine());

        // password
        get("/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("flashMessage", captureFlashMessage(req));
            return new ModelAndView(model, "password.hbs");
        }, new HandlebarsTemplateEngine());

        post("/password", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            String password = req.queryParams("password");
            // non-administrator password provided
            if (!password.equals(adminPassword)) {
                setFlashMessage(req, "Invalid password. Please provide password.");
                res.redirect("/password");
            }
            // administrator password provided
            else {
                // set password cookie only if admin password is provided
                res.cookie("password", password);
                setFlashMessage(req, "Administrator privileges granted.");
                res.redirect("/");
            }
            return null;
        });

        // add entry
        get("/add", (req, res) -> {
            return new ModelAndView(null, "new.hbs");
        }, new HandlebarsTemplateEngine());

        post("/entries", (req, res) -> {
            String title = req.queryParams("title");
            String body = req.queryParams("body");
            Entry newEntry = new Entry(title, body);
            blogDAO.addEntry(newEntry);
            res.redirect("/");
            return null;
        });

        // edit entry
        get("/edit/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("entry", blogDAO.findEntryBySlug(req.params("slug")));
            return new ModelAndView(model, "edit.hbs");
        }, new HandlebarsTemplateEngine());

        post("/entries/:slug", (req, res) -> {
            Entry entry = blogDAO.findEntryBySlug(req.params("slug"));
            entry.setTitle(req.queryParams("title"));
            entry.setBody(req.queryParams("body"));
            res.redirect("/");
            return null;
        });

        // delete entry
        post("/delete/entries/:slug", (req, res) -> {
            Entry entry = blogDAO.findEntryBySlug(req.params("slug"));
            blogDAO.deleteEntry(entry);
            res.redirect("/");
            return null;
        });
    }

    private static void setFlashMessage(Request req, String message) {
        req.session().attribute(FLASH_MESSAGE_KEY, message);
    }

    private static String getFlashMessage(Request req) {
        if (req.session(false) == null) {
            return null;
        }
        if (!req.session().attributes().contains(FLASH_MESSAGE_KEY)) {
            return null;
        }
        return (String) req.session().attribute(FLASH_MESSAGE_KEY);
    }

    private static String captureFlashMessage(Request req) {
        String message = getFlashMessage(req);
        if (message!=null) {
            req.session().removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;
    }

    public static BlogDAO generateInitialEntries(BlogDAO blogDAO) {
        // add three dummy initial entries
        // entry with 1 tag
        Entry entry1 = new Entry(
                "First Entry",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Dolor purus non enim praesent elementum facilisis. Erat velit scelerisque in dictum non consectetur. Ultrices neque ornare aenean euismod. Lectus arcu bibendum at varius vel pharetra vel. Eu volutpat odio facilisis mauris sit amet. Leo urna molestie at elementum eu facilisis sed odio morbi. Bibendum arcu vitae elementum curabitur vitae nunc. Ut tristique et egestas quis ipsum suspendisse ultrices gravida dictum. Viverra accumsan in nisl nisi scelerisque eu ultrices vitae. Turpis cursus in hac habitasse. In aliquam sem fringilla ut. Morbi tristique senectus et netus et malesuada fames ac.\n" +
                        "\n" +
                        "Amet luctus venenatis lectus magna fringilla urna porttitor rhoncus. Id aliquet risus feugiat in ante metus dictum. Bibendum neque egestas congue quisque egestas diam in arcu cursus. Nulla facilisi nullam vehicula ipsum a arcu cursus vitae. Turpis egestas pretium aenean pharetra magna ac placerat. Non quam lacus suspendisse faucibus interdum posuere. Sem integer vitae justo eget magna fermentum. Nec sagittis aliquam malesuada bibendum arcu vitae elementum curabitur. Varius duis at consectetur lorem donec. Platea dictumst quisque sagittis purus. Ut lectus arcu bibendum at varius vel pharetra. Ullamcorper dignissim cras tincidunt lobortis feugiat vivamus at. Nunc id cursus metus aliquam eleifend mi in nulla. Diam volutpat commodo sed egestas egestas fringilla phasellus faucibus. Rutrum quisque non tellus orci. Ipsum dolor sit amet consectetur adipiscing. Ullamcorper dignissim cras tincidunt lobortis. Amet nisl suscipit adipiscing bibendum est ultricies integer quis auctor. Dolor purus non enim praesent elementum facilisis leo vel fringilla."
        );
        entry1.addTag("Tag 4");
        blogDAO.addEntry(entry1);

        // entry with multiple tags
        Entry entry2 = new Entry(
                "Second Entry",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Hendrerit gravida rutrum quisque non tellus orci ac auctor augue. Vestibulum lectus mauris ultrices eros in. Accumsan sit amet nulla facilisi. Congue nisi vitae suscipit tellus mauris a. Proin nibh nisl condimentum id venenatis a condimentum vitae. Dui id ornare arcu odio ut sem nulla. Nisl purus in mollis nunc. Gravida in fermentum et sollicitudin ac orci phasellus. Sit amet nulla facilisi morbi tempus iaculis urna. Ullamcorper eget nulla facilisi etiam dignissim diam quis enim. Ac feugiat sed lectus vestibulum mattis ullamcorper. Aliquet eget sit amet tellus cras adipiscing enim eu. Nibh tortor id aliquet lectus proin nibh nisl.\n" +
                        "\n" +
                        "Proin nibh nisl condimentum id venenatis a condimentum vitae. Eu facilisis sed odio morbi quis commodo odio. Amet purus gravida quis blandit turpis. Cras fermentum odio eu feugiat pretium nibh ipsum consequat. Nulla malesuada pellentesque elit eget gravida cum sociis. Maecenas ultricies mi eget mauris pharetra et ultrices neque. Sed vulputate mi sit amet mauris commodo quis. Dignissim convallis aenean et tortor at risus viverra. Diam maecenas ultricies mi eget mauris pharetra. Pellentesque id nibh tortor id aliquet. Volutpat est velit egestas dui id ornare arcu odio. Et malesuada fames ac turpis egestas sed tempus urna.\n" +
                        "\n" +
                        "Id diam vel quam elementum pulvinar etiam non. Porta non pulvinar neque laoreet. Vitae turpis massa sed elementum tempus. Tempus quam pellentesque nec nam aliquam sem et tortor. Pellentesque nec nam aliquam sem et tortor consequat. Ullamcorper velit sed ullamcorper morbi tincidunt ornare massa eget egestas. Eu sem integer vitae justo. Nunc mattis enim ut tellus elementum. Quis viverra nibh cras pulvinar mattis nunc sed blandit. Condimentum mattis pellentesque id nibh."
        );
        entry2.addTag("Tag 3");
        entry2.addTag("Tag 2");
        entry2.addTag("Tag 1");
        blogDAO.addEntry(entry2);

        // entry with no tags
        Entry entry3 = new Entry(
                "Third Entry",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Scelerisque eu ultrices vitae auctor eu augue ut lectus arcu. Lectus magna fringilla urna porttitor rhoncus. Senectus et netus et malesuada fames ac. Egestas maecenas pharetra convallis posuere morbi leo. Tincidunt eget nullam non nisi est sit amet facilisis. Parturient montes nascetur ridiculus mus mauris. Scelerisque fermentum dui faucibus in ornare quam viverra orci sagittis. Nunc id cursus metus aliquam eleifend mi in nulla posuere. Velit dignissim sodales ut eu sem integer vitae. Scelerisque purus semper eget duis. Proin nibh nisl condimentum id venenatis a. Blandit turpis cursus in hac habitasse.\n" +
                        "\n" +
                        "Ac ut consequat semper viverra nam libero justo. Blandit aliquam etiam erat velit scelerisque in dictum non consectetur. Quam nulla porttitor massa id. Non enim praesent elementum facilisis leo vel fringilla est ullamcorper. Mauris rhoncus aenean vel elit scelerisque mauris. In fermentum et sollicitudin ac orci phasellus egestas tellus rutrum. Diam in arcu cursus euismod quis. Ridiculus mus mauris vitae ultricies leo integer malesuada. Diam quam nulla porttitor massa id neque aliquam vestibulum. In massa tempor nec feugiat nisl pretium fusce id velit. Nunc sed id semper risus in hendrerit gravida rutrum. Porttitor lacus luctus accumsan tortor posuere. Et pharetra pharetra massa massa ultricies mi quis hendrerit dolor. Enim tortor at auctor urna nunc id cursus. Metus aliquam eleifend mi in nulla. Massa vitae tortor condimentum lacinia quis vel. Scelerisque eu ultrices vitae auctor eu augue. Sollicitudin aliquam ultrices sagittis orci a scelerisque purus."
        );
        blogDAO.addEntry(entry3);

        return blogDAO;
    }
}
