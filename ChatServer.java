import java.net.*;
import java.io.*;

public class ChatServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ChatServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Chat server started on port " + port);
    }

    public void start() throws IOException {
        clientSocket = serverSocket.accept();
        System.out.println("Client connected");

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        Thread receiveThread = new Thread(new ReceiveThread(in));
        receiveThread.start();

        Thread sendThread = new Thread(new SendThread(out));
        sendThread.start();
    }

    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer(8000);
        server.start();
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
                    System.out.println("Client: " + message);
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
                    System.out.print("Server: ");
                    message = stdin.readLine();
                    out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}