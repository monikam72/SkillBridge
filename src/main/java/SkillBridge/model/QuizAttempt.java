
package SkillBridge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_attempts")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_id")
    private Long attemptId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false)
    private int score;

    @Column(name = "total_questions", nullable = false)
    private int totalQuestions;

    @Column(name = "attempted_at")
    private LocalDateTime attemptedAt;

    public QuizAttempt() {}

    @PrePersist
    public void setAttemptedAt() {
        attemptedAt = LocalDateTime.now();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public User getUser() {
        return user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }

}
