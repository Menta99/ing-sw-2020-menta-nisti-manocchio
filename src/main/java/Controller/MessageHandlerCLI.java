package Controller;

import Model.*;
import Model.Godcards.GodCard;
import Model.Godcards.GodsEnum;
import View.Colors;
import View.cli.Cli;

import java.util.ArrayList;
import java.util.Scanner;

public class MessageHandlerCLI {


    public static boolean legalCoordinates(int i, int j) {
        int size = PlayGround.getInstance().getSIZE();
        if (i < 0 || j < 0 || i > size - 1 || j > size - 1) {
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
            notValidDestination();
        }
    }

    public static boolean askForPower() {
        System.out.println("Would you like to use your divinity Power? \nyes / no");
        String msg = null;
        Scanner in = new Scanner(System.in);
        while (msg == null || !msg.equals("yes") && !msg.equals("no")) {
            msg = in.nextLine();
        }
        if (msg.equals("yes")) {
            return true;
        } else {
            return false;
        }
    }

    public static Worker askForWorker() {
        System.out.println("\nSelect one of your workers");
        Box box = askForCoordinates();
        Worker candidate = Game.getInstance().getActualPlayer().selectWorker(box);
        if (candidate == null) {
            System.out.println("\nNot a valid worker");
            return askForWorker();
        }
        return candidate;
    }

    public static Box askForMovement() {
        System.out.println("Where do you want to move your worker?");
        return askForCoordinates();
    }

    public static Box askForBuilding() {
        System.out.println("Where do you want to build?");
        return askForCoordinates();
    }

    public static void notValidDestination() {
        System.out.println("Not a valid destination, try again!");
    }

    public static Box askForPlacement(int workerNumber) {
        System.out.println("\nWhere do you want to place Worker number " + workerNumber + "?");
        return askForCoordinates();
    }

    public static void lose(){
        System.out.println("You Lost! Dumbo's");
    }

    public static void gameFinished(){
        String winner = Game.getInstance().getWinner().getNickName();
        System.out.println(winner + " has won !\nEverybody clap your hands!");
        Game.getInstance().Delete();
        PlayGround.getInstance().Delete();
        Controller controller = new Controller();
        controller.Hall(controller.getCli());
    }

    public static int askNumOfPlayers(){
        System.out.println("How many players?");
        Scanner in = new Scanner(System.in);
        int i = 0;
        while (i != 2 && i!=3){
            i = in.nextInt();
        }
        return i;
    }

    public static String askPlayerName(){
        Scanner in = new Scanner(System.in);
        System.out.println("What's your name?");
        String name;
        name = in.nextLine();
        for (Player player: Game.getInstance().getPlayer()){
            if (name.equals(player.getNickName())){
                System.out.println("Name already in use");
                return askPlayerName();
            }
        }
        return name;
    }

    public static void Hall(Cli cli){
        Scanner in = new Scanner((System.in));
        cli.ShowWelcomeScreen();
        int numOfPlayers = askNumOfPlayers();
        String playerName;
        while (Game.getInstance().getPlayer().size() < numOfPlayers){
            new Player(askPlayerName());
         }
        System.out.println(numOfPlayers + " players joined, let's begin");
        System.out.println(Game.getInstance().getPlayer().get(0).getNickName() + " press 'start' to start the game");
        while(!in.nextLine().equals("start")){};
        ChooseGodPhase(cli);
    }

    public static void ChooseGodPhase(Cli cli){
        cli.ShowGodList();
        Player player;
        int numOfPlayers = Game.getInstance().getPlayer().size();
        boolean alreadyIn = false;
        Scanner in = new Scanner((System.in));
        System.out.println("Please " + Game.getInstance().getPlayer().get(0).getColor() + Game.getInstance().getPlayer().get(0).getNickName() + Colors.RESET
                + " Select " + numOfPlayers + " GodCards indicating its number");
        ArrayList<Integer> index = new ArrayList<Integer>();
        int[] indici = new int[numOfPlayers];
        Integer candidato;
        while (index.size() < numOfPlayers){
            candidato = in.nextInt();
            if (!(candidato<0 || candidato > 14)){
                if (!index.contains(candidato)){
                    indici[index.size()] = candidato;
                    index.add(candidato);
                    System.out.println("God n°" + candidato + " added");
                }
            }
        }
        Game.getInstance().ExtractCard(indici);
        for (int i = 0; i < numOfPlayers; i++){
            if (i == numOfPlayers-1){
                player = Game.getInstance().getPlayer().get(0);
            }
            else {
                player = Game.getInstance().getPlayer().get(i + 1);
            }
            System.out.println(player.getColor() +  player.getNickName() +  Colors.RESET + " Pick a card");
            int j=0;
            for(GodCard card : Game.getInstance().getActiveCards()){
                if (!card.isPicked()){
                    System.out.println("Insert " + j + " to get " + card.getName());
                }
                j++;
            }
            do{
                candidato=in.nextInt();
            }while (candidato<0 || candidato >= numOfPlayers || Game.getInstance().getActiveCards().get(candidato).isPicked());
            player.ChooseGod(candidato);
        }
        cli.ShowInGameGods();
        GamePhase(cli);
    }

    public static void GamePhase(Cli cli){
        Game.getInstance().StartGame();
    }

    public static void TurnStartMessage(){
        Player actual = Game.getInstance().getActualPlayer();
        System.out.println(actual.getColor() + actual.getNickName() + Colors.RESET + " Turn Start!");
    }

    public static void Reset(){}
}
