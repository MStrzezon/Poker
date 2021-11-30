package pl.edu.agh.kis.pz1.protocol;

import pl.edu.agh.kis.pz1.cards.Card;
import pl.edu.agh.kis.pz1.game.Game;
import pl.edu.agh.kis.pz1.player.Player;
import pl.edu.agh.kis.pz1.server.Server;

import java.util.*;

/**
 * GameProtocol is a class that is responsible for client service.
 */
public class GameProtocol {
    /**
     * poker game
     */
    private Game game = null;
    /**
     * Stores message that not your turn.
     */
    private static final String NOT_YOUR_TURN = "Not your turn!";
    /**
     * Stores message that you must play the game to be able to call!.
     */
    private static final String PLAY_GAME_TO_CALL = "You must play the game to be able to call!";
    /**
     * Stores text "Player".
     */
    private static final String PLAYER = "Player ";
    /**
     * Stores text "Current bet:".
     */
    private static final String CURRENT_BET = ".    Current bet: ";
    /**
     * Stores message that game is over. Enter /result to see result.
     */
    private static final String SEE_RESULT = "Game is over. Enter /result to see result.";
    /**
     * Stores text "All funds".
     */
    private static final String ALL_FUNDS = "\nAll funds: ";
    /**
     * Stores message that the game has not been created yet.
     */
    private static final String ENTER_TO_CREATE_GAME = "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10";

    /**
     * game state
     */
    public enum GameState {
        NOT_CREATED,
        CREATED,
        ROUND,
        DRAW,
        END
    }

    /**
     * current state of this game.
     */
    private GameState state = GameState.NOT_CREATED;

    /**
     * Responds to the client's instructions such as:
     * <ul>
     *     <li>/help</li>
     *     <li>/state</li>
     *     <li>/create</li>
     *     <li>/join</li>
     *     <li>/start</li>
     *     <li>/players</li>
     *     <li>/id</li>
     *     <li>/money</li>
     *     <li>/hand</li>
     *     <li>/call</li>
     *     <li>/raise</li>
     *     <li>/fold</li>
     *     <li>/draw</li>
     *     <li>/result</li>
     *     <li>/end</li>
     *     <li>empty message</li>
     * </ul>
     * @param userId       if of user.
     * @param move         client instruction
     * @param parameters   parameters of move/instruction
     * @return             the inscription to be displayed to the client.
     */
    public String[] processInput(int userId, String move, List<Integer> parameters) {
        switch (move) {
            case "/help" -> {
                return help();
            }
            case "/state" -> {
                return state();
            }
            case "/create" -> {
                return create(parameters);
            }
            case "/join" -> {
                return join(userId);
            }
            case "/start" -> {
                return start(userId);
            }
            case "/players" -> {
                return players();
            }
            case "/id" -> {
                return id(userId);
            }
            case "/money" -> {
                return money(userId);
            }
            case "/funds" -> {
                return funds(userId);
            }
            case "/hand" -> {
                return hand(userId);
            }
            case "/call" -> {
                return call(userId);
            }
            case "/raise" -> {
                return raise(userId, parameters);
            }
            case "/fold" -> {
                return fold(userId);
            }
            case "/draw" -> {
                return draw(userId, parameters);
            }
            case "/result" -> {
                return result();
            }
            case "/end" -> {
                return end();
            }
            case "" -> {
                return emptyMessage(userId);
            }
            default -> {
                return new String[]{"ONE", "Command not recognized. Enter /help to print all commands"};
            }
        }
    }

    /**
     * Creates help information.
     * @return  help information about available functions.
     */
    private String[] help() {
        return new String[] {"ONE", """
                IN MENU:
                    /help - print all commands.
                    /state - print who makes move.
                    /create {ante} - create game (you have to enter ante). For example: /create 5. Max ante: 5.
                    /join - join to the poker game.
                    /start - start poker game (min. 2 players).
                IN GAME:\040\040\040\040\040\040\040\040\040\040
                    /players - print number of players in game.
                    /id - your id in game.
                    /money - print your money.
                    /funds - print money on the table.
                    /hand - print all your cards.
                    /call - call a bet.
                    /raise {number} - increase the opening bet. For example: /raise 10
                    /fold - end participation in a hand.
                    /draw {cards numbers}- draw cards. For example: /draw 123 <-- it means draw cards number 1, 2, 3.
                                           If no card is given, it means that the participant does not want to draw.
                    /result - print result of the game.
                    /end - end game in order to start new.
                    ENTER - after click enter you get info about game."""};
    }

