package VirtualView;

import CommunicationProtocol.CommandMsg;
import CommunicationProtocol.CommandType;
import CommunicationProtocol.SantoriniInfo.BoxInfo;
import CommunicationProtocol.SantoriniInfo.GodInfo;
import CommunicationProtocol.SantoriniInfo.Info;
import CommunicationProtocol.SantoriniInfo.PlayerInfo;
import CommunicationProtocol.ServerMsg;
import Model.*;
import Model.Godcards.GodCard;
import Server.ClientHandler;
import View.Colors;

import java.util.ArrayList;

/**
 * VirtualView class of the game
 */
public class VirtualView {
    private final Game myGame;
    private final PlayGround myMap;

    /**
     * Constructor of the Class
     * @param game instance of the Game
     */
    public VirtualView(Game game){
        this.myGame = game;
        this.myMap = PlayGround.getInstance();
    }

    /**
     * Send the Welcome Packet
     * @param type true if it's a new Game, false if it's a restarted one
     */
    public void WelcomePacket(boolean type) {
        GodInfo[] gods = new GodInfo[14];
        PlayerInfo[] players = new PlayerInfo[myGame.getPlayer().size()];
        ArrayList<GodCard> deck = myGame.getDeck().getCardList();
        ArrayList<Player> list = myGame.getPlayer();
        for (GodCard card : deck) {
            gods[deck.indexOf(card)] = new GodInfo(deck.indexOf(card), card.getName(), card.getPower(), card.isPicked());
        }
        if(type) {
            for (Player player : list) {
                players[list.indexOf(player)] = new PlayerInfo(list.indexOf(player), player.getNickName(), player.getColor(), -1);
            }
            Echo(new CommandMsg(CommandType.COM_WELCOME, new Info(null, gods, players)));
        }
        else{
            for (Player player : list) {
                players[list.indexOf(player)] = new PlayerInfo(list.indexOf(player), player.getNickName(), player.getColor(), deck.indexOf(player.getCard()));
            }
            Echo(new CommandMsg(CommandType.COM_RESTART, new Info(null, gods, players)));
        }
    }

    /**
     * Send to all the player an Update message in the start of a Turn
     */
    public void TurnStartMessage(){
        Info info = new Info(MapInfo(true, false), new PlayerInfo(myGame.getActualPlayer()));
        Echo(new CommandMsg(CommandType.UPDATE_TURN, info));
    }

    /**
     * Choosing gods phase, in which every player chose his god
     * @param challenger ClientHandler of the Challenger
     */
    public void ChooseGodPhase(ClientHandler challenger){
        int numOfPlayers = myGame.getController().getPlayerNum();
        ClientHandler handler;
        DrawGods(challenger);
        for (int i = 0; i < numOfPlayers; i++){
            if (i == numOfPlayers - 1){
                handler = myGame.getController().getHandlers().get(0);
            }
            else {
                handler = myGame.getController().getHandlers().get(i + 1);
            }
            int godCard = myGame.getActiveCards().indexOf(myGame.getDeck().getCardList().get(AskGod(handler)));
            handler.WriteMessage(new CommandMsg(CommandType.COM_WAIT_CHOICE, null));
            handler.getPlayer().ChooseGod(godCard);
        }
        InGameGods();
    }

    /**
     * Draw God Phase, in which the challenger chose the gods in game
     * Then are sent to all the players the information of the card in game
     * @param challenger ClientHandler of the Challenger
     */
    public void DrawGods(ClientHandler challenger){
        ArrayList<ClientHandler> handlers = myGame.getController().getHandlers();
        PlayerInfo[] players = new PlayerInfo[handlers.size()];
        for(ClientHandler player : handlers){
            players[handlers.indexOf(player)] = new PlayerInfo(player);
        }
        challenger.WriteMessage(new CommandMsg(CommandType.GOD, new Info(players)));
        ArrayList<Integer> index = challenger.ReadMessage().getList();
        challenger.WriteMessage(new CommandMsg(CommandType.COM_WAIT_CHOICE, null));
        myGame.ExtractCard(index);
        GodInfo[] gods = new GodInfo[myGame.getActiveCards().size()];
        for(GodCard card : myGame.getActiveCards()){
            gods[myGame.getActiveCards().indexOf(card)] = new GodInfo(myGame.getDeck().getCardList().indexOf(card), null, null, true);
        }
        Echo(new CommandMsg(CommandType.COM_GODS, new Info(gods)));
    }

