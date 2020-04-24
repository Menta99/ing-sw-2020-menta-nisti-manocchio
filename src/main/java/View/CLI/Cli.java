package View.CLI;

import Client.ConnectionHandler;
import ComunicationProtocol.BoxInfo;
import ComunicationProtocol.CliCommandMsg;
import ComunicationProtocol.ServerMsg;
import Model.PawnType;
import View.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Cli extends View {
    private Scanner scanner;
    private BoxInfo[][] map;

    public Cli(){
        this.scanner = new Scanner(System.in);
    }

    public void NameHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        String msg = scanner.nextLine();
        client.WriteMessage(new ServerMsg(msg, client.Layout()));
    }

    public void NumberHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        ArrayList<Integer> output = new ArrayList<>();
        output.add(scanner.nextInt());
        client.WriteMessage(new ServerMsg(output));
    }

    public void CoordinatesHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        ArrayList<Integer> coord = new ArrayList<>();
        coord.add(scanner.nextInt());
        coord.add(scanner.nextInt());
        client.WriteMessage(new ServerMsg(coord));
    }

    public void AnswerHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        String msg = null;
        msg = scanner.nextLine();
        while (msg == null || (!msg.equalsIgnoreCase("yes") && !msg.equalsIgnoreCase("no"))){
            msg = scanner.nextLine();
        }
        client.WriteMessage(new ServerMsg(msg));
    }

    public void GodHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        ArrayList<Integer> index = new ArrayList<>();
        int number;
        while (index.size() < command.getInfo()){
            number = scanner.nextInt();
            if (number > -1 && number < 15){
                if (!index.contains(number)){
                    index.add(number);
                    System.out.println("God n°" + number + " added");
                }
            }
        }
        client.WriteMessage(new ServerMsg(index));
    }

    public void CommunicationHandler(CliCommandMsg command){
        Display(command.getMsg());
    }

    public void UpdateHandler(CliCommandMsg command){
        map = command.getMap();
        ShowMap(map);
        Display(command.getMsg());
    }

    public void CloseHandler(CliCommandMsg command, ConnectionHandler client){
        Display(command.getMsg());
        client.setActive(false);
    }

    public void Display(ArrayList<String> msg){
        for(String str : msg){
            System.out.println(str);
        }
    }

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