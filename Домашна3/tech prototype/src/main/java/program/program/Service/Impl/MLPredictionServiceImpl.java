package program.program.Service.Impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jep.JepConfig;
import jep.SubInterpreter;
import org.springframework.stereotype.Service;
import program.program.Service.MLPredictionService;

import java.util.Map;

@Service
public class MLPredictionServiceImpl implements MLPredictionService {
    private SubInterpreter interpreter;
    private static final String DATA_PATH = "data/";
    private static final String PREDICTIONS_PATH = "predictions/";

    @PostConstruct
    public void initializePythonEnvironment() {
        try {
            JepConfig config = new JepConfig();
            interpreter = new SubInterpreter(config);

            // Import required Python modules
            interpreter.eval("import pandas as pd");
            interpreter.eval("from sklearn.model_selection import train_test_split");
            interpreter.eval("from sklearn.preprocessing import MinMaxScaler");
            interpreter.eval("from tensorflow import keras");
            interpreter.eval("from keras.src.models import Sequential");
            interpreter.eval("from keras.src.layers import Input, LSTM, Dense");
            interpreter.eval("from sklearn.metrics import r2_score, mean_squared_error, mean_absolute_error");
            interpreter.eval("import numpy as np");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Python environment", e);
        }
    }

    @Override
    public Map<String, Object> trainAndPredict(String issuerName) {
        try {
            interpreter.set("issuer", issuerName);

            interpreter.eval("""
                # Load and prepare data
                data = pd.read_csv(f"data/{issuer}_data.csv", sep=',', thousands=',')
                data = data.drop(columns="Date")
                
                # Feature engineering with shifted columns
                COLUMNS = data.columns
                for column in COLUMNS:
                    for i in range(1, 8):
                        data[f'{column}_shift_{i}'] = data[column].shift(i)
                
                data.dropna(inplace=True)
                
                # Split features and target
                y = data['Price']
                X = data.drop(columns='Price')
                
                # Train-test split
                train_X, test_X, train_y, test_y = train_test_split(X, y, test_size=0.3, shuffle=False)
                
                # Scale the data
                scaler = MinMaxScaler()
                train_X = scaler.fit_transform(train_X)
                test_X = scaler.transform(test_X)
                
                # Reshape for LSTM
                lag = 7
                train_X = train_X.reshape(train_X.shape[0], lag, (train_X.shape[1] // lag))
                test_X = test_X.reshape(test_X.shape[0], lag, (test_X.shape[1] // lag))
                
                # Create and compile model
                model = Sequential([
                    Input((lag, (train_X.shape[1] // lag))),
                    LSTM(64, activation="relu", return_sequences=True),
                    LSTM(32, activation="relu"),
                    Dense(1, activation="linear")
                ])
                
                model.compile(
                    loss="mean_squared_error",
                    optimizer="adam",
                    metrics=["mean_squared_error"]
                )
                
                # Train the model
                history = model.fit(
                    train_X, 
                    train_y, 
                    validation_split=0.2, 
                    epochs=64, 
                    batch_size=8,
                    verbose=0
                )
                
                # Make predictions
                pred_y = model.predict(test_X)
                
                # Store test_y and pred_y for metrics calculation
                result = {
                    'predictions': pred_y.flatten().tolist(),
                    'actual_values': test_y.tolist(),
                    'history': {
                        'loss': history.history['loss'],
                        'val_loss': history.history['val_loss']
                    }
                }
            """);

            Map<String, Object> result = (Map<String, Object>) interpreter.getValue("result");

            // Save predictions to CSV
            savePredictions(issuerName, interpreter.getValue("pred_y"));

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to train model and generate predictions", e);
        }
    }

    @Override
    public Map<String, Double> getMetrics(String issuerName) {
        try {
            interpreter.eval("""
                # Calculate metrics
                metrics = {
                    'r2_score': r2_score(test_y, pred_y),
                    'mse': mean_squared_error(test_y, pred_y),
                    'rmse': np.sqrt(mean_squared_error(test_y, pred_y)),
                    'mae': mean_absolute_error(test_y, pred_y)
                }
            """);

            return (Map<String, Double>) interpreter.getValue("metrics");

        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate metrics", e);
        }
    }

    @Override
    public void savePredictions(String issuerName, Object predictions) {
        try {
            interpreter.set("issuer", issuerName);
            interpreter.set("pred_y", predictions);

            interpreter.eval("""
                predictions_df = pd.DataFrame(pred_y)
                predictions_df.to_csv(f'predictions/{issuer}predictions.csv', index=False)
            """);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save predictions", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        if (interpreter != null) {
            interpreter.close();
        }
    }
}
