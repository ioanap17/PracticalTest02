package ro.pub.cs.systems.eim.practicaltest02.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import ro.pub.cs.systems.eim.practicaltest02.Constants;
import ro.pub.cs.systems.eim.practicaltest02.Utilities;

/**
 * Created by student on 23.05.2017.
 */

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            String date = bufferedReader.readLine();
            String type = bufferedReader.readLine();
            if (date == null || date.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }
            HashMap<Integer, String> data = serverThread.getData();
            if (data.containsKey(date)) {
                if(type.equals("RESET")){
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Deleting data from hashmap...");
                    data.remove(date);
                }
            } else {
                if(type.equals("SET")) {
                    data.put(0, date);
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Adding data to hashmap...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}