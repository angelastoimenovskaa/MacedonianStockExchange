from fastapi import FastAPI
import pandas as pd
from ta import add_all_ta_features
from ta.utils import dropna
from ta.volatility import BollingerBands
import numpy as np
from transformers import pipeline
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
from tensorflow import keras
from keras.src.models import Sequential
from keras.src.layers import Input
from keras.src.layers import LSTM, Dense
from sklearn.metrics import r2_score, mean_squared_error, root_mean_squared_error, mean_absolute_error

app = FastAPI()

@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.get("/hello/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}

@app.get("/basic/{issuer_name}")
async def basic(issuer_name: str):
    issuer = issuer_name

    data = pd.read_csv("./data/" + issuer + "_data.csv", sep=",", thousands=",")

    data = data.drop(columns="Date")

    dailyrolling = data.rolling(1).mean()
    weeklyrolling = data.rolling(7).mean()
    monthlyrolling = data.rolling(30).mean()

    weeklyrolling = weeklyrolling.dropna()
    monthlyrolling = monthlyrolling.dropna()

    print("Rolling Averages: ")
    print(dailyrolling)
    print(weeklyrolling)
    print(monthlyrolling)

    dailyewm = data.ewm(1).mean()
    weeklyewm = data.ewm(7).mean()
    monthlyewm = data.ewm(30).mean()

    weeklyewm = weeklyewm.dropna()
    monthlyewm = monthlyewm.dropna()

    print("Moving Averages: ")
    print(dailyewm)
    print(weeklyewm)
    print(monthlyewm)

    change = data["Price"].diff()
    change.dropna(inplace=True)

    change_up = change.copy()
    change_down = change.copy()

    change_up[change_up < 0] = 0
    change_down[change_down > 0] = 0

    change.equals(change_up + change_down)

    avg_up = change_up.rolling(14).mean()
    avg_down = change_down.rolling(14).mean().abs()

    rsi = 100 * avg_up / (avg_up + avg_down)

    rsi = rsi.dropna()

    recommendations = []

    for line in rsi:
        if line > 0:
            recommendations.append("Buy")
        elif line < 0:
            recommendations.append("Sell")
        else:
            recommendations.append("Hold")

    return recommendations[len(recommendations) - 1]

@app.get("/sentimentanalysis")
def sentiment_analysis():
    classifier = pipeline("sentiment-analysis")
    analized_data = classifier([
        "ALKALOID AD Skopje honored with four awards: Most Transparent Listed Joint Stock Company selected by the media and by market participants, Stock of the Year, and Good ESG Practices",
        "ALKALOID AD Skopje Registers New Company: ALKALOID ENERGETIKA DOOEL",
        "Alkaloid’s Success Story – Shared with New Pharmacy Students at UKIM",
        "ALKALOID ONCE AGAIN PART OF THE MOST DISTINGUISHED GLOBAL PHARMACEUTICAL EVENT - CPHI",
        "ALKALOID AD Skopje Registers New Company in Kazakhstan",
        "Komercijalna Banka AD Skopje announced plans to launch a takeover offer to acquire Stopanska banka a.d. Bitola for MKD 1.25 billion",
        "Komercijalna Banka AD Skopje cancelled the takeover offer to acquire Stopanska banka a.d. Bitola",
        "Polynickel Holding BV acquired Euronickel Industries DOO from Komercijalna Banka AD Skopje for €40 million",
        "BALFIN shpk. and Komercijalna Banka AD completed the acquisition of 98.83% stake in Tirana Bank Sh.A. from Piraeus Bank S.A",
        "Komercijalna, Stopanska Banka Explore Merger",
        "Makpetrol A.D.'s Equity Buyback announced on July 17, 2018, has expired",
        "Makpetrol A.D. commences an Equity Buyback Plan for 10% of its issued share capital for MKD 200 million, under the authorization approved on June 10, 2019",
        "Makpetrol A.D.'s Equity Buyback announced on August 6, 2019, has expired",
        "Oilko KDA Plans Takeover Bid for Makpetrol",
        "Oilko KDA Seeks Antitrust Nod for Makpetrol Takeover Bid",
        "Komercijalna Banka Skopje : Slovenian bank NLB acquires Serbia's Komercijalna Banka",
        "NLB Banka Announces Board Changes",
        "Makedonski Telekom Skopje : Smart home for modern living, Smart Home solutions of Makedonski Telekom",
        "Makedonski Telekom Skopje : Next Generation Networks must be built in partnerships and in service of the citizens",
        "Makedonski Telekom Skopje : Renovated Telekom shop in Gevgelija",
        "Makedonski Telekom Opens New Concept Store At Headquarters in Skopje",
        "Makedonski Telekom Skopje : The Telekom MK app keeps getting better"])
    data = pd.DataFrame(analized_data)
    data["title"] = [
        "ALKALOID AD Skopje honored with four awards: Most Transparent Listed Joint Stock Company selected by the media and by market participants, Stock of the Year, and Good ESG Practices",
        "ALKALOID AD Skopje Registers New Company: ALKALOID ENERGETIKA DOOEL",
        "Alkaloid’s Success Story – Shared with New Pharmacy Students at UKIM",
        "ALKALOID ONCE AGAIN PART OF THE MOST DISTINGUISHED GLOBAL PHARMACEUTICAL EVENT - CPHI",
        "ALKALOID AD Skopje Registers New Company in Kazakhstan",
        "Komercijalna Banka AD Skopje announced plans to launch a takeover offer to acquire Stopanska banka a.d. Bitola for MKD 1.25 billion",
        "Komercijalna Banka AD Skopje cancelled the takeover offer to acquire Stopanska banka a.d. Bitola",
        "Polynickel Holding BV acquired Euronickel Industries DOO from Komercijalna Banka AD Skopje for €40 million",
        "BALFIN shpk. and Komercijalna Banka AD completed the acquisition of 98.83% stake in Tirana Bank Sh.A. from Piraeus Bank S.A",
        "Komercijalna, Stopanska Banka Explore Merger",
        "Makpetrol A.D.'s Equity Buyback announced on July 17, 2018, has expired",
        "Makpetrol A.D. commences an Equity Buyback Plan for 10% of its issued share capital for MKD 200 million, under the authorization approved on June 10, 2019",
        "Makpetrol A.D.'s Equity Buyback announced on August 6, 2019, has expired",
        "Oilko KDA Plans Takeover Bid for Makpetrol", "Oilko KDA Seeks Antitrust Nod for Makpetrol Takeover Bid",
        "Komercijalna Banka Skopje : Slovenian bank NLB acquires Serbia's Komercijalna Banka",
        "NLB Banka Announces Board Changes",
        "Makedonski Telekom Skopje : Smart home for modern living, Smart Home solutions of Makedonski Telekom",
        "Makedonski Telekom Skopje : Next Generation Networks must be built in partnerships and in service of the citizens",
        "Makedonski Telekom Skopje : Renovated Telekom shop in Gevgelija",
        "Makedonski Telekom Opens New Concept Store At Headquarters in Skopje",
        "Makedonski Telekom Skopje : The Telekom MK app keeps getting better"]
    data["advice"] = np.where(data['label'] == "POSITIVE", "BUY", "SELL")

    data = data.drop(columns="label")
    data = data.drop(columns="score")
    return data

@app.get("/lstm/{issuername}")
def predict(issuername : str):
    issuer = issuername
    data = pd.read_csv("data/" + issuer + "_data.csv", sep=',', thousands=',')

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

    history = model.fit(train_X, train_y, validation_split=0.2, epochs=10, batch_size=8)
    pred_y = model.predict(test_X)
    print("R2 Score: ")
    print(r2_score(test_y, pred_y))
    print("MSE: ")
    print(mean_squared_error(test_y, pred_y))
    print("RMSE: ")
    print(root_mean_squared_error(test_y, pred_y))
    print("MAE: ")
    print(mean_absolute_error(test_y, pred_y))

    predictions = pd.DataFrame(pred_y)
    predictions.to_csv('predictions/' + issuer + 'predictions.csv', index=False)

    predictions = predictions.head(1)
    return predictions.to_dict()


