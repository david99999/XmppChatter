package com.xmpp.xmppprueba;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xmpp.xmppprueba.models.User;

import java.util.ArrayList;

/**
 * Created by david on 4/03/16.
 */
public class ContactsAdapter extends RecyclerView.Adapter<GenericHolder> {

    private final ArrayList<User> items;
    private RecyclerViewLItemClickistener listener;

    public ContactsAdapter(ArrayList<User> items, RecyclerViewLItemClickistener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public GenericHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ContactItemView holder = (ContactItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new GenericHolder(holder, listener);
    }

    @Override
    public void onBindViewHolder(GenericHolder holder, int position) {
        ((ContactItemView) holder.itemView).setUser(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
