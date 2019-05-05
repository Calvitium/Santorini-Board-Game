package Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.DateFormat;
import java.util.*;

public class Server {
    public ServerSocket serverSocket;


    private int serverPort;

    public Server(int port)  {
        try
        {
            serverPort = port;
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("A new client is connected : " + clientSocket);
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                Thread thread = new ClientHandler(clientSocket, in, out);
                thread.start();


            } catch (Exception e) {
                stop();
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler extends Thread {

        private final DataInputStream dis;
        private final DataOutputStream dos;
        private final Socket socket;


        // Constructor
        ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
            this.socket = s;
            this.dis = dis;
            this.dos = dos;
        }

        @Override
        public void run() {
            String received;
            while (true) {
                try {

                    // Ask user what he wants
                    dos.writeUTF("Write Yeah");

                    // receive the answer from client
                    received = dis.readUTF();
                    if (received == "Yeah")
                        System.out.print("Hell yeah.");
                    else
                        System.out.print("Hell no.");
                    break;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                // closing resources
                this.dis.close();
                this.dos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
