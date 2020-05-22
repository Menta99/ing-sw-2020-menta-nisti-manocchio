package View;

public enum CellType {
    VOID (null),
    LEVEL_1 ("Cells/level_1.png"),
    LEVEL_2 ("Cells/level_2.png"),
    LEVEL_3 ("Cells/level_3.png"),
    DOME ("Cells/dome.png"),
    LEVEL_12 ("Cells/level_12.png"),
    LEVEL_123 ("Cells/level_123.png"),
    LEVEL_123D ("Cells/level_123D.png"),
    LEVEL_1D ("Cells/level_1D.png"),
    LEVEL_12D ("Cells/level_12D.png"),
    LEVEL_1_E ("Cells/level_1_e.png"),
    LEVEL_12_E ("Cells/level_12_e.png"),
    LEVEL_123_E ("Cells/level_123_e.png"),
    GLOW ("Cells/glow.png"),
    PLAYER_0 ("Cells/player_yellow.png"),
    PLAYER_1 ("Cells/player_blue.png"),
    PLAYER_2 ("Cells/player_red.png");

    private final String path;

    CellType(String path) {
        this.path = path;
    }

    /**
     * Override method of toString
     * @return the corresponding string
     */
    @Override
    public String toString() { return path; }
}
