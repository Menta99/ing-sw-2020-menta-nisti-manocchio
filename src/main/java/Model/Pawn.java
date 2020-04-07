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
     * Getter of the piece's Box
     * @return position
     */
    public Box getPosition() {
        return position;
    }
}
