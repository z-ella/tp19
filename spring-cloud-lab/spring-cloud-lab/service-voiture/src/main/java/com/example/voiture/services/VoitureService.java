package com.example.voiture.services;

import com.example.voiture.clients.ClientService;
import com.example.voiture.entities.Voiture;
import com.example.voiture.repositories.VoitureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoitureService {

    private static final Logger log = LoggerFactory.getLogger(VoitureService.class);

    private final VoitureRepository voitureRepository;
    private final ClientService clientService;

    public VoitureService(VoitureRepository voitureRepository, ClientService clientService) {
        this.voitureRepository = voitureRepository;
        this.clientService = clientService;
    }

    public List<Voiture> findAll() {
        List<Voiture> voitures = voitureRepository.findAll();
        voitures.forEach(this::attacherClient);
        return voitures;
    }

    public Optional<Voiture> findById(Long id) {
        Optional<Voiture> v = voitureRepository.findById(id);
        v.ifPresent(this::attacherClient);
        return v;
    }

    public List<Voiture> findByClient(Long clientId) {
        List<Voiture> voitures = voitureRepository.findByClientId(clientId);
        voitures.forEach(this::attacherClient);
        return voitures;
    }

    public Voiture enregistrerVoiture(Voiture voiture) {
        return voitureRepository.save(voiture);
    }

    public void delete(Long id) {
        voitureRepository.deleteById(id);
    }

    private void attacherClient(Voiture v) {
        if (v.getClientId() == null) return;
        try {
            v.setClient(clientService.clientById(v.getClientId()));
        } catch (Exception e) {
            log.warn("Impossible de récupérer le client {} : {}", v.getClientId(), e.getMessage());
        }
    }
}
