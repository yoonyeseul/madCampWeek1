package com.example.madcampweek1.ui.contact;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek1.R;
import com.example.madcampweek1.databinding.FragmentContactBinding;
import com.example.madcampweek1.ui.contact.ContactItem;
import com.example.madcampweek1.ui.contact.MyRecyclerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ContactFragment extends Fragment {

    private ContactViewModel contactViewModel;
    private FragmentContactBinding binding;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mRecyclerAdapter;
    private ArrayList<ContactItem> mcontactItems = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        contactViewModel =
                new ViewModelProvider(this).get(ContactViewModel.class);

        binding = FragmentContactBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        contactViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


//        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
//
//        list = ContactItem.createContactList(5);
//        recyclerView.setHasFixedSize(true);
//        adapter = new MyRecyclerAdapter();
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(adapter);


//        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
//
//        /* initiate adapter */
//        mRecyclerAdapter = new MyRecyclerAdapter();
//
//        /* initiate recyclerview */
//        mRecyclerView.setAdapter(mRecyclerAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
//
//        /* adapt data */
//        mcontactItems = ContactItem.createContactList(5);
//        mRecyclerAdapter.setContactList(mcontactItems);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}