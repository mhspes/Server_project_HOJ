import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class SovellusX {
  
  public static final int PORT_Y = 3126;
  
  // Monitoriobjekti
  public synchronized static void main(String[] args) throws Exception {
    
    if (args.length != 1 ){
      System.out.println("Syötä parametriksi koneen osoite");
      System.exit(0);
    }
    
    try {
     
      Yhteys yhteys = new Yhteys(args[0],PORT_Y); // Datagram-yhteys
      Socket vierasSocket = yhteys.yhdista();     // Luodaan TCP-yhteys
        if ( vierasSocket != null ){
      System.out.println("Yhteydenotto osoitteesta " + vierasSocket.getInetAddress() + " portti: " + vierasSocket.getPort());    
      System.out.println("Oma portti: " + vierasSocket.getLocalPort());
      Streamer streamer = new Streamer(vierasSocket); // Yhteyden hallinta, summauspalvelinten luonti
      streamer.portListener(); // Porttien lähetys, yhteyden muodostukset näihin ym..
      
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }
  }
}