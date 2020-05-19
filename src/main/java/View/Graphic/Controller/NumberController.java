package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.ServerMsg;
import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class NumberController implements GuiController {

    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;

    @FXML
    Label two, three;
    @FXML
    ImageView button_2, button_3;

    public void press(MouseEvent e){
        if(e.getSource() == two){
            button_2.setOpacity(0);
            two.setLayoutY(333);
        }
        else if(e.getSource() == three){
            button_3.setOpacity(0);
            three.setLayoutY(333);
        }
    }

    public void release(MouseEvent e){
        if(e.getSource() == two){
            button_2.setOpacity(1);
            two.setLayoutY(330);
        }
        else if(e.getSource() == three){
            button_3.setOpacity(1);
            three.setLayoutY(330);
        }
    }

    public void select(MouseEvent e){
        ArrayList<Integer> output = new ArrayList<>();
        if(e.getSource() == two){
            output.add(2);
        }
        else if(e.getSource() == three){
            output.add(3);
        }
        client.WriteMessage(new ServerMsg(output));
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
