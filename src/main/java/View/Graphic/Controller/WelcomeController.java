package View.Graphic.Controller;

import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class WelcomeController implements GuiController {
    private Gui gui;
    @FXML
    private ImageView columns;
    @FXML
    private ImageView logo;

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void ColumnSetOpacity1(MouseEvent event){
        Bloom bloom=new Bloom();
        columns.setOpacity(1);
        logo.setEffect(bloom);
     }

    public void ColumnSetOpacity0(MouseEvent e){
        columns.setOpacity(0);
        logo.setEffect(null);
    }

    public void StartGame(MouseEvent e){
        gui.Connect();
    }
}
