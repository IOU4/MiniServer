package miniserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
  private ServerSocket ss;
  private HashMap<String, Socket> clients;

  public Server() {
    try {
      this.ss = new ServerSocket(4999);
      this.clients = new HashMap<>();
    } catch (IOException ex) {
      System.err.println(ex.getMessage());
    }
  }

  public void run() {
    try {
      System.out.println("server started ...");
      while (!ss.isClosed()) {
        var s = ss.accept();
        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              handleClient(s);
            } catch (IOException e) {
              closeSocket(s);
            }
          }
        }).start();
      }
    } catch (IOException e) {
      closeServer();
    }
  }

  private void handleClient(Socket s) throws IOException {
    var reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
    var username = reader.readLine();
    clients.put(username, s);
    System.out.println("Server: '" + username + "' joined the server.");
    messageToAll("Server: '" + username + "' joined the server.", username);
    while (s.isConnected()) {
      String msg = reader.readLine();
      if (msg == null)
        break;
      messageToAll(msg, username);
    }
    clients.get(username).close();
    clients.remove(username);
    messageToAll("server: '" + username + "' has left", username);
    System.out.println("server: '" + username + "' has left");
  }

  private void messageToAll(String msg, String sender) throws IOException {
    clients.forEach((username, s) -> {
      try {
        // ignore sender
        if (!username.equals(sender)) {
          var writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
          writer.write(msg);
          writer.newLine();
          writer.flush();
        }
      } catch (IOException e) {
        closeSocket(s);
      }
    });
  }

  private void closeServer() {
    try {
      if (ss != null)
        ss.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private void closeSocket(Socket socket) {
    try {
      if (socket != null)
        socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
