package game;

import game.events.GameEvent;

import java.io.*;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

public class Network {
    static private boolean running = false;
    static private Socket socket;
    static private ObjectInputStream inputStream;
    static private ObjectOutputStream outputStream;
    static private Thread inputThread;
    static private Deque<GameEvent> events = new ArrayDeque<GameEvent>();

    static public void init(Socket s) throws IOException {
        close();

        socket = s;

        outputStream = new ObjectOutputStream(s.getOutputStream());
        outputStream.flush();

        inputStream = new ObjectInputStream(s.getInputStream());

        running = true;
        inputThread = new Thread(new InputHandler());
        inputThread.start();
    }

    static public void close() {
        try {
            running = false;

            if (socket != null) {
                socket.close();
                socket = null;
            }

            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }

            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }

            if (inputThread != null) {
                inputThread.join();
                inputThread = null;
            }

            events.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void send(GameEvent event) {
        if (event == null) return;

        try {
            outputStream.writeObject(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public boolean hasEvents() {
        return !events.isEmpty();
    }

    static public GameEvent popEvent() {
        if (!events.isEmpty()) {
            return events.pop();
        }
        else {
            return null;
        }
    }

    private static class InputHandler implements Runnable {
        @Override
        public void run() {
            try {
                while (running) {
                    GameEvent event = (GameEvent)inputStream.readObject();
                    events.push(event);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
