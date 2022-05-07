package com.example.joystick;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;

public class GradientMaker {
    public GradientMaker()
    {

    }

    public RadialGradient getJoystickDrawable(Context c)
    {
        Resources res = c.getResources();
        return new RadialGradient(
                res.getDimension(R.dimen.startOfStick),
                res.getDimension(R.dimen.startOfStick),
                res.getDimension(R.dimen.joystick_radius),
                new int[]
                        {
                                res.getColor(R.color.joystick),
                                res.getColor(R.color.joystick),
                                res.getColor(R.color.joystickBorder),
                                res.getColor(R.color.joystickBorder),
                                res.getColor(R.color.joystickBorder_light)
                        },
                new float[]
                        {
                                0,
                                res.getDimension(R.dimen.padEnd_joystick),
                                res.getDimension(R.dimen.halfwayOfEdge_joystick),
                                res.getDimension(R.dimen.nearEdge_joystick),
                                1.0f
                        },
                Shader.TileMode.CLAMP
        );
    }
}
