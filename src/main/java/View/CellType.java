package View;

public enum CellType {
    VOID (null),
    LEVEL_1 ("/Cells/Building/level_1.png"),
    LEVEL_2 ("/Cells/Building/level_2.png"),
    LEVEL_3 ("/Cells/Building/level_3.png"),
    DOME ("/Cells/Building/dome.png"),
    LEVEL_12 ("/Cells/Building/level_12.png"),
    LEVEL_123 ("/Cells/Building/level_123.png"),
    LEVEL_123D ("/Cells/Building/level_123D.png"),
    LEVEL_1D ("/Cells/Building/level_1D.png"),
    LEVEL_12D ("/Cells/Building/level_12D.png"),
    LEVEL_1_E ("/Cells/Building/level_1_e.png"),
    LEVEL_12_E ("/Cells/Building/level_12_e.png"),
    LEVEL_123_E ("/Cells/Building/level_123_e.png"),
    GLOW ("/Cells/Building/glow.png"),
    PLAYER_0 ("/Cells/Building/player_yellow.png"),
    PLAYER_1 ("/Cells/Building/player_blue.png"),
    PLAYER_2 ("/Cells/Building/player_red.png");

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
