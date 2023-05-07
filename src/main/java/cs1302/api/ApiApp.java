package cs1302.api;

import java.net.http.HttpClient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.control.ProgressBar;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.lang.Thread;
import java.lang.Runnable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Pos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Checks usesrs IP to find their location and gives the weather.
 * in thier area
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene;
    VBox vBox;

/**
   Holds the values that are given in the Abstract response.
*/
    public class AbstractResponse {
        int resultCount;
        private String city;
        AbstractResult[] results;
        private String country;

        /**
           Gets the city.
           @return the city
        */
        public String getCity() {
            return city;
        }

        /**
           Gets the country.
           @return the country
        */
        public String getCountry() {
            return country;
        }
    }

/**
   Holds the results of the abstract query.
*/
    public class AbstractResult {
        String wrapperType;
        String kind;
        String artworkUrl100;
    }

    /**
       Holds the varaibes that are given in the response to the
       weather query.
    */
    public class WeatherResponse {

        private int cloud_pct;
        private int temp;
        private int feels_like;
        private int humidity;
        private int min_temp;
        private int max_temp;
        private double wind_speed;
        private int wind_degrees;

        /**
           Gets the cloudPct.
           @return the cloud pct
        */
        public int getCloudPct() {
            return cloud_pct;
        }
        /**
           Gets the temp.
           @return the temp
        */

        public int getTemperature() {
            return temp;
        }

        /**
           Gets the real feel.
           @return the real feel
        */
        public int getFeelsLike() {
            return feels_like;
        }

        /**
           Gets the humidity.
           @return the humidity
        */
        public int getHumidity() {
            return humidity;
        }

        /**
           Gets the min temp.
           @return the min temp
        */
        public int getMinTemperature() {
            return min_temp;
        }

        /**
           Gets the max temp.
           @return the max temp
        */
        public int getMaxTemperature() {
            return max_temp;
        }

        /**
           Gets the wind speed.
           @return the wind speed
        */
        public double getWindSpeed() {
            return wind_speed;
        }

        /**
           Gets the wind degrees.
           @return the wind degrees
        */
        public int getWindDegrees() {
            return wind_degrees;
        }


    }

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
         .version(HttpClient.Version.HTTP_2)
         .followRedirects(HttpClient.Redirect.NORMAL)
         .build();

     /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
         .setPrettyPrinting()                          // enable nice output when printing
         .create();                                    // builds and returns a Gson object


       /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        vBox = new VBox();
    } // ApiApp

    /** {@inheritDoc} */  @Override
    public void start(Stage stage) {

        this.stage = stage;

         // set up UI elements
        ImageView logo = new ImageView(new Image("file:resources/logo.png"));
        logo.setPreserveRatio(true);
        logo.setFitHeight(150);
        Label title = new Label("Weather App");
        title.setStyle("-fx-font-size: 24pt; -fx-font-weight: bold;");
        title.setAlignment(Pos.TOP_CENTER);
        Label subtitle = new Label("Click button to see weather in your area!");
        subtitle.setStyle("-fx-font-size: 14pt;");
        subtitle.setAlignment(Pos.CENTER);
        Button submitButton = new Button("Submit");
        submitButton.setAlignment(Pos.BOTTOM_CENTER);
        Label weatherLabel = new Label("");



        submitButton.setOnAction(event -> {

            // ((GridPane) scene.getRoot()).getChildren().clear();

            vBox.getChildren().clear();

            Label newTitle = new Label("Weather Info!");
            newTitle.setStyle("-fx-font-size: 20pt; -fx-font-weight: bold;");
            newTitle.setAlignment(Pos.TOP_CENTER);

            String allInfo = makeHttpRequest();

            Label informationLabel = new Label(allInfo);

            informationLabel.setAlignment(Pos.CENTER);


            submitButton.setLayoutX(10);
            submitButton.setLayoutY(scene.getHeight() - submitButton.getHeight() - 15);


            // VBox vBox = new VBox();
            vBox.setPadding(new Insets(20));
            vBox.setSpacing(10);
            vBox.setAlignment(Pos.TOP_LEFT);

            vBox.getChildren().addAll(newTitle, informationLabel, submitButton);



            scene.setRoot(vBox);
        });


          // set up layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.add(logo, 0, 0, 2, 1);
        gridPane.add(title, 0, 0, 1, 1);
        gridPane.add(subtitle, 0, 2, 2, 1);
        gridPane.add(submitButton, 1, 3, 1, 1);
        gridPane.add(weatherLabel, 0, 4, 2, 1);

        // setup scene
        scene = new Scene(gridPane);

        // setup stage
        stage.setTitle("Weather App");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.show();

    } // start

    /**
       Sets up the Http request for the APIs and returns the String of
       all the information needed.
       @return Returns the string value of all the needed weather information.
    */
    private String makeHttpRequest() {
        String weatherInfo = "";
        try {
            String url = "https://ipgeolocation.abstractapi.com/v1/?api_key=a25b2c3104f84b20bc84c0b8689234c7&" +
                "fields=country,city";

            HttpResponse<String> response = sendHttpRequest(url);
            String jsonString = response.body().trim();


            String currentCity = parseAbstractResponseCity(jsonString);

            System.out.println(currentCity);

            String encodedCity = URLEncoder.encode(currentCity, StandardCharsets.UTF_8);

            String currentCountry = parseAbstractResponseCountry(jsonString);

            System.out.println(currentCountry);

            String encodedCountry = URLEncoder.encode(currentCountry, StandardCharsets.UTF_8);

            String url2 = String.format("https://weather-by-api-ninjas.p.rapidapi.com/v1/weather?city=%s&country=%s",
                encodedCity, encodedCountry);

            System.out.println(url2);

            // HttpResponse<String> response2 = sendHttpRequest(url2);
            //String jsonString2 = response2.body().trim();


            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url2))
                .header("X-RapidAPI-Key", "9b8b71a0f3msha5b5a89ae826a55p168d73jsne2656d84923d")
                .header("X-RapidAPI-Host", "weather-by-api-ninjas.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
            HttpResponse<String> response2 = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());


            String jsonString2 = response2.body().trim();

            System.out.println(parseWeatherResponse(jsonString2));

            String weatherStats = parseWeatherResponse(jsonString2);

            //  System.out.println(jsonString2);

            weatherInfo =
                "Country Name: " + currentCountry + "\n" +
                "City Name: " + currentCity + "\n" +
                weatherStats;

        } catch (Exception e) {

            System.err.println(e);
            e.printStackTrace();
        }
        return weatherInfo;
    }

