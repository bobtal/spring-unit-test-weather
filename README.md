# weather
This project is used in the Treehouse workshop on Unit Testing a Spring App. The application is a Spring web app that uses two Google APIs and the forecast.io API.

## Getting Your API Keys
In order to use this application, you'll need to get a couple API keys. Here's how.
### Forecast.io
Go to https://developer.forecast.io/register and register with your email address. After registering, you'll see a page with an example URL that you can use to GET JSON data at an arbitrary latitude and longitude. Clicking that link will show you what the data looks like that our application will receive.

At the bottom of the page, you'll see your unique API key displayed. Copy and paste this into *api.properties* as the value for the property named `weather.api.key`.
### Google
While logged in to a Google account, navigate to https://console.developers.google.com. From the menu, choose **Credentials**, click **Create credentials**, and select **API key**. When asked, choose **Server key**. At this point you'll see your key, which you should copy. Paste this key into *api.properties* as the values for the `geocode.api.key` and `places.api.key` properties.

At this point you need to enable the Geocoding and Places API, so that your API key grants you access to those APIs. Under the menu item **Overview**, you should see a categorized list of Google APIs. Under the **Google Maps** category, enable the following APIs:
 
  - Google Maps Geocoding API, and
  - Google Places API Web Service

## Hidden API keys
The API keys are stored in an api.properties file which should be placed in resources root. You should replace YOUR_API_KEY_HERE with your actual API key and add this file manually to the project.

The file contents should look like this
```
# Timeout (in milliseconds) for 3rd party API requests
api.timeout = 3000

# Weather API
weather.api.name = forecast.io API
weather.api.key = YOUR_API_KEY_HERE
weather.api.host = api.forecast.io

# Weather Icons
clear-day = wi-day-sunny
clear-night = wi-night-clear
rain = wi-rain
snow = wi-snow
sleet = wi-sleet
wind = wi-strong-wind
fog = wi-fog
cloudy = wi-cloudy
partly-cloudy-day = wi-day-cloudy
partly-cloudy-night = wi-night-partly-cloudy

# Google Geocoding API
geocode.api.name = Google Geocode API
geocode.api.key = YOUR_API_KEY_HERE
geocode.api.host = maps.googleapis.com
geocode.api.region = US

# Google Places API
places.api.name = Google Places API
places.api.key = YOUR_API_KEY_HERE
places.api.host = maps.googleapis.com
```

### Additional resources on testing

Integration Tests with HTMLUnit

If you want rich testing of your application, including testing of navigation and form submission, check out this series of blog posts:

1.[Introducing Spring Test MVC HtmlUnit](https://spring.io/blog/2014/03/19/introducing-spring-test-mvc-htmlunit)
2.[Spring MVC Test with HtmlUnit](https://spring.io/blog/2014/03/25/spring-mvc-test-with-htmlunit)
3.[Spring MVC Test with WebDriver](https://spring.io/blog/2014/03/26/spring-mvc-test-with-webdriver)
4.[Spring MVC Test with Geb](https://spring.io/blog/2014/04/15/spring-mvc-test-with-geb)