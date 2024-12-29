package program.program.Service.Impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jep.JepConfig;
import jep.SubInterpreter;
import org.springframework.stereotype.Service;
import program.program.Service.TechnicalAnalysisService;

import java.util.Map;

@Service
public class TechnicalAnalysisServiceImpl implements TechnicalAnalysisService {
    private SubInterpreter interpreter;

    @PostConstruct
    public void initializePythonEnvironment() {
        try {
            JepConfig config = new JepConfig();
            interpreter = new SubInterpreter(config);

            interpreter.eval("import pandas as pd");
            interpreter.eval("import numpy as np");
            interpreter.eval("from ta import add_all_ta_features");
            interpreter.eval("from ta.utils import dropna");
            interpreter.eval("from ta.volatility import BollingerBands");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Python environment", e);
        }
    }

    public Map<String, Object> analyzeStock(String issuerName) {
        try {
            interpreter.set("issuer_name", issuerName);

            interpreter.eval("""
                data = pd.read_csv(f"data/{issuer_name}_data.csv", sep=",", thousands=",")
                data = data.drop(columns="Date")
                
                # Calculate rolling averages
                dailyrolling = data.rolling(1).mean()
                weeklyrolling = data.rolling(7).mean()
                monthlyrolling = data.rolling(30).mean()
                
                # Calculate RSI
                change = data["Price"].diff()
                change.dropna(inplace=True)
                change_up = change.copy()
                change_down = change.copy()
                
                change_up[change_up<0] = 0
                change_down[change_down>0] = 0
                
                avg_up = change_up.rolling(14).mean()
                avg_down = change_down.rolling(14).mean().abs()
                
                rsi = 100 * avg_up / (avg_up + avg_down)
                
                result = {
                    'daily_ma': dailyrolling.to_dict('records'),
                    'weekly_ma': weeklyrolling.dropna().to_dict('records'),
                    'monthly_ma': monthlyrolling.dropna().to_dict('records'),
                    'rsi': rsi.dropna().tolist()
                }
            """);

            return (Map<String, Object>) interpreter.getValue("result");
        } catch (Exception e) {
            throw new RuntimeException("Failed to perform technical analysis", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        if (interpreter != null) {
            interpreter.close();
        }
    }
}
