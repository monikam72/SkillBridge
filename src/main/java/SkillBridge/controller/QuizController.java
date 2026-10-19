
package SkillBridge.controller;

import SkillBridge.model.Quiz;
import SkillBridge.model.Question;
import SkillBridge.model.QuizAttempt;
import SkillBridge.model.User;
import SkillBridge.repository.QuizRepository;
import SkillBridge.repository.QuestionRepository;
import SkillBridge.repository.UserRepository;
import SkillBridge.repository.QuizAttemptRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

@Controller
public class QuizController {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final QuizAttemptRepository attemptRepository;

    public QuizController(
            QuizRepository quizRepository,
            QuestionRepository questionRepository,
            UserRepository userRepository,
            QuizAttemptRepository attemptRepository) {

        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.attemptRepository = attemptRepository;
    }

    // Display quiz questions
    @GetMapping("/quiz/{id}")
    public String startQuiz(@PathVariable Long id, Model model) {

        Quiz quiz = quizRepository.findById(id).orElse(null);

        if (quiz == null) {
            return "redirect:/assessments";
        }

        List<Question> questions =
                questionRepository.findByQuiz_QuizId(id);

        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);

        return "quiz";
    }

    // Check answers and save the result
    @PostMapping("/quiz/{id}/submit")
    public String submitQuiz(
            @PathVariable Long id,
            @RequestParam Map<String, String> answers,
            Authentication authentication,
            Model model) {

        Quiz quiz = quizRepository.findById(id).orElse(null);

        if (quiz == null) {
            return "redirect:/assessments";
        }

        List<Question> questions =
                questionRepository.findByQuiz_QuizId(id);

        if (questions.isEmpty()) {
            return "redirect:/assessments";
        }

        int score = 0;

        for (Question q : questions) {
            String answer =
                    answers.get("question_" + q.getQuestionId());

            if (answer != null &&
                    answer.equalsIgnoreCase(q.getCorrectOption())) {
                score++;
            }
        }

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setScore(score);
        attempt.setTotalQuestions(questions.size());

        attemptRepository.save(attempt);

        model.addAttribute("quizTitle", quiz.getQuizTitle());
        model.addAttribute("score", score);
        model.addAttribute("total", questions.size());
        model.addAttribute(
                "percentage",
                (score * 100) / questions.size()
        );

        return "quiz-result";
    }
}
