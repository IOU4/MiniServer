package miniserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
  public static void main(String[] args) throws IOException {
    printMenu();

    switch (getChoice()) {
      case 1:
        handleServer();
        break;
      case 2:
        handleClient();
        break;
      default:
        break;
    }
  }

  private static void printMenu() {
    System.out.println("welcome to miniserver:");
    System.out.println("1 - launch server.");
    System.out.println("2 - launch client.");
    System.out.println("anythign else will close the app.");
  }

  private static int getChoice() throws IOException {
    var input = new BufferedReader(new InputStreamReader(System.in));
    return input.read() - 48;
  }

  private static void handleClient() throws IOException {
    new Client().run();
  }

  private static void handleServer() throws IOException {
    new Server().run();
  }
}
