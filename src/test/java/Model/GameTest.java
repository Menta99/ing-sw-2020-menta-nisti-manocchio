package Model;

import Controller.Controller;
import Model.Godcards.GodsEnum;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class GameTest {
    Game myGame;

    @Before
    public void setUp() throws Exception {
        Game.getInstance().CleanGame();
        myGame = Game.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        myGame.CleanGame();
    }

    @Test
    public void startGame() {
        ArrayList<Player> myPlayers = new ArrayList<Player>();
        Assert.assertEquals(0, myGame.getPlayer().size());
        Assert.assertEquals(0, myGame.getActualTurn());
        Assert.assertNull(myGame.getWinner());
        myPlayers.add(new Player("Carlo"));
        myPlayers.add(new Player("Marco"));
        myPlayers.add(new Player("Paolo"));
        Assert.assertEquals(3, myGame.getPlayer().size());
    }

    @Test
    public void deckCompositionTest(){
        ArrayList<Player> myPlayers = new ArrayList<Player>();
        myPlayers.add(new Player("Carlo"));
        myPlayers.add(new Player("Marco"));
        myPlayers.add(new Player("Paolo"));
        for (GodsEnum god : GodsEnum.values()){
            Assert.assertEquals(god.toString(), myGame.getDeck().getCardList().get(god.getIndex()).getName().toUpperCase());
        }
    }
    @Test
    public void endgameTest(){
        Player player=new Player("carlo");
        player.setLoser(true);
        Assert.assertEquals(true,player.isLoser());
        player.setWorkers(null);
        Assert.assertNull(player.getWorkers());
    }

    @Test
    public void incrementActualTurn() {
        int turn = myGame.getActualTurn();
        myGame.IncrementActualTurn();
        Assert.assertEquals(true, myGame.getActualTurn()==(turn+1));
    }

    @Test
    public void loadGame() {
        myGame.loadGame("game0");
        Assert.assertEquals(true, myGame.getId() == 1606);
        Assert.assertEquals(true, myGame.getActualTurn() == 4);
        Assert.assertEquals(true, myGame.getPlayer().size() == 2);
        Assert.assertEquals(true, myGame.getPlayer().get(0).getNickName().equals("gio"));
        Assert.assertEquals(true, myGame.getPlayer().get(1).getNickName().equals("asd"));
        Assert.assertEquals(true, myGame.getActualPlayer().getNickName().equals("gio"));
        Assert.assertEquals(true, myGame.getActiveCards().contains(myGame.getPlayer().get(0).getCard()));
        Assert.assertEquals(true, myGame.getActiveCards().contains(myGame.getPlayer().get(1).getCard()));
        Assert.assertEquals(true, !myGame.isGameFinished());
        Assert.assertEquals(true, PlayGround.getInstance().getBox(0,0).isOccupied());
        Assert.assertEquals(true, myGame.getPlayer().get(0).getWorkers().get(0).getPosition().equals(PlayGround.getInstance().getBox(0,0)));
        Assert.assertEquals(true, myGame.getPlayer().get(0).getWorkers().get(1).getPosition().equals(PlayGround.getInstance().getBox(1,2)));
        Assert.assertEquals(true, myGame.getPlayer().get(1).getWorkers().get(0).getPosition().equals(PlayGround.getInstance().getBox(1,3)));
        Assert.assertEquals(true, myGame.getPlayer().get(1).getWorkers().get(1).getPosition().equals(PlayGround.getInstance().getBox(3,3)));
        Assert.assertEquals(true, myGame.getPlayer().get(0).getWorkers().get(0).getLastPosition().equals(PlayGround.getInstance().getBox(0,0)));
        Assert.assertEquals(true, myGame.getPlayer().get(0).getWorkers().get(1).getLastPosition().equals(PlayGround.getInstance().getBox(1,1)));
        Assert.assertEquals(true, myGame.getPlayer().get(1).getWorkers().get(0).getLastPosition().equals(PlayGround.getInstance().getBox(2,2)));
        Assert.assertEquals(true, myGame.getPlayer().get(1).getWorkers().get(1).getLastPosition().equals(PlayGround.getInstance().getBox(3,3)));
        Assert.assertEquals(true, !myGame.getPlayer().get(0).getWorkers().get(0).isMoved() && !myGame.getPlayer().get(0).getWorkers().get(0).isDidBuild() && !myGame.getPlayer().get(0).getWorkers().get(0).getDidClimb());
        Assert.assertEquals(true, myGame.getPlayer().get(1).getWorkers().get(0).isMoved() && myGame.getPlayer().get(1).getWorkers().get(0).isDidBuild() && myGame.getPlayer().get(1).getWorkers().get(0).getDidClimb());
    }


    @Test
    public void extractCard() {
        Player player0 = new Player("asd");
        Player player1 = new Player("dsa");
        ArrayList<Integer> index = new ArrayList<>();
        index.add(0);
        index.add(1);
        Assert.assertEquals(true, myGame.ExtractCard(index));
        Assert.assertEquals(true, myGame.getActiveCards().get(0).getName().equals("Apollo"));
        Assert.assertEquals(true, myGame.getActiveCards().get(1).getName().equals("Artemis"));
        Assert.assertEquals(true, player1.Draw(myGame.getDeck(), index) == null);
    }

    @Test
    public void illegalExtractCard() throws IndexOutOfBoundsException {
        ArrayList<Integer> index = new ArrayList<>();
        index.add(1);
        Assert.assertEquals(false, myGame.ExtractCard(index));
        Game.getInstance().loadGame("apolloArtemisTest");
        Assert.assertEquals(false, myGame.ExtractCard(index));
    }

    @Test
    public void setController() {
        Controller controller = new Controller();
        myGame.setController(controller);
        Assert.assertEquals(true, myGame.getController()==controller);
    }


    @Test
    public void setId() {
        myGame.setId(69);
        Assert.assertEquals(69, myGame.getId());
    }

    @Test
    public void setWinner() {
        Player player0 = new Player("asd");
        myGame.setWinner(player0);
        Assert.assertEquals(true, myGame.getWinner().equals(player0));
    }

    @Test
    public void setGameFinished() {
        myGame.setGameFinished(true);
        Assert.assertEquals(true, myGame.isGameFinished());
    }

    @Test
    public void inexistentSavedGame() throws Exception{
        Assert.assertEquals(null, myGame.getSavedGame("inexistentFile"));
    }

    @Test
    public void voidSavedGame() throws Exception{
        ArrayList<String> vuoto = new ArrayList<>();
        Assert.assertEquals(vuoto, myGame.getSavedGame("voidFile"));
    }

}