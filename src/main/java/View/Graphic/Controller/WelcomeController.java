package View.Graphic.Controller;

import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;


/**
 * Controller class for fxml Welcome file
 */
public class WelcomeController implements GuiController {
    private Gui gui;
    private Media choir;
    private MediaPlayer player;

    @FXML
    private ImageView columns;
    @FXML
    private ImageView logo;

    public void setGui(Gui gui) {
        this.gui = gui;
        choir = new Media(new File("src/main/resources/Cells/Music/Choir.wav").toURI().toString());
        player = new MediaPlayer(choir);
    }

    /**
     * Set the correct effect when mouse enter
     * @param event User Interaction
     */
    public void ColumnSetOpacity1(MouseEvent event){
        Bloom bloom=new Bloom();
        columns.setOpacity(1);
        logo.setEffect(bloom);
     }

    /**
     * Set the correct effect when mouse exit
     * @param e User Interaction
     */
    public void ColumnSetOpacity0(MouseEvent e){
        columns.setOpacity(0);
        logo.setEffect(null);
    }

    /**
     * Make a new game begins
     * @param e User Interaction
     */
    public void StartGame(MouseEvent e){
        player.play();
        gui.Connect();
    }
}
