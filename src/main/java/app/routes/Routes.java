package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
    private CandidateRoutes candidateRoutes =  new CandidateRoutes();

    public EndpointGroup getRoutes(){
        return () -> {
            get("/", ctx -> ctx.result("Hello api"));

            path("/candidates", candidateRoutes.getCandidateRoutes());
        };
    }
}
