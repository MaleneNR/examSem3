package app.services;

import app.dtos.ResponseDTO;
import app.entities.Candidate;
import app.dtos.SkillStatsDTO;
import app.entities.Skill;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;

public class SkillStatsService {

    public Candidate getSkillStatsForCandidate(Candidate candidate){
        String skillsStr = "";
        if (candidate.getSkills() != null) {
            skillsStr = candidate.getSkills().stream().map(skill -> skill.getName().toLowerCase()).collect(Collectors.joining(","));
        } else {
        return candidate;}

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //Fetch data from API
        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            //Create a request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://apiprovider.cphbusinessapps.dk/api/v1/skills/stats?slugs="+skillsStr))
                    .GET()
                    .build();

            //Sends request and gets response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ResponseDTO apiResponse = objectMapper.readValue(response.body(), ResponseDTO.class);

                //With a little help:
                for (Skill skill : candidate.getSkills()) {
                    String slug = skill.getName().toLowerCase().replace(" ", "-");


                    apiResponse.getData().stream()
                            .filter(dto -> dto.getSlug().equals(slug))
                            .findFirst()
                            .ifPresent(dto -> {
                                skill.setPopularityScore(dto.getPopularityScore());
                                skill.setAverageSalary(dto.getAverageSalary());
                            });
                }
            }else{
                System.out.println("SkillStatsAPI statusCode:"+response.statusCode());
            }
        }
        catch (Exception e) {
        e.printStackTrace();
    }
        return candidate;
    }
}
