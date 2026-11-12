package com.example.client.controllers;

import com.example.client.entities.Client;
import com.example.client.repositories.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/clients")
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    // /client/{id} (énoncé du lab) et /clients/{id} (alias RESTful)
    @GetMapping({"/client/{id}", "/clients/{id}"})
    public Client findById(@PathVariable Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé"));
    }

    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@RequestBody Client client) {
        client.setId(null);
        return clientRepository.save(client);
    }
}
