package ComunicationProtocol;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representing messages with the server (type COMMUNICATION)
 */
public class ServerMsg implements Message, Serializable {
    private CommandType commandType;
    private String msg;
    private ArrayList<Integer> list;

    public ServerMsg(String msg){
        this.commandType = CommandType.COMMUNICATION;
        this.msg = msg;
    }

    public ServerMsg(ArrayList<Integer> list){
        this.commandType = CommandType.COMMUNICATION;
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
