import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                broadcast(message);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }
    }

    private void broadcast(String message) {
        for (ClientHandler client : ChatServer.clients) {
            client.out.println(message);
        }
    }
}
