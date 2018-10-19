package com.example.a1605594.newsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.materialtextfield.MaterialTextField;

public class LoginFragment extends Fragment {

    private MaterialTextField emailEditText, passwordEditText;
    private Button loginButton;
    private TextView regTextView;
    private String emailId, password;
    private ProgressDialog progressDialog;

    private onRegisterClicked onRegisterClickedlistener;

    private DatabaseHelper databaseHelper;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.login_button);
        regTextView = view.findViewById(R.id.register_text_view);
        progressDialog = new ProgressDialog(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        progressDialog.setMessage("Verifying...");

        regTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterClickedlistener.slideFragment();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                emailId = emailEditText.getEditText().getText().toString();
                password = passwordEditText.getEditText().getText().toString();

                final boolean verifiedUser = databaseHelper.if_exists(emailId, password);
                passwordEditText.getEditText().setText("");
                final Intent intent = new Intent(getActivity(), NewsFeedActivity.class);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                        if(verifiedUser) {
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("USER INFO", Context.MODE_PRIVATE).edit();
                            editor.putBoolean("LoggedIn", true);
                            editor.putString("email", emailId);
                            editor.putString("password", password);
                            editor.apply();
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else
                            Snackbar.make(getView(), "NO ENTRY FOUND.REGISTER TO GET STARTED", 1000).show();

                    }
                },1500);

            }
        });



        return view;
    }

    @Override
    public void onAttach(Context context) {
        onRegisterClickedlistener = (onRegisterClicked) context;
        super.onAttach(context);
    }
}
