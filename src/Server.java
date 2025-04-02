import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final String FILES_PATH = "./files/server";
    private ServerSocket serverSocket;
    private static final int PORT = 3030;

    public Server(){
        try{
            serverSocket = new ServerSocket(PORT);
            acceptConnections();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void acceptConnections() {
        while(true){
            try{
                Socket clientSocket = serverSocket.accept();
                if(clientSocket.isConnected()){
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    new Thread(clientHandler).start();
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
