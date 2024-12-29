package program.program.Service;

import java.util.List;
import java.util.Map;

public interface SentimentAnalysisService {
    List<Map<String, String>> analyzeSentiment(List<String> news);
}
