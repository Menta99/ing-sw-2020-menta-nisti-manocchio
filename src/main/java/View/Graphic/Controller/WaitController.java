package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import View.Graphic.Gui;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for Wait fxml file
 */
public class WaitController implements Initializable, GuiController {
    private Gui gui;
    private CommandMsg command;
    private ConnectionHandler client;
    private PerspectiveTransform trs = new PerspectiveTransform();

    @FXML
    AnchorPane lancetPane;
    @FXML
    Label text;
    @FXML
    ImageView clock_face, clock_hand;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trs.setUlx(15);
        trs.setUly(5);
        trs.setLlx(25);
        trs.setLly(170);
        trs.setUrx(190);
        trs.setUry(0);
        trs.setLrx(180);
        trs.setLry(180);
        clock_face.setEffect(trs);
        setRotate(lancetPane);
    }

    /**
     * Create the correct clock's effect
     * @param pane fxml component element
     */
    private void setRotate(AnchorPane pane){
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), pane);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Timeline.INDEFINITE);
        rotateTransition.play();
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    /**
     * Create the wait lobby
     * @param command message from server
     * @param client object representing a client connected
     */
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
