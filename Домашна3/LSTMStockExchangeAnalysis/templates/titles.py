import requests
from bs4 import BeautifulSoup


def get_mse_news_titles(url):
    # Send GET request to MSE news page
    response = requests.get(url)

    if response.status_code == 200:
        soup = BeautifulSoup(response.text, 'html.parser')
        titles = []

        # Adjust the selector based on the actual structure of the MSE website
        news_items = soup.find_all('div', class_='news-item')  # Modify this according to MSE's HTML structure

        for item in news_items:
            title = item.find('h3')  # Modify based on actual HTML tags
            if title:
                titles.append(title.get_text())
        return titles
    else:
        print(f"Error fetching MSE news. Status code: {response.status_code}")
        return []


# Example usage:
mse_url = "MSE_NEWS_PAGE_URL"  # Replace with the actual MSE news page URL
news_titles = get_mse_news_titles(mse_url)

# Display the titles
if news_titles:
    print(f"News titles from Macedonian Stock Exchange:")
    for i, title in enumerate(news_titles, 1):
        print(f"{i}. {title}")
else:
    print("No news found.")
