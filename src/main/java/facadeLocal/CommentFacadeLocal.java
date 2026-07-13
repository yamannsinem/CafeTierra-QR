package facadelocal;

import entity.CafeComment;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface CommentFacadeLocal {
    void createComment(CafeComment comment);
    List<CafeComment> getAllComments();
}
