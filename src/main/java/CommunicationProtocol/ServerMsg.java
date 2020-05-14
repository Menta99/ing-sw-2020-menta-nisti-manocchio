package CommunicationProtocol;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Immutable Class representing messages from the Client to the Server
 */
public class ServerMsg implements Message, Serializable {
    private final CommandType commandType;
    private final String msg;
    private final ArrayList<Integer> list;

    /**
     * Constructor of the Class, string version
     * @param msg string containing single information
     */
    public ServerMsg(String msg){
        this.commandType = CommandType.DEFAULT;
        this.msg = msg;
        this.list = null;
    }

    /**
     * Constructor of the Class, list version
     * @param list ArrayList of Integer containing multiple information
     */
    public ServerMsg(ArrayList<Integer> list){
        this.commandType = CommandType.DEFAULT;
        this.msg = null;
        this.list = list;
    }

    @Override
    public CommandType getCommandType() {
        return commandType;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<Integer> getList() {
        return list;
    }
}
