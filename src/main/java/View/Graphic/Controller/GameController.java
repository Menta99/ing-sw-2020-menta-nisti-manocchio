package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import CommunicationProtocol.SantoriniInfo.BoxInfo;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import CommunicationProtocol.ServerMsg;
import Model.PawnType;
import View.CellType;
import View.Colors;
import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;


/**
 * Controller class for fxml Game file
 */
public class GameController implements GuiController {
    @FXML
    GridPane gridPane;
    @FXML
    Label name_0, name_1, name_2, god_name_0, god_name_1, god_name_2, message, turn;
    @FXML
    ImageView god_0, god_1, god_2;
    @FXML
    Group player_0, player_1, player_2;

    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;
    private ArrayList<Group> cells = new ArrayList<>();
    private ArrayList<Integer> box = new ArrayList<>();
    private Media move;
    private Media select;
    private Media build;

    private final Effect glow = new Glow();
    private final ArrayList<Effect> player_colors = new ArrayList<>();

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    /**
     * SetUp of the Game controller
     * @param command message from the server containing information
     * @param client reference to the ConnectionHandler of the Client
     */
    public void SetUp(CommandMsg command, ConnectionHandler client) {
        this.command = command;
        this.client = client;
        move = new Media(new File("src/main/resources/Cells/Music/Move.wav").toURI().toString());
        build = new Media(new File("src/main/resources/Cells/Music/Build.wav").toURI().toString());
        select = new Media(new File("src/main/resources/Cells/Music/Select.wav").toURI().toString());
        player_colors.add(new ColorAdjust(0.3, 0, 0, 0.3));
        player_colors.add(new ColorAdjust(-0.8, 0, 0, 0.3));
        player_colors.add(new ColorAdjust(0, 0, 0, 0));
        for (Node group : gridPane.getChildren()) {
            if (group.getId().startsWith("cell_")) {
                cells.add((Group) group);
            }
        }
        InitializeCards(gui.getPlayers());
    }

    /**
     * SetUp of the Game controller in the case of a Pose message
     * @param command Pose message from the server containing the information
     * @param client reference to the ConnectionHandler of the Client
     */
    public void SetUpPose(CommandMsg command, ConnectionHandler client){
        this.command = command;
        this.client = client;
        this.box = new ArrayList<>();
        UpdateMap();
        EnableBoxes();
        setMessage(command);
    }

    /**
     * Sends the box coordinates
     * @param e interaction from the user
     */
    public void SendBox(MouseEvent e){
        if(command.getCommandType() == CommandType.POS_BUILD){
            new MediaPlayer(build).play();
        }
        else if(command.getCommandType() == CommandType.POS_INITIAL || command.getCommandType() == CommandType.POS_WORKER){
            new MediaPlayer(select).play();
        }
        else{
            new MediaPlayer(move).play();
        }
        Group group = (Group) e.getSource();
        box.add(cells.indexOf(group)%5);
        box.add(cells.indexOf(group)/5);
        DisableBoxes();
        client.WriteMessage(new ServerMsg(box));
    }

    /**
     * Updates the map structure based on the information in the MapInfo[]
     */
    public void UpdateMap(){
        turn.setText("Turn count : " + gui.getTurnCount());
        BoxInfo box;
        for(Group group : cells){
            box = gui.getMap()[cells.indexOf(group)%5][cells.indexOf(group)/5];
            if(box.getLastBuilding() == PawnType.DOME){
                LoadDome(box, group);
            }
            else if (box.getLastBuilding() == PawnType.WORKER){
                LoadWorker(box, group);
            }
            else{
                LoadTower(box, group);
            }
        }
    }

