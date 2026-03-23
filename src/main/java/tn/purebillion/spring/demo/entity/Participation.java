package tn.purebillion.spring.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.purebillion.spring.demo.enums.StatutPresence;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"membre", "activite", "event"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idParticipation;

    LocalDate dateInscription;

    @Enumerated(EnumType.STRING)
    StatutPresence statutPresence;

    @ManyToOne
    @JoinColumn(name = "membre_id")
    Membre membre;  // ← PAS de @JsonIgnore (on veut voir le membre dans la participation)

    @ManyToOne
    @JoinColumn(name = "activite_id")
    Activite activite;  // ← PAS de @JsonIgnore (on veut voir l'activité dans la participation)

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
}