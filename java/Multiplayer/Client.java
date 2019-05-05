package Multiplayer;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;


    public Client(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage()  {
        try {
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            //Scanner scn = new Scanner(System.in);
            while (true) {
                String toSend = "Yeah";
                output.writeUTF(toSend);

                // If client sends exit,close this connection
                // and then break from the while loop
                if (toSend.equals("Exit")) {
                    System.out.println("Closing this connection : " + clientSocket);
                    clientSocket.close();
                    System.out.println("Connection closed");
                    break;
                }

                // printing date or time as requested by client
                String received = input.readUTF();
                System.out.println(received);
                //scn.close();
                output.close();
                input.close();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }





    }


}
