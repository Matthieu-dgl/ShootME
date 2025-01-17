package com.matthieudeglon.shootme.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameServer {

    private static final Logger logger = Logger.getLogger(GameServer.class.getName());
    private final int port;
    private Set<PrintWriter> clientWriters = new HashSet<>();

    public GameServer(int port) {
        this.port = port;
        setupLogger();
    }

    private void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("server.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.severe("Failed to set up logger: " + e.getMessage());
        }
    }

    public void start() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                logger.info("Game server started on port " + port);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Client connected");
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    clientWriters.add(out);
                    new Thread(new ClientHandler(clientSocket, out)).start();
                }
            } catch (IOException e) {
                logger.severe("Server error: " + e.getMessage());
            }
        }).start();
    }

    public void broadcast(String message) {
        logger.info("Broadcasting message: " + message);
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket, PrintWriter out) {
            this.socket = socket;
            this.out = out;
        }

        @Override
        public void run() {
            try {
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.severe("Error closing socket: " + e.getMessage());
                }
                clientWriters.remove(out);
            }
        }
    }
}

