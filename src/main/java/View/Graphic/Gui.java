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
    private Scene CommunicationScene;
    private Scene nameScene;
    private Scene selectGodScene;
    private Scene numberScene;
    private Scene resumeScene;
    private Scene waitScene;
    private Scene godPickScene;
    private Scene confirmScene;
    private Scene gameScene;

    private WelcomeController welcomeController;
    private CommunicationController communicationController;
    private NameController nameController;
    private SelectGodController selectGodController;
    private NumberController numberController;
    private ResumeController resumeController;
    private WaitChoiceController waitController;
    private GodPickController godPickController;
    private ConfirmController confirmController;
    private GameController gameController;

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

    public void Communication(CommandMsg command, ConnectionHandler client){
        communicationController.SetUp(command, client);
        dialog = new Stage();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.APPLICATION_MODAL);
        CommunicationScene.setFill(Color.TRANSPARENT);
        CommunicationScene.getRoot().setStyle("-fx-background-color: rgba(255, 255, 255, 0);");
        dialog.setScene(CommunicationScene);
        dialog.showAndWait();
    }

    public void Confirm(CommandMsg command, ConnectionHandler client){
        confirmController.SetUp(client, selectGodController, command);
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
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Communication.fxml").toURI().toURL());
            root = loader.load();
            communicationController = loader.getController();
            CommunicationScene = new Scene(root, 250, 278);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Name.fxml").toURI().toURL());
            root = loader.load();
            nameController = loader.getController();
            nameScene = new Scene(root, 800, 600);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Godphase.fxml").toURI().toURL());
            root = loader.load();
            selectGodController = loader.getController();
            selectGodScene = new Scene(root, 800, 600);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/GodPickPhase.fxml").toURI().toURL());
            root = loader.load();
            godPickController = loader.getController();
            godPickScene = new Scene(root, 800, 600);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/WaitingChoice.fxml").toURI().toURL());
            root = loader.load();
            waitController = loader.getController();
            waitScene = new Scene(root, 800, 600);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Number.fxml").toURI().toURL());
            root = loader.load();
            numberController = loader.getController();
            numberScene = new Scene(root, 800, 600);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Resume.fxml").toURI().toURL());
            root = loader.load();
            resumeController = loader.getController();
            resumeScene = new Scene(root, 800, 600);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Confirm.fxml").toURI().toURL());
            root = loader.load();
            confirmController = loader.getController();
            confirmScene = new Scene(root, 250, 278);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Game.fxml").toURI().toURL());
            root = loader.load();
            gameController = loader.getController();
            gameScene = new Scene(root, 800, 600);
        }
        catch (IOException e){
            System.err.println("problems initScenes");
        }
    }

    public void initControllers(){
        welcomeController.setGui(this);
        communicationController.setGui(this);
        nameController.setGui(this);
        selectGodController.setGui(this);
        numberController.setGui(this);
        resumeController.setGui(this);
        waitController.setGui(this);
        godPickController.setGui(this);
        confirmController.setGui(this);
        gameController.setGui(this);
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
            SwitchScene(nameScene);
            nameController.SetUp(command, client);
        });
    }

    public void FirstHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            SwitchScene(numberScene);
            numberController.SetUp(command, client);
        });
    }

    public void NumberHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            SwitchScene(godPickScene);
            godPickController.SetUp(command, client);
        });
    }

    public void AnswerHandler(CommandMsg command, ConnectionHandler client){
        if(command.getCommandType() == CommandType.ANS_POWER){
            Platform.runLater(() -> Confirm(command, client));
        }
        else {
            Platform.runLater(() -> {
                SwitchScene(resumeScene);
                resumeController.SetUp(command, client);
            });
        }
    }

    public void GodHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            SwitchScene(selectGodScene);
            selectGodController.SetUp(command, client);
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
