
package SkillBridge.controller;
import SkillBridge.repository.QuizAttemptRepository;
import SkillBridge.model.Skill;
import SkillBridge.repository.SkillRepository;
import java.util.List;
import SkillBridge.model.User;
import SkillBridge.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SkillRepository skillRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          SkillRepository skillRepository,
                          QuizAttemptRepository quizAttemptRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.skillRepository = skillRepository;
        this.quizAttemptRepository = quizAttemptRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String department,
            @RequestParam String password,
            Model model) {

        email = email.trim().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error",
                    "This email is already registered.");
            return "register";
        }

        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email);
        user.setDepartment(department.trim());
        user.setPasswordHash(passwordEncoder.encode(password));

        userRepository.save(user);

        return "redirect:/login?registered";
    }

    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        model.addAttribute("studentName", user.getFullName());
        model.addAttribute("department", user.getDepartment());

        List<Skill> skills = skillRepository.findAll();
        model.addAttribute("skills", skills);

        List<SkillBridge.model.QuizAttempt> attempts =
                quizAttemptRepository.findByUser_UserId(user.getUserId());
        model.addAttribute("hasFirstQuizBadge", !attempts.isEmpty());
        model.addAttribute("hasQuizExplorerBadge", attempts.size() >= 5);

        model.addAttribute("hasPerfectScoreBadge",
                attempts.stream().anyMatch(a ->
                        a.getTotalQuestions() > 0
                                && a.getScore() == a.getTotalQuestions()));
        int totalAttempts = attempts.size();
        int totalScore = 0;
        int totalQuestions = 0;

        for (SkillBridge.model.QuizAttempt attempt : attempts) {
            totalScore += attempt.getScore();
            totalQuestions += attempt.getTotalQuestions();
        }

        double averagePercentage = totalQuestions > 0
                ? totalScore * 100.0 / totalQuestions
                : 0.0;

        model.addAttribute("totalAttempts", totalAttempts);
        model.addAttribute("averagePercentage", averagePercentage);

        return "home";
    }


}
