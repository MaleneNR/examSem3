package app.controllers;

import app.daos.CandidateDAO;
import app.dtos.CandidateDTO;
import app.entities.Candidate;
import app.entities.Category;
import app.exceptions.ApiException;
import app.exceptions.IllegalInputException;
import app.services.Converters;
import app.services.SkillStatsService;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.validation.BodyValidator;
import jakarta.persistence.Converter;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.stream.Collectors;

public class CandidateController implements IController {
    private EntityManagerFactory emf;
    private CandidateDAO candidateDAO;
    private Converters converters;

    public CandidateController(EntityManagerFactory emf){
        this.emf = emf;
        this.candidateDAO = new CandidateDAO(emf);
        this.converters = new Converters();
    }

    @Override
    public Handler getAllAndByCategory() {
        return ctx -> {
            String category = ctx.queryParam("category");

            if (category == null){
                ctx.status(HttpStatus.OK).json(CandidateDTO.getEntities(candidateDAO.getAll()));
            } else {

                getByCategory(Category.valueOf(category.toUpperCase())).handle(ctx);
            }
        };
    }

    @Override
    public Handler getById() {
        return ctx -> {
            int id = validateAndParseId(ctx.pathParam("id"));
            try {
                Candidate candidate = candidateDAO.getById(id);
                candidate = new SkillStatsService().getSkillStatsForCandidate(candidate); //Getting the skillStats from API
                ctx.status(HttpStatus.OK).json(new CandidateDTO(candidate));
            } catch(NoResultException ex){
                throw new ApiException(404, "No candidate with id: " + id);
            }
        };
    }

    @Override
    public Handler create() {
        return ctx -> {
            BodyValidator<CandidateDTO> validator = ctx.bodyValidator(CandidateDTO.class);
            validator.check(candidateDTO -> candidateDTO.getName() == null,"Trip must contain a name");
            CandidateDTO candidateDTO = ctx.bodyAsClass(CandidateDTO.class);
            Candidate created = candidateDAO.create(converters.convertCandidateDTOToCandidate(candidateDTO));
            ctx.json(new CandidateDTO(created)).status(HttpStatus.CREATED);
        };
    }

    @Override
    public Handler update() {
        return ctx -> {
            int id = validateAndParseId(ctx.pathParam("id"));
            try {
                BodyValidator<CandidateDTO> validator = ctx.bodyValidator(CandidateDTO.class);
                validator.check(candidateDTO -> candidateDTO.getName()==null, "Candidate must contain a name to be updated");
                CandidateDTO candidateDTO = ctx.bodyAsClass(CandidateDTO.class);
                candidateDTO.setId(id);
                candidateDAO.update(converters.convertCandidateDTOToCandidate(candidateDTO));
                ctx.status(200).json(candidateDTO);}
            catch (Exception e){
                e.getMessage();
                ctx.status(400);
                throw new RuntimeException("Trip was not updated correctly");
            }
        };
    }

    @Override
    public Handler delete() {
        return ctx -> {
            int id = validateAndParseId(ctx.pathParam("id"));
            try{
            Candidate found = candidateDAO.getById(id);
            if(found == null)
                throw new ApiException(404, "No trip with that id: " + (id));
            CandidateDTO candidateDTO = new CandidateDTO(found);
            candidateDAO.delete(candidateDTO.getId()); //Tidligere har denne brugt en convert
            ctx.status(204);} catch (Exception e){
                ctx.status(400).result("Candidate was not deleted");
            }

        };
    }

    public Handler getByCategory(Category category) {
        return ctx -> {
            List<Candidate> candidatesSorted = candidateDAO.getAll().stream()
                    .filter(candidate -> candidate.getSkills().stream()
                            .anyMatch(skill -> skill.getCategory().name().equals(category.name())))
                    .collect(Collectors.toList());
            ctx.status(200).json(CandidateDTO.getEntities(candidatesSorted));
        };
    }

    private int validateAndParseId(String idStr){
        if(idStr ==null || idStr.isEmpty()){
            throw new IllegalInputException("Id can not be null or empty");
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            throw new IllegalInputException("Id must be a number/an integer");
        }

        if (id <= 0){
            throw new IllegalInputException("Id must be a positive number");
        }
        return id;
    }
}
