// Author: João dos Santos Cardoso de Jesus - 12522161564

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Base implements Runnable
{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public ServerUtils serverUtils;
    public boolean allowConnection = true;

    public ClientHandler(Socket socket, ServerUtils utils) 
    {
        super(socket);

        try 
        {
            this.serverUtils = utils;
            
            this.clientUsername = bufferedReader.readLine();

            if(clientUsername!=null && !clientUsername.isEmpty())
            {
                boolean hasSameUser = hasUser(clientUsername);

                if(hasSameUser)
                {
                    sendMessageToSelf("Usuario '" + this.clientUsername + "' ja conectado neste momento.");

                    allowConnection = false;
                }
                else    
                {
                    clientHandlers.add(this);
                    this.serverUtils.clients.add(this.clientUsername);
                    
                    sendMessage("-------------------- Chat liberado, bem vindo " + this.clientUsername + "! --------------------", this.clientUsername);

                    broadcastMessage("O usuario '" + clientUsername + "' entrou na conversa!!!");
                }
                
            } 
        }
        catch (IOException e) 
        {
            LogError("ClientHandler", "Constructor.IOException", e);
            closeEverything(e);
        }
        catch(Exception e)
        {
            LogError("ClientHandler", "Constructor", e);
        }
    }

    @Override
    public void run() 
    {
        String messageFromClient;

        while (!socket.isClosed()) 
        {
            try 
            {
                messageFromClient = bufferedReader.readLine();

                if(messageFromClient.toLowerCase().contains("/sair"))
                {
                    int index = serverUtils.clients.indexOf(clientUsername);
                    if (index>=0)
                    {
                        serverUtils.clients.remove(index);     
                    }

                    if(messageFromClient.indexOf(":")>=0)
                    {
                        String user = messageFromClient.substring(0, messageFromClient.indexOf(":"));

                        broadcastMessage("O usuario '" + user  + "' saiu da conversa");
                    }
                }
                else
                {
                    broadcastMessage(messageFromClient);
                }
            }
            catch (IOException e) 
            {
                LogError("ClientHandler", "run.IOException", e);
                closeEverything(e);
            }
        }
    }

    public void broadcastMessage (String messageToSend) 
    {
        if(messageToSend!=null && !clientUsername.isEmpty())
        {
            for (ClientHandler clientHandler : clientHandlers) 
            {
                if(clientHandler.clientUsername!=null && !clientHandler.clientUsername.isEmpty() && clientUsername!=null && !clientUsername.isEmpty())
                {
                    try 
                    {
                        if (!clientHandler.clientUsername.equals(clientUsername)) 
                        {
                            clientHandler.bufferedWriter.write(messageToSend);
                            clientHandler.bufferedWriter.newLine();
                            clientHandler.bufferedWriter.flush();
                        }
                    } 
                    catch (IOException e) 
                    {
                        LogError("ClientHandler", "broadcastMessage.IOException", e);
                        closeEverything(e);
                    }
                }
                else
                {
                    removeClientHandler();
                }
            }
        }
    }

    public void sendMessage(String messageToBeSent, String clientToSendMessage) 
    {
        if(messageToBeSent!=null && !clientToSendMessage.isEmpty())
        {
            for (ClientHandler clientHandler : clientHandlers) 
            {
                if(clientHandler.clientUsername!=null && !clientHandler.clientUsername.isEmpty() && clientToSendMessage!=null && !clientToSendMessage.isEmpty())
                {
                    try 
                    {
                        if (clientHandler.clientUsername.equals(clientToSendMessage)) 
                        {
                            clientHandler.bufferedWriter.write(messageToBeSent);
                            clientHandler.bufferedWriter.newLine();
                            clientHandler.bufferedWriter.flush();
                        }
                    } 
                    catch (IOException e) 
                    {
                        LogError("ClientHandler", "sendMessage.IOException", e);
                        closeEverything(e);
                    }
                }
                else
                {
                    removeClientHandler();
                }
            }
        }
    }

    public void sendMessageToSelf(String messageToBeSent) throws IOException
    {
        this.bufferedWriter.write(messageToBeSent);
        this.bufferedWriter.newLine();
        this.bufferedWriter.flush();
    }
    
    public void removeClientHandler()    
    {
        ClientHandler.clientHandlers.remove(this);

        int index = serverUtils.clients.indexOf(clientUsername);
        if (index>=0)
        {
            serverUtils.clients.remove(index);
        }

        broadcastMessage("O usuário '" + clientUsername + "' saiu da conversa.");
    }

    public boolean hasUser (String username) 
    {
        boolean output = false;

        for (String item : this.serverUtils.clients) 
        {
            if(item.toLowerCase().equals((username.toLowerCase())))
            {
                output=true;
                break;
            }
        } 

        return output;
    }
}