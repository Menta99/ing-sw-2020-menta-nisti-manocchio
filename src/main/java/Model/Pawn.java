package Model;


/**Represents the type of a single piece of the game */
public abstract class Pawn {

    private Box position;
    private PawnType type;

    /**
     * Getter of the piece's type
     * @return type
     */
    public PawnType getType(){
           return type;
    }

    /**
     * Setter of piece's type
     * @param type
     */
    public void setType(PawnType type) {
        this.type = type;
    }

    /**
     * Getter of the piece's Box
     * @return position
     */public Box getPosition() {
        return position;
    }


    /**
     * Setter of position
     * @param position
     */
    public void setPosition(Box position) {
        this.position = position;
    }
}
