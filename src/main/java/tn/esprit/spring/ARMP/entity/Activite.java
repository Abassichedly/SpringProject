package tn.esprit.spring.ARMP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.ARMP.enums.StatutActivite;
import tn.esprit.spring.ARMP.enums.TypeActivite;

import java.time.LocalDate;
import java.time.LocalTime;
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
public class Activite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idActivite;

    @Column(nullable = false)
    String titre;

    @Enumerated(EnumType.STRING)
    TypeActivite type;

    LocalDate date;

    LocalTime heureDebut;

    LocalTime heureFin;

    String lieu;

    Integer nbParticipantsMax;

    @Column(columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    StatutActivite statut;

    // Relation UNIDIRECTIONNELLE avec Club
    @ManyToOne
    @JoinColumn(name = "club_id")
    Club club;

    // PAS de relation directe avec Participation pour éviter la boucle
    // Les participations sont gérées via activiteId dans Participation

    @Transient
    private List<Participation> participations = new ArrayList<>();

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }
}