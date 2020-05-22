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

import java.util.ArrayList;

public class GameController implements GuiController {
    @FXML
    GridPane gridPane;
    @FXML
    Label name_0, name_1, name_2, god_name_0, god_name_1, god_name_2, message;
    @FXML
    ImageView god_0, god_1, god_2;

    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;
    private ArrayList<Group> cells = new ArrayList<>();
    private ArrayList<Integer> box = new ArrayList<>();

    private final Effect glow = new Glow();
    private final ArrayList<Effect> player_colors = new ArrayList<>();

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void SetUp(CommandMsg command, ConnectionHandler client) {
        this.command = command;
        this.client = client;
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

    public void SetUpPose(CommandMsg command, ConnectionHandler client){
        this.command = command;
        this.client = client;
        this.box = new ArrayList<>();
        UpdateMap();
        EnableBoxes();
        setMessage(command);
    }

    public void SendBox(MouseEvent e){
        Group group = (Group) e.getSource();
        box.add(cells.indexOf(group)%5);
        box.add(cells.indexOf(group)/5);
        DisableBoxes();
        client.WriteMessage(new ServerMsg(box));
    }

    public void UpdateMap(){
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

    public void DisableBoxes(){
        for(Group group : cells){
            group.setDisable(true);
            group.getChildren().get(0).setEffect(null);
        }
    }

    public void ChangeBoxFocus(MouseEvent e){
        Group group = (Group)e.getSource();
        if(group.getEffect() == null){
            group.setEffect(glow);
        }
        else{
            group.setEffect(null);
        }
    }

    public void InitializeCards(PlayerInfo[] players){
        int i = 0;
        for (PlayerInfo player : players) {
            switch (i) {
                case 0:
                    name_0.setText(player.getName());
                    god_name_0.setText(gui.getGods()[player.getGod()].getName());
                    god_0.setImage(new Image("Texture2D/podium-characters-" + god_name_0.getText().trim() + ".png", true));
                    break;
                case 1:
                    name_1.setText(player.getName());
                    god_name_1.setText(gui.getGods()[player.getGod()].getName());
                    god_1.setImage(new Image("Texture2D/podium-characters-" + god_name_1.getText().trim() + ".png", true));
                    break;

                case 2:
                    name_2.setText(player.getName());
                    god_name_2.setText(gui.getGods()[player.getGod()].getName());
                    god_2.setImage(new Image("Texture2D/podium-characters-" + god_name_2.getText().trim() + ".png", true));
                    break;
            }
            i++;
        }
    }

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
}