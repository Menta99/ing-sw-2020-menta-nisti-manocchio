package Controller.SavedData;

import Model.Pawn;
import Model.PlayGround;

import java.util.ArrayList;

public class MapData {

    private boolean[][] firstFloor;
    private boolean[][] secondFloor;
    private boolean[][] thirdFloor;
    private boolean[][] dome;
    private boolean[][] occupiedList;

    public MapData(){
        int size = PlayGround.getInstance().getSIZE();
        firstFloor = new boolean[size][size];
        secondFloor = new boolean[size][size];
        thirdFloor = new boolean[size][size];
        dome = new boolean[size][size];
        occupiedList = new boolean[size][size];
    }

    public void update(){
        int i,j;
        ArrayList<Pawn> myBoxStructure;
        for(i=0;i<PlayGround.getInstance().getSIZE(); i++){
            for(j=0;j<PlayGround.getInstance().getSIZE();j++){
                reset(i,j);
                myBoxStructure=PlayGround.getInstance().getBox(i,j).getStructure();
                for(Pawn pawn : myBoxStructure){
                    switch (pawn.getType()){
                        case Level_1:
                            firstFloor[i][j] = true;
                            break;
                        case Level_2:
                            secondFloor[i][j] = true;
                            break;
                        case Level_3:
                            thirdFloor[i][j] = true;
                            break;
                        case DOME:
                            dome[i][j] = true;
                            break;
                        case WORKER:
                            occupiedList[i][j] = true;
                            break;
                        case GROUND_LEVEL:
                            break;
                    }
                }
            }
        }
    }

    public void reset(int i, int j){
        firstFloor[i][j]=false;
        secondFloor[i][j]=false;
        thirdFloor[i][j]=false;
        dome[i][j]=false;
        occupiedList[i][j]=false;
    }

    @Override
    public String toString(){
        String string = "";
        for (int i = 0; i<PlayGround.getInstance().getSIZE(); i++){
            for(int j = 0; j<PlayGround.getInstance().getSIZE(); j++){
                string = string.concat((occupiedList[i][j]) ? "1\n" : "0\n");
                string = string.concat((firstFloor[i][j]) ? "1\n" : "0\n");
                string = string.concat((secondFloor[i][j]) ? "1\n" : "0\n");
                string = string.concat((thirdFloor[i][j]) ? "1\n" : "0\n");
                string = string.concat((dome[i][j]) ? "1\n" : "0\n");
            }
        }
        return string;
    }

    public static int Size(){
        return 125;
    }
}
