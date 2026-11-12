package com.example.voiture.clients;

import com.example.voiture.model.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "SERVICE-CLIENT")
public interface ClientService {

    @GetMapping("/client/{id}")
    Client clientById(@PathVariable("id") Long id);
}
