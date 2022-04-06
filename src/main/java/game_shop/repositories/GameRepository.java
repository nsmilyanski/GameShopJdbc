package game_shop.repositories;

import game_shop.entities.Game;

import java.math.BigDecimal;

public interface GameRepository extends BaseRepository<Long, Game> {
    Game findByGameTitle(String gameTitle);

    void editGame(Long gameID, BigDecimal price, BigDecimal size);
}
