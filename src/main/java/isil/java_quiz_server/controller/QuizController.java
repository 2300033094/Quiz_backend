package isil.java_quiz_server.controller;

import isil.java_quiz_server.modal.Quiz;
import isil.java_quiz_server.modal.QuizResult;
import isil.java_quiz_server.repository.QuizRepository;
import isil.java_quiz_server.service.QuizResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
public class QuizController {

    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    private final QuizRepository quizRepository;
    private final QuizResultService quizResultService;

    @Autowired
    public QuizController(QuizRepository quizRepository, QuizResultService quizResultService) {
        this.quizRepository = quizRepository;
        this.quizResultService = quizResultService;
    }

    @PostConstruct
    public void init() {
        logger.info("QuizResultService injected: {}", quizResultService != null);
    }

    @GetMapping("/quizzes")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        logger.debug("Fetching all quizzes");
        List<Quiz> quizzes = quizRepository.findAll();
        return ResponseEntity.ok(quizzes);
    }

    @PostMapping("/quizzes")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        logger.debug("Creating quiz: {}", quiz != null ? quiz.getTitle() : "null");
        if (quiz == null || quiz.getTitle() == null || quiz.getUsername() == null) {
            logger.warn("Invalid quiz data provided: title={}, username={}", 
                quiz != null ? quiz.getTitle() : "null", 
                quiz != null ? quiz.getUsername() : "null");
            return ResponseEntity.badRequest().build();
        }
        Quiz savedQuiz = quizRepository.save(quiz);
        logger.info("Quiz created successfully: id={}", savedQuiz.getId());
        return ResponseEntity.ok(savedQuiz);
    }

    @GetMapping("/quizzes/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        logger.debug("Fetching quiz with ID: {}", id);
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        return quizOptional
                .map(quiz -> {
                    logger.info("Quiz found: id={}", id);
                    return ResponseEntity.ok(quiz);
                })
                .orElseGet(() -> {
                    logger.warn("Quiz with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping("/quiz-results")
    public ResponseEntity<QuizResult> submitQuizResult(@RequestBody QuizResult quizResult) {
        logger.debug("Submitting quiz result for user ID: {}, quiz ID: {}", 
            quizResult != null ? quizResult.getUserId() : "null", 
            quizResult != null ? quizResult.getQuizId() : "null");
        if (quizResult == null || quizResult.getUserId() == null || quizResult.getQuizId() == null) {
            logger.warn("Invalid quiz result data provided");
            return ResponseEntity.badRequest().build();
        }
        QuizResult savedResult = quizResultService.saveQuizResult(quizResult);
        logger.info("Quiz result saved: id={}", savedResult.getId());
        return ResponseEntity.ok(savedResult);
    }

    @GetMapping("/quiz-results")
    public ResponseEntity<List<QuizResult>> getAllQuizResults() {
        logger.debug("Fetching all quiz results");
        List<QuizResult> results = quizResultService.getAllResults();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/quiz-results/quiz/{quizId}")
    public ResponseEntity<List<QuizResult>> getResultsByQuizId(@PathVariable Long quizId) {
        logger.debug("Fetching quiz results for quiz ID: {}", quizId);
        List<QuizResult> results = quizResultService.getResultsByQuizId(quizId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/quiz-results/user/{userId}")
    public ResponseEntity<List<QuizResult>> getResultsByUserId(@PathVariable Long userId) {
        logger.debug("Fetching quiz results for user ID: {}", userId);
        List<QuizResult> results = quizResultService.getResultsByUserId(userId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/quiz-results/username/{username}")
    public ResponseEntity<List<QuizResult>> getResultsByUsername(@PathVariable String username) {
        logger.debug("Fetching quiz results for username: {}", username);
        List<QuizResult> results = quizResultService.getResultsByUsername(username);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/quiz-results/recent")
    public ResponseEntity<List<QuizResult>> getRecentResults() {
        logger.debug("Fetching recent quiz results");
        List<QuizResult> results = quizResultService.getRecentResults();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/quiz-results/top-performers/{quizId}")
    public ResponseEntity<List<QuizResult>> getTopPerformersForQuiz(@PathVariable Long quizId) {
        logger.debug("Fetching top performers for quiz ID: {}", quizId);
        List<QuizResult> results = quizResultService.getTopPerformersForQuiz(quizId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/quiz-results/average-score/{quizId}")
    public ResponseEntity<Double> getAverageScoreForQuiz(@PathVariable Long quizId) {
        logger.debug("Fetching average score for quiz ID: {}", quizId);
        Double averageScore = quizResultService.getAverageScoreForQuiz(quizId);
        return ResponseEntity.ok(averageScore);
    }

    @GetMapping("/quiz-statistics")
    public ResponseEntity<List<Map<String, Object>>> getQuizStatistics() {
        logger.debug("Fetching quiz statistics");
        List<Map<String, Object>> statistics = quizResultService.getQuizStatistics();
        return ResponseEntity.ok(statistics);
    }
}