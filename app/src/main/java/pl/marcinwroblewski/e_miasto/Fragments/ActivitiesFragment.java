package pl.marcinwroblewski.e_miasto.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.marcinwroblewski.e_miasto.ErrorCardAdapter;
import pl.marcinwroblewski.e_miasto.Events.Event;
import pl.marcinwroblewski.e_miasto.Events.EventsAdapter;
import pl.marcinwroblewski.e_miasto.JSONTo;
import pl.marcinwroblewski.e_miasto.R;
import pl.marcinwroblewski.e_miasto.Requests;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActivitiesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActivitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivitiesFragment extends Fragment {


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    List<Event> eventList;
    View mainView;

    private OnFragmentInteractionListener mListener;

    public ActivitiesFragment() {
        // Required empty public constructor
    }


    public static ActivitiesFragment newInstance() {
        ActivitiesFragment fragment = new ActivitiesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_activities, container, false);
        //LinearLayout mainConatiner = (LinearLayout) view.findViewById(R.id.event_cards_container);

        eventList = new ArrayList<>();

        preferences = getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
        editor = preferences.edit();

        DownloadPartiesAsyncTask partiesAsyncTask = new DownloadPartiesAsyncTask();
        partiesAsyncTask.execute();

        return mainView;
    }

    public void showEvents() {
        hideLoadingIndicator();

        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        EventsAdapter adapter = new EventsAdapter(getContext(), eventList);
        recyclerView.setAdapter(adapter);
    }

    public void showErrorCard(String error) {
        hideLoadingIndicator();

        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ErrorCardAdapter adapter = new ErrorCardAdapter(error);
        recyclerView.setAdapter(adapter);
    }

    private void hideLoadingIndicator() {
        mainView.findViewById(R.id.loading_indicator).setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class DownloadPartiesAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String login = preferences.getString("login", "");
            String password = preferences.getString("password", "");
            Requests requests = new Requests(login, password);

            String allPersonalizedPartiesResponse = "-1";
            try {
                allPersonalizedPartiesResponse = requests.getPersonalizedParties();
                Log.d("Personal party", allPersonalizedPartiesResponse);
                JSONArray eventsJSON = new JSONArray(allPersonalizedPartiesResponse);
                eventList = JSONTo.events(getContext(), eventsJSON);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showEvents();
                    }
                });
            } catch (JSONException | IOException e) {
                final String finalAllPersonalizedPartiesResponse = allPersonalizedPartiesResponse;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorCard(finalAllPersonalizedPartiesResponse);
                    }
                });
                e.printStackTrace();
            }

            return null;
        }
    }
}
