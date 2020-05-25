package Server;

import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import CommunicationProtocol.SantoriniInfo.Info;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import CommunicationProtocol.ServerMsg;
import Controller.SavedData.GameData;
import Model.Game;
import Model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that handles the connection between the Server and a specified Client
 */
public class ClientHandler implements Runnable{
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final int playerNum;
    private String nickName;
    private Player player;
    private AtomicBoolean active;

    /**
     * Constructor of the class, complete.
     * Creates the Streams and sets up all the information of the Client
     * @param client Socket of the connection with the Client
     * @param playerNum number of the Player to handle
     */
    public ClientHandler(Socket client, int playerNum){
        this.playerNum = playerNum;
        this.active = new AtomicBoolean(true);
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
            NickName();
        } catch (IOException e) {
            System.err.println("Unable to open the Streams");
        }
    }

    /**
     * Auxiliary Constructor of the Class
     */
    public ClientHandler(){
        playerNum=-1;
    }

    @Override
    public void run() {
        while (active.get()){
        }
    }

    /**
     * Asks Player's Nickname
     * Invokes the Reload method to check the possibility to reload an interrupted game
     */
    public void NickName(){
        Info info = null;
        if(!(playerNum == 0) && !(Game.getInstance().getController().getLoadedGame().get())){
            PlayerInfo[] players = new PlayerInfo[playerNum];
            for(int i = 0; i < playerNum; i++){
                players[i] = new PlayerInfo(i, Game.getInstance().getPlayer().get(i).getNickName(), null, -1);
            }
            info = new Info(players);
        }
        WriteMessage(new CommandMsg(CommandType.NAME, info));
        nickName = ReadMessage().getMsg();
        ReloadPlayer(Game.getInstance().getController().getLoadedGame().get());
    }

    /**
     * Asks if the Player is the Challenger, the number of player of the game
     * Initializes then the player itself in the game
     */
    public void FirstPlayer(){
        if(playerNum == 0){
            if(GameData.isPlayerInIt(nickName)){
                WriteMessage(new CommandMsg(CommandType.ANS_RESTART, null));
                if (ReadMessage().getMsg().equalsIgnoreCase("yes")){
                    System.out.println("[S] - Game Restarted");
                    Game.getInstance().getController().setLoadedGame(new AtomicBoolean(true));
                    Game.getInstance().loadGame("savedGame");
                    for (Player player : Game.getInstance().getPlayer()){
                        if (player.getNickName().equals(nickName)){
                            this.player = player;
                        }
                    }
                    WriteMessage(new CommandMsg(CommandType.COM_WAIT_LOBBY, null));
                    return;
                }
            }
            CommandMsg msg = new CommandMsg(CommandType.FIRST, null);
            WriteMessage(msg);
            int result = ReadMessage().getList().get(0);
            Game.getInstance().getController().setPlayerNum(result);
        }
        player = new Player(nickName);
        WriteMessage(new CommandMsg(CommandType.COM_WAIT_LOBBY, null));
    }

    /**
     * Check if the game is reloaded and in this case links the player to the corresponding that already exists
     * @param reload boolean that indicates if the game was reloaded or not
     */
    public void ReloadPlayer(boolean reload){
        if(reload){
            if (!GameData.isPlayerInIt(nickName)){
                active.set(false);
                WriteMessage(new CommandMsg(CommandType.CLOSE_RESTART, null));
            }
            else {
                for (Player player : Game.getInstance().getPlayer()){
                    if (player.getNickName().equals(nickName)){
                        for(ClientHandler handler : Game.getInstance().getController().getHandlers()){
                            if(handler.getPlayer() == player){
                                WriteMessage(new CommandMsg(CommandType.CLOSE_RESTART, null));
                                return;
                            }
                        }
                        this.player = player;
                    }
                }
                WriteMessage(new CommandMsg(CommandType.COM_WAIT_LOBBY, null));
            }
        }
        else {
            FirstPlayer();
        }
    }

    /**
     * Write a message to the Client
     * @param msg CommandMsg to send
     */
    public void WriteMessage(CommandMsg msg){
        try {
            out.writeObject(msg);
            CommandType type = msg.getCommandType();
            if(type == CommandType.CLOSE_ANOMALOUS || type == CommandType.CLOSE_NORMAL || type== CommandType.CLOSE_RESTART || type == CommandType.CLOSE_SERVER){
                System.out.println("[W] - Close Message sent to player n°" + playerNum);
                active.set(false);
            }
            else{
                System.out.println("[W] - Message sent to player n°" + playerNum + " of type " + msg.getCommandType());
            }
        } catch (IOException e) {
            System.err.println("Unable to send the Message");
            Server.AnomalousGameClose(this);
            active.set(false);
        }
    }

    /**
     * Read a message from the Client
     * @return a ServerMsg containing the information
     */
    public ServerMsg ReadMessage(){
        try {
            ServerMsg msg = (ServerMsg) in.readObject();
            System.out.println("[R] - Message received from player n°" + playerNum);
            return msg;
        } catch (IOException e) {
            System.err.println("Unable to read the StringMessage");
            Server.AnomalousGameClose(this);
            active.set(false);
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

    public int getPlayerNum() {
        return playerNum;
    }

    public AtomicBoolean getActive() {
        return active;
    }
}
