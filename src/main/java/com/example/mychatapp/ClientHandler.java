import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String clientName;
    private PrintWriter out;
    private Scanner in;

    public ClientHandler(Socket clientSocket, String clientName) {
        this.clientSocket = clientSocket;
        this.clientName = clientName;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new Scanner(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getClientName() {
        return clientName;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (in.hasNext()) {
                    String message = in.nextLine();
                    System.out.println(clientName + " says: " + message);
                    Server.broadcastMessage(message, this);
                }
            }
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
