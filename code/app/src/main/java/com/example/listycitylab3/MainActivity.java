// MainActivity.java (additions marked // NEW)
package com.example.listycitylab3;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        AddCityFragment.AddCityDialogListener {
    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;

    @Override
    public void addCity(City city) {
        cityAdapter.add(city);
        cityAdapter.notifyDataSetChanged();
    }

    // NEW: called by fragment after edits
    public void onCityUpdated(int position, City updatedCity) {
        if (position >= 0 && position < dataList.size()) {
            dataList.set(position, updatedCity); // replace or keep same instance
            cityAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] cities = { "Edmonton", "Vancouver", "Toronto" };
        String[] provinces = { "AB", "BC", "ON" };
        dataList = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }

        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        FloatingActionButton fab = findViewById(R.id.button_add_city);
        fab.setOnClickListener(v -> {
            new AddCityFragment().show(getSupportFragmentManager(), "AddCity");
        });

        // NEW: tap a row to edit that city
        cityList.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            City target = dataList.get(position);
            AddCityFragment dialog = AddCityFragment.newInstance(target, position);
            dialog.show(getSupportFragmentManager(), "EditCity");
        });
    }
}
