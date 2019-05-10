package Multiplayer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private Socket clientSocket;
    private DataOutputStream output;
    private DataInputStream input;


    public Client(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            clientSocket.setSoTimeout(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendAcknowledgement()  {
        try {
            input = new DataInputStream(clientSocket.getInputStream());
            output = new DataOutputStream(clientSocket.getOutputStream());


                String toSend = "Acknowledge";
                output.writeUTF(toSend);
        }
        catch(IOException e) {
            e.printStackTrace();

        }
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
