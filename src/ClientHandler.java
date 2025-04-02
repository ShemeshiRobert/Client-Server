import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if (output != null) {
            output.println(message);
        }
    }

    public void sendFile() {
        sendMenu(); // Show available files
        int index = getSelectedFileIndex(); // Get selected file index
        sendSelectedFile(index); // Send selected file
    }

    private int getSelectedFileIndex() {
        try {
            String in = input.readLine(); // Read user's choice
            return Integer.parseInt(in) - 1;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return -1; // Invalid selection
        }
    }

    private void sendMenu() {
        StringBuilder menu = new StringBuilder("*** FILES ***\n");
        File[] fileList = new File(Server.FILES_PATH).listFiles();

        sendMessage("" + fileList.length); // Send number of files
        for (int i = 0; i < fileList.length; i++) {
            menu.append(String.format("%d. %s\n", i + 1, fileList[i].getName()));
        }
        sendMessage(menu.toString());
    }

    private void sendSelectedFile(int index) {
        try {
            File[] fileList = new File(Server.FILES_PATH).listFiles();
            if (index < 0 || index >= fileList.length) {
                sendMessage("Invalid file selection.");
                return;
            }

            File selectedFile = fileList[index];
            List<String> fileLines = Files.readAllLines(selectedFile.toPath());
            String fileContent = String.join("\n", fileLines);
            sendMessage(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            sendMessage("Error reading file.");
        }
    }

    @Override
    public void run() {
        try {
            sendMessage("Welcome! You are connected to the Server.");

            String clientMessage;
            while ((clientMessage = input.readLine()) != null) {
                System.out.println("Client: " + clientMessage);

                if (clientMessage.equalsIgnoreCase("SEND_FILE")) {
                    sendFile();
                } else {
                    sendMessage("Server received: " + clientMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
