package com.peterblackburn.shiftmanager.Events.Adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peterblackburn.shiftmanager.Events.Interfaces.EventInterface;
import com.peterblackburn.shiftmanager.Events.ViewHolders.BaseViewHolder;

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Context _context;

    public BaseAdapter(Context context) {
        _context = context;
    }

    public Context getContext() { return _context; }


    @NonNull
    @Override
    public abstract BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(@NonNull BaseViewHolder holder, int position);

    @Override
    public abstract int getItemCount();
}
