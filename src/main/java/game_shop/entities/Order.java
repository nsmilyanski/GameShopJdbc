package game_shop.entities;

import game_shop.repositories.Identifiable;

import java.util.Set;

public class Order implements Identifiable<Long> {
    private static long orderId = 0;
    private long id;


    private User buyer;

    private Set<Game> games;

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

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
