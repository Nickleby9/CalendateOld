package com.calendate.calendate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hilay on 28-מאי-2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ItemViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Item> data;

    public Adapter(Context context, List<Item> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = data.get(position);
        holder.tvTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Item item = data.get(position);

              Intent intent = new Intent(context, DetailedItem.class);
              intent.putExtra("title", item.getTitle());
              context.startActivity(intent);
                }
            });
        }
    }
}
