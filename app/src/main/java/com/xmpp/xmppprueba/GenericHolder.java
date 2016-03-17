package com.xmpp.xmppprueba;


import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by david on 4/03/16.
 */
public class GenericHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private RecyclerViewLItemClickistener listener;

    public GenericHolder(View itemView, RecyclerViewLItemClickistener listener) {
        super(itemView);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.OnItemClicked(getAdapterPosition());
        }
    }
}