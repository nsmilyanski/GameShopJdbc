package game_shop.repositories;

import game_shop.entities.Comment;

import java.util.List;

public interface CommentRepository extends BaseRepository<Long, Comment>{
    List<Comment> findAllCommentsFromCurrentUser(Long id);
}
