package isil.java_quiz_server.modal;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_results")
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "quiz_id")
    private Long quizId;

    @Column(name = "quiz_title")
    private String quizTitle;

    @Column(name = "score")
    private Integer score;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "percentage")
    private Double percentage;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "time_taken_seconds")
    private Integer timeTakenSeconds;

    public QuizResult() {
        this.completedAt = LocalDateTime.now();
    }

    public QuizResult(Long userId, String username, Long quizId, String quizTitle,
                     Integer score, Integer totalQuestions) {
        this();
        this.userId = userId;
        this.username = username;
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = totalQuestions > 0 ? (score * 100.0) / totalQuestions : 0.0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Long getQuizId() { return quizId; }
    public void setQuizId(Long quizId) { this.quizId = quizId; }
    public String getQuizTitle() { return quizTitle; }
    public void setQuizTitle(String quizTitle) { this.quizTitle = quizTitle; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) {
        this.score = score;
        if (this.totalQuestions != null && this.totalQuestions > 0) {
            this.percentage = (score * 100.0) / this.totalQuestions;
        }
    }
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
        if (this.score != null && totalQuestions > 0) {
            this.percentage = (this.score * 100.0) / totalQuestions;
        }
    }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public Integer getTimeTakenSeconds() { return timeTakenSeconds; }
    public void setTimeTakenSeconds(Integer timeTakenSeconds) { this.timeTakenSeconds = timeTakenSeconds; }

    @Override
    public String toString() {
        return "QuizResult{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", quizId=" + quizId +
                ", quizTitle='" + quizTitle + '\'' +
                ", score=" + score +
                ", totalQuestions=" + totalQuestions +
                ", percentage=" + percentage +
                ", completedAt=" + completedAt +
                ", timeTakenSeconds=" + timeTakenSeconds +
                '}';
    }
}