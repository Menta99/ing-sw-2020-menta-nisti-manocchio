package ComunicationProtocol;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for messages from command line interface
 */
public class CliCommandMsg implements Message, Serializable {
    private CommandType commandType;
    private SubCommandType subCommandType;
    private BoxInfo[][] map;
    private GodInfo[] gods;
    private PlayerInfo[] players;
    private ArrayList<Integer> list;

    public CliCommandMsg(CommandType cmd, SubCommandType sub, BoxInfo[][] map, GodInfo[] gods, PlayerInfo[] players, ArrayList<Integer> list){
        this.commandType = cmd;
        this.subCommandType = sub;
        this.map = map;
        this.gods = gods;
        this.players = players;
        this.list = list;
    }

    @Override
    public CommandType getCommandType() {
        return commandType;
    }

    public SubCommandType getSubCommandType() {
        return subCommandType;
    }

    public BoxInfo[][] getMap() {
        return map;
    }

    public GodInfo[] getGods() {
        return gods;
    }

    public PlayerInfo[] getPlayers() {
        return players;
    }

    public ArrayList<Integer> getList() {
        return list;
    }
}
