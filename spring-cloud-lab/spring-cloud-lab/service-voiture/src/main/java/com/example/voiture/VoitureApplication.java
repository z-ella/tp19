package com.example.voiture;

import com.example.voiture.clients.ClientService;
import com.example.voiture.entities.Voiture;
import com.example.voiture.model.Client;
import com.example.voiture.repositories.VoitureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class VoitureApplication {

    private static final Logger log = LoggerFactory.getLogger(VoitureApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(VoitureApplication.class, args);
    }

    @Bean
    CommandLineRunner initialiserBaseH2(VoitureRepository voitureRepository, ClientService clientService) {
        return args -> {
            try {
                Client c1 = clientService.clientById(1L);
                Client c2 = clientService.clientById(2L);
                log.info("Client 1 : {} ({} ans)", c1.getNom(), c1.getAge());
                log.info("Client 2 : {} ({} ans)", c2.getNom(), c2.getAge());
            } catch (Exception e) {
                log.warn("SERVICE-CLIENT injoignable au démarrage (pas encore enregistré dans Eureka ?) : {}", e.getMessage());
            }
            voitureRepository.save(new Voiture(null, "Toyota", "A 25 333", "Corolla", 1L, null));
            voitureRepository.save(new Voiture(null, "Renault", "B 6 3456", "Megane", 1L, null));
            voitureRepository.save(new Voiture(null, "Peugeot", "A 55 4444", "301", 2L, null));
        };
    }
}
