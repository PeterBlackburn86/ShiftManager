package com.peterblackburn.shiftmanager.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.peterblackburn.shiftmanager.Calendar.AddEventActivity;
import com.peterblackburn.shiftmanager.Events.EventTemplateFactory;
import com.peterblackburn.shiftmanager.Events.Interfaces.EventTemplateFactoryInterface;
import com.peterblackburn.shiftmanager.R;

public class TemplateFragment extends BaseFragment implements EventTemplateFactoryInterface {

    private static TemplateFragment _instance;
    private static final int ADD_TEMPLATE_CODE = 1;


    private RelativeLayout _templateContainer;
    private RelativeLayout _noTemplateContainer;
    private RecyclerView _templateRecycler;
    private ImageView _addTemplateButton;

    private EventTemplateFactory _templateFactory;
    private Context _context;

    public  TemplateFragment() {
    }

    public static TemplateFragment getInstance() {
        if(_instance == null)
            _instance = new TemplateFragment();
        return _instance;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_TEMPLATE_CODE) {
            _templateFactory.updateTemplates();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_templates, container, false);
        _templateContainer = view.findViewById(R.id.templateContainer);
        _noTemplateContainer = view.findViewById(R.id.noTemplateContainer);
        _templateRecycler = view.findViewById(R.id.templateRecycler);
        _addTemplateButton = view.findViewById(R.id.addTemplateBtn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TEMPLATES EVENTS SETUP
        _templateFactory = EventTemplateFactory.getInstance();

        _templateFactory.initEventFactory(_context, _templateRecycler, this);


        _templateFactory.updateTemplates();

        _addTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context, AddEventActivity.class);
                intent.putExtra("setSelectedDate", "");
                intent.putExtra("setIsTemplate", true);
                startActivityForResult(intent, ADD_TEMPLATE_CODE);
            }
        });
    }

    @Override
    public String getTitle() {
        return "Templates";
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _context = context;
        if(_templateFactory != null) {
            _templateFactory.addFactoryInterface(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(_templateFactory != null) {
            _templateFactory.removeFactoryInterface(this);
            _context = null;
        }
    }

    @Override
    public void onTemplateRemoved() {

    }

    @Override
    public void onTemplateAdded() {

    }

    @Override
    public void onTemplatesUpdated(int results) {
        if(results > 0) {
            _templateContainer.setVisibility(View.VISIBLE);
            _noTemplateContainer.setVisibility(View.GONE);
        } else {
            _templateContainer.setVisibility(View.GONE);
            _noTemplateContainer.setVisibility(View.VISIBLE);
        }
    }
}