    /**
     * Enables the boxes which are intractable
     */
    public void EnableBoxes(){
        BoxInfo box;
        for(Group group : cells){
            box = gui.getMap()[cells.indexOf(group)%5][cells.indexOf(group)/5];
            switch (command.getCommandType()){
                case POS_INITIAL:
                    if(box.getLastBuilding() != PawnType.WORKER){
                        LoadPosWorker(box, group);
                        group.setDisable(false);
                    }
                    break;
                case POS_WORKER:
                    if((box.getWorkerColor() != Colors.RESET) && (gui.getPlayers()[box.getWorkerColor().ordinal()].getName().equalsIgnoreCase(gui.getNickname()))){
                        LoadPosWorker(box, group);
                        group.setDisable(false);
                    }
                    break;
                case POS_MOVE:
                case POS_BUILD:
                    if(box.getBoxColor() != Colors.RESET){
                        group.setDisable(false);
                        if(command.getCommandType() == CommandType.POS_BUILD){
                            group.getChildren().get(0).setEffect(new ColorAdjust(-0.9, 0, 0, 0.1));
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Disables the boxes at the end of the interaction
     */
    public void DisableBoxes(){
        for(Group group : cells){
            group.setDisable(true);
            group.getChildren().get(0).setEffect(null);
        }
    }

    /**
     * Change the focus of a box, when enters it glows, when exits is restored to normal effect
     * @param e interaction from the user
     */
    public void ChangeBoxFocus(MouseEvent e){
        Group group = (Group)e.getSource();
        if(group.getEffect() == null){
            group.setEffect(glow);
        }
        else{
            group.setEffect(null);
        }
    }

    /**
     * Initialize the card info, with the corresponding image, god-name and player-name
     * @param players Array of the players in the game
     */
    public void InitializeCards(PlayerInfo[] players){
        int i = 0;
        if(players.length == 2){
            player_2.setDisable(true);
            player_2.toBack();
        }
        for (PlayerInfo player : players) {
            switch (i) {
                case 0:
                    name_0.setText(player.getName());
                    god_name_0.setText(gui.getGods()[player.getGod()].getName());
                    god_0.setImage(new Image("Cells/GodCard/" + god_name_0.getText().trim() + "_podium.png", true));
                    break;
                case 1:
                    name_1.setText(player.getName());
                    god_name_1.setText(gui.getGods()[player.getGod()].getName());
                    god_1.setImage(new Image("Cells/GodCard/" + god_name_1.getText().trim() + "_podium.png", true));
                    break;

                case 2:
                    name_2.setText(player.getName());
                    god_name_2.setText(gui.getGods()[player.getGod()].getName());
                    god_2.setImage(new Image("Cells/GodCard/" + god_name_2.getText().trim() + "_podium.png", true));
                    break;
            }
            i++;
        }
    }

    /**
     * Load a box that has a dome on top
     * @param box box to load
     * @param group cell from the grid pane to load
     */
    public void LoadDome(BoxInfo box, Group group){
        CellType type = CellType.DOME;
        ImageView tower = (ImageView) group.getChildren().get(0);
        ImageView worker = (ImageView) group.getChildren().get(1);
        switch (box.getHeight()){
                case 2:
                    type = CellType.DOME;
                    break;
                case 3:
                    type = CellType.LEVEL_1D;
                    break;
                case 4:
                    type = CellType.LEVEL_12D;
                    break;
                case 5:
                    type = CellType.LEVEL_123D;
                    break;
        }
        if(type == CellType.DOME){
            tower.setImage(null);
            worker.setImage(new Image(type.toString(), true));
        }
        else {
            tower.setImage(new Image(type.toString(), true));
            worker.setImage(null);
        }
    }

    /**
     * load a dome without a worker or a dome on top
     * @param box box to load
     * @param group cell from the grid pane to load
     */
    public void LoadTower(BoxInfo box, Group group){
        CellType type = CellType.VOID;
        ImageView tower = (ImageView) group.getChildren().get(0);
        ImageView worker = (ImageView) group.getChildren().get(1);
        if(box.getBoxColor() != Colors.RESET){
            switch (box.getHeight()) {
                case 1:
                    type = CellType.GLOW;
                    break;
                case 2:
                    type = CellType.LEVEL_1_E;
                    break;
                case 3:
                    type = CellType.LEVEL_12_E;
                    break;
                case 4:
                    type = CellType.LEVEL_123_E;
                    break;
            }
        }
        else {
            switch (box.getHeight()) {
                case 1:
                    type = CellType.VOID;
                    break;
                case 2:
                    type = CellType.LEVEL_1;
                    break;
                case 3:
                    type = CellType.LEVEL_12;
                    break;
                case 4:
                    type = CellType.LEVEL_123;
                    break;
            }
        }
        if(type == CellType.VOID){
            tower.setImage(null);
        }
        else {
            tower.setImage(new Image(type.toString(), true));
        }
        worker.setImage(null);
    }

    /**
     * load a dome with a worker on top
     * @param box box to load
     * @param group cell from the grid pane to load
     */
    public void LoadWorker(BoxInfo box, Group group){
        CellType towerType = CellType.VOID;
        CellType workerType = CellType.VOID;
        ImageView tower = (ImageView)group.getChildren().get(0);
        ImageView worker = (ImageView)group.getChildren().get(1);
        switch (box.getWorkerColor()){
            case GREEN:
                workerType = CellType.PLAYER_0;
                break;
            case BLUE:
                workerType = CellType.PLAYER_1;
                break;
            case RED:
                workerType = CellType.PLAYER_2;
                break;
        }
        if(box.getBoxColor() != Colors.RESET){
            switch (box.getHeight()){
                case 2:
                    towerType = CellType.GLOW;
                    break;
                case 3:
                    towerType = CellType.LEVEL_1_E;
                    break;
                case 4:
                    towerType = CellType.LEVEL_12_E;
                    break;
                case 5:
                    towerType = CellType.LEVEL_123_E;
                    break;
            }
        }
        else{
            switch (box.getHeight()){
                case 2:
                    towerType = CellType.VOID;
                    break;
                case 3:
                    towerType = CellType.LEVEL_1;
                    break;
                case 4:
                    towerType = CellType.LEVEL_12;
                    break;
                case 5:
                    towerType = CellType.LEVEL_123;
                    break;
            }
        }
        if(towerType == CellType.VOID){
            tower.setImage(null);
        }
        else {
            tower.setImage(new Image(towerType.toString(), true));
        }
        if(workerType == CellType.VOID){
            worker.setImage(null);
        }
        else {
            worker.setImage(new Image(workerType.toString(), true));
        }
    }

    /**
     * Load the box in the initial phase or in the select worker phase
     * @param box box to load
     * @param group cell from the grid pane to load
     */
    public void LoadPosWorker(BoxInfo box, Group group){
        CellType towerType = CellType.VOID;
        ImageView tower = (ImageView) group.getChildren().get(0);
        switch (box.getHeight()){
            case 1:
            case 2:
                towerType = CellType.GLOW;
                break;
            case 3:
                towerType = CellType.LEVEL_1_E;
                break;
            case 4:
                towerType = CellType.LEVEL_12_E;
                break;
            case 5:
                towerType = CellType.LEVEL_123_E;
                break;
        }
        if(towerType == CellType.VOID){
            tower.setImage(null);
        }
        else {
            tower.setImage(new Image(towerType.toString(), true));
        }
    }

    /**
     * Setup the message on the top label
     * @param command message to elaborate
     */
    public void setMessage(CommandMsg command){
        switch (command.getCommandType()){
            case POS_INITIAL:
                message.setText("Place your Workers");
                break;
            case POS_WORKER:
                message.setText("Select a Worker");
                break;
            case POS_MOVE:
                message.setText("Move Phase: Select a Box");
                break;
            case POS_BUILD:
                message.setText("Build Phase: Select a Box");
                break;
            case UPDATE_ACTION:
                if(command.getInfo().getPlayers()!=null){
                    PlayerInfo user = command.getInfo().getPlayers()[0];
                    if(!user.getName().equalsIgnoreCase(gui.getNickname())){
                        message.setText("Look at " + user.getName() + "'s move");
                    }
                }
                break;
            case UPDATE_TURN:
                PlayerInfo actual = command.getInfo().getPlayers()[0];
                if(actual.getName().equalsIgnoreCase(gui.getNickname())){
                    message.setText(actual.getName() + " is your Turn!");
                }
                else {
                    message.setText(actual.getName() + "'s playing, wait!");
                }
                break;
        }
    }

    /**
     * Creates a window with the information of the card selected
     * @param e interaction from the user
     */
    public void Info(MouseEvent e){
        int number;
        Group group = (Group) e.getSource();
        if(group == player_0) {
            number = 0;
        }
        else if(group == player_1) {
            number = 1;
        }
        else{
            number = 2;
        }
        gui.CardInfo(number);
    }
}