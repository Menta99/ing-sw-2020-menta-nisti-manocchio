package Client;

import View.Graphic.Gui;
import View.Graphic.GuiController;
import View.View;

import java.io.IOException;
import java.net.Socket;

public class ClientGui implements Runnable {
    private static final int PORT_NUM = 5555;
    private static final String IP = "127.0.0.1";
    private Socket server;
    private final Gui gui;

    public ClientGui() {
        gui = new Gui();
    }

    public static void main(String[] args) {
        Gui.main(args);
    }

    @Override
    public void run() {
        try {
            server = new Socket(IP, PORT_NUM);
            if(server.isConnected()) {
                SetUpClient(server);
                while (!server.isClosed()) {
                }
            }
            else{
                gui.ConnectionError();
            }
        } catch (IOException e) {
            try {
                gui.ConnectionError();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        finally {
            CloseClient();
        }

    }

    private void CloseClient() {
        try {
            if(server!=null){
                server.close();
            }
        } catch (IOException e) {
            System.err.println("Unable to close the Client Socket");
        }
    }

    private void SetUpClient(Socket server) {
        new Thread(new ConnectionHandler(server, this)).start();
    }

    public View getView(){
        return gui;
    }
}
