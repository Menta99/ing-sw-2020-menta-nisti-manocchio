package View.Graphic.Controller;

import CommunicationProtocol.SantoriniInfo.GodInfo;
import View.Graphic.Gui;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class CardController implements GuiController{
    private Gui gui;

    @FXML
    ImageView god;
    @FXML
    Group move;
    @FXML
    Label name;

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void SetUp(int player){
        GodInfo card = gui.getGods()[gui.getPlayers()[player].getGod()];
        name.setText(card.getName());
        Label power = (Label)move.getChildren().get(1);
        power.setText((card.getPower()));
        god.setImage(new Image("Cells/" + card.getName() + ".png", true));
    }

    public void Switch(MouseEvent e){
        if(move.getLayoutY()==363){
            move.setLayoutY(114);
        }
        else{
            move.setLayoutY(363);
        }
    }
}
