package pl.edu.agh.kis.pz1.player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.pz1.cards.Card;

import static org.junit.Assert.*;

public class PlayerTest {
    private Player player;

    @Before
    public void setUp() throws Exception {
        player = new Player(2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getId() {
        assertEquals(2, player.getId());
    }

    @Test
    public void getIsInPlay() {
        assertTrue(player.getIsInPlay());
    }

    @Test
    public void getHand() {
        assertNotNull(player.getHand());
    }

    @Test
    public void setIsInPlay() {
        player.setIsInPlay(false);
        assertFalse(player.getIsInPlay());
    }



    @Test
    public void fold() {
        player.fold();
        assertFalse(player.getIsInPlay());
    }

    @Test
    public void call() {
        player.call(10);
        assertEquals(40, player.getFunds());
        assertFalse(player.call(50));
        assertTrue(player.getIsInPlay());
    }

    @Test
    public void raise() {
        player.raise(2);
        assertEquals(48, player.getFunds());
        assertFalse(player.raise(50));
        assertTrue(player.getIsInPlay());
    }

}