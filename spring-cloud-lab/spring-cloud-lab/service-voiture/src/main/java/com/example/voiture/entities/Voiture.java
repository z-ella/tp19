package com.example.voiture.entities;

import com.example.voiture.model.Client;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Voiture {
    @Id
    @GeneratedValue
    private Long id;
    private String marque;
    private String matricule;
    private String model;
    private Long clientId;

    // Non persisté : alimenté via OpenFeign depuis SERVICE-CLIENT
    @Transient
    private Client client;
}
