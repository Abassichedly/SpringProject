package tn.purebillion.spring.demo.entity;

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
public class PredictionScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idPrediction;

    @ManyToOne
    @JoinColumn(name = "club_id")
    Club club;

    String type; // POPULARITE, CROISSANCE, RISQUE_DEPART, SUCCES_EVENT

    Double score;

    Double confiance;

    @Column(columnDefinition = "TEXT")
    String facteurs;

    LocalDateTime datePrediction;

    Boolean estVerifiee;
}