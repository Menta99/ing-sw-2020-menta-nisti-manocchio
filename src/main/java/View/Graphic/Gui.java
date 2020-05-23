package View.Graphic;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import CommunicationProtocol.SantoriniInfo.BoxInfo;
import CommunicationProtocol.SantoriniInfo.GodInfo;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import View.Graphic.Controller.*;
import View.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Gui extends Application implements View {
    private final int PORT_NUM = 5555;
    private final String IP = "127.0.0.1";
    private Socket server;
    ConnectionHandler handler;

    private Stage primaryStage;
    private Stage dialog;

    private Scene welcomeScene;
    private Scene communicationScene;
    private Scene waitScene;
    private Scene confirmScene;
    private Scene gameScene;
    private Scene godChoiceScene;
    private Scene loginScene;

    private WelcomeController welcomeController;
    private CommunicationController communicationController;
    private WaitController waitController;
    private ConfirmController confirmController;
    private GameController gameController;
    private GodChoiceController godChoiceController;
    private LoginController loginController;

    private String nickname;
    private BoxInfo[][] map;
    private GodInfo[] gods;
    private PlayerInfo[] players;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Santorini");
        initScenes();
        initControllers();
        this.primaryStage.setResizable(false);
        this.primaryStage.setScene(welcomeScene);
        this.primaryStage.setOnCloseRequest(e -> CloseClient());
        this.primaryStage.show();
    }

    public void SwitchScene(Scene scene){
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void Communication(CommandMsg command, ConnectionHandler client){
        communicationController.SetUp(command, client);
        dialog = new Stage();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.APPLICATION_MODAL);
        communicationScene.setFill(Color.TRANSPARENT);
        communicationScene.getRoot().setStyle("-fx-background-color: rgba(255, 255, 255, 0);");
        dialog.setScene(communicationScene);
        dialog.showAndWait();
    }

    public void Confirm(CommandMsg command, ConnectionHandler client, GodChoiceController controller){
        confirmController.SetUp(client, controller, command);
        dialog = new Stage();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.APPLICATION_MODAL);
        confirmScene.setFill(Color.TRANSPARENT);
        confirmScene.getRoot().setStyle("-fx-background-color: rgba(255, 255, 255, 0);");
        dialog.setScene(confirmScene);
        dialog.showAndWait();
    }

    public void initScenes(){
        try{
            FXMLLoader loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Welcome.fxml").toURI().toURL());
            Parent root = loader.load();
            welcomeController = loader.getController();
            welcomeScene = new Scene(root, 800, 600);
            //welcomeScene.setCursor(new ImageCursor(new Image("Texture2D/godpower_hand select.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Communication.fxml").toURI().toURL());
            root = loader.load();
            communicationController = loader.getController();
            communicationScene = new Scene(root, 250, 278);
            //communicationScene.setCursor(new ImageCursor(new Image("Texture2D/godpower_hand select.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Login.fxml").toURI().toURL());
            root = loader.load();
            loginController = loader.getController();
            loginScene = new Scene(root, 800, 600);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Wait.fxml").toURI().toURL());
            root = loader.load();
            waitController = loader.getController();
            waitScene = new Scene(root, 800, 600);
            //waitScene.setCursor(new ImageCursor(new Image("Texture2D/godpower_hand select.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Confirm.fxml").toURI().toURL());
            root = loader.load();
            confirmController = loader.getController();
            confirmScene = new Scene(root, 250, 278);
            //confirmScene.setCursor(new ImageCursor(new Image("Texture2D/godpower_hand select.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Game.fxml").toURI().toURL());
            root = loader.load();
            gameController = loader.getController();
            gameScene = new Scene(root, 800, 600);
            //gameScene.setCursor(new ImageCursor(new Image("Texture2D/godpower_hand select.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/GodChoice.fxml").toURI().toURL());
            root = loader.load();
            godChoiceController = loader.getController();
            godChoiceScene = new Scene(root, 800, 600);
        }
        catch (IOException e){
            System.err.println("problems initScenes");
        }
    }

    public void initControllers(){
        welcomeController.setGui(this);
        communicationController.setGui(this);
        loginController.setGui(this);
        waitController.setGui(this);
        confirmController.setGui(this);
        gameController.setGui(this);
        godChoiceController.setGui(this);
    }

    public void Connect(){
        try {
            server = new Socket(IP, PORT_NUM);
            if(server.isConnected()) {
                SetUp(server);
            }
            else{
                Communication(null, null);
            }
        } catch (IOException e) {
            Communication(null, null);
        }
    }

    public void SetUp(Socket server){
        handler = new ConnectionHandler(server, this);
        new Thread(handler).start();
    }

    public void CloseClient() {
        try {
            if(server!=null){
                handler.setActive(false);
                server.close();
            }
        } catch (IOException e) {
            System.err.println("Unable to close the Client Socket");
        }
    }

    public void NameHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            SwitchScene(loginScene);
            loginController.SetUp(command, client);
        });
    }

    public void FirstHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            loginController.SetUp(command, client);
        });
    }

    public void NumberHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            SwitchScene(godChoiceScene);
            godChoiceController.SetUp(command, client);
        });
    }

    public void AnswerHandler(CommandMsg command, ConnectionHandler client){
        if(command.getCommandType() == CommandType.ANS_POWER){
            Platform.runLater(() -> Confirm(command, client, null));
        }
        else {
            Platform.runLater(() -> {
                loginController.SetUp(command, client);
            });
        }
    }

    public void GodHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            SwitchScene(godChoiceScene);
            godChoiceController.SetUp(command, client);
        });
    }

    public void PoseHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> gameController.SetUpPose(command, client));
    }

    public void UpdateHandler(CommandMsg command, ConnectionHandler client){
        map = command.getInfo().getGrid();
        Platform.runLater(() -> {
            gameController.UpdateMap();
            gameController.setMessage(command);
        });
    }

    public void CloseHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> Communication(command, client));
        client.setActive(false);
    }

    public void CommunicationHandler(CommandMsg command, ConnectionHandler client) {
        switch (command.getCommandType()) {
            case COM_WELCOME:
            case COM_RESTART:
                gods = command.getInfo().getGods();
                players = command.getInfo().getPlayers();
                if(command.getCommandType() == CommandType.COM_RESTART){
                    Platform.runLater(() -> {
                        SwitchScene(gameScene);
                        gameController.SetUp(command, client);
                    });
                }
                break;
            case COM_GODS:
                GodInGame(command.getInfo().getGods());
                break;
            case COM_CHOSEN:
                PlayerGod(command.getInfo().getPlayers());
                Platform.runLater(() -> {
                    Platform.runLater(() -> SwitchScene(gameScene));
                    gameController.SetUp(command, client);
                });
                break;
            case COM_WAIT_CHOICE:
            case COM_WAIT_LOBBY:
                Platform.runLater(() -> {
                    Platform.runLater(() -> SwitchScene(waitScene));
                    waitController.SetUp(command, client);
                });
                break;
            case COM_INVALID_WORKER:
            case COM_INVALID_POS:
            case COM_LOSE:
                Platform.runLater(() -> Communication(command, client));
                break;
        }
    }

    /**
     * Updates the god list, marking the active cards
     * @param godInfo Array of GodInfo
     */
    public void GodInGame(GodInfo[] godInfo){
        GodInfo[] update = new GodInfo[14];
        ArrayList<Integer> list = new ArrayList<>();
        for(GodInfo info : godInfo){
            list.add(info.getPosition());
        }
        for(GodInfo god : gods){
            if(list.contains(god.getPosition())){
                update[god.getPosition()] = new GodInfo(god.getPosition(), god.getName(), god.getPower(), true);
            }
            else{
                update[god.getPosition()] = new GodInfo(god.getPosition(), god.getName(), god.getPower(), false);
            }
        }
        gods = update;
    }

    /**
     * Updates the player list, inserting the respective god
     * @param playerInfo Array of PlayerInfo
     */
    public void PlayerGod(PlayerInfo[] playerInfo){
        PlayerInfo[] update = new PlayerInfo[players.length];
        for(PlayerInfo user : players){
            update[user.getPosition()] = new PlayerInfo(user.getPosition(), user.getName(), user.getColor(), playerInfo[user.getPosition()].getGod());
        }
        players = update;
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

    public GodInfo[] getGods() {
        return gods;
    }

    public PlayerInfo[] getPlayers() {
        return players;
    }

    public BoxInfo[][] getMap() {
        return map;
    }
}
