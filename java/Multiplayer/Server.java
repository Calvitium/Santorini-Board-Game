package Multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    private ServerSocket serverSocket;
    private int playerLimit;
    private int clientNumber = 0;
    private LinkedList<Socket> activeSockets;

    public Server(int port,int serverPlayerLimit)  {
        try
        {

            playerLimit = serverPlayerLimit;
            activeSockets = new LinkedList<Socket>();
            serverSocket = new ServerSocket(port);
            //serverSocket.setSoTimeout(5000);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void update()
    {
        for(int i = 0;i<clientNumber;i++)
        {
            if(activeSockets.get(i).isClosed())
            {
                activeSockets.remove(i);
                i--;
                clientNumber--;
            }
        }
    }

    public void run() {
        try {
        while (true) {

                if (clientNumber < playerLimit)
                {
                    Socket clientSocket = serverSocket.accept();

                    System.out.println("A new client is trying to connect : " + clientSocket);
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                    activeSockets.addLast(clientSocket);
                    clientNumber++;


                    Thread thread = new ClientHandler(clientSocket, in, out);
                    thread.start();
                }
                update();
            }
        }
        catch (Exception e) {
            stop();
            e.printStackTrace();
        }
    }

    private void stop() {
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

            if(checkIfAlive()==false)
                return;
            while (true) {
                try {

                    String received = dis.readUTF();
                    if (received.equals("Acknowledge"))

                        System.out.println(socket + " is connected to server");
                    else if(received.equals("I need player list"))
                    {
                        System.out.println(socket + " is asking for players list");
                        String toSend = "";
                        for(int i=0;i<clientNumber;i++)
                        {
                            toSend += getActiveSockets().get(i) + "\n";
                        }
                        dos.writeUTF(toSend);
                    }

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private boolean checkIfAlive()
        {
            return !socket.isClosed();
        }
    }

    public LinkedList<Socket> getActiveSockets()
    {
        return activeSockets;
    }
    public int getPlayerLimit()
    {
        return playerLimit;
    }
}
