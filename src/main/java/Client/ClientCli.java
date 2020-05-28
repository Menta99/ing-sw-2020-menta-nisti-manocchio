package Client;

import View.CLI.Cli;
import View.View;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client class which implements Command Line Interface
 */
public class ClientCli implements Runnable{
    private static final int PORT_NUM = 5555;
    private static String IP = "127.0.0.1";
    private Socket server;
    private final Cli cli;
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * Constructor of the class, creates the instance of the Command Line Interface
     */
    public ClientCli(){
        this.cli = new Cli();
    }

    public ClientCli(String file) throws IOException{
        this.cli = new Cli(file);
    }

    /**
     * Main method, wait player's confirm, then launches the Client execution
     * @param args standard parameter for main method
     */
    public static void main(String[] args) {
        if (args.length!=0 && args[0].equals("1")){ //debug read from file
            try {
                new ClientCli(args[1]).run();
            }
            catch (IOException e) {
                System.out.println("new clientcli error");
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Press enter to Start");
            while (!keyboard.nextLine().equals("")) {
            }
            new ClientCli().run();
        }

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
            System.out.println("Insert server IP");
            IP = keyboard.nextLine().trim();
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
            System.out.println("Do you want to play another game?");
            Scanner keyboard = new Scanner(System.in);
            if(keyboard.nextLine().equalsIgnoreCase("yes")){
                new ClientCli().run();
            }
        } catch (IOException e) {
            System.err.println("Unable to close the Client Socket");
        }
    }

    public View getView(){
        return cli;
    }
}
