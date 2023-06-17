// Author: João dos Santos Cardoso de Jesus - 12522161564

import java.util.concurrent.CopyOnWriteArrayList;

public class ServerUtils
{
    public volatile CopyOnWriteArrayList <String> clients = new CopyOnWriteArrayList <>();
}