    /**
     * Inform all the players of the gods selected by each player
     */
    public void InGameGods() {
        PlayerInfo[] update = new PlayerInfo[myGame.getController().getPlayerNum()];
        for (Player user : myGame.getPlayer()) {
            update[myGame.getPlayer().indexOf(user)] = new PlayerInfo(user);
        }
        Echo(new CommandMsg(CommandType.COM_CHOSEN, new Info(update)));
    }

    /**
     * Ask to the Player to che his god
     * @param player ClientHandler of the Player that has to chose
     * @return the index of the GodCard
     */
    public int AskGod(ClientHandler player){
        int i = 0;
        for(GodCard card : myGame.getActiveCards()){
            if(!card.isPicked()){
                i++;
            }
        }
        GodInfo[] gods = new GodInfo[i];
        i = 0;
        for(GodCard card : myGame.getActiveCards()){
            if(!card.isPicked()){
                gods[i] = new GodInfo(card);
                i++;
            }
        }
        player.WriteMessage(new CommandMsg(CommandType.NUMBER, new Info(gods)));
        return player.ReadMessage().getList().get(0);
    }

    /**
     * Request of a Box through coordinates
     * @param player ClientHandler of the Player that has to chose
     * @param type CoordinateType that indicates the Game Phase
     * @return asked Box
     */
    public Box AskCoordinates(ClientHandler player, CoordinateType type) {
        CommandType cmd = CommandType.DEFAULT;
        switch (type){
            case INITIAL:
                cmd = CommandType.POS_INITIAL;
                break;
            case WORKER:
                cmd = CommandType.POS_WORKER;
                break;
            case MOVE:
                cmd = CommandType.POS_MOVE;
                break;
            case BUILD:
                cmd = CommandType.POS_BUILD;
                break;
        }
        player.WriteMessage(new CommandMsg(cmd, null));
        ServerMsg answer = player.ReadMessage();
        return myMap.getBox(answer.getList().get(0), answer.getList().get(1));
    }

    /**
     * Ask if the player would like to use his god power
     * @param player ClientHandler of the Player that has to answer
     * @return true or false
     */
    public boolean AskPower(ClientHandler player) {
        player.WriteMessage(new CommandMsg(CommandType.ANS_POWER, null));
        ServerMsg answer = player.ReadMessage();
        return answer.getMsg().equalsIgnoreCase("yes");
    }

    /**
     * Select one of the player's worker
     * @param player ClientHandler of the Player that has to chose
     * @return selected worker
     */
    public Worker AskWorker(ClientHandler player) {
        Box box = AskCoordinates(player, CoordinateType.WORKER);
        Worker candidate = myGame.getActualPlayer().selectWorker(box);
        if (candidate == null) {
            player.WriteMessage(new CommandMsg(CommandType.COM_INVALID_WORKER, null));
            return AskWorker(player);
        }
        return candidate;
    }

    /**
     * Send Error message if an invalid box is selected
     * @param player ClientHandler of the Player that has chosen
     */
    public void NotValidDest(ClientHandler player) {
        player.WriteMessage(new CommandMsg(CommandType.COM_INVALID_POS, null));
    }

    /**
     * Send message of losing game
     * @param player ClientHandler of the Player who lost
     */
    public void Lose(ClientHandler player){
        player.WriteMessage(new CommandMsg(CommandType.COM_LOSE, null));
    }

    /**
     * Send message of Finish to the Player
     * @param player ClientHandler of the Player
     */
    public void GameFinished(ClientHandler player){
        player.WriteMessage(new CommandMsg(CommandType.CLOSE_NORMAL, new Info(new PlayerInfo(myGame.getWinner()))));
    }

    /**
     * Send an Update message of the Game Map after a generic move
     * @param player ClientHandler of the Player
     * @param generic type of map, true if is generic, false if it specific to a game phase
     * @param phase phase of the game
     */
    public void UpdateMap(ClientHandler player, boolean generic, boolean phase){
        player.WriteMessage(new CommandMsg(CommandType.UPDATE_ACTION, new Info(MapInfo(generic, phase))));
    }

    /**
     * Send a message to all the Players
     * @param msg CommandMsg to be send
     */
    public void Echo(CommandMsg msg){
        for (ClientHandler handler : myGame.getController().getHandlers()){
            handler.WriteMessage(msg);
        }
    }

    /**
     * Send a message to all the Players, except one that receives another one
     * @param actual ClientHandler of the Player that receives the special message
     * @param msgActual the special message
     * @param msg the normal message
     */
    public void Echo(ClientHandler actual, CommandMsg msgActual, CommandMsg msg){
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
     * @param generic type of map, true if is generic, false if it specific to a game phase
     * @param phase phase of the game
     * @return the playground information
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