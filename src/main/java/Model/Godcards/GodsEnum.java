package Model.Godcards;

/**
 * Enumeration class of implemented godcards
 */
public enum GodsEnum {
        APOLLO (0),
        ARTEMIS (1),
        ATHENA (2),
        ATLAS (3),
        CHRONUS (4),
        DEMETER (5),
        HEPHAESTUS (6),
        HERA (7),
        HESTIA (8),
        MINOTAUR (9),
        PAN (10),
        PROMETEO (11),
        TRITON (12),
        ZEUS (13);
        private int index;

        private GodsEnum(int index){
                this.index = index;
        }

        public int getIndex() {
                return index;
        }
}
