package reactive_backend.bootcamp.infrastructure;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactive_backend.bootcamp.application.client.adapter.AbilityClient;
import reactive_backend.bootcamp.application.jpa.adapter.BootcampJpaAdapter;
import reactive_backend.bootcamp.application.jpa.mapper.IBootcampEntityMapper;
import reactive_backend.bootcamp.application.jpa.repository.IBootcampRepository;
import reactive_backend.bootcamp.domain.api.IBootcampServicePort;
import reactive_backend.bootcamp.domain.spi.IAbilityClientPort;
import reactive_backend.bootcamp.domain.spi.IBootcampPersistencePort;
import reactive_backend.bootcamp.domain.usecase.BootcampCase;

@Configuration
@AllArgsConstructor
public class BeanConfiguration {
    private final WebClient.Builder webClientBuilder;
    private final IBootcampEntityMapper bootcampEntityMapper;
    private final IBootcampRepository bootcampRepository;

    @Bean
    public IBootcampServicePort bootcampServicePort() {
        return new BootcampCase(bootcampPersistencePort(), abilityClientPort());
    }

    @Bean
    public IBootcampPersistencePort bootcampPersistencePort() {
        return new BootcampJpaAdapter(bootcampRepository, bootcampEntityMapper);
    }

    @Bean
    public IAbilityClientPort abilityClientPort(){
        return new AbilityClient(webClientBuilder);
    }

    @Bean
    public ApplicationRunner initializer(DatabaseClient client) {
        return args -> client.sql("""
        CREATE TABLE IF NOT EXISTS bootcamp_entity (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            description TEXT,
            CONSTRAINT uk_technology_name UNIQUE (name)
        )
        """).fetch().rowsUpdated().subscribe();
    }
}
