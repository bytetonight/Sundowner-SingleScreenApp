package android.example.com.singlescreenapp.Intents;

import android.content.Context;
import android.content.Intent;
import android.example.com.singlescreenapp.R;
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

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.intentUriFacebook)));
            intent.setPackage(context.getString(R.string.packageFacebook));

            if (intent.resolveActivity(context.getPackageManager()) != null)
                return intent;

            String exMsg = String.format(context.getString(R.string.appNotAvailableMsg),
                    context.getString(R.string.appNameFacebook));
            throw new AppNotAvailableException(exMsg);
        }
        catch (AppNotAvailableException e)
        {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.webFacebook)));
        }
    }
}
