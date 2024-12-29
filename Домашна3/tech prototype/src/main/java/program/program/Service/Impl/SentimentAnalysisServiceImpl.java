package program.program.Service.Impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jep.JepConfig;
import jep.SubInterpreter;
import org.springframework.stereotype.Service;
import program.program.Service.SentimentAnalysisService;

import java.util.List;
import java.util.Map;

@Service
public class SentimentAnalysisServiceImpl implements SentimentAnalysisService {
    private SubInterpreter interpreter;

    @PostConstruct
    public void initializePythonEnvironment() {
        try {
            JepConfig config = new JepConfig();
            interpreter = new SubInterpreter(config);

            interpreter.eval("from transformers import pipeline");
            interpreter.eval("import pandas as pd");
            interpreter.eval("import numpy as np");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Python environment", e);
        }
    }

    public List<Map<String, String>> analyzeSentiment(List<String> news) {
        try {
            interpreter.set("news_list", news);

            interpreter.eval("""
                classifier = pipeline("sentiment-analysis")
                analyzed_data = classifier(news_list)
                data = pd.DataFrame(analyzed_data)
                data["advice"] = np.where(data['label'] == "POSITIVE", "BUY", "SELL")
                result = data.drop(columns=["label", "score"]).to_dict('records')
            """);

            return (List<Map<String, String>>) interpreter.getValue("result");
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze sentiment", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        if (interpreter != null) {
            interpreter.close();
        }
    }
}
