package ComunicationProtocol;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class representing messages with the server (type COMMUNICATION)
 */
public class ServerMsg extends Message implements Serializable {
    private String msg;
    private ArrayList<Integer> list;
    private boolean view;

    /**
     * Constructor of the class
     * @param msg
     */
    public ServerMsg(String msg){
        setCommandType(CommandType.COMMUNICATION);
        this.msg = msg;
    }

    /**
     * Constructor of the class
     * @param list
     */
    public ServerMsg(ArrayList<Integer> list){
        setCommandType(CommandType.COMMUNICATION);
        this.list = list;
    }

    /**
     * Constructor of the class
     * @param msg
     * @param view
     */
    public ServerMsg(String msg, boolean view){
        setCommandType(CommandType.COMMUNICATION);
        this.view = view;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<Integer> getList() {
        return list;
    }

    public boolean isView() {
        return view;
    }
}
