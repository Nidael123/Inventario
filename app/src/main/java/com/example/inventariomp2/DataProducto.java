package com.example.inventariomp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.inventariomp2.adaptadores.AdapterItem;
import com.example.inventariomp2.clases.Productos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataProducto extends AppCompatActivity {

    TextView txt_producto,txt_max,txt_min,txt_total;
    RecyclerView recicler;
    Button btn_guardar,btn_regresar;
    String id_producto,api_productos;
    RequestQueue n_requerimiento;
    ArrayList<Productos> productos;
    AdapterItem adaptador;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_producto);

        txt_producto = findViewById(R.id.txt_p_nombre);
        txt_max = findViewById(R.id.txt_p_max);
        txt_min = findViewById(R.id.txt_p_min);
        txt_total = findViewById(R.id.txt_p_total);
        btn_guardar = findViewById(R.id.btn_p_guardar);
        btn_regresar = findViewById(R.id.btn_p_salir);

        productos = new ArrayList<>();
        api_productos = getString(R.string.api_productos);
        id_producto = getIntent().getExtras().getString("id_producto");
        Log.d("datos",id_producto);

        cargar();



        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_max.getText() != "" && txt_min.getText() != "")
                {
                    guarda(txt_max.getText().toString(),txt_min.getText().toString());
                }else{
                    Toast.makeText(DataProducto.this,"LLENE LOS TODOS LOS CAMPOS",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cargar()
    {
        final int[] total = {0};
        int total2=0;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_productos+"?id_producto="+id_producto, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    jsonObject = new JSONObject(jsonArray.get(0).toString());
                    txt_producto.setText(jsonObject.getString("producto"));
                    txt_max.setText(jsonObject.getInt("maximo")+"");
                    txt_min.setText(jsonObject.getInt("minimo")+"");

                    for(int i = 0;i<=jsonArray.length()-1;i++)
                    {
                        Productos help = new Productos();
                        jsonObject = new JSONObject(jsonArray.get(i).toString());
                        help.setProducto(jsonObject.getString("producto"));
                        help.setCantidad(jsonObject.getInt("cantidad"));
                        help.setMaximo(jsonObject.getInt("maximo"));
                        help.setMinimo(jsonObject.getInt("minimo"));
                        help.setId_producto("");
                        help.setBodega(jsonObject.getString("bodega"));
                        //help.setTotal(jsonObject.getInt("cantidad"));
                        help.setUbicacion(jsonObject.getString("ubicacion"));
                        Log.d("mensaje",jsonObject.getString("bodega"));
                        productos.add(help);
                        total[0] = total[0] + jsonObject.getInt("cantidad");
                    }
                    txt_total.setText("Total en el inventario: "+(total[0]));
                    adaptador = new AdapterItem(productos);
                    //recicler.setAdapter(adaptador);
                }catch (JSONException e)
                {
                    Log.d("items",e.toString());
                    Toast.makeText(DataProducto.this,"Error de base consulte con sistemas",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("items",error.toString());
                Toast.makeText(DataProducto.this,"Error de CONEXION consulte con sistemas",Toast.LENGTH_SHORT).show();
            }
        });
        n_requerimiento = Volley.newRequestQueue(this);
        n_requerimiento.add(jsonObjectRequest);
    }

    public void guarda(String max,String min)
    {
        StringRequest requerimiento = new StringRequest(Request.Method.POST, api_productos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(DataProducto.this,"Todo bien Todo bonito",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DataProducto.this,"error guardado",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();

                parametros.put("v_maximo",max);
                parametros.put("v_minimo",min);
                parametros.put("v_id_producto",id_producto);
                return parametros;
            }
        };
        n_requerimiento = Volley.newRequestQueue(this);
        n_requerimiento.add(requerimiento);
    }
}