package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import View.Graphic.Gui;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class WaitController implements Initializable, GuiController {
    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;


    @FXML
    AnchorPane lancetPane;
    @FXML
    Label text;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setRotate(lancetPane, 3800 , 120);
        fadeText();
    }

    private void setRotate(AnchorPane pane, int angle, int duration){
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(duration), pane);
        rotateTransition.setAutoReverse(true);
        rotateTransition.setByAngle(angle);
        rotateTransition.setDelay(Duration.seconds(0));
        rotateTransition.setRate(10);
        rotateTransition.setCycleCount(10);
        rotateTransition.play();
    }

    private void fadeText(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), text);
        fadeTransition.setFromValue(0.2);
        fadeTransition.setToValue(1);
        fadeTransition.setAutoReverse(true);
        fadeTransition.setCycleCount(40);
        fadeTransition.play();

    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void SetUp(CommandMsg command, ConnectionHandler client){
        this.command = command;
        this.client = client;
        if(command.getCommandType() == CommandType.COM_WAIT_LOBBY){
            text.setText("Waiting for Players...");
        }
        else{
            text.setText("Waiting other players' choice ...");
        }
    }
}
