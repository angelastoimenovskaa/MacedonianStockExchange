package program.program.Service;

import java.util.Map;

public interface MLPredictionService {
    Map<String, Object> trainAndPredict(String issuerName);
    Map<String, Double> getMetrics(String issuerName);
    void savePredictions(String issuerName, Object predictions);
}
