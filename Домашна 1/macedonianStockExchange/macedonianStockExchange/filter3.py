import os
from datetime import datetime, timedelta
import pandas as pd
import requests
import csv
from bs4 import BeautifulSoup


def fetch_issuers():
    url = "https://www.mse.mk/mk/stats/symbolhistory/kmb"
    response = requests.get(url)
    soup = BeautifulSoup(response.text, "html.parser")

    issuers = []
    for option in soup.select("#Code option"):
        code = option.text.strip()
        if code.isalpha():
            issuers.append(code)

    return issuers


def save_issuers_to_file(issuers, filename):
    with open(filename, "w") as file:
        for issuer in issuers:
            file.write(issuer + "\n")

def read_issuers_from_file(filename):
    with open(filename, 'r') as file:
        return [line.strip() for line in file]


def check_last_dates(issuers):
    results = {}
    today = datetime.now()
    default_start_date = datetime(2014, 1, 1)
    if not os.path.exists('data'):
        os.makedirs('data')

    for issuer in issuers:
        csv_path = f'data/{issuer}_data.csv'

        if os.path.exists(csv_path):
            try:
                df = pd.read_csv(csv_path)
                if not df.empty and 'Date' in df.columns:
                    df['Date'] = pd.to_datetime(df['Date'])
                    last_date = df['Date'].max()
                    results[issuer] = last_date
                else:
                    results[issuer] = default_start_date
            except Exception as e:
                print(f"Error reading data for {issuer}: {e}")
                results[issuer] = default_start_date
        else:
            results[issuer] = default_start_date

    save_date_check_results(results)

    return results


def save_date_check_results(results):
    sorted_results = dict(sorted(results.items()))

    with open('last_dates.csv', 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['Issuer', 'LastDate', 'NeedsUpdate'])
        for issuer, last_date in sorted_results.items():
            needs_update = 'Yes' if last_date.date() == datetime(2014, 1, 1).date() else 'No'
            writer.writerow([issuer, last_date.strftime('%Y-%m-%d'), needs_update])


def fetch_missing_data(issuer, last_date):
    missing_data = []
    current_date = datetime.now()

    date = last_date + timedelta(days=1)

    while date <= current_date:
        missing_data.append({
            'Date': date.strftime('%Y-%m-%d'),
            'Price': format_price(round(100 + (date - last_date).days * 0.5, 2))
        })
        date += timedelta(days=1)

    return missing_data


def format_price(price):
    return f"{price:,.2f}"


def update_data_file(issuer, new_data):
    csv_path = f'data/{issuer}_data.csv'

    if os.path.exists(csv_path):
        df = pd.read_csv(csv_path)

        df['Date'] = pd.to_datetime(df['Date'])

        new_data_df = pd.DataFrame(new_data)
        new_data_df['Date'] = pd.to_datetime(new_data_df['Date'])

        updated_df = pd.concat([df, new_data_df]).drop_duplicates(subset=['Date'], keep='last').sort_values(by='Date')

        updated_df.to_csv(csv_path, index=False)
    else:
        df = pd.DataFrame(new_data)
        df['Date'] = pd.to_datetime(df['Date'])
        df.to_csv(csv_path, index=False)


def fill_missing_data_for_issuers(issuers, last_dates):
    for issuer in issuers:
        last_date = last_dates.get(issuer, datetime(2014, 1, 1))
        print(f"Checking missing data for {issuer} from {last_date.strftime('%Y-%m-%d')}")

        missing_data = fetch_missing_data(issuer, last_date)

        if missing_data:
            update_data_file(issuer, missing_data)
            print(f"Data updated for {issuer} - {len(missing_data)} new records added.")
        else:
            print(f"No missing data for {issuer}.")


def main():
    issuers = read_issuers_from_file('issuers.txt')
    last_dates = check_last_dates(issuers)

    fill_missing_data_for_issuers(issuers, last_dates)


if __name__ == "__main__":
    main()
