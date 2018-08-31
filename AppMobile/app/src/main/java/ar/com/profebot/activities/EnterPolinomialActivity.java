package ar.com.profebot.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.profebot.activities.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.com.profebot.service.ExpressionsManager;
import io.github.kexanie.library.MathView;

public class EnterPolinomialActivity extends AppCompatActivity {

    public static Map<Integer, Integer> polynomialTerms;
    private TextInputEditText coefficientTermInput;
    private TextInputEditText potentialTermInput;
    private ToggleButton signToogleButton;
    private String firstSign = "";
    private ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polinomial_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        polynomialTerms = new HashMap<>();

        Button enterPolinomial = (Button)findViewById(R.id.AddEquationButton);
        Button eraseLastTerm = (Button)findViewById(R.id.erase_last_term);
        Button deletePolinomial = (Button)findViewById(R.id.delete_polinomial);

        coefficientTermInput = (TextInputEditText) findViewById(R.id.coefficientTerm);
        potentialTermInput = (TextInputEditText) findViewById(R.id.potentialTerm);
        signToogleButton = (ToggleButton)findViewById(R.id.signToogleButton);

        CustomKeyboard customKeyboard = new CustomKeyboard();
        coefficientTermInput.setOnEditorActionListener(customKeyboard);
        potentialTermInput.setOnEditorActionListener(customKeyboard);

        ((Button)findViewById(R.id.clear_blackboard_id)).setVisibility(View.INVISIBLE);

        enterPolinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                addTerm();
            }
        });

        eraseLastTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                deleteLastTerm();
            }
        });
        deletePolinomial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                deletePolinomial();
            }
        });

        ((Button)findViewById(R.id.start_resolution_id)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SolvePolynomialActivity.class);
                startActivity(intent);
            }
        });

        playButton = (ImageButton)findViewById(R.id.solve_equation_id);
        disablePlayButton();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SolvePolynomialActivity.class);
                startActivity(intent);
            }
        });
    }

    class CustomKeyboard implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
            if(actionId == EditorInfo.IME_ACTION_DONE){
                if(coefficientTermInput.getText().length() == 0){
                    coefficientTermInput.requestFocus();
                }else if(potentialTermInput.getText().length() == 0){
                    potentialTermInput.requestFocus();
                }else{
                    coefficientTermInput.clearFocus();
                    potentialTermInput.clearFocus();
                    addTerm();
                }
            }
            return false;
        }
    }

    private void disablePlayButton(){
        playButton.setBackgroundResource(R.color.colorGreyText);
        playButton.setEnabled(false);
    }

    private void enablePlayButton(){
        playButton.setBackgroundResource(R.color.colorAccent);
        playButton.setEnabled(true);
    }

    private void addTerm(){
        if (this.validTerms(coefficientTermInput,potentialTermInput) & reachLimitOfTerms() ){
            String termSign = signToogleButton.isChecked() ? "-":"+";

            Integer coefficient = Integer.parseInt(coefficientTermInput.getText().toString());
            coefficient = signToogleButton.isChecked() ? -1 * coefficient : coefficient;
            Integer exponent = Integer.parseInt(potentialTermInput.getText().toString());
            Integer newCoefficient;
            if(!polynomialTerms.containsKey(exponent)){
                newCoefficient = coefficient;
            }else {
                newCoefficient = polynomialTerms.get(exponent) + coefficient;
                polynomialTerms.remove(exponent);
            }
            polynomialTerms.put(exponent, newCoefficient);

            String equation = beautifierEquation().trim();
            if (beautifierEquation().trim().substring(0,1).matches("-")){
                firstSign = "-";
                equation = equation.substring(1);
            } else {
                firstSign = "";
            }
            ExpressionsManager.setPolinomialEquation(equation.concat("=0"), getApplicationContext());
            ((MathView) findViewById(R.id.equation_to_solve_id)).config(
                    "MathJax.Hub.Config({\n"+
                            "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                            "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                            "         SVG: { linebreaks: { automatic: true } }\n"+
                            "});");
            ((MathView) findViewById(R.id.equation_to_solve_id)).setText("\\(\\color{White}{" + firstSign + ExpressionsManager.getPolinomialEquationAsLatex() + "}\\)" );
            coefficientTermInput.setText("");
            potentialTermInput.setText("");
            enablePlayButton();
        }
    }

    private boolean validTerms(TextInputEditText coefficientTermInput, TextInputEditText potentialTermInput) {
        StringBuilder message = new StringBuilder("");
        if(coefficientTermInput.getText().toString().matches("")){
            message.append("No olvides ingresar el coeficiente!\n");
        }
        if(potentialTermInput.getText().toString().matches("")){
            message.append("No olvides ingresar el exponente!\n");
        }

        if (message.length() > 0) {
            Toast toast1 = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();
            return false;
        } else {
            return true;
        }
    }

    public void deletePolinomial(){
        polynomialTerms = new HashMap<>();
        disablePlayButton();
        ((MathView) findViewById(R.id.equation_to_solve_id)).setText("$$" + "$$" );
    }

    public void deleteLastTerm(){
        if (polynomialTerms.size()!= 0) {
            polynomialTerms.remove(Collections.min(polynomialTerms.keySet()));
            if(polynomialTerms.size()!= 0){
                String equation = beautifierEquation().trim();
                if (beautifierEquation().trim().substring(0,1).matches("-")){
                    firstSign = "-";
                    equation = equation.substring(1);
                } else {
                    firstSign = "";
                }
                ExpressionsManager.setPolinomialEquation(equation.concat("=0"), getApplicationContext());
                ((MathView) findViewById(R.id.equation_to_solve_id)).config(
                        "MathJax.Hub.Config({\n"+
                                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                                "         SVG: { linebreaks: { automatic: true } }\n"+
                                "});");
                ((MathView) findViewById(R.id.equation_to_solve_id)).setText("\\(\\color{White}{" + firstSign + ExpressionsManager.getPolinomialEquationAsLatex() + "}\\)" );
            }else{
                disablePlayButton();
                ((MathView) findViewById(R.id.equation_to_solve_id)).setText("$$" + "$$" );
            }
        }
        else{
            disablePlayButton();
            ((MathView) findViewById(R.id.equation_to_solve_id)).setText("$$" + "$$" );
        }
    }
    private boolean reachLimitOfTerms() {
        int maxSize = 10;
        if (polynomialTerms.size() >= maxSize) {
            Toast toast1 = Toast.makeText(getApplicationContext(), "El polinomio ingresado no puede tener más de " + maxSize + " términos", Toast.LENGTH_LONG);
            toast1.setGravity(Gravity.CENTER, 0, 0);
            toast1.show();
            return false;
        }
        else{
            return true;
        }
    }

    public String beautifierEquation(){
        List<Integer> exponents = new ArrayList<>(polynomialTerms.keySet());
        Collections.sort(exponents, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 >= o1 ? 1 : -1;
            }
        });

        StringBuilder stringBuilder = new StringBuilder("");
        Boolean firstTerm = true;
        for(Integer exponent : exponents){
            Integer coefficient = polynomialTerms.get(exponent);
            String operator = "+";
            if(coefficient < 0 || firstTerm){
                operator = "";
            }
            stringBuilder.append(operator);
            stringBuilder.append(polynomialTerms.get(exponent));
            stringBuilder.append("x^");
            stringBuilder.append(exponent);
            firstTerm = false;
        }

        return stringBuilder.toString().replaceAll("x\\^0","");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, EnterPolinomialEquationOptionsActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, EnterPolinomialEquationOptionsActivity.class));
        return true;
    }
}
