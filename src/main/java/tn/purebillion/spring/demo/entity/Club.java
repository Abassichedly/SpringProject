package tn.purebillion.spring.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.purebillion.spring.demo.enums.DomaineClub;
import tn.purebillion.spring.demo.enums.StatutClub;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"membres", "activites", "events"})
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

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Membre> membres = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Activite> activites = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Event> events = new ArrayList<>();
}