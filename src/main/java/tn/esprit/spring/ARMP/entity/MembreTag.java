package tn.esprit.spring.ARMP.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class MembreTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idTag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

    String tag;

    Integer poids;

    LocalDateTime dateAjout;

    @PrePersist
    protected void onCreate() {
        dateAjout = LocalDateTime.now();
    }
}