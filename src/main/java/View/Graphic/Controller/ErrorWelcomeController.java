package View.Graphic.Controller;

import View.Graphic.Gui;
import javafx.scene.input.MouseEvent;

public class ErrorWelcomeController implements GuiController {
    private Gui gui;

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void Close(MouseEvent e){
        gui.getDialog().close();
        gui.getPrimaryStage().close();
    }
}
