package app.dtos;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter

public class ResponseDTO {
    private List<SkillStatsDTO> data;
}
