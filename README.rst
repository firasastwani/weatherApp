WeatherApp 🌦️


Overview: 
WeatherApp is a JavaFX-based desktop application that provides real-time weather information based on the user's current location. The app leverages two RESTful APIs to deliver accurate and up-to-date weather details. The first API determines the user's city based on their location, while the second API fetches weather data specific to that city.

Key Features
Automatic Location Detection: Utilizes a RESTful API to seamlessly detect the user's current city.
Real-Time Weather Updates: Retrieves detailed weather data (temperature, humidity, wind speed, etc.) for the detected city using a second RESTful API.
Responsive JavaFX Interface: Designed with an intuitive and responsive UI using JavaFX, providing a smooth user experience.
Maven Integration: Managed dependencies, build processes, and project configurations using Maven.
Error Handling: Handles potential API errors or network issues, ensuring reliable application performance.


Technologies Used
JavaFX: For building the dynamic and interactive user interface.
RESTful APIs: Integrated to fetch both location and weather data.
Java: Core programming language used for implementing business logic and API integration.
Maven: Utilized for project management, dependency handling, and building the application.

Why This Project Stands Out
Demonstrates proficiency in integrating multiple RESTful APIs to deliver a cohesive application experience.
Showcases experience with JavaFX, a popular framework for building modern Java applications.
Highlights skills in handling asynchronous data retrieval and updating the UI in real-time.
Incorporates Maven, illustrating knowledge of effective project management and build automation.

How It Works
Location Detection: Upon pressing the start button, the application calls a location-based API to determine the user's city.
Weather Fetching: The identified city is then passed to a weather API, which returns current weather data.
User Interface: The weather information is displayed in a JavaFX interface, updating as the user's location changes.
