import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<String> events;

    public static void main(String[] args) {
        events = new ArrayList<>();
        events.add("Event 1: Hackathon");
        events.add("Event 2: Workshop");
        events.add("Event 3: Conference");

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("RPC Server is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request;
                while ((request = in.readLine()) != null) {
                    String[] requestData = request.split(":");
                    String operation = requestData[0];
                    switch (operation) {
                        case "logClientLogin":
                            logClientLogin(requestData[1], out);
                            break;
                        case "getEvents":
                            sendEvents(out);
                            break;
                        case "submitFeedback":
                            if (requestData.length == 5) { // Ensure correct number of arguments
                                submitFeedback(requestData[1], requestData[2], requestData[3], requestData[4], out);
                            } else {
                                out.println("Invalid feedback submission format.");
                            }
                            break;
                        default:
                            out.println("Invalid operation");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (clientSocket != null) {
                        clientSocket.close(); // Ensure the socket is closed
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void logClientLogin(String clientName, PrintWriter out) {
            System.out.println("Client " + clientName + " has logged in.");
            out.println("Client " + clientName + " has logged in.");
        }

        private void sendEvents(PrintWriter out) {
            out.println(events.size());
            for (String event : events) {
                out.println(event);
            }
        }

        private void submitFeedback(String clientName, String event, String issue, String description,
                PrintWriter out) {
            System.out.println("Feedback received from " + clientName + ":");
            System.out.println("Event: " + event);
            System.out.println("Issue: " + issue);
            System.out.println("Description: " + description);
            out.println("Feedback submitted by " + clientName + " for " + event);
        }
    }
}
