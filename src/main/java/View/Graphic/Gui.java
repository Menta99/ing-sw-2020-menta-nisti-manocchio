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
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Class representing graphic user interface
 */
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
    private Scene cardScene;
    private Scene loginScene;

    private WelcomeController welcomeController;
    private CommunicationController communicationController;
    private WaitController waitController;
    private ConfirmController confirmController;
    private GameController gameController;
    private GodChoiceController godChoiceController;
    private CardController cardController;
    private LoginController loginController;

    private String nickname;
    private int turnCount = 0;
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

    /**
     * Set the primary stage with an other new scene
     * @param scene new scene to see
     */
    public void SwitchScene(Scene scene){
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Set the stage for the communication message
     * @param command message from server
     * @param client object representing a client connected
     */
    public void Communication(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            communicationController.SetUp(command, client);
            dialog = new Stage();
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.initModality(Modality.APPLICATION_MODAL);
            communicationScene.setFill(Color.TRANSPARENT);
            communicationScene.getRoot().setStyle("-fx-background-color: rgba(255, 255, 255, 0);");
            dialog.setScene(communicationScene);
            dialog.showAndWait();
        });
    }

    /**
     * Set the stage for the confirm message
     * @param command message from server
     * @param client object representing a client connected
     * @param controller controller of GodChoice fxml file
     */
    public void Confirm(CommandMsg command, ConnectionHandler client, GodChoiceController controller){
        Platform.runLater(() -> {
            confirmController.SetUp(client, controller, command);
            dialog = new Stage();
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.initModality(Modality.APPLICATION_MODAL);
            confirmScene.setFill(Color.TRANSPARENT);
            confirmScene.getRoot().setStyle("-fx-background-color: rgba(255, 255, 255, 0);");
            dialog.setScene(confirmScene);
            dialog.showAndWait();
        });
    }

    /**
     * SetUp the pop up window with the cards information
     * @param player number of player which belongs the card
     */
    public void CardInfo(int player){
        Platform.runLater(() -> {
            cardController.SetUp(player);
            dialog = new Stage();
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(cardScene);
            dialog.showAndWait();
        });
    }

    /**
     * Load the correct fxml file to show
     */
    public void initScenes(){
        try{
            FXMLLoader loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Welcome.fxml").toURI().toURL());
            welcomeScene = new Scene(loader.load(), 800, 600);
            welcomeController = loader.getController();
            welcomeScene.setCursor(new ImageCursor(new Image("Cells/Ambient/cursor.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Communication.fxml").toURI().toURL());
            communicationScene = new Scene(loader.load(), 250, 278);
            communicationController = loader.getController();
            communicationScene.setCursor(new ImageCursor(new Image("Cells/Ambient/cursor.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Login.fxml").toURI().toURL());
            loginScene = new Scene(loader.load(), 800, 600);
            loginController = loader.getController();
            loginScene.setCursor(new ImageCursor(new Image("Cells/Ambient/cursor.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Wait.fxml").toURI().toURL());
            waitScene = new Scene(loader.load(), 800, 600);
            waitController = loader.getController();
            waitScene.setCursor(new ImageCursor(new Image("Cells/Ambient/cursor.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Confirm.fxml").toURI().toURL());
            confirmScene = new Scene(loader.load(), 250, 278);
            confirmController = loader.getController();
            confirmScene.setCursor(new ImageCursor(new Image("Cells/Ambient/cursor.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Game.fxml").toURI().toURL());
            gameScene = new Scene(loader.load(), 800, 600);
            gameController = loader.getController();
            gameScene.setCursor(new ImageCursor(new Image("Cells/Ambient/cursor.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/GodChoice.fxml").toURI().toURL());
            godChoiceScene = new Scene(loader.load(), 800, 600);
            godChoiceController = loader.getController();
            godChoiceScene.setCursor(new ImageCursor(new Image("Cells/Ambient/cursor.png", true)));
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Card.fxml").toURI().toURL());
            cardScene = new Scene(loader.load(), 267, 400);
            cardController = loader.getController();
            cardScene.setCursor(new ImageCursor(new Image("Cells/Ambient/cursor.png", true)));
        }
        catch (IOException e){
            System.err.println("problems initScenes");
        }
    }

    /**
     * Initialise the fxml controllers
     */
    public void initControllers(){
        welcomeController.setGui(this);
        communicationController.setGui(this);
        loginController.setGui(this);
        waitController.setGui(this);
        confirmController.setGui(this);
        gameController.setGui(this);
        godChoiceController.setGui(this);
        cardController.setGui(this);
    }

    /**
     * Create a new connection with the server
     */
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

    /**
     * Create a new object for connection handling
     * @param server
     */
    public void SetUp(Socket server){
        handler = new ConnectionHandler(server, this);
        new Thread(handler).start();
    }

    /**
     * Shut down the server
     */
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

    /**
     * Switch to loginScene
     * @param command message from server
     * @param client object representing a client connected
     */
    public void NameHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            SwitchScene(loginScene);
            loginController.SetUp(command, client);
        });
    }

    /**
     * Switch to challenger scene
     * @param command message from server
     * @param client object representing a client connected
     */
    public void FirstHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            loginController.SetUp(command, client);
        });
    }

    /**
     * Switch to GodChoice scene
     * @param command message from server
     * @param client object representing a client connected
     */
    public void NumberHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            SwitchScene(godChoiceScene);
            godChoiceController.SetUp(command, client);
        });
    }

    /**
     * Switch to confirm scene if answered yes
     * @param command message from server
     * @param client object representing a client connected
     */
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

    /**
     * Switch to GodChoice scene
     * @param command message from server
     * @param client object representing a client connected
     */
    public void GodHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> {
            SwitchScene(godChoiceScene);
            godChoiceController.SetUp(command, client);
        });
    }

    /**
     * Handle a new player move
     * @param command message from server
     * @param client object representing a client connected
     */
    public void PoseHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> gameController.SetUpPose(command, client));
    }

    /**
     * Update the map after a move
     * @param command message from server
     * @param client object representing a client connected
     */
    public void UpdateHandler(CommandMsg command, ConnectionHandler client){
        map = command.getInfo().getGrid();
        if(command.getCommandType() == CommandType.UPDATE_TURN) {
            turnCount++;
        }
        Platform.runLater(() -> {
            gameController.UpdateMap();
            gameController.setMessage(command);
        });
    }

    /**
     * Manage the client's close
     * @param command message from server
     * @param client object representing a client connected
     */
    public void CloseHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> Communication(command, client));
        client.setActive(false);
    }

    /**
     * Select the correct scene based on messages from server
     * @param command message from server
     * @param client object representing a client connected
     */
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

    public int getTurnCount() {
        return turnCount;
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
