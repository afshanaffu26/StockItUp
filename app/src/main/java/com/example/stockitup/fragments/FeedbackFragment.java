package com.example.stockitup.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stockitup.R;
import com.example.stockitup.utils.AppConstants;

/**
 * This class deals with feedback of application
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment implements View.OnClickListener {

    private Button buttonSend,buttonDiscard;
    private EditText editTextMultiLine;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Non-parameterized constructor
     * */
    public FeedbackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedbackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to do initial creation of a fragment
     * Note that this can be called while the fragment's activity is still in the process of being created. As such, you can not rely on things like the activity's content view hierarchy being initialized at this point. If you want to do work once the activity itself is created, add a {@link androidx.lifecycle.LifecycleObserver} on the activity's Lifecycle, removing it when it receives the Lifecycle.State.CREATED callback.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and non-graphical fragments can return null. This will be called between onCreate(Bundle) and onViewCreated(View, Bundle).
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feedback, container, false);

        buttonSend = v.findViewById(R.id.buttonSend);
        buttonDiscard = v.findViewById(R.id.buttonDiscard);
        editTextMultiLine = v.findViewById(R.id.editTextMultiLine);

        buttonSend.setOnClickListener(this);
        buttonDiscard.setOnClickListener(this);

        return v;
    }

    /**
     * This method sends email via Email app with provided data.
     */
    private void sendEmailWithoutChooser() {
        String email = AppConstants.ADMIN_EMAIL;
        String feedback_msg = editTextMultiLine.getText().toString().trim();
        if(feedback_msg.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your experience..", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        String aEmailList[] = {email};
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<i><font color='your color'>" + feedback_msg + "</font></i>"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");

        PackageManager packageManager = getActivity().getPackageManager();
        boolean isIntentSafe = emailIntent.resolveActivity(packageManager) != null;
        if (isIntentSafe) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(getActivity(), "Email app is not found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method send email using third party applications to choose from native android chooser
     */
    protected void sendEmailWithChooser() {
        String feedbackMessage = editTextMultiLine.getText().toString().trim();
        if(feedbackMessage.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your experience..", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { AppConstants.ADMIN_EMAIL });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        intent.putExtra(Intent.EXTRA_TEXT, feedbackMessage);
        startActivity(Intent.createChooser(intent, ""));
    }

    /**
     * Called when a view has been clicked.
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.buttonSend:
                sendEmailWithoutChooser();
                break;
            case R.id.buttonDiscard:
                discardEmail();
                break;
        }
    }

    /**
     * This method discards the entered feedback
     */
    private void discardEmail() {
        editTextMultiLine.setText("");
    }
}