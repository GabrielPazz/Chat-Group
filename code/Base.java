
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public abstract class Base {
    protected Socket socket;
    protected BufferedReader bufferedReader;
    protected BufferedWriter bufferedWriter;
    protected String clientUsername;
    protected static int socketPort;

    public Base(Socket socket) {
        super();

        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            LogError("Base", "Constructor", e);
            closeEverything(e);
        }
    }

    public Base() {
        super();
    }

    protected void Print(String input) {
        System.out.println(input);
    }

    protected void closeEverything(IOException e) {
        try {
            closeEverything();
        } catch (IOException errException) {
            LogError("Base", "closeEverything", e);
        }
    }

    protected void closeEverything() throws IOException {
        if (bufferedReader != null) {
            bufferedReader.close();
        }

        if (bufferedWriter != null) {
            bufferedWriter.close();
        }

        if (socket != null) {
            socket.close();
        }
    }

    protected void LogError(String className, String methodName, Exception e) {
        String erroMessage = "Ocorreu um erro na classe " + className + ".Method:" + methodName + ".Details:"
                + e.getMessage() + " - " + e.toString();

        Print(erroMessage);

    }
}
