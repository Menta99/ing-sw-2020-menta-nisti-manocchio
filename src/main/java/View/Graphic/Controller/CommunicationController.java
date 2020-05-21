package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import View.Colors;
import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void Close(MouseEvent e){
        gui.getDialog().close();
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
                    text_lbl.setText("You Lose, Dumbo");
                    break;
                case CLOSE_ANOMALOUS:
                    player = command.getInfo().getPlayers()[0];
                    text_lbl.setText(player.getName() + " disconnected\nEndGame");
                    break;
                case CLOSE_NORMAL:
                    player = command.getInfo().getPlayers()[0];
                    if (gui.getNickname().equalsIgnoreCase(player.getName())) {
                        greek.setImage(new Image("Texture2D/endgame_victorywin.png", true));
                        text_lbl.setText("You have won\nCongratulations");
                    } else {
                        text_lbl.setText(player.getName() + " has won!\nClap your hands!");
                    }
                    break;
                case CLOSE_RESTART:
                    text_lbl.setText("A game is already started, try later");
                    break;
                case CLOSE_SERVER:
                    text_lbl.setText("Server is down");
                    break;
            }
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
}
