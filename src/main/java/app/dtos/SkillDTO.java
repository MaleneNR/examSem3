package app.dtos;

import app.entities.Category;
import app.entities.Skill;
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

    private Set<Integer> candidateIds;

    public SkillDTO(Skill skill) {
        this.id = skill.getSkillId();
        this.name = skill.getName();
        this.category = skill.getCategory();
        this.description = skill.getDescription();

        if (skill.getCandidates() != null) {
            this.candidateIds = skill.getCandidates()
                    .stream()
                    .map(candidate -> candidate.getCandidateId())
                    .collect(Collectors.toSet());
        }
    }
}

