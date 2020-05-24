package Server;

import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import CommunicationProtocol.SantoriniInfo.Info;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import Controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class representing the Server
 */
public class Server {
    private static final int PORT_NUM = 5555;
    private static ServerSocket server;
    private static final Object lock = new Object();
    private static Controller controller;

    /**
     * Main method that configures the server
     * Handle possible ServerSocket issues
     * @param args standard parameter of main method
     */
    public static void main(String[] args){
        try {
            System.out.println("[1] - Configuring the Server on port n° " + PORT_NUM);
            server = new ServerSocket(PORT_NUM);
            System.out.println("[2] - Server ready on port n° " + PORT_NUM);
            while(true){
                GameSetting();
                synchronized (lock){
                    try{
                        lock.wait();
                        if(args.length!=0 && args[0].equals("1")) return; //debug only
                    }catch (InterruptedException e){
                        System.out.println("[6] - Controller Update");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to open the Server Socket");
        }
        finally {
            System.out.println("[Z] - Closing the Server");
            ServerClose();
        }
    }

    /**
     * Starts the Game by creating the Controller and assigning a new Thread to it
     */
    public static void GameSetting(){
        controller = new Controller();
        new Thread(controller).start();
    }

    /**
     * Update message called by the Controller when the Game finishes
     */
    public static void UpdateServer(){
        synchronized (lock){
            lock.notifyAll();
        }
    }

    /**
     * Shuts down the server, communicating it also to the Clients connected
     */
    public static void ServerClose(){
        try {
            if(controller.getActive().get()) {
                for (ClientHandler handler : controller.getHandlers()) {
                    handler.WriteMessage(new CommandMsg(CommandType.CLOSE_SERVER, null));
                }
            }
            controller.setActive(new AtomicBoolean(false));
            server.close();
        } catch (IOException e1) {
            System.out.println("Unable to close the Server Socket properly");
        }
    }

    /**
     * Manage an unexpected Game Closure (Client disconnection)
     * @param disconnected ClientHandler of the Client who disconnected
     */
    public static void AnomalousGameClose(ClientHandler disconnected){
        System.out.println("[Z] - Anomalous Disconnection of player n°" + controller.getHandlers().indexOf(disconnected));
        Info info = new Info(new PlayerInfo(disconnected));
        controller.getHandlers().remove(disconnected);
        for(ClientHandler handler : controller.getHandlers()){
            handler.WriteMessage(new CommandMsg(CommandType.CLOSE_ANOMALOUS, info));
        }
        controller.setActive(new AtomicBoolean(false));
    }

    public static ServerSocket getServer() {
        return server;
    }
}