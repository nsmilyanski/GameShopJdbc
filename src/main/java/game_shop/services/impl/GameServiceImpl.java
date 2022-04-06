package game_shop.services.impl;

import game_shop.entities.Game;
import game_shop.exceptions.NonExistingEntityException;
import game_shop.repositories.GameRepository;
import game_shop.services.GameService;
import game_shop.services.UserService;

import java.math.BigDecimal;
import java.util.Collection;

public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final UserService userService;



    public GameServiceImpl(GameRepository gameRepository, UserService userService){
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    @Override
    public void loadData() {
    }



    @Override
    public void addGame(Game game) {
        if (userService.getLogInUser() == null){
            System.out.println("First you have to log in!");
            return;
        }else if (!userService.getLogInUser().getAdmin()){
            System.out.println("Just admin can add games to Database");
            return;
        }

        gameRepository.create(game);
    }

    @Override
    public void editGame(Long gameID, BigDecimal price, BigDecimal size) {
        if (userService.getLogInUser() == null){
            System.out.println("First you have to log in!");
            return;
        }else if (!userService.getLogInUser().getAdmin()){
            System.out.println("Just admin can add games to Database");
            return;
        }

        gameRepository.editGame(gameID, price, size);

    }

    @Override
    public void deleteGame(long id) throws NonExistingEntityException {
        if (userService.getLogInUser() == null){
            System.out.println("First you have to log in!");
            return;
        }else if (!userService.getLogInUser().getAdmin()){
            System.out.println("Just admin can add games to Database");
            return;
        }else if (gameRepository.findById(id) == null) {
            System.out.printf("Game with %d dose not exist!%n", id);
            return;
        }
        gameRepository.deleteById(id);

    }

    @Override
    public void findAllGames() {
        Collection<Game> all = gameRepository.findAll();
        if (all.isEmpty()){
            return;
        }

        all.forEach(System.out::println);
    }

    @Override
    public void findGameByTitle(String title) {
        Game byGameTitle = gameRepository.findByGameTitle(title);
        System.out.println(byGameTitle.toString());
    }

    @Override
    public void getOwnedGamesFromLoggedInUser() {
        if (userService.getLogInUser() == null){
            System.out.println("First you should log in!");
            return;
        }else if (userService.getLogInUser().getGames().isEmpty()){
            System.out.println("You still do not have your own games!");
        }
        userService.getLogInUser().getGames()
                .forEach(System.out::println);
    }
}
