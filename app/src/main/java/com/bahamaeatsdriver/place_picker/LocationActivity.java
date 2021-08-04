package com.bahamaeatsdriver.place_picker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bahamaeatsdriver.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

public class LocationActivity extends AppCompatActivity implements
        PlaceAutoCompleteAdapter.ClickListener
{
    private PlaceAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Places.initialize(this, getResources().getString(R.string.google_maps_key));

        recyclerView = findViewById(R.id.places_recycler_view);
        back = findViewById(R.id.iv_backArrow);
        back.setOnClickListener(view -> finish());

        ((EditText) findViewById(R.id.place_search)).addTextChangedListener(filterTextWatcher);

        mAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {recyclerView.setVisibility(View.VISIBLE);}
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {recyclerView.setVisibility(View.GONE);}
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
    };

    @Override
    public void click(Place place)
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("address",place.getAddress());
        returnIntent.putExtra("latitude",String.valueOf(place.getLatLng().latitude));
        returnIntent.putExtra("longitude",String.valueOf(place.getLatLng().longitude));
        returnIntent.putExtra("name",place.getName());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
