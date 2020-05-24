package View.Graphic.Controller;

import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


/**
 * Controller class for fxml Welcome file
 */
public class WelcomeController implements GuiController {
    private Gui gui;
    @FXML
    private ImageView columns;
    @FXML
    private ImageView logo;

    public void setGui(Gui gui) {
        this.gui = gui;
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
        gui.Connect();
    }
}
