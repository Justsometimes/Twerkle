package Client;

import Qwirkle.Tile;
import player.Player;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;

/**
 * Created by Jan-Willem on 30-1-2016.
 */
public class Client implements Runnable{

    private int port;
    private Socket sock;
    private String hostname;
    private BufferedReader in;
    private BufferedWriter out;
    private boolean running;
    private boolean joined;
    private Player player;
    private int playernum;
    private int AItime;


    public Client(){
        this("localhost", 4444, new Player("Bert", new HashSet<Tile>()));
    }

    public Client(String address, int port, Player player) {
        this.player = player;
        this.hostname = address;
        this.port = port;
        running = true;
        joined = false;

        try {
            sock = new Socket(address, port);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        while (running) {
            try {
                if (!joined) {
                    sendHello();
                }
                String lline;
                while ((lline = in.readLine()) != null) {
                    String[] elements = lline.split(" ");
                    switch (elements[0]) {
                        case "WELCOME":
                            readWelcome(elements);
                            break;
                        case "NAMES":
                            readNames(elements);
                            break;
                        case "NEXT":
                            readNext(elements);
                            break;
                        case "TURN":
                            readTurn(elements);
                            break;
                        case "KICK":
                            readKick(elements);
                            break;
                        case "WINNER":
                            readWinner(elements);
                            break;
                        default:
                            break;
                    }
                    // implement commands to be send?;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void readWinner(String[] elements) {
        if (elements.length == 2) {
            if (Integer.parseInt(elements[2]) == playernum) {
                System.out.println("You won!");
            } else {
                System.out.println("You lost! Winning player: " + elements[1]);
            }
        }
    }

    private void readKick(String[] elements) {
        //TODO
    }

    private void readTurn(String[] elements) {
        //TODO
    }

    private void readNext(String[] elements) {
        if (elements.length == 2) {
            if (Integer.parseInt(elements[2]) == playernum) {
                makeMove();
            }
        }
    }

    private void makeMove() {
        //TODO
    }

    private void readNames(String[] elements) {
        if(elements.length % 2 == 0  && elements.length >= 4) {
            for (int i = 0; i < (elements.length - 2) / 2; i++) {
                System.out.println("Player: " + elements[i*2 + 2] + " - " + elements[i*2+1] + " connected");
            }
            AItime = Integer.parseInt(elements[elements.length - 1]);
            System.out.println("AITime = " + AItime);
        }
    }

    private void readWelcome(String[] elements) {
        if (elements.length == 3) {
            if (elements[1].equals(player.getName())) {
                joined = true;
                playernum = Integer.parseInt(elements[2]);
            }
        }
    }

    public void sendHello() {
        try {
            out.write("HELLO " + player.getName());
            Thread.sleep(100);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client().run();
    }
}
