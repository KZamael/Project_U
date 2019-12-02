package app;

import app.business.util.HerokuUtil;
import app.business.util.Path;
import app.business.util.ViewUtil;
import app.business.util.Filters;

import app.web.application.PaymentDao;
import app.web.controller.HomeController;
import app.web.controller.LoginController;
import app.web.application.UserDao;

import app.web.controller.PaymentController;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class MainApplication {

    public static UserDao userDao;
    public static PaymentDao paymentDao;

    public static void main(String[] args) {

        // Initialize the context
        userDao = new UserDao();
        paymentDao = new PaymentDao();


        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/static/images");
            config.addStaticFiles("/static/css");
        }).start(HerokuUtil.getAssignedPort());

        //app.get("/", ctx -> ctx.render("src/main/webapp/WEB-INF/templates/login.html"));
        app.routes(() -> {
            before(Filters.handleLocaleChange);
            before(LoginController.ensureLoginBeforeViewingPayments);
            get("/", HomeController.serveIndexPage);
            //get(Path.Web.INDEX, HomeController.serveIndexPage);
            get(Path.Web.PAYMENTS, PaymentController.fetchAllPayments);
            get(Path.Web.ONE_PAYMENT, PaymentController.fetchOnePayment);
            post(Path.Web.LOGIN, LoginController.handleLoginPost);
            post(Path.Web.LOGOUT, LoginController.handleLogoutPost);
        });

        //app.get("/", ctx -> ctx.render("WEB-INF/templates/login/login.html"));

        app.error(404, ViewUtil.notFound);
    }
}