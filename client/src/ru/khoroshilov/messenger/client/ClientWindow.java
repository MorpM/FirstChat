package ru.khoroshilov.messenger.client;

import ru.khoroshilov.messenger.network.TCPConnection;
import ru.khoroshilov.messenger.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {
    private static final String IP_ADDR="192.168.1.66";
    private static final int PORT=8189;
    private static final int WIDTH=600;
    private static final int HEIGHT=400;



    public static void main(String[] args){
SwingUtilities.invokeLater(new Runnable() {
    @Override
    public void run() {
        new ClientWindow();
    }
});
    }

    private final JTextArea log=new JTextArea();
    private final JTextField fieldNick=new JTextField("Morp");
    private final JTextField fieldInput=new JTextField();
    private TCPConnection connection;


    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);
        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNick, BorderLayout.NORTH);
        fieldInput.addActionListener(this);
        try {
            connection=new TCPConnection(this, IP_ADDR,PORT);
        } catch (IOException e) {
            printMsg("ConnectionException" +e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg=fieldInput.getText();
        if(msg.equals(""))return;
        fieldInput.setText(null);
        connection.sendString(fieldNick.getText() + ": "+msg);

    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready");
    }

    @Override
    public void onRecieveString(TCPConnection tcpConnection, String value) {
printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
printMsg("Connection closed");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {

    }
    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg+"\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
