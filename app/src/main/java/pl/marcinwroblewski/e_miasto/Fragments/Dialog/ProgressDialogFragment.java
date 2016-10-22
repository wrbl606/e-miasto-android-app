package pl.marcinwroblewski.e_miasto.Fragments.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.marcinwroblewski.e_miasto.R;

/**
 * Created by Admin on 18.10.2016.
 */

public class ProgressDialogFragment extends DialogFragment {

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static ProgressDialogFragment newInstance() {
        ProgressDialogFragment f = new ProgressDialogFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setCancelable(false);
        return inflater.inflate(R.layout.progress_dialog_fragment, container, false);
    }

}
