package tn.esprit.spring.ARMP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.ARMP.enums.DomaineClub;
import tn.esprit.spring.ARMP.enums.StatutClub;

import java.time.LocalDate;
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
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idClub;

    @Column(unique = true, nullable = false)
    String nom;

    String sigle;

    @Column(columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    DomaineClub domaine;

    LocalDate dateCreation;

    @Enumerated(EnumType.STRING)
    StatutClub statut;

    String logo;

    String siteWeb;

    Integer nbMembresMax;

    // Relation avec User - UNIDIRECTIONNELLE (pas de mappedBy)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "club_id")
    @JsonIgnore
    List<User> users = new ArrayList<>();

    // Relation avec Activite - UNIDIRECTIONNELLE
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "club_id")
    @JsonIgnore
    List<Activite> activites = new ArrayList<>();

    // Relation avec Event - UNIDIRECTIONNELLE
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "club_id")
    @JsonIgnore
    List<Event> events = new ArrayList<>();
}