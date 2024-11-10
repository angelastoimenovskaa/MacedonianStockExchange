import requests
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

issuers = fetch_issuers()

save_issuers_to_file(issuers, "issuers.txt")

print("Issuers saved to 'issuers.txt'.")