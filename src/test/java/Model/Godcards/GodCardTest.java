package Model.Godcards;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GodCardTest {

    GodCard card = new Atlas(); //doesn't have the majority of method implementation


    @Test
    public void myMovement() {
        Assert.assertEquals(false, card.myMovement());
    }

    @Test
    public void enemyMovement() {
        Assert.assertEquals(false, card.enemyMovement());
    }

    @Test
    public void myBuild() {
        Assert.assertEquals(false, card.myBuild());
    }

    @Test
    public void myVictoryCondition() {
        Assert.assertEquals(false, card.myVictoryCondition());
    }

    @Test
    public void enemyVictoryCondition() {
        Assert.assertEquals(false, card.enemyVictoryCondition(null));
    }

    @Test
    public void activeSubroutine() {
        Assert.assertEquals(false, card.activeSubroutine());
    }

    @Test
    public void canMoveOthers() {
        Assert.assertEquals(false, card.canMoveOthers(null));
    }

    @Test
    public void moveOthers() {
        Assert.assertEquals(false, card.moveOthers(null));
    }

    @Test
    public void getOwner() {
        Assert.assertEquals(null, card.getOwner());
    }

    @Test
    public void specialMovement() {
        Assert.assertEquals(null, card.specialMovement(null));
    }

    @Test
    public void specialBuilding() {
        Assert.assertEquals(null, card.specialBuilding(null));
    }

    @Test
    public void isActivePower() {
        Assert.assertEquals(false, card.isActivePower());
    }

    @Test
    public void getPower() {
        Assert.assertEquals("You can build a Dome upon every level", card.getPower());
    }
}