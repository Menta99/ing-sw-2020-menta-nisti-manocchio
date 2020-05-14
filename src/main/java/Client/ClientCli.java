package Client;

import View.CLI.Cli;
import View.View;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client class which implements Command Line Interface
 */
public class ClientCli implements Runnable{
    private static final int PORT_NUM = 5555;
    private static final String IP = "127.0.0.1";
    private Socket server;
    private final Cli cli;

    /**
     * Constructor of the class, creates the instance of the Command Line Interface
     */
    public ClientCli(){
        this.cli = new Cli();
    }

    /**
     * Main method, wait player's confirm, then launches the Client execution
     * @param args standard parameter for main method
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Press enter to Connect");
        while(!keyboard.nextLine().equals("")){
        }
        new ClientCli().run();
    }

    /**
     * Try to connect to a Server on an established port
     * Then enters in a loop till the Socket is open
     * At the end terminates the client execution
     * Handles fail during connection
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
     * Set up the client connection, creating the ConnectionHandler and starting it
     * @param server Socket of the connection with the Server
     */
    public void SetUpClient(Socket server){
        new Thread(new ConnectionHandler(server, this)).start();
    }

    /**
     * Shuts down the client
     */
    public void CloseClient(){
        try {
            server.close();
        } catch (IOException e) {
            System.err.println("Unable to close the Client Socket");
        }
    }

    public View getView(){
        return cli;
    }
}
