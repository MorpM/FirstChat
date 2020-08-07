package ru.khoroshilov.messenger.server;

import ru.khoroshilov.messenger.network.TCPConnection;
import ru.khoroshilov.messenger.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class MessengerServer implements TCPConnectionListener {
    public static void main(String[] args){
        new MessengerServer();

    }
    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private MessengerServer(){
        System.out.println("Server running");
       try( ServerSocket serverSocket=new ServerSocket(8189)){
           while (true){
               try{
                   new TCPConnection(this, serverSocket.accept());
               }
               catch (IOException e){
                   System.out.println(e);
               }
           }

       }
       catch (IOException e){
           throw new RuntimeException();
       }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: "+tcpConnection);
    }

    @Override
    public synchronized void onRecieveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: "+tcpConnection);

    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("e");
    }
    private void sendToAllConnections(String value){
        System.out.println(value);
        for(TCPConnection connection : connections){
            connection.sendString(value);
        }
    }
}
