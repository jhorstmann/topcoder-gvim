package de.planetxml.topcoder;

import java.util.*;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

public class VimController implements Runnable {
    private EditorBuffer editor;
    private BufferedWriter writer;

    private int lastSequenceNumber = 0;

    private final Object currentBufferLock = new Object();
    private int currentBuffer      = 0;

    private int replyForGetText    = -1;
    private int replyForGetCursor  = -1;

    private VimBufferList buffers  = new VimBufferList();

    class MessageHandler implements Runnable {
        private Message message;

        public MessageHandler(final Message message) {
            this.message = message;
        }

        public void run() {
            if (message.getType() == Message.REPLY) {
                if (message.getSeqno() == replyForGetText) {
                    editor.setSource(message.getStringArgsDecoded());
                }
                else if (message.getSeqno() == replyForGetCursor) {
                    //synchronized (currentBufferLock) {
                        currentBuffer = message.getIntArgs();
                        //currentBufferLock.notify();
                    //}
                }
            }
            else if (message.getType() == Message.EVENT) {
                String name = message.getName();
                if ("fileOpened".equals(name)) {
                    String bufname = message.getStringArgsDecoded();
                    if (bufname != null && bufname.length() > 0) {
                        VimBuffer buf = buffers.getBufferByName(bufname);
                        System.out.println("putBufferNumber " + buf.getId() + " " + bufname);
                        sendCommand("putBufferNumber", buf.getId(), StringHelper.quote(bufname));
                    }
                }
                else if ("killed".equals(name)) {
                    buffers.removeBufferById(message.getBufno());
                }
                else if ("keyCommand".equals(name)) {
                    String key = message.getStringArgsDecoded();
                    if ("e".equals(key)) {
                        vimEditFile();
                    }
                    else if ("w".equals(key)) {
                        if (message.getBufno() > 0) {
                            vimGetText(message.getBufno());
                        }
                        else {
                            vimGetText();
                        }
                    }
                }
                else if ("insert".equals(name)) {
                    String   source = editor.getSource();
                    String[] args   = message.getPairArgs();
                    int      off    = Integer.parseInt(args[0]);
                    String   ins = new Parser(args[1]).parseString();
                    /*
                    if (off >= source.length()) {
                    source += ins;
                    } else {
                    source = source.substring(0, off) + ins + source.substring(off);
                    }
                     */
                    StringBuilder sb = new StringBuilder(source);
                    if (off >= source.length()) {
                        sb.append(ins);
                    } else {
                        sb.insert(off, ins);
                    }
                    editor.setSource(sb.toString());

                    //editor.setSource(source.subString(0, ))
                }
                else if ("remove".equals(name)) {
                    String   source = editor.getSource();
                    String[] args   = message.getPairArgs();
                    int      off    = Integer.parseInt(args[0]);
                    int      len    = Integer.parseInt(args[1]);
                    StringBuilder sb = new StringBuilder(source);

                    // FIXME: len is supposed to be in bytes,
                    // this works because vim apparently always sends a delete for the whole line
                    sb.delete(off, Math.min(off+len, sb.length()));
                    editor.setSource(sb.toString());
                }
            }
        }
    }

    class ReaderThread implements Runnable {
        private BufferedReader reader;

        public ReaderThread(InputStream input) {
            try {
                reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        public void run() {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    Message m = MessageFactory.parse(line);
                    System.out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + " " + m);

                    try {
                        SwingUtilities.invokeAndWait(new MessageHandler(m));
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    catch (InvocationTargetException e) {
                        if (e.getCause() != null) {
                            e.getCause().printStackTrace();
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public VimController(EditorBuffer editor) {
        this.editor = editor;
    }

    public EditorBuffer getEditor() {
        return editor;
    }

    private static String createRandomPassword() {
        String passwordStartChars = "abcdefghifklmnopqrstuvwxyzABCDEFGHIFKLMNOPQRSTUVWXY";
        String passwordchars      = passwordStartChars + "_-.+$Z01234567890";
        char[] password = new char[12];

        password[0] = passwordStartChars.charAt((int)(Math.random()*passwordStartChars.length()));
        for (int i=1; i<password.length; i++) {
            password[i] = passwordchars.charAt((int)(Math.random()*passwordchars.length()));
        }

        return new String(password);
    }

    private void sendMessage(Message message) {
        try {
            String str = message.encode();
            System.out.println("Sending: " + str);
            writer.write(str);
            writer.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCommand(String cmd, int bufno, String args) {
        sendMessage(MessageFactory.command(bufno, cmd, ++lastSequenceNumber, args));
    }

    private void sendFunction(String func, int bufno, String args) {
        sendMessage(MessageFactory.function(bufno, func, ++lastSequenceNumber, args));
    }

    private void sendFunction(String func, int bufno) {
        sendFunction(func, bufno, null);
    }

    private int getLastSequenceNumber() {
        return lastSequenceNumber;
    }

    public void vimEditFile() {
        //synchronized (currentBufferLock) {
            String name = editor.getFileName();
            String content = editor.getSource();
            VimBuffer buf = buffers.getBufferByName(name);

            int id = currentBuffer = buf.getId();

            System.out.println("vimEditFile(name=" + name + ", bufno=" + id + ")");

            sendCommand("setFullName", id, StringHelper.quote(name));
            sendFunction("insert"    , id, "1 " + StringHelper.quote(content));
            //sendCommand("insertDone" , id, null);
            sendCommand("initDone"   , id, null);
            //sendCommand("stopDocumentListen", id, null);
        //}
    }

    private int vimGetCurrentBuffer() {
        //synchronized (currentBufferLock) {
            sendFunction("getCursor", 0);
            replyForGetCursor = getLastSequenceNumber();

            /*
            try {
                currentBufferLock.wait();
            }
            catch (InterruptedException e) {
            }
            */

            return currentBuffer;
        //}
    }

    public void vimGetText(int id) {
        sendFunction("getText", id);
        replyForGetText = getLastSequenceNumber();
    }

    public void vimGetText() {
        //synchronized (currentBufferLock) {
            vimGetText(vimGetCurrentBuffer());
        //}
    }

    public void vimGetText(String name) {
        VimBuffer buf = buffers.getBufferByName(name);
        vimGetText(buf.getId());
    }

    public void run() {
        try {
            ServerSocket server = new ServerSocket(0);
            String password = createRandomPassword();

            System.out.println(server);
            String nb = "-nb:localhost:" + server.getLocalPort() + ":" + password;
            System.out.println(nb);
            //Runtime.getRuntime().exec(new String[]{"c:/programme/Vim/vim70/gvim.exe", nb});
            Runtime.getRuntime().exec(new String[]{"gvim", nb});
            //Runtime.getRuntime().exec(new String[]{"c:/temp/vim7/vim7/src/gvim.exe", nb});

            Socket       socket = server.accept();
            InputStream  input  = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            new Thread(new ReaderThread(input)).start();

            try {
                writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            //sendCommand("create", 0, null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new VimController(null);
    }
}
