package pl.marcinwroblewski.e_miasto.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tangxiaolv.telegramgallery.GalleryActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pl.marcinwroblewski.e_miasto.Bitmaps.BitmapsStorage;
import pl.marcinwroblewski.e_miasto.Complains.Complain;
import pl.marcinwroblewski.e_miasto.Complains.ComplainAdapter;
import pl.marcinwroblewski.e_miasto.ErrorCardAdapter;
import pl.marcinwroblewski.e_miasto.Fragments.Dialog.ErrorDialogFragment;
import pl.marcinwroblewski.e_miasto.Fragments.Dialog.ProgressDialogFragment;
import pl.marcinwroblewski.e_miasto.Fragments.Dialog.SuccessDialogFragment;
import pl.marcinwroblewski.e_miasto.JSONTo;
import pl.marcinwroblewski.e_miasto.R;
import pl.marcinwroblewski.e_miasto.Requests;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendComplainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SendComplainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendComplainFragment extends Fragment implements LocationListener {

    private OnFragmentInteractionListener mListener;
    private ImageView issuePhoto;
    private LocationManager locationManager;
    private Location userLocation;
    private File issuePhotoFile;
    private boolean useLocation;
    private View mainView;
    private ArrayList<Complain> complainsArrayList;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SendComplainFragment() {
        // Required empty public constructor
    }

    public static SendComplainFragment newInstance() {
        SendComplainFragment fragment = new SendComplainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_send_complain, container, false);
        issuePhoto = (ImageView) mainView.findViewById(R.id.issue_photo);
        AppCompatButton addPhotoButton = (AppCompatButton) mainView.findViewById(R.id.add_photo_button);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryActivity.openActivity(getActivity(), true, 1337);
            }
        });

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        tryToRegisterLocationListener();

        AppCompatButton sendIssueButton = (AppCompatButton) mainView.findViewById(R.id.send_issue);
        sendIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String issueTitle, issueDescription;

                if(issuePhotoFile == null) {
                    showErrorDialog("Dodaj zdjęcie");
                    return;
                }

                if(userLocation == null) {
                    showErrorDialog("Twoja lokalizacja nie została jeszcze określona. Proszę spróbować ponownie za chwilę.");
                    return;
                }

                issueTitle = ((TextView)(mainView.findViewById(R.id.issue_title))).getText().toString();
                issueDescription = ((TextView)(mainView.findViewById(R.id.issue_content))).getText().toString();

                AddComplainAsyncTask addComplainAsyncTask = new AddComplainAsyncTask();
                addComplainAsyncTask.execute(issueTitle, issueDescription);
            }
        });

        preferences = getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
        editor = preferences.edit();

        DownloadComplainsAsyncTask complainsAsyncTask = new DownloadComplainsAsyncTask();
        complainsAsyncTask.execute();

        return mainView;
    }

    void tryToRegisterLocationListener() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1337);
        } else {
            try {
                if(locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    useLocation = true;
                } else if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    useLocation = true;
                } else {
                    locationManager = null;
                    useLocation = false;
                }
            } catch (NullPointerException e) {
                Log.e("Location Manager", "was null");
                e.printStackTrace();
            }
        }
    }

    void showComplains() {
        hideLoadingIndicator();

        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.complains_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ComplainAdapter adapter = new ComplainAdapter(getContext(), complainsArrayList);
        recyclerView.setAdapter(adapter);
    }

    public void showErrorCard(String error) {
        hideLoadingIndicator();

        RecyclerView recyclerView = (RecyclerView) mainView.findViewById(R.id.complains_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ErrorCardAdapter adapter = new ErrorCardAdapter(error);
        recyclerView.setAdapter(adapter);
    }

    void showErrorDialog(String text) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = ErrorDialogFragment.newInstance(text);
        newFragment.show(ft, "Error");
    }

    void showSuccessDialog(String text) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = SuccessDialogFragment.newInstance(text);
        newFragment.show(ft, "Error");
    }

    DialogFragment showProgressDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment newFragment = ProgressDialogFragment.newInstance();
        newFragment.show(ft, "Error");
        return newFragment;
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

    @SuppressWarnings("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();

        tryToRegisterLocationListener();

        Log.d("onResume", "called");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE);
        BitmapsStorage bitmapsStorage = new BitmapsStorage(getContext());
        if (sharedPreferences.contains("image")) {
            issuePhotoFile = bitmapsStorage.loadImageFromStorage(sharedPreferences.getString("image", ""),
                    "image" + (sharedPreferences.getInt("issueCounter", 0) - 1));
            Glide.with(getContext()).load(issuePhotoFile).into(issuePhoto);
            Log.d("Load image", "image" + (sharedPreferences.getInt("issueCounter", 0) - 1));

            Log.d("Issue photo file", issuePhotoFile.getAbsolutePath());

//            ServerConnection serverConnection = new ServerConnection();
//            serverConnection.sendBitmap(issuePhotoBitmap);
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onPause() {

        if(useLocation)
            //noinspection MissingPermission
            locationManager.removeUpdates(this);
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude(), lon = location.getLongitude();
        userLocation = location;
        Log.d("Location", "lat: " + lat + ", lon: " + lon);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Location", s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Location provider en", s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Location provider dis", s);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1337: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permissions", "GPS permissions granted");
                    //noinspection MissingPermission
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    class DownloadComplainsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String login = preferences.getString("login", "");
            String password = preferences.getString("password", "");
            Requests requests = new Requests(login, password);

            String allComplainsResponse = "-1";
            try {
                allComplainsResponse = requests.getAllComplains();
                Log.d("All complains", allComplainsResponse);
                JSONArray complainsJSON = new JSONArray(allComplainsResponse);
                complainsArrayList = JSONTo.complains(getContext(), complainsJSON);
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showComplains();
                        }
                    });
                } catch (NullPointerException e) {
                    Log.e("AsyncTask", "getActivity() returned null");
                    e.printStackTrace();
                    return null;
                }
            } catch (JSONException | IOException e) {
                final String finalAllComplainsResponse = allComplainsResponse;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorCard(finalAllComplainsResponse);
                    }
                });
                e.printStackTrace();
            }
            return null;
        }
    }

    class AddComplainAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String issueTitle = strings[0];
            String issueDescription = strings[1];

            String login = preferences.getString("login", "");
            String password = preferences.getString("password", "");
            Requests requests = new Requests(login, password);

            DialogFragment progressFragment = showProgressDialog();

            String addComplainResponse = "-1";
            try {
                addComplainResponse = requests.addComplain(issueTitle, issueDescription, (float) userLocation.getLongitude(), (float) userLocation.getLatitude(), issuePhotoFile);
                Log.d("Add complain", addComplainResponse);
                progressFragment.dismiss();
                showSuccessDialog("Zgłoszenie odebrane pomyślnie!");
            } catch (final IOException e) {
                final String finalAddComplainResponse = addComplainResponse;
                progressFragment.dismiss();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showErrorDialog("Nie udało się wysłać zgłoszenia. Proszę spróbować ponownie później (" + e.getMessage() + ")");
                    }
                });
                e.printStackTrace();
            }

            return null;
        }
    }
}
