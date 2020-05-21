package View.Graphic.Controller;

import View.Graphic.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ErrorWelcomeController implements GuiController {
    private Gui gui;

    @FXML
    Label close_lbl;
    @FXML
    ImageView pressed_btn;

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void Close(MouseEvent e){
        gui.getDialog().close();
        gui.getPrimaryStage().close();
    }

    public void pressButton(MouseEvent e){
        pressed_btn.setOpacity(1);
        close_lbl.setLayoutY(216);
    }
    public void releaseButton(MouseEvent e){
        pressed_btn.setOpacity(0);
        close_lbl.setLayoutY(213);
    }
}
