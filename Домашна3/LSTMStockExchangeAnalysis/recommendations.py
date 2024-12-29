
import pandas as pd
from ta import add_all_ta_features
from ta.utils import dropna
from ta.volatility import BollingerBands

issuer = input("Input the issuer name")

data = pd.read_csv("./data/" +  issuer + "_data.csv", sep=",", thousands=",")

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


change_up[change_up<0] = 0
change_down[change_down>0] = 0

change.equals(change_up+change_down)

avg_up = change_up.rolling(14).mean()
avg_down = change_down.rolling(14).mean().abs()

rsi = 100 * avg_up / (avg_up + avg_down)

rsi = rsi.dropna()

recommendations = []

for line in rsi:
 if line >0:
  recommendations.append("Buy")
 elif line <0:
  recommendations.append("Sell")
 else:
  recommendations.append("Hold")

recommendations