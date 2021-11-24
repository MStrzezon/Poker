package pl.edu.agh.kis.pz1.protocol;

import pl.edu.agh.kis.pz1.cards.Card;
import pl.edu.agh.kis.pz1.game.Game;

import java.util.List;

public class GameProtocol {
    private Game game = null;

    public enum GameStatus {
        NOT_CREATED,
        CREATED,
        ROUND,
        DRAW,
        END;
    }
    private static int counter = 0;

    private GameStatus state = GameStatus.NOT_CREATED;

    public String[] processInput(int userId, String move, List<Integer> parameters) {
        switch (move) {
            case "/help" -> {
                return new String[]{"ONE", help()};
            }
            case "/status" -> {
                if (state==GameStatus.NOT_CREATED) {
                    return new String[]{"ONE", "Game not created!"};
                }
                if (state==GameStatus.CREATED) {
                    return new String[]{"ONE", "Game created but not started!"};
                }
                if (state==GameStatus.ROUND) {
                    return new String[]{"ONE", "It's: "+game.getCurrentRound()+" round. Now playing player nr: "+game.getCurrentPlayer()};
                }
                if (state==GameStatus.DRAW) {
                    return new String[]{"ONE", "It's a draw time!"};
                }
//                if (state == ROUND && game.getPlayers().indexOf(game.getPlayer(playerId)) == game.getCurrentPlayer()) {
//                    return new String[]{"ONE", "Your turn!"};
//                }
//                if (state == ROUND && game.getPlayers().indexOf(game.getPlayer(playerId)) != game.getCurrentPlayer()) {
//                    return new String[]{"ONE", "Player number "+Integer.toString(game.getCurrentPlayer())};
//                }
//                if (state == DRAW) {
//                    return new String[]{"ONE", "It's draw time!"};
//                }
                if (state==GameStatus.END) {
                    return new String[]{"ONE", "Game is finished. Enter /result to show results."};
                }
            }
            case "/create" -> {
                if (state==GameStatus.NOT_CREATED) {
                    if (parameters.size() == 1 && parameters.get(0) > 0 && parameters.get(0) < 25) {
                        game = new Game(counter++, parameters.get(0));
                        state = GameStatus.CREATED;
                        return new String[]{"MORE", "Game was created! Ante: "+parameters.get(0)};
                    } else {
                        return new String[]{"ONE", "You should enter ante! Max ante: 25"};
                    }
                } else {
                    return new String[]{"ONE", "The game was already created! Enter /join to join to the game!"};
                }
            }
            case "/join" -> {
                if (state==GameStatus.NOT_CREATED) return new String[]{"ONE",
                        "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 25"};
                if (state==GameStatus.CREATED && !game.isInGame(userId) && game.addPlayer(userId)) {
                    return new String[]{"ONE", "You joined the game, your number in game: "+ (game.getPlayers().size() - 1)};
                } else if (state==GameStatus.CREATED && !game.isInGame(userId) && !game.addPlayer(userId)) {
                    return new String[]{"ONE", "Too many participants. You cannot join to this game"};
                } else if (state==GameStatus.CREATED && game.isInGame(userId)) {
                    return new String[]{"ONE", "You are already join to the game"};
                }
                if (state==GameStatus.ROUND || state==GameStatus.DRAW) {
                    return new String[]{"ONE", "Game already started. You cannot join"};
                }
            }
            case "/start" -> {
                if (state==GameStatus.NOT_CREATED) return new String[]{"ONE",
                        "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 25"};
                if (state==GameStatus.CREATED && game.getPlayers().size() < 2 && game.isInGame(userId)) {
                    return new String[]{"ONE", "Wait for another player! Minimum number of players: 2"};
                }
                if (state==GameStatus.CREATED && game.getPlayers().size() >= 2 && game.isInGame(userId)) {
                    state=GameStatus.ROUND;
                    game.deal();
                    return new String[]{"MORE", "Game was started! Player number 0 play!\nAll funds: "+Integer.toString(game.getAllFunds())
                            +".    Current bet: "+ Integer.toString(game.getBet())
                            +"\nYour funds:     "+Integer.toString(game.getPlayer(userId).getFunds())};
                }
                if (state==GameStatus.CREATED && !game.isInGame(userId)) {
                    return new String[]{"MORE", "You should first join to the game!"};
                }
                if (state==GameStatus.ROUND || state==GameStatus.DRAW) {
                    return new String[]{"ONE", "Game already started!"};
                }
            }
            case "/players" -> {
                if (state==GameStatus.NOT_CREATED) {
                    return new String[]{"ONE", "\n" +
                            "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 25"};
                } else {
                    return new String[]{"ONE", Integer.toString(game.getPlayers().size())};
                }
            }
            case "/id" -> {
                if (state==GameStatus.NOT_CREATED) {
                    return new String[]{"ONE", "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 25"};
                } else if(state==GameStatus.CREATED && !game.isInGame(userId)) {
                    return new String[]{"ONE", "You should join to the game to see your id in game!"};
                } else {
                    return new String[]{"ONE", "Your game id: "+Integer.toString(game.getPlayers().indexOf(game.getPlayer(userId)))};
                }
            }
            case "/hand" -> {
                if (state==GameStatus.NOT_CREATED || state==GameStatus.CREATED) return new String[]{"ONE", "You must play the game to display your deck of cards!"};
                if (state==GameStatus.ROUND || state==GameStatus.DRAW) {
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
//            case "/call" -> {
//                int playerIndex = game.getPlayers().indexOf(game.getPlayer(playerId));
//                if (state == ROUND && game.getCurrentPlayer()==playerIndex) {
//                    int current = game.getCurrentPlayer();
//                    game.makeAMove(1, parameters.get(0), game.getPlayers().get(playerIndex));
//                    if (game.getCurrentPlayer() == 0) {
//                        state = DRAW;
//                        return new String[]{"MORE", "Player "+Integer.toString(current)+" called.\n" +"Now time to draw!\nAll funds: "
//                                +Integer.toString(game.getAllFunds())
//                                +".    Current bet: "+ Integer.toString(game.getBet())};
//                    }
//                    return new String[]{"MORE", "Player "+Integer.toString(current)+" called.\n" +
//                            "Now player "+Integer.toString(game.getCurrentPlayer())+"\nAll funds: "+Integer.toString(game.getAllFunds())
//                            +".    Current bet: "+ Integer.toString(game.getBet())};
//                } else {
//                    return new String[]{"ONE", "This move is forbidden!"};
//                }
//            }
//            case "/raise" -> {
//                int playerIndex = game.getPlayers().indexOf(game.getPlayer(playerId));
//                if (state == ROUND && game.getCurrentPlayer()==playerIndex) {
//                    if (parameters.get(0)==-1) return new String[]{"ONE", "In raise enter wage!"};
//                    int current = game.getCurrentPlayer();
//                    if(!game.makeAMove(2, parameters.get(0), game.getPlayers().get(playerIndex))) {
//                        return new String[]{"ONE", "You should raise bet!"};
//                    }
//                    if (game.getCurrentPlayer() == 0) {
//                        state = DRAW;
//                        return new String[]{"MORE", "Player "+Integer.toString(current)+" raised.\n" +"Now time to draw!\nAll funds: "
//                                +Integer.toString(game.getAllFunds())
//                                +".    Current bet: "+ Integer.toString(game.getBet())};
//                    }
//                    return new String[]{"MORE", "Player "+Integer.toString(current)+" raised.\nNow player "+
//                            Integer.toString(game.getCurrentPlayer())+"\nAll funds: "+Integer.toString(game.getAllFunds())
//                            +".    Current bet: "+ Integer.toString(game.getBet())};
//                } else {
//                    return new String[]{"ONE", "This move is forbidden!"};
//                }
//            }
//            case "/fold" -> {
//                int playerIndex = game.getPlayers().indexOf(game.getPlayer(playerId));
//                if (state == ROUND && game.getCurrentPlayer()==playerIndex) {
//                    int current = game.getCurrentPlayer();
//                    game.makeAMove(2, parameters.get(0), game.getPlayers().get(playerIndex));
//                    if (game.getCurrentPlayer() == 0) {
//                        state = DRAW;
//                        return new String[]{"MORE", "Time to draw!"};
//                    }
//                    return new String[]{"MORE", "Player " + Integer.toString(current) + " fold.\nNow player " +
//                            Integer.toString(game.getCurrentPlayer())+"\nAll funds: "+Integer.toString(game.getAllFunds())
//                            +".    Current bet: "+ Integer.toString(game.getBet())};
//                } else {
//                    return new String[]{"ONE", "This move is forbidden!"};
//                }
//            }
//            case "/draw" -> {
//                if (state == DRAW) {
//                    return new String[]{"ONE", "Choose cards to draw!"};
//                }
//                return new String[]{"ONE", "This move is forbidden!"};
//            }
//            case "/result" -> {
//                if(!game.isFinish()) {
//                    return new String[]{"ONE", "Game not finished yet"};
//                } else {
//                    return new String[]{"ONE", "Waiiiitt"};
//                }
//            }
        }
        return new String[]{"ONE", "Command not recognized. Enter /help to print all commands"};
    }


    public String help() {
        String help_message = """
                ------------------------------------------------------------------------------------------------------
                IN MENU:
                    /help - print all commands.
                    /status - print who makes move.
                    /create {ante} - create game (you have to enter ante). For example: /create 5.
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
                ------------------------------------------------------------------------------------------------------""";
        return help_message;
    }

}
