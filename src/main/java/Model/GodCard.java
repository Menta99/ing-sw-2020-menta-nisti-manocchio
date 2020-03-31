package Model;

public abstract class GodCard {
    private String name;
    private String power;
    private String activePhase;
    private boolean chosen; //indica se la carta è stat scelta per il Game, default false
    private boolean picked; //indica se la carta è stata scelta da un Giocatore, default false

    /**
     * Setter of Chosen
     * @param state value to assign
     */
    public void setChosen(boolean state) {
        this.chosen = state;
    }

    /**
     * Getter of chosen
     * @return true or false if it has been chosen or not
     */
    public boolean isChosen(){
        return chosen;
    }

    /**
     * Setter of Picked
     * @param state value to assign
     */
    public void setPicked(boolean state){
        this.picked = state;
    }

    /**
     * Getter of picked
     * @return true or false if it has been picked or not
     */
    public boolean isPicked(){
        return picked;
    }

    /**
     * Getter of name
     * @return name of the card
     */
    public String getName(){
        return name;
    }
}
