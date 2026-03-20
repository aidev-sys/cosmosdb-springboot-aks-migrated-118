package com.azure.cosmosdb.demo;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

class CosmosData {

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

interface CosmosDataRepository {
    List<CosmosData> findByDataContaining(String data);
}

@Configuration
@EnableConfigurationProperties(CosmosProperties.class)
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