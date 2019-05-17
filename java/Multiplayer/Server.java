package Multiplayer;

import sun.net.ConnectionResetException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;


public class Server extends  Thread{
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
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void run() {
        try {
        while (true) {

            Socket clientSocket = serverSocket.accept();

            System.out.println("A new client is trying to connect : " + clientSocket);
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            if (clientNumber < playerLimit) {

                activeSockets.addLast(clientSocket);
                clientNumber++;
                out.writeUTF("Connection achieved");

                Thread thread = new ClientHandler(clientSocket, in, out);
                thread.start();
            }
            else {
                out.writeUTF("Connection failed, room full");
            }
            }
        }
        catch (Exception e) {
            stopServer();
            e.printStackTrace();
        }
    }

    private void stopServer() {
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


            while (true) {
                try {
                    if(!checkIfAlive())
                        return;
                    String received = dis.readUTF();
                    if(received.equals("ByeBye"))
                    {
                        activeSockets.remove(socket);
                        clientNumber--;
                    }
                    else if (received.equals("Acknowledge"))

                        System.out.println(socket + " is connected to server");
                    else if(received.equals("I need player list"))
                    {
                        String toSend = "";
                        for(int i=0;i<clientNumber;i++)
                        {
                            toSend += getActiveSockets().get(i) + "\n";
                        }
                        dos.writeUTF(toSend);
                    }


                }
                catch (SocketException e) {

                    e.printStackTrace();
                    activeSockets.remove(socket);
                    clientNumber--;
                    return;

                }
                catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        }
        private boolean checkIfAlive()
        {
            return !socket.isClosed();
        }
    }

    private LinkedList<Socket> getActiveSockets()
    {
        return activeSockets;
    }
    public int getPlayerLimit()
    {
        return playerLimit;
    }
}
