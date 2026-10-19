
package SkillBridge.repository;

import SkillBridge.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository
        extends JpaRepository<Question, Long> {

    List<Question> findByQuiz_QuizId(Long quizId);
}
