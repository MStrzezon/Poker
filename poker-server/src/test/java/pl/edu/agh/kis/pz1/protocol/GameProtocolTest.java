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
        numbers = new ArrayList<Integer>();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void statusProcess() {
        assertEquals("Game not created!", gp.processInput(0, "/status", numbers)[1]);
        numbers.add(10);
        gp.processInput(0, "/create", numbers);
        gp.processInput(0, "/join", numbers);
        gp.processInput(1, "/join", numbers);
        gp.processInput(2, "/join", numbers);
        assertEquals("Game created but not started!", gp.processInput(0, "/status", numbers)[1]);
        gp.processInput(0, "/start", numbers);
        assertEquals("It's: 1 round. Now playing player nr: 0", gp.processInput(0, "/status", numbers)[1]);

        //TODO fulfill after making moves
//        assertEquals("Player number 0", gp.processInput(1, "/status", numbers)[1]);
//        assertEquals("Player number 0", gp.processInput(2, "/status", numbers)[1]);
//        gp.processInput(0, "/call", numbers);
//        gp.processInput(1, "/call", numbers);
//        gp.processInput(2, "/call", numbers);
//        assertEquals("It's draw time!", gp.processInput(2, "/status", numbers)[1]);
//        gp.processInput(0, "/call", numbers);
//        gp.processInput(1, "/call", numbers);
//        gp.processInput(2, "/call", numbers);
//        assertEquals("Game is finished. Enter /result to show results.", gp.processInput(2, "/status", numbers)[1]);
    }

    @Test
    public void createProcess() {
        assertEquals("You should enter ante! Max ante: 25", gp.processInput(0, "/create", numbers)[1]);
        numbers.add(10);
        assertEquals("Game was created! Ante: 10", gp.processInput(0, "/create", numbers)[1]);
        assertEquals("The game was already created! Enter /join to join to the game!", gp.processInput(1, "/create", numbers)[1]);
}

    @Test
    public void joinProcess() {
        assertEquals("The game has not been created yet! Enter /create {ante} to create a game! Max ante: 25", gp.processInput(0, "/join", numbers)[1]);
        numbers.add(10);
        assertEquals("Game was created! Ante: 10", gp.processInput(0, "/create", numbers)[1]);
        assertEquals("0", gp.processInput(1, "/players", numbers)[1]);
        assertEquals("You joined the game, your number in game: 0", gp.processInput(1, "/join", numbers)[1]);
        assertEquals("1", gp.processInput(1, "/players", numbers)[1]);
        assertEquals("You joined the game, your number in game: 1", gp.processInput(0, "/join", numbers)[1]);
        assertEquals("2", gp.processInput(1, "/players", numbers)[1]);
        assertEquals("You are already join to the game", gp.processInput(1, "/join", numbers)[1]);
        assertEquals("2", gp.processInput(1, "/players", numbers)[1]);
        assertEquals("You are already join to the game", gp.processInput(0, "/join", numbers)[1]);
        assertEquals("2", gp.processInput(1, "/players", numbers)[1]);
        assertEquals("You joined the game, your number in game: 2", gp.processInput(2, "/join", numbers)[1]);
        assertEquals("3", gp.processInput(1, "/players", numbers)[1]);
        assertEquals("You joined the game, your number in game: 3", gp.processInput(3, "/join", numbers)[1]);
        assertEquals("4", gp.processInput(1, "/players", numbers)[1]);
        assertEquals("Too many participants. You cannot join to this game", gp.processInput(4, "/join", numbers)[1]);
        assertEquals("4", gp.processInput(1, "/players", numbers)[1]);
    }

    @Test
        public void playersProcess() {
        numbers.add(10);
        gp.processInput(1, "/create", numbers);
        assertEquals("0", gp.processInput(0, "/players", numbers)[1]);
        gp.processInput(1, "/join", numbers);
        assertEquals("1", gp.processInput(0, "/players", numbers)[1]);
        gp.processInput(0, "/join", numbers);
        assertEquals("2", gp.processInput(0, "/players", numbers)[1]);
    }

    @Test
    public void startProcess() {
        assertEquals("The game has not been created yet! Enter /create {ante} to create a game! Max ante: 25", gp.processInput(0, "/start", numbers)[1]);
        numbers.add(10);
        gp.processInput(1, "/create", numbers);
        assertEquals("You should first join to the game!", gp.processInput(0, "/start", numbers)[1]);
        gp.processInput(0, "/join", numbers);
        assertEquals("Wait for another player! Minimum number of players: 2", gp.processInput(0, "/start", numbers)[1]);
        gp.processInput(1, "/join", numbers);
        assertEquals("You should first join to the game!", gp.processInput(2, "/start", numbers)[1]);
        assertEquals("Game was started! Player number 0 play!\nAll funds: 20.    Current bet: 10\nYour funds:     50",
                gp.processInput(0, "/start", numbers)[1]);
        assertEquals("Game already started!", gp.processInput(2, "/start", numbers)[1]);
        assertEquals("Game already started!", gp.processInput(0, "/start", numbers)[1]);
    }

    @Test
    public void idProcess() {
        assertEquals("The game has not been created yet! Enter /create {ante} to create a game! Max ante: 25", gp.processInput(0, "/id", numbers)[1]);
        numbers.add(10);
        gp.processInput(1, "/create", numbers);
        gp.processInput(0, "/join", numbers);
        gp.processInput(1, "/join", numbers);
        assertEquals("Your game id: 0", gp.processInput(0, "/id", numbers)[1]);
        assertEquals("Your game id: 1", gp.processInput(1, "/id", numbers)[1]);
        assertEquals("You should join to the game to see your id in game!", gp.processInput(2, "/id", numbers)[1]);
    }

    @Test
    public void handProcess() {
        assertEquals("You must play the game to display your deck of cards!", gp.processInput(0, "/hand", numbers)[1]);
        numbers.add(10);
        gp.processInput(0, "/create", numbers);
        assertEquals("You must play the game to display your deck of cards!", gp.processInput(0, "/hand", numbers)[1]);
        gp.processInput(0, "/join", numbers);
        gp.processInput(1, "/join", numbers);
        assertEquals("You must play the game to display your deck of cards!", gp.processInput(0, "/hand", numbers)[1]);
        gp.processInput(0, "/start", numbers);
        assertEquals(5, gp.processInput(0, "/hand", numbers)[1].lines().count());
        assertEquals(5, gp.processInput(1, "/hand", numbers)[1].lines().count());
        assertEquals("You are not playing the game. Wait for the end of game!", gp.processInput(3, "/hand", numbers)[1]);
    }

