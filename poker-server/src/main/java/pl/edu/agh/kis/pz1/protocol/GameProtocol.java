package pl.edu.agh.kis.pz1.protocol;

import pl.edu.agh.kis.pz1.cards.Card;
import pl.edu.agh.kis.pz1.game.Game;

import java.util.List;

public class GameProtocol {
    private Game game = new Game(1, 5);
    private final int WAITING = 1;
    private final int ROUND = 2;
    private final int DRAW = 3;
    private final int END = 4;

    private int state = WAITING;

    public String[] processInput(int playerId, String input, List<Integer> numbers) {
        switch (input) {
            case "/help" -> {
                return new String[]{"ONE", help()};
            }
            case "/players" -> {
                return new String[]{"ONE", numberOfPlayers()};
            }
            case "/join" -> {
                if (game.getPlayer(playerId) == null && game.addPlayer(playerId)) {
                    return new String[]{"ONE", "You joined the game, your number in game: "+ (game.getPlayers().size() - 1)};
                } else if (game.getPlayer(playerId) != null) {
                    return new String[]{"ONE", "You are joined the game"};
                }
                return new String[]{"ONE", "Too many person in game"};
            }
            case "/start" -> {
                if (game.getPlayer(playerId) == null) {
                    return new String[]{"ONE", "You have to join to the game"};
                }
                if (game.getPlayers().size() < 2) {
                    return new String[]{"ONE", "Wait for another player!"};
                }
                state = ROUND;
                game.deal();
                return new String[]{"MORE", "Game was started! Player number 0 play!\nAll funds: "+Integer.toString(game.getAllFunds())
                +".    Current bet: "+ Integer.toString(game.getBet())};
            }
            case "/hand" -> {
                if (state==ROUND || state==DRAW) {
                    StringBuilder cards = new StringBuilder();
                    for (Card card : game.getPlayer(playerId).getHand().getCards()) {
                        cards.append(card.toString()).append("\n");
                    }
                    return new String[]{"ONE", cards.toString()};
                }
                return new String[]{"ONE", "Game not started!"};
            }
            case "/status" -> {
                if (state==WAITING) {
                    return new String[]{"ONE", "Game not started!"};
                }
                if (state == ROUND && game.getPlayers().indexOf(game.getPlayer(playerId)) == game.getCurrentPlayer()) {
                    return new String[]{"ONE", "Your turn!"};
                }
                if (state == ROUND && game.getPlayers().indexOf(game.getPlayer(playerId)) != game.getCurrentPlayer()) {
                    return new String[]{"ONE", "Player number "+Integer.toString(game.getCurrentPlayer())};
                }
                if (state == DRAW) {
                    return new String[]{"ONE", "It's draw time"};
                }
                if (game.isFinish()) {
                    return new String[]{"ONE", "Game is finished. Enter /result to show results."};
                }
            }
            case "/result" -> {
                if(!game.isFinish()) {
                    return new String[]{"ONE", "Game not finished yet"};
                } else {
                    return new String[]{"ONE", "Waiiiitt"};
                }
            }
            case "/call" -> {
                if (state == ROUND && game.getCurrentPlayer()==game.getPlayers().indexOf(game.getPlayer(playerId))) {
                    game.getPlayer(playerId).call();
                    int current = game.getCurrentPlayer();
                    game.nextPlayer();
                    if (game.getCurrentPlayer() == 0) {
                        state = DRAW;
                        return new String[]{"MORE", "Time to draw!"};
                    }
                    return new String[]{"MORE", "Player "+Integer.toString(current)+" called.\n" +
                            "Now player "+Integer.toString(game.getCurrentPlayer())+"\nAll funds: "+Integer.toString(game.getAllFunds())
                            +".    Current bet: "+ Integer.toString(game.getBet())};
                } else {
                    return new String[]{"ONE", "This move is forbidden!"};
                }
            }
            case "/raise" -> {
                if (state == ROUND && game.getCurrentPlayer()==game.getPlayers().indexOf(game.getPlayer(playerId))) {
                    if (numbers.get(0)==-1) return new String[]{"ONE", "In raise enter wage!"};
                    game.getPlayer(playerId).raise(numbers.get(0));
                    int current = game.getCurrentPlayer();
                    game.nextPlayer();
                    if (game.getCurrentPlayer() == 0) {
                        state = DRAW;
                        return new String[]{"MORE", "Time to draw!"};
                    }
                    return new String[]{"MORE", "Player "+Integer.toString(current)+" raised.\nNow player "+
                            Integer.toString(game.getCurrentPlayer())+"\nAll funds: "+Integer.toString(game.getAllFunds())
                            +".    Current bet: "+ Integer.toString(game.getBet())};
                } else {
                    return new String[]{"ONE", "This move is forbidden!"};
                }
            }
            case "/fold" -> {
                if (state == ROUND && game.getCurrentPlayer()==game.getPlayers().indexOf(game.getPlayer(playerId))) {
                    game.getPlayer(playerId).fold();
                    int current = game.getCurrentPlayer();
                    game.nextPlayer();
                    if (game.getCurrentPlayer() == 0) {
                        state = DRAW;
                        return new String[]{"MORE", "Time to draw!"};
                    }
                    return new String[]{"MORE", "Player " + Integer.toString(current) + " fold.\nNow player " +
                            Integer.toString(game.getCurrentPlayer())+"\nAll funds: "+Integer.toString(game.getAllFunds())
                            +".    Current bet: "+ Integer.toString(game.getBet())};
                } else {
                    return new String[]{"ONE", "This move is forbidden!"};
                }
            }
            case "/draw" -> {
                if (state == DRAW) {
                    return new String[]{"ONE", "Choose cards to draw!"};
                }
                return new String[]{"ONE", "This move is forbidden!"};
            }
        }
        return new String[]{"ONE", "Command not recognized. Enter /help to print all commands"};
    }


    public String help() {
        String help_message = """
                ------------------------------------------------
                IN MENU:
                    /help - print all commands.
                    /players - print number of players.
                    /start - start poker game (min. 2 players).
                IN GAME:          
                    /number - your number in game.
                    /hand - print all your cards.
                    /status - print who makes move.
                    /call - call a bet. For example: 
                    /raise {number} - increase the opening bet. For example: /raise 10
                    /fold - end participation in a hand.
                    /draw {cards numbers}- draw cards. For example: /draw 123 <-- it means draw cards number 1, 2, 3.
                ------------------------------------------------""";
        return help_message;
    }

    public String numberOfPlayers() {
        int numberOfPlayers = game.getPlayers().size();
        return Integer.toString(numberOfPlayers);
    }
}
