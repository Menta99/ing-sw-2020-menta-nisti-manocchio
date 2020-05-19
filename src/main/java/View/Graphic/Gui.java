package View.Graphic;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import View.Graphic.Controller.ErrorWelcomeController;
import View.Graphic.Controller.NameController;
import View.Graphic.Controller.WelcomeController;
import View.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Gui extends Application implements View {
    private final int PORT_NUM = 5555;
    private final String IP = "127.0.0.1";
    private Socket server;

    private Stage primaryStage;
    private Stage dialog;
    private Scene welcomeScene;
    private Scene errorWelcomeScene;
    private Scene nameScene;
    private WelcomeController welcomeController;
    private ErrorWelcomeController errorWelcomeController;
    private NameController nameController;

    private String nickname;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        initScenes();
        initControllers();
        this.primaryStage.setScene(welcomeScene);
        this.primaryStage.setOnCloseRequest(e -> CloseClient());
        this.primaryStage.show();
    }

    public void SwitchScene(Scene scene){
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void ConnectionError(){
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(errorWelcomeScene);
        //dialog.setOnCloseRequest(e -> primaryStage.close());
        dialog.showAndWait();
    }

    public void initScenes(){
        try{
            FXMLLoader loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Welcome.fxml").toURI().toURL());
            Parent root = loader.load();
            welcomeController = loader.getController();
            welcomeScene = new Scene(root, 800, 600);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/WelcomeError.fxml").toURI().toURL());
            root = loader.load();
            errorWelcomeController = loader.getController();
            errorWelcomeScene = new Scene(root, 300, 80);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Name.fxml").toURI().toURL());
            root = loader.load();
            nameController = loader.getController();
            nameScene = new Scene(root, 800, 600);
            //welcomeScene = new Scene(FXMLLoader.load(new File("src/main/java/View/Graphic/FXML/Welcome.fxml").toURI().toURL()), 800, 600);
            //errorWelcomeScene = new Scene(FXMLLoader.load(new File("src/main/java/View/Graphic/FXML/WelcomeError.fxml").toURI().toURL()), 300, 80);
            //nameScene = new Scene(FXMLLoader.load(new File("src/main/java/View/Graphic/FXML/Name.fxml").toURI().toURL()), 800, 600);
        }
        catch (IOException e){
            System.err.println("problems initScenes");
        }
    }

    public void initControllers(){
        welcomeController.setGui(this);
        errorWelcomeController.setGui(this);
        nameController.setGui(this);
    }

    public void Connect(){
        try {
            server = new Socket(IP, PORT_NUM);
            if(server.isConnected()) {
                SetUp(server);
            }
            else{
                ConnectionError();
            }
        } catch (IOException e) {
            ConnectionError();
        }
    }

    public void SetUp(Socket server){
        new Thread(new ConnectionHandler(server, this)).start();
    }

    public void CloseClient() {
        try {
            if(server!=null){
                server.close();
            }
        } catch (IOException e) {
            System.err.println("Unable to close the Client Socket");
        }
    }

    public void NameHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> SwitchScene(nameScene));
        nameController.SetUp(command, client);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Stage getDialog() {
        return dialog;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
