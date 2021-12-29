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
//        contactViewModel =
//                new ViewModelProvider(this).get(ContactViewModel.class);
////
//        binding = FragmentContactBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.contact_item, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
//
//        mcontactItems = ContactItem.createContactList(5);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerAdapter = new MyRecyclerAdapter(getActivity(), mcontactItems);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.setAdapter(mRecyclerAdapter);


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

        return rootView;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
}