package com.example.voiture.controllers;

import com.example.voiture.clients.ClientService;
import com.example.voiture.entities.Voiture;
import com.example.voiture.model.Client;
import com.example.voiture.repositories.VoitureRepository;
import com.example.voiture.services.VoitureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VoitureController {

    private final VoitureRepository voitureRepository;
    private final VoitureService voitureService;
    private final ClientService clientService;

    public VoitureController(VoitureRepository voitureRepository,
                             VoitureService voitureService,
                             ClientService clientService) {
        this.voitureRepository = voitureRepository;
        this.voitureService = voitureService;
        this.clientService = clientService;
    }

    @GetMapping(value = "/voitures", produces = "application/json")
    public ResponseEntity<Object> findAll() {
        try {
            return ResponseEntity.ok(voitureService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching voitures: " + e.getMessage());
        }
    }

    @GetMapping("/voitures/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return voitureService.findById(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Voiture not found with ID: " + id));
    }

    @GetMapping("/voitures/client/{id}")
    public ResponseEntity<List<Voiture>> findByClient(@PathVariable Long id) {
        try {
            Client client = clientService.clientById(id);
            if (client == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(voitureService.findByClient(id));
        } catch (feign.FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/voitures/{clientId}")
    public ResponseEntity<Object> save(@PathVariable Long clientId, @RequestBody Voiture voiture) {
        try {
            Client client = clientService.clientById(clientId);
            if (client == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Client not found with ID: " + clientId);
            }
            voiture.setId(null);
            voiture.setClientId(clientId);
            Voiture saved = voitureService.enregistrerVoiture(voiture);
            saved.setClient(client);
            return ResponseEntity.ok(saved);
        } catch (feign.FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Client not found with ID: " + clientId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving voiture: " + e.getMessage());
        }
    }

    @PutMapping("/voitures/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody Voiture updated) {
        return voitureRepository.findById(id).<ResponseEntity<Object>>map(existing -> {
            if (updated.getMatricule() != null && !updated.getMatricule().isEmpty()) {
                existing.setMatricule(updated.getMatricule());
            }
            if (updated.getMarque() != null && !updated.getMarque().isEmpty()) {
                existing.setMarque(updated.getMarque());
            }
            if (updated.getModel() != null && !updated.getModel().isEmpty()) {
                existing.setModel(updated.getModel());
            }
            return ResponseEntity.ok(voitureRepository.save(existing));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Voiture not found with ID: " + id));
    }

    @DeleteMapping("/voitures/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!voitureRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        voitureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
