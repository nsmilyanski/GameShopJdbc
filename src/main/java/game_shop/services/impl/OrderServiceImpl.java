package game_shop.services.impl;

import game_shop.entities.Game;
import game_shop.entities.Order;
import game_shop.entities.User;
import game_shop.repositories.OrderRepository;
import game_shop.services.OrderService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void addOrder(User buyer, BigDecimal sum) {
        Order order = new Order();
        order.setBuyer(buyer);
        order.setSum(sum);
        orderRepository.create(order);
        System.out.println("You game is on the way");
    }

    @Override
    public void findAll() {
        Collection<Order> allOrders = orderRepository.findAll();

        allOrders.forEach(System.out::println);
    }
}
