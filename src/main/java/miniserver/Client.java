package miniserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
  private Socket s;
  private BufferedWriter bwriter;
  private BufferedReader breader;
  private String username;
  private Scanner scanner;

  public Client() throws IOException {
    this.s = new Socket("localhost", 4999);
    this.scanner = new Scanner(System.in);
    System.out.printf("Who are you ?: ");
    this.username = scanner.nextLine();
    this.breader = new BufferedReader(new InputStreamReader(s.getInputStream()));
    this.bwriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
  }

  @Override
  public void run() {
    readIncomingMessages();
    handleMessageSending();
  }

  private void handleMessageSending() {
    sendMessage(username);
    while (s.isConnected())
      sendMessage(username + ": " + scanner.nextLine());
  }

  private void sendMessage(String msg) {
    try {
      bwriter.write(msg);
      bwriter.newLine();
      bwriter.flush();
    } catch (IOException e) {
      closeAll();
    }
  }

  private void readIncomingMessages() {
    new Thread(
        new Runnable() {
          @Override
          public void run() {
            while (s.isConnected()) {
              try {
                var msg = breader.readLine();
                if (msg == null)
                  break;
                System.out.println(msg);
              } catch (IOException e) {
                closeAll();
              }
            }
            System.out.println("server disconnected! \nclosing..");
          }
        }).start();
  }

  private void closeAll() {
    try {
      System.out.println("closing socket ...");
      if (s != null)
        s.close();
      if (breader != null)
        breader.close();
      if (bwriter != null)
        bwriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
