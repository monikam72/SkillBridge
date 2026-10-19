
package SkillBridge.controller;

import SkillBridge.model.Skill;
import SkillBridge.model.User;
import SkillBridge.model.UserSkill;
import SkillBridge.repository.SkillRepository;
import SkillBridge.repository.UserRepository;
import SkillBridge.repository.UserSkillRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserSkillController {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;

    public UserSkillController(
            UserRepository userRepository,
            SkillRepository skillRepository,
            UserSkillRepository userSkillRepository) {

        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
    }

    @GetMapping("/my-skills")
    public String mySkills(Authentication authentication, Model model) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        List<Skill> skills = skillRepository.findAll();

        List<UserSkill> mySkills =
                userSkillRepository.findByUser_UserId(user.getUserId());

        model.addAttribute("skills", skills);
        model.addAttribute("mySkills", mySkills);

        return "my-skills";
    }

    @PostMapping("/my-skills")
    public String saveSkill(
            Authentication authentication,
            @RequestParam Long skillId,
            @RequestParam String skillType,
            @RequestParam String proficiency,
            RedirectAttributes redirectAttributes) {

        if (!skillType.equals("TEACH") && !skillType.equals("LEARN")) {
            return "redirect:/my-skills";
        }

        if (!List.of("Beginner", "Intermediate", "Advanced")
                .contains(proficiency)) {
            return "redirect:/my-skills";
        }

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow();

        boolean exists = userSkillRepository
                .findByUser_UserId(user.getUserId())
                .stream()
                .anyMatch(us ->
                        us.getSkill().getSkillId().equals(skillId)
                                && us.getSkillType().equals(skillType));

        if (exists) {
            redirectAttributes.addFlashAttribute(
                    "message", "You have already added this skill.");
            return "redirect:/my-skills";
        }

        UserSkill userSkill = new UserSkill();
        userSkill.setUser(user);
        userSkill.setSkill(skill);
        userSkill.setSkillType(skillType);
        userSkill.setProficiency(proficiency);

        userSkillRepository.save(userSkill);

        redirectAttributes.addFlashAttribute(
                "message", "Skill added successfully!");

        return "redirect:/my-skills";
    }
}
