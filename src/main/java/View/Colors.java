package View;

/**
 * Enumeration class of some colors
 */
public enum Colors {
    GREEN("\u001B[32m"),
    BLUE("\u001B[34m"),
    RED("\u001B[31m"),
    YELLOW("\u001B[33m"),
    PURPLE("\u001B[35m"),
    RESET("\u001B[0m");

    private final String color;

    /**
     * Setter of a color
     * @param color is the color you desire
     */
    Colors(String color){
        this.color = color;
    }

    /**
     * Override method of toString
     * @return the corresponding string
     */
    @Override
    public String toString() { return color; }
}
