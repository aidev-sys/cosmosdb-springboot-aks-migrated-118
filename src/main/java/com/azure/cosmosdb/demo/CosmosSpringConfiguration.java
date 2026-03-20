package com.azure.cosmosdb.demo;

import jakarta.persistence.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@Entity
@Table(name = "cosmos_data")
class CosmosData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String data;

    public CosmosData() {}

    public CosmosData(String data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

interface CosmosDataRepository extends JpaRepository<CosmosData, Long> {
    List<CosmosData> findByDataContaining(String data);
}

@Configuration
@EnableConfigurationProperties(CosmosProperties.class)
@EnableJpaRepositories(basePackageClasses = CosmosDataRepository.class)
@PropertySource("classpath:application.properties")
public class CosmosSpringConfiguration {

    private final CosmosProperties properties;

    public CosmosSpringConfiguration(CosmosProperties properties) {
        this.properties = properties;
    }

    @Bean
    public String cosmosDatabaseName() {
        return properties.getDatabase();
    }
}