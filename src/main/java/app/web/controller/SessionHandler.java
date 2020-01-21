package app.web.controller;

import app.business.model.User;
import app.web.application.UserRepository;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.Role;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.codec.digest.DigestUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of javalins {@link AccessManager} interface that
 * <ul>
 *     <li>controls access to ressources by evaluating every incoming requests</li>
 *     <li>handles login form posts in order to grant or deny access and initiating a authenticated user session</li>
 * </ul>
 *
 * @see <a href="https://javalin.io/documentation#access-manager">Javalin docs section on AccessManager</a>
 */
public class SessionHandler implements AccessManager {

    private UserRepository userRepo;

    private static final Logger LOG = LoggerFactory.getLogger(SessionHandler.class);

    /**
     * Constructor. Takes an {@link UserRepository} instance in order to lookup users from database for login handling
     *
     * @param userRepo the {@link UserRepository} to use for authentication
     */
    public SessionHandler(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * overrides the manage method defined in {@link AccessManager}. Javalin calls this method before every endpoint handler
     * invocation in order to hand over control of access decision. This method checks every request (except the ones for the login form)
     * by checking the sessionAttribute map for an existing entry with the key 'currentuser'. If it exists, the endpoint handler
     * is invoked. If not, a redirect to the login route is sent back and the handler will not be invoked (access denied)
     *
     * @param handler a handler that javalin hands over. the 'handle' method of that handler is called if access is granted in order to proceed with the normal routing flow
     * @param context the Context instance used for evaluation of session data
     * @param set     a roleset (application roles for role based access). Currently not used here
     * @throws Exception if a severe error occurs
     */
    @Override
    public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<Role> set) throws Exception {
        if (context.path().equals("/login")) {
            handler.handle(context);
        } else {
            String currentUser = context.sessionAttribute("currentuser");
            if (currentUser == null) {
               handleLogout(context);
            } else {
                handler.handle(context);
            }
        }
    }

    /**
     * consumes a login form post. it searches for the user with the given username via {@link UserRepository}
     * and checks the password for equality. if all checks are passed a redirect to the app root (/) is sent.
     * If any check fails, a login redirect or a '401 not authorized' is sent back.
     *
     * @param context used for adding the current user to sessionAttributes
     */
    public void handleLogin(@NotNull Context context) {
        if (context.formParam("username") == null
                || Objects.requireNonNull(context.formParam("username")).isEmpty()
                || context.formParam("password") == null
                || Objects.requireNonNull(context.formParam("password")).isEmpty()) {
            context.render("/login.html");
        } else {
            try {
                User user = userRepo.getUser(context.formParam("username"));
                if (user != null && user.getPassword().equals(hashFunction(context.formParam("password")))) {  // context.formParam("password")
                    context.sessionAttribute("currentuser", user.getUsername());
                    context.redirect("/");
                } else {
                    context.status(401).html("Access Denied");
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
                context.status(501).html("Internal Error "+ex.getMessage());
            }
        }
    }

    public void handleLogout(@NotNull Context context) throws IOException {

        context.sessionAttribute("currentuser", null);
        context.sessionAttribute("loggedout", "true");
        context.status(401).redirect("/login");
    }

    private String hashFunction(String originalPassword) {
        //String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(originalPassword);
        //System.out.println("HASH IS: " + sha256hex);
        return DigestUtils.sha256Hex(originalPassword);
    }
}