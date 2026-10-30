
package SkillBridge.repository;

import SkillBridge.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByLearningRequest_RequestIdOrderBySentAtAsc(
            Long requestId);
}
