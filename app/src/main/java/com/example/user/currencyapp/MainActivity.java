package com.example.user.currencyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    LinearLayout mainView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView ratesListView;
    public CryptoRate cryptoRate;
    RequestQueue rQueue;
    String requestUrl;

    SharedPreferences settings;
    int orderMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainView = (LinearLayout) findViewById(R.id.main_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        ratesListView = (ListView) findViewById(R.id.rates_list_view);
        ratesListView.setDivider(null); //to remove dividers from the list view

        CryptoRate cryptoRate = new CryptoRate();
        rQueue = Volley.newRequestQueue(getApplicationContext());
        requestUrl = Consts.url; //Currencies will NOT be displayed in this here order
        fetchRate();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchRate();
            }
        });
    }

    private static class ViewHolder {
        private TextView currencyTextView;
        private TextView btcTextView;
        private TextView ethTextView;
    }


    private class CryptoAdapter extends BaseAdapter {
        ArrayList<CryptoRate.MoneyRate> rates;

        private CryptoAdapter(ArrayList<CryptoRate.MoneyRate> ratesInstance) {
            rates = ratesInstance;
        }

        public int getCount() {
            return rates.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("DefaultLocale")
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.single_layout, parent, false);
                holder = new ViewHolder();
                holder.currencyTextView = (TextView) convertView.findViewById(R.id.currency_name);
                holder.btcTextView = (TextView) convertView.findViewById(R.id.btc_rate);
                holder.ethTextView = (TextView) convertView.findViewById(R.id.eth_rate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final CryptoRate.MoneyRate rateRow = rates.get(position);

            final String crossCurrency = rateRow.getCurrency();
            final double crossBtc = rateRow.getBtcRate();
            final double crossEth = rateRow.getEthRate();

            holder.currencyTextView.setText(crossCurrency);
            holder.btcTextView.setText(String.format("%1$,.2f", crossBtc));
            holder.ethTextView.setText(String.format("%1$,.2f", crossEth));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, CryptoConvert.class);
                    intent.putExtra(Consts.CURRENCY, crossCurrency);
                    intent.putExtra(Consts.BTC_RATE, crossBtc);
                    intent.putExtra(Consts.ETH_RATE, crossEth);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.no_anim);
                }
            });

            return convertView;

        }
    }


    //Method downloads the rates of the 20 currencies in the URL, receives JSON response, parses response and displays rates
    public void fetchRate() {
        cryptoRate = new CryptoRate();

        JsonObjectRequest requestNameAvatar = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject btc_rates = response.getJSONObject("BTC".trim());
                            JSONObject eth_rates = response.getJSONObject("ETH".trim());

                            Iterator<?> keysBTC = btc_rates.keys();
                            Iterator<?> keysETH = eth_rates.keys();

                            while (keysBTC.hasNext() && keysETH.hasNext()) {
                                String keyBTC = (String) keysBTC.next();
                                String keyETH = (String) keysETH.next();

                                cryptoRate.add(keyBTC, btc_rates.getDouble(keyBTC), eth_rates.getDouble(keyETH));
                            }

                            mSwipeRefreshLayout.setRefreshing(false); //to remove the progress bar for refresh
                            cryptoRate.orderList(orderMode);
                            ratesListView.setAdapter(new CryptoAdapter(cryptoRate.ratesArrayList));


                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error parsing data", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(mainView, Consts.REFRESH_ERROR, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }


        });

        rQueue.add(requestNameAvatar);
    }

    @Override
    protected void onStop() {
        super.onStop();
        rQueue.cancelAll(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            mSwipeRefreshLayout.setRefreshing(true); //to show the progress bar for refresh
            fetchRate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
