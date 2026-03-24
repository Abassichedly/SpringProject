package tn.purebillion.spring.demo.entity;

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
    @JoinColumn(name = "membre_id")
    @JsonIgnore
    Membre membre;

    String tag;

    Integer poids;

    LocalDateTime dateAjout;

    @PrePersist
    protected void onCreate() {
        dateAjout = LocalDateTime.now();
    }
}