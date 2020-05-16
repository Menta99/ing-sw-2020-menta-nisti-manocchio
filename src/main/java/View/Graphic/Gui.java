package View.Graphic;

import View.View;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Gui extends Application implements View {

    private static Stage primaryStage;
    private static Stage dialog;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage=primaryStage;
        Parent root = FXMLLoader.load(new File("src/main/java/View/Graphic/FXML/Welcome.fxml").toURI().toURL());
        primaryStage.setScene(new Scene(root,800,600));
        primaryStage.show();

    }

    public static void SwitchScene(String url) throws IOException {
        Parent root = FXMLLoader.load(new File(url).toURI().toURL());
        primaryStage.setScene(new Scene(root,800,600));
        primaryStage.show();
    }


    public void ConnectionError() throws IOException {
        Parent root = FXMLLoader.load(new File("src/main/java/View/Graphic/FXML/WelcomeError.fxml").toURI().toURL());
        dialog=new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(root,360,89));
        dialog.showAndWait();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Stage getDialog() {
        return dialog;
    }

}
