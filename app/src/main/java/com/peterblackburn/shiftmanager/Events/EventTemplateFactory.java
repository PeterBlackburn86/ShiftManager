package com.peterblackburn.shiftmanager.Events;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.peterblackburn.shiftmanager.Events.Adapters.EventTemplateAdapter;
import com.peterblackburn.shiftmanager.Events.Interfaces.EventTemplateFactoryInterface;
import com.peterblackburn.shiftmanager.Events.Interfaces.TemplateInterface;
import com.peterblackburn.shiftmanager.Events.Models.EventTemplate;
import com.peterblackburn.shiftmanager.RealmHelper;
import java.util.ArrayList;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class EventTemplateFactory implements TemplateInterface {

    private static EventTemplateFactory _instance;

    private EventTemplateAdapter _adapter;
    private Realm _realm;
    private Context _context;
    private RecyclerView _recyclerView;
    private ArrayList<EventTemplateFactoryInterface> _interfaces = new ArrayList<>();

    public static EventTemplateFactory getInstance() {
        if(_instance == null)
            _instance = new EventTemplateFactory();
        return _instance;
    }

    public void initEventFactory(Context context, RecyclerView recyclerView, EventTemplateFactoryInterface eventFactoryInterface) {
        _realm = Realm.getDefaultInstance();
        _context = context;
        addFactoryInterface(eventFactoryInterface);
        _adapter = new EventTemplateAdapter(context, this);
        _recyclerView = recyclerView;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(_context);
        _recyclerView.setLayoutManager(layoutManager);
        _recyclerView.setAdapter(_adapter);//
        // _recyclerView.addItemDecoration(new DividerItemDecoration(_context, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void removeTemplate(EventTemplate template) {
        RealmHelper.getInstance().deleteTemplate(template);
        updateTemplates();
        notifyTemplateRemoved();
    }
    private void notifyTemplateRemoved() {
        for(EventTemplateFactoryInterface efInterface : _interfaces) {
            efInterface.onTemplateRemoved();
        }
    }

    public void addFactoryInterface(EventTemplateFactoryInterface factoryInterface) {
        if(!_interfaces.contains(factoryInterface)) {
            _interfaces.add(factoryInterface);
        }
    }

    public void removeFactoryInterface(EventTemplateFactoryInterface factoryInterface) {
        if(_interfaces.contains(factoryInterface)) {
            _interfaces.remove(factoryInterface);
        }
    }

    public EventTemplateAdapter getAdapter() { return _adapter; }
    public void setAdapter(EventTemplateAdapter value) {
        _adapter = value;
        updateTemplates();
    }

    public void updateTemplates() {
        final RealmResults<EventTemplate> templateResults = _realm.where(EventTemplate.class).findAll().sort("templateName", Sort.ASCENDING);
        _adapter.updateTemplates(templateResults);
        notifyTemplatesUpdated(templateResults.size());
    }
    private void notifyTemplatesUpdated(int results) {
        for(EventTemplateFactoryInterface cfInterface : _interfaces) {
            cfInterface.onTemplatesUpdated(results);
        }
    }

    public ArrayList<EventTemplate> getTemplates() { return _adapter.getTemplates(); }
}
