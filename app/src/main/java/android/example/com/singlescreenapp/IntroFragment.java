package android.example.com.singlescreenapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFragment extends Fragment
{


    public IntroFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.intro_text, container, false);
        /*TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);*/
        return rootView;
    }

    public static IntroFragment newInstance(/* add your params here */)
    {
        IntroFragment fmc = new IntroFragment();
        Bundle args = new Bundle();
        /*args.putString(KEY_QUESTION, question);
        args.putStringArrayList(KEY_CHOICES, options);*/
        fmc.setArguments(args);
        return fmc;
    }
}
