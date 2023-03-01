import java.io.*;
import java.net.*;
import java.util.*;

public class ElectedMembersServer {
    
    private List<Member> electedMembers;
    
    public ElectedMembersServer(List<Member> electedMembers) {
        this.electedMembers = electedMembers;
    }

    // Method to start the server and listen for incoming client connections
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started. Listening on port " + serverSocket.getLocalPort() + "...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Inner class representing a client handler
    private class ClientHandler implements Runnable {
        
        private Socket clientSocket;
        
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        
        @Override
        public void run() {
            try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
                out.writeObject(electedMembers);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Client disconnected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // Inner class representing a member in the distributed system
    private static class Member implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        private long time;
        private int trust;
        
        public Member(long time, int trust) {
            this.time = time;
            this.trust = trust;
        }

        public long getTime() {
            return time;
        }

        public int getTrust() {
            return trust;
        }
    }
    
    // Example usage
    /*
    public static void main(String[] args) {
        
        List<Member> electedMembers = new ArrayList<>();
        electedMembers.add(new Member(100, 5));
        electedMembers.add(new Member(200, 4));
        electedMembers.add(new Member(150, 3));
        electedMembers.add(new Member(300, 2));
        electedMembers.add(new Member(250, 1));
        
        ElectedMembersServer server = new ElectedMembersServer(electedMembers);
        server.start();
    }
    */
}
