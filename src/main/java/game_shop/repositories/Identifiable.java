package game_shop.repositories;

public interface Identifiable<K>{
    K getId();
    void setId(K id);
}
