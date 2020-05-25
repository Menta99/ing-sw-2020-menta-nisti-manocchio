package View.Graphic.Controller;

import CommunicationProtocol.SantoriniInfo.GodInfo;
import View.Graphic.Gui;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

/**
 * Controller class for fxml Card file
 */

public class CardController implements GuiController{
    private Gui gui;
    private Media click;
    private Media info;
    private MediaPlayer clickPlayer;
    private MediaPlayer cardPlayer;

    @FXML
    ImageView god, btn;
    @FXML
    Group move;
    @FXML
    Label name, close;

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    /**
     * Display the correct image with his name and a description of his power
     * @param playerNumber number of player which belongs the card
     */
    public void SetUp(int playerNumber){
        click = new Media(new File("src/main/resources/Cells/Music/Click.wav").toURI().toString());
        info = new Media(new File("src/main/resources/Cells/Music/Info.wav").toURI().toString());
        clickPlayer = new MediaPlayer(click);
        clickPlayer.setRate(1.5);
        cardPlayer = new MediaPlayer(info);
        GodInfo card = gui.getGods()[gui.getPlayers()[playerNumber].getGod()];
        name.setText(card.getName());
        Label power = (Label)move.getChildren().get(1);
        power.setText((card.getPower()));
        god.setImage(new Image("Cells/GodCard/" + card.getName() + "_long.png", true));
    }

    /**
     * Picture a description of a God selected for the game
     * @param e User Interaction
     */
    public void ChangeFocus(MouseEvent e){
        if(!cardPlayer.isMute()){
            cardPlayer = new MediaPlayer(info);
            cardPlayer.play();
        }
        TranslateTransition trans = new TranslateTransition();
        trans.setDuration(Duration.seconds(1.5));
        if(e.getEventType().equals(MouseEvent.MOUSE_ENTERED)) {
            trans.setToY(-242);
        }
        else{
            trans.setToY(0);
        }
        trans.setNode(move);
        trans.play();
    }

    /**
     * Modify opacity of button pressed
     * @param e User interaction
     */
    public void pressBtn(MouseEvent e){
        clickPlayer.play();
        btn.setOpacity(0);
        close.setLayoutY(243);
    }

    /**
     * Modify opacity of button released
     * @param e User interaction
     */
    public void releaseBtn(MouseEvent e){
        btn.setOpacity(1);
        close.setLayoutY(240);
        clickPlayer.stop();
    }

    /**
     * Shut down the Dialog Pane when button close is pressed
     * @param e User interaction
     */
    public void Close(MouseEvent e){
        cardPlayer.setMute(true);
        gui.getDialog().close();
    }
}
