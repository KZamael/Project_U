package app.web.controller;

import io.javalin.http.Handler;
import java.util.Map;

import app.business.util.Path;
import app.business.util.ViewUtil;

import static app.MainApplication.*;
import static app.business.util.RequestUtil.*;

public class PaymentController {
    public static Handler fetchAllPayments = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        model.put("payments", paymentDao.getAllPayments());
        ctx.render(Path.Template.PAYMENTS_ALL, model);
    };

    public static Handler fetchOnePayment = ctx -> {
        Map<String, Object> model = ViewUtil.baseModel(ctx);
        model.put("payment", paymentDao.getPaymentById(getParamId(ctx)));
        ctx.render(Path.Template.PAYMENTS_ONE, model);
    };
}
