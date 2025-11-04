package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString

@Entity
public class Skill {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer skillId;
    private String name;
    private Category category;
    private String description;

    @Transient
    @Setter//Gem ikke i db
    private Integer popularityScore;
    @Transient
    @Setter
    private Integer averageSalary;

    @ManyToMany (mappedBy = "skills", fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Candidate> candidates = new HashSet<>();

    public Skill(String name, Category category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public void setCandidates(Set<Candidate> candidates) {
        this.candidates = candidates;
    }
}
