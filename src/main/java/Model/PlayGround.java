package Model;

import java.util.ArrayList;

public class PlayGround {
    private static PlayGround instance = null;
    private Box[][] grid;
    private final int SIZE = 5;


    /**
     * Load the saved status of the map
     */
    public void restore(ArrayList <Box> grid){

    }          //to implement

    /**
     * Save the map status
     */
    public void saveState(){

    }             //to implement

    /**
     * Private constructor of the Playground (Singleton)
     */
    private PlayGround(){
        this.grid = new Box[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                this.grid[i][j] = new Box(i,j);
            }
        }
    }

    /**
     * Get a reference to the Playground (Singleton), creates a new object if none is present
     */
    public static PlayGround getInstance(){
        if (instance == null){
            instance = new PlayGround();
        }
        return instance;
    }

    /** Returns the Box in the indicate position
     * @param x the x coordinate of the Box requested
     * @param y the y coordinate of the Box requested
     * @return Box requested
     * @throws ArrayIndexOutOfBoundsException if asking an invalid position
     */
    public Box getBox(int x, int y) {
        try {
            return grid[x][y];
        }
        catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            return null;
        }
    }

    public Box[][] getGrid() {
        return grid;
    }

    /**
     * Delete all the playground (support method for testing)
     */
    public void Delete(){
        instance = null;
    }



    public int getSIZE() {
        return SIZE;
    }
}
