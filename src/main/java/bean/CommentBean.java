package bean;

import entity.CafeComment;
import facadelocal.CommentFacadeLocal;
import util.FacesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class CommentBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CommentFacadeLocal commentFacade;

    private String author;
    private String commentText;
    private int rating = 5;
    
    private List<CafeComment> commentsCache;

    @PostConstruct
    public void init() {
        refreshComments();
    }

    public void refreshComments() {
        commentsCache = commentFacade.getAllComments();
    }

    public List<CafeComment> getComments() {
        if (commentsCache == null) {
            refreshComments();
        }
        return commentsCache;
    }

    public void submitComment() {
        if (author == null || author.trim().isEmpty()) {
            FacesUtil.addErrorMessage("Hata", "Lütfen adınızı giriniz.");
            return;
        }
        if (commentText == null || commentText.trim().isEmpty()) {
            FacesUtil.addErrorMessage("Hata", "Lütfen yorumunuzu yazınız.");
            return;
        }
        if (rating < 1 || rating > 5) {
            rating = 5;
        }

        CafeComment newComment = new CafeComment(author.trim(), commentText.trim(), rating);
        commentFacade.createComment(newComment);
        
        // Reset inputs except author (so they don't have to retype their name)
        commentText = "";
        rating = 5;

        // Refresh cache
        refreshComments();
        
        FacesUtil.addInfoMessage("Teşekkürler!", "Yorumunuz başarıyla paylaşıldı.");
    }

    // Getters and Setters
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
