package app.dtos;

import app.entities.Category;
import app.entities.Skill;
import jakarta.persistence.Transient;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillDTO {
    private Integer id;
    private String name;
    private Category category;
    private String description;

    private Integer popularityScore;
    private Integer averageSalary;


    private Set<Integer> candidateIds;

    public SkillDTO(Skill skill) {
        this.id = skill.getSkillId();
        this.name = skill.getName();
        this.category = skill.getCategory();
        this.description = skill.getDescription();

        this.popularityScore = skill.getPopularityScore();
        this.averageSalary = skill.getAverageSalary();

        if (skill.getCandidates() != null) {
            this.candidateIds = skill.getCandidates()
                    .stream()
                    .map(candidate -> candidate.getCandidateId())
                    .collect(Collectors.toSet());
        }
    }
}

