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
    private int x, y;
    //Player me
    //Client

    public Cli() {
    }

    public void ShowCommands() {
        System.out.println("map - To show the map of the game\nplayers - To show who is playing\n" +
                "godlist - shows all the available godcards of the game and relative powers\n" +
                "gods - shows all the godcards in game\nhelp - shows  full command list");
    }


    public void ShowWelcomeScreen() {
        System.out.println(Colors.PURPLE + "╭╮╭╮╭╮" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "╭╮\n" +
                "┃┃┃┃┃┃" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃┃\n" +
                "┃┃┃┃┃┣━━┫┃╭━━┳━━┳╮╭┳━━╮\n" +
                "┃╰╯╰╯┃┃━┫┃┃╭━┫╭╮┃╰╯┃┃━┫\n" +
                "╰╮╭╮╭┫┃━┫╰┫╰━┫╰╯┃┃┃┃┃━┫\n" +
                Colors.YELLOW + "╱" + Colors.PURPLE + "╰╯╰╯╰━━┻━┻━━┻━━┻┻┻┻━━╯\n" +
                "╭━━━━╮" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "╭━━━╮" + Colors.YELLOW + "╱╱╱╱╱" + Colors.PURPLE + "╭╮\n" +
                "┃╭╮╭╮┃" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃╭━╮┃" + Colors.YELLOW + "╱╱╱╱" + Colors.PURPLE + "╭╯╰╮\n" +
                "╰╯┃┃┣┻━╮┃╰━━┳━━┳━╋╮╭╋━━┳━┳┳━╮╭╮\n" +
                Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃┃┃╭╮┃╰━━╮┃╭╮┃╭╮┫┃┃╭╮┃╭╋┫╭╮╋┫\n" +
                Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃┃┃╰╯┃┃╰━╯┃╭╮┃┃┃┃╰┫╰╯┃┃┃┃┃┃┃┃\n" +
                Colors.YELLOW + "╱╱" + Colors.PURPLE + "╰╯╰━━╯╰━━━┻╯╰┻╯╰┻━┻━━┻╯╰┻╯╰┻╯" + Colors.RESET);
        System.out.flush();
    }

    public void ShowMap() {
        PlayGround myMap = PlayGround.getInstance();
            Box myBox;
            Player player;
            System.out.println("          X: ⇉     Y: ⇊     \n");
            System.out.println("  ╔═════════════════════════════╗");
            for (int i = myMap.getSIZE() - 1; i >= 0; i--) {
                System.out.print(i + " ║");
                for (int j = 0; j < myMap.getSIZE(); j++) {
                    myBox = myMap.getBox(j, i);
                    if (myBox.getUpperLevel() == PawnType.DOME) {
                        System.out.print("  D  ");
                    } else if (myBox.isOccupied()) {
                        player = ((Worker) myBox.getStructure().get(myBox.getStructure().size() - 1)).getOwner();
                        System.out.print(player.getColor() + " ☻" + Colors.RESET + ":");
                        System.out.print((myBox.getStructure().size() - 2) + " ");

                    } else if (myBox.Playable()) {
                        System.out.print("  " + (myBox.getStructure().size() - 1) + "  ");
                    }
                    if (j != 4) {
                        System.out.print(" ");
                    }
                }
            System.out.print("║\n");
            if (i != 0) {
                System.out.println("  ║     ┼     ┼     ┼     ┼     ║");
            }
        }
        System.out.println("  ╚═════════════════════════════╝");
        System.out.println("     0     1     2     3     4   ");
        System.out.flush();
    }

    ;


    public void ShowPlayers() {
        ArrayList<Player> inGamePlayers = Game.getInstance().getPlayer();
        System.out.println("List of players:");
        for (Player player : inGamePlayers) {
            System.out.println(player.getColor() + player.getNickName());
        }
        System.out.flush();
    }

    public void ShowGodList() {
        Game myGame = Game.getInstance();
        int i = 0;
        for (GodCard god : myGame.getDeck().getCardList()) {
            System.out.println("[" + i + "] " + god.getName() + " - " + god.getPower());
            i++;
        }
        System.out.flush();
    }

    public void ShowInGameGods() {
        Game myGame = Game.getInstance();
        for (Player player : myGame.getPlayer()) {
            System.out.println("Player: " + player.getColor() + player.getNickName() + Colors.RESET + " GodCard: " + player.getCard().getName());
        }
        System.out.flush();
    }

    public void ShowAvailableMovement() {
        PlayGround myMap = PlayGround.getInstance();
        Box myBox;
        Player player;
        System.out.println("          X: ⇉     Y: ⇊     \n");
        System.out.println("  ╔═════════════════════════════╗");
        boolean legal;
        Player actualPlayer = Game.getInstance().getActualPlayer();
        Worker actual = actualPlayer.getSelectedWorker();
        for (int i = myMap.getSIZE() - 1; i >= 0; i--) {
            System.out.print(i + " ║");
            for (int j = 0; j < myMap.getSIZE(); j++) {
                myBox = myMap.getBox(j, i);
                legal = actual.LegalMovement(myBox);
                if (myBox.getUpperLevel() == PawnType.DOME) {
                    System.out.print("  D  ");
                } else if (myBox.isOccupied()) {
                    if (legal) {
                        player = ((Worker) myBox.getStructure().get(myBox.getStructure().size() - 1)).getOwner();
                        System.out.print(player.getColor() + " ☻" + Colors.RESET + ":");
                        System.out.print(actualPlayer.getColor() + "" + (myBox.getStructure().size() - 2) + " " + Colors.RESET);
                    } else {
                        player = ((Worker) myBox.getStructure().get(myBox.getStructure().size() - 1)).getOwner();
                        System.out.print(player.getColor() + " ☻" + Colors.RESET + ":");
                        System.out.print((myBox.getStructure().size() - 2) + " ");
                    }

                } else if (myBox.Playable()) {
                    if (legal) {
                        System.out.print("  " + actualPlayer.getColor() + (myBox.getStructure().size() - 1) + "  " + Colors.RESET);
                    } else {
                        System.out.print("  " + (myBox.getStructure().size() - 1) + "  ");
                    }
                }
                if (j != 4) {
                    System.out.print(" ");
                }
            }
            System.out.print("║\n");
            if (i != 0) {
                System.out.println("  ║     ┼     ┼     ┼     ┼     ║");
            }
        }
        System.out.println("  ╚═════════════════════════════╝");
        System.out.println("     0     1     2     3     4   ");
        System.out.flush();
    }

    public void ShowAvailableBuilding() {
        PlayGround myMap = PlayGround.getInstance();
        Box myBox;
        Player player;
        System.out.println("          X: ⇉     Y: ⇊     \n");
        System.out.println("  ╔═════════════════════════════╗");
        boolean legal;
        Player actualPlayer = Game.getInstance().getActualPlayer();
        Worker actual = actualPlayer.getSelectedWorker();
        for (int i = myMap.getSIZE() - 1; i >= 0; i--) {
            System.out.print(i + " ║");
            for (int j = 0; j < myMap.getSIZE(); j++) {
                myBox = myMap.getBox(j, i);
                legal = actual.LegalBuild(myBox);
                if (myBox.getUpperLevel() == PawnType.DOME) {
                    System.out.print("  D  ");
                } else if (myBox.isOccupied()) {
                    if (legal) {
                        player = ((Worker) myBox.getStructure().get(myBox.getStructure().size() - 1)).getOwner();
                        System.out.print(player.getColor() + " ☻" + Colors.RESET + ":");
                        System.out.print(actualPlayer.getColor() + "" + (myBox.getStructure().size() - 2) + " " + Colors.RESET);
                    } else {
                        player = ((Worker) myBox.getStructure().get(myBox.getStructure().size() - 1)).getOwner();
                        System.out.print(player.getColor() + " ☻" + Colors.RESET + ":");
                        System.out.print((myBox.getStructure().size() - 2) + " ");
                    }

                } else if (myBox.Playable()) {
                    if (legal) {
                        System.out.print("  " + actualPlayer.getColor() + (myBox.getStructure().size() - 1) + "  " + Colors.RESET);
                    } else {
                        System.out.print("  " + (myBox.getStructure().size() - 1) + "  ");
                    }
                }
                if (j != 4) {
                    System.out.print(" ");
                }
            }
            System.out.print("║\n");
            if (i != 0) {
                System.out.println("  ║     ┼     ┼     ┼     ┼     ║");
            }
        }
        System.out.println("  ╚═════════════════════════════╝");
        System.out.println("     0     1     2     3     4   ");
        System.out.flush();
    }
}