    /**
     * Support the case /state.
     * @return  <code>Game not created!</code>.
     *          <code>Game created but not started!</code>
     *          <code>It's: `current round` round. Now playing player nr: `current player`</code>
     *          <code>It's a draw time!</code>
     *          <code>Game is finished. Enter /result to show result.</code>
     */
    private String[] state() {
        if (state == GameState.NOT_CREATED) {
            return new String[]{"ONE", "Game not created!"};
        }
        if (state == GameState.CREATED) {
            return new String[]{"ONE", "Game created but not started!"};
        }
        if (state == GameState.ROUND) {
            return new String[]{"ONE", "It's: " + game.getCurrentRound() + " round. Now playing player nr: " + game.getCurrentPlayer()};
        }
        if (state == GameState.DRAW) {
            return new String[]{"ONE", "It's a draw time!"};
        }
        if (state == GameState.END) {
            return new String[]{"ONE", "Game is finished. Enter /result to show result."};
        }
        return new String[2];
    }

    /**
     * Supports the case /create.
     * @param parameters ante
     * @return           <code>Game was created! Ante: `ante`</code>.
     *                   <code>You should enter ante! Max ante: 10</code>
     *                   <code>The game was already created! Enter /join to join to the game!</code>
     */
    private String[] create(List<Integer> parameters) {
        if (state == GameState.NOT_CREATED) {
            if (parameters.size() == 1 && parameters.get(0) > 0 && parameters.get(0) <= 10) {
                game = new Game(1, parameters.get(0));
                return changeState(GameState.CREATED, "Game was created! Ante: " + parameters.get(0));
            } else {
                return new String[]{"ONE", "You should enter ante! Max ante: 10"};
            }
        } else {
            return new String[]{"ONE", "The game was already created! Enter /join to join to the game!"};
        }
    }

    /**
     * Supports the case /join.
     * @param userId id of user.
     * @return       <code>Too many participants. Max number of players: `max_number_of_players`</code>.
     *               <code>You joined the game, your number in game: `player_game_id`</code>
     *               <code>You are already join to the game</code>
     *               <code>Game already started. You cannot join</code>
     */
    private String[] join(int userId) {
        if (state == GameState.NOT_CREATED) return new String[]{"ONE",
                ENTER_TO_CREATE_GAME};
        if (state==GameState.CREATED && game.getPlayers().size() == Server.maxPlayers) {
            return new String[]{"ONE", "Too many participants. Max number of players: " + Server.maxPlayers};
        }
        if (state == GameState.CREATED && !game.isInGame(userId) && game.addPlayer(userId)) {
            return new String[]{"ONE", "You joined the game, your number in game: " + (game.getPlayers().size() - 1)};
        } else if (state == GameState.CREATED && game.isInGame(userId)) {
            return new String[]{"ONE", "You are already join to the game"};
        }
        if (state == GameState.ROUND || state == GameState.DRAW) {
            return new String[]{"ONE", "Game already started. You cannot join"};
        }
        return new String[2];
    }

    /**
     * Supports the case /start.
     * @param userId id of user.
     * @return        <code>The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10</code>
     *                <code>Wait for another player! Minimum number of players: 2</code>.
     *                <code>Game was started! Player number 0 play! All funds: `funds` + Current bet: `bet`. Your funds: `player money`</code>
     *                <code>You should first join to the game!</code>
     *                <code>Game already started!</code>
     */
    private String[] start(int userId) {
        if (state == GameState.NOT_CREATED) return new String[]{"ONE",
                ENTER_TO_CREATE_GAME};
        if (state == GameState.CREATED && game.getPlayers().size() < 2 && game.isInGame(userId)) {
            return new String[]{"ONE", "Wait for another player! Minimum number of players: 2"};
        }
        if (state == GameState.CREATED && game.getPlayers().size() >= 2 && game.isInGame(userId)) {
            state = GameState.ROUND;
            game.deal();
            return new String[]{"MORE", "Game was started! Player number 0 play!\nAll funds: " + game.getAllFunds()
                    + CURRENT_BET + game.getBet()
                    + "\nYour funds:     " + game.getPlayer(userId).getFunds()};
        }
        if (state == GameState.CREATED && !game.isInGame(userId)) {
            return new String[]{"MORE", "You should first join to the game!"};
        }
        if (state == GameState.ROUND || state == GameState.DRAW) {
            return new String[]{"ONE", "Game already started!"};
        }
        return new String[2];
    }

