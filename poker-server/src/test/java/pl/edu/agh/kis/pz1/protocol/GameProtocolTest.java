package pl.edu.agh.kis.pz1.protocol;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.kis.pz1.cards.Card;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameProtocolTest {
    GameProtocol gp;
    ArrayList<Integer> parameters;

    @Before
    public void setUp() throws Exception {
        gp = new GameProtocol();
        parameters = new ArrayList<Integer>();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void statusProcess() {
        assertEquals("Game not created!", gp.processInput(0, "/state", parameters)[1]);
        parameters.add(5);
        gp.processInput(0, "/create", parameters);
        gp.processInput(0, "/join", parameters);
        gp.processInput(1, "/join", parameters);
        gp.processInput(2, "/join", parameters);
        assertEquals("Game created but not started!", gp.processInput(0, "/state", parameters)[1]);
        gp.processInput(0, "/start", parameters);
        assertEquals("It's: 1 round. Now playing player nr: 0", gp.processInput(0, "/state", parameters)[1]);

        gp.processInput(0, "/call", parameters);
        assertEquals("It's: 1 round. Now playing player nr: 1", gp.processInput(1, "/state", parameters)[1]);
        gp.processInput(1, "/call", parameters);
        assertEquals("It's: 1 round. Now playing player nr: 2", gp.processInput(2, "/state", parameters)[1]);
        gp.processInput(2, "/call", parameters);
        assertEquals("It's a draw time!", gp.processInput(2, "/state", parameters)[1]);
        parameters.clear();
        gp.processInput(0, "/draw", parameters);
        gp.processInput(1, "/draw", parameters);
        gp.processInput(2, "/draw", parameters);
        assertEquals("It's: 2 round. Now playing player nr: 0", gp.processInput(0, "/state", parameters)[1]);
        gp.processInput(0, "/call", parameters);
        assertEquals("It's: 2 round. Now playing player nr: 1", gp.processInput(1, "/state", parameters)[1]);
        gp.processInput(1, "/call", parameters);
        assertEquals("It's: 2 round. Now playing player nr: 2", gp.processInput(2, "/state", parameters)[1]);
        gp.processInput(2, "/call", parameters);
        assertEquals("Game is finished. Enter /result to show result.", gp.processInput(2, "/state", parameters)[1]);
    }

    @Test
    public void createProcess() {
        assertEquals("You should enter ante! Max ante: 10", gp.processInput(0, "/create", parameters)[1]);
        parameters.add(5);
        assertEquals("Game was created! Ante: 5", gp.processInput(0, "/create", parameters)[1]);
        assertEquals("The game was already created! Enter /join to join to the game!", gp.processInput(1, "/create", parameters)[1]);
}

    @Test
    public void joinProcess() {
        assertEquals("The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10", gp.processInput(0, "/join", parameters)[1]);
        parameters.add(5);
        assertEquals("Game was created! Ante: 5", gp.processInput(0, "/create", parameters)[1]);
        assertEquals("0", gp.processInput(1, "/players", parameters)[1]);
        assertEquals("You joined the game, your number in game: 0", gp.processInput(1, "/join", parameters)[1]);
        assertEquals("1", gp.processInput(1, "/players", parameters)[1]);
        assertEquals("You joined the game, your number in game: 1", gp.processInput(0, "/join", parameters)[1]);
        assertEquals("2", gp.processInput(1, "/players", parameters)[1]);
        assertEquals("You are already join to the game", gp.processInput(1, "/join", parameters)[1]);
        assertEquals("2", gp.processInput(1, "/players", parameters)[1]);
        assertEquals("You are already join to the game", gp.processInput(0, "/join", parameters)[1]);
        assertEquals("2", gp.processInput(1, "/players", parameters)[1]);
        assertEquals("You joined the game, your number in game: 2", gp.processInput(2, "/join", parameters)[1]);
        assertEquals("3", gp.processInput(1, "/players", parameters)[1]);
        assertEquals("You joined the game, your number in game: 3", gp.processInput(3, "/join", parameters)[1]);
        assertEquals("4", gp.processInput(1, "/players", parameters)[1]);
        assertEquals("Too many participants. Max number of players: 4", gp.processInput(4, "/join", parameters)[1]);
        assertEquals("4", gp.processInput(1, "/players", parameters)[1]);
    }

    @Test
        public void playersProcess() {
        parameters.add(5);
        gp.processInput(1, "/create", parameters);
        assertEquals("0", gp.processInput(0, "/players", parameters)[1]);
        gp.processInput(1, "/join", parameters);
        assertEquals("1", gp.processInput(0, "/players", parameters)[1]);
        gp.processInput(0, "/join", parameters);
        assertEquals("2", gp.processInput(0, "/players", parameters)[1]);
    }

    @Test
    public void startProcess() {
        assertEquals("The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10", gp.processInput(0, "/start", parameters)[1]);
        parameters.add(5);
        gp.processInput(1, "/create", parameters);
        assertEquals("You should first join to the game!", gp.processInput(0, "/start", parameters)[1]);
        gp.processInput(0, "/join", parameters);
        assertEquals("Wait for another player! Minimum number of players: 2", gp.processInput(0, "/start", parameters)[1]);
        gp.processInput(1, "/join", parameters);
        assertEquals("You should first join to the game!", gp.processInput(2, "/start", parameters)[1]);
        assertEquals("Game was started! Player number 0 play!\nAll funds: 10.    Current bet: 5\nYour funds:     50",
                gp.processInput(0, "/start", parameters)[1]);
        assertEquals("Game already started!", gp.processInput(2, "/start", parameters)[1]);
        assertEquals("Game already started!", gp.processInput(0, "/start", parameters)[1]);
    }

    @Test
    public void idProcess() {
        assertEquals("The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10", gp.processInput(0, "/id", parameters)[1]);
        parameters.add(10);
        gp.processInput(1, "/create", parameters);
        gp.processInput(0, "/join", parameters);
        gp.processInput(1, "/join", parameters);
        assertEquals("Your game id: 0", gp.processInput(0, "/id", parameters)[1]);
        assertEquals("Your game id: 1", gp.processInput(1, "/id", parameters)[1]);
        assertEquals("You should join to the game to see your id in game!", gp.processInput(2, "/id", parameters)[1]);
    }
    @Test
    public void moneyProcess() {
        assertEquals("The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10", gp.processInput(0, "/money", parameters)[1]);
        parameters.add(10);
        gp.processInput(1, "/create", parameters);
        gp.processInput(0, "/join", parameters);
        gp.processInput(1, "/join", parameters);
        gp.processInput(1, "/start", parameters);
        assertEquals("Money: 50", gp.processInput(0, "/money", parameters)[1]);
        assertEquals("Money: 50", gp.processInput(1, "/money", parameters)[1]);
        assertEquals("You should join to the game to see your money in game!", gp.processInput(2, "/money", parameters)[1]);
        gp.processInput(0, "/call", parameters);
        assertEquals("Money: 40", gp.processInput(0, "/money", parameters)[1]);
        gp.processInput(1, "/call", parameters);
        assertEquals("Money: 40", gp.processInput(1, "/money", parameters)[1]);
        gp.processInput(0, "/draw", parameters);
        gp.processInput(1, "/draw", parameters);
        gp.processInput(0, "/call", parameters);
        assertEquals("Money: 40", gp.processInput(0, "/money", parameters)[1]);
        gp.processInput(1, "/call", parameters);
        assertEquals("Money: 40", gp.processInput(1, "/money", parameters)[1]);
    }

    @Test
    public void handProcess() {
        assertEquals("You must play the game to display your deck of cards!", gp.processInput(0, "/hand", parameters)[1]);
        parameters.add(10);
        gp.processInput(0, "/create", parameters);
        assertEquals("You must play the game to display your deck of cards!", gp.processInput(0, "/hand", parameters)[1]);
        gp.processInput(0, "/join", parameters);
        gp.processInput(1, "/join", parameters);
        assertEquals("You must play the game to display your deck of cards!", gp.processInput(0, "/hand", parameters)[1]);
        gp.processInput(0, "/start", parameters);
        assertEquals(5, gp.processInput(0, "/hand", parameters)[1].lines().count());
        assertEquals(5, gp.processInput(1, "/hand", parameters)[1].lines().count());
        assertEquals("You are not playing the game. Wait for the end of game!", gp.processInput(3, "/hand", parameters)[1]);
    }

    @Test
    public void callProcess() {
        parameters.add(5);
        gp.processInput(0, "/create", parameters);
        assertEquals("You must play the game to be able to call!", gp.processInput(0, "/call", parameters)[1]);
        gp.processInput(0, "/join", parameters);
        gp.processInput(1, "/join", parameters);
        gp.processInput(2, "/join", parameters);
        assertEquals("You must play the game to be able to call!", gp.processInput(0, "/call", parameters)[1]);
        gp.processInput(0, "/start", parameters);
        assertEquals("It's: 1 round. Now playing player nr: 0", gp.processInput(0, "/state", parameters)[1]);
        assertEquals("Player 0 called. Now player 1\nAll funds: 20.    Current bet: 5", gp.processInput(0, "/call", parameters)[1]);
        assertEquals("It's: 1 round. Now playing player nr: 1", gp.processInput(0, "/state", parameters)[1]);
        assertEquals("Player 1 called. Now player 2\nAll funds: 25.    Current bet: 5", gp.processInput(1, "/call", parameters)[1]);
        assertEquals("It's: 1 round. Now playing player nr: 2", gp.processInput(0, "/state", parameters)[1]);
        assertEquals("Not your turn!", gp.processInput(1, "/call", parameters)[1]);
        assertEquals("Player 2 called. Now time to draw!\nAll funds: 30.    Current bet: 5", gp.processInput(2, "/call", parameters)[1]);
        assertEquals("Now is time to draw. You cannot call!", gp.processInput(0, "/call", parameters)[1]);
        parameters.clear();
        parameters.add(2);
        gp.processInput(0, "/draw", parameters);
        gp.processInput(1, "/draw", parameters);
        gp.processInput(2, "/draw", parameters);
        assertEquals("Player 0 called. Now player 1\nAll funds: 35.    Current bet: 5", gp.processInput(0, "/call", parameters)[1]);
        assertEquals("Player 1 called. Now player 2\nAll funds: 40.    Current bet: 5", gp.processInput(1, "/call", parameters)[1]);
        gp.processInput(0, "/call", parameters);
        gp.processInput(1, "/call", parameters);
        gp.processInput(2, "/call", parameters);
        assertEquals("Game is over. Enter /result to see result.", gp.processInput(2, "/call", parameters)[1]);
    }

    @Test
    public void raiseProcess() {
        parameters.add(5);
        gp.processInput(0, "/create", parameters);
        gp.processInput(0, "/join", parameters);
        gp.processInput(1, "/join", parameters);
        gp.processInput(2, "/join", parameters);
        gp.processInput(0, "/start", parameters);
        parameters.clear();
        parameters.add(15);
        assertEquals("Player 0 raised. Now player 1\nAll funds: 30.    Current bet: 15", gp.processInput(0, "/raise", parameters)[1]);
        assertEquals("It's: 1 round. Now playing player nr: 1", gp.processInput(0, "/state", parameters)[1]);
        parameters.clear();
        parameters.add(16);
        assertEquals("Player 1 raised. Now player 2\nAll funds: 46.    Current bet: 16", gp.processInput(1, "/raise", parameters)[1]);
        assertEquals("It's: 1 round. Now playing player nr: 2", gp.processInput(0, "/state", parameters)[1]);
        parameters.clear();
        parameters.add(14);
        assertEquals("You should raise bet or you don't have money!", gp.processInput(2, "/raise", parameters)[1]);
        parameters.clear();
        parameters.add(20);
        assertEquals("Player 2 raised. Now time to draw!\nAll funds: 66.    Current bet: 20", gp.processInput(2, "/raise", parameters)[1]);
        assertEquals("Now is time to draw. You cannot raise!", gp.processInput(0, "/raise", parameters)[1]);
        parameters.clear();
        parameters.add(2);
        gp.processInput(0, "/draw", parameters);
        gp.processInput(1, "/draw", parameters);
        gp.processInput(2, "/draw", parameters);
        gp.processInput(0, "/call", parameters);
        gp.processInput(1, "/call", parameters);
        gp.processInput(2, "/call", parameters);
        assertEquals("Game is over. Enter /result to see result.", gp.processInput(2, "/raise", parameters)[1]);
    }

    @Test
    public void foldProcess() {
        parameters.add(5);
        gp.processInput(0, "/create", parameters);
        gp.processInput(0, "/join", parameters);
        gp.processInput(1, "/join", parameters);
        gp.processInput(2, "/join", parameters);
        gp.processInput(0, "/start", parameters);

        assertEquals("Player 0 folded. Now player 1\nAll funds: 15.    Current bet: 5", gp.processInput(0, "/fold", parameters)[1]);
        assertEquals("It's: 1 round. Now playing player nr: 1", gp.processInput(0, "/state", parameters)[1]);
        gp.processInput(1, "/call", parameters);
        assertEquals("It's: 1 round. Now playing player nr: 2", gp.processInput(0, "/state", parameters)[1]);
        assertEquals("Not your turn!", gp.processInput(1, "/fold", parameters)[1]);
        assertEquals("Player 2 folded. Now time to draw!\nAll funds: 20.    Current bet: 5", gp.processInput(2, "/fold", parameters)[1]);
        assertEquals("Now is time to draw. You cannot fold!", gp.processInput(0, "/fold", parameters)[1]);
    }

    @Test
    public void drawProcess() {
        parameters.add(5);
        gp.processInput(0, "/create", parameters);
        gp.processInput(0, "/join", parameters);
        gp.processInput(1, "/join", parameters);
        gp.processInput(0, "/start", parameters);
        gp.processInput(0, "/call", parameters);
        gp.processInput(1, "/call", parameters);
        assertEquals("Not your turn!", gp.processInput(2, "/draw", parameters)[1]);
        parameters.clear();
        assertEquals("Now player 1", gp.processInput(0, "/draw", parameters)[1]);
        parameters.add(1);
        parameters.add(3);
        parameters.add(1);
        parameters.add(3);
        parameters.add(1);
        parameters.add(3);
        assertEquals("You should enter max 5 cards!", gp.processInput(1, "/draw", parameters)[1]);
        parameters.remove(0);
        parameters.remove(1);
        assertEquals("Number of cards must be unique!", gp.processInput(1, "/draw", parameters)[1]);
        parameters.clear();
        parameters.add(0);
        assertEquals("End of draw. Now player 0", gp.processInput(1, "/draw", parameters)[1]);
    }

    @Test
    public void resultProcess() {
        parameters.add(5);
        gp.processInput(0, "/create", parameters);
        gp.processInput(0, "/join", parameters);
        gp.processInput(1, "/join", parameters);
        gp.processInput(0, "/start", parameters);
        gp.processInput(0, "/call", parameters);
        gp.processInput(1, "/call", parameters);
        parameters.clear();
        gp.processInput(0, "/draw", parameters);
        gp.processInput(1, "/draw", parameters);
        assertEquals("Game is not over yet", gp.processInput(1, "/result", parameters)[1]);
        gp.processInput(0, "/call", parameters);
        gp.processInput(1, "/call", parameters);
        assertEquals(3, gp.processInput(1, "/result", parameters)[1].lines().count());
    }

    @Test
    public void endProcess() {
        parameters.add(5);
        assertEquals("You can end game after all rounds!", gp.processInput(1, "/end", parameters)[1]);
        gp.processInput(0, "/create", parameters);
        gp.processInput(0, "/join", parameters);
        gp.processInput(1, "/join", parameters);
        gp.processInput(0, "/start", parameters);
        gp.processInput(0, "/call", parameters);
        gp.processInput(1, "/call", parameters);
        parameters.clear();
        gp.processInput(0, "/draw", parameters);
        gp.processInput(1, "/draw", parameters);
        gp.processInput(0, "/call", parameters);
        gp.processInput(1, "/call", parameters);
        assertEquals("Game is over. To create new enter /create.", gp.processInput(1, "/end", parameters)[1]);
        assertEquals("You must play the game to be able to call!", gp.processInput(1, "/call", parameters)[1]);
    }
}