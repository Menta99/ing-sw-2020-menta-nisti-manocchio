package View.Graphic;

import Client.ClientCli;
import Client.ClientGui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class GuiController {
@FXML
 ImageView columns;
@FXML
 ImageView logo;
@FXML
Button closebtn;


    public void ColumnSetOpacity1(MouseEvent event){
        Bloom bloom=new Bloom();
        columns.setOpacity(1);
        logo.setEffect(bloom);
     }

    public void ColumnSetOpacity0(MouseEvent e){
        columns.setOpacity(0);
        logo.setEffect(null);
    }

    public void StartGame(MouseEvent e) throws IOException {
        //Gui.SwitchScene("src/main/java/View/Graphic/FXML/Name.fxml");
        new ClientGui().run();
    }

    public void Close(MouseEvent e){
        Gui.getDialog().close();
        Gui.getPrimaryStage().close();
    }





}
