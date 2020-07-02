package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import CommunicationProtocol.ServerMsg;
import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;

/**
 * Controller class for Login fxml file
 */
public class LoginController implements GuiController{
    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;
    private boolean single;
    private Media click;
    private MediaPlayer player;

    @FXML
    TextField nickField;
    @FXML
    Label query, lbl_left, lbl_right, lbl_center;
    @FXML
    ImageView prs_btn_left, btn_left, prs_btn_right, btn_right, prs_confirm, confirm;
    @FXML
    Group left_right, name;

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    /**
     * Print the correct message based on situation
     * @param command message from server
     * @param client object representing a client connected
     */
    public void SetUp(CommandMsg command, ConnectionHandler client){
        this.command = command;
        this.client = client;
        click = new Media(getClass().getResource("/Cells/Music/Click.wav").toString());
        player = new MediaPlayer(click);
        player.setVolume(gui.getVolume());
        player.setRate(1.5);
        switch (command.getCommandType()){
            case NAME:
                single = true;
                Switch();
                break;
            case FIRST:
                single = false;
                Switch();
                query.setText("How many Players?");
                lbl_left.setText("2");
                lbl_right.setText("3");
                break;
            case ANS_RESTART:
                single = false;
                Switch();
                query.setText("Resume the Game?");
                lbl_left.setText("Yes");
                lbl_right.setText("No");
                break;
        }
    }

    /**
     * Set the correct option for some fxml group
     */
    public void Switch(){
        if(single) {
            name.setDisable(false);
            name.toFront();
            left_right.setDisable(true);
            left_right.toBack();
        }
        else{
            left_right.setDisable(false);
            left_right.toFront();
            name.setDisable(true);
            name.toBack();
        }
    }

    /**
     * Modify opacity of button pressed
     * @param e User interaction
     */
    public void press(MouseEvent e){
        player.play();
        if(single) {
            confirm.setOpacity(0);
            lbl_center.setLayoutY(0);
        }
        else{
            if(e.getSource() == lbl_left){
                btn_left.setOpacity(0);
                lbl_left.setLayoutY(3);
            }
            else if(e.getSource() == lbl_right){
                btn_right.setOpacity(0);
                lbl_right.setLayoutY(3);
            }
        }
    }

    /**
     * Modify opacity of button released
     * @param e User interaction
     */
    public void release(MouseEvent e){
        if(single) {
            confirm.setOpacity(1);
            lbl_center.setLayoutY(-3);
        }
        else{
            if(e.getSource() == lbl_left){
                btn_left.setOpacity(1);
                lbl_left.setLayoutY(0);
            }
            else if(e.getSource() == lbl_right){
                btn_right.setOpacity(1);
                lbl_right.setLayoutY(0);
            }
        }
        player.stop();
    }

    /**
     * Check the information entered by the user
     * @param e User Interaction
     */
    public void select(MouseEvent e){
        switch (command.getCommandType()){
            case NAME:
                CheckName();
                break;
            case FIRST:
                CheckFirst((Node) e.getSource());
                break;
            case ANS_RESTART:
                CheckRestart((Node) e.getSource());
                break;
        }
    }

    /**
     * Write the correct message based on player's choice
     * @param node graphic element
     */
    public void CheckRestart(Node node){
        if(node == lbl_left){
            client.WriteMessage(new ServerMsg("yes"));
        }
        else if(node == lbl_right){
            client.WriteMessage(new ServerMsg("no"));
        }
    }

    /**
     * Select the correct game's player based on challenger's choice
     * @param node graphic element
     */
    public void CheckFirst(Node node){
        ArrayList<Integer> output = new ArrayList<>();
        if(node == lbl_left){
            output.add(2);
        }
        else if(node == lbl_right){
            output.add(3);
        }
        client.WriteMessage(new ServerMsg(output));
    }

    /**
     * Check if a valid nick was entered
     */
    public void CheckName(){
        boolean found = false;
        String msg = nickField.getText().trim();
        nickField.clear();
        if(msg.equals("")){
            lbl_center.requestFocus();
            nickField.setPromptText("Insert a non-void name");
        }
        else{
            found = true;
            if(command.getInfo() != null) {
                for (PlayerInfo player : command.getInfo().getPlayers()) {
                    if (player.getName().equalsIgnoreCase(msg)) {
                        found = false;
                        break;
                    }
                }
                if (!found) {
                    lbl_center.requestFocus();
                    nickField.setPromptText("Name already chosen");
                }
            }
        }
        if(found){
            gui.setNickname(msg);
            client.WriteMessage(new ServerMsg(msg));
        }
    }
}
