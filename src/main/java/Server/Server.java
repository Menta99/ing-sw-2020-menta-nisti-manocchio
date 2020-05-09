package Server;

import ComunicationProtocol.CliCommandMsg;
import ComunicationProtocol.CommandType;
import ComunicationProtocol.SubCommandType;
import Controller.Controller;
import Model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class representing the server
 */
public class Server {
    private static final int PORT_NUM = 5555;
    private static ServerSocket server;
    private static Object lock = new Object();

    /**
     * Configure the server and at the end close if there's nothing to do
     * @param args
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
                    }catch (InterruptedException e){
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
     * Starting the Controller in a different thread
     */
    public static void GameSetting(){
        new Thread(new Controller()).start();
    }

    /**
     * Updating message
     */
    public static void UpdateServer(){
        synchronized (lock){
            lock.notifyAll();
        }
    }

    /**
     * Shut down the server
     */
    public static void ServerClose(){
        try {
            if(Game.getInstance().getController().getActive().get()) {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(1);
                for (ClientHandler handler : Game.getInstance().getController().getHandlers()) {
                    handler.WriteMessage(new CliCommandMsg(CommandType.CLOSE, SubCommandType.DEFAULT, null, null, null, list));
                }
            }
            Game.getInstance().getController().setActive(new AtomicBoolean(false));
            server.close();
        } catch (IOException e1) {
            System.out.println("Unable to close the Server Socket properly");
        }
    }

    /**
     * Manage an unexpected game close( client disconnected)
     * @param disconnected
     */
    public static void AnomalousGameClose(ClientHandler disconnected){
        System.out.println("[Z] - Anomalous Disconnection of player n°" + Game.getInstance().getController().getHandlers().indexOf(disconnected));
        ArrayList<Integer> list = new ArrayList<>();
        list.add(-1);
        list.add(Game.getInstance().getController().getHandlers().indexOf(disconnected));
        Game.getInstance().getController().getHandlers().remove(disconnected);
        for(ClientHandler handler : Game.getInstance().getController().getHandlers()){
            handler.WriteMessage(new CliCommandMsg(CommandType.CLOSE, SubCommandType.DEFAULT, null, null, null, list));
        }
        Game.getInstance().getController().setActive(new AtomicBoolean(false));
    }

    public static ServerSocket getServer() {
        return server;
    }
}