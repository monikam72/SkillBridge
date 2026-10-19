
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LearningPartnerController {

    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;

    public LearningPartnerController(
            UserRepository userRepository,
            SkillRepository skillRepository,
            UserSkillRepository userSkillRepository) {

        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.userSkillRepository = userSkillRepository;
    }

    @GetMapping("/partners")
    public String partners(
            Authentication authentication,
            @RequestParam(required = false) Long skillId,
            Model model) {

        User currentUser = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        List<Skill> skills = skillRepository.findAll();
        model.addAttribute("skills", skills);
        model.addAttribute("selectedSkillId", skillId);

        List<UserSkill> partners;

        if (skillId != null
                && skillRepository.existsById(skillId)) {

            partners = userSkillRepository
                    .findBySkill_SkillIdAndSkillType(
                            skillId, "TEACH");
        } else {
            partners = userSkillRepository.findAll()
                    .stream()
                    .filter(us -> "TEACH".equals(us.getSkillType()))
                    .toList();
        }

        partners = partners.stream()
                .filter(us -> !us.getUser().getUserId()
                        .equals(currentUser.getUserId()))
                .toList();

        model.addAttribute("partners", partners);

        return "partners";
    }
}
