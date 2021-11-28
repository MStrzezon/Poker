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
     *
     */
    private static final String NOT_YOUR_TURN = "Not your turn!";
    private static final String PLAY_GAME_TO_CALL = "You must play the game to be able to call!";
    private static final String PLAYER = "Player ";
    private static final String CURRENT_BET = ".    Current bet: ";
    private static final String SEE_RESULT = "Game is over. Enter /result to see result.";
    private static final String ALL_FUNDS = "\nAll funds: ";
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
                return fold(userId, parameters);
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
     * Informs about current state.
     * @return  <code>Game not created!</code>.
     *          <code>Game created but not started!</code>
     *          <code>It's: ``current round`` round. Now playing player nr: ``current player```</code>
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
//TODO create docs below
    /**
     * Creates a game.
     * @param parameters ante
     * @return information about execution of the creating a game.
     */
    private String[] create(List<Integer> parameters) {
        if (state == GameState.NOT_CREATED) {
            if (parameters.size() == 1 && parameters.get(0) > 0 && parameters.get(0) <= 10) {
                game = new Game(1, parameters.get(0));
                return stateToEnd(GameState.CREATED, "Game was created! Ante: " + parameters.get(0));
            } else {
                return new String[]{"ONE", "You should enter ante! Max ante: 10"};
            }
        } else {
            return new String[]{"ONE", "The game was already created! Enter /join to join to the game!"};
        }
    }

    /**
     * Joins to the game
     * @param userId id of user.
     * @return       information about execution of the joining to the game.
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

    private String[] players() {
        if (state == GameState.NOT_CREATED) {
            return new String[]{"ONE", "\n" +
                    ENTER_TO_CREATE_GAME};
        } else {
            return new String[]{"ONE", Integer.toString(game.getPlayers().size())};
        }
    }

    private String[] id(int userId) {
        if (state == GameState.NOT_CREATED) {
            return new String[]{"ONE", ENTER_TO_CREATE_GAME};
        } else if (state == GameState.CREATED && !game.isInGame(userId)) {
            return new String[]{"ONE", "You should join to the game to see your id in game!"};
        } else {
            return new String[]{"ONE", "Your game id: " + game.getPlayers().indexOf(game.getPlayer(userId))};
        }
    }

    private String[] money(int userId) {
        if (state == GameState.NOT_CREATED) {
            return new String[]{"ONE", ENTER_TO_CREATE_GAME};
        } else if (!game.isInGame(userId)) {
            return new String[]{"ONE", "You should join to the game to see your money in game!"};
        } else {
            return new String[]{"ONE", "Money: " + game.getPlayer(userId).getFunds() };
        }
    }

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

    private String[] call(int userId) {
        if (state == GameState.NOT_CREATED || state == GameState.CREATED) return new String[]{"ONE", PLAY_GAME_TO_CALL};
        if (state == GameState.ROUND) return round_call(userId);
        if (state == GameState.DRAW) return new String[]{"ONE", "Now is time to draw. You cannot call!"};
        if (state == GameState.END) return new String[]{"ONE", SEE_RESULT};
        return new String[2];
    }

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

    private String[] updateStateCall(int previousPlayer, int previousRound) {
        if (game.getCurrentRound() == previousRound +1 && previousRound ==1) return stateToDraw(previousPlayer, " called. Now time to draw!\nAll funds: ");
        else if (game.getCurrentRound() == previousRound +1 && previousRound ==2) {
            return stateToEnd(GameState.END, SEE_RESULT);
        }
        return new String[]{"MORE", PLAYER + previousPlayer + " called. Now player " + game.getCurrentPlayer() + ALL_FUNDS + game.getAllFunds()
                + CURRENT_BET + game.getBet()};
    }

    private String[] raise(int userId, List<Integer> parameters) {
        if (state==GameState.NOT_CREATED || state == GameState.CREATED) return new String[]{"ONE", PLAY_GAME_TO_CALL};
        if (state==GameState.ROUND) return round_raise(userId, parameters);
        if (state == GameState.DRAW) return new String[]{"ONE", "Now is time to draw. You cannot raise!"};
        if (state == GameState.END) return new String[]{"ONE", SEE_RESULT};
        return new String[2];
    }

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

    private String[] updateStateRaise(int previousPlayer, int previousRound) {
        if (game.getCurrentRound() == previousRound+1) {
            return stateToDraw(previousPlayer, " raised. Now time to draw!\nAll funds: ");
        } else if (game.getCurrentRound() == previousRound+1 && previousRound==2) {
            return stateToEnd(GameState.END, SEE_RESULT);
        }
        return new String[]{"MORE", PLAYER + previousPlayer + " raised. Now player "+ game.getCurrentPlayer() + ALL_FUNDS
                + game.getAllFunds()
                + CURRENT_BET + game.getBet()};
    }

    private String[] fold(int userId, List<Integer> parameters) {
        if (state == GameState.NOT_CREATED || state == GameState.CREATED)
            return new String[]{"ONE", PLAY_GAME_TO_CALL};
        if (state == GameState.ROUND) {
            int playerIndex = game.getPlayers().indexOf(game.getPlayer(userId));
            if (game.getCurrentPlayer() == playerIndex) {
                int previousPlayer = game.getCurrentPlayer();
                int previousRound = game.getCurrentRound();
                game.makeAMove(3, parameters.get(0), game.getPlayers().get(playerIndex));
                if (game.getCurrentRound() == previousRound+1) {
                    return stateToDraw(previousPlayer, " folded. Now time to draw!\nAll funds: ");
                }
                else if (game.getCurrentRound() == previousRound+1 && previousRound==2) {
                    return stateToEnd(GameState.END, SEE_RESULT);
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

    private String[] stateToDraw(int previousPlayer, String s) {
        state = GameState.DRAW;
        return new String[]{"MORE", PLAYER + previousPlayer + s
                + game.getAllFunds()
                + CURRENT_BET + game.getBet()};
    }

    private String[] draw(int userId, List<Integer> parameters) {
        if (state == GameState.NOT_CREATED || state==GameState.CREATED) return new String[]{"ONE", "You must play the game to be able to draw!"};
        if (state==GameState.ROUND) return new String[]{"ONE", "This is not the time to draw!"};
        if (state==GameState.DRAW) {
            return drawDraw(userId, parameters);
        }
        if (state == GameState.END) return new String[]{"ONE", "Game is over. You cannot draw!"};
        return new String[2];
    }

    private String[] drawDraw(int userId, List<Integer> parameters) {
        int playerIndex = game.getPlayers().indexOf(game.getPlayer(userId));
        if (game.getCurrentDrawPlayer() == playerIndex) return currentPlayerDraw(userId, parameters);
        else return new String[]{"MORE", NOT_YOUR_TURN};
    }

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

    private String[] msgAfterDraw() {
        if (!game.nextDrawPlayer()) {
            return stateToEnd(GameState.ROUND, "End of draw. Now player " + game.getCurrentPlayer());
        }
        return new String[]{"MORE", "Now player " + game.getCurrentDrawPlayer()};
    }

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

    private String[] end() {
        if (state==GameState.NOT_CREATED || state==GameState.CREATED || state==GameState.ROUND || state==GameState.DRAW) return new String[]{"ONE", "You can end game after all rounds!"};
        if (state==GameState.END) {
            game = null;
            return stateToEnd(GameState.NOT_CREATED, "Game is over. To create new enter /create.");
        }
        return new String[2];
    }

    private String[] stateToEnd(GameState end, String seeResult) {
        state = end;
        return new String[]{"MORE", seeResult};
    }

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
