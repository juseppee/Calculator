package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StateActivity extends AppCompatActivity {
    ArrayList<State> states = new ArrayList<State>();
    ListView countriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        // начальная инициализация списка
        setInitialData();
        // получаем элемент ListView
        countriesList = findViewById(R.id.countriesList);
        // создаем адаптер
        StateAdapter stateAdapter = new StateAdapter(this, R.layout.list_item, states);
        // устанавливаем адаптер
        countriesList.setAdapter(stateAdapter);
        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // получаем выбранный пункт
                State selectedState = (State)parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Был выбран пункт " + selectedState.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        };
        countriesList.setOnItemClickListener(itemListener);
    }

    public void onClick1(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClick2(View view) {
        Intent intent = new Intent(this, ConverterActivity.class);
        startActivity(intent);
    }

    private void setInitialData(){
        states.add(new State ("Россия", "Российский рубль", R.drawable.russia));
        states.add(new State ("США", "Американский доллар", R.drawable.flagusa));
        states.add(new State ("Евросоюз", "Евро", R.drawable.euflag));
        states.add(new State ("Монголия", "Монгольский тугрик", R.drawable.mongol));
    }
}
