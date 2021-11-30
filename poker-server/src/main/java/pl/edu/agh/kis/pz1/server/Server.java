package pl.edu.agh.kis.pz1.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server for the poker game.
 */
public class Server {
    /**
     * server socket.
     */
    private final ServerSocket serverSocket;
    /**
     * max number of players in game.
     */
    private static int maxPlayers;
    /**
     * logger.
     */
    private static final Logger logger = Logger.getLogger( Server.class.getName() );

    /**
     * creates server on the given socket.
     * @param serverSocket  server socket.
     */
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;}

    /**
     * Sets maxPlayers
     * @param maxPlayers max number of players in game
     */
    public static void setMaxPlayers(int maxPlayers) {
        Server.maxPlayers = maxPlayers;
    }

    /**
     * Gets maxPlayers
     * @return max number players in game
     */
    public static int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * starts server and waiting for clients.
     */
    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                    Socket socket = serverSocket.accept();
                    logger.info("A new client has connected!");
                    ClientHandler clientHandler = new ClientHandler(socket);

                    Thread thread = new Thread(clientHandler);
                    thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    /**
     * closes server socket.
     */
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch(IOException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * creates server socket and starts server.
     * @param args         the maximum number of players that can participate in the game (2-4).
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            logger.log(Level.WARNING, "Usage: java -jar ... <max_players(2-4)>");
            System.exit(1);
        }
        if (Integer.parseInt(args[0]) < 2 || Integer.parseInt(args[0]) > 4) {
            logger.log(Level.WARNING, "Usage: java -jar ... <max_players(2-4)>");
            System.exit(1);
        }
        maxPlayers = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
