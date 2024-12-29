from transformers import pipeline
import pandas as pd
import numpy as np

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
data