    /**
     * Supports the case /players.
     * @return  <code>The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10</code>
     *          <code>`number of players`</code>
     */
    private String[] players() {
        if (state == GameState.NOT_CREATED) {
            return new String[]{"ONE", "\n" +
                    ENTER_TO_CREATE_GAME};
        } else {
            return new String[]{"ONE", Integer.toString(game.getPlayers().size())};
        }
    }

    /**
     * Supports the case /id.
     * @param userId id of user.
     * @return       <code>The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10</code>
     *               <code>You should join to the game to see your id in game!</code>
     *               <code>Your game id: `player id`.</code>
     */
    private String[] id(int userId) {
        if (state == GameState.NOT_CREATED) {
            return new String[]{"ONE", ENTER_TO_CREATE_GAME};
        } else if (state == GameState.CREATED && !game.isInGame(userId)) {
            return new String[]{"ONE", "You should join to the game to see your id in game!"};
        } else {
            return new String[]{"ONE", "Your game id: " + game.getPlayers().indexOf(game.getPlayer(userId))};
        }
    }

    /**
     * Supports the case /money.
     * @param userId id of user.
     * @return       <code>The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10</code>
     *               <code>You should join to the game to see your money in game!</code>
     *               <code>Money: `player money`</code>
     */
    private String[] money(int userId) {
        if (state == GameState.NOT_CREATED) {
            return new String[]{"ONE", ENTER_TO_CREATE_GAME};
        } else if (!game.isInGame(userId)) {
            return new String[]{"ONE", "You should join to the game to see your money in game!"};
        } else {
            return new String[]{"ONE", "Money: " + game.getPlayer(userId).getFunds() };
        }
    }

    /**
     * Supports the case /funds.
     * @param userId id of user.
     * @return       <code>The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10</code>
     *               <code>You should join to the game to see your money in game!</code>
     *               <code>Funds: `money on the table`</code>
     */
    private String[] funds(int userId) {
        if (state == GameState.NOT_CREATED) {
            return new String[]{"ONE", ENTER_TO_CREATE_GAME};
        } else if (!game.isInGame(userId)) {
            return new String[]{"ONE", "You should join to the game to see your money in game!"};
        } else {
            return new String[]{"ONE", "Funds: " + game.getAllFunds() };
        }
    }

    /**
     * Supports the case /hand.
     * @param userId id of user.
     * @return       <code>You must play the game to display your deck of cards!</code>
     *               <code>`cards`</code>
     *               <code>You are not playing the game. Wait for the end of game!</code>
     */
    private String[] hand(int userId) {
        if (state == GameState.NOT_CREATED || state == GameState.CREATED)
            return new String[]{"ONE", "You must play the game to display your deck of cards!"};
        if (state == GameState.ROUND || state == GameState.DRAW || state == GameState.END) {
            if (game.isInGame(userId)) {
                StringBuilder hand = new StringBuilder();
                int card_counter = 0;
                for (Card card : game.getPlayer(userId).getHand().getCards()) {
                    hand.append(card_counter++).append(": ").append(card.toString()).append("\n");
                }
                return new String[]{"ONE", hand.toString()};
            }
            if (!game.isInGame(userId)) {
                return new String[]{"ONE", "You are not playing the game. Wait for the end of game!"};
            }
        }
        return new String[2];
    }

    /**
     * Supports the case /call
     * @param userId id of user.
     * @return <code>You must play the game to be able to call!</code>
     *         <code>round_call(userId)</code>
     *         <code>Now is time to draw. You cannot call!</code>
     *         <code>Game is over. Enter /result to see result.</code>
     */
    private String[] call(int userId) {
        if (state == GameState.NOT_CREATED || state == GameState.CREATED) return new String[]{"ONE", PLAY_GAME_TO_CALL};
        if (state == GameState.ROUND) return round_call(userId);
        if (state == GameState.DRAW) return new String[]{"ONE", "Now is time to draw. You cannot call!"};
        if (state == GameState.END) return new String[]{"ONE", SEE_RESULT};
        return new String[2];
    }

