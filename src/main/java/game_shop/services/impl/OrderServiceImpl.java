package game_shop.services.impl;

import game_shop.entities.Game;
import game_shop.entities.Order;
import game_shop.entities.User;
import game_shop.repositories.OrderRepository;
import game_shop.services.OrderService;

import java.util.Set;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void addOrder(User buyer, Set<Game> items) {
        Order order = new Order();
        order.setBuyer(buyer);
        order.setGames(items);
        orderRepository.create(order);
        System.out.println("You game is on the way");
    }
}
