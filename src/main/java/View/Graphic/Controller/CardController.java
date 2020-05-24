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
import javafx.util.Duration;

public class CardController implements GuiController{
    private Gui gui;

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

    public void SetUp(int player){
        GodInfo card = gui.getGods()[gui.getPlayers()[player].getGod()];
        name.setText(card.getName());
        Label power = (Label)move.getChildren().get(1);
        power.setText((card.getPower()));
        god.setImage(new Image("Cells/GodCard/" + card.getName() + ".png", true));
    }

    public void ChangeFocus(MouseEvent e){
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

    public void pressBtn(MouseEvent e){
        btn.setOpacity(0);
        close.setLayoutY(243);
    }

    public void releaseBtn(MouseEvent e){
        btn.setOpacity(1);
        close.setLayoutY(240);
    }

    public void Close(MouseEvent e){
        gui.getDialog().close();
    }
}
