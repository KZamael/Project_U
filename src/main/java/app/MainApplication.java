package app;

import java.util.Map;

import app.business.util.AppBuilder;
import app.business.util.AppFactory;

import io.javalin.Javalin;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;

import static io.javalin.apibuilder.ApiBuilder.before;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;


import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;


import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Main Class des Project_U.
 */
public class MainApplication {

    /**
     * Main Methode, die als Einstiegspunkt dient.
     * @param args Arguments, die vom
     */
    public static void main(String[] args) {
        AppBuilder.create().withFactory(AppFactory.getInstance()).build();
    }

    /*private TemplateEngine templateEngine;
    private Map<String, IndexController> controllersByURL;

    public static UserRepository userDao;
    public static PaymentgroupRepository paymentDao;
    //Data Sources window

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
        userDao = new UserRepository();
        paymentDao = new PaymentgroupRepository();


        MongoDatabase db;


        //           MongoCredential cred = MongoCredential.createCredential(username,database,password.toCharArray());
        CodecRegistry pRegistry = fromRegistries(
                MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        /*MongoClient client = new MongoClient(
            List.of(new ServerAddress("localhost")),
                           cred,
            MongoClientOptions.builder().codecRegistry(pRegistry).build()
        );

        db = client.getDatabase("quitt");

        System.out.println(db.getName());*/

        /*Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/static/images");
            config.addStaticFiles("/static/css");
        }).start(HerokuUtil.getAssignedPort());

        //app.get("/", ctx -> ctx.render("src/main/webapp/WEB-INF/templates/login.html"));
        app.routes(() -> {
            before(Filters.handleLocaleChange);
            before(LoginController.ensureLoginBeforeViewingPayments);
            get(Path.Web.INDEX, IndexController.serveIndexPage);
            get(Path.Web.PAYMENTS, PaymentgroupController.fetchAllPayments);
            get(Path.Web.ONE_PAYMENT, PaymentgroupController.fetchOnePayment);
            post(Path.Web.LOGIN, LoginController.handleLoginPost);
            post(Path.Web.LOGOUT, LoginController.handleLogoutPost);
        });

        //app.get("/", ctx -> ctx.render("WEB-INF/templates/login/login.html"));

        app.error(404, ViewUtil.notFound);
    }*/
}