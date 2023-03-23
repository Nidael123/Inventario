package com.example.inventariomp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.inventariomp2.adaptadores.AdapterItem;
import com.example.inventariomp2.clases.Productos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Items extends AppCompatActivity {

    RecyclerView recicler;
    AdapterItem adaptador;
    ArrayList<Productos> productos;
    String api_inventario;
    RequestQueue n_requerimiento;
    JSONObject jsonObject;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        recicler = findViewById(R.id.i_recicler);
        productos = new ArrayList<>();
        api_inventario=getString(R.string.api_inventario);
        recicler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        cargar_inventario();

    }
    public void cargar_inventario()
    {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_inventario+"?bandera=1", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("items","cargo2");
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    Log.d("items",jsonArray.toString());
                    for(int i = 0;i<=jsonArray.length()-1;i++)
                    {
                        Productos help = new Productos();
                        jsonObject = new JSONObject(jsonArray.get(i).toString());
                        help.setProducto(jsonObject.getString("producto"));
                        help.setCantidad(jsonObject.getInt("cantidad"));
                        help.setMaximo(jsonObject.getInt("maximo"));
                        help.setMinimo(jsonObject.getInt("minimo"));
                        help.setId_producto("");
                        help.setBodega("");
                        help.setTotal(jsonObject.getInt("cantidad"));
                        help.setUbicacion("");
                        //Log.d("mensaje",help.getProducto());
                        productos.add(help);
                    }
                    Log.d("Items",""+productos.get(1).getId_producto());
                    adaptador = new AdapterItem(productos);
                    recicler.setAdapter(adaptador);
                }catch (JSONException e)
                {
                    Log.d("items",e.toString());
                    Toast.makeText(Items.this,"Error de base consulte con sistemas",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("items",error.toString());
                Toast.makeText(Items.this,"Error de CONEXION consulte con sistemas",Toast.LENGTH_SHORT).show();
            }
        });
        n_requerimiento = Volley.newRequestQueue(this);
        n_requerimiento.add(jsonObjectRequest);
    }
}