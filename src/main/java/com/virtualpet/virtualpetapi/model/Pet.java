package com.virtualpet.virtualpetapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    // Atributos de estado (Rango 0-100)
    private int hunger;
    private int energy;
    private int happiness;
    private int health;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public void feed() {
        // Al alimentar: baja el hambre y sube salud/energía
        this.hunger = Math.max(0, this.hunger - 25);
        this.energy = Math.min(100, this.energy + 10);
        this.health = Math.min(100, this.health + 5);
    }

    public void play() {
        // Para jugar debe tener energía y no estar famélico (hunger < 90)
        if (this.energy >= 15 && this.hunger < 90) {
            this.energy = Math.max(0, this.energy - 15);
            this.happiness = Math.min(100, this.happiness + 25);
            this.hunger = Math.min(100, this.hunger + 15); // Jugar da más hambre
        } else {
            // Si está muy cansado o hambriento, pierde felicidad y un poco de salud
            this.happiness = Math.max(0, this.happiness - 10);
            this.health = Math.max(0, this.health - 5);
        }
    }

    public void sleep() {
        // Dormir recupera energía al máximo pero aumenta el hambre
        this.energy = 100;
        this.health = Math.min(100, this.health + 15);
        this.hunger = Math.min(100, this.hunger + 20); // Se despierta con hambre
    }
}