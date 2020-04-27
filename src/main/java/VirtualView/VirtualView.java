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

/**
 * VirtualView class of the game
 */
public class VirtualView {
    private Game myGame;
    private PlayGround myMap;

    /**
     * class' constructor
     * @param game
     */
    public VirtualView(Game game){
        this.myGame = game;
        this.myMap = PlayGround.getInstance();
    }

    /**
     * Print a welcome screen
     * @param player
     */
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

    /**
     * Print a list of available gods for the game
     * @param player
     */
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

    /**
     * Print the picked gods
     */
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

    /**
     * Choosing gods phase
     * @param challenger
     */
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

    /**
     * Tell if it's a legal box for the game
     * @param i
     * @param j
     * @return
     */
    public boolean CliLegalCoordinates(int i, int j) {
        int size = myMap.getSIZE();
        if (i < 0 || j < 0 || i > size - 1 || j > size - 1) {
            return false;
        }
        return true;
    }

    /**
     * Request of a box through coordinates
     * @param player
     * @param msg
     * @return asked box or error message
     */
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

    /**
     * Ask if the player would like to use his god power
     * @param player
     * @return
     */
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

    /**
     * Player must select one of his worker
     * @param player
     * @return selected worker or error message
     */
    public Worker CliAskForWorker(ClientHandler player) {
        Box box = CliAskForCoordinates(player,"Select one of your workers");
        Worker candidate = myGame.getActualPlayer().selectWorker(box);
        if (candidate == null) {
            player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, "Not a valid worker"));
            return CliAskForWorker(player);
        }
        return candidate;
    }

    /**
     * Ask for the move phase
     * @param player
     * @return
     */
    public Box CliAskForMovement(ClientHandler player) {
        return CliAskForCoordinates(player, "Where do you want to move your worker?");
    }

    /**
     * Ask for the build phase
     * @param player
     * @return
     */
    public Box CliAskForBuilding(ClientHandler player) {
        return CliAskForCoordinates(player, "Where do you want to build?");
    }

    /**
     * Error message selecting a box destination
     * @param player
     */
    public void CliNotValidDestination(ClientHandler player) {
        player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, "Not a valid destination, try again!"));
    }

    /**
     * Ask for a destination box for your selected worker
     * @param player
     * @param workerNumber
     * @return
     */
    public Box CliAskForPlacement(ClientHandler player, int workerNumber) {
        return CliAskForCoordinates(player, "Where do you want to place Worker number " + workerNumber + "?");
    }

    /**
     * Print message of loosing game
     * @param player
     */
    public void CliLose(ClientHandler player){
        player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, "You Lost! Dumbo's"));
    }

    /**
     * Print message of victory for the winner
     * @param player
     */
    public void CliGameFinished(ClientHandler player){
        Player winner = myGame.getWinner();
        if(player == myGame.getController().getHandlers().get(myGame.getPlayer().indexOf(winner))){
            player.WriteMessage(new CliCommandMsg(CommandType.CLOSE, "You have won\nCongratulations"));
        }
        else{
            player.WriteMessage(new CliCommandMsg(CommandType.CLOSE, winner.getColor() + winner.getNickName() + Colors.RESET + " has won !\nEverybody clap your hands!"));
        }
    }

    /**
     * Ask for some Gods
     * @param player
     * @param msg
     * @return
     */
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

    /**
     * A player's starting his turn, others are waiting
     */
    public void CliTurnStartMessage(){
        Player actual = myGame.getActualPlayer();
        ClientHandler handler = myGame.getController().getHandlers().get(myGame.getPlayer().indexOf(actual));
        CliCommandMsg msg1 = MapInfo(true, false, "\n" + actual.getColor() + actual.getNickName() + Colors.RESET + " Turn Start!");
        CliCommandMsg msg2 = MapInfo(true, false,  actual.getColor() + actual.getNickName() + Colors.RESET + " is playing\nWait your Turn!!");
        Echo(handler, msg1, msg2);
    }

    /**
     * Update the game map after a generic move
     * @param player
     * @param generic
     * @param phase
     */
    public void UpdateMap(ClientHandler player, boolean generic, boolean phase){
        player.WriteMessage(MapInfo(generic, phase, ""));
    }

    /**
     * Send an update message for all the players
     * @param msg
     */
    public void Echo(CliCommandMsg msg){
        for (ClientHandler handler : myGame.getController().getHandlers()){
            handler.WriteMessage(msg);
        }
    }

    /**
     * Send an update message, if player's actual send another one
     * @param actual
     * @param msgActual
     * @param msg
     */
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

    /**
     * Info of the map updated after a move
     * @param generic
     * @param phase
     * @param msg
     * @return
     */
    public CliCommandMsg MapInfo(boolean generic, boolean phase, String msg){//phase == false --> movement
        //generic=true mappa di inizio turno, se false colora
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