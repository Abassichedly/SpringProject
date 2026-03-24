package tn.purebillion.spring.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySuggestionDTO {
    private String titre;
    private String type;
    private String description;
    private String lieuSuggere;
    private String heureSuggeree;
    private Integer dureeEstimee;
    private Integer participantsEstimes;
    private Double scoreConfiance;
    private List<String> tags;
    private List<String> motsCles;
    private String raison;
    private LocalDateTime dateSuggestion;
}