package game_shop.entities;

import java.util.Set;

public class Category {
    private static long categoryId = 0;

    private long id;

    private String name;


    private Set<Game> games;

    public Category() {
        this.id = categoryId++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
