package View.cli;

import Controller.Controller;
import Model.*;
import Model.Godcards.GodCard;
import View.Colors;
import java.util.ArrayList;
import java.util.Scanner;
public class Cli {
    private Scanner scanner = new Scanner(System.in);
    private String msg;
    private int x,y;
    private Game myGame;
    private PlayGround myMap;
    //Player me
    //Client

    public Cli(){
        myGame = Game.getInstance();
        myMap = PlayGround.getInstance();
    }

    public void ShowCommands(){
        System.out.println("map - To show the map of the game\nplayers - To show who is playing\n" +
                "godlist - shows all the available godcards of the game and relative powers\n"+
                "gods - shows all the godcards in game\nhelp - shows  full command list");
    }


    public void ShowWelcomeScreen(){
        System.out.println( Colors.PURPLE +"╭╮╭╮╭╮"+ Colors.YELLOW +"╱╱"+ Colors.PURPLE +"╭╮\n" +
                "┃┃┃┃┃┃"+ Colors.YELLOW + "╱╱"+ Colors.PURPLE +"┃┃\n" +
                "┃┃┃┃┃┣━━┫┃╭━━┳━━┳╮╭┳━━╮\n" +
                "┃╰╯╰╯┃┃━┫┃┃╭━┫╭╮┃╰╯┃┃━┫\n" +
                "╰╮╭╮╭┫┃━┫╰┫╰━┫╰╯┃┃┃┃┃━┫\n" +
                Colors.YELLOW +"╱"+ Colors.PURPLE +"╰╯╰╯╰━━┻━┻━━┻━━┻┻┻┻━━╯\n" +
                "╭━━━━╮"+ Colors.YELLOW +"╱╱"+ Colors.PURPLE +"╭━━━╮"+ Colors.YELLOW +"╱╱╱╱╱"+ Colors.PURPLE +"╭╮\n" +
                "┃╭╮╭╮┃"+ Colors.YELLOW +"╱╱"+ Colors.PURPLE +"┃╭━╮┃"+ Colors.YELLOW +"╱╱╱╱"+ Colors.PURPLE +"╭╯╰╮\n" +
                "╰╯┃┃┣┻━╮┃╰━━┳━━┳━╋╮╭╋━━┳━┳┳━╮╭╮\n" +
                 Colors.YELLOW +"╱╱"+ Colors.PURPLE +"┃┃┃╭╮┃╰━━╮┃╭╮┃╭╮┫┃┃╭╮┃╭╋┫╭╮╋┫\n" +
                 Colors.YELLOW +"╱╱"+ Colors.PURPLE +"┃┃┃╰╯┃┃╰━╯┃╭╮┃┃┃┃╰┫╰╯┃┃┃┃┃┃┃┃\n" +
                 Colors.YELLOW +"╱╱"+ Colors.PURPLE +"╰╯╰━━╯╰━━━┻╯╰┻╯╰┻━┻━━┻╯╰┻╯╰┻╯" + Colors.RESET);
        System.out.println("Please enter your name\n");
        msg = scanner.nextLine();
        System.out.flush();
    }

    public void ShowMap(){
        Box myBox;
        System.out.println("          X: ⇉     Y: ⇊     \n");
        System.out.println("  ╔═════════════════════════════╗");
        for (int i=myMap.getSIZE()-1; i>=0; i--){
            System.out.print(i + " ║");
            for (int j=0; j<myMap.getSIZE(); j++) {
                myBox = myMap.getBox(j, i);
                if (myBox.getUpperLevel() == PawnType.DOME) {
                    System.out.print("  D  ");
                } else if (myBox.isOccupied()) {
                    //distinzione myworker enemyWorker
                    System.out.print(" ☻:");
                    System.out.print((myBox.getStructure().size() - 2) + " ");

                } else if (myBox.Playable()){
                    System.out.print("  " + (myBox.getStructure().size() - 1) + "  ");
                }
                if (j!=4){
                    System.out.print(" ");
                }
            }
            System.out.print("║\n");
            if (i!=0) {
                System.out.println("  ║     ┼     ┼     ┼     ┼     ║");
            }
        }
        System.out.println("  ╚═════════════════════════════╝");
        System.out.println("     0     1     2     3     4   ");
        System.out.flush();
    };


    public void ShowPlayers(){
        ArrayList<Player> inGamePlayers = Game.getInstance().getPlayer();
        System.out.println("List of players:");
        for(Player player : inGamePlayers){
            //if enemy Red, else Green
            System.out.println(player.getNickName());
        }
        System.out.flush();
    }

    public void ShowGodList(){
        int i=0;
        for(GodCard god : myGame.getDeck().getCardList()){
            System.out.println("["+ i + "] " + god.getName() + " - " + god.getPower());
            i++;
        }
        System.out.flush();
    }

    public void ShowInGameGods(){
        for (GodCard god : myGame.getActiveCards()){
            System.out.println("Player: " + god.getOwner().getNickName() + " GodCard: " + god.getName());
        }
        System.out.flush();
    }


    public void ShowAvailableMovement(int x, int y){
        if (Controller.legalCoordinates(x, y)){
            Box myBox = PlayGround.getInstance().getBox(x, y);
            ArrayList<Box> availableBoxes = new ArrayList<Box>();
            if (myBox.isOccupied()){
                Worker myWorker = (Worker) myBox.getStructure().get(myBox.getStructure().size()-1);
                for (Box box : myBox.BorderBoxes()){
                    if (myWorker.LegalMovement(box)){
                        availableBoxes.add(box);
                    }
                }
            }
        }
        // Se la casella appartiene alle availableBoxes colore diverso
        // la casella in posizione x, y ancora color diverso
        else {
            System.out.println("Not valid coordinates, please try again");
        }
        System.out.flush();
    }

    public void ShowAvailableBuilding(int x, int y){
        if (Controller.legalCoordinates(x, y)){
            Box myBox = PlayGround.getInstance().getBox(x,y);
            ArrayList<Box> availableBoxes = new ArrayList<Box>();
            if (myBox.isOccupied()){
                Worker myWorker = (Worker) myBox.getStructure().get(myBox.getStructure().size()-1);
                for (Box box : myBox.BorderBoxes()){
                    if (myWorker.LegalMovement(box)){
                        availableBoxes.add(box);
                    }
                }
            }
        }
        // Se la casella appartiene alle availableBoxes colore diverso
        // la casella in posizione x, y ancora color diverso
        else{
            System.out.println("Not valid coordinates, please try again");
        }
        System.out.flush();
    }
}
