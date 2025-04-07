package reactive_backend.bootcamp.infrastructure.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive_backend.bootcamp.application.http.handler.IBootcampHandler;
import reactive_backend.bootcamp.domain.util.ConstRoute;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BootcampRouter {

    @Bean
    public RouterFunction<ServerResponse> bootcampRoutes(IBootcampHandler bootcampHandler) {
        return route(POST(ConstRoute.BOOTCAMP_REST_ROUTE + ConstRoute.CREATE_BOOTCAMP_REST_ROUTE),
                bootcampHandler::createBootcamp)
                .andRoute(POST(ConstRoute.BOOTCAMP_REST_ROUTE + ConstRoute.LIST_BOOTCAMPS_REST_ROUTE),
                        bootcampHandler::getAllBootcamps);
    }
}
