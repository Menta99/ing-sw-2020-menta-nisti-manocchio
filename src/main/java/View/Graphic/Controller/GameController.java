package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import Model.Godcards.GodsEnum;
import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void SetUp(CommandMsg command, ConnectionHandler client) {
        this.command = command;
        this.client = client;
        int i;
        for (Node group : gridPane.getChildren()) {
            if (group.getId().startsWith("cell_")) {
                cells.add((Group) group);
                //((Group) group).getChildren().get(3).setOpacity(1);
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

}