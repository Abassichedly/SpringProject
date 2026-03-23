package tn.purebillion.spring.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.purebillion.spring.demo.enums.StatutEvent;

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

    @ManyToOne
    @JoinColumn(name = "club_id")
    Club club;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    List<Participation> participations = new ArrayList<>();
}