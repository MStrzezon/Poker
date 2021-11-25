package pl.edu.agh.kis.pz1.protocol;

import pl.edu.agh.kis.pz1.cards.Card;
import pl.edu.agh.kis.pz1.game.Game;
import pl.edu.agh.kis.pz1.player.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameProtocol {
    private Game game = null;

    public enum GameState {
        NOT_CREATED,
        CREATED,
        ROUND,
        DRAW,
        END;
    }
    private static int counter = 0;

    private GameState state = GameState.NOT_CREATED;

    public String[] processInput(int userId, String move, List<Integer> parameters) {
        switch (move) {
            case "/help" -> {
                return new String[]{"ONE", help()};
            }
            case "/state" -> {
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
                    return new String[]{"ONE", "Game is finished. Enter /result to show results."};
                }
            }
            case "/create" -> {
                if (state == GameState.NOT_CREATED) {
                    if (parameters.size() == 1 && parameters.get(0) > 0 && parameters.get(0) <= 10) {
                        game = new Game(counter++, parameters.get(0));
                        state = GameState.CREATED;
                        return new String[]{"MORE", "Game was created! Ante: " + parameters.get(0)};
                    } else {
                        return new String[]{"ONE", "You should enter ante! Max ante: 10"};
                    }
                } else {
                    return new String[]{"ONE", "The game was already created! Enter /join to join to the game!"};
                }
            }
            case "/join" -> {
                if (state == GameState.NOT_CREATED) return new String[]{"ONE",
                        "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10"};
                if (state == GameState.CREATED && !game.isInGame(userId) && game.addPlayer(userId)) {
                    return new String[]{"ONE", "You joined the game, your number in game: " + (game.getPlayers().size() - 1)};
                } else if (state == GameState.CREATED && !game.isInGame(userId) && !game.addPlayer(userId)) {
                    return new String[]{"ONE", "Too many participants. You cannot join to this game"};
                } else if (state == GameState.CREATED && game.isInGame(userId)) {
                    return new String[]{"ONE", "You are already join to the game"};
                }
                if (state == GameState.ROUND || state == GameState.DRAW) {
                    return new String[]{"ONE", "Game already started. You cannot join"};
                }
            }
            case "/start" -> {
                if (state == GameState.NOT_CREATED) return new String[]{"ONE",
                        "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10"};
                if (state == GameState.CREATED && game.getPlayers().size() < 2 && game.isInGame(userId)) {
                    return new String[]{"ONE", "Wait for another player! Minimum number of players: 2"};
                }
                if (state == GameState.CREATED && game.getPlayers().size() >= 2 && game.isInGame(userId)) {
                    state = GameState.ROUND;
                    game.deal();
                    return new String[]{"MORE", "Game was started! Player number 0 play!\nAll funds: " + Integer.toString(game.getAllFunds())
                            + ".    Current bet: " + Integer.toString(game.getBet())
                            + "\nYour funds:     " + Integer.toString(game.getPlayer(userId).getFunds())};
                }
                if (state == GameState.CREATED && !game.isInGame(userId)) {
                    return new String[]{"MORE", "You should first join to the game!"};
                }
                if (state == GameState.ROUND || state == GameState.DRAW) {
                    return new String[]{"ONE", "Game already started!"};
                }
            }
            case "/players" -> {
                if (state == GameState.NOT_CREATED) {
                    return new String[]{"ONE", "\n" +
                            "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10"};
                } else {
                    return new String[]{"ONE", Integer.toString(game.getPlayers().size())};
                }
            }
            case "/id" -> {
                if (state == GameState.NOT_CREATED) {
                    return new String[]{"ONE", "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10"};
                } else if (state == GameState.CREATED && !game.isInGame(userId)) {
                    return new String[]{"ONE", "You should join to the game to see your id in game!"};
                } else {
                    return new String[]{"ONE", "Your game id: " + Integer.toString(game.getPlayers().indexOf(game.getPlayer(userId)))};
                }
            }
            case "/hand" -> {
                if (state == GameState.NOT_CREATED || state == GameState.CREATED)
                    return new String[]{"ONE", "You must play the game to display your deck of cards!"};
                if (state == GameState.ROUND || state == GameState.DRAW) {
                    if (game.isInGame(userId)) {
                        StringBuilder hand = new StringBuilder();
                        int card_counter = 1;
                        for (Card card : game.getPlayer(userId).getHand().getCards()) {
                            hand.append(Integer.toString(card_counter)).append(": ").append(card.toString()).append("\n");
                        }
                        return new String[]{"ONE", hand.toString()};
                    }
                    if (!game.isInGame(userId)) {
                        return new String[]{"ONE", "You are not playing the game. Wait for the end of game!"};
                    }
                }
            }
            case "/call" -> {
                if (state == GameState.NOT_CREATED || state == GameState.CREATED)
                    return new String[]{"ONE", "You must play the game to be able to call!"};
                if (state == GameState.ROUND) {
                    int playerIndex = game.getPlayers().indexOf(game.getPlayer(userId));
                    if (game.getCurrentPlayer() == playerIndex) {
                        int previousPlayer = game.getCurrentPlayer();
                        int previousRound = game.getCurrentRound();
                        if (game.makeAMove(1, 0, game.getPlayers().get(playerIndex))) {
                            if (game.getCurrentRound() == previousRound+1 && previousRound==1) {
                                state = GameState.DRAW;
                                return new String[]{"MORE", "Player " + Integer.toString(previousPlayer) + " called. Now time to draw!\nAll funds: "
                                        + Integer.toString(game.getAllFunds())
                                        + ".    Current bet: " + Integer.toString(game.getBet())};
                            } else if (game.getCurrentRound() == previousRound+1 && previousRound==2) {
                                state=GameState.END;
                                return new String[]{"MORE", "Game is over. Enter /results to see results."};
                            }
                            return new String[]{"MORE", "Player " + Integer.toString(previousPlayer) + " called. Now player " + Integer.toString(game.getCurrentPlayer()) + "\nAll funds: " + Integer.toString(game.getAllFunds())
                                    + ".    Current bet: " + Integer.toString(game.getBet())};
                        } else return new String[]{"ONE", "You don't have money!"};
                    } else {
                        return new String[]{"ONE", "Not your turn!"};
                    }
                }
                if (state == GameState.DRAW) return new String[]{"ONE", "Now is time to draw. You cannot call!"};
                if (state == GameState.END) return new String[]{"ONE", "Game is over. You cannot call!"};
            }
            case "/raise" -> {
                if (state==GameState.NOT_CREATED || state == GameState.CREATED)
                    return new String[]{"ONE", "You must play the game to be able to call!"};
                if (state==GameState.ROUND) {
                    int playerIndex = game.getPlayers().indexOf(game.getPlayer(userId));
                    if (game.getCurrentPlayer() == playerIndex) {
                        int previousPlayer = game.getCurrentPlayer();
                        int previousRound = game.getCurrentRound();
                        if (parameters.size() == 0) return new String[]{"ONE", "Enter new wage!"};
                        if (game.makeAMove(2, parameters.get(0), game.getPlayers().get(playerIndex))) {
                            if (game.getCurrentRound() == previousRound+1) {
                                state = GameState.DRAW;
                                return new String[]{"MORE", "Player " + Integer.toString(previousPlayer) + " raised. Now time to draw!\nAll funds: "
                                        + Integer.toString(game.getAllFunds())
                                        + ".    Current bet: " + Integer.toString(game.getBet())};
                            } else if (game.getCurrentRound() == previousRound+1 && previousRound==2) {
                                state=GameState.END;
                                return new String[]{"MORE", "Game is over. Enter /results to see results."};
                            }
                            return new String[]{"MORE", "Player " + Integer.toString(previousPlayer) + " raised. Now player "+ Integer.toString(game.getCurrentPlayer()) + "\nAll funds: "
                                    + Integer.toString(game.getAllFunds())
                                    + ".    Current bet: " + Integer.toString(game.getBet())};

                        } else return new String[]{"ONE", "You should raise bet or you don't have money!"};
                    } else {
                        return new String[]{"ONE", "Not your turn!"};
                    }
                }
                if (state == GameState.DRAW) return new String[]{"ONE", "Now is time to draw. You cannot raise!"};
                if (state == GameState.END) return new String[]{"ONE", "Game is over. You cannot raise!"};
            }
            case "/fold" -> {
                if (state == GameState.NOT_CREATED || state == GameState.CREATED)
                    return new String[]{"ONE", "You must play the game to be able to call!"};
                if (state == GameState.ROUND) {
                    int playerIndex = game.getPlayers().indexOf(game.getPlayer(userId));
                    if (game.getCurrentPlayer() == playerIndex) {
                        int previousPlayer = game.getCurrentPlayer();
                        int previousRound = game.getCurrentRound();
                        game.makeAMove(3, parameters.get(0), game.getPlayers().get(playerIndex));
                        if (game.getCurrentRound() == previousRound+1) {
                            state = GameState.DRAW;
                            return new String[]{"MORE", "Player " + Integer.toString(previousPlayer) + " folded. Now time to draw!\nAll funds: "
                                    + Integer.toString(game.getAllFunds())
                                    + ".    Current bet: " + Integer.toString(game.getBet())};
                        }
                        else if (game.getCurrentRound() == previousRound+1 && previousRound==2) {
                            state=GameState.END;
                            return new String[]{"MORE", "Game is over. Enter /results to see results."};
                        }
                        return new String[]{"MORE", "Player " + Integer.toString(previousPlayer) + " folded. Now player " +
                                Integer.toString(game.getCurrentPlayer()) + "\nAll funds: " + Integer.toString(game.getAllFunds())
                                + ".    Current bet: " + Integer.toString(game.getBet())};
                    } else {
                        return new String[]{"ONE", "Not your turn!"};
                    }
                }
                if (state == GameState.DRAW) return new String[]{"ONE", "Now is time to draw. You cannot fold!"};
                if (state == GameState.END) return new String[]{"ONE", "Game is over. You cannot fold!"};
            }
            case "/draw" -> {
                if (state == GameState.NOT_CREATED || state==GameState.CREATED) return new String[]{"ONE", "You must play the game to be able to draw!"};
                if (state==GameState.ROUND) return new String[]{"ONE", "This is not the time to draw!"};
                if (state==GameState.DRAW) {
                    int playerIndex = game.getPlayers().indexOf(game.getPlayer(userId));
                    if (game.getCurrentDrawPlayer() == playerIndex) {
                        if (parameters.size()==0) {
                            if (!game.nextDrawPlayer()) {
                                state=GameState.ROUND;
                                return new String[]{"MORE", "End of draw. Now player " + game.getCurrentPlayer()};
                            }
                            return new String[]{"MORE", "Now player "+game.getCurrentDrawPlayer()};
                        } else if (parameters.size() <= 5) {
                            List<Card> cards = new ArrayList<>();
                            Set<Integer> set = new HashSet<Integer>(parameters);
                            if(set.size() < parameters.size()){
                                return new String[]{"ONE", "Number of cards must be unique!"};
                            }
                            for (Integer parameter : parameters) {
                                if (parameter < 0 || parameter > 4) return new String[]{"ONE", "You should enter cards number from 0 to 4!"};
                                cards.add(game.getPlayer(userId).getHand().getCards().get(parameter));
                            }
                            game.draw(cards, game.getPlayer(userId));
                            if (!game.nextDrawPlayer()) {
                                state=GameState.ROUND;
                                return new String[]{"MORE", "End of draw. Now player " + game.getCurrentPlayer()};
                            }
                            return new String[]{"MORE", "Now player "+game.getCurrentDrawPlayer()};
                        } else return new String[]{"MORE", "You should enter max 5 cards!"};
                    } else return new String[]{"MORE", "Not your turn!"};
                }
                if (state == GameState.END) return new String[]{"ONE", "Game is over. You cannot draw!"};
            }
            case "/result" -> {
                if (state==GameState.NOT_CREATED || state==GameState.CREATED) return new String[]{"ONE", "You should play the game to see results!"};
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
            }
            case "/end" -> {
                if (state==GameState.NOT_CREATED || state==GameState.CREATED || state==GameState.ROUND || state==GameState.DRAW) return new String[]{"ONE", "You can end game after all rounds!"};
                if (state==GameState.END) {
                    game = null;
                    state=GameState.NOT_CREATED;
                    return new String[]{"MORE", "Game is over. To create new enter /create."};
                }
            }
        }
        return new String[]{"ONE", "Command not recognized. Enter /help to print all commands"};
    }



    public String help() {
        String help_message = """
                ------------------------------------------------------------------------------------------------------
                IN MENU:
                    /help - print all commands.
                    /state - print who makes move.
                    /create {ante} - create game (you have to enter ante). For example: /create 5. Max ante: 5.
                    /join - join to the poker game.
                    /start - start poker game (min. 2 players).
                IN GAME:          
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
                ------------------------------------------------------------------------------------------------------""";
        return help_message;
    }

}
