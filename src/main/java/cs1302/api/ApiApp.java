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
import javafx.geometry.Insets;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.Arrays;
import java.util.Random;

/**
 * City photo guesser game that displays a random city photo from one of the
 * cities in the list to which the user has to match to an answer displayed.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene startScene;
    Scene gameScene;
    Scene endScene;
    VBox root;
    VBox rootOfGameScene;
    HBox round;
    HBox newScene;
    VBox end;
    VBox image;
    ImageView imageView;
    VBox answers;
    Button start;
    Background background;
    BackgroundImage backDrop;
    Image skylineImage;
    Label pressStart;
    Label cityDump;
    Label roundNumber;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    Label whichCityIsThis;
    String rightAnswer;
    int roundCounter;
    int scoreCounter;
    Label yourScore;
    Label quote;
    Button startOver;
    Button next;
    HBox nextHBox;
    Label description;

    private static final List<String> CITIES = Arrays.asList(
        "Tokyo, Japan", "Delhi, India", "Shanghai, China",
        "San Francisco, California", "London, UK",
        "Sao Paulo, Brazil", "Mexico City, Mexico",
        "Cairo, Egypt", "Mumbai, India",
        "Beijing, China", "Dhaka, Bangladesh", "Osaka, Japan",
        "New York, USA", "Karachi, Pakistan", "Buenos Aires, Argentina",
        "Chongqing, China", "Istanbul, Turkey", "Kolkata, India",
        "Manila, Philippines", "Lagos, Nigeria", "Rio de Janeiro, Brazil",
        "Tianjin, China", "Bucharest, Romania",
        "Moscow, Russia", "Lahore, Pakistan",
        "Paris, France", "Bogota, Colombia", "Jakarta, Indonesia",
        "Lima, Peru", "Bangkok, Thailand", "Seoul, South Korea",
        "Tehran, Iran", "Chicago, Illinois", "Ho Chi Minh City, Vietnam",
        "Luanda, Angola", "Kuala Lumpur, Malaysia", "Hong Kong, China",
        "Riyadh, Saudi Arabia", "Baghdad, Iraq", "Santiago, Chile", "Surat, India",
        "Madrid, Spain", "Houston, Texas, USA", "Dallas, Texas", "Toronto, Canada",
        "Dar es Salaam, Tanzania", "Miami, Florida", "Belo Horizonte, Brazil",
        "Singapore", "Philadelphia, Pennsylvania", "Atlanta, Georgia",
        "Fukuoka, Japan", "Khartuom, Sudan", "Barcelona, Spain",
        "Johannesburg, South Africa", "Saint Petersburg, Russia",
        "Washington D.C.", "Los Angeles, California",
        "Guadalajara, Mexico", "Cordoba, Argentina", "Dubai",
        "San Diego, California", "Phoenix, Arizona", "Budapest, Hungary",
        "Berlin, Germany", "Rome, Italy", "Florence, Italy", "Vienna, Austria",
        "Amsterdam, Netherlands", "Stockholm, Sweden", "Lisbon, Portugal",
        "Athens, Greece", "Athens, Georgia", "Munich, Germany", "Brussels, Belgium",
        "Dublin, Ireland", "Oslo, Norway", "Milan, Italy", "Warsaw, Poland",
        "Kyiv, Ukraine", "Copenhagen, Denmark", "Ontario, Canada", "Sydney, Australia",
        "Melbourne, Australia", "Brisbane, Australia", "Medellin, Colombia", "Nairobi, Kenya"
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
        end = new VBox(20);
        imageView = new ImageView();
        answer1 = new Button();
        answer2 = new Button();
        answer3 = new Button();
        answer4 = new Button();
        whichCityIsThis = new Label("Match the photo to a city below");
        rootOfGameScene = new VBox(20);
        round = new HBox(20);
        gameScene = new Scene(rootOfGameScene, 728, 364);
        endScene = new Scene(end, 728, 364);
        rightAnswer = "";
        roundCounter = 0;
        scoreCounter = 0;
        yourScore = new Label();
        quote = new Label();
        startOver = new Button("PLAY AGAIN");
        roundNumber = new Label();
        nextHBox = new HBox(20);
        next = new Button("NEXT");
        description = new Label("Random city photo guesser");
    } // ApiApp

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        //Start scene
        root.setBackground(background);
        cityDump.setFont(new Font("Open Sans Extrabold", 75));
        pressStart.setFont(new Font("Open Sans Semibold", 16));
        description.setFont(new Font("Open Sans Semibold", 24));
        root.getChildren().addAll(cityDump, description, pressStart, start);
        root.setAlignment(Pos.CENTER);
        start.setFont(new Font("Monospaced", 15));
        //game scene
        answers.getChildren().addAll(whichCityIsThis, answer1, answer2, answer3, answer4);
        image.getChildren().addAll(imageView);
        newScene.getChildren().addAll(image, answers);
        nextHBox.getChildren().add(next);
        round.getChildren().add(roundNumber);
        rootOfGameScene.getChildren().addAll(round, newScene, nextHBox);
        rootOfGameScene.setBackground(background);
        newScene.setAlignment(Pos.CENTER);
        image.setAlignment(Pos.CENTER);
        answers.setAlignment(Pos.CENTER);
        nextHBox.setAlignment(Pos.CENTER_LEFT);
        round.setAlignment(Pos.CENTER);
        nextHBox.setPadding(new Insets(0, 0, 20, 20));
        roundNumber.setPadding(new Insets(20, 0, 0, 0));
        imageView.setFitWidth(300);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        VBox.setVgrow(answer1, Priority.ALWAYS);
        VBox.setVgrow(answer2, Priority.ALWAYS);
        VBox.setVgrow(answer3, Priority.ALWAYS);
        VBox.setVgrow(answer4, Priority.ALWAYS);
        HBox.setHgrow(image, Priority.ALWAYS);
        HBox.setHgrow(answers, Priority.ALWAYS);
        answer1.setPrefWidth(300);
        answer2.setPrefWidth(300);
        answer3.setPrefWidth(300);
        answer4.setPrefWidth(300);
        answer1.setPrefHeight(50);
        answer2.setPrefHeight(50);
        answer3.setPrefHeight(50);
        answer4.setPrefHeight(50);
        //end scene
        end.setBackground(background);
        end.setAlignment(Pos.CENTER);
        end.getChildren().addAll(yourScore, quote, startOver);
        yourScore.setFont(new Font("Open Sans Extrabold", 24));
        quote.setFont(new Font("Open Sans Semibold", 16));
        startOver.setFont(new Font("Monospaced", 15));
        setAllButtonFunctionalities();
    } // init

    /**
     * Sets the functionalities for all the buttons in the game.
     */
    public void setAllButtonFunctionalities() {
        start.setOnAction(e -> this.handleStart());
        answer1.setOnAction(e -> this.handleAnswer1());
        answer2.setOnAction(e -> this.handleAnswer2());
        answer3.setOnAction(e -> this.handleAnswer3());
        answer4.setOnAction(e -> this.handleAnswer4());
        startOver.setOnAction(e -> this.handleStartOver());
        next.setOnAction(e -> this.handleNext());
    } // setAllButtonFunctionalities

    /**
     * Button handler for the next button on the game screen.
     */
    public void handleNext() {
        if (roundCounter == 10) {
            yourScore.setText("Score: " + scoreCounter + "/10");
            if (scoreCounter < 4) {
                quote.setText("hmm... your geography needs some work. Keep practicing!");
            } else if (scoreCounter > 3 && scoreCounter < 7) {
                quote.setText("Not bad!");
            } else {
                quote.setText("Impressive! You know your places.");
            } // if
            stage.setScene(endScene);
        } else {
            handleStart();
        } // if
    } // handleNext

    /**
     * Button handler for the start over button.
     */
    public void handleStartOver() {
        roundCounter = 0;
        scoreCounter = 0;
        handleStart();
    } // handleStartOver

    /**
     * Button handler for the first answer choice.
     */
    public void handleAnswer1() {
        changeColors();
        if (answer1.getText().equals(rightAnswer)) {
            scoreCounter++;
        } // if
        next.setDisable(false);
    } // handleAnswer1

    /**
     * Button handler for the second answer choice.
     */
    public void handleAnswer2() {
        changeColors();
        if (answer2.getText().equals(rightAnswer)) {
            scoreCounter++;
        } // if
        next.setDisable(false);
    } // handleAnswer2

    /**
     * Button handler for the third answer choice.
     */
    public void handleAnswer3() {
        changeColors();
        if (answer3.getText().equals(rightAnswer)) {
            scoreCounter++;
        } // if
        next.setDisable(false);
    } // handleAnswer3

    /**
     * Button handler for the fourth answer choice.
     */
    public void handleAnswer4() {
        changeColors();
        if (answer4.getText().equals(rightAnswer)) {
            scoreCounter++;
        } // if
        next.setDisable(false);
    } // handleAnswer4

    /**
     * Changes the colors of the answer choice texts after an answer
     * is clicked.
     */
    private void changeColors() {
        if (answer1.getText().equals(rightAnswer)) {
            answer1.setTextFill(Color.GREEN);
        } else {
            answer1.setTextFill(Color.RED);
        } // if
        if (answer2.getText().equals(rightAnswer)) {
            answer2.setTextFill(Color.GREEN);
        } else {
            answer2.setTextFill(Color.RED);
        } // if
        if (answer3.getText().equals(rightAnswer)) {
            answer3.setTextFill(Color.GREEN);
        } else {
            answer3.setTextFill(Color.RED);
        } // if
        if (answer4.getText().equals(rightAnswer)) {
            answer4.setTextFill(Color.GREEN);
        } else {
            answer4.setTextFill(Color.RED);
        } // if

    } // changeColors

    /**
     * Sets the answer choices and ensures that all are unique, and that none of the
     * wrong answers are set to the right answer's city name.
     */
    private void setAnswers() {
        next.setDisable(true);
        Random rand = new Random();
        String wrongAnswer1 = CITIES.get(rand.nextInt(CITIES.size()));
        String wrongAnswer2 = CITIES.get(rand.nextInt(CITIES.size()));
        String wrongAnswer3 = CITIES.get(rand.nextInt(CITIES.size()));
        // ensures none of the other answer choices are the same / none of them
        // equal the right answer.
        while (wrongAnswer1.equals(rightAnswer) || wrongAnswer1.equals(wrongAnswer2) ||
        wrongAnswer1.equals(wrongAnswer3)) {
            wrongAnswer1 = CITIES.get(rand.nextInt(CITIES.size()));
        } // while
        while (wrongAnswer2.equals(rightAnswer) ||
        wrongAnswer2.equals(wrongAnswer3)) {
            wrongAnswer2 = CITIES.get(rand.nextInt(CITIES.size()));
        } // while
        while (wrongAnswer3.equals(rightAnswer)) {
            wrongAnswer3 = CITIES.get(rand.nextInt(CITIES.size()));
        } //while

        int whichBox = rand.nextInt(4);
        if (whichBox == 0) {
            answer1.setText(rightAnswer);
            answer2.setText(wrongAnswer1);
            answer3.setText(wrongAnswer2);
            answer4.setText(wrongAnswer3);
        } else if (whichBox == 1) {
            answer2.setText(rightAnswer);
            answer1.setText(wrongAnswer1);
            answer3.setText(wrongAnswer2);
            answer4.setText(wrongAnswer3);
        } else if (whichBox == 2) {
            answer3.setText(rightAnswer);
            answer1.setText(wrongAnswer1);
            answer2.setText(wrongAnswer2);
            answer4.setText(wrongAnswer3);
        } else {
            answer4.setText(rightAnswer);
            answer1.setText(wrongAnswer1);
            answer2.setText(wrongAnswer2);
            answer3.setText(wrongAnswer3);
        } // if
    }

    /**
     * Handles the start button, makes the HTTP requests and is responsible
     * for every photo change.
     */
    public void handleStart() {
        stage.setScene(gameScene);
        answer1.setTextFill(Color.BLACK);
        answer2.setTextFill(Color.BLACK);
        answer3.setTextFill(Color.BLACK);
        answer4.setTextFill(Color.BLACK);
        try {
            double random = Math.floor(Math.random() * CITIES.size());
            String randomCity = CITIES.get((int)random);
            rightAnswer = randomCity;
            randomCity = randomCity.replaceAll("\\s", "");
            setAnswers();
            String uri1 = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
                + randomCity + "&types=(cities)&key=" + "AIzaSyBQQ90g4GhcqJ5gFrsXIZiBVWRbq82QSHI";

            HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create(uri1)).build();
            HttpResponse<String> response1 = HTTP_CLIENT.send(request1, BodyHandlers.ofString());
            String json1 = response1.body();
            if (response1.statusCode() != 200) {
                throw new IOException(response1.toString());
            } // if
            AutocompleteResponse autocompleteResponse = GSON.fromJson(
                json1, AutocompleteResponse.class);
            AutocompleteResult result1 = autocompleteResponse.predictions[0];
            String placeid = result1.placeId;
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
            int n = rand.nextInt(placeDetailsResponse.result.photos.length);
            PlaceDetailsResult result2 = placeDetailsResponse.result.photos[n];
            String reference = result2.photoReference;
            String uri3 = "https://maps.googleapis.com/maps/api/place/photo?photoreference="
                + reference + "&key=AIzaSyBQQ90g4GhcqJ5gFrsXIZiBVWRbq82QSHI"
                + "&maxwidth=300&maxheight=200";
            HttpRequest request3 = HttpRequest.newBuilder().uri(URI.create(uri3)).build();
            HttpResponse<InputStream> response3 = HTTP_CLIENT.send
                (request3, BodyHandlers.ofInputStream());
            if (response3.statusCode() != 200) {
                throw new IOException(response3.toString());
            } // if
            String url = response3.toString();
            String trimmedUrl = url.substring(5, url.length() - 5);
            Image image = new Image(trimmedUrl);
            imageView.setImage(image);
            roundCounter++;
            roundNumber.setText(roundCounter + "/10");
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

    /**
     * Show a modal error alert based on {@code cause}.
     * @param cause a Throwable.
     */
    public static void alertError(Throwable cause) {
        TextArea text = new TextArea(cause.toString());
        Alert alert = new Alert(AlertType.ERROR);
        alert.getDialogPane().setContent(text);
        alert.setResizable(true);
        alert.showAndWait();
    } // alertError

} // ApiApp
