package app.web.controller;

import app.business.model.Paymentgroup;
import app.business.model.Payment;
import app.web.application.PaymentgroupRepository;
import app.web.application.UserRepository;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PaymentgroupController extends AbstractController {

    private PaymentgroupRepository repo;
    private UserRepository userRepos;

    public PaymentgroupController(PaymentgroupRepository repo, UserRepository userRepos){
        this.repo = repo;
        this.userRepos = userRepos;
    }

    public void handleGetGroups(Context ctx){
        try{
            ctx.render(
                    "/groups.html",
                    Map.of(
                            "payGroups",
                            repo.getPaygroupsForUsername(ctx.sessionAttribute("currentuser"))));
        } catch(Throwable throwable) {
            handleError(ctx, throwable);
        }
    }

    /**
     * lookup of specific paygroup (dynamic path param), calculating the actual balance of the current user
     * and rendering the detail view in balance.html thymeleaf template
     *
     * @param ctx the Javalin Context instance
     */
    public void handleGetPaygroup(Context ctx) {
        try {
            Paymentgroup pg = repo.getPaygroupForTitle(ctx.pathParam("title"));
            if(!pg.getUsers().contains(ctx.sessionAttribute("currentuser"))) {
                ctx.status(401).result("not found");
            } else {
                Float balance = calculateBalanceForUser(pg, ctx.sessionAttribute("currentuser"));
                ctx.render(
                  "/balance.html",
                        Map.of(
                                "payments",
                                pg.getTransactions(),
                                "balance",
                                balance,
                                "groupName",
                                pg.getTitle()

                        )
                );
            }
        } catch (Throwable throwable){
            handleError(ctx, throwable);
        }
    }

    /**
     * This renders a form from thymeleaf template add_group.html.
     * for creating a new paygroup with a populated list of all known users in the database to
     * add them as members.
     *
     * @param ctx the Javalin Context instance.
     */
    public void handleShowAddPaygroup(Context ctx){
        try {
            ctx.render("/add_group.html", Map.of("users", userRepos.findAll()));
        } catch(Throwable throwable) {
            handleError(ctx, throwable);
        }
    }

    /**
     * consumes a form post from the 'add paygroup' form rendered in handleAddPaygroup
     * adds the currently logged in user as a member and saves it via {@link PaymentgroupRepository}
     * to database. After that a redirect to the application root ('/') is returned where
     * the new paygroup is shown as a new list entry.
     *
     * @param ctx the Javalin Context instance
     */
    public void handleAddPaygroup(Context ctx) {
        if(ctx.formParam("title") == null ||
                ctx.formParam("title").isEmpty() ||
                ctx.formParam("members").isEmpty()) {
            ctx.render("/");
        } else {
            List<String> members= new ArrayList<>(ctx.formParams("currentuser"));
            Paymentgroup pg = new Paymentgroup();
            pg.setTitle(ctx.formParam("title"));
            if(!members.contains(ctx.sessionAttribute("currentuser"))){
                members.add(ctx.sessionAttribute("currentuser"));
            }
            pg.setUsers(members);
            try {
                repo.create(pg);
                ctx.redirect("/");
            } catch(Throwable throwable) {
                throwable.printStackTrace();
                handleError(ctx, throwable);
            }
        }
    }

    /**
     * render a form from thymeleaf template add_money.html
     * for adding a payment to a paygroup.
     *
     * @param ctx the Javalin Context instance
     */
    public void handleGetMoney(Context ctx) {
        ctx.render("/add_money.html", Map.of("groupname", ctx.pathParam("title")));
    }

    /**
     * consumes a form post from the 'add payment' form rendered in handleGetMoney.
     * It creates a new {@link Payment} instance, populates it with the appropriate information
     * looks up the paygroup in the database, adds the new payment to it and updates the paygroup document in database
     *
     * @param ctx the Javalin Context instance
     */
    public void handleAddMoney(Context ctx) {
        if(ctx.formParam("amount") == null
            || ctx.formParam("amount").isEmpty()) {
            handleGetMoney(ctx);
        } else {
            Payment p = new Payment();
            p.setUser(ctx.sessionAttribute("currentuser"));
            p.setDate(new Date());
            p.setAmount(BigDecimal.valueOf(Long.parseLong(ctx.formParam("amount"))));
            try {
                Paymentgroup pg = repo.getPaygroupForTitle(ctx.pathParam("title"));
                pg.getTransactions().add(p);
                repo.save("title", pg.getTitle(), pg);
                ctx.redirect("/balance/" + pg.getTitle());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                handleError(ctx, throwable);
            }
        }
    }

    /**
     * helper function that calculates the current balance for a given user and paygroup
     *
     * @param pg       paygroup where the payments are saved
     * @param username the user whose balance should be calculated
     * @return the current balance as Float value
     */
    private Float calculateBalanceForUser(Paymentgroup pg, String username) {
        float userSum = 0f;
        float sum = 0f;
        for(Payment it : pg.getTransactions()) {
            if(it.getUser().equals(username)) {
                userSum += it.getAmount().floatValue();
            }
            sum += it.getAmount().floatValue();
        }
        float avg = sum / Integer.valueOf(pg.getUsers().size()).floatValue();
        return avg - userSum;
    }


    /*public static Handler fetchAllPayments = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        model.put("payments", paymentDao.getAllPayments());
        ctx.render(Path.Template.PAYMENTS_ALL, model);
    };

    public static Handler fetchOnePayment = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        model.put("payment", paymentDao.getPaymentById(getParamId(ctx)));
        ctx.render(Path.Template.PAYMENTS_ONE, model);
    };*/
}
