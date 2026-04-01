package tn.esprit.spring.ARMP.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.spring.ARMP.entity.User;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorMatchDTO {
    private User mentor;
    private User mentore;
    private Double matchScore;
    private String matchLevel;
    private List<String> commonInterests;
    private List<String> complementarySkills;
    private Map<String, Double> scoreBreakdown;
    private List<String> recommendations;
}