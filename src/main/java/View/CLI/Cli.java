package View.CLI;

import Client.ConnectionHandler;
import ComunicationProtocol.BoxInfo;
import ComunicationProtocol.CliCommandMsg;
import ComunicationProtocol.ServerMsg;
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
        Display(command.getMsg());
        String msg = scanner.nextLine().trim();
        while(msg.equalsIgnoreCase("")){
            System.out.println("Insert a non-void name, retry");
            msg = scanner.nextLine().trim();
        }
        client.WriteMessage(new ServerMsg(msg, client.Layout()));
    }

    /**
     * Handler for CommandType First
     * @param command
     * @param client
     */
    public void FirstHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
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
     * Handler for CommandType Number
     * @param command
     * @param client
     */
    public void NumberHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        ArrayList<Integer> output = new ArrayList<>();
        String line = scanner.nextLine().trim();
        boolean found = false;
        int result = 0;
        while (!found){
            if(line.matches("\\d+")){
                result = Integer.parseInt(line);
                if(result > -1 && result < command.getInfo()){
                    output.add(result);
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
     * Handler for CommandType Coordinates
     * @param command
     * @param client
     */
    public void CoordinatesHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
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
     * Handler for CommandType Answer
     * @param command
     * @param client
     */
    public void AnswerHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        String msg = scanner.nextLine().trim();
        while ((!msg.equalsIgnoreCase("yes") && !msg.equalsIgnoreCase("no"))){
            System.out.println("Invalid input, required 'yes' or 'no', retry");
            msg = scanner.nextLine().trim();
        }
        client.WriteMessage(new ServerMsg(msg));
    }

    /**
     * Handler for CommandType God
     * @param command
     * @param client
     */
    public void GodHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        ArrayList<Integer> index = new ArrayList<>();
        int number;
        String line;
        while (index.size() < command.getInfo()){
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
     * Print a communication message
     * @param command
     */
    public void CommunicationHandler(CliCommandMsg command){
        Display(command.getMsg());
    }

    /**
     * Print a updating message
     * @param command
     */
    public void UpdateHandler(CliCommandMsg command){
        map = command.getMap();
        ShowMap(map);
        Display(command.getMsg());
    }

    /**
     * Closing a ClientHandler...
     * @param command
     * @param client
     */
    public void CloseHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        client.setActive(false);
    }

    /**
     * Print the msg messages
     * @param msg
     */
    public void Display(ArrayList<String> msg){
        for(String str : msg){
            System.out.println(str);
        }
    }

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
    }
}