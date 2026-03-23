package tn.purebillion.spring.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.purebillion.spring.demo.enums.StatutActivite;
import tn.purebillion.spring.demo.enums.TypeActivite;

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
@ToString(exclude = {"club", "participations"})
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

    @ManyToOne
    @JoinColumn(name = "club_id")
    Club club;

    @OneToMany(mappedBy = "activite", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Participation> participations = new ArrayList<>();
}