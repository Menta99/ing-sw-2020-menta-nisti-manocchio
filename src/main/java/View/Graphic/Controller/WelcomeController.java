package View.Graphic.Controller;

import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


/**
 * Controller class for fxml Welcome file
 */
public class WelcomeController implements GuiController {
    private Gui gui;
    private Media choir;
    private Media click;
    private MediaPlayer clickPlayer;
    private MediaPlayer choirPlayer;
    private final Bloom bloom = new Bloom();

    @FXML
    private ImageView logo, menu, columns;

    public void setGui(Gui gui) {
        this.gui = gui;
        choir = new Media(getClass().getResource("/Cells/Music/Choir.wav").toString());
        choirPlayer = new MediaPlayer(choir);
        click = new Media(getClass().getResource("/Cells/Music/Click.wav").toString());
        clickPlayer = new MediaPlayer(click);
    }

    /**
     * Make a new game begins
     * @param e User Interaction
     */
    public void StartGame(MouseEvent e){
        choirPlayer.setVolume(gui.getVolume());
        choirPlayer.play();
        gui.Connect();
    }

    public void Switch(MouseEvent e){
        Node node = (Node)e.getSource();
        if(node == logo){
            if(e.getEventType() == MouseEvent.MOUSE_ENTERED){
                columns.setOpacity(1);
                logo.setEffect(bloom);
            }
            else if(e.getEventType() == MouseEvent.MOUSE_EXITED){
                columns.setOpacity(0);
                logo.setEffect(null);
            }
        }
        else if(node == menu){
            if(e.getEventType() == MouseEvent.MOUSE_ENTERED){
                menu.setOpacity(0);
            }
            else if(e.getEventType() == MouseEvent.MOUSE_EXITED){
                menu.setOpacity(1);
            }
        }
    }

    /**
     * Opens Options menu (IP, volume)
     * @param e User interaction
     */
    public void Option(MouseEvent e){
        clickPlayer.stop();
        clickPlayer.play();
        gui.Option();
        clickPlayer.setVolume(gui.getVolume());
        choirPlayer.setVolume(gui.getVolume());
    }
}
