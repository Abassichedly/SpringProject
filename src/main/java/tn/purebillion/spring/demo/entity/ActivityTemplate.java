package tn.purebillion.spring.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idTemplate;

    @Column(nullable = false)
    String titre;

    String type; // ATELIER, CONFERENCE, FORMATION, REUNION

    @Column(columnDefinition = "TEXT")
    String description;

    String dureeEstimee; // "2h", "1h30", etc.

    Integer nbParticipantsMin;
    Integer nbParticipantsMax;

    String materielNecessaire;

    String tags; // Stocké comme "ROBOTIQUE,IA,PROGRAMMATION"

    Integer popularite; // Score de popularité (0-100)

    String niveau; // DEBUTANT, INTERMEDIAIRE, EXPERT

    Boolean actif;
}