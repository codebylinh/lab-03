// AddCityFragment.java
package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    public interface AddCityDialogListener {
        void addCity(City city);
        default void onCityUpdated(int position, City updatedCity) {}
    }

    private static final String ARG_CITY = "arg_city";
    private static final String ARG_POSITION = "arg_position";

    private AddCityDialogListener listener;

    public AddCityFragment() {} // required

    // Preferred way to open in EDIT mode (pass null for ADD mode)
    public static AddCityFragment newInstance(@Nullable City city, int position) {
        AddCityFragment f = new AddCityFragment();
        if (city != null) {
            Bundle b = new Bundle();
            b.putSerializable(ARG_CITY, city);
            b.putInt(ARG_POSITION, position);
            f.setArguments(b);
        }
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new IllegalStateException("Host Activity must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_add_city, null);

        EditText nameEt = view.findViewById(R.id.edit_text_city_text);
        EditText provEt = view.findViewById(R.id.edit_text_province_text);

        Bundle args = getArguments();
        final boolean isEdit = args != null && args.containsKey(ARG_CITY);
        final City editing = isEdit ? (City) args.getSerializable(ARG_CITY) : null;
        final int position = isEdit ? args.getInt(ARG_POSITION, -1) : -1;

        if (isEdit && editing != null) {
            nameEt.setText(editing.getName());
            provEt.setText(editing.getProvince());
        }

        return new AlertDialog.Builder(requireContext())
                .setTitle("Add/edit city")
                .setView(view)
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                .setPositiveButton("OK", (d, w) -> {
                    String name = nameEt.getText().toString().trim();
                    String prov = provEt.getText().toString().trim();
                    if (name.isEmpty() || prov.isEmpty()) return;

                    if (isEdit && editing != null && position >= 0) {
                        editing.setName(name);
                        editing.setProvince(prov);
                        listener.onCityUpdated(position, editing);
                    } else {
                        listener.addCity(new City(name, prov));
                    }
                })
                .create();
    }
}
