package game_shop.repositories.impl;

import game_shop.entities.Game;
import game_shop.exceptions.EntityPersistenceException;
import game_shop.exceptions.NonExistingEntityException;
import game_shop.repositories.GameRepository;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class GameRepositoryJdbc implements GameRepository {
    public static final String SELECT_ALL_GAMES = "select * from `game`;";
    private static final String SELECT_COUNT_GAMES = "select count(*) as count from `game`;";
    public static final String FIND_BY_ID = "select * from `game` where id = ?;";
    public static final String FIND_GAME_BY_TITLE = "select * from `game` where `title` = ?;";
    public static final String UPDATE_GAME = "update game set `price` = ?,  `size` = ?\n" +
            "where id = ?; ";
    public static final String DELETE_GAME = "delete from game " +
            "where id = ?; ";
    public static final String UPDATE_THE_HALL_GAME = "update game set `title` = ?,  `price` = ?, " +
            "size = ?,  trailer = ?, thumbnail_url = ?, description = ?, year = ? " +
            "where id = ?; ";
    public static final String INSERT_NEW_GAME =
            "insert into `game` (`title`, `price`, `size`, `trailer`, `thumbnail_url`, `description`, `year`) " +
                    "values (?, ?, ?, ?, ?, ?, ?);";
    private Connection connection;

    public GameRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }



    @Override
    public Collection<Game> findAll() throws EntityPersistenceException {
        try(var stmt = connection.prepareStatement(SELECT_ALL_GAMES)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
           return toGames(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_GAMES, ex);
        }
    }


    @Override
    public Game findById(Long id) {
        try(var stmt = connection.prepareStatement(FIND_BY_ID)) {
            // 4. Set params and execute SQL query
            stmt.setInt(1, id.intValue());
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toGames(rs).get(0);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_BY_ID, ex);
        }
    }

    @Override
    public Game create(Game entity) {
        try(var stmt = connection.prepareStatement(INSERT_NEW_GAME, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, entity.getTitle());
            stmt.setBigDecimal(2, entity.getPrice());
            stmt.setBigDecimal(3, entity.getSize());
            stmt.setString(4, entity.getTrailer());
            stmt.setString(5, entity.getThumbnailUrl());
            stmt.setString(6, entity.getDescription());
            stmt.setInt(7, entity.getYear());
            // 5. Execute insert statement
            connection.setAutoCommit(false);
            var affectedRows = stmt.executeUpdate();
            // more updates here ...
            connection.commit();
            connection.setAutoCommit(true);

            // 6. Check results and Get generated primary ke
            if (affectedRows == 0) {
                throw new EntityPersistenceException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                    return entity;
                }
                else {
                    throw new EntityPersistenceException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + SELECT_ALL_GAMES, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_GAMES, ex);
        }
    }

    @Override
    public void addAll(Collection<Game> entities) {

    }


    @Override
    public Game update(Game entity) throws NonExistingEntityException {
        try(var stmt = connection.prepareStatement(UPDATE_THE_HALL_GAME)) {

            stmt.setString(1, entity.getTitle());
            stmt.setBigDecimal(2, entity.getPrice());
            stmt.setBigDecimal(3, entity.getSize());
            stmt.setString(4, entity.getTrailer());
            stmt.setString(5, entity.getThumbnailUrl());
            stmt.setString(6, entity.getDescription());
            stmt.setInt(7, entity.getYear());
            stmt.setInt(8, entity.getId().intValue());
            var rs = stmt.executeUpdate();

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            System.out.println(ex.getMessage());
            throw new EntityPersistenceException("Error executing SQL query: " + UPDATE_THE_HALL_GAME, ex);
        }

        return null;
    }

    @Override
    public Game deleteById(Long id) throws NonExistingEntityException {

        Game game = null;
        try(var stmt = connection.prepareStatement(DELETE_GAME)) {

            game = this.findById(id);

            stmt.setInt(1, id.intValue());
            var rs = stmt.executeUpdate();

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            System.out.println(ex.getMessage());
            throw new EntityPersistenceException("Error executing SQL query: " + DELETE_GAME, ex);
        }
        return game;
    }

    @Override
    public long count() {
        try(var stmt = connection.prepareStatement(SELECT_COUNT_GAMES)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            rs.next();
            return rs.getInt("count");

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_COUNT_GAMES, ex);
        }
    }

    // utility methods
    public List<Game> toGames(ResultSet rs) throws SQLException {
        List<Game> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new Game(
                    rs.getLong(1),
                    rs.getString("title"),
                    rs.getBigDecimal("price"),
                    rs.getBigDecimal("size"),
                    rs.getString("trailer"),
                    rs.getString("thumbnail_url"),
                    rs.getString("description"),
                    rs.getInt("year")

            ));
        }
        return results;
    }

    @Override
    public Game findByGameTitle(String gameTitle) {
        try(var stmt = connection.prepareStatement(FIND_GAME_BY_TITLE)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, gameTitle);
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            rs.next();
            return new Game(rs.getLong("id"), rs.getString("title"), rs.getBigDecimal("price"),
                    rs.getBigDecimal("size"), rs.getString("trailer"), rs.getString("thumbnail_url"),
                    rs.getString("description"), rs.getInt("year"));
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_GAME_BY_TITLE, ex);
        }
    }

    @Override
    public void editGame(Long gameID, BigDecimal price, BigDecimal size) {
        try(var stmt = connection.prepareStatement(UPDATE_GAME)) {

            stmt.setBigDecimal(1, price);
            stmt.setBigDecimal(2, size);
            stmt.setInt(3, gameID.intValue());
            var rs = stmt.executeUpdate();

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            System.out.println(ex.getMessage());
            throw new EntityPersistenceException("Error executing SQL query: " + UPDATE_GAME, ex);
        }

    }

}
