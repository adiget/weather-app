# weather-app

This app is a weather forecast app, which display today's weather along with the next 6 days weather forecast.'

### Build and Run:-
1. Android Studio->Build->Build APK
2. On Terminal adb install <path>/*.apk
   Or send the apk to device by email, then click apk.
   Make sure device is internet connected.
3. Run the app
4. It will display current weather and forecast weather of the device location.
5. API Calls
a) App finds the longitude and latitude of current location, and get the weather data.
  If failed to get the location data, it gets the weather from default city=London.
  
b) From the weather data, it picks the city, and then call the forecast api to get the weather forecast data for that city.

6. URL
http://api.openweathermap.org/data/2.5/weather?&APPID=<api_key>&units=metric&lat=51.6501929&lon=-0.1828601

http://api.openweathermap.org/data/2.5/forecast?&APPID=<api_key>&units=metric&q=<city>

  
  

### Test:-
1. Manually get the data with api in some tool like Postman or any rest client.
Verify the data with the app weather screen data.
 
2. Run Unit test & Instrumentation Test 



### Framework:-
1. Retrofit.
2. MVP
3. RxJava