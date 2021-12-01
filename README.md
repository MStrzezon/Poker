# SOCKET POKER GAME
## STRUKTURA PROGRAMU
* Javadoc - zawiera dokumentację projektu
* poker-client - klient gry
* poker-common - przydatne klasy
* poker-model - logika gry
* poker-server - zawiera serwer gry i protokół komunikacyjny
* sonar-cube - zawiera raport z sonar-cuber (pokrycie testami, code smells)

## ZASADY GRY:
1. Gracz na początku musi stworzyć grę podając ante.
2. Po stworzeniu gry, należy dołączyć do gry.
3. Gdy wystarczająca liczba graczy dołączy, należy wystartować grę.
4. Gracz otrzymuje 5 kart do ręki i 50 żetonów na start.
5. Rozpoczyna się runda licytacji, gdzie gracz może sprawdzić, podbić, spasować.
Runda licytacji trwa do momentu, gdy wszyscy gracze włożą tą samą ilość żetonów.
6. Po rundzie licytacji jest czas na wymianę kart, każdy gracz musi poinformować czy wymienia karty i które.
7. Po tym przechodzimy do ostatniej rundy licytacji, która rozgrywa się na takich samych zasadach jak pierwsza.
8. Po skończeniu gry, gracz może wyświetlić ranking i zobaczyć, które miejsce zajął. Należy zamknąć grę całkowicie, by móc rozpocząć nową.

## SPOSÓB URUCHOMIENIA PROGRAMU:
### Aby uruchomić program:
1) Należy uruchomić plik 'poker-server-1.0-jar-with-dependencies.jar' podając jako pierwszy argument maksymalną liczbę
graczy mogącą uczestniczyć w grze (od 2 do 4). W ten sposób uruchomimy serwer rozgrywki.
2) Należy uruchomić plik 'poker-client-1.0-jar-with-dependencies.jar'. W ten sposób będziemy mogli grać w pokera.

## PROTOKÓł KOMUNIKACJI:
### MOZLIWE KOMUNIKATY WYSYLANE PRZEZ KLIENTA:
        /help - wypisuje wszystkie możliwe komenty | brak parametrów
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
                ENTER - after click enter you get info about game.

        /state - informuje o stanie gry | brak parametrów
            * "Game not created!"
            * "Game created but not started!"
            * "It's: " + game.getCurrentRound() + " round. Now playing player nr: " + game.getCurrentPlayer()
            * "It's a draw time!"
            * "Game is finished. Enter /result to show result."

        /create {ante} - stworzenie gry | ante
            * "Game was created! Ante: " + parameters.get(0)
            * "You should enter ante! Max ante: 10"
            * "The game was already created! Enter /join to join to the game!"

        /join - dołączenie do stworzonej gry | brak parametrów
            * "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10"
            * "Too many participants. Max number of players: " + Server.maxPlayers
            * "You joined the game, your number in game: " + (game.getPlayers().size() - 1)
            * "You are already join to the game"
            * "Game already started. You cannot join"

        /start - rozpoczęcie gry | brak parametrów
            * "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10"
            * "Wait for another player! Minimum number of players: 2"
            * "Game was started! Player number 0 play!\nAll funds: " + game.getAllFunds()
                                + CURRENT_BET + game.getBet()
                                + "\nYour funds:     " + game.getPlayer(userId).getFunds()
            * "You should first join to the game!"
            * "Game already started!"

        /players - informacja o liczbie graczy | brak parametrów
            * "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10"
            * number of players
        /id - id gracza | brak parametrów
            * "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10"
            * "You should join to the game to see your id in game!"
            * "Your game id: " + game.getPlayers().indexOf(game.getPlayer(userId))

        /money - informacja o dostępnych żetonach | brak parametrów
            * "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10"
            * "You should join to the game to see your money in game!"
            * "Money: " + game.getPlayer(userId).getFunds()

        /funds - informacja o żetonach na stole | brak parametrów
            * "The game has not been created yet! Enter /create {ante} to create a game! Max ante: 10"
            * "You should join to the game to see your money in game!"
            * "Funds: " + game.getAllFunds()

        /hand - karty w ręce | brak parametrów
            * "You must play the game to display your deck of cards!"
            * player cards
            * "You are not playing the game. Wait for the end of game!"

        /call - sprawdzenie | brak parametrów
            * "You must play the game to be able to call!"
            * "You don't have money!"
            * "Not your turn!"
            * "Now is time to draw. You cannot call!"
            * "Game is over. Enter /result to see result."

        /raise {number} - podbicie | nowa stawka
            * "You must play the game to be able to raise!"
            * "You should raise bet or you don't have money!"
            * "Not your turn!"
            * "Now is time to draw. You cannot raise!"
            * "Game is over. Enter /result to see result."

        /fold - spasowanie | brak parametrów
            * "You must play the game to be able to fold!"
            *
            * "Not your turn!"
            * "Now is time to draw. You cannot fold!"
            * "Game is over. You cannot fold"

        /draw {cards numbers}- wymiana kart | karty, które chcemy wymienić
            * "You must play the game to be able to draw!"
            * "This is not the time to draw!"
            * "End of draw. Now player " + game.getCurrentPlayer()
            * "Number of cards must be unique!"
            * "You should enter cards number from 0 to 4!"
            * "You should enter max 5 cards!"
            * "Not your turn!"
            * "Game is over. You cannot draw!"

        /result - wyniki gry | brak parametrów
            * "You should play the game to see result!"
            * "Game is not over yet"
            * result

        /end - zakończenie gry | brak parametrów
            * "You can end game after all rounds!"
            * "Game is over. To create new enter /create."

        ENTER - wyświetlenie informacji o grze | brak parametrów
            * "Game not created yet"
            * "Game ID: " + game.getId() + ". Number of players: " + game.getPlayers().size() + "\n" +
                                  "All funds: " + game.getAllFunds() +
                                  ". Your funds: " + game.getPlayer(userId).getFunds() + "\n"
### MOZLIWE KOMUNIKATY WYSYLANE PRZEZ SERWER:
        * "Welcome " + clientUsername + ". A number of participants: " + clientHandlers.size() +
                            "\nEnter /help to see all commands."
        * Pozostałe to komunikaty odpowiedzi na zapytania klienta, są wypisane powyżej.
