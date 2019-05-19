package Multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private DataOutputStream output;
    private DataInputStream input;
    private boolean isHost;
    private boolean isInGame = false;



    public Client(String ip, int port, boolean host) {
        try {
            clientSocket = new Socket(ip, port);
            isHost = host;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isHost()
    {
        return isHost;
    }

    public int sendAcknowledgement()  {
        try {
            input = new DataInputStream(clientSocket.getInputStream());
            output = new DataOutputStream(clientSocket.getOutputStream());


            String toSend = "Acknowledge";
            output.writeUTF(toSend);

            String received = input.readUTF();
            if(received.equals("Connection failed, room full")) {
                clientSocket.close();
                System.out.println(received);
                return -1;
            }




        }
        catch(IOException e) {
            e.printStackTrace();

        }
        return 0;
    }
    public void sendGameTrigger()
    {
        try{
            output.writeUTF("TriggerGame");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void startClientThread()
    {
        Thread thread = new ClientListener(input,output,clientSocket);
        thread.start();
    }
    public boolean checkIfGameStarts()
    {
        try {
            String received = input.readUTF();
            if(received.equals("GameInit"))
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void closeConnection(boolean isHost)
    {
        try {
            output.writeUTF("ByeBye");
            if(isHost == true)
                output.writeUTF("CloseServer");
            output.close();
            input.close();
            clientSocket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public int askForPlayerCount()
    {
        try {
            output.writeUTF("PlayerCount");
            return input.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public String askForPlayerList() {
        String received = "There are no players, sth wrong.";
        try {
            String toSend = "I need player list";
            output.writeUTF(toSend);

             received = input.readUTF();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return received;
    }

    private class ClientListener extends Thread
    {
        private DataOutputStream dos;
        private DataInputStream dis;
        private Socket socket;

        ClientListener(DataInputStream dis, DataOutputStream dos, Socket socket)
        {
            this.dos = dos;
            this.dis = dis;
            this.socket = socket;
        }

        @Override
        public void run()
        {
            while(true)
            {
                try {
                    String received = dis.readUTF();

                    switch(received) {
                        case "GameInit":
                            isInGame = true;
                            break;

                    }

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



    }



}
