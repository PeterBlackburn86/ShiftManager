package com.peterblackburn.shiftmanager.Events.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.peterblackburn.shiftmanager.Events.Interfaces.EventInterface;
import com.peterblackburn.shiftmanager.Events.Interfaces.TemplateInterface;
import com.peterblackburn.shiftmanager.Events.Models.EventTemplate;
import com.peterblackburn.shiftmanager.Events.ViewHolders.BaseViewHolder;
import com.peterblackburn.shiftmanager.Events.ViewHolders.EventTemplateViewHolder;
import com.peterblackburn.shiftmanager.R;

import java.util.ArrayList;

import io.realm.RealmResults;

public class EventTemplateAdapter extends BaseAdapter {

    private ArrayList<EventTemplate> templates = new ArrayList<>();
    private TemplateInterface _interface;

    public EventTemplateAdapter(Context context, TemplateInterface iFace) {
        super(context);
        _interface = iFace;
    }

    public void updateTemplates(RealmResults<EventTemplate> eventResults) {
        this.templates.clear();
        this.templates.addAll(eventResults);
        this.notifyDataSetChanged();
    }

    public ArrayList<EventTemplate> getTemplates() { return templates; }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.template_event_item, parent, false);
        EventTemplateViewHolder svh = new EventTemplateViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(templates.get(position), _interface);
    }


    @Override
    public int getItemCount() {
        return templates.size();
    }

    public TemplateInterface getInterface() { return _interface; }
}
