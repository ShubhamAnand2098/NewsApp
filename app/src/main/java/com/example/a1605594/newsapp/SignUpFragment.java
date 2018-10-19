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

import com.github.florent37.materialtextfield.MaterialTextField;

public class SignUpFragment extends Fragment {

    private MaterialTextField nameTextView, emailTextView, passwordTextView;
    private Button submitButton;
    private String name, emailId, password;
    private ProgressDialog progressDialog;
    private DatabaseHelper databaseHelper;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        nameTextView = view.findViewById(R.id.name);
        emailTextView = view.findViewById(R.id.email);
        passwordTextView = view.findViewById(R.id.password);
        submitButton = view.findViewById(R.id.submit_button);
        progressDialog = new ProgressDialog(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());

        progressDialog.setMessage("Validating...");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                name = nameTextView.getEditText().getText().toString();
                emailId = emailTextView.getEditText().getText().toString();
                password = passwordTextView.getEditText().getText().toString();

                final boolean isSuccessful = databaseHelper.insert_data(name, emailId, password);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(progressDialog.isShowing())
                            progressDialog.dismiss();

                        if(name.isEmpty()||emailId.isEmpty()||password.isEmpty())
                            Snackbar.make(getView(), "ENTER FULL DETAILS", 1000).show();
                        else {
                            if (isSuccessful) {
                                Intent intent = new Intent(getActivity(), NewsFeedActivity.class);
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("USER INFO", Context.MODE_PRIVATE).edit();
                                editor.putBoolean("LoggedIn", true);
                                editor.putString("name", name);
                                editor.putString("email", emailId);
                                editor.putString("password", password);
                                editor.apply();
                                startActivity(intent);
                                getActivity().finish();
                            } else
                                Snackbar.make(getView(), "Error: The email id is already used", 1000).show();

                        }

                    }
                },1500);


            }
        });



        return view;
    }


}
