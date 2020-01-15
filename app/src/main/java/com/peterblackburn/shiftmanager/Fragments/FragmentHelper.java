package com.peterblackburn.shiftmanager.Fragments;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import com.peterblackburn.shiftmanager.R;

public class FragmentHelper {

    private static FragmentHelper _instance;
    private Context _context;
    private FragmentManager _manager;
    private BaseFragment _lastFragment;

    private FragmentHelper(FragmentManager manager, Context context) {
        _context = context;
        _manager = manager;
    }

    public static FragmentHelper getInstance(FragmentManager manager, Context context) {
        if(_instance == null)
            _instance = new FragmentHelper(manager, context);

        _instance.setContext(context);
        _instance.setFragmentManager(manager);
        return _instance;
    }

    public void setFragmentManager(FragmentManager value) { _manager = value; }
    public void setContext(Context value) { _context = value; }

    public boolean loadLastFragment() {
        if(_lastFragment != null) {
            if(_manager != null) {
                _manager.beginTransaction().
                        replace(R.id.fragmentContainer, _lastFragment).commit();
                return true;
            }
        }
        return false;
    }

    public boolean loadFragment(BaseFragment fragment) {
        if(fragment != null && _manager != null) {
            _manager.beginTransaction().
                    replace(R.id.fragmentContainer, fragment).commit();
            _lastFragment = fragment;
            return true;
        }
        return false;
    }

    public String getLastFragmentTitle() {
        return _lastFragment.getTitle();
    }


}
