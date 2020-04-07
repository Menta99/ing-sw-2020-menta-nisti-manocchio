package Model;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

public class TestCardTest {
    Game match;
    PlayGround field;
    Player player_1, player_2;
    Worker worker_1_a, worker_1_b, worker_2_a, worker_2_b;
    GodCard card_1, card_2;

    @Before
    public void setUp() throws Exception {
        match = Game.getInstance();
        field = PlayGround.getInstance();
        player_1 = new Player("MyName1");
        player_2 = new Player("MyName2");
        match.getPlayer().add(player_1);
        match.getPlayer().add(player_2);
        worker_1_a = player_1.getWorkers().get(0);
        worker_1_b = player_1.getWorkers().get(1);
        worker_2_a = player_2.getWorkers().get(0);
        worker_2_b = player_2.getWorkers().get(1);
        card_1 = new TestCard("GodName1");
        card_2 = new TestCard("GodName2");
        match.getDeck().getCardList().add(card_1);
        match.getDeck().getCardList().add(card_2);
    }

    @After
    public void tearDown() throws Exception {
        field.Delete();
        match.Delete();
    }
}