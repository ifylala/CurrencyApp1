package com.example.user.currencyapp;

/**
 * Created by USER on 10/28/2017.
 */

public interface Consts {

    String CURRENCY = "extra currency";
    String BTC_RATE = "extra btc";
    String ETH_RATE = "extra eth";

    int ORDER_ALPHABETICAL = 1;
    int ORDER_BY_RATE = 2;

    String INVALID_CONVERSION = "Invalid conversion value provided";
    String REFRESH_ERROR = "Could not refresh";
    String url ="https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=" +
            "NGN,USD,EUR,NAD,NOK,AUD,CAD,CHF,CNY,KES,GHS,UGX,RSD,XAF,NZD,MYR,BIF,GEL,CLP,INR";

}