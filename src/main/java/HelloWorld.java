import io.javalin.Javalin;

public class HelloWorld {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.config.addStaticFiles("/static");


        app.get("/", ctx -> ctx.render("login.html"))
        ;
    }
}