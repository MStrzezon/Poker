package pl.edu.agh.kis.pz1.protocol;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.edu.agh.kis.pz1.game.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GameProtocolTest {
    GameProtocol gp;
    ArrayList<Integer> numbers;

    @Before
    public void setUp() throws Exception {
        gp = new GameProtocol();
        numbers = new ArrayList<Integer>(List.of(1));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void playersProcess() {
        assertEquals(0, Integer.parseInt(gp.numberOfPlayers()));
        gp.processInput(0, "/join", numbers);
        assertEquals(1, Integer.parseInt(gp.numberOfPlayers()));
        gp.processInput(0, "join", numbers);
    }

    @Test
    public void joinProcess() {
        assertEquals(0, Integer.parseInt(gp.numberOfPlayers()));
        gp.processInput(0, "/join", numbers);
        assertEquals(1, Integer.parseInt(gp.numberOfPlayers()));
        gp.processInput(0, "join", numbers);
        assertEquals(1, Integer.parseInt(gp.numberOfPlayers()));
        gp.processInput(1, "/join", numbers);
        assertEquals(2, Integer.parseInt(gp.numberOfPlayers()));
        gp.processInput(0, "/start", numbers);
        assertEquals("You are already joined the game.", gp.processInput(1, "/join", numbers)[1]);
        assertEquals("Game already started. You cannot join", gp.processInput(2, "/join", numbers)[1]);
    }

    @Test
    public void startProcess() {
        assertEquals("You have to join to the game", gp.processInput(0, "/start", numbers)[1]);
        gp.processInput(0, "/join", numbers);
        assertEquals("Wait for another player!", gp.processInput(0, "/start", numbers)[1]);
        gp.processInput(1, "/join", numbers);
        assertEquals("Game was started! Player number 0 play!\nAll funds: 10.    Current bet: 5"
                , gp.processInput(0, "/start", numbers)[1]);
        assertEquals("Game already started!", gp.processInput(0, "/start", numbers)[1]);
    }

    @Test
    public void handProcess() {
        assertEquals("You are not playing the game!", gp.processInput(0, "/hand", numbers)[1]);
        gp.processInput(0, "/join", numbers);
        gp.processInput(1, "/join", numbers);
        assertEquals("Game not started!", gp.processInput(0, "/hand", numbers)[1]);
        gp.processInput(2, "/join", numbers);
        gp.processInput(0, "/start", numbers);
        assertEquals(5 ,gp.processInput(0, "/hand", numbers)[1].lines().count());
        assertEquals("You are not playing the game!", gp.processInput(3, "/hand", numbers)[1]);
        assertEquals("Game already started. You cannot join", gp.processInput(3, "/join", numbers)[1]);
    }

    @Test
    public void statusProcess() {
        gp.processInput(0, "/join", numbers);
        gp.processInput(1, "/join", numbers);
        gp.processInput(2, "/join", numbers);
        gp.processInput(0, "/start", numbers);
        assertEquals("Your turn!", gp.processInput(0, "/status", numbers)[1]);
        assertEquals("Player number 0", gp.processInput(1, "/status", numbers)[1]);
        assertEquals("Player number 0", gp.processInput(2, "/status", numbers)[1]);
        gp.processInput(0, "/call", numbers);
        gp.processInput(1, "/call", numbers);
        gp.processInput(2, "/call", numbers);
        assertEquals("It's draw time!", gp.processInput(2, "/status", numbers)[1]);
        gp.processInput(0, "/call", numbers);
        gp.processInput(1, "/call", numbers);
        gp.processInput(2, "/call", numbers);
        assertEquals("Game is finished. Enter /result to show results.", gp.processInput(2, "/status", numbers)[1]);
    }

    @Test
    public void callProcess() {
        gp.processInput(0, "/join", numbers);
        gp.processInput(1, "/join", numbers);
        gp.processInput(2, "/join", numbers);
        gp.processInput(0, "/start", numbers);
        assertEquals("Player 0 called.\nNow player 1\nAll funds: 20.    Current bet: 5", gp.processInput(0, "/call", numbers)[1]);
        assertEquals("Player number 1", gp.processInput(0, "/status", numbers)[1]);
        assertEquals("Player 1 called.\nNow player 2\nAll funds: 25.    Current bet: 5", gp.processInput(1, "/call", numbers)[1]);
        assertEquals("Player number 2", gp.processInput(0, "/status", numbers)[1]);
        assertEquals("Player 2 called.\nNow time to draw!\nAll funds: 30.    Current bet: 5", gp.processInput(2, "/call", numbers)[1]);
        assertEquals("It's draw time!", gp.processInput(0, "/status", numbers)[1]);
    }

    @Test
    public void raiseProcess() {
        gp.processInput(0, "/join", numbers);
        gp.processInput(1, "/join", numbers);
        gp.processInput(2, "/join", numbers);
        gp.processInput(0, "/start", numbers);
        numbers.clear();
        numbers.add(15);
        assertEquals("Player 0 raised.\nNow player 1\nAll funds: 30.    Current bet: 15", gp.processInput(0, "/raise", numbers)[1]);
        assertEquals("Player number 1", gp.processInput(0, "/status", numbers)[1]);
        assertEquals("Player 1 called.\nNow player 2\nAll funds: 45.    Current bet: 15", gp.processInput(1, "/call", numbers)[1]);
        assertEquals("Player number 2", gp.processInput(0, "/status", numbers)[1]);
        numbers.clear();
        numbers.add(5);
        assertEquals("You should raise bet!", gp.processInput(2, "/raise", numbers)[1]);
        numbers.clear();
        numbers.add(20);
        assertEquals("Player 2 raised.\nNow time to draw!\nAll funds: 65.    Current bet: 20", gp.processInput(2, "/raise", numbers)[1]);
        assertEquals("It's draw time!", gp.processInput(0, "/status", numbers)[1]);
    }

    @Test
    public void numberOfPlayers() {
        assertEquals(0, Integer.parseInt(gp.numberOfPlayers()));
        gp.processInput(1, "/join", numbers);
        assertEquals(1, Integer.parseInt(gp.numberOfPlayers()));
    }
}