// Author: João dos Santos Cardoso de Jesus - 12522161564

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Base implements Runnable
{
    public Client(Socket socket, String username)
    {
        super(socket);
        
        this.clientUsername = username;       
    }

    public void sendMessage() 
    {
        try
        {
            bufferedWriter.write(clientUsername);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected())
            {
                String mensagem = scanner.nextLine();
                
                bufferedWriter.write(clientUsername + ": " + mensagem);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                if(mensagem.toLowerCase().contains("/sair"))
                {
                    System.out.print("\033[H\033[2J");  
                    System.out.flush();

                    bufferedWriter.write("/sair");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    Print("Você foi desconectado.");
                }
               
            }

            bufferedWriter.close();
            scanner.close();

        } 
        catch (IOException e) 
        {
            LogError("Client", "sendMessage.IOException", e);
            closeEverything(e);
        }
    }

    @Override
    public void run() 
    {
        String msgFromGroupChat;

        while (socket.isConnected()) 
        {
            try 
            {
                msgFromGroupChat = bufferedReader.readLine();

                if (msgFromGroupChat!=null && !msgFromGroupChat.isEmpty()) 
                {
                    if (msgFromGroupChat.toLowerCase().contains("/sair"))
                    {
                        closeEverything();                        
                        break;
                    }
                    else    
                    {
                        Print(msgFromGroupChat);
                    }
                }

            }
            catch (IOException e) 
            {
                LogError("Client", "Run.IOException", e);
                closeEverything(e);
            }

        }
    }
   
    public static void main(String[] args) throws IOException 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Servidor - Insira seu nome de usuario: ");   

        String username = scanner.nextLine();

        if(username!=null && !username.isEmpty())
        {          
            while(username.toLowerCase().contains("sair"))
            {
                System.out.println("Por favor, informe um nome de usuario valido:");
                username = scanner.nextLine();
            }
            try 
            {
                Socket socket = new Socket("localhost", 1234);            

                Client client = new Client(socket, username);

                Thread thread = new Thread(client);
                thread.start();

                System.out.print("\033[H\033[2J");  
                System.out.flush();

                client.sendMessage();

                scanner.close();
            }
            catch (Exception e)
            {
                if(e.getMessage().contains("Connection refused: connect"))
                {
                    System.out.println("Não foi possível realizar conexão ao localhost:1234");
                }            
            }
            
        }
        else   
        {
            System.out.println("username null ou empty");
        }
           
    }
}