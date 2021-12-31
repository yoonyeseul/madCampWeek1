package com.example.madcampweek1.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek1.R;
import com.example.madcampweek1.databinding.FragmentContactBinding;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ContactFragment extends Fragment {

    private ContactViewModel contactViewModel;
    private FragmentContactBinding binding;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mRecyclerAdapter;
    private ArrayList<ContactItem> mcontactItems = new ArrayList<>();
    EditText editText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contact, container, false);
        editText = (EditText)rootView.findViewById(R.id.filter_text);
//        editText.addTextChangedListener(this);
//        String json = getJsonString(getActivity().getApplicationContext());
        String json = getJsonString();
        System.out.println(json);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mcontactItems = ContactItem.createContactList(json);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerAdapter = new MyRecyclerAdapter(getActivity(), this.mcontactItems);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mRecyclerAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRecyclerAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Button addButton = rootView.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactAdd.class);
                startActivity(intent);
            }

        });


        return rootView;

    }


    private String getJsonString() {

        String fileTitle = "contact.json";
        File file = new File(getActivity().getFilesDir(), fileTitle);

        String result = "";
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;

            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();

            return result;

        } catch (FileNotFoundException e1) {
            Log.i("파일못찾음",e1.getMessage());
        } catch (IOException e2) {
            Log.i("읽기오류",e2.getMessage());
        }
        return result;
//        String json = null;
//        try {
//            AssetManager am = context.getAssets();
//            InputStream is = am.open("jsons/contact.json");
//            int fileSize = is.available();
//
//            byte[] buffer = new byte[fileSize];
//            is.read(buffer);
//            is.close();
//
//            json = new String(buffer, "UTF-8");
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//            return null;
//        }
//
//        return json;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
}