    /**
     * Calls
     * @param userId id of user.
     * @return       <code>updateStateCall(previousPlayer, previousRound)</code>
     *               <code>You don't have money!</code>
     *               <code>Not your turn!</code>
     */
    private String[] round_call(int userId) {
        int playerIndex = game.getPlayers().indexOf(game.getPlayer(userId));
        if (game.getCurrentPlayer() == playerIndex) {
            int previousPlayer = game.getCurrentPlayer();
            int previousRound = game.getCurrentRound();
            if (game.makeAMove(1, 0, game.getPlayers().get(playerIndex))) {
                return updateStateCall(previousPlayer, previousRound);
            } else return new String[]{"ONE", "You don't have money!"};
        } else {
            return new String[]{"ONE", NOT_YOUR_TURN};
        }
    }

    /**
     * Changes state after call/
     * @param previousPlayer previous player
     * @param previousRound  previous round
     * @return               <code>stateToDraw(previousPlayer, " called. Now time to draw!\nAll funds: ")</code>
     *                       <code>stateToEnd(GameState.END, SEE_RESULT)</code>
     *                       <code>PLAYER `previousPlayer` called. Now player `current player` ALL_FUNDS 'funds'
     *                             CURRENT_BET `bet`</code>
     */
    private String[] updateStateCall(int previousPlayer, int previousRound) {
        if (game.getCurrentRound() == previousRound +1 && previousRound ==1) return stateToDraw(previousPlayer, " called. Now time to draw!\nAll funds: ");
        else if (game.getCurrentRound() == previousRound +1 && previousRound ==2) {
            return changeState(GameState.END, SEE_RESULT);
        }
        return new String[]{"MORE", PLAYER + previousPlayer + " called. Now player " + game.getCurrentPlayer() + ALL_FUNDS + game.getAllFunds()
                + CURRENT_BET + game.getBet()};
    }

    /**
     * Supports the case /raise
     * @param userId     id of user
     * @param parameters new bet
     * @return           <code>You must play the game to be able to raise!</code>
     *                   <code>round_raise(userId, parameters)</code>
     *                   <code>Now is time to draw. You cannot raise!</code>
     *                   <code>Game is over. Enter /result to see result.</code>
     */
    private String[] raise(int userId, List<Integer> parameters) {
        if (state==GameState.NOT_CREATED || state == GameState.CREATED) return new String[]{"ONE", "You must play the game to be able to raise!"};
        if (state==GameState.ROUND) return round_raise(userId, parameters);
        if (state == GameState.DRAW) return new String[]{"ONE", "Now is time to draw. You cannot raise!"};
        if (state == GameState.END) return new String[]{"ONE", SEE_RESULT};
        return new String[2];
    }
    /**
     * Raises
     * @param userId id of user.
     * @param parameters new bet
     * @return           <code>Enter new wage!</code>
     *                   <code>updateStateRaise(previousPlayer, previousRound)</code>
     *                   <code>You should raise bet or you don't have money!</code>
     *                   <code>Not your turn!</code>
     */
    private String[] round_raise(int userId, List<Integer> parameters) {
        int playerIndex = game.getPlayers().indexOf(game.getPlayer(userId));
        if (game.getCurrentPlayer() == playerIndex) {
            int previousPlayer = game.getCurrentPlayer();
            int previousRound = game.getCurrentRound();
            if (parameters.size() == 0) return new String[]{"ONE", "Enter new wage!"};
            if (game.makeAMove(2, parameters.get(0), game.getPlayers().get(playerIndex))) {
                return updateStateRaise(previousPlayer, previousRound);
            } else return new String[]{"ONE", "You should raise bet or you don't have money!"};
        } else {
            return new String[]{"ONE", NOT_YOUR_TURN};
        }
    }

