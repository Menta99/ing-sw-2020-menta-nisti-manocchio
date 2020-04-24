package ComunicationProtocol;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerMsg extends Message implements Serializable {
    private String msg;
    private ArrayList<Integer> list;
    private boolean view;

    public ServerMsg(String msg){
        setCommandType(CommandType.COMMUNICATION);
        this.msg = msg;
    }

    public ServerMsg(ArrayList<Integer> list){
        setCommandType(CommandType.COMMUNICATION);
        this.list = list;
    }

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
