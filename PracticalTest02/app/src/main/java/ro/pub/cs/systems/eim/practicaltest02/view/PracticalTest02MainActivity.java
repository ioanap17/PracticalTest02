package ro.pub.cs.systems.eim.practicaltest02.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02.Constants;
import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText dateEditText = null;
    private Button setButton = null;
    private Button resetButton = null;
    private Button pollButton = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private SetButtonClickListener setButtonClickListener = new SetButtonClickListener();
    private class SetButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            //String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = serverPortEditText.getText().toString();
            if (clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String date = dateEditText.getText().toString();
            if (date == null || date.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            clientThread = new ClientThread(Integer.parseInt(clientPort), date, "SET");
            clientThread.start();
        }
    }

        private ResetButtonClickListener resetButtonClickListener = new ResetButtonClickListener();
        private class ResetButtonClickListener implements Button.OnClickListener {

            @Override
            public void onClick(View view) {
                //String clientAddress = clientAddressEditText.getText().toString();
                String clientPort = serverPortEditText.getText().toString();
                if (clientPort == null || clientPort.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (serverThread == null || !serverThread.isAlive()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String date = dateEditText.getText().toString();
                if (date == null || date.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                clientThread = new ClientThread(Integer.parseInt(clientPort), date, "RESET");
                clientThread.start();
            }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.editText_port);
        connectButton = (Button)findViewById(R.id.button_start);
        connectButton.setOnClickListener(connectButtonClickListener);

        dateEditText = (EditText)findViewById(R.id.editText);

        setButton = (Button)findViewById(R.id.button_set);
        setButton.setOnClickListener(setButtonClickListener);
        resetButton = (Button)findViewById(R.id.button_reset);
        resetButton.setOnClickListener(resetButtonClickListener);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

}
