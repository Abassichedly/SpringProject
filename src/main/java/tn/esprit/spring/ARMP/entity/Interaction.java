package tn.esprit.spring.ARMP.entity;

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
    @JoinColumn(name = "user_source_id")
    @JsonIgnore
    User userSource;

    @ManyToOne
    @JoinColumn(name = "user_cible_id")
    @JsonIgnore
    User userCible;

    String type;

    String contenu;

    LocalDateTime dateInteraction;

    Integer poids;
}