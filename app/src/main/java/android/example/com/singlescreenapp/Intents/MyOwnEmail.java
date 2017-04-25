package android.example.com.singlescreenapp.Intents;

import android.content.Context;
import android.content.Intent;
import android.example.com.singlescreenapp.exceptions.AppNotAvailableException;
import android.example.com.singlescreenapp.R;
import android.net.Uri;

/**
 * Created by ByteTonight on 07.04.2017.
 */

public class MyOwnEmail extends MyIntent
{
    public MyOwnEmail(Context c)
    {
        super(c);
    }


    @Override
    public Intent create()
    {
        try
        {
            Intent intent;
            intent = new Intent(Intent.ACTION_SENDTO);
            // only email apps should handle the mailto Uri
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getString(R.string.action_email)});
            intent.setData(Uri.parse(context.getString(R.string.intentUriEmail)));
            if (intent.resolveActivity(context.getPackageManager()) != null)
                return intent;

            String exMsg = String.format(context.getString(R.string.appNotAvailableMsg),
                    context.getString(R.string.appNameEmail));
            throw new AppNotAvailableException(exMsg);
        }
        catch (AppNotAvailableException e)
        {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.webEmail)));
        }
    }
}
