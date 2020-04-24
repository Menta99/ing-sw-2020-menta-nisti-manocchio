package Server;

import ComunicationProtocol.*;
import Model.Game;
import Model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler implements Runnable{
    private final Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final int playerNum;
    private String nickName;
    private Player player;
    private AtomicBoolean active;

    public ClientHandler(Socket client, int playerNum){
        this.client = client;
        this.playerNum = playerNum;
        this.active = new AtomicBoolean(true);
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
            NickName();
            FirstPlayer(playerNum);
        } catch (IOException e) {
            System.err.println("Unable to open the Streams");
        }
    }

    @Override
    public void run() {
        while (active.get()){
            //do stuff
        }
    }

    public void FirstPlayer(int playerNum){
        if(playerNum == 0){
            CliCommandMsg msg = new CliCommandMsg(CommandType.NUMBER, "You are the challenger\nTell me how many Players will join the game");
            WriteMessage(msg);
            int result = ReadMessage().getList().get(0);
            while (result != 2 && result != 3){
                msg = new CliCommandMsg(CommandType.NUMBER, "Invalid number, required 2 or 3, retry");
                WriteMessage(msg);
                result = ReadMessage().getList().get(0);
            }
            Game.getInstance().getController().setPlayerNum(result);
        }
    }

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

    public void NickName(){
        CliCommandMsg msg = new CliCommandMsg(CommandType.NAME, "What's your NickName?");
        WriteMessage(msg);
        ServerMsg answer = ReadMessage();
        nickName = answer.getMsg();
        player = new Player(nickName);
        player.setView(answer.isView());
    } //check on duplicates name needed

    public String getNickName() {
        return nickName;
    }

    public Player getPlayer() {
        return player;
    }
}
