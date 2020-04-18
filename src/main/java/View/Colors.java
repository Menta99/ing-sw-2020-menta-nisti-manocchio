package View;

public enum Colors {
    GREEN("\u001B[32m"),
    BLUE("\u001B[34m"),
    RED("\u001B[31m"),
    YELLOW("\u001B[33m"),
    PURPLE("\u001B[35m");
    public static final String RESET = "\u001B[0m";

    private String colore;
    private int cod;

    Colors(String color){
        this.colore=color;
    }

    @Override
    public String toString() { return colore; }

}
