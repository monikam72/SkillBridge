
package SkillBridge.controller;

import SkillBridge.model.LearningRequest;
import SkillBridge.model.Skill;
import SkillBridge.model.User;
import SkillBridge.repository.LearningRequestRepository;
import SkillBridge.repository.SkillRepository;
import SkillBridge.repository.UserRepository;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LearningRequestController {

    private final LearningRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public LearningRequestController(
            LearningRequestRepository requestRepository,
            UserRepository userRepository,
            SkillRepository skillRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    @PostMapping("/requests/send")
    public String sendRequest(
            Authentication authentication,
            @RequestParam Long receiverId,
            @RequestParam Long skillId,
            @RequestParam(required = false) String message) {

        User requester = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        User receiver = userRepository.findById(receiverId)
                .orElseThrow();

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow();

        if (requester.getUserId().equals(receiver.getUserId())) {
            return "redirect:/partners";
        }

        boolean duplicate = requestRepository
                .findByRequester_UserId(requester.getUserId())
                .stream()
                .anyMatch(r ->
                        r.getReceiver().getUserId().equals(receiverId)
                                && r.getSkill().getSkillId().equals(skillId)
                                && "PENDING".equals(r.getStatus()));

        if (duplicate) {
            return "redirect:/partners?error=duplicate";
        }

        LearningRequest request = new LearningRequest();
        request.setRequester(requester);
        request.setReceiver(receiver);
        request.setSkill(skill);
        request.setMessage(message);
        request.setStatus("PENDING");

        requestRepository.save(request);

        return "redirect:/partners?success=requestSent";
    }

    @GetMapping("/requests")
    public String viewRequests(
            Authentication authentication,
            Model model) {

        User currentUser = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();
        model.addAttribute("currentUser", currentUser);

        model.addAttribute("incomingRequests",
                requestRepository.findByReceiver_UserIdAndStatus(
                        currentUser.getUserId(), "PENDING"));

        model.addAttribute("outgoingRequests",
                requestRepository.findByRequester_UserId(
                        currentUser.getUserId()));

        List<LearningRequest> acceptedRequests =
                new java.util.ArrayList<>();

        acceptedRequests.addAll(
                requestRepository.findByReceiver_UserIdAndStatus(
                        currentUser.getUserId(), "ACCEPTED"));

        acceptedRequests.addAll(
                requestRepository.findByRequester_UserIdAndStatus(
                        currentUser.getUserId(), "ACCEPTED"));

        model.addAttribute("acceptedRequests", acceptedRequests);
        return "learning-requests";
    }

    @PostMapping("/requests/{requestId}/accept")
    public String acceptRequest(
            Authentication authentication,
            @PathVariable Long requestId) {

        User currentUser = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        LearningRequest request = requestRepository.findById(requestId)
                .orElseThrow();

        if (!request.getReceiver().getUserId()
                .equals(currentUser.getUserId())
                || !"PENDING".equals(request.getStatus())) {
            return "redirect:/requests";
        }

        request.setStatus("ACCEPTED");
        requestRepository.save(request);

        return "redirect:/requests";
    }

    @PostMapping("/requests/{requestId}/decline")
    public String declineRequest(
            Authentication authentication,
            @PathVariable Long requestId) {

        User currentUser = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        LearningRequest request = requestRepository.findById(requestId)
                .orElseThrow();

        if (!request.getReceiver().getUserId()
                .equals(currentUser.getUserId())
                || !"PENDING".equals(request.getStatus())) {
            return "redirect:/requests";
        }

        request.setStatus("DECLINED");
        requestRepository.save(request);

        return "redirect:/requests";
    }
}
