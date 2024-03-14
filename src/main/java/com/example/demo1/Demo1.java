package com.example.demo1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Demo1 extends Application {
    private ImageView fullSizeImageView;
    private Label imageLabel;
    private int currentIndex = 0;
    private String[] imagePaths = {"M1.jpg", "M2.jpg", "M3.jpg"};
    private Map<String, String> imageLabels = new HashMap<>();
    private boolean isFullSizeDisplayed = false;
    private Button clickedButton;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        fullSizeImageView = new ImageView();
        fullSizeImageView.setFitWidth(500);
        fullSizeImageView.setPreserveRatio(true);
        showImage();

        imageLabels.put("M1.jpg", "Drake");
        imageLabels.put("M2.jpg", "J Cole");
        imageLabels.put("M3.jpg", "Kendrick Lamar");


        imageLabel = new Label();
        imageLabel.setAlignment(Pos.CENTER);

        VBox imageBox = new VBox(10, fullSizeImageView, imageLabel);
        imageBox.setAlignment(Pos.CENTER);

        HBox thumbnailsBox = getThumbnailsBox();
        HBox controlBox = getControlBox();

        root.setTop(thumbnailsBox);
        root.setCenter(imageBox);
        root.setBottom(controlBox);

        Scene scene = new Scene(root, 700, 600);
        String cssPath = "/style.css";
        scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Image Gallery");
        primaryStage.show();
    }

    private HBox getThumbnailsBox() {
        HBox thumbnailsBox = new HBox(10);
        thumbnailsBox.setAlignment(Pos.CENTER);

        for (String imagePath : imagePaths) {
            try {
                InputStream inputStream = getClass().getResourceAsStream("/" + imagePath);
                if (inputStream != null) {
                    Image image = new Image(inputStream);
                    ImageView thumbnail = new ImageView(image);
                    thumbnail.setFitWidth(100);
                    thumbnail.setPreserveRatio(true);
                    thumbnail.setOnMouseClicked(event -> {
                        if (isFullSizeDisplayed) {
                            isFullSizeDisplayed = false;
                            showThumbnailView();
                        } else {
                            int index = thumbnailsBox.getChildren().indexOf(thumbnail);
                            currentIndex = index;
                            showFullSizeView();
                        }
                    });
                    thumbnailsBox.getChildren().add(thumbnail);
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return thumbnailsBox;
    }

    private HBox getControlBox() {
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            if (currentIndex > 0) {
                currentIndex--;
                showImage();
            }
        });

        Button nextButton = new Button("Next");
        nextButton.setOnAction(event -> {
            if (currentIndex < imagePaths.length - 1) {
                currentIndex++;
                showImage();
            }
        });

        Button zoomInButton = new Button("Zoom In");
        zoomInButton.setOnAction(event -> {
            fullSizeImageView.setFitWidth(fullSizeImageView.getFitWidth() * 1.1);
            fullSizeImageView.setFitHeight(fullSizeImageView.getFitHeight() * 1.1);
        });

        Button zoomOutButton = new Button("Zoom Out");
        zoomOutButton.setOnAction(event -> {
            fullSizeImageView.setFitWidth(fullSizeImageView.getFitWidth() * 0.9);
            fullSizeImageView.setFitHeight(fullSizeImageView.getFitHeight() * 0.9);
        });

        HBox controlBox = new HBox(10, backButton, nextButton, zoomOutButton, zoomInButton);
        controlBox.setAlignment(Pos.CENTER);
        return controlBox;
    }

    private void showFullSizeView() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + imagePaths[currentIndex]);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                fullSizeImageView.setImage(image);
                inputStream.close();
                if (imageLabel == null) {
                    imageLabel = new Label();
                    imageLabel.setAlignment(Pos.CENTER);
                }
                imageLabel.setText(imageLabels.get(imagePaths[currentIndex]));
                isFullSizeDisplayed = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showThumbnailView() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + imagePaths[currentIndex]);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                fullSizeImageView.setImage(null);
                if (imageLabel == null) {
                    imageLabel = new Label();
                    imageLabel.setAlignment(Pos.CENTER);
                }
                imageLabel.setText(null);
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showImage() {
        if (isFullSizeDisplayed) {
            showFullSizeView();
        } else {
            showThumbnailView();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
