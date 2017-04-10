package android.example.com.singlescreenapp.Intents;

import android.content.Context;
import android.content.Intent;

/**
 * Created by ByteTonight on 07.04.2017.
 */

public abstract class MyIntent
{
    protected Context context = null;

    private MyIntent()
    {

    }

    public MyIntent(Context c)
    {
        context = c;
    }

    public abstract Intent create();
}
