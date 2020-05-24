package Model.Godcards;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GodFactoryTest {
    @Test
    public void create() {
        GodFactory factory = new GodFactory();
        Assert.assertEquals("Apollo", factory.create(GodsEnum.APOLLO).getName());
        Assert.assertEquals("Artemis", factory.create(GodsEnum.ARTEMIS).getName());
        Assert.assertEquals("Athena", factory.create(GodsEnum.ATHENA).getName());
        Assert.assertEquals("Atlas", factory.create(GodsEnum.ATLAS).getName());
        Assert.assertEquals("Chronus", factory.create(GodsEnum.CHRONUS).getName());
        Assert.assertEquals("Demeter", factory.create(GodsEnum.DEMETER).getName());
        Assert.assertEquals("Hephaestus", factory.create(GodsEnum.HEPHAESTUS).getName());
        Assert.assertEquals("Hera", factory.create(GodsEnum.HERA).getName());
        Assert.assertEquals("Hestia", factory.create(GodsEnum.HESTIA).getName());
        Assert.assertEquals("Minotaur", factory.create(GodsEnum.MINOTAUR).getName());
        Assert.assertEquals("Pan", factory.create(GodsEnum.PAN).getName());
        Assert.assertEquals("Prometeo", factory.create(GodsEnum.PROMETEO).getName());
        Assert.assertEquals("Triton", factory.create(GodsEnum.TRITON).getName());
        Assert.assertEquals("Zeus", factory.create(GodsEnum.ZEUS).getName());
    }
}