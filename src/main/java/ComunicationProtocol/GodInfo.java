package ComunicationProtocol;

import java.io.Serializable;

public class GodInfo implements Serializable {
    private int position;
    private String name;
    private String power;
    private boolean active;

    public GodInfo(int position, String name, String power, boolean active){
        this.position = position;
        this.name = name;
        this.power = power;
        this.active = active;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getPower() {
        return power;
    }

    public boolean isActive() {
        return active;
    }
}
