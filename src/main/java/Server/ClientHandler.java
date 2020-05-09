package Server;

import ComunicationProtocol.*;
import Model.Game;
import Model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for manage a client connection
 */
public class ClientHandler implements Runnable{
    private final Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final int playerNum;
    private String nickName;
    private Player player;
    private AtomicBoolean active;

    /**
     * Constructor of the class
     * @param client
     * @param playerNum
     */
    public ClientHandler(Socket client, int playerNum){
        this.client = client;
        this.playerNum = playerNum;
        this.active = new AtomicBoolean(true);
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
            NickName();
            FirstPlayer();
        } catch (IOException e) {
            System.err.println("Unable to open the Streams");
        }
    }

    public ClientHandler(){
        client=null;
        playerNum=-1;
    }

    @Override
    public void run() {
        while (active.get()){
            //do stuff
        }
    }

    /**
     * Ask for a nick when a new client is connected
     */
    public void NickName(){
        PlayerInfo[] players;
        if(playerNum == 0){
            players = null;
        }
        else{
            players = new PlayerInfo[playerNum];
            for(int i = 0; i < playerNum; i++){
                players[i] = new PlayerInfo(i, Game.getInstance().getPlayer().get(i).getNickName(), null, -1);
            }
        }
        CliCommandMsg msg = new CliCommandMsg(CommandType.NAME, SubCommandType.DEFAULT, null, null, players, null);
        WriteMessage(msg);
        ServerMsg answer = ReadMessage();
        nickName = answer.getMsg();
        player = new Player(nickName);
    }

    /**
     * Welcome to the challenger and ask for players' number
     */
    public void FirstPlayer(){
        if(playerNum == 0){
            CliCommandMsg msg = new CliCommandMsg(CommandType.FIRST, SubCommandType.DEFAULT, null, null, null, null);
            WriteMessage(msg);
            int result = ReadMessage().getList().get(0);
            Game.getInstance().getController().setPlayerNum(result);
        }
        WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, SubCommandType.WAIT, null, null, null, null));
    }

    /**
     * Write a message at the OutputStream
     * @param msg
     */
    public void WriteMessage(CliCommandMsg msg){
        try {
            out.writeObject(msg);
            if(msg.getCommandType() == CommandType.CLOSE){
                System.out.println("[W] - Close Message sent to player n°" + playerNum);
                active.set(false);
            }
            else{
                System.out.println("[W] - Message sent to player n°" + playerNum);
            }
        } catch (IOException e) {
            System.err.println("Unable to send the StringMessage");
            Server.AnomalousGameClose(this);
        }
    }

    /**
     * Read a message at the InputStream
     * @return
     */
    public ServerMsg ReadMessage(){
        try {
            ServerMsg msg = (ServerMsg) in.readObject();
            System.out.println("[R] - Message received from player n°" + playerNum);
            return msg;
        } catch (IOException e) {
            System.err.println("Unable to read the StringMessage");
            Server.AnomalousGameClose(this);
        } catch (ClassNotFoundException e) {
            System.err.println("It's not a StringMessage");
        }
        return null;
    }

    public String getNickName() {
        return nickName;
    }

    public Player getPlayer() {
        return player;
    }
}
