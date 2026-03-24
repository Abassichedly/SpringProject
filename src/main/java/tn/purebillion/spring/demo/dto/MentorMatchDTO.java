package tn.purebillion.spring.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.purebillion.spring.demo.entity.Membre;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorMatchDTO {
    private Membre mentor;
    private Membre mentore;
    private Double matchScore;
    private String matchLevel;
    private List<String> commonInterests;
    private List<String> complementarySkills;
    private Map<String, Double> scoreBreakdown;
    private List<String> recommendations;
}