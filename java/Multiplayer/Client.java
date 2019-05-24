package Multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    private Socket clientSocket;
    private DataOutputStream output;
    private DataInputStream input;
    private boolean isHost;

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
    public int askForIndex()
    {
        try{
            output.writeUTF("GiveMeAnIndex");
            return input.readInt();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }
    public String askForUpdates()
    {
        try {
            output.writeUTF("WereUpdatesMade");
            String updates = input.readUTF();
            if(updates.equals("Yes"))
            {
                output.writeUTF("SendTheUpdates");
                return input.readUTF();
            }
        }catch (SocketException e) {
            try {
                clientSocket.close();
                System.exit(1);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public void sendUpdates(String buffer)
    {
        try {
            output.writeUTF("Updates");
            output.writeUTF(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void sendInitOrder()
    {
        try {
            output.writeUTF("InitOrder");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean checkIfGameStarted()
    {
        try {
            output.writeUTF("HasTheGameStarted");
            return input.readBoolean();
        }
        catch (SocketException e) {
            try {
                input.close();
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void closeConnection(boolean isHost)
    {
        try {
            output.writeUTF("CloseClient");
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
    public int askForPlayerLimit(){
        try {
            output.writeUTF("PlayerLimit");
            return input.readInt();
        }
        catch (SocketException e) {
            try {
                clientSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        catch (IOException e) {
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


}
