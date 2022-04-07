package game_shop.repositories.impl;

import game_shop.entities.Game;
import game_shop.entities.ShoppingCard;
import game_shop.exceptions.EntityPersistenceException;
import game_shop.exceptions.NonExistingEntityException;
import game_shop.repositories.ShoppingCardRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;


@Slf4j
public class ShoppingCardRepositoryJdbc implements ShoppingCardRepository {
    public static final String SELECT_ALL_CARD = "select * from `game`;";
    private static final String INSERT_NEW_SHOPPING_CARD =
            "insert into `shopping_card` (`number`, `user_id`) " +
                    "values (?, ?);";

    public static final String FIND_BY_ID = "select * from `shopping_card` where id = ?;";
    public static final String DELETE_CARD = "delete from shopping_card " +
            "where id = ?; ";
    private static final String SELECT_COUNT_CARD = "select count(*) as count from `shopping_card`;";

    private Connection connection;

    public ShoppingCardRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Collection<ShoppingCard> findAll() throws EntityPersistenceException {
        return null;
    }

    @Override
    public ShoppingCard findById(Long id) {
        try(var stmt = connection.prepareStatement(FIND_BY_ID)) {
            // 4. Set params and execute SQL query
            stmt.setInt(1, id.intValue());
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            rs.next();
            return new ShoppingCard(rs.getString("number"));
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_BY_ID, ex);
        }
    }

    @Override
    public ShoppingCard create(ShoppingCard entity) {

        try(var stmt = connection.prepareStatement(INSERT_NEW_SHOPPING_CARD, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, entity.getNumber());
            stmt.setInt(2, entity.getUser().getId().intValue());
            // 5. Execute insert statement
            connection.setAutoCommit(false);
            var affectedRows = stmt.executeUpdate();
            // more updates here ...
            connection.commit();
            connection.setAutoCommit(true);

            // 6. Check results and Get generated primary ke
            if (affectedRows == 0) {
                throw new EntityPersistenceException("Creating shopping card failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                    return entity;
                }
                else {
                    throw new EntityPersistenceException("Creating shopping card failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + INSERT_NEW_SHOPPING_CARD, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + INSERT_NEW_SHOPPING_CARD, ex);
        }
    }

    @Override
    public void addAll(Collection<ShoppingCard> entities) {

    }

    @Override
    public ShoppingCard update(ShoppingCard entity) throws NonExistingEntityException {
        return null;
    }

    @Override
    public ShoppingCard deleteById(Long id) throws NonExistingEntityException {
        ShoppingCard shoppingCard = null;
        try(var stmt = connection.prepareStatement(DELETE_CARD)) {

            shoppingCard = this.findById(id);

            stmt.setInt(1, id.intValue());
            var rs = stmt.executeUpdate();

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            System.out.println(ex.getMessage());
            throw new EntityPersistenceException("Error executing SQL query: " + DELETE_CARD, ex);
        }
        return shoppingCard;
    }

    @Override
    public long count() {
        try(var stmt = connection.prepareStatement(SELECT_COUNT_CARD)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            rs.next();
            return rs.getInt("count");

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_COUNT_CARD, ex);
        }
    }
}
