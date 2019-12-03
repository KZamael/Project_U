package app.web.controller;

import java.util.Map;
import io.javalin.http.Handler;

import java.util.Map;

import app.business.util.Path;
import app.business.util.ViewUtil;

import app.business.util.Path;
import app.business.util.ViewUtil;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

import static app.MainApplication.*;

public class HomeController {
    public static Handler serveIndexPage = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        model.put("users", userDao.getAllUserNames());
        model.put("payments", paymentDao.getAllPayments());
        ctx.render(Path.Template.LOGIN, model);
    };
}
