
package SkillBridge.repository;

import SkillBridge.model.LearningRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearningRequestRepository
        extends JpaRepository<LearningRequest, Long> {

    List<LearningRequest> findByReceiver_UserId(Long userId);

    List<LearningRequest> findByRequester_UserId(Long userId);

    List<LearningRequest> findByReceiver_UserIdAndStatus(
            Long userId, String status);

    List<LearningRequest> findByRequester_UserIdAndStatus(
            Long userId, String status);
}
