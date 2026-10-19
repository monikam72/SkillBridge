
package SkillBridge.controller;

import SkillBridge.model.Skill;
import SkillBridge.model.LearningResource;
import SkillBridge.repository.SkillRepository;
import SkillBridge.repository.LearningResourceRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LearningHubController {

    private final SkillRepository skillRepository;
    private final LearningResourceRepository resourceRepository;

    public LearningHubController(
            SkillRepository skillRepository,
            LearningResourceRepository resourceRepository) {
        this.skillRepository = skillRepository;
        this.resourceRepository = resourceRepository;
    }

    @GetMapping("/learning-hub")
    public String learningHub(
            @RequestParam(required = false) Long skillId,
            Model model) {

        List<Skill> skills = skillRepository.findAll();
        model.addAttribute("skills", skills);
        model.addAttribute("selectedSkillId", skillId);

        List<LearningResource> resources;

        if (skillId != null && skillRepository.existsById(skillId)) {
            resources = resourceRepository.findBySkill_SkillId(skillId);
        } else {
            resources = resourceRepository.findAll();
        }

        model.addAttribute("resources", resources);

        return "learning-hub";
    }
}
