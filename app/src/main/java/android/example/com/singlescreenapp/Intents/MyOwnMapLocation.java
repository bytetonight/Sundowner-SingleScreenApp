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
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri intentUri = Uri.parse(context.getString(R.string.intentUriGoogleMaps));

        try
        {
            Intent intent;

            // Create an Intent from intentUri. Set the action to ACTION_VIEW
            intent = new Intent(Intent.ACTION_VIEW, intentUri);
            // Make the Intent explicit by setting the Google Maps package
            intent.setPackage(context.getString(R.string.packageGoogleMaps));

            if (intent.resolveActivity(context.getPackageManager()) != null)
                return intent;

            String exMsg = String.format(context.getString(R.string.appNotAvailableMsg),
                    context.getString(R.string.appNameGoogleMaps));
            throw new AppNotAvailableException(exMsg);
        }
        catch (AppNotAvailableException e)
        {
            return new Intent(Intent.ACTION_VIEW, intentUri);
        }
    }
}
