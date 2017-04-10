package android.example.com.singlescreenapp.Intents;

import android.content.Context;
import android.content.Intent;
import android.example.com.singlescreenapp.R;
import android.net.Uri;

/**
 * Created by ByteTonight on 07.04.2017.
 */

public class MyOwnPhoneCall extends MyIntent
{
    public MyOwnPhoneCall(Context c)
    {
        super(c);
    }


    @Override
    public Intent create()
    {
        Intent intent;
        intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(context.getString(R.string.action_call)));
        return intent;
    }
}
