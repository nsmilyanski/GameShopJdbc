package game_shop.services;

import game_shop.entities.Game;
import game_shop.entities.User;

import java.math.BigDecimal;
import java.util.Set;

public interface OrderService {
    void addOrder(User buyer, BigDecimal sum);

    void findAll();

}
