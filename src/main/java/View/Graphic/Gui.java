package View.Graphic;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.SantoriniInfo.BoxInfo;
import CommunicationProtocol.SantoriniInfo.GodInfo;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import CommunicationProtocol.ServerMsg;
import View.Graphic.Controller.*;
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
import java.util.ArrayList;

public class Gui extends Application implements View {
    private final int PORT_NUM = 5555;
    private final String IP = "127.0.0.1";
    private Socket server;

    private Stage primaryStage;
    private Stage dialog;
    private Scene welcomeScene;
    private Scene errorWelcomeScene;
    private Scene nameScene;
    private Scene selectGodScene;
    private Scene waitPlayerScene;
    private Scene numberScene;
    private Scene resumeScene;
    private Scene waitChoiceScene;
    private Scene godPickScene;
    private WelcomeController welcomeController;
    private ErrorWelcomeController errorWelcomeController;
    private NameController nameController;
    private SelectGodController selectGodController;
    private WaitPlayerController waitPlayerController;
    private NumberController numberController;
    private ResumeController resumeController;
    private WaitChoiceController waitChoiceController;
    private GodPickController godPickController;

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
            errorWelcomeScene = new Scene(root, 360, 100);
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
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/WaitingPlayers.fxml").toURI().toURL());
            root = loader.load();
            waitPlayerController = loader.getController();
            waitPlayerScene = new Scene(root, 800, 600);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/WaitingChoice.fxml").toURI().toURL());
            root = loader.load();
            waitChoiceController = loader.getController();
            waitChoiceScene = new Scene(root, 800, 600);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Number.fxml").toURI().toURL());
            root = loader.load();
            numberController = loader.getController();
            numberScene = new Scene(root, 800, 600);
            loader = new FXMLLoader(new File("src/main/java/View/Graphic/FXML/Resume.fxml").toURI().toURL());
            root = loader.load();
            resumeController = loader.getController();
            resumeScene = new Scene(root, 800, 600);

        }
        catch (IOException e){
            System.err.println("problems initScenes");
        }
    }

    public void initControllers(){
        welcomeController.setGui(this);
        errorWelcomeController.setGui(this);
        nameController.setGui(this);
        waitPlayerController.setGui(this);
        selectGodController.setGui(this);
        numberController.setGui(this);
        resumeController.setGui(this);
        waitChoiceController.setGui(this);
        godPickController.setGui(this);
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

    public void FirstHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> SwitchScene(numberScene));
        numberController.SetUp(command, client);
    }

    public void NumberHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> SwitchScene(godPickScene));
        godPickController.SetUp(command, client);
    }

    public void CommunicationHandler(CommandMsg command, ConnectionHandler client) {
        switch (command.getCommandType()) {
            case COM_WELCOME:
            case COM_RESTART:
                gods = command.getInfo().getGods();
                players = command.getInfo().getPlayers();
                break;
            case COM_GODS:
                GodInGame(command.getInfo().getGods());
                break;
            case COM_CHOSEN:
                //PlayerGod(command.getInfo().getPlayers());
                //for (PlayerInfo user : players) {
                //   System.out.println("Player: " + user.getColor() + user.getName() + Colors.RESET + " GodCard: " + gods[user.getGod()].getName());
                //}
                break;
            case COM_WAIT_CHOICE:
                //System.out.println("Waiting the other player's choice");
                Platform.runLater(() -> SwitchScene(waitChoiceScene));
                waitChoiceController.SetUp(command, client);
                break;
            case COM_WAIT_LOBBY:
                //System.out.println("Waiting the other players in the Lobby");
                Platform.runLater(() -> SwitchScene(waitPlayerScene));
                waitPlayerController.SetUp(command, client);
                break;
            case COM_INVALID_WORKER:
                System.out.println("Not a valid worker, retry");
                break;
            case COM_INVALID_POS:
                System.out.println("Not a valid destination, retry");
                break;
            case COM_LOSE:
                System.out.println("You lose, Dumbos");
                break;
        }
    }

    public void AnswerHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> SwitchScene(resumeScene));
        resumeController.SetUp(command, client);
    }

    public void GodHandler(CommandMsg command, ConnectionHandler client){
        Platform.runLater(() -> SwitchScene(selectGodScene));
        selectGodController.SetUp(command, client);
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
