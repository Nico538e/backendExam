package app.routes;

import app.controllers.CandidateController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class CandidateRoutes {
    private final CandidateController candidateController = new CandidateController();

    public EndpointGroup getCandidateRoutes() {
        return () -> {
            get("/", candidateController::getAll);
            get("/{id}", candidateController::getById);
            post("/", candidateController::create);
            put("/{id}", candidateController::update);
            delete("/{id}", candidateController::delete);
            put("/{candidateId}/skills/{skillId}", candidateController::attachSkillToCandidate);
        };
    }
}
