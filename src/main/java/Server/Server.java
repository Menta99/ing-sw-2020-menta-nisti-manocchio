package Server;

import ComunicationProtocol.CliCommandMsg;
import ComunicationProtocol.CommandType;
import Controller.Controller;
import Model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private static final int PORT_NUM = 5555;
    private static ServerSocket server;
    private static Object lock = new Object();
    private static AtomicBoolean active = new AtomicBoolean(true);

    public static void main(String[] args){
        new Thread(new Keyboard()).start();
        try {
            System.out.println("[1] - Configuring the Server on port n° " + PORT_NUM);
            server = new ServerSocket(PORT_NUM);
            System.out.println("[2] - Server ready on port n° " + PORT_NUM);
            while(active.get()){
                GameSetting();
                synchronized (lock){
                    try{
                        lock.wait();
                    }catch (InterruptedException e){
                    }
                }
            }
            System.out.println("[Z] - Closing the Server");
            ServerClose();
        } catch (IOException e) {
            System.err.println("Unable to open the Server Socket");
        }
    }

    public static void GameSetting(){
        new Thread(new Controller()).start();
    }

    public static void UpdateServer(){
        synchronized (lock){
            lock.notifyAll();
        }
    }

    public static void ServerClose(){
        try {
            if(Game.getInstance().getController().getActive().get()) {
                for (ClientHandler handler : Game.getInstance().getController().getHandlers()) {
                    handler.WriteMessage(new CliCommandMsg(CommandType.CLOSE, "Server is down"));
                }
            }
            Game.getInstance().getController().setActive(new AtomicBoolean(false));
            server.close();
        } catch (IOException e1) {
            System.out.println("Unable to close the Server Socket properly");
        }
    }

    public static void AnomalousGameClose(ClientHandler disconnected){
        System.out.println("[Z] - Anomalous Disconnection of player n°" + Game.getInstance().getController().getHandlers().indexOf(disconnected));
        Game.getInstance().getController().getHandlers().remove(disconnected);
        for(ClientHandler handler : Game.getInstance().getController().getHandlers()){
            handler.WriteMessage(new CliCommandMsg(CommandType.CLOSE, "Player " + disconnected.getNickName() +
                    " disconnected\nEndGame"));
        }
        Game.getInstance().getController().setActive(new AtomicBoolean(false));
    }

    public static ServerSocket getServer() {
        return server;
    }

    public static void setActive(boolean active) {
        Server.active.set(active);
    }
}