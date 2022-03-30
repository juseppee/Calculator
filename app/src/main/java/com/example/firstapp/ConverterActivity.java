package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConverterActivity extends AppCompatActivity {

    Double operand = null;  // операнд операции
    String lastOperation = "="; // последняя операция
    EditText numberField;   // поле для ввода числа
    TextView operationField;    // текстовое поле для вывода знака операции
    TextView resultField; // текстовое поле для вывода результата

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView calcView = findViewById(R.id.calcView);
            String age = extras.getString(MainActivity.result_Field);
            if (age.length() > 0)
                calcView.setText("Привет из калькулятора: " +  age);
        }
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

    public void onNumberClick(View view){
        if (numberField.getText().toString().equals("Error input")){
            numberField.setText("");
        }

        Button button = (Button)view;
        numberField.append(button.getText());

        if((lastOperation.equals("€") || lastOperation.equals("$") || lastOperation.equals("₮")) && operand!=null){
            operand = null;
        }
    }

    public void onClearInput(View view){
        resultField.setText("");
        numberField.setText("");
    }

    private void performOperation(Double number, String operation) {

        // если операнд ранее не был установлен (при вводе самой первой операции)
        if (operand == null) {
            operand = number;
        } else {
            if (lastOperation.equals("€") || lastOperation.equals("$") || lastOperation.equals("₮")) {
                lastOperation = operation;
            }
            switch (lastOperation) {
                case "=":
                    operand = number;
                    break;
                case "€":
                    operand /= 96.47;
                    break;
                case "$":
                    operand /= 87.00;
                    break;
                case "₮":
                    operand /= 0.03;
                    break;
            }
            resultField.setText(operand.toString().replace('.', ','));
            numberField.setText("");
        }
    }



    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClick1(View view) {
        Intent intent = new Intent(this, StateActivity.class);
        startActivity(intent);
    }

    private void sendMessage(String message){

        Intent data = new Intent();
//        data.putExtra(MainActivity.ACCESS_MESSAGE, message);
        setResult(RESULT_OK, data);
        finish();
    }
}
