package de.planetxml.topcoder;

import java.util.*;
import java.net.*;
import java.io.*;

public class Main {
    public static final String PASSWORD = "passwort";

    private static byte[] encode(String str) {
        try {
            return str.getBytes("UTF-8");
        }
        catch (Exception e) {
            return new byte[0];
        }
    }

    private void startVIM() throws IOException {
        //ServerSocket server = new ServerSocket(0, 16, InetAddress.getLocalHost());
        ServerSocket server = new ServerSocket(21124);

        System.out.println(server);
        String nb = "-nb:localhost:" + server.getLocalPort() + ":" + PASSWORD;
        System.out.println(nb);
        Runtime.getRuntime().exec(new String[]{"gvim.exe", nb});

        Socket         socket = server.accept();
        BufferedReader in     = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        BufferedWriter out    = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

        String         line   = null;

        while ((line = in.readLine()) != null) {
            System.out.println(line);
            System.out.println(MessageFactory.parse(line));
        }

        /*
           try {
           Thread.sleep(1000);
           }
           catch (InterruptedException e) {
           }

           Socket s = new Socket(HOST, PORT);
           OutputStream out = s.getOutputStream();
           out.write(encode("AUTH " + PASSWORD + "\n"));
           out.write(encode("0:setFullName!1 \"test.txt\"\n"));
           out.write(encode("0:insert!2 0 \"abc\"\n"));
           out.write(encode("0:insertDone!3\n"));

           try {
           Thread.sleep(5000);
           }
           catch (InterruptedException e) {
           }

           out.close();
           */
    }

    public static void main(String[] args) throws IOException {
        Main m = new Main();
        m.startVIM();
    }
}
