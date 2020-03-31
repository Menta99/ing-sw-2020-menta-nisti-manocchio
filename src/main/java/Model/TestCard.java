package Model;

public class TestCard extends GodCard {
    private String name;
    private String power;
    private String activePhase;
    private boolean chosen;
    private boolean picked;

    public TestCard(String name){
        this.name = name;
        this.power = null;
        this.activePhase = null;
        this.chosen = false;
        this.picked = false;
    }
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
