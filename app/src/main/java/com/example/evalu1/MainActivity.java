package com.example.evalu1;


import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends Activity {
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String opcionFiltro = "nameKey";
    public static final String opcionText = "emailKey";
    LinearLayout lyVJuegos;
    String[] opciones={"Todo","Año", "Categoria", "Titulo" };
    Spinner sp;
    String url = "https://www.freetogame.com/api/games";
    TextInputLayout txtINpLy;
    String OpBusqueda="Todo";
    JSONArray leerApiArrayRes;
    JSONArray jsonArray;
    static String juegoUrlInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lyVJuegos=findViewById(R.id.lyJuegos);
        sp=findViewById(R.id.spinner);
        txtINpLy =findViewById(R.id. textInputLayout);
        txtINpLy.setVisibility(View.INVISIBLE);
        findViewById(R.id.btnLastSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtrarResp(opcionText,opcionFiltro);
            }
        });
        sharedpreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
        if (sharedpreferences.contains(opcionText) && sharedpreferences.contains((opcionFiltro))) {
            findViewById(R.id.btnLastSearch).setEnabled(true);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        LeerApi(url);
        jsonArray=leerApiArrayRes;
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String op=sp.getSelectedItem().toString();
                switch (op){
                    case "Año":
                        txtINpLy.setVisibility(View.VISIBLE);
                        OpBusqueda="Año";
                        break;
                    case "Categoria":
                        txtINpLy.setVisibility(View.VISIBLE);
                        OpBusqueda="Categoria";
                        break;
                    case "Titulo":
                        txtINpLy.setVisibility(View.VISIBLE);
                        OpBusqueda="Titulo";
                        break;
                    default:
                        txtINpLy.setVisibility(View.INVISIBLE);
                        OpBusqueda="Todo";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                url = "https://www.freetogame.com/api/games";
            }
        });
        findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.input);
                String campo = input.getText().toString();
                lyVJuegos.removeAllViews();
                if (OpBusqueda.equals("Todo")){
                    try {
                        insertarJuegos();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(opcionFiltro, campo);
                    editor.putString(opcionText, OpBusqueda);
                    editor.commit();
                    findViewById(R.id.btnLastSearch).setEnabled(true);
                    filtrarResp(campo,OpBusqueda);
                }
            }
        });
    }
    public void filtrarResp( String param, String modo){
        try{
            switch (modo){
                case "Año":
                    for (int i=0; i<leerApiArrayRes.length(); i++){
                        JSONObject jsonObject=leerApiArrayRes.getJSONObject(i);
                        try {
                            if (jsonObject.getString("release_date").substring(0,4).equals(param)){
                                addCardUnic(jsonObject.getString("title"), jsonObject.getString("thumbnail"));
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    break;
                case "Titulo":
                    for (int i=0; i<leerApiArrayRes.length(); i++){
                        JSONObject jsonObject=leerApiArrayRes.getJSONObject(i);
                        try {
                            if (jsonObject.getString("title").toLowerCase(Locale.ROOT).contains(param.toLowerCase(Locale.ROOT))){
                                addCardUnic(jsonObject.getString("title"), jsonObject.getString("thumbnail"));
                                break;
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    break;
                case "Categoria":
                    for (int i=0; i<leerApiArrayRes.length(); i++){
                        JSONObject jsonObject=leerApiArrayRes.getJSONObject(i);
                        try {
                            if (jsonObject.getString("genre").contains(param)){
                                addCardUnic(jsonObject.getString("title"), jsonObject.getString("thumbnail"));
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void insertarJuegos() throws JSONException {
        for (int i=0; i<leerApiArrayRes.length(); i++){
            JSONObject jsonObject=leerApiArrayRes.getJSONObject(i);
            try {
                JSONObject jsonObject1=leerApiArrayRes.getJSONObject(i+1);
                addCard(jsonObject.getString("title"), jsonObject.getString("thumbnail"),jsonObject1.getString("title"), jsonObject1.getString("thumbnail"));
            }catch (JSONException e){
                addCard(jsonObject.getString("title"), jsonObject.getString("thumbnail"),"", "");
            }
            i++;
        }
    }
    public void LeerApi(String url){
        StringRequest postResquest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    leerApiArrayRes = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(postResquest);
    }
    public void addCard(String nombre1, String url1,String nombre2, String url2){
        View view =getLayoutInflater().inflate(R.layout.juego, null);
        TextView nameView =view.findViewById(R.id.name);
        ImageView imgJuego= view.findViewById(R.id.imageView);
        CardView cvIzq= view.findViewById(R.id.derechaCard);
        CardView cvDere= view.findViewById(R.id.izqCard);

        nameView.setText(nombre1);
        Picasso.get().load(url1).into(imgJuego);
        cvIzq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jogo = nombre1;
                try {
                    GetinfoJuego(jogo);
                    Intent myIntent = new Intent(MainActivity.this, InfoJuego.class);
                    MainActivity.this.startActivity(myIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        TextView nameView2 =view.findViewById(R.id.name2);
        ImageView imgJuego2= view.findViewById(R.id.imageView2);
        if (!(nombre2.equals("")||nombre2.equals(null))){
            nameView2.setText(nombre2);
            Picasso.get().load(url2).into(imgJuego2);
            cvDere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String jogo = nombre2;
                    try {
                        GetinfoJuego(jogo);
                        Intent myIntent = new Intent(MainActivity.this, InfoJuego.class);
                        MainActivity.this.startActivity(myIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        lyVJuegos.addView(view);
    }
    public void GetinfoJuego( String param) throws JSONException {
        String urlQ="https://www.freetogame.com/api/game?id=";
        for (int i=0; i<leerApiArrayRes.length(); i++){
            JSONObject jsonObject=leerApiArrayRes.getJSONObject(i);
            try {
                if (jsonObject.getString("title").toLowerCase(Locale.ROOT).contains(param.toLowerCase(Locale.ROOT))){
                    urlQ+=jsonObject.getString("id");
                    System.out.println(urlQ);
                    juegoUrlInfo=urlQ;
                    break;
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
    public void addCardUnic(String nombre1, String url1){
        View view =getLayoutInflater().inflate(R.layout.juegounico, null);
        TextView nameView =view.findViewById(R.id.name2);
        ImageView imgJuego= view.findViewById(R.id.imageView2);
        CardView cvUniq= view.findViewById(R.id.cardUniq);
        nameView.setText(nombre1);
        Picasso.get().load(url1).into(imgJuego);
        lyVJuegos.addView(view);
        cvUniq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jogo =nameView.getText().toString();
                try {
                    GetinfoJuego(jogo);
                    Intent myIntent = new Intent(MainActivity.this, InfoJuego.class);
                    MainActivity.this.startActivity(myIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
