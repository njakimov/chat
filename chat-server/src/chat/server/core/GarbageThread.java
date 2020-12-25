package chat.server.core;

import chat.network.SocketThread;

import java.util.Date;
import java.util.Vector;

public class GarbageThread extends Thread {
    private Vector<SocketThread> clients;
    private ChatServer chatServer;

    public GarbageThread(ChatServer chatServer, Vector<SocketThread> clients, String name) {
        super(name);
        this.clients = clients;
        this.chatServer = chatServer;
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                long currentDateTime = new Date().getTime();
                for (int i = 0; i < clients.size(); i++) {
                    if((currentDateTime - clients.get(i).getDateTimeStartThread().getTime())>120000 && !((ClientThread) clients.get(i)).isAuthorized()) {
                        chatServer.putLog("TimeOut authorization > 12с");
                        clients.get(i).close();
                    }else {
                        chatServer.putLog("" + (currentDateTime - clients.get(i).getDateTimeStartThread().getTime()));
                        chatServer.putLog("TimeOut authorization < 12с");
                    }
                }

                sleep(2000);
                chatServer.putLog("WaitCheckTimeOutAuthorization");
            }
        } catch (InterruptedException e) {
            chatServer.putLog("ErrorCheckTimeOutAuthorization");
            chatServer.putLog(e.getMessage());
        }
    }



}
