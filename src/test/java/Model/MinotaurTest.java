package Model;

import Model.*;
import Model.Godcards.Minotaur;
import Model.Godcards.GodCard;
import Model.Godcards.Zeus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MinotaurTest {
    Game match;
    PlayGround field;
    Player player_1, player_2;
    Worker worker_1_a, worker_1_b, worker_2_a, worker_2_b;
    GodCard card_1, card_2;


    @Before
    public void setUp() throws Exception {
        match = Game.getInstance();
        field = PlayGround.getInstance();
        ArrayList<Integer> indici = new ArrayList<Integer>();
        indici.add(9);
        indici.add(13);
        player_1 = new Player("MyName1");
        player_2 = new Player("MyName2");
        worker_1_a = player_1.getWorkers().get(0);
        worker_1_b = player_1.getWorkers().get(1);
        worker_2_a = player_2.getWorkers().get(0);
        worker_2_b = player_2.getWorkers().get(1);
        card_1 = match.getDeck().getCardList().get(9); //1 almeno è Godcard!!
        card_2 = match.getDeck().getCardList().get(13);
        match.ExtractCard(indici);
        player_1.ChooseGod(0);
        player_2.ChooseGod(1);
        //Assert.assertEquals(player_1, card_1.getOwner());



    }

    @After
    public void tearDown() throws Exception {
       field.Clean();
       match.CleanGame();
    }

    @Test
    public void moveOthers() {
        Box box1 = field.getBox(0, 0);
        Box box2 = field.getBox(1, 0);
        Box box3=  field.getBox(2,0);
        worker_1_a.setInitialPosition(box1);
        worker_2_a.setInitialPosition(box2);
        worker_1_b.setInitialPosition(field.getBox(4,4));
        worker_2_b.setInitialPosition(field.getBox(3,4));
        player_1.setSelectedWorker(worker_1_a);
        player_2.setSelectedWorker(worker_2_a);
        player_1.getCard().moveOthers(box2);
        Assert.assertEquals(box2,worker_1_a.getPosition());
        Assert.assertEquals(box3,worker_2_a.getPosition());

    }


    @Test
    public void canMoveOthers() {
        Box box1 = field.getBox(0, 0);
        Box box2 = field.getBox(1, 0);
        Box box3=  field.getBox(2,0);
        worker_1_a.setInitialPosition(box2);
        worker_2_a.setInitialPosition(box1);
        worker_1_b.setInitialPosition(field.getBox(4,4));
        worker_2_b.setInitialPosition(box3);
        player_1.setSelectedWorker(worker_1_a);
        player_2.setSelectedWorker(worker_2_a);
        Assert.assertEquals(false,player_1.getCard().canMoveOthers(box1));
        Assert.assertEquals(true,player_1.getCard().canMoveOthers(box3));
    }
}
