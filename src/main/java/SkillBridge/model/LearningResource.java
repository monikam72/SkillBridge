
package SkillBridge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "learning_resources")
public class LearningResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long resourceId;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String topic;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    private String difficulty;

    private String description;

    public LearningResource() {}

    public Long getResourceId() {
        return resourceId;
    }

    public Skill getSkill() {
        return skill;
    }

    public String getTitle() {
        return title;
    }

    public String getTopic() {
        return topic;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getDescription() {
        return description;
    }
}
