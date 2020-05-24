package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CommunicationController implements GuiController {
    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;

    @FXML
    Label close_lbl, text_lbl;
    @FXML
    ImageView pressed_btn, greek;
    @FXML
    Group win_tr, lose_tr;

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void Close(MouseEvent e){
        gui.getDialog().close();
        Switch();
        win_tr.setOpacity(0);
        lose_tr.setOpacity(0);
        if(command == null) {
            gui.getPrimaryStage().close();
        }
        else{
            switch (command.getCommandType()){
                case COM_INVALID_POS:
                case COM_INVALID_WORKER:
                case COM_LOSE:
                    break;
                case CLOSE_ANOMALOUS:
                case CLOSE_NORMAL:
                case CLOSE_RESTART:
                case CLOSE_SERVER:
                    gui.getPrimaryStage().close();
                    break;
            }
        }
    }

    public void SetUp(CommandMsg command, ConnectionHandler client){
        this.command = command;
        this.client = client;
        Switch();
        PlayerInfo player;
        if(command != null) {
            close_lbl.setText("Ok");
            switch (command.getCommandType()){
                case COM_INVALID_POS:
                    text_lbl.setText("Invalid Box");
                    break;
                case COM_INVALID_WORKER:
                    text_lbl.setText("Invalid Worker");
                    break;
                case COM_LOSE:
                    lose_tr.setOpacity(1);
                    text_lbl.setText("You Lost!");
                    break;
                case CLOSE_ANOMALOUS:
                    player = command.getInfo().getPlayers()[0];
                    text_lbl.setText(player.getName() + " disconnected");
                    break;
                case CLOSE_NORMAL:
                    player = command.getInfo().getPlayers()[0];
                    if (gui.getNickname().equalsIgnoreCase(player.getName())) {
                        win_tr.setOpacity(1);
                        greek.setImage(new Image("Cells/Ambient/window.png", true));
                        text_lbl.setText("You Won");
                    } else {
                        lose_tr.setOpacity(1);
                        text_lbl.setText(player.getName() + " has Won!");
                    }
                    break;
                case CLOSE_RESTART:
                    text_lbl.setText("Game in Progress");
                    break;
                case CLOSE_SERVER:
                    text_lbl.setText("Server is down");
                    break;
            }
        }
        else {
            text_lbl.setText("Server is down");
        }
    }

    public void pressButton(MouseEvent e){
        pressed_btn.setOpacity(1);
        close_lbl.setLayoutY(216);
    }

    public void releaseButton(MouseEvent e){
        pressed_btn.setOpacity(0);
        close_lbl.setLayoutY(213);
    }

    public void Switch(){
        Node root = gui.getPrimaryStage().getScene().getRoot();
        if(root.getEffect()==null) {
            root.setEffect(new ColorAdjust(0, -1, 0, 0));
        }
        else{
            root.setEffect(null);
        }
    }
}
