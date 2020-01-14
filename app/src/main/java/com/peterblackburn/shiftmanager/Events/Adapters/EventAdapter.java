package com.peterblackburn.shiftmanager.Events.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.peterblackburn.shiftmanager.Events.Interfaces.EventInterface;
import com.peterblackburn.shiftmanager.R;
import com.peterblackburn.shiftmanager.Realm.Objects.Shift;
import com.peterblackburn.shiftmanager.ShiftApplication;


import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;

import io.realm.RealmResults;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ShiftViewHolder> {

    private ArrayList<Shift> shifts = new ArrayList<>();
    private Context context;
    private EventInterface _interface;

    public EventAdapter(Context context, EventInterface _interface) {
        this.context = context;
        this._interface = _interface;
    }

    public void updateShifts(RealmResults<Shift> shiftResults) {
        this.shifts.clear();
        this.shifts.addAll(shiftResults);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.calendar_event_item, parent, false);
        ShiftViewHolder svh = new ShiftViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftViewHolder holder, int position) {
        holder.bind(shifts.get(position), _interface);
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    static class ShiftViewHolder extends RecyclerView.ViewHolder {

        TextView shiftDateText;
        TextView eventTimeTxt;
        TextView eventType;
        ImageView removeBtn;
        ImageView editBtn;

        ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            shiftDateText = itemView.findViewById(R.id.eventDateText);
            removeBtn = itemView.findViewById(R.id.removeEventBtn);
            editBtn = itemView.findViewById(R.id.editEventBtn);
            eventTimeTxt = itemView.findViewById(R.id.eventTimeTxt);
            eventType = itemView.findViewById(R.id.eventType);
        }

        void bind(final Shift shift, final EventInterface eventInterface) {
            Resources res = ShiftApplication.getRes();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM");
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
            String title = shift.getStartTime().format(formatter);
            String startTime = shift.getStartTime().format(formatterTime);
            String endTime = shift.getEndTime().format(formatterTime);
            shiftDateText.setText(title);
            eventTimeTxt.setText(res.getString(R.string.event_time, startTime, endTime));
            eventType.setText(res.getString(R.string.event_type, "Contracted Shift", "Tesco"));

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(eventInterface != null)
                        eventInterface.removeShift(shift);
                }
            });
        }
    }
}
