package game_shop.repositories.impl;

import game_shop.entities.Comment;
import game_shop.entities.Game;
import game_shop.exceptions.EntityPersistenceException;
import game_shop.exceptions.NonExistingEntityException;
import game_shop.repositories.CommentRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class CommentRepositoryJdbc implements CommentRepository {
    public static final String SELECT_ALL_COMMENTS = "select * from `comment`;";
    public static final String SELECT_ALL_COMMENTS_BY_USER = "select * from `comment` where user_id = ?;";
    private static final String INSERT_NEW_COMMENT =
            "insert into `comment` (`description`, `user_id`, `game_id`) " +
                    "values (?, ?, ?);";
    public static final String FIND_BY_ID = "select * from `comment` where id = ?;";
    public static final String UPDATE_COMMENT = "update comment set `description` = ? " +
            "where id = ?; ";
    public static final String DELETE_COMMENT = "delete from comment " +
            "where id = ?; ";
    private static final String SELECT_COUNT_COMMENTS = "select count(*) as count from `comment`;";
    Connection connection;

    public CommentRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Collection<Comment> findAll() throws EntityPersistenceException {
        try(var stmt = connection.prepareStatement(SELECT_ALL_COMMENTS)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toComments(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_COMMENTS, ex);
        }
    }

    @Override
    public Comment findById(Long id) {
        try(var stmt = connection.prepareStatement(FIND_BY_ID)) {
            // 4. Set params and execute SQL query
            stmt.setInt(1, id.intValue());
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toComments(rs).get(0);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + FIND_BY_ID, ex);
        }
    }

    @Override
    public Comment create(Comment entity) {
        try(var stmt = connection.prepareStatement(INSERT_NEW_COMMENT, Statement.RETURN_GENERATED_KEYS)) {
            // 4. Set params and execute SQL query
            stmt.setString(1, entity.getDescription());
            stmt.setInt(2, entity.getUser().getId().intValue());
            stmt.setInt(3, entity.getGame().getId().intValue());
            // 5. Execute insert statement
            connection.setAutoCommit(false);
            var affectedRows = stmt.executeUpdate();
            // more updates here ...
            connection.commit();
            connection.setAutoCommit(true);

            // 6. Check results and Get generated primary ke
            if (affectedRows == 0) {
                throw new EntityPersistenceException("Creating comment failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                    return entity;
                }
                else {
                    throw new EntityPersistenceException("Creating comment failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                throw new EntityPersistenceException("Error rolling back SQL query: " + INSERT_NEW_COMMENT, ex);
            }
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + INSERT_NEW_COMMENT, ex);
        }
    }

    @Override
    public void addAll(Collection<Comment> entities) {

    }

    @Override
    public Comment update(Comment entity) throws NonExistingEntityException {
        try(var stmt = connection.prepareStatement(UPDATE_COMMENT)) {

            stmt.setString(1, entity.getDescription());
            stmt.setInt(2, (int)entity.getId());
            var rs = stmt.executeUpdate();

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            System.out.println(ex.getMessage());
            throw new EntityPersistenceException("Error executing SQL query: " + UPDATE_COMMENT, ex);
        }

        return null;
    }

    @Override
    public Comment deleteById(Long id) throws NonExistingEntityException {
        Comment comment = null;
        try(var stmt = connection.prepareStatement(DELETE_COMMENT)) {

            comment = this.findById(id);

            stmt.setInt(1, id.intValue());
            var rs = stmt.executeUpdate();

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            System.out.println(ex.getMessage());
            throw new EntityPersistenceException("Error executing SQL query: " + DELETE_COMMENT, ex);
        }
        return comment;
    }

    @Override
    public long count() {
        try(var stmt = connection.prepareStatement(SELECT_COUNT_COMMENTS)) {
            // 4. Set params and execute SQL query
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            rs.next();
            return rs.getInt("count");

        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_COUNT_COMMENTS, ex);
        }
    }
    public List<Comment> toComments(ResultSet rs) throws SQLException {
        List<Comment> results = new ArrayList<>();
        while (rs.next()) {
            results.add(new Comment(
                    rs.getLong(1),
                    rs.getString("description")

            ));
        }
        return results;
    }

    @Override
    public List<Comment> findAllCommentsFromCurrentUser(Long id) {
        try(var stmt = connection.prepareStatement(SELECT_ALL_COMMENTS_BY_USER)) {
            // 4. Set params and execute SQL query
            stmt.setInt(1, id.intValue());
            var rs = stmt.executeQuery();
            // 5. Transform ResultSet to Book
            return toComments(rs);
        } catch (SQLException ex) {
            log.error("Error creating connection to DB", ex);
            throw new EntityPersistenceException("Error executing SQL query: " + SELECT_ALL_COMMENTS_BY_USER, ex);
        }
    }
}
