package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.ServerMsg;
import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class ResumeController implements GuiController {

    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;

    @FXML
    ImageView button_yes, button_no;
    @FXML
    Label yes, no;

    public void press(MouseEvent e){
        if(e.getSource() == no){
            button_no.setOpacity(0);
            no.setLayoutY(333);
        }
        else if(e.getSource() == yes){
            button_yes.setOpacity(0);
            yes.setLayoutY(333);
        }
    }

    public void release(MouseEvent e){
        if(e.getSource() == no){
            button_no.setOpacity(1);
            no.setLayoutY(330);
        }
        else if(e.getSource() == yes){
            button_yes.setOpacity(1);
            yes.setLayoutY(330);
        }
    }

    public void select(MouseEvent e){
        String msg = "";
        if(e.getSource() == no){
            msg = "no";
        }
        else if(e.getSource() == yes){
            msg = "yes";
        }
        client.WriteMessage(new ServerMsg(msg));
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void SetUp(CommandMsg command, ConnectionHandler client){
        this.command = command;
        this.client = client;
    }
}
