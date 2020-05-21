package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import CommunicationProtocol.SantoriniInfo.BoxInfo;
import CommunicationProtocol.SantoriniInfo.GodInfo;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import CommunicationProtocol.ServerMsg;
import Model.Godcards.GodsEnum;
import Model.PawnType;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GameController implements GuiController {
    @FXML
    GridPane gridPane;
    @FXML
    Label name_0, name_1, name_2, god_name_0, god_name_1, god_name_2;
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
        int i;
        player_colors.add(new ColorAdjust(0.3, 0, 0, 0.3));
        player_colors.add(new ColorAdjust(-0.8, 0, 0, 0.3));
        player_colors.add(new ColorAdjust(0, 0, 0, 0));
        for (Node group : gridPane.getChildren()) {
            if (group.getId().startsWith("cell_")) {
                cells.add((Group) group);
            }
        }
        i=0;
        for (PlayerInfo player : gui.getPlayers()) {
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

    public void SetUpPose(CommandMsg command, ConnectionHandler client){
        this.command = command;
        this.client = client;
        this.box = new ArrayList<>();
        UpdateMap();
        EnableBoxes();
    }

    public void SendBox(MouseEvent e){
        Node source = (Node) e.getSource();
        Group group = (Group) source.getParent();
        box.add(cells.indexOf(group)%5);
        box.add(cells.indexOf(group)/5);
        DisableBoxes();
        client.WriteMessage(new ServerMsg(box));
    }

    public void UpdateMap(){
        BoxInfo box;
        int index;
        for(Group group : cells){
            box = gui.getMap()[cells.indexOf(group)%5][cells.indexOf(group)/5];
            for(Node node : group.getChildren()){
                index = group.getChildren().indexOf(node);
                if(box.getLastBuilding() == PawnType.DOME){
                    if(index == 4 || index < box.getHeight()-2){
                        node.setOpacity(1);
                    }
                    else{
                        node.setOpacity(0);
                    }
                }
                else if(box.getLastBuilding() == PawnType.WORKER){
                    if(index==3 ||index < box.getHeight()-2){
                        node.setOpacity(1);
                        if(index == 3){
                            node.setEffect(player_colors.get(box.getWorkerColor().ordinal()));
                        }
                    }
                    else{
                        node.setOpacity(0);
                    }
                }
                else{
                    if(index < box.getHeight()-1){
                        node.setOpacity(1);
                    }
                    else{
                        node.setOpacity(0);
                    }
                }
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
                        group.setDisable(false);
                        group.getChildren().get(5).setOpacity(1);
                        group.getChildren().get(5).setDisable(false);
                    }
                    break;
                case POS_WORKER:
                    if((box.getWorkerColor() != Colors.RESET) && (gui.getPlayers()[box.getWorkerColor().ordinal()].getName().equalsIgnoreCase(gui.getNickname()))){
                        group.setDisable(false);
                        group.getChildren().get(5).setOpacity(1);
                        group.getChildren().get(5).setDisable(false);
                    }
                    break;
                case POS_MOVE:
                case POS_BUILD:
                    if(box.getBoxColor() != Colors.RESET){
                        group.setDisable(false);
                        if(command.getCommandType() == CommandType.POS_BUILD){
                            group.getChildren().get(5).setEffect(new ColorAdjust(-0.9, 0, 0, 0.1));
                        }
                        group.getChildren().get(5).setOpacity(1);
                        group.getChildren().get(5).setDisable(false);
                    }
                    break;
            }
        }
    }

    public void DisableBoxes(){
        for(Group group : cells){
            group.setDisable(true);
            group.getChildren().get(5).setEffect(null);
            group.getChildren().get(5).setOpacity(0);
            group.getChildren().get(5).setDisable(true);
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
}