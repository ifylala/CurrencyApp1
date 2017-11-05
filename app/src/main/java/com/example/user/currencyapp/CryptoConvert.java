package com.example.user.currencyapp;

/**
 * Created by USER on 10/28/2017.
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CryptoConvert extends AppCompatActivity {
    String currencyCode;
    String currencyFullName;
    EditText btcValueEdit, ethValueEdit, flatValueEdit;
    Button btcButton, ethButton, flatButton, closeButton;

    double btcRate;
    double ethRate;

    TextView moneyCodeView;
    TextView fullNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_convert);

        moneyCodeView = (TextView) findViewById(R.id.money_code_view);
        fullNameView = (TextView) findViewById(R.id.full_name_view);
        btcValueEdit = (EditText) findViewById(R.id.btc_value_edit);
        ethValueEdit = (EditText) findViewById(R.id.eth_value_edit);
        flatValueEdit = (EditText) findViewById(R.id.flat_value_edit);
        btcButton = (Button) findViewById(R.id.btc_convert_button);
        ethButton = (Button) findViewById(R.id.eth_convert_button);
        flatButton = (Button) findViewById(R.id.flat_convert_button);
        closeButton = (Button) findViewById(R.id.button_close);

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.no_anim);
                finish();
            }
        });

        Intent intent = getIntent();
        currencyCode = intent.getStringExtra(Consts.CURRENCY);
        btcRate = intent.getDoubleExtra(Consts.BTC_RATE, 0);
        ethRate = intent.getDoubleExtra(Consts.ETH_RATE, 0);
        currencyFullName = getFullName(currencyCode);
        String conversionMessage = currencyFullName + " Conversion";

        moneyCodeView.setText(currencyCode);
        fullNameView.setText(conversionMessage);
        if(getActionBar() != null) getActionBar().setTitle(currencyFullName);
        if(getSupportActionBar() != null) getSupportActionBar().setTitle(currencyFullName);

    }

    //Get full money name from curency code
    public String getFullName(String moneyCode) {
        switch (moneyCode) {
            case "NGN": return "Naira";
            case "USD": return "US Dollar";
            case "EUR": return "Euro";
            case "NAD": return "Namibian dollar";
            case "NOK": return "Norwegian krone";
            case "AUD": return "Australian Dollar";
            case "CAD": return "Canadian Dollar";
            case "CHF": return "Swiss Franc";
            case "CNY": return "Yuan";
            case "KES": return "Kenyan Shilling";
            case "GHS": return "Cedi";
            case "UGX": return "Ugandan Shilling";
            case "RSD": return "Serbian dinar";
            case "XAF": return "CFA Franc BCEAO";
            case "NZD": return "New Zealand Dollar";
            case "MYR": return "Malaysian Ringgit";
            case "BIF": return "Burundi franc";
            case "GEL": return "Lari";
            case "CLP": return "Chilean peso";
            case "INR": return "Indian Rupee";
            default: return "";
        }
    }

    @SuppressLint("DefaultLocale")
    //Method to do the conversion from one currency to the 2 others
    public void ConvertCurrency(View view) {
        if(view == btcButton) {
            try {
                double btcAmount = Double.parseDouble(btcValueEdit.getText().toString());
                flatValueEdit.setText(String.format("%1$,.2f", (btcAmount * btcRate)));
                ethValueEdit.setText(String.format("%1$,.2f", (btcAmount * (ethRate / btcRate))));
            } catch (NumberFormatException e) {
                Snackbar.make(findViewById(R.id.main_scroll_view), Consts.INVALID_CONVERSION, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }

        } else if(view == ethButton) {
            try {
                double ethAmount = Double.parseDouble(ethValueEdit.getText().toString());
                flatValueEdit.setText(String.format("%1$,.2f", (ethAmount * ethRate)));
                btcValueEdit.setText(String.format("%1$,.2f", (ethAmount * (btcRate / ethRate))));
            } catch (NumberFormatException e) {
                Snackbar.make(findViewById(R.id.main_scroll_view), Consts.INVALID_CONVERSION, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }

        } else if(view == flatButton) {
            try {
                double flatAmount = Double.parseDouble(flatValueEdit.getText().toString());
                btcValueEdit.setText(String.format("%1$,.2f", (flatAmount / btcRate)));
                ethValueEdit.setText(String.format("%1$,.2f", (flatAmount / ethRate)));
            } catch (NumberFormatException e) {
                Snackbar.make(findViewById(R.id.main_scroll_view), Consts.INVALID_CONVERSION, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
    }
}

