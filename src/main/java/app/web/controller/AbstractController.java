package app.web.controller;

import io.javalin.http.Context;

/**
 * base class for implementations of controllers. In this project Controller objects handle
 * a defined set of actions related to routes (configured in)
 * by coordinating the information given by a {@link Context} instance and using subclasses of
 * for databse related actions. Subclasses are requied to add methods on top of common methods implemented here.
 */
public abstract class AbstractController {

    /**
     * common error handler method that handle serverside errors (Exceptions)
     *
     * @param ctx the Context instance used to render an error message
     * @param ex  a subclass of Throwable (checked and unchecked Exceptions)
     */
    protected void handleError(Context ctx, Throwable ex) {
        String res = "error";
        if (ex != null) {
            res = ex.getMessage();
        }
        ctx.status(501).html(res);
    }
}
