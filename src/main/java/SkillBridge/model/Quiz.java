
package SkillBridge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long quizId;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "quiz_title", nullable = false)
    private String quizTitle;

    public Quiz() {}

    public Long getQuizId() {
        return quizId;
    }

    public Skill getSkill() {
        return skill;
    }

    public String getQuizTitle() {
        return quizTitle;
    }
}
