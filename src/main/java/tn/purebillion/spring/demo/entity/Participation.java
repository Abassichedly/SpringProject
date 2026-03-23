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
    Membre membre;

    @ManyToOne
    @JoinColumn(name = "activite_id")
    Activite activite;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
}