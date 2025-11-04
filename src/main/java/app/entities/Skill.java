package app.entities;

import jakarta.persistence.*;
import lombok.*;

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
    @ManyToMany (mappedBy = "skills")
    private Set<Candidate> candidates;

    public Skill(String name, Category category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }
}
