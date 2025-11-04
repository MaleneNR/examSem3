package app.dtos;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SkillStatsDTO {
    private String id;
    private String slug;
    private String name;
    private String categoryKey;
    private String description;
    private int popularityScore;
    private int averageSalary;
    private String updatedAt;
}
