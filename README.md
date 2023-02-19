# Weatherdex
The project is using these following libraries:
1. Jetpack compose
2. Retrofit
3. Gson
4. Android Architecture Components
5. Hilt
6. Glide
7. MockK
8. Coroutines
9. Chucker

### Module Structure
- app
- weather: feature module
- data: a folder containing data layer interface & implementation
  - city: data module for fetching city data(https://api.api-ninjas.com)
  - favorites: data module for save/delete/update favorite city data to SharedPreferences
  - weather: data module for fetching weather data(https://api.openweathermap.org)
- base
  - core: core module that contains essential objects, helper classes, etc for feature modules
  - network: base module for data modules

### Dependencies graph
application (app) --Depends to--> feature modules(weather)
feature modules(weather) --Depends to--> data modules(data.city, data.favorites, data.weather)
feature modules(weather) --Depends to--> core modules(base.core, base.network)
data modules(data.city, data.favorites, data.weather) --Depends to--> core modules(base.core, base.network)

### Libraries Usage
1. Jetpack compose: UI related codes
2. Retrofit: Network calls
3. Gson: parsing from/to Json format
4. Android Architecture Components: MVVM
5. Hilt: Dependency Injection
6. Glide: Runtime image load
7. MockK: Unit-testing
8. Coroutines: Asynchronous tasks
9. Chucker: Runtime network traffic monitoring

### How to build
1. Clone the project
2. Open in Android Studio
3. Build, then run in either emulator or a real device