/**
   Sends the HTTP request out.
   @param url Takes in the parsed URL
   @return returns the JSON response to the query
*/
    private HttpResponse<String> sendHttpRequest(String url) throws IOException,
        InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException(response.toString());
        }
        return response;
    }

    /**
       Uses GSON to parse the JSON response to find the Counntry.
       @param jsonString takes in the json string
       @return returns the parsed string
    */
    private String parseAbstractResponseCountry(String jsonString) {

        Gson gson = new Gson();

        AbstractResponse abstractResponse = gson.fromJson(jsonString, AbstractResponse.class);

        String countryName = abstractResponse.getCountry();

        return countryName;

    }

    /**
       Uses GSON to pare the JSON response to find the City.
       @param jsonString takes in the json string
       @return returns the parsed string
    */
    private String parseAbstractResponseCity(String jsonString) {
        Gson gsonC = new Gson();

        AbstractResponse abstractResponse = gsonC.fromJson(jsonString, AbstractResponse.class);

        String cityName = abstractResponse.getCity();

        return cityName;
    }



    /**
       Uses GSON to parse the weather JSON reposen to find information.
       @param jsonString2 takes in the json string
       @return returns the parsed string
    */
    private String parseWeatherResponse(String jsonString2) {
        Gson gson2 = new Gson();

        WeatherResponse weatherResponse = gson2.fromJson(jsonString2, WeatherResponse.class);

        int cloudPctt = weatherResponse.getCloudPct();
        int temperature = weatherResponse.getTemperature();
        int feelsLikee = weatherResponse.getFeelsLike();
        int humidity = weatherResponse.getHumidity();
        int minTemperaturee = weatherResponse.getMinTemperature();
        int maxTemperaturee = weatherResponse.getMaxTemperature();
        double windSpeedd = weatherResponse.getWindSpeed();
        int windDegreess = weatherResponse.getWindDegrees();


        String response =
            "cloudPct: " + cloudPctt + "\n" +
            "temperature: " + temperature + "\n" +
            "feelsLike: " + feelsLikee + "\n" +
            "humidity: " + humidity + "\n" +
            "minTemperature: " + minTemperaturee + "\n" +
            "maxTemperature: " + maxTemperaturee + "\n" +
            "windSpeed: " + windSpeedd + "\n" +
            "windDegrees: " + windDegreess + "\n";

        return response;

    }


} // ApiApp
