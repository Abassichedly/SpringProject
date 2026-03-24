package tn.purebillion.spring.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idInteraction;

    @ManyToOne
    @JoinColumn(name = "membre_source_id")
    @JsonIgnore
    Membre membreSource;

    @ManyToOne
    @JoinColumn(name = "membre_cible_id")
    @JsonIgnore
    Membre membreCible;

    String type; // LIKE, COMMENT, COLLABORATION, MENTION

    String contenu;

    LocalDateTime dateInteraction;

    Integer poids; // 1-10 pour l'algorithme de recommandation
}