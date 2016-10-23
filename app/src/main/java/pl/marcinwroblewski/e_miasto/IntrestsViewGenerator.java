package pl.marcinwroblewski.e_miasto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Marcin Wróblewski on 17.10.2016.
 */

public class IntrestsViewGenerator {

    public static View getView(Context context, String text) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.intrest, null);

        ((TextView)view.findViewById(R.id.text)).setText(text);
        return view;
    }

}
