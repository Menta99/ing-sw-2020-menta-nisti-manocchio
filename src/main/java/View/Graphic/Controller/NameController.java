package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import CommunicationProtocol.ServerMsg;
import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class NameController implements GuiController {
    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;
    @FXML
    private TextField nickfield;
    @FXML
    private ImageView pressed_btn;
    @FXML
    private Label confirm_label;

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void CheckName(MouseEvent e){

        boolean found = false;
        String msg = nickfield.getText().trim();
        nickfield.clear();
        if(msg.equals("")){
            confirm_label.requestFocus();
            nickfield.setPromptText("Insert a non-void name");
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
                    confirm_label.requestFocus();
                    nickfield.setPromptText("Name already chosen");
                }
            }
        }
        if(found){
            gui.setNickname(msg);
            client.WriteMessage(new ServerMsg(msg));
        }
    }

    public void SetUp(CommandMsg command, ConnectionHandler client){
        this.command = command;
        this.client = client;
    }

    public void pressButton(MouseEvent e){
        pressed_btn.setOpacity(1);
        confirm_label.setLayoutY(336);
    }
    public void releaseButton(MouseEvent e){
        pressed_btn.setOpacity(0);
        confirm_label.setLayoutY(333);
    }
}