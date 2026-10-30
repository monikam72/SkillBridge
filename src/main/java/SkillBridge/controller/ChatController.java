
package SkillBridge.controller;

import SkillBridge.model.ChatMessage;
import SkillBridge.model.LearningRequest;
import SkillBridge.model.User;
import SkillBridge.repository.ChatMessageRepository;
import SkillBridge.repository.LearningRequestRepository;
import SkillBridge.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final LearningRequestRepository requestRepository;
    private final UserRepository userRepository;

    public ChatController(
            ChatMessageRepository chatMessageRepository,
            LearningRequestRepository requestRepository,
            UserRepository userRepository) {

        this.chatMessageRepository = chatMessageRepository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow();
    }

    private LearningRequest getAuthorizedRequest(
            Long requestId, User currentUser) {

        LearningRequest request = requestRepository.findById(requestId)
                .orElseThrow();

        boolean isRequester = request.getRequester()
                .getUserId().equals(currentUser.getUserId());

        boolean isReceiver = request.getReceiver()
                .getUserId().equals(currentUser.getUserId());

        if (!isRequester && !isReceiver) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to access this chat.");
        }

        if (!"ACCEPTED".equals(request.getStatus())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Chat is available only for accepted requests.");
        }

        return request;
    }

    @GetMapping("/chat/{requestId}")
    public String openChat(
            @PathVariable Long requestId,
            Authentication authentication,
            Model model) {

        User currentUser = getCurrentUser(authentication);

        LearningRequest request =
                getAuthorizedRequest(requestId, currentUser);

        model.addAttribute("request", request);

        model.addAttribute("messages",
                chatMessageRepository
                        .findByLearningRequest_RequestIdOrderBySentAtAsc(
                                requestId));

        model.addAttribute("currentUser", currentUser);

        return "chat";
    }

    @PostMapping("/chat/{requestId}/send")
    public String sendMessage(
            @PathVariable Long requestId,
            @RequestParam String content,
            Authentication authentication) {

        User currentUser = getCurrentUser(authentication);

        LearningRequest request =
                getAuthorizedRequest(requestId, currentUser);

        String cleanedContent = content.trim();

        if (cleanedContent.isEmpty() || cleanedContent.length() > 2000) {
            return "redirect:/chat/" + requestId;
        }

        ChatMessage message = new ChatMessage();
        message.setLearningRequest(request);
        message.setSender(currentUser);
        message.setContent(cleanedContent);

        chatMessageRepository.save(message);

        return "redirect:/chat/" + requestId;
    }
}
