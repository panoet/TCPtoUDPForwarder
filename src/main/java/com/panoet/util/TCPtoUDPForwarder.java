package com.panoet.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class TCPtoUDPForwarder {

    /**
     *
     * @param args
     *        [0] TCP address to receive packet from
     *        [1] TCP port to receive packet from
     *        [2] UDP address to send packet to
     *        [3] TCP port to send packet to
     */
    public static void main(String[] args) {
            while (true) {
                try {
                    System.out.println("Waiting 5 seconds...");
                    Thread.sleep(5000);
                    Socket tcpSocket = new Socket(args[0], Integer.parseInt(args[1]));
                    System.out.println("Connected to TCP source...");
                    BufferedReader inputStream = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
                    String inputLine;
                    while ((inputLine = inputStream.readLine()) != null) {
                        if (tcpSocket.isInputShutdown() || tcpSocket.isClosed()) {
                            System.out.println("Socket closed or input stream not available...");
                            Thread.sleep(5000);
                            tcpSocket = new Socket(args[0], Integer.parseInt(args[1]));
                            inputStream = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
                            continue;
                        } else if (tcpSocket.isConnected()) {
                            System.out.println(inputLine);
                            DatagramSocket udpSocket = new DatagramSocket();
                            byte[] message = inputLine.getBytes();
                            DatagramPacket datagramPacket = new DatagramPacket(message, message.length, InetAddress.getByName(args[2]), Integer.parseInt(args[3]));
                            udpSocket.send(datagramPacket);
                            udpSocket.close();
                        }
                    }
                    System.out.println("No data received, closing socket...");
                    tcpSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }
}
