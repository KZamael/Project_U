package app.web.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import app.business.entities.Payment;
import com.google.common.collect.ImmutableList;

public class PaymentDao {

    private final List<Payment> payments = ImmutableList.of(
            new Payment(1,"payment#1", new BigDecimal(12.50)),
            new Payment(2,"payment#2", new BigDecimal(56.52)),
            new Payment(3,"payment#3", new BigDecimal(1556.02))
    );

    public Iterable<Payment> getAllPayments() {
        return payments;
    }

    public Payment getPayment(int i){
        return payments.get(i);
    }

    public Payment getPaymentById(String name) {
        return payments.stream().filter(
                b -> b.getName().equals(name)).findFirst().orElse(null);
    }
}
