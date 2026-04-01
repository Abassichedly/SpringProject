package tn.esprit.spring.ARMP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.ARMP.enums.StatutPresence;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idParticipation;

    LocalDate dateInscription;

    @Enumerated(EnumType.STRING)
    StatutPresence statutPresence;

    String role;

    // Stocke l'ID de l'utilisateur (String) - PAS DE RELATION DIRECTE
    @Column(name = "user_id")
    String userId;

    // Relation UNIDIRECTIONNELLE avec Activite
    @ManyToOne
    @JoinColumn(name = "activite_id")
    @JsonIgnore
    Activite activite;

    // Relation UNIDIRECTIONNELLE avec Event
    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonIgnore
    Event event;

    @Transient
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getId();
        }
    }
}