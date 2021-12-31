package com.example.madcampweek1.ui.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;


import androidx.recyclerview.widget.RecyclerView;


import com.example.madcampweek1.R;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.Holder> implements Filterable{

    private Context context;
    private ArrayList<ContactItem> list;
    private ArrayList<ContactItem> filteredList;
    public MyRecyclerAdapter(Context context, ArrayList<ContactItem> list) {
        super();
        this.context = context;
        this.list = list;
        this.filteredList = list;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    filteredList = list;
                } else {
                    ArrayList<ContactItem> filteringList = new ArrayList<>();
                    for(ContactItem contact : list) {
                        if(contact.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(contact);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<ContactItem>)results.values;
                notifyDataSetChanged();
            }
        };
    }
    // ViewHolder 생성
    // row layout을 화면에 뿌려주고 holder에 연결
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    /*
     * Todo 만들어진 ViewHolder에 data 삽입 ListView의 getView와 동일
     *
     * */
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // 각 위치에 문자열 세팅
        holder.name.setText(filteredList.get(position).name);
//        holder.number.setText(list.get(itemposition).number);
        //Log.e("StudyApp", "onBindViewHolder" + itemposition);
    }

    // 몇개의 데이터를 리스트로 뿌려줘야하는지 반드시 정의해줘야한다
    @Override
    public int getItemCount() {
        return filteredList.size(); // RecyclerView의 size return
    }

    // ViewHolder는 하나의 View를 보존하는 역할을 한다
    public class Holder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView number;


        public Holder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.name);
//            number = (TextView) view.findViewById(R.id.number);

            view.setClickable(true);

            view.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        ContactItem item = list.get(pos);
                        Intent intent = new Intent(context, ContactDetail.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                        intent.putExtra("contact", item);
                        intent.putExtra("contact_name", item.name);
                        intent.putExtra("contact_number", item.number);
                        intent.putExtra("contact_id", item.id);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
                    }
                }
            });
        }
    }

}
