package com.uab.es.cat.foodnetwork.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uab.es.cat.foodnetwork.ActionsFragment;
import com.uab.es.cat.foodnetwork.EditProfileFragment;
import com.uab.es.cat.foodnetwork.R;
import com.uab.es.cat.foodnetwork.ViewProfileFragment;
import com.uab.es.cat.foodnetwork.model.NavDrawerItem;

import java.util.Collections;
import java.util.List;

/**
 * Created by ramonmacias on 19/12/15.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        switch (position) {
            case 0:
                holder.image.setImageResource(R.drawable.ic_insert_emoticon_black_24dp);
                break;
            case 1:
                holder.image.setImageResource(R.drawable.ic_mode_edit_black_24dp);
                break;
            case 2:
                holder.image.setImageResource(R.drawable.ic_apps_black_24dp);
                break;
            case 3:
                holder.image.setImageResource(R.drawable.ic_info_outline_black_24dp);
                break;
            case 4:
                holder.image.setImageResource(R.drawable.ic_help_outline_black_24dp);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.icon_drawer);
        }
    }
}
