import java.net.*;
import java.io.*;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ChatClient(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        System.out.println("Connected to chat server");

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        Thread receiveThread = new Thread(new ReceiveThread(in));
        receiveThread.start();

        Thread sendThread = new Thread(new SendThread(out));
        sendThread.start();
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        ChatClient client = new ChatClient("localhost", 8000);
    }

    private static class ReceiveThread implements Runnable {
        private BufferedReader in;

        public ReceiveThread(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Server: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class SendThread implements Runnable {
        private PrintWriter out;

        public SendThread(PrintWriter out) {
            this.out = out;
        }

        @Override
        public void run() {
            try {
                BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                String message;
                while (true) {
                    System.out.print("Client: ");
                    message = stdin.readLine();
                    out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}