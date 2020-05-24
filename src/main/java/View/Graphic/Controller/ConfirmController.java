package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import CommunicationProtocol.ServerMsg;
import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;



/**
 * Controller class for fxml Confirm file
 */
public class ConfirmController implements GuiController{
    private Gui gui;
    private ConnectionHandler client;
    private CommandMsg command;
    private GodChoiceController controller;

    @FXML
    Label yes_lbl, no_lbl, text_lbl;
    @FXML
    ImageView pressed_btn_yes, pressed_btn_no;


    /**
     * Modify opacity of button pressed
     * @param e User interaction
     */
    public void press(MouseEvent e){
        if(e.getSource() == yes_lbl){
            pressed_btn_yes.setOpacity(1);
            yes_lbl.setLayoutY(216);
        }
        else if(e.getSource() == no_lbl){
            pressed_btn_no.setOpacity(1);
            no_lbl.setLayoutY(216);
        }
    }

    /**
     * Modify opacity of button released
     * @param e User interaction
     */
    public void release(MouseEvent e){
        if(e.getSource() == yes_lbl){
            pressed_btn_yes.setOpacity(0);
            yes_lbl.setLayoutY(213);
        }
        else if(e.getSource() == no_lbl){
            pressed_btn_no.setOpacity(0);
            no_lbl.setLayoutY(213);
        }
    }

    /**
     * Send the correct message based on client action
     * @param e User interaction
     */
    public void select(MouseEvent e){
        if(e.getSource() == yes_lbl){
            if(command.getCommandType() == CommandType.ANS_POWER){
                client.WriteMessage(new ServerMsg("yes"));
            }
            else {
                client.WriteMessage(new ServerMsg(controller.getIndex()));
                controller.restore();
                Switch();
            }
            gui.getDialog().close();
        }
        else if(e.getSource() == no_lbl){
            if(command.getCommandType() == CommandType.ANS_POWER){
                client.WriteMessage(new ServerMsg("no"));
            }
            else {
                controller.reset();
                Switch();
            }
            gui.getDialog().close();
        }
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    /**
     * Ask if you want to use your power
     * @param client object representing a client connected
     * @param controller controller of GodChoice fxml file
     * @param command message from server
     */
    public void SetUp(ConnectionHandler client, GodChoiceController controller, CommandMsg command){
        this.client = client;
        this.controller = controller;
        this.command = command;
        if(command.getCommandType() == CommandType.ANS_POWER){
            text_lbl.setText("Use your Power?");
        }
        else{
            Switch();
        }
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
