package com.JavaChatApp;
//This java program imports necessary classes for GUI(Graphical User Interface), Event handling, and Networking.
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

//This user is necessarily considered as the server(User2) and the user 1 is the client, the server should run first because In a client-server architecture,
// it's important for the server to run first because the server needs to be in a listening state, waiting for incoming connections from clients. If the client tries to connect before the server is running, the client will receive a connection refused error or a timeout error, depending on the network configuration.
// This is because there is no server actively accepting connections at that time.



public class JavaChatAppUser2 extends JFrame implements ActionListener,Runnable {
    JTextArea displayArea;  //Instance variables for the GUI components (displayArea, msgInputField, sendButton) and
    JTextField msgInputField;
    JButton sendButton;

    Socket socket;  // And for networking (socket, dataInputStream, dataOutputStream)
    ServerSocket serverSocket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Thread readInputThread; //and thread (readInputThread).

    JavaChatAppUser2(){ //The constructor initializes the GUI components (text area, text field, and send button)
        displayArea = new JTextArea();
        displayArea.setBounds(10, 30, 180, 310);

        msgInputField = new JTextField();
        msgInputField.setBounds(10, 340, 180, 30);

        sendButton = new JButton("Send");
        sendButton.setBounds(150, 368, 40, 25);
        sendButton.addActionListener(this);

        add(displayArea);
        add(msgInputField);
        add(sendButton);

        try {
            serverSocket = new ServerSocket(12000); // Setting up the networking by creating a server socket,
            socket = serverSocket.accept(); //accepting a client connection,

            dataInputStream = new DataInputStream(socket.getInputStream());//And setting up input and output streams.
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e){

        }

        ThreadGroup tg = new ThreadGroup("JavaChatApp");
        readInputThread = new Thread(tg,this);
        readInputThread.setDaemon(true);
        readInputThread.start();

        setLayout(null);
        setTitle("User2");
        setSize(200,400);
        setVisible(true);

    }

    //To send the output
    public void actionPerformed(ActionEvent e) {
        String msg = msgInputField.getText();
        displayArea.append("User2: " + msg + "\n");
        msgInputField.setText("");

        try {
            dataOutputStream.writeUTF(msg);
            dataOutputStream.flush();
        } catch (IOException ex) {

        }

    }


    //To receive the input
    public void run() {
        while (true){
            try {
                String msg = dataInputStream.readUTF();
                displayArea.append("User1: "+msg+"\n\n");
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public static void main(String[] args) throws IOException {
        new JavaChatAppUser2();
    }

}

