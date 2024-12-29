import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MinMaxScaler
from tensorflow import keras
from keras.src.models import Sequential
from keras.src.layers import Input
from keras.src.layers import LSTM, Dense

from sklearn.metrics import r2_score, mean_squared_error, root_mean_squared_error, mean_absolute_error

issuer = input("Enter the issuer name: ")

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
    Input((lag, (train_X.shape[1] // lag))), # (timesteps, features)
    LSTM(64, activation="relu", return_sequences=True),
    LSTM(32, activation="relu"),
    Dense(1, activation="linear")
])

model.compile(
    loss="mean_squared_error",
    optimizer="adam",
    metrics=["mean_squared_error"],
)

history = model.fit(train_X, train_y, validation_split=0.2, epochs=64, batch_size=8)
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
predictions.to_csv( 'predictions/' + issuer + 'predictions.csv', index=False)


