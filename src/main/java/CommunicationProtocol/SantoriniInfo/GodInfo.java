package CommunicationProtocol.SantoriniInfo;

import Model.Game;
import Model.Godcards.GodCard;

import java.io.Serializable;

/**
 * Immutable Class containing Card information
 */
public class GodInfo implements Serializable {
    private final int position;
    private final String name;
    private final String power;
    private final boolean active;

    /**
     * Constructor of the class
     * @param position index of the card
     * @param name name of the card
     * @param power script of the power of the card
     * @param active indicates if the card is part of the game or not
     */
    public GodInfo(int position, String name, String power, boolean active){
        this.position = position;
        this.name = name;
        this.power = power;
        this.active = active;
    }

    public GodInfo(GodCard card){
        this.position = Game.getInstance().getDeck().getCardList().indexOf(card);
        this.name = card.getName();
        this.power = card.getPower();
        this.active = card.isChosen();
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
