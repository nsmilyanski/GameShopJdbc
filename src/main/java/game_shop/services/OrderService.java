package game_shop.services;

import game_shop.entities.Game;
import game_shop.entities.User;

import java.util.Set;

public interface OrderService {
    void addOrder(User buyer, Set<Game> items);
}
