import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            String response;
            while (true) {
                // Read the menu from the server
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                    if (response.contains("Choose an option:")) {
                        break;
                    }
                }

                // Send the menu choice to the server
                String menuChoice = userInput.readLine();
                out.println(menuChoice);

                if (menuChoice.equals("3")) { // Exit option
                    System.out.println("Thank you for using the application. Goodbye!");
                    break;
                }

                // Event selection or notifications
                if (menuChoice.equals("1")) { // Give feedback
                    // Read the prompt for event selection
                    System.out.println(in.readLine());
                    String eventChoice = userInput.readLine();
                    out.println(eventChoice);

                    // Username and feedback
                    System.out.println(in.readLine()); // Prompt for username
                    String username = userInput.readLine();
                    out.println(username);

                    System.out.println(in.readLine()); // Prompt for feedback
                    String feedback = userInput.readLine();
                    out.println(feedback);
                    System.out.println(in.readLine()); // Response from server

                } else if (menuChoice.equals("2")) { // View notifications
                    System.out.println(in.readLine()); // Response from server
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while communicating with the server. Please try again later.");
            e.printStackTrace();
        }
    }
}
