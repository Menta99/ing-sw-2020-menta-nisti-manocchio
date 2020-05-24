package Controller.SavedData;

import Model.Game;
import Model.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class GameData {

    private int gameID;
    private int turnNumber;
    private int playerNumber;
    private MapData map;
    private PlayerData[] players;

    public GameData(){
        Game myGame = Game.getInstance();
        gameID=-1;
        playerNumber=myGame.getPlayer().size();
        map = new MapData();
        players = new PlayerData[playerNumber];
        for (int i=0; i<playerNumber; i++){
            Player player = myGame.getPlayer().get(i);
            players[i] = new PlayerData(player.getNickName(), player.getCard().getName());
        }
    }

    public void update(){
        gameID=Game.getInstance().getId();
        turnNumber = Game.getInstance().getActualTurn();
        //System.out.println("Turn number updated");
        for (PlayerData player : players){
            player.update();
            //System.out.println("player updated");
        }
        map.update();
    }

    public void save() throws IOException {
        File gameInformation = new File("temp\\savedGame.txt");
        try{
        FileWriter myWriter = new FileWriter(gameInformation);
            myWriter.write(toString());
            myWriter.close();
        }
        catch (IOException e){
            System.out.println("Cannot write on File" + gameInformation);
        }
    }

    /**
     * Creates all the necessary information to build back a Game:
     * 1) Game ID number
     * 2) Actual Turn
     * 3) Number of Players
     * 4) Player Info
     * 5) Player Info
     * 6*) Player Info
     * 6-7*) map Info
     */
    @Override
    public String toString(){
        String toSave = "";
        toSave = toSave.concat(gameID + "\n" + turnNumber + "\n" + playerNumber + "\n");
        toSave = toSave.concat(map.toString());
        for (int i=0; i < playerNumber; i++) {
            toSave = toSave.concat(players[i].toString());
        }
        return (toSave);
    }

    public static int Size(){
        return 3;
    }

    public static boolean isPlayerInIt(String playerName){
        ArrayList<String> gameInfo = Game.getInstance().getSavedGame("savedGame");
        if (gameInfo.get(0).equals("-1")){ //If there's no saved game returns false
            return false;
        }
        int nameIndex = 1;
        for (int i = 0; i < parseInt(gameInfo.get(2)); i++ ){  //gameInfo.2 contains number of players in the game
            if (playerName.equals(gameInfo.get(GameData.Size() + MapData.Size() + nameIndex))){
                return true;
            }
            else {
                nameIndex += 2*WorkerData.Size() + PlayerData.Size();
            }
        }
        return false;
    }

    public void SaveAll(){
        this.update();
        try {
            this.save();
        } catch (IOException e) {
            System.out.println("Couldn't save the data");
        }
    }

    public void ResetData(){
        gameID = -1;
        try {
            save();
        } catch (IOException e) {
            System.out.println("Couldn't save the Data");
            e.printStackTrace();
        }
    }
}
