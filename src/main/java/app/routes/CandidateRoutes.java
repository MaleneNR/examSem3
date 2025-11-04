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
            get("/", candidateController.getAllAndByCategory());
            get("/{id}", candidateController.getById()); //TODO SHOULD INCLUDE SKILLS
            post("/", candidateController.create());
            put("/{id}", candidateController.update());
            delete("/{id}", candidateController.delete());
            //put("/{candidateId}/skills/{skillId}", ); //TODO SKAL LAVES OM TIL EGEN FUNKTION
        };
    }
}
