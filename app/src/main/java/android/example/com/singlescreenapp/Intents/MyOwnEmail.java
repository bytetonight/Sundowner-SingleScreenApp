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
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {context.getString(R.string.action_email)});
            if (intent.resolveActivity(context.getPackageManager()) != null)
                return intent;

            throw new AppNotAvailableException("No Email client installed or configured");
        }
        catch (AppNotAvailableException e)
        {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/gmail/"));
        }
    }
}
