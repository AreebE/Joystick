package com.example.joystick;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Adapter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager {

    private static final String TAG = "BluetoothManager";

    public enum State
    {
        CONNECTED, DISCONNECTED
    }

    private BluetoothAdapter adapter;
    private State state;
    private String deviceAddress;
    private BluetoothSocket socket;
    private OutputStream outputStream;

    public BluetoothManager(BluetoothAdapter adapter, String deviceAddress)
    {
        this.adapter = adapter;
        this.state = State.DISCONNECTED;
        this.deviceAddress = deviceAddress;
    }

    public void openConnection()
    {
        socket = null;
        try
        {
            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            for (BluetoothDevice device: devices) {
                if (device.getAddress().equals(deviceAddress)) {
                    Log.d(TAG, "found device.");
                    socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    Log.d(TAG, "found ." + socket);
//                    socket.close();
                    adapter.cancelDiscovery();
//                    socket.
                    break;
                }
            }

            socket.connect().;
            outputStream = socket.getOutputStream();
        }
        catch (IOException ioe)
        {
            Log.d(TAG, ioe.toString());
        }
    }

    public void sendData(double[] ratios)
    {
        if (socket != null && socket.isConnected())
        {
            try
            {
                String amounts = ratios[0] + " ; " + ratios[1];
                outputStream.write(amounts.getBytes());
                Log.d(TAG, "Theoretically sneind data");
            }
            catch (IOException ioe)
            {
                Log.d(TAG, ioe.toString());
            }

        }

    }

    public void closeConnection()
    {
        try
        {
            socket.close();
        } catch (IOException|NullPointerException ioException)
        {

        }
    }
}
