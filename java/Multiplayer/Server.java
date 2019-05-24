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

                activeSockets.addLast(new ClientHandler(clientSocket, in, out,clientNumber-1));
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
        private boolean isActive = false;
        private String bufferToBroadcast ="";
        private final int clientIndex;
        private boolean turnPhase = true; // true is finished turn false - the turn is halfway through


        // Constructor
        ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos,int clientIndex) {
            this.socket = s;
            this.dis = dis;
            this.dos = dos;
            this.clientIndex = clientIndex;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if(!checkIfAlive())
                        return;
                    String received = dis.readUTF();

                    switch(received) {
                        case "CloseClient": // Client is disconnected from the server
                            activeSockets.remove(socket);
                            clientNumber--;
                            break;
                        case "CloseServer": // Host left the server
                            serverSocket.close();
                            clientNumber = 0;
                            activeSockets.clear();
                            return;
                        case "Acknowledge": // Client asks to be accepted
                            System.out.println(socket + " is connected to server");
                            break;
                        case "GiveMeAnIndex": //Client asks the server for an Index in in the players array
                            dos.writeInt(clientIndex);
                            break;

                        case "HasTheGameStarted": // Client asks whether the game has started
                            if(isInGame == true)
                                dos.writeBoolean(true);
                            else
                                dos.writeBoolean(false);
                            break;
                        case "TriggerGame": // Host orders the Game to start
                            for(int i=0;i<clientNumber;i++)
                            {
                                activeSockets.get(i).isInGame = true;
                            }
                            break;
                        case "InitOrder": // Host orders the Server to initialize synchronization rules
                            activeSockets.getFirst().isActive = true;
                            break;
                        case "Updates": // Clients asks what other clients have made.
                            turnPhase=!turnPhase;
                            String buffer = dis.readUTF();
                            for(int i=0;i<clientNumber;i++)
                            {
                                if(!activeSockets.get(i).isActive)
                                {
                                    activeSockets.get(i).bufferToBroadcast = buffer;
                                }
                            }
                            if(turnPhase == true) {
                                isActive = false;
                                activeSockets.get((clientIndex+1)%clientNumber).isActive= true;
                            }
                            break;
                        case "WereUpdatesMade": // Client asks whether there are any updates to be made
                            if(!bufferToBroadcast.isEmpty())
                            {
                                dos.writeUTF("Yes");
                            }
                            else
                            {
                                dos.writeUTF("No");
                            }
                            break;
                        case "SendTheUpdates":
                            dos.writeUTF(bufferToBroadcast);
                            bufferToBroadcast = "";
                            break;
                        case "PlayerCount": // Client asks for the player count
                            dos.writeInt(clientNumber);
                            break;
                        case "PlayerLimit":
                            dos.writeInt(playerLimit);
                            break;
                        case "I need player list": // Client asks for the player list
                            String toSend = "";
                            for(int i=0;i<clientNumber;i++)
                            {
                                toSend += getActiveSockets().get(i).socket.getInetAddress() + "\n";
                            }
                            dos.writeUTF(toSend);
                            break;
                    }
                }
                catch (SocketException e) {

                    e.printStackTrace();
                    activeSockets.remove(this);
                    clientNumber--;
                    if(isInGame == true)
                        System.exit(1);
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
