package game_shop.services;

import game_shop.entities.Game;
import game_shop.exceptions.NonExistingEntityException;

import java.math.BigDecimal;

public interface GameService {

    void addGame(Game game);

    void editGame(Long gameID, BigDecimal price, BigDecimal size);

    void deleteGame(long id) throws NonExistingEntityException;

    void findAllGames();

    void findGameByTitle(String title);

    void getOwnedGamesFromLoggedInUser();
}
