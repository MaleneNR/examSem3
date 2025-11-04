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
    @ManyToMany
    @JoinTable (name = "CandidateSkill")
    private Set<Skill> skills = new HashSet<>();

    //bi-directional
    public void addSkill(Skill skill) {
        this.skills.add(skill);
        skill.getCandidates().add(this);
    }

    //We could add a removeSkill(), but let's say they will always have the skill there have been added
}
