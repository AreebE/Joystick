package com.example.joystick;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FindDeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindDeviceFragment extends DialogFragment {
    public static final int DEVICE_LOGIN = 12921;
    public static final String DEVICE_ADDRESS_KEY = "device address";

    private BroadcastReceiver receiver;

    private class DeviceItem {
        private String name;
        private String address;

        public DeviceItem(String name, String address) {
            this.name = name;
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public String getName() {
            return name;
        }
    }

    private class DeviceAdapter extends ArrayAdapter<DeviceItem> {
        public DeviceAdapter(@NonNull Context context, List<DeviceItem> items) {
            super(context, R.layout.device_item, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.device_item, null);
            }
            ((TextView) convertView.findViewById(R.id.deviceName)).setText(getItem(position).getName());
            return convertView;
        }
    }

    public FindDeviceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FindDeviceFragment newInstance() {
        FindDeviceFragment fragment = new FindDeviceFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_find_device, container, false);
        ListView listView = (ListView) v.findViewById(R.id.listView);
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        @SuppressLint("MissingPermission") Set<BluetoothDevice> connectedDevices = adapter.getBondedDevices();
        List<DeviceItem> items = new ArrayList<>();

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String actionDone = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(actionDone))
                {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    items.add(new DeviceItem(device.getAlias(), device.getAddress()));
                }
            }
        };
        getActivity().registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        adapter.startDiscovery();

        for (BluetoothDevice device: connectedDevices)
        {
            items.add(new DeviceItem(device.getAlias(), device.getAddress()));
        }
        listView.setAdapter(new DeviceAdapter(getContext(), items));
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = onCreateView(getLayoutInflater(), null, savedInstanceState);
        ListView listView = (ListView) v.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent data = new Intent();
                data.putExtra(DEVICE_ADDRESS_KEY, ((DeviceItem) adapterView.getItemAtPosition(i)).getAddress());
                getTargetFragment().onActivityResult(DEVICE_LOGIN, Activity.RESULT_OK, data);
                dismiss();
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(v)
                .create();
        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().unregisterReceiver(receiver);
    }


}