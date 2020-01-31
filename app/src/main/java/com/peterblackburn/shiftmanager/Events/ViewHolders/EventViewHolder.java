package com.peterblackburn.shiftmanager.Events.ViewHolders;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.peterblackburn.shiftmanager.Events.Interfaces.BaseInterface;
import com.peterblackburn.shiftmanager.Events.Interfaces.EventInterface;
import com.peterblackburn.shiftmanager.Events.Models.Event;
import com.peterblackburn.shiftmanager.R;
import com.peterblackburn.shiftmanager.ShiftApplication;

import org.threeten.bp.format.DateTimeFormatter;

import io.realm.RealmObject;

public class EventViewHolder extends BaseViewHolder {

//    TextView eventDateText;
//    TextView eventTimeTxt;
//    TextView eventType;
    TextView eventInfo;
    ImageView removeBtn;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
//        eventDateText = itemView.findViewById(R.id.eventDateText);
        removeBtn = itemView.findViewById(R.id.removeEventBtn);
        eventInfo = itemView.findViewById(R.id.eventInfo);
//        eventTimeTxt = itemView.findViewById(R.id.eventTimeTxt);
//        eventType = itemView.findViewById(R.id.eventType);
    }

    @Override
    public void bind(RealmObject object, BaseInterface baseInterface) {
        final Event event = (Event)object;
        final EventInterface eventInterface = (EventInterface)baseInterface;

        Resources res = ShiftApplication.getInstance().getRes();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        String title = event.getStartTime().format(formatter);
        String startTime = event.getStartTime().format(formatterTime);
        String endTime = event.getEndTime().format(formatterTime);

        eventInfo.setText(res.getString(R.string.event_item_title, event.getEventType().getReadableName(), startTime, endTime)) ;
//        eventDateText.setText(title);
//        eventTimeTxt.setText(res.getString(R.string.event_time, startTime, endTime));
//        eventType.setText(res.getString(R.string.event_type, event.getEventType().getReadableName(), "Tesco"));

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventInterface != null)
                    eventInterface.removeEvent(event);
            }
        });
    }
}