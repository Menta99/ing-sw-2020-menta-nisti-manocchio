package View.Graphic.Controller;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.ServerMsg;
import View.Graphic.Gui;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class ConfirmGodController implements GuiController{
    private Gui gui;
    private ConnectionHandler client;
    private SelectGodController controller;

    public void confirm(MouseEvent e){
        client.WriteMessage(new ServerMsg(controller.getIndex()));
        gui.getDialog().close();
    }

    public void cancel(MouseEvent e){
        controller.reset();
        gui.getDialog().close();
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void SetUp(ConnectionHandler client, SelectGodController controller){
        this.client = client;
        this.controller = controller;
    }
}
