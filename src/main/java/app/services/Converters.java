package app.services;

import app.dtos.CandidateDTO;
import app.dtos.SkillDTO;
import app.entities.Candidate;
import app.entities.Skill;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Converters {

    public Candidate convertCandidateDTOToCandidate(CandidateDTO candidateDTO){
        Set<Skill> skills = new HashSet<>();
        if (candidateDTO.getSkills()!=null){
            skills = candidateDTO.getSkills().stream().map(skillDTO -> convertSkillDTOToSkill(skillDTO)).collect(Collectors.toSet());
        }

        Candidate candidate = Candidate.builder()
                .name(candidateDTO.getName())
                .phone(candidateDTO.getPhone())
                .educationBackground(candidateDTO.getEducationBackground())
                .skills(skills)
                .build();
        return candidate;
    }

    public Skill convertSkillDTOToSkill(SkillDTO skillDTO) {
        if (skillDTO == null) return null;

        Skill skill = Skill.builder()
                .skillId(skillDTO.getId())
                .name(skillDTO.getName())
                .category(skillDTO.getCategory())
                .description(skillDTO.getDescription())
                .build();

        if (skillDTO.getCandidateIds() != null) {
            Set<Candidate> candidates = skillDTO.getCandidateIds().stream()
                    .map(id -> {
                        Candidate candidate = new Candidate();
                        candidate.setCandidateId(id);
                        return candidate;
                    })
                    .collect(Collectors.toSet());
            skill.setCandidates(candidates);
        }

        return skill;
    }


}
