package game_shop.entities;

import game_shop.repositories.Identifiable;

import java.math.BigDecimal;
import java.util.Set;

public class Order implements Identifiable<Long> {
    private static long orderId = 0;
    private long id;


    private User buyer;

    private BigDecimal sum;

    public Order() {
        this.id = orderId++;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public static long getOrderId() {
        return orderId;
    }

    public static void setOrderId(long orderId) {
        Order.orderId = orderId;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
