package program.program.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import program.program.Service.MLPredictionService;
import program.program.Service.SentimentAnalysisService;
import program.program.Service.TechnicalAnalysisService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final MLPredictionService mlPredictionService;
    private final SentimentAnalysisService sentimentAnalysisService;
    private final TechnicalAnalysisService technicalAnalysisService;

    @Autowired
    public AnalysisController(
            MLPredictionService mlPredictionService,
            SentimentAnalysisService sentimentAnalysisService,
            TechnicalAnalysisService technicalAnalysisService) {
        this.mlPredictionService = mlPredictionService;
        this.sentimentAnalysisService = sentimentAnalysisService;
        this.technicalAnalysisService = technicalAnalysisService;
    }

    @PostMapping("/sentiment")
    public ResponseEntity<List<Map<String, String>>> analyzeSentiment(@RequestBody List<String> news) {
        return ResponseEntity.ok(sentimentAnalysisService.analyzeSentiment(news));
    }

    @GetMapping("/technical/{issuerName}")
    public ResponseEntity<Map<String, Object>> getTechnicalAnalysis(@PathVariable String issuerName) {
        return ResponseEntity.ok(technicalAnalysisService.analyzeStock(issuerName));
    }

    @GetMapping("/predictions/{issuerName}")
    public ResponseEntity<Map<String, Double>> getPredictions(@PathVariable String issuerName) {
        return ResponseEntity.ok(mlPredictionService.getMetrics(issuerName));
    }

    @GetMapping("/metrics/{issuerName}")
    public ResponseEntity<Map<String, Double>> getMetrics(@PathVariable String issuerName) {
        return ResponseEntity.ok(mlPredictionService.getMetrics(issuerName));
    }
}