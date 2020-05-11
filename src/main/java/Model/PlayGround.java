package Model;

import Controller.SavedData.GameData;

import java.util.ArrayList;

/**
 * Class representing the game's field
 */
public class PlayGround {
    private static PlayGround instance = null;
    private Box[][] grid;
    private final int SIZE = 5;


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
     * Clean the playground
     */
    public void Clean(){
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
            System.err.println("Index not valid");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Getter of the Board Size
     */
    public int getSIZE() {
        return SIZE;
    }

    public void loadMap(ArrayList<String> gameData){
        Box myBox;
        for (int i=0; i < PlayGround.getInstance().getSIZE(); i++){
            for(int j=0; j < PlayGround.getInstance().getSIZE(); j++){
                myBox = PlayGround.getInstance().getBox(i,j);
                if (gameData.get( i*25 + j*5 + GameData.Size()).equals("1")){  //For every Box there are 5 booleans as explained in mapData toString() method, the first indicates if the box is occupied
                    myBox.setOccupied(true);
                }
                for (int k=1; k < 4; k++ ){
                    if (gameData.get(i*25 + j*5 + k + GameData.Size()).equals("1")){
                        myBox.Build();
                    }
                }
                if (gameData.get(i*25 + j*5 + 4 + GameData.Size()).equals("1")){
                    myBox.BuildDome();
                }
            }
        }
    }
}
