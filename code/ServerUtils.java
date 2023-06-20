
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerUtils {
    public volatile CopyOnWriteArrayList<String> clients = new CopyOnWriteArrayList<>();
}