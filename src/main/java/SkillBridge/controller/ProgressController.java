
package SkillBridge.controller;

import SkillBridge.model.User;
import SkillBridge.repository.QuizAttemptRepository;
import SkillBridge.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProgressController {

    private final QuizAttemptRepository attemptRepository;
    private final UserRepository userRepository;

    public ProgressController(
            QuizAttemptRepository attemptRepository,
            UserRepository userRepository) {
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/progress")
    public String showProgress(Authentication authentication, Model model) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("attempts",
                attemptRepository.findByUser_UserId(user.getUserId()));

        return "progress";
    }
}
