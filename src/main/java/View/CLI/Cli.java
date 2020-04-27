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
        String msg = scanner.nextLine();
        client.WriteMessage(new ServerMsg(msg, client.Layout()));
    }

    /**
     * Handler for CommandType Number
     * @param command
     * @param client
     */
    public void NumberHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        ArrayList<Integer> output = new ArrayList<>();
        output.add(scanner.nextInt());
        client.WriteMessage(new ServerMsg(output));
    }

    /**
     * Handler for CommandType Coordinates
     * @param command
     * @param client
     */
    public void CoordinatesHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        ArrayList<Integer> coord = new ArrayList<>();
        coord.add(scanner.nextInt());
        coord.add(scanner.nextInt());
        client.WriteMessage(new ServerMsg(coord));
    }

    /**
     * Handler for CommandType Answer
     * @param command
     * @param client
     */
    public void AnswerHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        String msg = null;
        msg = scanner.nextLine();
        while (msg == null || (!msg.equalsIgnoreCase("yes") && !msg.equalsIgnoreCase("no"))){
            msg = scanner.nextLine();
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
        while (index.size() < command.getInfo()){
            number = scanner.nextInt();
            if (number > -1 && number < 14){
                if (!index.contains(number)){
                    index.add(number);
                    System.out.println("God n°" + number + " added");
                }
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