package program.program.Service;


import java.util.Map;

public interface AnalysisService {
    public String getBasicAnalysis(String issuerName);

    public String getLSTMPrediction(String issuerName);

    public String getSentimentAnalysis();
}
