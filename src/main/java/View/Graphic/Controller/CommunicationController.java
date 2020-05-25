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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * Controller class for fxml Communication file
 */
public class CommunicationController implements GuiController {
    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;
    private Media click;
    private MediaPlayer player;

    @FXML
    Label close_lbl, text_lbl;
    @FXML
    ImageView pressed_btn, greek;
    @FXML
    Group win_tr, lose_tr;

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    /**
     * Close the primary stage if needed
     * @param e User interaction
     */
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

    /**
     * Prints the correct message for every command
     * @param command message from server
     * @param client object representing a client connected
     */
    public void SetUp(CommandMsg command, ConnectionHandler client){
        this.command = command;
        this.client = client;
        click = new Media(new File("src/main/resources/Cells/Music/Click.wav").toURI().toString());
        player = new MediaPlayer(click);
        player.setRate(1.5);
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
                    if(command.getInfo()!=null) {
                        player = command.getInfo().getPlayers()[0];
                        text_lbl.setText(player.getName() + " disconnected");
                    }
                    else{
                        text_lbl.setText("Unknown player disconnected");
                    }
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

    /**
     * Modify opacity of button pressed
     * @param e User interaction
     */
    public void pressButton(MouseEvent e){
        player.play();
        pressed_btn.setOpacity(1);
        close_lbl.setLayoutY(216);
    }

    /**
     * Modify opacity of button released
     * @param e User interaction
     */
    public void releaseButton(MouseEvent e){
        pressed_btn.setOpacity(0);
        close_lbl.setLayoutY(213);
        player.stop();
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
