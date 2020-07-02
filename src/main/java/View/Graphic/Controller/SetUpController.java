package View.Graphic.Controller;

import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;

public class SetUpController implements GuiController{
    private Gui gui;
    private Media click;
    private MediaPlayer player;

    @FXML
    Slider volume;
    @FXML
    ImageView normal_btn;
    @FXML
    Label confirm_lbl;
    @FXML
    TextField ip;

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
        click = new Media(getClass().getResource("/Cells/Music/Click.wav").toString());;
        player = new MediaPlayer(click);
        player.setVolume(gui.getVolume());
        confirm_lbl.requestFocus();
    }

    /**
     * Modify opacity of button pressed
     * @param e User interaction
     */
    public void pressButton(MouseEvent e){
        player.play();
        normal_btn.setOpacity(0);
        confirm_lbl.setLayoutY(274);
    }

    /**
     * Modify opacity of button released
     * @param e User interaction
     */
    public void releaseButton(MouseEvent e){
        normal_btn.setOpacity(1);
        confirm_lbl.setLayoutY(271);
        player.stop();
    }

    /**
     * Saves user preferences (Volume, IP)
     * @param e User interaction
     */
    public void Confirm(MouseEvent e){
        gui.setVolume(volume.getValue()/100);
        gui.setIP(ip.getText());
        gui.getDialog().close();
    }

    /**
     * Real-time adjustment of the volume,
     * volume normalization.
     * @param e User interaction
     */
    public void Adjust(MouseEvent e){
        player.setVolume(volume.getValue()/100);
    }
}
