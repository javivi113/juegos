package com.example.evalu1;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoJuego extends AppCompatActivity {
    String juegoUrlInfor;
    JSONObject juegoInfor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_juego);
        juegoUrlInfor= MainActivity.juegoUrlInfo;
        cargarInfo(juegoUrlInfor);


    }
    public void cargarInfo(String url){
        StringRequest postResquest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    juegoInfor = new JSONObject(response);
                    insertarDatos();
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
    public void insertarDatos(){
        ImageView mainIm=findViewById(R.id.mainImg);
        TextView tvTitulo=findViewById(R.id.tvTitulo);
        TextView tvDesc=findViewById(R.id.tvDescp);
        TextView tvCategoria=findViewById(R.id.tvCatego);
        TextView tvDesarrol=findViewById(R.id.tvDesa);
        TextView tvplat=findViewById(R.id.tvPlatform);
        TextView tvpubli=findViewById(R.id.tvpubli);
        TextView tvpOS=findViewById(R.id.tvOs);
        TextView tvGra=findViewById(R.id.tvGraphics);
        TextView tvsto=findViewById(R.id.tvStorage);
        TextView tvpro=findViewById(R.id.tvProcessor);
        TextView tvMemo=findViewById(R.id.tvMemo);
        try {
            Picasso.get().load(juegoInfor.getString("thumbnail")).into(mainIm);
            tvTitulo.setText(juegoInfor.getString("title"));
            tvDesc.setText(juegoInfor.getString("description"));
            tvplat.setText("Plataforma: "+juegoInfor.getString("platform"));
            tvCategoria.setText("Genero: "+juegoInfor.getString("genre"));
            tvDesarrol.setText("Desarrolador: "+juegoInfor.getString("developer"));
            tvpubli.setText("Fecha de salida: "+juegoInfor.getString("release_date"));
            tvpOS.setText("Sistema Operativo: "+ juegoInfor.getJSONObject("minimum_system_requirements").getString("os"));
            tvGra.setText("Gráfica: "+juegoInfor.getJSONObject("minimum_system_requirements").getString("graphics"));
            tvMemo.setText("Memoria: "+juegoInfor.getJSONObject("minimum_system_requirements").getString("memory"));
            tvsto.setText("Almacenamiento: "+juegoInfor.getJSONObject("minimum_system_requirements").getString("storage"));
            tvpro.setText("Procesador: "+juegoInfor.getJSONObject("minimum_system_requirements").getString("processor"));
            JSONArray imgs= juegoInfor.getJSONArray("screenshots");
            for(int i =0; i<5; i++){
                switch (i){
                    case 0:
                        ImageView img1=findViewById(R.id.img1);
                        Picasso.get().load(imgs.getJSONObject(i).getString("image")).into(img1);
                        break;
                    case 1:
                        ImageView img2=findViewById(R.id.img2);
                        Picasso.get().load(imgs.getJSONObject(i).getString("image")).into(img2);
                        break;
                    case 2:
                        ImageView img3=findViewById(R.id.img3);
                        Picasso.get().load(imgs.getJSONObject(i).getString("image")).into(img3);
                        break;
                    case 3:
                        ImageView img4=findViewById(R.id.img4);
                        Picasso.get().load(imgs.getJSONObject(i).getString("image")).into(img4);
                        break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}