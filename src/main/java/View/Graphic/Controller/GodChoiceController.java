package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import CommunicationProtocol.SantoriniInfo.GodInfo;
import CommunicationProtocol.ServerMsg;
import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class GodChoiceController implements GuiController{
    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;
    private ArrayList<Integer> index;
    private ArrayList<ImageView> full;

    @FXML
    Pane main;
    @FXML
    GridPane grid;
    @FXML
    Label godname, godpower, query;
    @FXML
    ImageView anonymousfull, drape;

    private Effect glow = new Glow();
    private Effect frame = new ColorAdjust(0, 0.7, 0, 0.2);

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void SetUp(CommandMsg command, ConnectionHandler client){
        this.command = command;
        this.client = client;
        index = new ArrayList<>();
        full = new ArrayList<>();
        for (Node image : main.getChildren()){
            if (image.getId()!=null && image.getId().endsWith("full")){
                full.add((ImageView)image);
            }
        }
        full.remove(anonymousfull);
        if(command.getCommandType() == CommandType.NUMBER){
            query.setText("Select a God");
            for (GodInfo god : command.getInfo().getGods()){
                grid.getChildren().get(god.getPosition()).setDisable(false);
                grid.getChildren().get(god.getPosition()+14).setDisable(false);
                grid.getChildren().get(god.getPosition()).setEffect(null);
                grid.getChildren().get(god.getPosition()+14).setEffect(null);
            }
        }
        else{
            query.setText("Select the Gods");
            for(Node node : grid.getChildren()){
                node.setDisable(false);
                node.setEffect(null);
            }
        }
    }

    public void ChangeBoxFocus(MouseEvent e){
        Node node = (Node)e.getSource();
        int index = grid.getChildren().indexOf(node);
        if(node.getEffect() == null){
            drape.setOpacity(1);
            godname.setText(gui.getGods()[index].getName());
            godpower.setText(gui.getGods()[index].getPower());
            node.setEffect(glow);
            full.get(index).setOpacity(1);
            anonymousfull.setOpacity(0);
        }
        else{
            drape.setOpacity(0);
            godname.setText(null);
            godpower.setText(null);
            node.setEffect(null);
            anonymousfull.setOpacity(1);
            full.get(index).setOpacity(0);
        }
    }

    public void select(MouseEvent e){
        Node source = (Node)e.getSource();
        int number = grid.getChildren().indexOf(source);
        if(command.getCommandType() == CommandType.NUMBER){
            index.add(number);
            grid.getChildren().get(number + 14).setEffect(frame);
            gui.Confirm(command, client, this);
        }
        else{
            if (!index.contains(number)){
                index.add(number);
                grid.getChildren().get(number + 14).setEffect(frame);
                if (index.size() == command.getInfo().getPlayers().length){
                    gui.Confirm(command, client, this);
                }
            }
        }
    }

    public void reset(){
        index = new ArrayList<>();
        for(int i = 14; i < grid.getChildren().size(); i++){
            grid.getChildren().get(i).setEffect(null);
        }
    }

    public void restore(){
        for(Node node : grid.getChildren()){
            if(grid.getChildren().indexOf(node) < 14){
                node.setEffect(new ColorAdjust(0, -1, 0, 0));
            }
            else {
                node.setEffect(null);
            }
            node.setDisable(true);
        }
    }

    public ArrayList<Integer> getIndex() {
        return index;
    }
}
