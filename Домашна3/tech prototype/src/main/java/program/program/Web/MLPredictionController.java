package program.program.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import program.program.Service.MLPredictionService;

import java.util.Map;

@RestController
@RequestMapping("/api/ml")
public class MLPredictionController {
    private final MLPredictionService mlPredictionService;

    @Autowired
    public MLPredictionController(MLPredictionService mlPredictionService) {
        this.mlPredictionService = mlPredictionService;
    }

    @PostMapping("/predict/{issuerName}")
    public ResponseEntity<Map<String, Object>> predictPrice(@PathVariable String issuerName) {
        return ResponseEntity.ok(mlPredictionService.trainAndPredict(issuerName));
    }

    @GetMapping("/metrics/{issuerName}")
    public ResponseEntity<Map<String, Double>> getMetrics(@PathVariable String issuerName) {
        return ResponseEntity.ok(mlPredictionService.getMetrics(issuerName));
    }
}