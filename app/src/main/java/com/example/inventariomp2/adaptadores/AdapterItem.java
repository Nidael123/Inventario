package com.example.inventariomp2.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventariomp2.DataProducto;
import com.example.inventariomp2.R;
import com.example.inventariomp2.clases.Productos;

import java.util.ArrayList;
import java.util.zip.DataFormatException;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ViewHolder> {

    ArrayList<Productos> producto;

    public AdapterItem (ArrayList<Productos> p)
    {
        producto = p;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View vista = layout.inflate(R.layout.item_producto,parent,false);
        ViewHolder viewHolder = new ViewHolder(vista);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("adapter",position+"");
        holder.asignardatos(producto.get(position));
    }

    @Override
    public int getItemCount() {
        return producto.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String id_producto;
        Context context;
        TextView nombreproducto,maximos,minimos,total;
        ImageButton max_min;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nombreproducto = itemView.findViewById(R.id.txt_i_producto);
            maximos = itemView.findViewById(R.id.txt_i_max);
            minimos = itemView.findViewById(R.id.txt_i_minimo);
            total=itemView.findViewById(R.id.txt_i_total);
            max_min = itemView.findViewById(R.id.btn_i_cambio);

            max_min.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DataProducto.class);
                    intent.putExtra("id_producto",id_producto);
                    Log.d("adaptador",id_producto);
                    context.startActivity(intent);
                }
            });
        }
        public void asignardatos(Productos p)
        {
            nombreproducto.setText(p.getProducto());
            maximos.setText(""+p.getMaximo());
            minimos.setText((""+p.getMinimo()));
            total.setText(""+p.getTotal());
            id_producto = p.getId_producto();
            Log.d("adapter dentro",p.getId_producto());
        }
    }
}
