package ComunicationProtocol;

import java.io.Serializable;
import java.util.ArrayList;

public class CliCommandMsg extends  Message implements Serializable {
    private ArrayList<String> msg;
    private BoxInfo[][] map;
    private int info;

    public CliCommandMsg(CommandType type, ArrayList<String> msg){
        setCommandType(type);
        this.msg = msg;
        this.info = 0;
    }

    public CliCommandMsg(CommandType type, String msg){
        setCommandType(type);
        this.msg = new ArrayList<>();
        this.msg.add(msg);
        this.info = 0;
    }

    public CliCommandMsg(CommandType type, String msg, int info){
        setCommandType(type);
        this.msg = new ArrayList<>();
        this.msg.add(msg);
        this.info = info;
    }

    public CliCommandMsg(CommandType type, BoxInfo[][] map, String msg){
        setCommandType(type);
        this.map = map;
        this.msg = new ArrayList<>();
        this.msg.add(msg);
    }

    public ArrayList<String> getMsg() {
        return msg;
    }

    public int getInfo() {
        return info;
    }

    public BoxInfo[][] getMap() {
        return map;
    }
}
