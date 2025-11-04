package app.routes;

import app.dtos.CandidateDTO;
import app.services.Populator;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
    private EntityManagerFactory emf;
    private CandidateRoutes candidateRoutes;

    public Routes(EntityManagerFactory emf){
        this.emf = emf;
        candidateRoutes = new CandidateRoutes(emf);
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", context -> context.json("Api is running").status(200));
            get("/populate", Populator.populate());
            path("/candidates", candidateRoutes.getRoutes());
        };
    }
}
