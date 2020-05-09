package VirtualView;

import ComunicationProtocol.*;
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
     * @param gods
     * @param players
     */
    public void WelcomePacket(GodInfo[] gods, PlayerInfo[] players) {
        for(ClientHandler handler : Game.getInstance().getController().getHandlers()) {
            handler.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, SubCommandType.WELCOME, null, gods, players, null));
        }
    }

    /**
     * A player's starting his turn, others are waiting
     */
    public void TurnStartMessage(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(myGame.getPlayer().indexOf(myGame.getActualPlayer()));
        list.add(1);
        BoxInfo[][] map = MapInfo(true, false);
        Echo(new CliCommandMsg(CommandType.UPDATE, SubCommandType.DEFAULT, map, null, null, list));
    }

    /**
     * Choosing gods phase
     * @param challenger
     */
    public void ChooseGodPhase(ClientHandler challenger){
        ArrayList<Integer> output = new ArrayList<>();
        int numOfPlayers = myGame.getController().getPlayerNum();
        ClientHandler handler;
        output.add(numOfPlayers);
        challenger.WriteMessage(new CliCommandMsg(CommandType.GOD, SubCommandType.DEFAULT, null, null, null, output));
        ArrayList<Integer> index = challenger.ReadMessage().getList();
        challenger.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, SubCommandType.WAIT, null, null, null, null));
        myGame.ExtractCard(index);
        for(ClientHandler user : myGame.getController().getHandlers()){
            user.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, SubCommandType.IN_GAME_GOD, null, null, null, index));
        }
        for (int i = 0; i < numOfPlayers; i++){
            if (i == numOfPlayers - 1){
                handler = myGame.getController().getHandlers().get(0);
            }
            else {
                handler = myGame.getController().getHandlers().get(i + 1);
            }
            int godCard = myGame.getActiveCards().indexOf(myGame.getDeck().getCardList().get(AskGod(handler)));
            handler.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, SubCommandType.WAIT, null, null, null, null));
            handler.getPlayer().ChooseGod(godCard);
        }
        InGameGods();
    }

    /**
     * Print the picked gods
     */
    public void InGameGods() {
        PlayerInfo[] update = new PlayerInfo[myGame.getController().getPlayerNum()];
        for (Player user : myGame.getPlayer()) {
            update[myGame.getPlayer().indexOf(user)] = new PlayerInfo(myGame.getPlayer().indexOf(user), user.getNickName(), user.getColor(), myGame.getDeck().getCardList().indexOf(user.getCard()));
        }
        for(ClientHandler player : myGame.getController().getHandlers()) {
            player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, SubCommandType.PLAYER_GOD, null, null, update, null));
        }
    }

    /**
     * Ask for some Gods
     * @param player
     * @return
     */
    public int AskGod(ClientHandler player){
        ArrayList<Integer> list = new ArrayList<>();
        for(GodCard card : myGame.getActiveCards()){
            if(!card.isPicked()){
                list.add(myGame.getDeck().getCardList().indexOf(card));
            }
        }
        player.WriteMessage(new CliCommandMsg(CommandType.NUMBER, SubCommandType.DEFAULT, null, null, null, list));
        int index = player.ReadMessage().getList().get(0);
        return index;
    }

    /**
     * Request of a box through coordinates
     * @param player
     * @param type
     * @return asked box or error message
     */
    public Box AskCoordinates(ClientHandler player, CoordinateType type) {
        ServerMsg answer;
        ArrayList<Integer> info = new ArrayList<>();
        switch (type){
            case INITIAL:
                info.add(0);
                break;
            case WORKER:
                info.add(1);
                break;
            case MOVE:
                info.add(2);
                break;
            case BUILD:
                info.add(3);
                break;
        }
        player.WriteMessage(new CliCommandMsg(CommandType.COORDINATES, SubCommandType.DEFAULT, null, null, null, info));
        answer = player.ReadMessage();
        return myMap.getBox(answer.getList().get(0), answer.getList().get(1));
    }

    /**
     * Ask if the player would like to use his god power
     * @param player
     * @return
     */
    public boolean AskPower(ClientHandler player) {
        ServerMsg answer;
        player.WriteMessage(new CliCommandMsg(CommandType.ANSWER, SubCommandType.DEFAULT, null, null, null, null));
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
    public Worker AskWorker(ClientHandler player) {
        Box box = AskCoordinates(player, CoordinateType.WORKER);
        Worker candidate = myGame.getActualPlayer().selectWorker(box);
        if (candidate == null) {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(1);
            player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, SubCommandType.NOT_VALID, null, null, null, list));
            return AskWorker(player);
        }
        return candidate;
    }

    /**
     * Error message selecting a box destination
     * @param player
     */
    public void NotValidDest(ClientHandler player) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);
        player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, SubCommandType.NOT_VALID, null, null, null, list));
    }

    /**
     * Print message of loosing game
     * @param player
     */
    public void Lose(ClientHandler player){
        player.WriteMessage(new CliCommandMsg(CommandType.COMMUNICATION, SubCommandType.LOSE, null, null, null, null));
    }

    /**
     * Print message of victory for the winner
     * @param player
     */
    public void GameFinished(ClientHandler player){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(myGame.getPlayer().indexOf(myGame.getWinner()));
        player.WriteMessage(new CliCommandMsg(CommandType.CLOSE, SubCommandType.DEFAULT, null, null, null, list));
    }

    /**
     * Update the game map after a generic move
     * @param player
     * @param generic
     * @param phase
     */
    public void UpdateMap(ClientHandler player, boolean generic, boolean phase){
        player.WriteMessage(new CliCommandMsg(CommandType.UPDATE, SubCommandType.DEFAULT, MapInfo(generic, phase), null, null, null));
    }

    /**
     * Send an update message
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
     * @return
     */
    public BoxInfo[][] MapInfo(boolean generic, boolean phase){//phase == false --> movement
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
        return map;
    }
}