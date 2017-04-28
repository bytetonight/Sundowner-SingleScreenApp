package android.example.com.singlescreenapp;

import android.app.Fragment;
import android.content.res.AssetFileDescriptor;
import android.example.com.singlescreenapp.Intents.MyIntent;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnTouchListener
{
    // Log tag
    private static final String TAG = MainActivity.class.getName();
    // Asset video file name
    private static final String BACKGROUND_VIDEO = "silk_1.mp4";
    // MediaPlayer instance to control playback of video file.
    private MediaPlayer mMediaPlayer;
    private TextureView mTextureView;
    private Rect rect;
    private Boolean processClick = false;
    private ImageButton btnFacebook;
    private ImageButton btnTwitter;
    private ImageButton btnPhoneCall;
    private ImageButton btnEmail;
    private ImageButton btnMap;
    private float mVideoWidth;
    private float mVideoHeight;
    private IntroFragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Keep the Screen from dimming without requesting permissions in manifest.
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        prepareMembers();
        rigTouchables();
        calculateVideoSize();
        initView();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        updateTextureViewSize(width, height);

        currentFragment = IntroFragment.newInstance(/* add your params here*/);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        ft.replace(R.id.fragment_container, currentFragment, null);
        ft.commit();
    }

    private void prepareMembers()
    {
        btnFacebook = (ImageButton) findViewById(R.id.btn_facebook);
        btnTwitter = (ImageButton) findViewById(R.id.btn_twitter);
        btnPhoneCall = (ImageButton) findViewById(R.id.btn_phone);
        btnEmail = (ImageButton) findViewById(R.id.btn_mail);
        btnMap = (ImageButton) findViewById(R.id.btn_map);
    }

    private void rigTouchables()
    {
        btnFacebook.setOnTouchListener(this);
        btnTwitter.setOnTouchListener(this);
        btnPhoneCall.setOnTouchListener(this);
        btnEmail.setOnTouchListener(this);
        btnMap.setOnTouchListener(this);
    }


    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (v instanceof ImageButton)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN: /** Pressed & Held */
                    processClick = true;
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    int imageButtonHighLight = ContextCompat.getColor(
                            MainActivity.this, R.color.imageButtonHighLight);

                    ((ImageButton) v).setColorFilter(
                            Color.argb(
                            Color.alpha(imageButtonHighLight),
                            Color.red(imageButtonHighLight),
                            Color.green(imageButtonHighLight),
                            Color.blue(imageButtonHighLight))
                    ); // Set Tint from colors.xml

                    return true; // if you want to handle the touch event

                case MotionEvent.ACTION_MOVE: /** finger slide */
                    if (!rect.contains(v.getLeft() + (int) event.getX(),
                            v.getTop() + (int) event.getY()))
                    {
                        // User moved outside bounds
                        processClick = false;
                        ((ImageButton) v).clearColorFilter(); // No Tint
                    }
                    break;

                case MotionEvent.ACTION_UP: /** Released */
                    ((ImageButton) v).clearColorFilter(); // No Tint
                    if (processClick)
                        syndicationAction(v);

                    return true; // if you want to handle the touch event
            }
        }
        return false;
    }

    private void syndicationAction(View v)
    {
        if (v instanceof ImageButton)
        {

            String targetClass = v.getTag().toString();
            String fullyQualifiedClassName;

            // Just making sure no Classes are being called that aren't supposed to be
            /*if (!Arrays.asList(allowedIntents).contains(targetClass))
                return;*/

            /**
             Only Classes starting with the prefix "MyOwn" can be instantiated here,
             to prevent accidental or intentional injection and execution of
             potentially dangerous code (I think)
             */
            if (!targetClass.startsWith("MyOwn"))
                return;

            fullyQualifiedClassName = getPackageName() + ".Intents." + targetClass;

            try
            {

                /**
                 A somewhat different approach to RIP Pattern (Replace If with Polymorphism)
                 This approach allows me to add unlimited intents without changing this code,
                 and without using loads of IF statements or switch.
                 */

                Class<?> classToLoad = Class.forName(fullyQualifiedClassName);
                Constructor<?> con = classToLoad.getConstructors()[0];
                MyIntent intendedIntent = (MyIntent) con.newInstance(MainActivity.this);
                startActivity(intendedIntent.create());
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void initView()
    {
        mTextureView = (TextureView) findViewById(R.id.textureView);
        // SurfaceTexture is available only after the TextureView
        // is attached to a window and onAttachedToWindow() has been invoked.
        // We need to use SurfaceTextureListener to be notified when the SurfaceTexture
        // becomes available.
        mTextureView.setSurfaceTextureListener(this);
    }


    private void updateTextureViewSize(int viewWidth, int viewHeight)
    {
        float scaleX = 1.0f;
        float scaleY = 1.0f;

        if (mVideoWidth > viewWidth && mVideoHeight > viewHeight)
        {
            scaleX = mVideoWidth / viewWidth;
            scaleY = mVideoHeight / viewHeight;
        }
        else if (mVideoWidth < viewWidth && mVideoHeight < viewHeight)
        {
            scaleY = viewWidth / mVideoWidth;
            scaleX = viewHeight / mVideoHeight;
        }
        else if (viewWidth > mVideoWidth)
        {
            scaleY = (viewWidth / mVideoWidth) / (viewHeight / mVideoHeight);
        }
        else if (viewHeight > mVideoHeight)
        {
            scaleX = (viewHeight / mVideoHeight) / (viewWidth / mVideoWidth);
        }

        // Calculate pivot points, in our case crop from center
        int pivotPointX = viewWidth / 2;
        int pivotPointY = viewHeight / 2;

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY, pivotPointX, pivotPointY);

        mTextureView.setTransform(matrix);
        mTextureView.setLayoutParams(new RelativeLayout.LayoutParams(viewWidth, viewHeight));
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mMediaPlayer != null)
        {
            // Make sure we stop video and release resources when activity is destroyed.
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2)
    {
        final Surface surface = new Surface(surfaceTexture);

        try
        {
            AssetFileDescriptor afd = getAssets().openFd(BACKGROUND_VIDEO);
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer
                    .setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setLooping(true);

            // don't forget to call MediaPlayer.prepareAsync() method when you use constructor for
            // creating MediaPlayer
            mMediaPlayer.prepareAsync();

            // Play video when the media source is ready for playback.
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer)
                {
                    //mediaPlayer.setScreenOnWhilePlaying(true);
                    mediaPlayer.start();
                    //startAnimations();
                }
            });
        }
        catch (IllegalArgumentException e)
        {
            Log.d(TAG, e.getMessage());
        }
        catch (SecurityException e)
        {
            Log.d(TAG, e.getMessage());
        }
        catch (IllegalStateException e)
        {
            Log.d(TAG, e.getMessage());
        }
        catch (IOException e)
        {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2)
    {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture)
    {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture)
    {
    }

    private void calculateVideoSize()
    {
        try
        {
            AssetFileDescriptor afd = getAssets().openFd(BACKGROUND_VIDEO);
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(
                    afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            String height = metaRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            String width = metaRetriever
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            mVideoHeight = Float.parseFloat(height);
            mVideoWidth = Float.parseFloat(width);
        }
        catch (IOException e)
        {
            Log.d(TAG, e.getMessage());
        }
        catch (NumberFormatException e)
        {
            Log.d(TAG, e.getMessage());
        }
    }
}
