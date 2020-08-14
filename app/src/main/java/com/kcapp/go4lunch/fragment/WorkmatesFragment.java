package com.kcapp.go4lunch.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.adapter.ListWorkmatesAdapter;
import com.kcapp.go4lunch.api.helper.UserHelper;
import com.kcapp.go4lunch.model.User;

import java.util.List;

public class WorkmatesFragment extends Fragment {
    RecyclerView mListWorkmates;
    ListWorkmatesAdapter mListWorkmatesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workmates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListWorkmatesAdapter = new ListWorkmatesAdapter(UserHelper.generateOptionsForAdapter(UserHelper.getAllUsers(),this));

        mListWorkmates = view.findViewById(R.id.list_workmates);
        mListWorkmates.setLayoutManager(new LinearLayoutManager(getContext()));
        mListWorkmates.setAdapter(mListWorkmatesAdapter);
    }
}