package com.peterblackburn.shiftmanager.Events.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.peterblackburn.shiftmanager.Events.Interfaces.EventInterface;
import com.peterblackburn.shiftmanager.Events.ViewHolders.BaseViewHolder;
import com.peterblackburn.shiftmanager.Events.ViewHolders.EventViewHolder;
import com.peterblackburn.shiftmanager.R;
import com.peterblackburn.shiftmanager.Events.Models.Event;
import java.util.ArrayList;
import io.realm.RealmResults;

public class EventAdapter extends BaseAdapter {

    private ArrayList<Event> events = new ArrayList<>();
    private EventInterface _interface;

    public EventAdapter(Context context, EventInterface iFace) {
        super(context);
        _interface = iFace;

    }

    public void updateEvents(RealmResults<Event> eventResults) {
        this.events.clear();
        this.events.addAll(eventResults);
        this.notifyDataSetChanged();
    }

    public ArrayList<Event> getEvents() { return events; }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.calendar_event_item, parent, false);
        EventViewHolder svh = new EventViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(events.get(position), _interface);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public EventInterface getInterface() { return _interface; }


}
