package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.SantoriniInfo.GodInfo;
import CommunicationProtocol.ServerMsg;
import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GodPickController implements Initializable, GuiController {
    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;
    private ArrayList<Integer> index = new ArrayList<>();
    private ArrayList<ImageView> full = new ArrayList<>();
    private ArrayList<ImageView> active = new ArrayList<>();

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
        index.add(number);
        client.WriteMessage(new ServerMsg(index));
    }


    public void hideOthers(int index){
        ImageView icon;
        anonymousfull.setOpacity(0);
        for (ImageView image : full){
            if (full.indexOf(image)!=index){
                image.setOpacity(0);
                icon = (ImageView) grid.getChildren().get(full.indexOf(image));
                if(active.contains(icon)){
                    icon.setEffect(null);
                }
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
        full.remove(anonymousfull);
        for (GodInfo god : command.getInfo().getGods()){
            active.add((ImageView) grid.getChildren().get(god.getPosition()));
            grid.getChildren().get(god.getPosition()).setDisable(false);
            grid.getChildren().get(god.getPosition()+14).setDisable(false);
            grid.getChildren().get(god.getPosition()).setEffect(null);
            grid.getChildren().get(god.getPosition()+14).setEffect(null);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
