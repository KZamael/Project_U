package app;

import java.util.HashMap;
import java.util.Map;

import app.business.util.HerokuUtil;
import app.business.util.Path;
import app.business.util.ViewUtil;
import app.business.util.Filters;

import app.web.application.PaymentDao;
import app.web.controller.IndexController;
import app.web.controller.LoginController;
import app.web.application.UserDao;

import app.web.controller.PaymentController;
import io.javalin.Javalin;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;

import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class MainApplication {

    private TemplateEngine templateEngine;
    private Map<String, IndexController> controllersByURL;

    public static UserDao userDao;
    public static PaymentDao paymentDao;

    public MainApplication(final ServletContext servletContext)
    {
        super();

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);

        // HTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode(TemplateMode.HTML);
        // This will convert "home" to "/WEB-INF/templates/index/home.html"
    }

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
            get(Path.Web.INDEX, IndexController.serveIndexPage);
            get(Path.Web.PAYMENTS, PaymentController.fetchAllPayments);
            get(Path.Web.ONE_PAYMENT, PaymentController.fetchOnePayment);
            post(Path.Web.LOGIN, LoginController.handleLoginPost);
            post(Path.Web.LOGOUT, LoginController.handleLogoutPost);
        });

        //app.get("/", ctx -> ctx.render("WEB-INF/templates/login/login.html"));

        app.error(404, ViewUtil.notFound);
    }
}