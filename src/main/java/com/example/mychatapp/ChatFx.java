package com.example.mychatapp;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;

public class ChatFx extends Application {
    private PrintWriter out;
    @FXML
    private VBox messageContainer; // VBox to hold the messages
    private VBox messageContainer2;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chatUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();


        // Connect to the server
        try {
            Socket socket = new Socket("127.0.0.1", 8080);
            out = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                try {
                    Scanner in = new Scanner(socket.getInputStream());
                    while (in.hasNext()) {
                        String receivedMessage = in.nextLine();
                        Platform.runLater(() -> addMessage(receivedMessage));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setOnCloseRequest(event -> {
            if (out != null) {
                out.close();
            }
        });
    }

    private void sendMessage(TextField messageInputField) {
        String message = messageInputField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            appendMessage("right");
            messageInputField.clear();
        }
    }

    private void addMessage(String message) {
        messageContainer2 = new VBox();
        Label label = new Label(message);
        messageContainer.getChildren().add(messageContainer2);
        messageContainer2.getChildren().add(label);

    }
    private void appendMessage(String alignment) {
        String styleClass = alignment.equals("left") ? "left-message" : "right-message";
        if (alignment.equals("right")) {
            messageContainer2.setAlignment(Pos.TOP_RIGHT);
        } else {
            messageContainer2.setAlignment(Pos.TOP_LEFT);
        }

    }

}


