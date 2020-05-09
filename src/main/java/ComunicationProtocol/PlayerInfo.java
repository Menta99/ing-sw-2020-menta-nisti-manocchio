package ComunicationProtocol;

import View.Colors;

import java.io.Serializable;

public class PlayerInfo implements Serializable {
    private int position;
    private String name;
    private Colors color;
    private int god;

    public PlayerInfo(int position, String name, Colors color, int god){
        this.position = position;
        this.name = name;
        this.color = color;
        this.god = god;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public Colors getColor() {
        return color;
    }

    public int getGod() {
        return god;
    }
}
