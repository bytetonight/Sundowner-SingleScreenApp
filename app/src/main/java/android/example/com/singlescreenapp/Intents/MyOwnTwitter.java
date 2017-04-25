package android.example.com.singlescreenapp.Intents;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.example.com.singlescreenapp.R;
import android.example.com.singlescreenapp.exceptions.AppNotAvailableException;
import android.net.Uri;

/**
 * Created by ByteTonight on 07.04.2017.
 */

public class MyOwnTwitter extends MyIntent
{

    public MyOwnTwitter(Context c)
    {
        super(c);
    }


    @Override
    public Intent create()
    {
        Intent intent;
        try
        {
            // get the Twitter app if possible
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://root"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(context.getPackageManager()) != null)
                return intent;

            String exMsg = String.format(context.getString(R.string.appNotAvailableMsg),
                    context.getString(R.string.appNameTwitter));
            throw new AppNotAvailableException(exMsg);
        }
        catch (PackageManager.NameNotFoundException n)
        {

        }
        catch (AppNotAvailableException e)
        {

        }
        finally
        {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(context.getString(R.string.webTwitter)));
            return intent;
        }
    }
}
