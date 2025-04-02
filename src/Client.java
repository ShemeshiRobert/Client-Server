import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private Scanner scanner;

    public Client() {
        try {
            socket = new Socket("127.0.0.1", Server.PORT);
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);

            getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFile() {
        try {
            output.println("SEND_FILE");
            // Read number of files
            String filesLen = input.readLine();
            int maxFiles = Integer.parseInt(filesLen);

            // Read and display menu
            String menu = input.readLine();
            System.out.println(menu);

            int userSelection = 1;
            boolean isSelectionValid = false;

            while (!isSelectionValid) {
                System.out.print("Select a file number: ");
                userSelection = scanner.nextInt();
                isSelectionValid = userSelection > 0 && userSelection <= maxFiles;

                if (!isSelectionValid) {
                    System.out.println("Invalid selection! Please try again.");
                }
            }

            // Send selected file number
            output.println(userSelection);

            // Read and print file content
            String fileContent = input.readLine();
            System.out.println("FILE START");
            System.out.println(fileContent);
            System.out.println("FILE END");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
