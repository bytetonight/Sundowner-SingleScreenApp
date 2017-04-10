package android.example.com.singlescreenapp.Intents;

import android.content.Context;
import android.content.Intent;
import android.example.com.singlescreenapp.exceptions.AppNotAvailableException;
import android.net.Uri;

/**
 * Created by ByteTonight on 07.04.2017.
 */

public class MyOwnFacebook extends MyIntent
{


    public MyOwnFacebook(Context c)
    {
        super(c);
    }


    @Override
    public Intent create()
    {
        try
        {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://root"));
            intent.setPackage("com.facebook.katana");

            if (intent.resolveActivity(context.getPackageManager()) != null)
                return intent;

            throw new AppNotAvailableException("Google Maps not available");
        }
        catch (AppNotAvailableException e)
        {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
        }
    }
}
