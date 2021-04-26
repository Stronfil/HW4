package gb.cloud.client.service.impl;

import gb.cloud.client.service.NetworkService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class IONetworkService implements NetworkService {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8189;

    private static IONetworkService instance;

    public static Socket socket;
    public static DataInputStream in;
    public static DataOutputStream out;

    private IONetworkService() { }

    public static IONetworkService getInstance() {
        if (instance == null) {
            instance = new IONetworkService();

            initializeSocket();
            initializeIOStreams();
        }

        return instance;
    }

    private static void initializeSocket() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeIOStreams() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendCommand(String command) {
        try {
            out.writeUTF(command);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String readCommandResult() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            throw new RuntimeException("Read command result exception: " + e.getMessage());
        }
    }

    @Override
    public void closeConnection() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
