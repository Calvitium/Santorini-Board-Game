package Multiplayer;


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
    private LinkedList<ClientHandler> activeSockets;

    public Server(int port,int serverPlayerLimit)  {
        try
        {

            playerLimit = serverPlayerLimit;
            activeSockets = new LinkedList<ClientHandler>();
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void run() {
        try {
        while (!serverSocket.isClosed()) {

            Socket clientSocket = serverSocket.accept();
            System.out.println("A new client is trying to connect : " + clientSocket);
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            if (clientNumber < playerLimit) {


                clientNumber++;
                out.writeUTF("Connection achieved");

                activeSockets.addLast(new ClientHandler(clientSocket, in, out));
                Thread thread = activeSockets.getLast();
                thread.start();
            }
            else {
                out.writeUTF("Connection failed, room full");
            }
            }
        }
        catch (IOException  e) {
            stopServer();
            e.printStackTrace();
            return;
        }

    }

    private void stopServer() {
        try {
            serverSocket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        clientNumber = 0;
        activeSockets.clear();
    }
    private class ClientHandler extends Thread {

        private final DataInputStream dis;
        private final DataOutputStream dos;
        private final Socket socket;
        private boolean isInGame = false;


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

                    switch(received) {
                        case "ByeBye":
                            activeSockets.remove(socket);
                            clientNumber--;
                            break;
                        case "CloseServer":
                            serverSocket.close();
                            clientNumber = 0;
                            activeSockets.clear();
                            return;
                        case "Acknowledge":
                            System.out.println(socket + " is connected to server");
                            break;
                        case "HasTheGameStarted":
                            if(isInGame == true)
                                dos.writeBoolean(true);
                            else
                                dos.writeBoolean(false);
                            break;
                        case "TriggerGame":
                            for(int i=0;i<clientNumber;i++)
                            {
                                //activeSockets.get(i).dos.writeUTF("InitGame");
                                activeSockets.get(i).isInGame = true;
                            }
                            break;
                        case "PlayerCount":
                            dos.writeInt(clientNumber);
                            break;
                        case "I need player list":
                            String toSend = "";
                            for(int i=0;i<clientNumber;i++)
                            {
                                toSend += getActiveSockets().get(i) + "\n";
                            }
                            dos.writeUTF(toSend);
                            break;
                    }
                }
                catch (SocketException e) {

                    e.printStackTrace();
                    activeSockets.remove(this);
                    clientNumber--;
                    return;

                }
                catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                        return;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        }
        private boolean checkIfAlive() { return !socket.isClosed(); }
    }

    private LinkedList<ClientHandler> getActiveSockets()
    {
        return activeSockets;
    }
    public int getPlayerLimit()
    {
        return playerLimit;
    }
}
