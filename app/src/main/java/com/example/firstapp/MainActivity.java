package com.example.firstapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    static final String result_Field = "result_field"; // текстовое поле для вывода результата

    TextView resultField; // текстовое поле для вывода результата
    EditText numberField;   // поле для ввода числа
    TextView operationField;    // текстовое поле для вывода знака операции
    Double operand = null;  // операнд операции
    String lastOperation = "="; // последняя операция
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // получаем все поля по id из activity_main.xml
        resultField = findViewById(R.id.resultField);
        numberField = findViewById(R.id.numberField);
        operationField = findViewById(R.id.operationField);
    }
    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("OPERATION", lastOperation);
        if(operand!=null)
            outState.putDouble("OPERAND", operand);
        super.onSaveInstanceState(outState);
    }
    // получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand= savedInstanceState.getDouble("OPERAND");
        resultField.setText(operand.toString());
        operationField.setText(lastOperation);
    }
    // обработка нажатия на числовую кнопку
    public void onNumberClick(View view){
        if (numberField.getText().toString().equals("Error input")){
            numberField.setText("");
        }

        Button button = (Button)view;
        numberField.append(button.getText());

        if(lastOperation.equals("=") && operand!=null){
            operand = null;
        }
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    TextView textView = findViewById(R.id.resultField);
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        String accessMessage = intent.getStringExtra(result_Field);
                        textView.setText(accessMessage);
                    }
                    else{
                        textView.setText("");
                    }
                }
            });

    public void onClick(View view) {
        Intent intent = new Intent(this, ConverterActivity.class);
        String res = resultField.getText().toString();
        intent.putExtra(result_Field, res);
        mStartForResult.launch(intent);
    }

    public void onClick1(View view) {
        Intent intent = new Intent(this, StateActivity.class);
        startActivity(intent);
    }


    // обработка нажатия на кнопку операции
    public void onOperationClick(View view){

        Button button = (Button)view;
        String op = button.getText().toString();
        String number = numberField.getText().toString();
        // если введенно что-нибудь
        if(number.length()>0){
            number = number.replace(',', '.');
            try{
                performOperation(Double.valueOf(number), op);
            }catch (NumberFormatException ex){
                numberField.setText("Error input");
            }
        }
        lastOperation = op;
        operationField.setText(lastOperation);
    }

    public void onPrintE(View view){

        if (numberField.getText().toString().equals("Error input")){
            numberField.setText("");
        }
        numberField.append(String.valueOf(Math.E));

        if(lastOperation.equals("=") && operand!=null){
            operand = null;
        }
    }

    public void onClearInput(View view){
        resultField.setText("");
        numberField.setText("");
    }

    private void performOperation(Double number, String operation){

        // если операнд ранее не был установлен (при вводе самой первой операции)
        if(operand ==null){
            operand = number;
        }
        else{
            if(lastOperation.equals("=")){
                lastOperation = operation;
            }
            switch(lastOperation){
                case "=":
                    operand =number;
                    break;
                case "/":
                    if(number==0){
                        operand =0.0;
                    }
                    else{
                        operand /=number;
                    }
                    break;
                case "*":
                    operand *=number;
                    break;
                case "+":
                    operand +=number;
                    break;
                case "-":
                    operand -=number;
                    break;
                case "^":
                    operand = Math.pow(operand, number);
                    break;
                case "√":
                    operand = Math.pow(operand, 1/number);
                    break;
            }
        }
        resultField.setText(operand.toString().replace('.', ','));
        numberField.setText("");
    }
}