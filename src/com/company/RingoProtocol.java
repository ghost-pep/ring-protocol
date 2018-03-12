package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class RingoProtocol {
    private static DatagramSocket socket;
    public final static byte NEW_NODE = 0;
    public final static byte UPDATE_IP_TABLE = 1;
    public final static byte PING_HELLO = 3;
    public final static byte PING_RESPONSE = 4;
    public final static byte RTT_UPDATE = 5;
    public static void sendNewNode(String name, int port, int local_port) throws IOException {
        socket = new DatagramSocket();
        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName(name);
        System.out.println(address.toString() + " fuck me up fam");
        buf[0] = NEW_NODE;
        byte[] loc_port_bytes = ByteBuffer.allocate(4).putInt(local_port).array();
        System.arraycopy(loc_port_bytes, 0, buf, 1, loc_port_bytes.length);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }
    public static void sendUpdateIpTable(DatagramSocket socket, InetAddress address, int port, byte[] data) {
        byte[] sendData = new byte[data.length + 1];
        System.arraycopy(data, 0, sendData, 1, data.length);
        sendData[0] = UPDATE_IP_TABLE;
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port);
        try {
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPingHello(DatagramSocket socket, InetAddress address, int port, long time, int local_port) {
        byte[] timebytes = ByteBuffer.allocate(8).putLong(time).array();
        byte[] loc_port_bytes = ByteBuffer.allocate(4).putInt(local_port).array();
        byte[] sendData = new byte[loc_port_bytes.length + timebytes.length + 1];
        System.arraycopy(loc_port_bytes, 0, sendData, 1, loc_port_bytes.length);
        System.arraycopy(timebytes, 0, sendData, loc_port_bytes.length, timebytes.length);
        sendData[0] = PING_HELLO;
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port);
        try {
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPingResponse(DatagramSocket socket, InetAddress address, int port, byte[] data) {
        byte[] sendData = new byte[data.length + 1];
        sendData[0] = PING_RESPONSE;
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port);
        try {
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
