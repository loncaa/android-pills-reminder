package hr.magicpot.projectpliva.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.magicpot.projectpliva.R;
import hr.magicpot.projectpliva.dialogs.MyTimePickerDialog;

/**
 * Created by xxx on 5.4.2016..
 */
public class AddpillsFragment extends Fragment{
    public AddpillsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.add_layout)
    public void onClick(View v)
    {
        DialogFragment dialogFragment = new MyTimePickerDialog();
        dialogFragment.show(getActivity().getSupportFragmentManager(), "time_fragment");
    }
}
