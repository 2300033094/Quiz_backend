package isil.java_quiz_server.config;

import isil.java_quiz_server.modal.User;
import isil.java_quiz_server.modal.Quiz;
import isil.java_quiz_server.modal.Question;
import isil.java_quiz_server.modal.QuizResult;
import isil.java_quiz_server.repository.UserRepository;
import isil.java_quiz_server.repository.QuizRepository;
import isil.java_quiz_server.repository.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizResultRepository quizResultRepository;

    private final Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Only initialize if there's no quiz result data
        if (quizResultRepository.count() == 0) {
            initializeSampleData();
        }
    }

    @Transactional
    private void initializeSampleData() {
        System.out.println("Initializing sample student data...");

        // Clear existing data to avoid duplicates
        userRepository.deleteAllInBatch();
        quizRepository.deleteAllInBatch();
        quizResultRepository.deleteAllInBatch();

        // Create teacher
        createTeacher();

        // Create sample students
        List<User> students = createSampleStudents();

        // Create sample quizzes
        List<Quiz> quizzes = createSampleQuizzes();

        // Create quiz results for students
        createQuizResults(students, quizzes);

        System.out.println("Sample data initialization completed!");
    }

    @Transactional
    private void createTeacher() {
        User teacher = new User();
        teacher.setUsername("teacher");
        teacher.setEmail("teacher@school.edu");
        teacher.setPassword("password123");
        teacher.setIs_teacher(true);
        teacher.setPhone("123-456-7890");
        userRepository.save(teacher);
        System.out.println("Created teacher user.");
    }

    @Transactional
    private List<User> createSampleStudents() {
        String[] studentNames = {
            "john_doe", "jane_smith", "mike_johnson", "sarah_wilson", "david_brown",
            "emily_davis", "chris_miller", "amanda_garcia", "james_rodriguez", "jessica_martinez",
            "ryan_anderson", "melissa_taylor", "kevin_thomas", "nicole_jackson", "brandon_white",
            "stephanie_harris", "daniel_martin", "rachel_thompson", "andrew_garcia", "laura_martinez",
            "matthew_robinson", "amy_clark", "justin_rodriguez", "megan_lewis", "tyler_lee"
        };

        List<User> createdStudents = new ArrayList<>();
        for (String username : studentNames) {
            if (userRepository.findByUsername(username) == null) {
                User student = new User();
                student.setUsername(username);
                student.setEmail(username + "@student.edu");
                student.setPassword("password123");
                student.setIs_teacher(false);
                student.setPhone("000-000-0000");
                createdStudents.add(userRepository.save(student));
            }
        }

        // Get all non-teacher users
        List<User> allUsers = userRepository.findAll();
        List<User> students = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getIs_teacher() != null && !user.getIs_teacher()) {
                students.add(user);
            }
        }

        return students;
    }

    @Transactional
    private List<Quiz> createSampleQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();

        // Create sample quizzes if none exist
        Quiz mathQuiz = new Quiz();
        mathQuiz.setTitle("Basic Mathematics");
        mathQuiz.setUsername("teacher");

        List<Question> mathQuestions = new ArrayList<>();

        Question q1 = new Question();
        q1.setText("What is 2 + 2?");
        q1.setOptions(Arrays.asList("3", "4", "5", "6"));
        q1.setCorrectOption("4");
        mathQuestions.add(q1);

        Question q2 = new Question();
        q2.setText("What is 10 - 3?");
        q2.setOptions(Arrays.asList("6", "7", "8", "9"));
        q2.setCorrectOption("7");
        mathQuestions.add(q2);

        Question q3 = new Question();
        q3.setText("What is 5 × 3?");
        q3.setOptions(Arrays.asList("12", "15", "18", "20"));
        q3.setCorrectOption("15");
        mathQuestions.add(q3);

        Question q4 = new Question();
        q4.setText("What is 20 ÷ 4?");
        q4.setOptions(Arrays.asList("4", "5", "6", "7"));
        q4.setCorrectOption("5");
        mathQuestions.add(q4);

        Question q5 = new Question();
        q5.setText("What is 3²?");
        q5.setOptions(Arrays.asList("6", "8", "9", "12"));
        q5.setCorrectOption("9");
        mathQuestions.add(q5);

        mathQuiz.setQuestions(mathQuestions);
        quizzes.add(quizRepository.save(mathQuiz));

        // Science Quiz
        Quiz scienceQuiz = new Quiz();
        scienceQuiz.setTitle("Basic Science");
        scienceQuiz.setUsername("teacher");

        List<Question> scienceQuestions = new ArrayList<>();

        Question sq1 = new Question();
        sq1.setText("What is H2O?");
        sq1.setOptions(Arrays.asList("Oxygen", "Water", "Hydrogen", "Carbon"));
        sq1.setCorrectOption("Water");
        scienceQuestions.add(sq1);

        Question sq2 = new Question();
        sq2.setText("How many planets are in our solar system?");
        sq2.setOptions(Arrays.asList("7", "8", "9", "10"));
        sq2.setCorrectOption("8");
        scienceQuestions.add(sq2);

        Question sq3 = new Question();
        sq3.setText("What gas do plants absorb?");
        sq3.setOptions(Arrays.asList("Oxygen", "Nitrogen", "Carbon Dioxide", "Helium"));
        sq3.setCorrectOption("Carbon Dioxide");
        scienceQuestions.add(sq3);

        Question sq4 = new Question();
        sq4.setText("What is the largest organ in human body?");
        sq4.setOptions(Arrays.asList("Heart", "Brain", "Liver", "Skin"));
        sq4.setCorrectOption("Skin");
        scienceQuestions.add(sq4);

        Question sq5 = new Question();
        sq5.setText("What is the speed of light?");
        sq5.setOptions(Arrays.asList("300,000 km/s", "150,000 km/s", "450,000 km/s", "600,000 km/s"));
        sq5.setCorrectOption("300,000 km/s");
        scienceQuestions.add(sq5);

        scienceQuiz.setQuestions(scienceQuestions);
        quizzes.add(quizRepository.save(scienceQuiz));

        // History Quiz
        Quiz historyQuiz = new Quiz();
        historyQuiz.setTitle("World History");
        historyQuiz.setUsername("teacher");

        List<Question> historyQuestions = new ArrayList<>();

        Question hq1 = new Question();
        hq1.setText("In which year did World War II end?");
        hq1.setOptions(Arrays.asList("1944", "1945", "1946", "1947"));
        hq1.setCorrectOption("1945");
        historyQuestions.add(hq1);

        Question hq2 = new Question();
        hq2.setText("Who was the first person on the moon?");
        hq2.setOptions(Arrays.asList("Buzz Aldrin", "Neil Armstrong", "John Glenn", "Alan Shepard"));
        hq2.setCorrectOption("Neil Armstrong");
        historyQuestions.add(hq2);

        Question hq3 = new Question();
        hq3.setText("Which empire built Machu Picchu?");
        hq3.setOptions(Arrays.asList("Aztec", "Maya", "Inca", "Olmec"));
        hq3.setCorrectOption("Inca");
        historyQuestions.add(hq3);

        Question hq4 = new Question();
        hq4.setText("When did the Berlin Wall fall?");
        hq4.setOptions(Arrays.asList("1987", "1988", "1989", "1990"));
        hq4.setCorrectOption("1989");
        historyQuestions.add(hq4);

        Question hq5 = new Question();
        hq5.setText("Who invented the telephone?");
        hq5.setOptions(Arrays.asList("Thomas Edison", "Alexander Graham Bell", "Nikola Tesla", "Guglielmo Marconi"));
        hq5.setCorrectOption("Alexander Graham Bell");
        historyQuestions.add(hq5);

        historyQuiz.setQuestions(historyQuestions);
        quizzes.add(quizRepository.save(historyQuiz));

        return quizzes;
    }

    @Transactional
    private void createQuizResults(List<User> students, List<Quiz> quizzes) {
        LocalDateTime now = LocalDateTime.now();

        for (User student : students) {
            for (Quiz quiz : quizzes) {
                // Not every student takes every quiz (70% participation rate)
                if (random.nextDouble() > 0.3) {
                    QuizResult result = new QuizResult();
                    result.setUserId(student.getId());
                    result.setQuizId(quiz.getId());
                    result.setUsername(student.getUsername());
                    result.setQuizTitle(quiz.getTitle());

                    // Get actual number of questions
                    int totalQuestions = quiz.getQuestions().size();
                    int correctAnswers = generateRealisticScore(totalQuestions);

                    result.setScore(correctAnswers);
                    result.setTotalQuestions(totalQuestions);
                    // Percentage is calculated automatically in the entity

                    // Random completion time within the last 30 days
                    int daysAgo = random.nextInt(30);
                    int hoursAgo = random.nextInt(24);
                    int minutesAgo = random.nextInt(60);
                    result.setCompletedAt(now.minusDays(daysAgo).minusHours(hoursAgo).minusMinutes(minutesAgo));

                    // Random time taken (1-10 minutes)
                    result.setTimeTakenSeconds(60 + random.nextInt(540)); // 1-10 minutes

                    quizResultRepository.save(result);
                }
            }
        }
    }

    private int generateRealisticScore(int totalQuestions) {
        // Generate realistic score distribution
        // 20% excellent (90-100%), 30% good (75-89%), 30% average (60-74%), 20% below average (40-59%)
        double rand = random.nextDouble();

        if (rand < 0.2) {
            // Excellent students (90-100%)
            return (int) Math.ceil(totalQuestions * (0.9 + random.nextDouble() * 0.1));
        } else if (rand < 0.5) {
            // Good students (75-89%)
            return (int) Math.ceil(totalQuestions * (0.75 + random.nextDouble() * 0.14));
        } else if (rand < 0.8) {
            // Average students (60-74%)
            return (int) Math.ceil(totalQuestions * (0.6 + random.nextDouble() * 0.14));
        } else {
            // Below average students (40-59%)
            return (int) Math.ceil(totalQuestions * (0.4 + random.nextDouble() * 0.19));
        }
    }
}