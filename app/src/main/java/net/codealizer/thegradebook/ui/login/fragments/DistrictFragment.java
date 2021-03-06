package net.codealizer.thegradebook.ui.login.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import net.codealizer.thegradebook.R;
import net.codealizer.thegradebook.apis.ic.RequestTask;
import net.codealizer.thegradebook.data.SessionManager;
import net.codealizer.thegradebook.listeners.onAuthenticationListener;
import net.codealizer.thegradebook.ui.dialogs.Alert;
import net.codealizer.thegradebook.ui.login.LoginActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class DistrictFragment extends Fragment implements View.OnClickListener, onAuthenticationListener {

    private EditText districtCodeEditText;

    public DistrictFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_login_district, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();

    }

    @Override
    public void onClick(View view) {
        attemptLogin();
    }

    @Override
    public void onAuthenticated() {
        CredentialsFragment fragment = new CredentialsFragment();

        LoginActivity
                .mLoginFragmentManager
                .beginTransaction()
                .replace(R.id.login_container, fragment)
                .addToBackStack(getClass().getName())
                .commit();
    }

    @Override
    public void onUnauthorized() {
        districtCodeEditText.setError(getString(R.string.login_prompt_district_id_error));
    }

    @Override
    public void onNetworkError() {
        Alert.showNetworkErrorDialog(getActivity());
    }

    private void attemptLogin() {
        String districtCode = districtCodeEditText.getText().toString();

        if (!districtCode.isEmpty() && districtCode.length() > 2) {
            RequestTask background = new RequestTask(getActivity(), RequestTask.OPTION_SET_DISTRICT,
                    SessionManager.mCoreManager, this, districtCode);
            background.execute();
        } else {
            districtCodeEditText.setError(getString(R.string.error_field_required));
        }


    }

    private void initialize() {
        districtCodeEditText = (EditText) getView().findViewById(R.id.district_id);
        Button goButton = (Button) getView().findViewById(R.id.accept_district_id_button);
        Button districtButton = (Button) getView().findViewById(R.id.district_id_problem);

        LoginActivity.mActionBar.setDisplayHomeAsUpEnabled(false);
        LoginActivity.mActionBar.setDisplayShowHomeEnabled(false);
        LoginActivity.mActionBar.setTitle("Sign in");

        goButton.setOnClickListener(this);
        districtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                Alert.showDistrictStateDialog(getActivity());
            }
        });
    }
}
