package tn.esprit.spring.ARMP.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataDTO {
    private String type; // LINE, BAR, PIE, RADAR, HEATMAP
    private String title;
    private List<String> labels;
    private List<DatasetDTO> datasets;
    private Map<String, Object> options;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatasetDTO {
        private String label;
        private List<Double> data;
        private String backgroundColor;
        private String borderColor;
        private Double borderWidth;
        private String fill;
    }
}