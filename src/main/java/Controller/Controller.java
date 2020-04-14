package Controller;

import Model.Box;
import Model.PlayGround;
import java.util.Scanner;

public class Controller {

    public static boolean legalCoordinates(int i, int j){
        int size = PlayGround.getInstance().getSIZE();
        if(i<0 || j<0 || i > size-1 || j > size-1){
            return false;
        }
        return true;
    }

    public static Box askForCoordinates() {
        Scanner in = new Scanner(System.in);
        int x, y;
        while (true) {
            if (legalCoordinates(x = in.nextInt(), y = in.nextInt())) {
                return PlayGround.getInstance().getBox(x, y);
            }
        }
    }
}
