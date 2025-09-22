package isil.java_quiz_server.repository;

import isil.java_quiz_server.modal.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    List<QuizResult> findByUserId(Long userId);
    List<QuizResult> findByQuizId(Long quizId);
    List<QuizResult> findByUserIdAndQuizId(Long userId, Long quizId);
    List<QuizResult> findByUsername(String username);
    List<QuizResult> findAllByOrderByCompletedAtDesc();
    List<QuizResult> findByQuizIdOrderByScoreDesc(Long quizId);
    List<QuizResult> findByCompletedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT AVG(qr.percentage) FROM QuizResult qr WHERE qr.quizId = :quizId")
    Double getAverageScoreForQuiz(@Param("quizId") Long quizId);

    @Query("SELECT qr FROM QuizResult qr WHERE qr.quizId = :quizId ORDER BY qr.score DESC, qr.completedAt ASC")
    List<QuizResult> getTopPerformersForQuiz(@Param("quizId") Long quizId);

    @Query("SELECT qr FROM QuizResult qr WHERE qr.completedAt >= :thirtyDaysAgo ORDER BY qr.completedAt DESC")
    List<QuizResult> getRecentResults(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

    @Query("SELECT qr.quizId, qr.quizTitle, COUNT(qr), AVG(qr.percentage), MAX(qr.score), MIN(qr.score) " +
           "FROM QuizResult qr GROUP BY qr.quizId, qr.quizTitle")
    List<Object[]> getQuizStatistics();
}