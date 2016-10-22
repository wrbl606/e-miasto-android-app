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

public class SuccessDialogFragment extends DialogFragment {

    String text;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static SuccessDialogFragment newInstance(String text) {
        SuccessDialogFragment f = new SuccessDialogFragment();

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
        View v = inflater.inflate(R.layout.success_dialog_fragment, container, false);
        TextView textView = (TextView) v.findViewById(R.id.message);
        textView.setText(text);

        View okButton = v.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

}
