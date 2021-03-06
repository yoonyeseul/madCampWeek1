package com.example.madcampweek1.ui.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.AbstractCursor;
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
    // ViewHolder ??????
    // row layout??? ????????? ???????????? holder??? ??????
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    /*
     * Todo ???????????? ViewHolder??? data ?????? ListView??? getView??? ??????
     *
     * */
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // ??? ????????? ????????? ??????
        holder.name.setText(filteredList.get(position).name);
//        holder.number.setText(list.get(itemposition).number);
        //Log.e("StudyApp", "onBindViewHolder" + itemposition);
    }

    // ????????? ???????????? ???????????? ????????????????????? ????????? ?????????????????????
    @Override
    public int getItemCount() {
        return filteredList.size(); // RecyclerView??? size return
    }

    // ViewHolder??? ????????? View??? ???????????? ????????? ??????
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
                        ContactItem item = filteredList.get(pos);
                        Intent intent = new Intent(context, ContactDetail.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                        intent.putExtra("contact", item);
                        intent.putExtra("contact_name", item.name);
                        intent.putExtra("contact_number", item.number);
                        intent.putExtra("contact_email", item.email);
                        intent.putExtra("contact_web", item.web);
                        intent.putExtra("contact_job", item.job);
                        intent.putExtra("contact_sns", item.sns);
                        intent.putExtra("contact_address", item.address);
                        intent.putExtra("contact_id", item.id);
                        context.startActivity(intent);
//                        ((Activity) context).finish();
                        ((Activity) context).overridePendingTransition(R.anim.slide_left2, R.anim.slide_left);
                    }
                }
            });
        }
    }

}
