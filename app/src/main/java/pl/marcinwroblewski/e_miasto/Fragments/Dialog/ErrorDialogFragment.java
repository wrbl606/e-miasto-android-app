package pl.marcinwroblewski.e_miasto.Fragments.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.marcinwroblewski.e_miasto.R;

/**
 * Created by Admin on 18.10.2016.
 */

public class ErrorDialogFragment extends DialogFragment {

    String text;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static ErrorDialogFragment newInstance(String text) {
        ErrorDialogFragment f = new ErrorDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("text", text);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.text = getArguments().getString("text");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.error_dialog_fragment, container, false);
        TextView textView = (TextView) v.findViewById(R.id.error_message);
        textView.setText(text);

        return v;
    }

}