    /**
     * Changes state after raise
     * @param previousPlayer previous player
     * @param previousRound  previous round
     * @return               <code>stateToDraw(previousPlayer, " called. Now time to draw!\nAll funds: ")</code>
     *                       <code>stateToEnd(GameState.END, SEE_RESULT)</code>
     *                       <code>PLAYER `previousPlayer` raised. Now player `current player` ALL_FUNDS 'funds'
     *                             CURRENT_BET `bet`</code>
     */
    private String[] updateStateRaise(int previousPlayer, int previousRound) {
        if (game.getCurrentRound() == previousRound+1) {
            return stateToDraw(previousPlayer, " raised. Now time to draw!\nAll funds: ");
        } else if (game.getCurrentRound() == previousRound+1 && previousRound==2) {
            return changeState(GameState.END, SEE_RESULT);
        }
        return new String[]{"MORE", PLAYER + previousPlayer + " raised. Now player "+ game.getCurrentPlayer() + ALL_FUNDS
                + game.getAllFunds()
                + CURRENT_BET + game.getBet()};
    }

    /**
     * Supports the case /fold
     * @param userId id of user
     * @return       <code>You must play the game to be able to fold!</code>
     *               <code>stateToDraw(previousPlayer, " folded. Now time to draw!\nAll funds: ")</code>
     *               <code>stateToEnd(GameState.END, SEE_RESULT)</code>
     *               <code>PLAYER `previousPlayer` folded. Now player `current player` ALL_FUNDS 'funds'
     *                     CURRENT_BET `bet`</code>
     *               <code>Not your turn!</code>
     *               <code>Now is time to draw. You cannot fold!</code>
     *               <code>Game is over. You cannot fold!</code>
     */
    private String[] fold(int userId) {
        if (state == GameState.NOT_CREATED || state == GameState.CREATED)
            return new String[]{"ONE", "You must play the game to be able to fold!"};
        if (state == GameState.ROUND) {
            int playerIndex = game.getPlayers().indexOf(game.getPlayer(userId));
            if (game.getCurrentPlayer() == playerIndex) {
                int previousPlayer = game.getCurrentPlayer();
                int previousRound = game.getCurrentRound();
                game.makeAMove(3, 0, game.getPlayers().get(playerIndex));
                if (game.getCurrentRound() == previousRound+1 && previousRound==1) {
                    return stateToDraw(previousPlayer, " folded. Now time to draw!\nAll funds: ");
                }
                else if (game.getCurrentRound() == previousRound+1 && previousRound==2) {
                    return changeState(GameState.END, SEE_RESULT);
                }
                return new String[]{"MORE", PLAYER + previousPlayer + " folded. Now player " +
                        game.getCurrentPlayer() + ALL_FUNDS + game.getAllFunds()
                        + CURRENT_BET + game.getBet()};
            } else {
                return new String[]{"ONE", NOT_YOUR_TURN};
            }
        }
        if (state == GameState.DRAW) return new String[]{"ONE", "Now is time to draw. You cannot fold!"};
        if (state == GameState.END) return new String[]{"ONE", "Game is over. You cannot fold!"};
        return new String[2];
    }

    /**
     * Changes state to draw with additional information.
     * @param previousPlayer previous player
     * @param s              string to print
     * @return               <code>PLAYER + previousPlayer + s
     *                             + game.getAllFunds()
     *                             + CURRENT_BET + game.getBet()</code>
     */
    private String[] stateToDraw(int previousPlayer, String s) {
        state = GameState.DRAW;
        return new String[]{"MORE", PLAYER + previousPlayer + s
                + game.getAllFunds()
                + CURRENT_BET + game.getBet()};
    }

    /**
     * Suports the case /draw
     * @param userId     id of user
     * @param parameters cards to draw
     * @return           <code>You must play the game to be able to draw!</code>
     *                   <code>This is not the time to draw!</code>
     *                   <code>drawDraw(userId, parameters)</code>
     *                   <code>Game is over. You cannot draw!</code>
     */
    private String[] draw(int userId, List<Integer> parameters) {
        if (state == GameState.NOT_CREATED || state==GameState.CREATED) return new String[]{"ONE", "You must play the game to be able to draw!"};
        if (state==GameState.ROUND) return new String[]{"ONE", "This is not the time to draw!"};
        if (state==GameState.DRAW) {
            return drawDraw(userId, parameters);
        }
        if (state == GameState.END) return new String[]{"ONE", "Game is over. You cannot draw!"};
        return new String[2];
    }

    /**
     * Draws with additional information
     * @param userId     id of user
     * @param parameters cards to draw
     * @return           <code>currentPlayerDraw(userId, parameters)</code>
     *                   <code>Not your turn!</code>
     */
    private String[] drawDraw(int userId, List<Integer> parameters) {
        int playerIndex = game.getPlayers().indexOf(game.getPlayer(userId));
        if (game.getCurrentDrawPlayer() == playerIndex) return currentPlayerDraw(userId, parameters);
        else return new String[]{"MORE", NOT_YOUR_TURN};
    }

