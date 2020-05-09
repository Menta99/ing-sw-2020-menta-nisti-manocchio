package View.CLI;

import Client.ConnectionHandler;
import ComunicationProtocol.*;
import Model.PawnType;
import View.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class representing the command line interface
 */
public class Cli extends View {
    private Scanner scanner;
    private BoxInfo[][] map;
    private GodInfo[] gods;
    private PlayerInfo[] players;
    private String nickname;

    /**
     * Constructor of the class
     */
    public Cli(){
        this.scanner = new Scanner(System.in);
    }

    /**
     * Handler for CommandType Name
     * @param command
     * @param client
     */
    public void NameHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command);
        boolean found = false;
        String msg = scanner.nextLine().trim();
        while(!found){
            if(msg.equalsIgnoreCase("")) {
                System.out.println("Insert a non-void name, retry");
                msg = scanner.nextLine().trim();
            }
            else{
                found = true;
                if(command.getPlayers() != null) {
                    for (PlayerInfo player : command.getPlayers()) {
                        if (player.getName().equalsIgnoreCase(msg)) {
                            found = false;
                        }
                    }
                    if (!found) {
                        System.out.println("Name already chosen, retry");
                        msg = scanner.nextLine().trim();
                    }
                }
            }
        }
        nickname = msg;
        client.WriteMessage(new ServerMsg(msg));
    }

    /**
     * Handler for CommandType First
     * @param command
     * @param client
     */
    public void FirstHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command);
        ArrayList<Integer> output = new ArrayList<>();
        String line = scanner.nextLine().trim();
        while(!line.equalsIgnoreCase("2") && !line.equalsIgnoreCase("3")) {
            System.out.println("Invalid input, required '2' or '3', retry");
            line = scanner.nextLine().trim();
        }
        output.add(Integer.parseInt(line));
        client.WriteMessage(new ServerMsg(output));
    } //ready


    /**
     * Handler for CommandType Number
     * @param command
     * @param client
     */
    public void NumberHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command);
        ArrayList<Integer> output = new ArrayList<>();
        boolean found = false;
        int result;
        String line = scanner.nextLine().trim();
        while (!found){
            if(line.matches("\\d+")){
                result = Integer.parseInt(line);
                if(result > -1 && result < command.getList().size()){
                    output.add(command.getList().get(result));
                    found = true;
                }
                else {
                    System.out.println("Invalid index, retry");
                    line = scanner.nextLine().trim();
                }
            }
            else{
                System.out.println("Invalid input, required a number, retry");
                line = scanner.nextLine().trim();
            }
        }
        client.WriteMessage(new ServerMsg(output));
    }//ready

    /**
     * Handler for CommandType Coordinates
     * @param command
     * @param client
     */
    public void CoordinatesHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command);
        ArrayList<Integer> coordinates = new ArrayList<>();
        System.out.println("Insert the coordinates");
        boolean found = false;
        String x;
        String y;
        int result_x;
        int result_y;
        String line = scanner.nextLine().trim();
        while (!found){
            if(line.matches("\\d+\\s+\\d+")){
                x = line.substring(0, line.indexOf(' '));
                y = line.substring(line.indexOf(x) + 1).trim();
                result_x = Integer.parseInt(x);
                result_y = Integer.parseInt(y);
                if((result_x > -1) && (result_x < 5) && (result_y > -1) && (result_y < 5)){
                    coordinates.add(result_x);
                    coordinates.add(result_y);
                    found = true;
                }
                else{
                    System.out.println("Invalid index, required numbers between '0' and '4', retry");
                    line = scanner.nextLine().trim();
                }
            }
            else{
                System.out.println("Invalid input, required two numbers, retry");
                line = scanner.nextLine().trim();
            }
        }
        client.WriteMessage(new ServerMsg(coordinates));
    }//ready

    /**
     * Handler for CommandType Answer
     * @param command
     * @param client
     */
    public void AnswerHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command);
        String msg = scanner.nextLine().trim();
        while ((!msg.equalsIgnoreCase("yes") && !msg.equalsIgnoreCase("no"))){
            System.out.println("Invalid input, required 'yes' or 'no', retry");
            msg = scanner.nextLine().trim();
        }
        client.WriteMessage(new ServerMsg(msg));
    }//ready

    /**
     * Handler for CommandType God
     * @param command
     * @param client
     */
    public void GodHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command);
        ArrayList<Integer> index = new ArrayList<>();
        int number;
        String line;
        while (index.size() < command.getList().get(0)){
            line = scanner.nextLine().trim();
            if(line.matches("\\d+")) {
                number = Integer.parseInt(line);
                if (number > -1 && number < 14) {
                    if (!index.contains(number)) {
                        index.add(number);
                        System.out.println("God n°" + number + " added");
                    }
                    else {
                        System.out.println("Already selected God, retry");
                    }
                }
                else {
                    System.out.println("Index not valid, required number between 0 and 13");
                }
            }
            else{
                System.out.println("Invalid input, required a number, retry");
            }
        }
        client.WriteMessage(new ServerMsg(index));
    }//ready

    /**
     * Print a updating message
     * @param command
     */
    public void UpdateHandler(CliCommandMsg command){
        map = command.getMap();
        ShowMap(map);
        Display(command);
    }//ready

    /**
     * Closing a ClientHandler...
     * @param command
     * @param client
     */
    public void CloseHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command);
        client.setActive(false);
    }//ready

    /**
     * Handler for CommandType Communication
     * @param command
     */
    public void CommunicationHandler(CliCommandMsg command){
        switch (command.getSubCommandType()){
            case DEFAULT:
                break;
            case WELCOME:
                WelcomeScreen();
                gods = command.getGods();
                players = command.getPlayers();
                break;
            case IN_GAME_GOD:
                GodInGame(command.getList());
                break;
            case PLAYER_GOD:
                PlayerGod(command.getPlayers());
                for (PlayerInfo user : players) {
                    System.out.println("Player: " + user.getColor() + user.getName() + Colors.RESET + " GodCard: " + gods[user.getGod()].getName());
                }
                break;
            case WAIT:
                System.out.println("Waiting the other player's choice");
                break;
            case NOT_VALID:
                if(command.getList().get(0) == 0){
                    System.out.println("Not a valid destination, retry");
                }
                else {
                    System.out.println("Not a valid worker, retry");
                }
                break;
            case LOSE:
                System.out.println("You lose Dumbo");
                break;
        }
    }//ready

    /**
     * Print the msg messages
     * @param command
     */
    public void Display(CliCommandMsg command){
        switch (command.getCommandType()){
            case NAME:
                System.out.println("Insert your NickName");
                break;
            case FIRST:
                System.out.println("You are the Challenger!\nTell me the Number of Players of this Match");
                break;
            case COORDINATES:
                switch (command.getList().get(0)){
                    case 0:
                        System.out.println("Where do you want to place your Worker?");
                        break;
                    case 1:
                        System.out.println("Select one of your Workers");
                        break;
                    case 2:
                        System.out.println("Where do you want to Move?");
                        break;
                    case 3:
                        System.out.println("Where do you want to Build?");
                        break;
                    default:
                        break;
                }
                break;
            case NUMBER:
                for(Integer god : command.getList()){
                    System.out.println("Insert " + command.getList().indexOf(god) + " to get " + gods[god].getName());
                }
                break;
            case ANSWER:
                System.out.println("Would you like to use your divinity Power? \nyes / no");
                break;
            case GOD:
                System.out.println("These are the gods available :");
                for(GodInfo god : gods){
                    System.out.println("[" + god.getPosition() + "] " + god.getName() + " - " + god.getPower());
                }
                System.out.println("Please " + players[0].getColor() + players[0].getName() + Colors.RESET
                        + " Select " + players.length + " GodCards indicating their numbers");
                break;
            case CLOSE:
                if(command.getList().get(0) == -1){
                    System.out.println(players[command.getList().get(1)].getColor() + players[command.getList().get(1)].getName() + Colors.RESET + " disconnected\nEndGame");
                }
                else if(command.getList().get(0) == 1){
                    System.out.println("Server is down");
                }
                else{
                    PlayerInfo winner = players[command.getList().get(1)];
                    if(nickname.equalsIgnoreCase(winner.getName())){
                        System.out.println("You have won\nCongratulations");
                    }
                    else {
                        System.out.println(winner.getColor() + winner.getName() + Colors.RESET + " has won!\nEverybody clap your hands!");
                    }
                }
                break;
            case UPDATE:
                if(command.getList() != null){
                    PlayerInfo actual = players[command.getList().get(0)];
                    if(actual.getName().equalsIgnoreCase(nickname)){
                        if(command.getList().get(1)!=0) {
                            System.out.println(actual.getColor() + actual.getName() + Colors.RESET + " Turn Start!");
                        }
                    }
                    else {
                        if (command.getList().get(1) == 0) {
                            System.out.println("Look at " + actual + actual.getName() + Colors.RESET + "'s move");
                        }
                        else {
                            System.out.println(actual.getColor() + actual.getName() + Colors.RESET + " is playing\nWait your Turn!");
                        }
                    }
                }
                break;
            case COMMUNICATION:
                break;
        }
    }//ready

    /**
     * Print the game's map
     * @param map
     */
    public void ShowMap(BoxInfo[][] map){
        String line = "";
        ArrayList<String> output = new ArrayList<>();
        output.add("          X: ⇉     Y: ⇊     \n");
        output.add("  ╔═════════════════════════════╗");
        for(int i = 4; i >= 0; i--){
            line = line.concat(i + " ║");
            for(int j = 0; j <= 4; j++){
                if (map[j][i].getLastBuilding() == PawnType.DOME) {
                    line = line.concat("  D  ");
                } else if (map[j][i].getLastBuilding() == PawnType.WORKER) {
                    line = line.concat(map[j][i].getWorkerColor() + " ☻" + Colors.RESET + ":");
                    line = line.concat(map[j][i].getBoxColor() + "" + (map[j][i].getHeight() - 2) + Colors.RESET + " ");
                } else{
                    line = line.concat("  " + map[j][i].getBoxColor() + "" + (map[j][i].getHeight() - 1) + Colors.RESET + "  ");
                }
                if (j != 4) {
                    line = line.concat(" ");
                }
            }
            line = line.concat("║");
            output.add(line);
            line = "";
            if (i != 0) {
                output.add("  ║     ┼     ┼     ┼     ┼     ║");
            }
        }
        output.add("  ╚═════════════════════════════╝");
        output.add("     0     1     2     3     4   ");
        for(String string : output){
            System.out.println(string);
        }
    }//ready

    /**
     * Updates the god list, marking the active cards
     * @param list
     */
    public void GodInGame(ArrayList<Integer> list){
        GodInfo[] update = new GodInfo[14];
        for(GodInfo god : gods){
            if(list.contains(god.getPosition())){
                update[god.getPosition()] = new GodInfo(god.getPosition(), god.getName(), god.getPower(), true);
            }
            else{
                update[god.getPosition()] = new GodInfo(god.getPosition(), god.getName(), god.getPower(), false);
            }
        }
        gods = update;
    }//ready

    /**
     * Updates the player list, inserting the respective god
     * @param playerInfo
     */
    public void PlayerGod(PlayerInfo[] playerInfo){
        PlayerInfo[] update = new PlayerInfo[players.length];
        for(PlayerInfo user : players){
            update[user.getPosition()] = new PlayerInfo(user.getPosition(), user.getName(), user.getColor(), playerInfo[user.getPosition()].getGod());
        }
        players = update;
    }

    /**
     * Prints the Welcome Screen
     */
    public void WelcomeScreen() {
        System.out.println("\n"+ Colors.PURPLE + "╭╮╭╮╭╮" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "╭╮");
        System.out.println("┃┃┃┃┃┃" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃┃");
        System.out.println("┃┃┃┃┃┣━━┫┃╭━━┳━━┳╮╭┳━━╮\n┃╰╯╰╯┃┃━┫┃┃╭━┫╭╮┃╰╯┃┃━┫\n╰╮╭╮╭┫┃━┫╰┫╰━┫╰╯┃┃┃┃┃━┫");
        System.out.println(Colors.YELLOW + "╱" + Colors.PURPLE + "╰╯╰╯╰━━┻━┻━━┻━━┻┻┻┻━━╯");
        System.out.println("╭━━━━╮" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "╭━━━╮" + Colors.YELLOW + "╱╱╱╱╱" + Colors.PURPLE + "╭╮");
        System.out.println("┃╭╮╭╮┃" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃╭━╮┃" + Colors.YELLOW + "╱╱╱╱" + Colors.PURPLE + "╭╯╰╮");
        System.out.println("╰╯┃┃┣┻━╮┃╰━━┳━━┳━╋╮╭╋━━┳━┳┳━╮╭╮");
        System.out.println(Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃┃┃╭╮┃╰━━╮┃╭╮┃╭╮┫┃┃╭╮┃╭╋┫╭╮╋┫");
        System.out.println(Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃┃┃╰╯┃┃╰━╯┃╭╮┃┃┃┃╰┫╰╯┃┃┃┃┃┃┃┃");
        System.out.println(Colors.YELLOW + "╱╱" + Colors.PURPLE + "╰╯╰━━╯╰━━━┻╯╰┻╯╰┻━┻━━┻╯╰┻╯╰┻╯" + Colors.RESET + "\n");
    }
}