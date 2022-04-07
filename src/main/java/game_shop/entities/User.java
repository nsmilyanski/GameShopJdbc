package game_shop.entities;


import game_shop.repositories.Identifiable;

import java.util.HashSet;
import java.util.Set;

public class User implements Identifiable<Long> {
    private static long userId = 0;

    private long id;


    private String email;


    private String password;


    private String fullName;


    private Boolean isAdmin;


    private Set<Game> games;


    private ShoppingCard shoppingCard;

    public User() {
       this.id = ++userId;
       isAdmin = false;
    }

    public User(long id, String email, String fullName) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
    }

    public User(long id , String email, String password, String fullName, Boolean isAdmin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.isAdmin = isAdmin;
    }

    public User(String email, String password, String fullName) {
        this.id = ++userId;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.isAdmin = false;
        games = new HashSet<>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public ShoppingCard getShoppingCard() {
        return shoppingCard;
    }

    public void setShoppingCard(ShoppingCard shoppingCard) {
        this.shoppingCard = shoppingCard;
    }


}
