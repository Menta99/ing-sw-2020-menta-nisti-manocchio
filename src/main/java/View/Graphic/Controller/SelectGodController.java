package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.ServerMsg;
import Server.ClientHandler;
import View.Graphic.Gui;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SelectGodController implements GuiController{

    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;
    private ArrayList<Integer> index = new ArrayList<>();
    private ArrayList<ImageView> full = new ArrayList<>();

    @FXML
    ImageView apolloicon, athenaicon, artemisicon, atlasicon, chronusicon, demetericon, hephaestusicon, heraicon, hestiaicon, minotauricon, panicon, prometeoicon, tritonicon, zeusicon;
    @FXML
    ImageView apollofull, athenafull, artemisfull, atlasfull, chronusfull, demeterfull, hephaestusfull, herafull, hestiafull, minotaurfull, panfull, prometeofull, tritonfull, zeusfull, anonymousfull;
    @FXML
    ImageView cornice_apollo, cornice_athena, cornice_artemis, cornice_atlas, cornice_chronus, cornice_demeter, cornice_hephaestus, cornice_hera, cornice_hestia, cornice_minotaur, cornice_pan, cornice_prometeo, cornice_triton, cornice_zeus;
    @FXML
    Label godname, godpower;
    @FXML
    GridPane grid;
    @FXML
    Pane main;

    private Effect glow = new Glow();
    private Effect frame = new ColorAdjust(0, 0.7, 0, 0.2);

    public void show(MouseEvent e){
        int index = grid.getChildren().indexOf((Node)e.getSource());
        godname.setText(gui.getGods()[index].getName());
        godpower.setText(gui.getGods()[index].getPower());
        ((Node) e.getSource()).setEffect(glow);
        full.get(index).setOpacity(1);
        hideOthers(index);
    }

    public void select(MouseEvent e){
        Node source = (Node)e.getSource();
        int number = grid.getChildren().indexOf(source);
        /*int x = number%4;
        int y = number/4;*/
        if (!index.contains(number)){
            index.add(number);
            grid.getChildren().get(number + 14).setEffect(frame);
            if (index.size() == command.getInfo().getPlayers().length){
                client.WriteMessage(new ServerMsg(index));
            }
        }
    }


    public void hideOthers(int index){
        anonymousfull.setOpacity(0);
        for (ImageView image : full){
            if (full.indexOf(image)!=index){
                image.setOpacity(0);
                grid.getChildren().get(full.indexOf(image)).setEffect(null);
            }
        }
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void SetUp(CommandMsg command, ConnectionHandler client){
        this.command = command;
        this.client = client;
        for (Node image : main.getChildren()){
            if (image.getId()!=null && image.getId().endsWith("full")){
                full.add((ImageView)image);
            }
        }
    }
}
