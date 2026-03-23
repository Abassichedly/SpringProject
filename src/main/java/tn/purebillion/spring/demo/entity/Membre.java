package tn.purebillion.spring.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.purebillion.spring.demo.entity.Club;
import tn.purebillion.spring.demo.entity.Participation;
import tn.purebillion.spring.demo.enums.RoleMembre;

import java.time.LocalDate;
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
public class Membre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idMembre;

    @Column(nullable = false)
    String nom;

    @Column(nullable = false)
    String prenom;

    @Column(unique = true, nullable = false)
    String email;

    @Enumerated(EnumType.STRING)
    RoleMembre role;

    LocalDate dateAdhesion;

    Boolean estActif;

    @ManyToOne
    @JoinColumn(name = "club_id")
    Club club;

    @OneToMany(mappedBy = "membre", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Participation> participations = new ArrayList<>();
}