package com.example.joystick;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.content.Context;
import android.widget.Adapter;

import java.io.IOException;

public class BluetoothManager {

    public enum State
    {
        CONNECTED, DISCONNECTED
    }

    private BluetoothAdapter adapter;
    private State state;

    public BluetoothManager(BluetoothAdapter adapter)
    {
        this.adapter = adapter;
        this.state = State.DISCONNECTED;

    }

    public void openConnection()
    {
        BluetoothServerSocket serverSocket = null;
        try
        {
            serverSocket = adapter.listenUsingRfcommWithServiceRecord(null,null);
        }
        catch (IOException ioe)
        {

        }

    }

    public void closeConnection()
    {

    }
}
