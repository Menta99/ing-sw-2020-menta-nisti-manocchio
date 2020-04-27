package Client;

import View.CLI.Cli;
import View.View;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class representing a client
 */
public class Client implements Runnable{
    private static final int PORT_NUM = 5555;
    private static final String IP = "127.0.0.1";
    private Socket server;
    private ConnectionHandler handler;
    private Cli cli;
    private boolean layout;//false = cli -- true = gui

    /**
     * Constructor of the class
     * @param layout
     */
    public Client(boolean layout){
        if(layout){
            System.out.println("Not yet implemented, stay tuned");
            this.cli = new Cli();
            this.layout = !layout;
        }
        else {
            this.cli = new Cli();
            this.layout = layout;
        }
    }

    /**
     * Launching a new client
     * @param args
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        boolean input = false;
        System.out.println("0) Cli\n1) Gui");
        if(keyboard.nextLine().equalsIgnoreCase("1")){
            input = true;
        }
        new Client(input).run();
    }

    /**
     * Trying connect to the server
     */
    @Override
    public void run() {
        try {
            System.out.println("[1] - Trying to connect to the server on port n° " + PORT_NUM);
            server = new Socket(IP, PORT_NUM);
            if(server.isConnected()) {
                System.out.println("[2] - Connection established");
                SetUpClient(server);
                while (!server.isClosed()) {
                }
            }
        } catch (IOException e) {
            System.err.println("Problems connecting to the Server");
        }
        finally {
            CloseClient();
        }
    }

    /**
     * Managing a new connection from the client
     * @param server
     */
    public void SetUpClient(Socket server){
        ConnectionHandler helper = new ConnectionHandler(server, this);
        new Thread(helper).start();
        this.handler = helper;
    }

    /**
     * Shutting down the client
     */
    public void CloseClient(){
        try {
            server.close();
        } catch (IOException e) {
            System.err.println("Unable to close the Client Socket");
        }
    }

    public View getView(){
        if(layout){
            return null;//gui
        }
        else{
            return cli;
        }
    }

    public boolean isLayout() {
        return layout;
    }
}
