
package SkillBridge.controller;

import SkillBridge.model.Skill;
import SkillBridge.repository.SkillRepository;
import SkillBridge.repository.QuizRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AssessmentController {

    private final SkillRepository skillRepository;
    private final QuizRepository quizRepository;

    public AssessmentController(
            SkillRepository skillRepository,
            QuizRepository quizRepository) {
        this.skillRepository = skillRepository;
        this.quizRepository = quizRepository;
    }

    @GetMapping("/assessments")
    public String assessments(
            @RequestParam(required = false) Long skillId,
            Model model) {

        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute("selectedSkillId", skillId);

        if (skillId != null &&
                skillRepository.existsById(skillId)) {
            model.addAttribute(
                    "quizzes",
                    quizRepository.findBySkill_SkillId(skillId));
        } else {
            model.addAttribute("quizzes",
                    quizRepository.findAll());
        }

        return "assessments";
    }
}
