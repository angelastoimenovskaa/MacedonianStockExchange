from flask import Flask, request, render_template
import os
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import r2_score, mean_squared_error, mean_absolute_error
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Input, LSTM, Dense

app = Flask(__name__)

# Ensure directories exist
os.makedirs('predictions', exist_ok=True)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/process', methods=['POST'])
def process():
    issuer = request.form['issuer']
    data_file = f"data/{issuer}_data.csv"

    if not os.path.exists(data_file):
        return f"Data file for {issuer} not found.", 404

    # Load and process the data
    data = pd.read_csv(data_file, sep=',', thousands=',')
    data = data.drop(columns="Date")

    COLUMNS = data.columns
    for column in COLUMNS:
        for i in range(1, 8):
            data[f'{column}_shift_{i}'] = data[column].shift(i)

    data.dropna(inplace=True)

    y = data['Price']
    X = data.drop(columns='Price')

    train_X, test_X, train_y, test_y = train_test_split(X, y, test_size=0.3, shuffle=False)
    scaler = MinMaxScaler()
    train_X = scaler.fit_transform(train_X)
    test_X = scaler.transform(test_X)
    lag = 7
    train_X = train_X.reshape(train_X.shape[0], lag, (train_X.shape[1] // lag))
    test_X = test_X.reshape(test_X.shape[0], lag, (test_X.shape[1] // lag))

    # Define the LSTM model
    model = Sequential([
        Input((lag, (train_X.shape[1] // lag))),  # (timesteps, features)
        LSTM(64, activation="relu", return_sequences=True),
        LSTM(32, activation="relu"),
        Dense(1, activation="linear")
    ])

    model.compile(
        loss="mean_squared_error",
        optimizer="adam",
        metrics=["mean_squared_error"],
    )

    # Train the model
    model.fit(train_X, train_y, validation_split=0.2, epochs=10, batch_size=8)  # Reduced epochs for testing
    pred_y = model.predict(test_X)

    # Metrics
    metrics = {
        "R2 Score": r2_score(test_y, pred_y),
        "MSE": mean_squared_error(test_y, pred_y),
        "MAE": mean_absolute_error(test_y, pred_y),
    }

    # Save predictions
    predictions = pd.DataFrame(pred_y, columns=["Predicted"])
    predictions.to_csv(f'predictions/{issuer}_predictions.csv', index=False)

    return f"""
        <h1>Results for {issuer}</h1>
        <p>R2 Score: {metrics['R2 Score']}</p>
        <p>MSE: {metrics['MSE']}</p>
        <p>MAE: {metrics['MAE']}</p>
        <p>Predictions saved to: <code>predictions/{issuer}_predictions.csv</code></p>
        <a href="/">Go Back</a>
    """

if __name__ == "__main__":
    app.run(debug=True)
