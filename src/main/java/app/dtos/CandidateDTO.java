package app.dtos;

import app.entities.Candidate;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateDTO {
    private Integer id;
    private String name;
    private String phone;
    private String educationBackground;
    private Set<SkillDTO> skills;

    public CandidateDTO(Candidate candidate) {
        this.id = candidate.getCandidateId();
        this.name = candidate.getName();
        this.phone = candidate.getPhone();
        this.educationBackground = candidate.getEducationBackground();

        if (candidate.getSkills() != null) {
            this.skills = candidate.getSkills()
                    .stream()
                    .map(skill -> new SkillDTO(skill))
                    .collect(Collectors.toSet());
        }
    }
}

