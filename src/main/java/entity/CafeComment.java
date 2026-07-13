package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "cafe_comments")
public class CafeComment extends BaseEntity {

    @Column(nullable = false)
    private String author;

    @Column(name = "comment_text", nullable = false, length = 1000)
    private String commentText;

    @Column(nullable = false)
    private int rating; // 1 to 5 stars

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public CafeComment() {
        this.createdAt = new Date();
    }

    public CafeComment(String author, String commentText, int rating) {
        this();
        this.author = author;
        this.commentText = commentText;
        this.rating = rating;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
