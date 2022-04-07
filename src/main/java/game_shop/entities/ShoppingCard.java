package game_shop.entities;

import java.util.HashSet;
import java.util.Set;

public class ShoppingCard {
    private long id;

    private String number;

    private User user;


    private Set<Game> items;

    public ShoppingCard() {
    }

    public ShoppingCard(String number) {
        this.number = number;
        this.items = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Set<Game> getItems() {
        return items;
    }

    public void addItems(Game item) {
        this.items.add(item);
    }

    public void setItems(Set<Game> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void removeItemFromShoppingCard(Game game){
        if (items.contains(game)){
            System.out.println("You removed successfully" + game.getTitle());
        }else {
            System.out.println("Game does not exist in your shopping card");
        }
     items.remove(game);
    }
}
