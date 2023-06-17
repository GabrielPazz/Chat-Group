// Author: João dos Santos Cardoso de Jesus - 12522161564

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Base
{
    private final ServerSocket serverSocket;
    public volatile ServerUtils serverUtils = new ServerUtils();

    public Server (ServerSocket serverSocket) 
    {
        this.serverSocket = serverSocket;
    }
    
    public static void main(String[] args) throws IOException 
    {
        System.out.println("Iniciando o servidor..");

        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
        
    }

    public void startServer()
    {      
        try 
        {
            Print("Aguardando novas conexoes em: 127.0.0.1:1234");

            while (!serverSocket.isClosed()) 
            {          
                Socket socket = serverSocket.accept();   
                
                ClientHandler clientHandler = new ClientHandler(socket, serverUtils);

                if(clientHandler.allowConnection)
                {
                    Print("Novo cliente conectado: " + clientHandler.clientUsername + ".");

                    Thread thread = new Thread(clientHandler);
                    thread.start();              
                }
                
            }
        } 
        catch (IOException e)
        {
            LogError("Server", "startServer.IOException", e);
        } 
        catch (Exception e)
        {
           LogError("Server", "startServer", e);
        }
    }

    public void closeServerSocket() 
    {
        try 
        {
            if (serverSocket != null) 
            {
                serverSocket.close();
            }
        }
        catch (IOException e) 
        {
            LogError("Server", "closeServerSocket.IOException", e);
        }
    }
}