package com.example.joystick;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoystickFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoystickFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = "JoystickFragment";
    private double[] currentLocation;
    private BluetoothManager connectionManager;
    // TODO: Rename and change types of parameters


    public JoystickFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static JoystickFragment newInstance() {
        JoystickFragment fragment = new JoystickFragment();
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
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_joystick, container, false);
        currentLocation = new double[2];
        View joyStick = v.findViewById(R.id.joystick);
        FrameLayout xAxis = (FrameLayout) v.findViewById(R.id.xAxis);
        FrameLayout yAxis = (FrameLayout) v.findViewById(R.id.yAxis);

        ArrowView xArrow = new ArrowView(getContext());
        xArrow.setColor(getResources().getColor(R.color.xAxis));
        xArrow.setVertical(false);
        xArrow.setRatio(0);

        ArrowView yArrow = new ArrowView(getContext());
        yArrow.setVertical(true);
        yArrow.setColor(getResources().getColor(R.color.yAxis));
        yArrow.setRatio(0);

        xAxis.addView(xArrow);
        yAxis.addView(yArrow);

        Log.d(TAG, "Default margin - " + ((ViewGroup.MarginLayoutParams)joyStick.getLayoutParams()).leftMargin);
        boolean[] isActive = new boolean[1];
        final int padMargin = getResources().getDimensionPixelSize(R.dimen.padMargin);
        final int stickSize = getResources().getDimensionPixelSize(R.dimen.joystick_radius) / 2;
        final int midpoint = getResources().getDimensionPixelSize(R.dimen.defaultMargin) + stickSize;

        View mainPad = v.findViewById(R.id.mainPad);
//        ScrollView scrollView = new ScrollView(getApplicationContext());
//        scrollView.drag
        mainPad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                int midpoint = mainPad.getWidth() / 2;
                float xCoord = motionEvent.getX();
                float yCoord = motionEvent.getY();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) joyStick.getLayoutParams();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN
                        || (motionEvent.getAction() == MotionEvent.ACTION_MOVE && isActive[0]))
                {
                    isActive[0] = true;
                    params.leftMargin = (int) xCoord - stickSize;
                    params.topMargin = (int) yCoord - stickSize;
                    Log.d(TAG, "Coordinates -- " + xCoord + " , " + yCoord);
                    Log.d(TAG, "Final results -- " + params.leftMargin + " , " + params.topMargin);

                    if (params.leftMargin < padMargin - stickSize)
                    {
                        params.leftMargin = padMargin - stickSize;
                    }
                    else if (params.leftMargin > mainPad.getWidth() - padMargin - stickSize)
                    {
                        params.leftMargin = mainPad.getWidth() - padMargin - stickSize;
                    }

                    if (params.topMargin < padMargin - stickSize)
                    {
                        params.topMargin = padMargin - stickSize;
                    }
                    else if (params.topMargin > mainPad.getHeight() - padMargin - stickSize)
                    {
                        params.topMargin = mainPad.getHeight() - padMargin - stickSize;
                    }
                }
                else
                {
                    isActive[0] = false;
                    params.leftMargin = midpoint - stickSize;
                    params.topMargin = midpoint - stickSize;

                }
                joyStick.setLayoutParams(params);

                int midPointOfJoystick = midpoint - padMargin;
                double relativeLeft = params.leftMargin + stickSize - padMargin;
                double relativeTop = params.topMargin + stickSize - padMargin;
                Log.d(TAG, "" + (params.leftMargin + stickSize - padMargin) + ", " + midpoint);
                currentLocation[0] = (relativeLeft - midPointOfJoystick) / midPointOfJoystick;
                currentLocation[1] = (relativeTop - midPointOfJoystick) / midPointOfJoystick;
                xArrow.setRatio(currentLocation[0]);
                yArrow.setRatio(currentLocation[1]);
                updatePoints(currentLocation);
//                Log.d(TAG, "Final results -- " + params.leftMargin + " , " + params.topMargin);
//
//                Log.d(TAG, findViewById(R.id.mainPad).getWidth() + ", " + findViewById(R.id.mainPad).getHeight());
//                Log.d(TAG, maxSize + "");
//                Log.d(TAG, maxSize + padMargin - stickSize + " = ");
                return true;
            }
        });
        return v;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bluetooth_connection_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.connect:
                DialogFragment fragment = FindDeviceFragment.newInstance();
                fragment.setTargetFragment(this, FindDeviceFragment.DEVICE_LOGIN);
                fragment.show(getFragmentManager(), null);
                break;
            case R.id.connectionStatus:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case FindDeviceFragment.DEVICE_LOGIN:
                if (resultCode == Activity.RESULT_OK)
                {
                    String address = data.getStringExtra(FindDeviceFragment.DEVICE_ADDRESS_KEY);
                    if (connectionManager != null)
                    {
                        connectionManager.closeConnection();
                    }
                    this.connectionManager = new BluetoothManager(BluetoothAdapter.getDefaultAdapter(), address);
                    this.connectionManager.openConnection();
                }
                break;
        }
    }

    public void updatePoints(double[] data)
    {
        if (connectionManager != null)
        {
            connectionManager.sendData(data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connectionManager != null)
        {
            connectionManager.closeConnection();

        }
    }
}