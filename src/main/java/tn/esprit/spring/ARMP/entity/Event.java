package tn.esprit.spring.ARMP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.ARMP.enums.StatutEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idEvent;

    String nom;

    LocalDateTime dateDebut;

    LocalDateTime dateFin;

    String lieu;

    Integer capacite;

    Double prixAdherent;

    Double prixNonAdherent;

    @Column(columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    StatutEvent statut;

    // Relation UNIDIRECTIONNELLE avec Club
    @ManyToOne
    @JoinColumn(name = "club_id")
    Club club;

    // PAS de relation directe avec Participation pour éviter la boucle
    // Les participations sont gérées via eventId dans Participation

    @Transient
    private List<Participation> participations = new ArrayList<>();

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }
}