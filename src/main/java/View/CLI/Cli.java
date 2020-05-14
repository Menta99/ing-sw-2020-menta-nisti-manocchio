package View.CLI;

import Client.ConnectionHandler;
import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.SantoriniInfo.BoxInfo;
import CommunicationProtocol.SantoriniInfo.GodInfo;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import CommunicationProtocol.ServerMsg;
import Model.PawnType;
import View.Colors;
import View.View;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class representing the Command Line Interface
 */
public class Cli implements View {
    private final Scanner scanner;
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
     * Handler for CommandType family Name
     * @param command CommandMsg send by the Server
     * @param client reference to the ConnectionHandler
     */
    public void NameHandler(CommandMsg command, ConnectionHandler client){
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
                if(command.getInfo() != null) {
                    for (PlayerInfo player : command.getInfo().getPlayers()) {
                        if (player.getName().equalsIgnoreCase(msg)) {
                            found = false;
                            break;
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
     * Handler for CommandType family First
     * @param command CommandMsg send by the Server
     * @param client reference to the ConnectionHandler
     */
    public void FirstHandler(CommandMsg command, ConnectionHandler client){
        Display(command);
        ArrayList<Integer> output = new ArrayList<>();
        String line = scanner.nextLine().trim();
        while(!line.equalsIgnoreCase("2") && !line.equalsIgnoreCase("3")) {
            System.out.println("Invalid input, required '2' or '3', retry");
            line = scanner.nextLine().trim();
        }
        output.add(Integer.parseInt(line));
        client.WriteMessage(new ServerMsg(output));
    }

    /**
     * Handler for CommandType family Number
     * @param command CommandMsg send by the Server
     * @param client reference to the ConnectionHandler
     */
    public void NumberHandler(CommandMsg command, ConnectionHandler client){
        Display(command);
        ArrayList<Integer> output = new ArrayList<>();
        boolean found = false;
        int result;
        String line = scanner.nextLine().trim();
        while (!found){
            if(line.matches("\\d+")){
                result = Integer.parseInt(line);
                if(result > -1 && result < command.getInfo().getGods().length){
                    output.add(command.getInfo().getGods()[result].getPosition());
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
    }

    /**
     * Handler for CommandType family Pose
     * @param command CommandMsg send by the Server
     * @param client reference to the ConnectionHandler
     */
    public void PoseHandler(CommandMsg command, ConnectionHandler client){
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
    }

    /**
     * Handler for CommandType family Answer
     * @param command CommandMsg send by the Server
     * @param client reference to the ConnectionHandler
     */
    public void AnswerHandler(CommandMsg command, ConnectionHandler client){
        Display(command);
        String msg = scanner.nextLine().trim();
        while ((!msg.equalsIgnoreCase("yes") && !msg.equalsIgnoreCase("no"))){
            System.out.println("Invalid input, required 'yes' or 'no', retry");
            msg = scanner.nextLine().trim();
        }
        client.WriteMessage(new ServerMsg(msg));
    }

    /**
     * Handler for CommandType family God
     * @param command CommandMsg send by the Server
     * @param client reference to the ConnectionHandler
     */
    public void GodHandler(CommandMsg command, ConnectionHandler client){
        Display(command);
        ArrayList<Integer> index = new ArrayList<>();
        int number;
        String line;
        while (index.size() < command.getInfo().getPlayers().length){
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
    }

    /**
     * Handler for CommandType family Update
     * @param command CommandMsg send by the Server
     */
    public void UpdateHandler(CommandMsg command){
        map = command.getInfo().getGrid();
        ShowMap();
        Display(command);
    }

    /**
     * Handler for CommandType family Close
     * @param command CommandMsg send by the Server
     * @param client reference to the ConnectionHandler
     */
    public void CloseHandler(CommandMsg command, ConnectionHandler client){
        Display(command);
        client.setActive(false);
    }

    /**
     * Handler for CommandType family Communication
     * @param command CommandMsg send by the Server
     */
    public void CommunicationHandler(CommandMsg command){
        switch (command.getCommandType()){
            case COM_WELCOME:
                WelcomeScreen();
                gods = command.getInfo().getGods();
                players = command.getInfo().getPlayers();
                break;
            case COM_RESTART:
                gods = command.getInfo().getGods();
                players = command.getInfo().getPlayers();
                break;
            case COM_GODS:
                GodInGame(command.getInfo().getGods());
                break;
            case COM_CHOSEN:
                PlayerGod(command.getInfo().getPlayers());
                for (PlayerInfo user : players) {
                    System.out.println("Player: " + user.getColor() + user.getName() + Colors.RESET + " GodCard: " + gods[user.getGod()].getName());
                }
                break;
            case COM_WAIT_CHOICE:
                System.out.println("Waiting the other player's choice");
                break;
            case COM_WAIT_LOBBY:
                System.out.println("Waiting the other players in the Lobby");
                break;
            case COM_INVALID_WORKER:
                System.out.println("Not a valid worker, retry");
                break;
            case COM_INVALID_POS:
                System.out.println("Not a valid destination, retry");
                break;
            case COM_LOSE:
                System.out.println("You lose, Dumbos");
                break;
        }
    }

    /**
     * Displays the corresponding messages of each message
     * @param command CommandMsg send by the Server
     */
    public void Display(CommandMsg command){
        switch (command.getCommandType()){
            case NAME:
                System.out.println("Insert your NickName");
                break;
            case FIRST:
                System.out.println("You are the Challenger!\nTell me the Number of Players of this Match");
                break;
            case POS_INITIAL:
                System.out.println("Where do you want to place your Worker?");
                break;
            case POS_WORKER:
                System.out.println("Select one of your Workers");
                break;
            case POS_MOVE:
                System.out.println("Where do you want to Move?");
                break;
            case POS_BUILD:
                System.out.println("Where do you want to Build?");
                break;
            case NUMBER:
                for(int i = 0; i < command.getInfo().getGods().length; i++){
                    System.out.println("Insert " + i + " to get " + command.getInfo().getGods()[i].getName());
                }
                break;
            case ANS_POWER:
                System.out.println("Would you like to use your divinity Power? \nyes / no");
                break;
            case ANS_RESTART:
                System.out.println("Would you like to resume your previous Game? \nyes / no");
                break;
            case GOD:
                System.out.println("These are the gods available :");
                for(GodInfo god : gods){
                    System.out.println("[" + god.getPosition() + "] " + god.getName() + " - " + god.getPower());
                }
                System.out.println("Please " + players[0].getColor() + players[0].getName() + Colors.RESET
                        + " Select " + players.length + " GodCards indicating their numbers");
                break;
            case CLOSE_ANOMALOUS:
                PlayerInfo player = command.getInfo().getPlayers()[0];
                System.out.println(player.getColor() + player.getName() + Colors.RESET + " disconnected\nEndGame");
                break;
            case CLOSE_NORMAL:
                PlayerInfo winner = command.getInfo().getPlayers()[0];
                if (nickname.equalsIgnoreCase(winner.getName())) {
                    System.out.println("You have won\nCongratulations");
                } else {
                    System.out.println(winner.getColor() + winner.getName() + Colors.RESET + " has won!\nEverybody clap your hands!");
                }
                break;
            case CLOSE_RESTART:
                System.out.println("A game is already started, try later");
                break;
            case CLOSE_SERVER:
                System.out.println("Server is down");
                break;
            case UPDATE_ACTION:
                if(command.getInfo().getPlayers()!=null){
                    PlayerInfo user = command.getInfo().getPlayers()[0];
                    if(!user.getName().equalsIgnoreCase(nickname)){
                        System.out.println("Look at " + user.getColor() + user.getName() + Colors.RESET + "'s move");
                    }
                }
                break;
            case UPDATE_TURN:
                PlayerInfo actual = command.getInfo().getPlayers()[0];
                if(actual.getName().equalsIgnoreCase(nickname)){
                    System.out.println(actual.getColor() + actual.getName() + Colors.RESET + " Turn Start!");
                }
                else {
                    System.out.println(actual.getColor() + actual.getName() + Colors.RESET + " is playing\nWait your Turn!");
                }
                break;
        }
    }

    /**
     * Print the game's map
     */
    public void ShowMap(){
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
    }

    /**
     * Updates the god list, marking the active cards
     * @param godInfo Array of GodInfo
     */
    public void GodInGame(GodInfo[] godInfo){
        GodInfo[] update = new GodInfo[14];
        ArrayList<Integer> list = new ArrayList<>();
        for(GodInfo info : godInfo){
            list.add(info.getPosition());
        }
        for(GodInfo god : gods){
            if(list.contains(god.getPosition())){
                update[god.getPosition()] = new GodInfo(god.getPosition(), god.getName(), god.getPower(), true);
            }
            else{
                update[god.getPosition()] = new GodInfo(god.getPosition(), god.getName(), god.getPower(), false);
            }
        }
        gods = update;
    }

    /**
     * Updates the player list, inserting the respective god
     * @param playerInfo Array of PlayerInfo
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