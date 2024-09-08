package com.example.babybuy.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.babybuy.Database.Database;
import com.example.babybuy.R;


public class frag_of_user extends Fragment {


    public frag_of_user() {
        // Required empty public constructor
    }


    TextView userfragFullname, userfragEmail,userfragFullnameforlogo;
    String emailid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user, container, false);
        Database db = new Database(getActivity());
        userfragFullname = view.findViewById(R.id.userfragfullname);
        userfragEmail = view.findViewById(R.id.userfragEmail);
        userfragFullnameforlogo = view.findViewById(R.id.tv_name);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("AUTH", 0);
        String value = sharedPreferences.getString("email", "null");

        if(value != null){
            String fullname = db.getfullname(value);
            userfragEmail.setText(value);
            userfragFullname.setText(fullname);
            userfragFullnameforlogo.setText(fullname);
        }
        return view;
    }
}