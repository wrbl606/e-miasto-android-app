package pl.marcinwroblewski.e_miasto;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Admin on 16.10.2016.
 */

public class ErrorCardAdapter extends RecyclerView.Adapter<ErrorCardAdapter.MyViewHolder> {

    private String error;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View view) {
            super(view);

        }
    }


    public ErrorCardAdapter(String error) {
        this.error = error;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.error_card, parent, false);

        TextView errorTV = (TextView) itemView.findViewById(R.id.error);
        errorTV.setText(error);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
