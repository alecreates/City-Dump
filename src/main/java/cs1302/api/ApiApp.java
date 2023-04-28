package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.net.URI;

import javafx.scene.control.Label;
import javafx.scene.control.Labeled;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.Arrays;
import java.util.Random;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene startScene;
    Scene gameScene;
    VBox root;
    HBox newScene;
    VBox image;
    ImageView imageView;
    VBox answers;
    Button start;
    Background background;
    BackgroundImage backDrop;
    Image skylineImage;
    Label pressStart;
    Label cityDump;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    Label whichCityIsThis;

    private static final List<String> CITIES = Arrays.asList(
        "Tokyo", "Delhi", "Shanghai",
        "San Francisco", "London",
        "Sao Paulo", "Mexico City",
        "Cairo", "Mumbai",
        "Beijing", "Dhaka", "Osaka",
        "New York", "Karachi", "Buenos Aires",
        "Chongqing", "Istanbul", "Kolkata",
        "Manila", "Lagos", "Rio de Janeiro",
        "Tianjin", "Kinshasa", "Guangzhou",
        "Moscow", "Shenzhen", "Lahore", "Bangalore",
        "Paris", "Bogota", "Jakarta",
        "Lima", "Bangkok", "Seoul", "Nagoya",
        "Tehran", "Chicago", "Ho Chi Minh City",
        "Luanda", "Kuala Lumpur", "Hong Kong",
        "Riyadh", "Baghdad", "Santiago", "Surat",
        "Madrid", "Houston", "Dallas", "Toronto",
        "Dar es Salaam", "Miami", "Belo Horizonte",
        "Singapore", "Philadelphia", "Atlanta",
        "Fukuoka", "Khartuom", "Barceloma",
        "Johannesburg", "Saint Petersburg",
        "Qingdao", "Washington D.C.", "Alexandria",
        "Guadalajara"
        );


    private static final String BACKGROUND_IMG = "file:resources/skyline.jpg";

    /** HTTP Client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    /** Google Gson object. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox(10);
        start = new Button("START");
        skylineImage = new Image(BACKGROUND_IMG);
        backDrop = new BackgroundImage(skylineImage, null, null, BackgroundPosition.CENTER, null);
        BackgroundImage[] array = {backDrop};
        background = new Background(array);
        pressStart = new Label("Press START to play");
        cityDump = new Label("City Dump");
        newScene = new HBox(20);
        image = new VBox();
        answers = new VBox(20);
        imageView = new ImageView();
        answer1 = new Button();
        answer2 = new Button();
        answer3 = new Button();
        answer4 = new Button();
        whichCityIsThis = new Label("Match the photo to a city below");
        gameScene = new Scene(newScene, 728, 364);

    } // ApiApp


    public void init() {
        //Start scene
        root.setBackground(background);
        root.getChildren().addAll(cityDump, pressStart, start);
        root.setAlignment(Pos.CENTER);

        //game scene
        image.getChildren().add(imageView);
        answers.getChildren().addAll(whichCityIsThis, answer1, answer2, answer3, answer4);


        VBox.setVgrow(answer1, Priority.ALWAYS);
        VBox.setVgrow(answer2, Priority.ALWAYS);
        VBox.setVgrow(answer3, Priority.ALWAYS);
        VBox.setVgrow(answer4, Priority.ALWAYS);
        HBox.setHgrow(image, Priority.ALWAYS);
        HBox.setHgrow(answers, Priority.ALWAYS);
        answer1.setMaxWidth(Double.MAX_VALUE);
        answer2.setMaxWidth(Double.MAX_VALUE);
        answer3.setMaxWidth(Double.MAX_VALUE);
        answer4.setMaxWidth(Double.MAX_VALUE);
        answer1.setMaxHeight(Double.MAX_VALUE);
        answer2.setMaxHeight(Double.MAX_VALUE);
        answer3.setMaxHeight(Double.MAX_VALUE);
        answer4.setMaxHeight(Double.MAX_VALUE);

        newScene.getChildren().addAll(image, answers);
        newScene.setBackground(background);

        start.setOnAction(e -> this.handleStart());
    } // init


    public void handleStart() {
        stage.setScene(gameScene);
        try {
            double random = Math.floor(Math.random() * CITIES.size());
            String randomCity = CITIES.get(0);
            String uri1 = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
                + randomCity + "&types=(cities)&key=" + "AIzaSyBQQ90g4GhcqJ5gFrsXIZiBVWRbq82QSHI";

            HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create(uri1)).build();
            HttpResponse<String> response1 = HTTP_CLIENT.send(request1, BodyHandlers.ofString());
            String json1 = response1.body();

            if (response1.statusCode() != 200) {
                throw new IOException(response1.toString());
            } // if

            System.out.println(json1);

            AutocompleteResponse autocompleteResponse = GSON.fromJson(
                json1, AutocompleteResponse.class);

            AutocompleteResult result1 = autocompleteResponse.predictions[0];
            String placeid = result1.place_id;

            String uri2 = "https://maps.googleapis.com/maps/api/place/details/json?place_id="
                + placeid + "&fields=photo&key=AIzaSyBQQ90g4GhcqJ5gFrsXIZiBVWRbq82QSHI";
            HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create(uri2)).build();
            HttpResponse<String> response2 = HTTP_CLIENT.send(request2, BodyHandlers.ofString());
            String json2 = response2.body();

            if (response2.statusCode() != 200) {
                throw new IOException(response2.toString());
            } // if

            PlaceDetailsResponse placeDetailsResponse = GSON.fromJson(
                json2, PlaceDetailsResponse.class);

            Random rand = new Random();
            int n = rand.nextInt(10);

            PlaceDetailsResult result2 = placeDetailsResponse.result.photos[n];
            String photoReference = result2.photo_reference;

            String uri3 = "https://maps.googleapis.com/maps/api/place/photo?photoreference="
                + photoReference + "&key=AIzaSyBQQ90g4GhcqJ5gFrsXIZiBVWRbq82QSHI"
                + "&maxwidth=400&maxheight=400";

            HttpRequest request3 = HttpRequest.newBuilder().uri(URI.create(uri3)).build();
            HttpResponse<InputStream> response3 = HTTP_CLIENT.send
                (request3, BodyHandlers.ofInputStream());

            if (response3.statusCode() != 200) {
                throw new IOException(response3.toString());
            } // if

            String url = response3.toString();
            String trimmedUrl = url.substring(5, url.length() - 5);
            System.out.println(trimmedUrl);
            Image image = new Image(trimmedUrl);

            imageView.setImage(image);

        } catch (IOException | InterruptedException e) {
            alertError(e);
        } // try




    } // handleStart

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        VBox vbox = new VBox();
        this.stage = stage;
        // setup scene
        startScene = new Scene(root, 728, 364);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(startScene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

    public static void alertError(Throwable cause) {
        TextArea text = new TextArea(cause.toString());
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
    } // alertError

} // ApiApp
