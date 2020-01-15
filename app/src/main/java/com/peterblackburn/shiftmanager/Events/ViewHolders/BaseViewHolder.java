package com.peterblackburn.shiftmanager.Events.ViewHolders;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peterblackburn.shiftmanager.Events.Interfaces.BaseInterface;

import io.realm.RealmObject;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bind(RealmObject base, BaseInterface eventInterface);
}