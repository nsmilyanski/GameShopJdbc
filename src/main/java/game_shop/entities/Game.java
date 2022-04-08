package game_shop.entities;

import game_shop.repositories.Identifiable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Game implements Identifiable<Long>, Serializable {
    private long id;

    private String title;

    private BigDecimal price;

    private BigDecimal size;

    private String trailer;

    private String thumbnailUrl;


    private String description;


    private int year;

    private Category category;

    public Game() {

    }

    public Game(String title, BigDecimal price, BigDecimal size, String trailer, String thumbnailUrl, String description, int releaseDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.size = size;
        this.trailer = trailer;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.year = releaseDate;
    }

    public Game(long id, String title, BigDecimal price, BigDecimal size, String trailer, String thumbnailUrl, String description, int releaseDate) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.size = size;
        this.trailer = trailer;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.year = releaseDate;
    }

    @Override
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {

        return "Game{" +
                "title='" + title + '\'' +
                ", price=" + price +
                ", size=" + size +
                ", trailer='" + trailer + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + year +
                ", category=" + category +
                '}';

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && Objects.equals(title, game.title) && Objects.equals(price, game.price) && Objects.equals(size, game.size) && Objects.equals(trailer, game.trailer) && Objects.equals(thumbnailUrl, game.thumbnailUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, size, trailer, thumbnailUrl);
    }
}
