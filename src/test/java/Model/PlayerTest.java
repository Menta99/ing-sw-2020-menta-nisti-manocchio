package Model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    Player player1;
    Player player2;
    PlayGround tab;
    Game partita;


    @Before
    public void setUp() throws Exception {
        partita=Game.getInstance();
        tab=PlayGround.getInstance();
        player1=new Player("Andrea");
        player2=new Player("Nisto");

    }

    @After
    public void tearDown() throws Exception {
        tab.Delete();
        partita.Delete();

    }

    @Test
    public void draw() {


        int[] indici = {1,3};
        partita.getDeck().getCardList().add(new TestCard("Apollo"));
        partita.getDeck().getCardList().add(new TestCard("Zeus"));
        partita.getDeck().getCardList().add(new TestCard("Prometeo"));
        partita.getDeck().getCardList().add(new TestCard("Era"));
        partita.ExtractCard(indici);
        Assert.assertEquals("Zeus",partita.getActiveCards().get(0).getName());
        Assert.assertEquals("Era",partita.getActiveCards().get(1).getName());

    }
/*
    @Test
    public void chooseGod() {
        int[] indici = {1,3};
        partita.getDeck().getCardList().add(new TestCard("Apollo"));
        partita.getDeck().getCardList().add(new TestCard("Zeus"));
        partita.getDeck().getCardList().add(new TestCard("Prometeo"));
        partita.getDeck().getCardList().add(new TestCard("Era"));
        partita.ExtractCard(indici);

        Assert.assertEquals(null,player1.getCard());
        Assert.assertEquals(null,player2.getCard());
        player1.ChooseGod(partita.getActiveCards(),0);
        Assert.assertEquals("Zeus",player1.getCard().getName());
        player2.ChooseGod(partita.getActiveCards(),0);
        Assert.assertEquals(null,player2.getCard());
        player2.ChooseGod(partita.getActiveCards(),1);
        Assert.assertEquals("Era",player2.getCard().getName());




    }*/

    @Test
    public void selectWorker() {
        Box box=tab.getBox(2,2);
        Box box2=tab.getBox(1,2);
        //Assert.assertEquals(null,player1.selectWorker(box));
        //Assert.assertEquals(true,worker.setInitialPosition(box));
        player1.getWorkers().get(0).setInitialPosition(box);
        player1.getWorkers().get(0).Build(box2);
        player1.getWorkers().get(0).Move(box2);
        Assert.assertEquals(PawnType.WORKER,box2.getUpperLevel());
        Assert.assertEquals(player1.getWorkers().get(0),box2.getStructure().get(box2.getStructure().size()-1));

    }

    @Test
    public void turnStart() {

    }


}