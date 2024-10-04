package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class quick_calc extends AppCompatActivity {
    private StringBuilder expression;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_calc);
        textView = findViewById(R.id.display_text);
        expression = new StringBuilder("CALC");
        textView.setText(expression.toString());

        Button button0 = findViewById(R.id.button0);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);
        Button buttonPlus = findViewById(R.id.button_plus);
        Button buttonMinus = findViewById(R.id.button_sub);
        Button buttonEqual = findViewById(R.id.button_equal);
        Button buttonBackspace = findViewById(R.id.button_x);

        setNumberButtonListeners(button0, "0");
        setNumberButtonListeners(button1, "1");
        setNumberButtonListeners(button2, "2");
        setNumberButtonListeners(button3, "3");
        setNumberButtonListeners(button4, "4");
        setNumberButtonListeners(button5, "5");
        setNumberButtonListeners(button6, "6");
        setNumberButtonListeners(button7, "7");
        setNumberButtonListeners(button8, "8");
        setNumberButtonListeners(button9, "9");

        setOperatorButtonListener(buttonPlus, "+");
        setOperatorButtonListener(buttonMinus, "-");

        buttonEqual.setOnClickListener(v -> calculateResult());

        buttonBackspace.setOnClickListener(v -> removeLastCharacter());

    }

    private void removeLastCharacter() {
        if (expression.length() > 0 && !expression.toString().equals("CALC")) {
            expression.deleteCharAt(expression.length() - 1);
            if (expression.length() == 0) {
                expression.append("CALC"); // Reset to initial state
            }
            textView.setText(expression.toString());
        }
    }

    private void calculateResult() {
        try {
            String result = evaluateExpression(expression.toString());
            expression.setLength(0);
            expression.append(result);
            textView.setText(result);
        } catch (Exception e) {
            textView.setText("Error");
            expression.setLength(0);
        }
    }

    private void setOperatorButtonListener(Button button, String operator) {
        button.setOnClickListener(v -> {
            if (expression.toString().equals("CALC")) {
                expression.setLength(0);  // Reset expression if it's the initial state
            }
            expression.append(operator);
            textView.setText(expression.toString());
        });
    }

    private void setNumberButtonListeners(Button button, String value) {
        button.setOnClickListener(v -> {
            if (expression.toString().equals("CALC")) {
                expression.setLength(0);  // Reset expression if it's the initial state
            }
            expression.append(value);
            textView.setText(expression.toString());
        });
    }

    private String evaluateExpression(String expression) {
        try {
            if (expression.equals("CALC")) return "0";

            String[] tokens = expression.split("(?=[+-])|(?<=[+-])");
            double result = Double.parseDouble(tokens[0]);

            for (int i = 1; i < tokens.length; i += 2) {
                String operator = tokens[i];
                double operand = Double.parseDouble(tokens[i + 1]);

                if (operator.equals("+")) {
                    result += operand;
                } else if (operator.equals("-")) {
                    result -= operand;
                }
            }
            return String.valueOf(result);
        } catch (Exception e) {
            return "Error";
        }
    }
}

