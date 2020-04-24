package VirtualView;

import ComunicationProtocol.BoxInfo;
import ComunicationProtocol.CliCommandMsg;
import ComunicationProtocol.CommandType;
import ComunicationProtocol.ServerMsg;
import Model.*;
import Model.Godcards.GodCard;
import Server.ClientHandler;
import View.Colors;
import java.util.ArrayList;

public class VirtualView {
    private Game myGame;
    private PlayGround myMap;

    public VirtualView(Game game){
        this.myGame = game;
        this.myMap = PlayGround.getInstance();
    }

    public void CliWelcomeScreen(ClientHandler player) {
        ArrayList<String> output = new ArrayList<>();
        output.add("\n"+ Colors.PURPLE + "╭╮╭╮╭╮" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "╭╮");
        output.add("┃┃┃┃┃┃" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃┃");
        output.add("┃┃┃┃┃┣━━┫┃╭━━┳━━┳╮╭┳━━╮\n┃╰╯╰╯┃┃━┫┃┃╭━┫╭╮┃╰╯┃┃━┫\n╰╮╭╮╭┫┃━┫╰┫╰━┫╰╯┃┃┃┃┃━┫");
        output.add(Colors.YELLOW + "╱" + Colors.PURPLE + "╰╯╰╯╰━━┻━┻━━┻━━┻┻┻┻━━╯");
        output.add("╭━━━━╮" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "╭━━━╮" + Colors.YELLOW + "╱╱╱╱╱" + Colors.PURPLE + "╭╮");
        output.add("┃╭╮╭╮┃" + Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃╭━╮┃" + Colors.YELLOW + "╱╱╱╱" + Colors.PURPLE + "╭╯╰╮");
        output.add("╰╯┃┃┣┻━╮┃╰━━┳━━┳━╋╮╭╋━━┳━┳┳━╮╭╮");
        output.add(Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃┃┃╭╮┃╰━━╮┃╭╮┃╭╮┫┃┃╭╮┃╭╋┫╭╮╋┫");
        output.add(Colors.YELLOW + "╱╱" + Colors.PURPLE + "┃┃┃╰╯┃┃╰━╯┃╭╮┃┃┃┃╰┫╰╯┃┃┃┃┃┃┃┃");
        output.add(Colors.YELLOW + "╱╱" + Colors.PURPLE + "╰╯╰━━╯╰━━━┻╯╰┻╯╰┻━┻━━┻╯╰┻╯╰┻╯" + Colors.RESET + "\n");
        player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, output));
    } //ready

    public void CliGodList(ClientHandler player) {
        ArrayList<String> output = new ArrayList<>();
        int i = 0;
        output.add("These are the GodCards available :\n");
        for (GodCard god : myGame.getDeck().getCardList()) {
            output.add("[" + i + "] " + god.getName() + " - " + god.getPower());
            i++;
        }
        player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, output));
    } //ready

    public void CliInGameGods() {
        ArrayList<String> output = new ArrayList<>();
        output.add("\n");
        for (Player user : myGame.getPlayer()) {
            output.add("Player: " + user.getColor() + user.getNickName() + Colors.RESET + " GodCard: " + user.getCard().getName());
        }
        for(ClientHandler player : myGame.getController().getHandlers()) {
            player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, output));
        }
    } //ready

    //methods MessageHandlerCLI

    public void CliChooseGodPhase(ClientHandler challenger){
        CliGodList(challenger);
        ClientHandler handler;
        ArrayList<String> output = new ArrayList<>();
        Player player;
        int godCard;
        int numOfPlayers = myGame.getController().getPlayerNum();
        challenger.WriteMessage(new CliCommandMsg(CommandType.GOD, "\nPlease " + myGame.getPlayer().get(0).getColor() + myGame.getPlayer().get(0).getNickName() + Colors.RESET
                + " Select " + numOfPlayers + " GodCards indicating their numbers", numOfPlayers));
        ArrayList<Integer> index = challenger.ReadMessage().getList();
        myGame.ExtractCard(index);
        for (int i = 0; i < numOfPlayers; i++){
            if (i == numOfPlayers - 1){
                player = myGame.getPlayer().get(0);
                handler = myGame.getController().getHandlers().get(0);
            }
            else {
                player = myGame.getPlayer().get(i + 1);
                handler = myGame.getController().getHandlers().get(i + 1);
            }
            output.add(player.getColor() +  player.getNickName() +  Colors.RESET + " Pick a card");
            int j=0;
            for(GodCard card : Game.getInstance().getActiveCards()){
                if (!card.isPicked()){
                    output.add("Insert " + j + " to get " + card.getName());
                }
                j++;
            }
            godCard = CliAskGod(handler, output);
            player.ChooseGod(godCard);
            output = new ArrayList<>();
        }
        CliInGameGods();
    }

    public boolean CliLegalCoordinates(int i, int j) {
        int size = myMap.getSIZE();
        if (i < 0 || j < 0 || i > size - 1 || j > size - 1) {
            return false;
        }
        return true;
    }

    public Box CliAskForCoordinates(ClientHandler player, String msg) {
        ServerMsg answer;
        while (true) {
            player.WriteMessage(new CliCommandMsg(CommandType.COORDINATES, msg));
            answer = player.ReadMessage();
            if (CliLegalCoordinates(answer.getList().get(0), answer.getList().get(1))) {
                return myMap.getBox(answer.getList().get(0), answer.getList().get(1));
            }
            CliNotValidDestination(player);
        }
    }

    public boolean CliAskForPower(ClientHandler player) {
        ServerMsg answer;
        player.WriteMessage(new CliCommandMsg(CommandType.ANSWER, "Would you like to use your divinity Power? \nyes / no"));
        answer = player.ReadMessage();
        if (answer.getMsg().equalsIgnoreCase("yes")) {
            return true;
        } else {
            return false;
        }
    }

    public Worker CliAskForWorker(ClientHandler player) {
        Box box = CliAskForCoordinates(player,"Select one of your workers");
        Worker candidate = myGame.getActualPlayer().selectWorker(box);
        if (candidate == null) {
            player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, "Not a valid worker"));
            return CliAskForWorker(player);
        }
        return candidate;
    }

    public Box CliAskForMovement(ClientHandler player) {
        return CliAskForCoordinates(player, "Where do you want to move your worker?");
    }

    public Box CliAskForBuilding(ClientHandler player) {
        return CliAskForCoordinates(player, "Where do you want to build?");
    }

    public void CliNotValidDestination(ClientHandler player) {
        player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, "Not a valid destination, try again!"));
    }

    public Box CliAskForPlacement(ClientHandler player, int workerNumber) {
        return CliAskForCoordinates(player, "Where do you want to place Worker number " + workerNumber + "?");
    }

    public void CliLose(ClientHandler player){
        player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, "You Lost! Dumbo's"));
    }

    public void CliGameFinished(ClientHandler player){
        Player winner = myGame.getWinner();
        if(player == myGame.getController().getHandlers().get(myGame.getPlayer().indexOf(winner))){
            player.WriteMessage(new CliCommandMsg(CommandType.CLOSE, "You have won\nCongratulations"));
        }
        else{
            player.WriteMessage(new CliCommandMsg(CommandType.CLOSE, winner.getColor() + winner.getNickName() + Colors.RESET + " has won !\nEverybody clap your hands!"));
        }
    }

    public int CliAskGod(ClientHandler player, ArrayList<String> msg){
        player.WriteMessage(new CliCommandMsg(CommandType.NUMBER, msg));
        int index = player.ReadMessage().getList().get(0);
        if(index < 0 || index >= myGame.getController().getPlayerNum() || myGame.getActiveCards().get(index).isPicked()){
            ArrayList<String> retry = new ArrayList<>();
            retry.add("Index not valid, retry");
            CliAskGod(player, retry);
        }
        return index;
    }

    public void CliTurnStartMessage(){
        Player actual = myGame.getActualPlayer();
        ClientHandler handler = myGame.getController().getHandlers().get(myGame.getPlayer().indexOf(actual));
        CliCommandMsg msg1 = MapInfo(true, false, "\n" + actual.getColor() + actual.getNickName() + Colors.RESET + " Turn Start!");
        CliCommandMsg msg2 = MapInfo(true, false,  actual.getColor() + actual.getNickName() + Colors.RESET + " is playing\nWait your Turn!!");
        Echo(handler, msg1, msg2);
    }

    public void UpdateMap(ClientHandler player, boolean generic, boolean phase){
        player.WriteMessage(MapInfo(generic, phase, ""));
    }

    public void Echo(CliCommandMsg msg){
        for (ClientHandler handler : myGame.getController().getHandlers()){
            handler.WriteMessage(msg);
        }
    }

    public void Echo(ClientHandler actual, CliCommandMsg msgActual, CliCommandMsg msg){
        for (ClientHandler handler : myGame.getController().getHandlers()){
            if(handler == actual){
                handler.WriteMessage(msgActual);
            }
            else {
                handler.WriteMessage(msg);
            }
        }
    }

    public CliCommandMsg MapInfo(boolean generic, boolean phase, String msg){//phase == false --> movement
        Box box;
        boolean legal;
        Worker actualWorker;
        BoxInfo[][] map = new BoxInfo[myMap.getSIZE()][myMap.getSIZE()];
        for (int i = 0; i < myMap.getSIZE(); i++){
            for (int j = 0; j < myMap.getSIZE(); j++){
                box = myMap.getBox(i, j);
                if (generic) {
                    if(box.isOccupied()){
                        map[i][j] = new BoxInfo(box.getStructure().size(), box.getUpperLevel(), ((Worker)box.getStructure().get(box.getStructure().size()-1)).getOwner().getColor(), Colors.RESET);
                    }
                    else{
                        map[i][j] = new BoxInfo(box.getStructure().size(), box.getUpperLevel(), Colors.RESET, Colors.RESET);
                    }
                }
                else{
                    actualWorker = myGame.getActualPlayer().getSelectedWorker();
                    if (phase){
                        legal = actualWorker.LegalBuild(box);
                    }
                    else {
                        legal = actualWorker.LegalMovement(box);
                    }
                    if(legal){
                        if(box.isOccupied()){
                            map[i][j] = new BoxInfo(box.getStructure().size(), box.getUpperLevel(), ((Worker)box.getStructure().get(box.getStructure().size()-1)).getOwner().getColor(), actualWorker.getOwner().getColor());
                        }
                        else{
                            map[i][j] = new BoxInfo(box.getStructure().size(), box.getUpperLevel(), Colors.RESET, actualWorker.getOwner().getColor());
                        }
                    }
                    else {
                        if(box.isOccupied()){
                            map[i][j] = new BoxInfo(box.getStructure().size(), box.getUpperLevel(), ((Worker)box.getStructure().get(box.getStructure().size()-1)).getOwner().getColor(), Colors.RESET);
                        }
                        else{
                            map[i][j] = new BoxInfo(box.getStructure().size(), box.getUpperLevel(), Colors.RESET, Colors.RESET);
                        }
                    }
                }
            }
        }
        return new CliCommandMsg(CommandType.UPDATE, map, msg);
    }
}