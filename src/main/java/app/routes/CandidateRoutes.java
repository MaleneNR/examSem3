package app.routes;

import app.controllers.CandidateController;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class CandidateRoutes { private EntityManagerFactory emf;
    private CandidateController candidateController;

    public CandidateRoutes(EntityManagerFactory emf){
        this.emf = emf;
        this.candidateController = new CandidateController(emf);
    }

    public EndpointGroup getRoutes(){
        return () -> {
            get("/", candidateController.getAllAndByCategory());                //role.anyone
            get("/{id}", candidateController.getById()); //include skills       //role.user
            post("/", candidateController.create());                            //role.admin
            put("/{id}", candidateController.update());                         //role.admin
            delete("/{id}", candidateController.delete());                      //role.admin
            put("/{candidateId}/skills/{skillId}", candidateController.linkSkillToCandidate); //role.admin
        };
    }
}
