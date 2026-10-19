
package SkillBridge.controller;

import SkillBridge.model.QuizAttempt;
import SkillBridge.repository.QuizAttemptRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LeaderboardController {

    private final QuizAttemptRepository attemptRepository;

    public LeaderboardController(
            QuizAttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model) {

        List<QuizAttempt> attempts = attemptRepository.findAll();

        Map<Long, int[]> totals = new HashMap<>();
        Map<Long, String> names = new HashMap<>();

        for (QuizAttempt attempt : attempts) {
            Long userId = attempt.getUser().getUserId();

            int[] values = totals.computeIfAbsent(
                    userId, k -> new int[3]);

            values[0]++;
            values[1] += attempt.getScore();
            values[2] += attempt.getTotalQuestions();

            names.put(userId, attempt.getUser().getFullName());
        }

        List<Map<String, Object>> leaderboard = new ArrayList<>();

        for (Map.Entry<Long, int[]> entry : totals.entrySet()) {
            int[] values = entry.getValue();

            double percentage = values[2] > 0
                    ? values[1] * 100.0 / values[2]
                    : 0.0;

            Map<String, Object> student = new LinkedHashMap<>();
            student.put("studentName", names.get(entry.getKey()));
            student.put("attempts", values[0]);
            student.put("percentage", percentage);

            leaderboard.add(student);
        }

        leaderboard.sort((a, b) ->
                Double.compare(
                        (Double) b.get("percentage"),
                        (Double) a.get("percentage")));

        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboard.get(i).put("rank", i + 1);
        }

        model.addAttribute("leaderboard", leaderboard);

        return "leaderboard";
    }
}
