package Model;

/**
 * Enumeration class of possible type of pawn
 */
public enum PawnType {
        GROUND_LEVEL(0),
        Level_1(1),
        Level_2(2),
        Level_3(3),
        DOME(4),
        WORKER(-1);
        private final int value;

        /**
         * Constructor of the enumeration PawnType
         * @param value default value for the PawnType
         */
        PawnType(int value){
            this.value = value;
        }

        public int getValue(){
            return value;
        }
    }




