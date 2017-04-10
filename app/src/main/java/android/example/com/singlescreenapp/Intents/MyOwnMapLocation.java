package android.example.com.singlescreenapp.Intents;

import android.content.Context;
import android.content.Intent;
import android.example.com.singlescreenapp.R;
import android.example.com.singlescreenapp.exceptions.AppNotAvailableException;
import android.net.Uri;

/**
 * Created by ByteTonight on 07.04.2017.
 */

public class MyOwnMapLocation extends MyIntent
{

    public MyOwnMapLocation(Context c)
    {
        super(c);
    }

    @Override
    public Intent create()
    {
        try
        {
            Intent intent;
            // Create a Uri from an intent string. Use the result to create an Intent.
            Uri gmmIntentUri = Uri.parse(context.getString(R.string.action_map));

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            // Make the Intent explicit by setting the Google Maps package
            intent.setPackage("com.google.android.apps.maps");

            if (intent.resolveActivity(context.getPackageManager()) != null)
                return intent;

            throw new AppNotAvailableException("Google Maps not available");
        }
        catch (AppNotAvailableException e)
        {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.action_map)));
        }
    }
}
