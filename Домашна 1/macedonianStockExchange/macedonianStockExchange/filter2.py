import os
from datetime import datetime, timedelta
import pandas as pd
import csv
import sys


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


def main():
    issuers = read_issuers_from_file('issuers.txt')
    last_dates = check_last_dates(issuers)

    print("\nResults of date checking:")
    try:
        for issuer, last_date in sorted(last_dates.items()):
            needs_update = "NEEDS UPDATE" if last_date.date() == datetime(2014, 1, 1).date() else "Up to date"
            print(f"{issuer}: Last available date is {last_date.strftime('%Y-%m-%d')} - {needs_update}")
    except BrokenPipeError:
        sys.exit(0)


if __name__ == "__main__":
    main()