//
//    @Test
//    public void callProcess() {
//        gp.processInput(0, "/join", numbers);
//        gp.processInput(1, "/join", numbers);
//        gp.processInput(2, "/join", numbers);
//        gp.processInput(0, "/start", numbers);
//        assertEquals("Player 0 called.\nNow player 1\nAll funds: 20.    Current bet: 5", gp.processInput(0, "/call", numbers)[1]);
//        assertEquals("Player number 1", gp.processInput(0, "/status", numbers)[1]);
//        assertEquals("Player 1 called.\nNow player 2\nAll funds: 25.    Current bet: 5", gp.processInput(1, "/call", numbers)[1]);
//        assertEquals("Player number 2", gp.processInput(0, "/status", numbers)[1]);
//        assertEquals("Player 2 called.\nNow time to draw!\nAll funds: 30.    Current bet: 5", gp.processInput(2, "/call", numbers)[1]);
//        assertEquals("It's draw time!", gp.processInput(0, "/status", numbers)[1]);
//    }
//
//    @Test
//    public void raiseProcess() {
//        gp.processInput(0, "/join", numbers);
//        gp.processInput(1, "/join", numbers);
//        gp.processInput(2, "/join", numbers);
//        gp.processInput(0, "/start", numbers);
//        numbers.clear();
//        numbers.add(15);
//        assertEquals("Player 0 raised.\nNow player 1\nAll funds: 30.    Current bet: 15", gp.processInput(0, "/raise", numbers)[1]);
//        assertEquals("Player number 1", gp.processInput(0, "/status", numbers)[1]);
//        assertEquals("Player 1 called.\nNow player 2\nAll funds: 45.    Current bet: 15", gp.processInput(1, "/call", numbers)[1]);
//        assertEquals("Player number 2", gp.processInput(0, "/status", numbers)[1]);
//        numbers.clear();
//        numbers.add(5);
//        assertEquals("You should raise bet!", gp.processInput(2, "/raise", numbers)[1]);
//        numbers.clear();
//        numbers.add(20);
//        assertEquals("Player 2 raised.\nNow time to draw!\nAll funds: 65.    Current bet: 20", gp.processInput(2, "/raise", numbers)[1]);
//        assertEquals("It's draw time!", gp.processInput(0, "/status", numbers)[1]);
//    }
//

}