package com.kcapp.go4lunch.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kcapp.go4lunch.PlaceActivity;
import com.kcapp.go4lunch.R;
import com.kcapp.go4lunch.adapter.ListWorkmatesAdapter;
import com.kcapp.go4lunch.api.helper.UserHelper;
import com.kcapp.go4lunch.api.services.Constants;
import com.kcapp.go4lunch.api.services.InternetManager;
import com.kcapp.go4lunch.api.services.InternetManagerImpl;

import org.jetbrains.annotations.NotNull;

public class WorkmatesFragment extends Fragment implements ListWorkmatesAdapter.ListWorkmatesListener {
    RecyclerView mListWorkmates;
    ListWorkmatesAdapter mListWorkmatesAdapter;
    ListWorkmatesAdapter.ListWorkmatesListener mListWorkmatesListener;

    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workmates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InternetManager internetManager = new InternetManagerImpl(mContext);
        if (!internetManager.isConnected()) {
            TextView information = view.findViewById(R.id.fragment_workmates_information);
            information.setText(R.string.error_no_internet);
            information.setVisibility(View.VISIBLE);
            return;
        }

        mListWorkmates = view.findViewById(R.id.list_workmates);
        // Show the list of workmates
        showWorkmates();
    }

    @Override
    public void onClick(String placeId) {
        Intent intent = new Intent(mContext, PlaceActivity.class);
        intent.putExtra(Constants.PLACE_ID, placeId);
        startActivityForResult(intent, Constants.CODE_REQUEST_WORKMATES_FRAGMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CODE_REQUEST_WORKMATES_FRAGMENT) {
            showWorkmates();
        }
    }

    /**
     * Show the list of workmates
     */
    public void showWorkmates() {
        mListWorkmatesAdapter = new ListWorkmatesAdapter(UserHelper.generateOptionsForAdapter(UserHelper.getAllUsers(),this), Constants.WORKMATES_FRAMGMENT, WorkmatesFragment.this);

        mListWorkmates.setLayoutManager(new LinearLayoutManager(mContext));
        mListWorkmates.setAdapter(mListWorkmatesAdapter);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}