package tn.esprit.spring.ARMP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.ARMP.enums.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String passwordHash;

    @Column(nullable = false)
    String phone;

    @Column
    String department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserRole role;

    @Column(nullable = false)
    Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column
    LocalDateTime lastLoginAt;

    @Column(nullable = false)
    Integer totalLoginCount = 0;

    @Column
    String preferredLanguage;

    @Column(nullable = false)
    Boolean isTwoFactorEnabled = false;

    @Column
    String googleId;

    @Column
    String linkedInId;

    @Column
    LocalDateTime scheduledDeletionAt;

    // Champs pour la gestion des clubs
    LocalDate dateAdhesion;

    // Relation UNIDIRECTIONNELLE avec Club
    @ManyToOne
    @JoinColumn(name = "club_id")
    Club club;

    // PAS de relation directe avec Participation pour éviter la boucle
    // Les participations sont gérées via userId dans Participation

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    List<MembreTag> tags = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        dateAdhesion = LocalDate.now();
    }
}