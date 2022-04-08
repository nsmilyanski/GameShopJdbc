package game_shop.entities;

public class Comment {
    private long id;
    private String description;
    private User user;
    private Game game;

    public Comment() {
    }

    public Comment(long id, String description, User user, Game game) {
        this.id = id;
        this.description = description;
        this.user = user;
        this.game = game;
    }

    public Comment(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
