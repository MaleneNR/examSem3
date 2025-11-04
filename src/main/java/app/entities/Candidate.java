package app.entities;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString

@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer candidateId;
    private String name;
    private String phone;
    private String educationBackground;
    @ManyToMany (fetch = FetchType.EAGER) //We want skills to be fetch fast and everytime = eager
    @JoinTable (name = "CandidateSkill")
    private Set<Skill> skills = new HashSet<>();

    //bi-directional
    public void addSkill(Skill skill) {
        if(this.skills == null){
            this.skills = new HashSet<>();
        }
        this.skills.add(skill);

        if (skill.getCandidates() == null) {
            skill.setCandidates(new HashSet<>());
        }
        skill.getCandidates().add(this);
    }

    public void setCandidateId(Integer id) {
        this.candidateId = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEducationBackground(String educationBackground) {
        this.educationBackground = educationBackground;
    }

    //We could add a removeSkill(), but let's say they will always have the skill there have been added
}