    /**
     * Support drawDraw function
     * @param userId     id of user
     * @param parameters cards to draw
     * @return           <code>msgAfterDraw()</code>    
     *                   <code>Number of cards must be unique!</code>
     *                   <code>You should enter cards number from 0 to 4!</code>
     *                   <code>You should enter max 5 cards!</code>
     */
    private String[] currentPlayerDraw(int userId, List<Integer> parameters) {
        if (parameters.size()==0) {
            return msgAfterDraw();
        } else if (parameters.size() <= 5) {
            List<Card> cards = new ArrayList<>();
            Set<Integer> set = new HashSet<>(parameters);
            if(set.size() < parameters.size()){
                return new String[]{"ONE", "Number of cards must be unique!"};
            }
            for (Integer parameter : parameters) {
                if (parameter < 0 || parameter > 4) return new String[]{"ONE", "You should enter cards number from 0 to 4!"};
                cards.add(game.getPlayer(userId).getHand().getCards().get(parameter));
            }
            game.draw(cards, game.getPlayer(userId));
            return msgAfterDraw();
        } else return new String[]{"MORE", "You should enter max 5 cards!"};
    }

    /**
     * Create message after draw
     * @return <code>End of draw. Now player `current player`</code>
     *         <code>Now player `current player`</code>
     */
    private String[] msgAfterDraw() {
        if (!game.nextDrawPlayer()) {
            return changeState(GameState.ROUND, "End of draw. Now player " + game.getCurrentPlayer());
        }
        return new String[]{"MORE", "Now player " + game.getCurrentDrawPlayer()};
    }

    /**
     * Supports the case /result
     * @return <code>You should play the game to see result!</code>
     *         <code>Game is not over yet</code>
     *         <code>`result`</code>
     */
    private String[] result() {
        if (state==GameState.NOT_CREATED || state==GameState.CREATED) return new String[]{"ONE", "You should play the game to see result!"};
        if (state==GameState.ROUND || state==GameState.DRAW) return new String[]{"ONE", "Game is not over yet"};
        if (state==GameState.END) {
            StringBuilder result = new StringBuilder();
            result.append("RESULTS:\n");
            int playerCounter = 1;
            for (Player player : game.result()) {
                result.append(playerCounter++).append(") Player ").append(player.getId()).append("\n");
            }
            return new String[]{"ONE", result.toString()};
        }
        return new String[2];
    }

    /**
     * Supports the case /end
     * @return <code>You can end game after all rounds!</code>
     *         <code>Game is over. To create new enter /create.</code>
     */
    private String[] end() {
        if (state==GameState.NOT_CREATED || state==GameState.CREATED || state==GameState.ROUND || state==GameState.DRAW) return new String[]{"ONE", "You can end game after all rounds!"};
        if (state==GameState.END) {
            game = null;
            return changeState(GameState.NOT_CREATED, "Game is over. To create new enter /create.");
        }
        return new String[2];
    }

    /**
     * Changes state
     * @param newState    new state
     * @param information additional information
     * @return            <code>information</code>
     */
    private String[] changeState(GameState newState, String information) {
        state = newState;
        return new String[]{"MORE", information};
    }

    /**
     * Support the case ""
     * @param userId id of user
     * @return information about current situation
     */
    private String[] emptyMessage(int userId) {
        String message = "";
        if (state==GameState.NOT_CREATED) message = "Game not created yet";
        if (state==GameState.CREATED) {
            message = "Game ID: " + game.getId() + ". Number of players: " + game.getPlayers().size() + "\n" +
                    "All funds: " + game.getAllFunds() + ". Your funds: " + game.getPlayer(userId).getFunds() + "\n";
        }
        if (state==GameState.ROUND) {
            message += "State: ROUND. Player: " + game.getCurrentPlayer() + ". Round: " + game.getCurrentRound() + "\n";
        }
        if (state==GameState.DRAW) {
            message += "State: DRAW. Player: " + game.getCurrentPlayer() + "\n";
        }
        if (state==GameState.END) {
            message += "State: END.\n";
        }
        return new String[] {"ONE", message};
    }
}
