package isil.java_quiz_server.service;

import isil.java_quiz_server.modal.QuizResult;
import isil.java_quiz_server.repository.QuizResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuizResultService {
    private static final Logger logger = LoggerFactory.getLogger(QuizResultService.class);

    @Autowired
    private QuizResultRepository quizResultRepository;

    public QuizResult saveQuizResult(QuizResult quizResult) {
        logger.debug("Saving quiz result: userId={}, quizId={}", quizResult.getUserId(), quizResult.getQuizId());
        return quizResultRepository.save(quizResult);
    }

    public List<QuizResult> getAllResults() {
        logger.debug("Fetching all quiz results");
        return quizResultRepository.findAllByOrderByCompletedAtDesc();
    }

    public List<QuizResult> getResultsByUserId(Long userId) {
        logger.debug("Fetching quiz results for userId: {}", userId);
        return quizResultRepository.findByUserId(userId);
    }

    public List<QuizResult> getResultsByQuizId(Long quizId) {
        logger.debug("Fetching quiz results for quizId: {}", quizId);
        return quizResultRepository.findByQuizIdOrderByScoreDesc(quizId);
    }

    public List<QuizResult> getResultsByUsername(String username) {
        logger.debug("Fetching quiz results for username: {}", username);
        return quizResultRepository.findByUsername(username);
    }

    public List<QuizResult> getRecentResults() {
        logger.debug("Fetching recent quiz results");
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return quizResultRepository.getRecentResults(thirtyDaysAgo);
    }

    public List<QuizResult> getTopPerformersForQuiz(Long quizId) {
        logger.debug("Fetching top performers for quizId: {}", quizId);
        return quizResultRepository.getTopPerformersForQuiz(quizId);
    }

    public Double getAverageScoreForQuiz(Long quizId) {
        logger.debug("Fetching average score for quizId: {}", quizId);
        Double average = quizResultRepository.getAverageScoreForQuiz(quizId);
        return average != null ? average : 0.0;
    }

    public List<Map<String, Object>> getQuizStatistics() {
        logger.debug("Fetching quiz statistics");
        List<Object[]> rawData = quizResultRepository.getQuizStatistics();
        List<Map<String, Object>> statistics = new ArrayList<>();

        for (Object[] row : rawData) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("quizId", row[0]);
            stat.put("quizTitle", row[1]);
            stat.put("totalAttempts", row[2]);
            stat.put("averageScore", row[3]);
            stat.put("highestScore", row[4]);
            stat.put("lowestScore", row[5]);
            statistics.add(stat);
        }

        return statistics;
    }

    public QuizResult createQuizResult(Long userId, String username, Long quizId,
                                     String quizTitle, Integer score, Integer totalQuestions) {
        logger.debug("Creating quiz result: userId={}, quizId={}", userId, quizId);
        QuizResult result = new QuizResult(userId, username, quizId, quizTitle, score, totalQuestions);
        return saveQuizResult(result);
    }

    public QuizResult getBestScoreForUserOnQuiz(Long userId, Long quizId) {
        logger.debug("Fetching best score for userId: {}, quizId: {}", userId, quizId);
        List<QuizResult> results = quizResultRepository.findByUserIdAndQuizId(userId, quizId);
        return results.stream()
                .max((r1, r2) -> Integer.compare(r1.getScore(), r2.getScore()))
                .orElse(null);
    }

    public boolean hasUserTakenQuiz(Long userId, Long quizId) {
        logger.debug("Checking if userId: {} has taken quizId: {}", userId, quizId);
        List<QuizResult> results = quizResultRepository.findByUserIdAndQuizId(userId, quizId);
        return !results.isEmpty();
    }

    public long getUserAttemptCount(Long userId, Long quizId) {
        logger.debug("Fetching attempt count for userId: {}, quizId: {}", userId, quizId);
        List<QuizResult> results = quizResultRepository.findByUserIdAndQuizId(userId, quizId);
        return results.size();
    }
}