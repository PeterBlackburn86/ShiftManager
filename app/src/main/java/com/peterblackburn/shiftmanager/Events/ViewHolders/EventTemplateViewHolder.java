package com.peterblackburn.shiftmanager.Events.ViewHolders;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.peterblackburn.shiftmanager.Events.Interfaces.BaseInterface;
import com.peterblackburn.shiftmanager.Events.Interfaces.TemplateInterface;
import com.peterblackburn.shiftmanager.Events.Models.EventTemplate;
import com.peterblackburn.shiftmanager.R;
import com.peterblackburn.shiftmanager.ShiftApplication;

import org.threeten.bp.format.DateTimeFormatter;

import io.realm.RealmObject;

public class EventTemplateViewHolder extends BaseViewHolder {

    private TextView _templateName;
    private TextView _templateEventType;
    private TextView _templateEventTime;
    private ImageView _templateRemove;

    public EventTemplateViewHolder(@NonNull View itemView) {
        super(itemView);

        _templateName = itemView.findViewById(R.id.templateName);
        _templateEventType = itemView.findViewById(R.id.templateEventType);
        _templateEventTime = itemView.findViewById(R.id.templateEventTimeTxt);
        _templateRemove = itemView.findViewById(R.id.removeTemplateBtn);
    }

    @Override
    public void bind(RealmObject object, BaseInterface baseInterface) {
        final EventTemplate template = (EventTemplate)object;
        final TemplateInterface templateInterface = (TemplateInterface) baseInterface;

        Resources res = ShiftApplication.getInstance().getRes();
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
        String startTime = template.getStartTime().format(formatterTime);
        String endTime = template.getEndTime().format(formatterTime);
        _templateName.setText(template.getTemplateName());
        _templateEventTime.setText(res.getString(R.string.event_time, startTime, endTime));
        _templateEventType.setText(template.getEventType().getReadableName());

        _templateRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(templateInterface != null)
                    templateInterface.removeTemplate(template);
            }
        });
    }
}
