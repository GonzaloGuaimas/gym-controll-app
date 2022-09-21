package com.example.gymsoftware.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymsoftware.Objetos.Actividad;
import com.example.gymsoftware.Objetos.Usuario;
import com.example.gymsoftware.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterActividad extends RecyclerView.Adapter<AdapterActividad.MyViewHolder> implements  View.OnClickListener,View.OnLongClickListener{
    private View.OnClickListener listener;
    private View.OnLongClickListener listener2;
    private List<Actividad> listado;
    Context context;

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }
    @Override
    public boolean onLongClick(View view) {
        if(listener2!=null){
            listener2.onLongClick(view);
        }
        return true;
    }
    public  void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    public  void setOnLongClickListener(View.OnLongClickListener listener2){
        this.listener2=listener2;
    }
    public AdapterActividad(List<Actividad> listado, Context context) {
        this.listado = listado;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterActividad.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_actividad,parent,false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new AdapterActividad.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterActividad.MyViewHolder holder, int position) {
        holder.textNombre.setText(listado.get(position).getNombre());
        holder.textPrecio.setText(listado.get(position).getPrecio().toString());
    }

    @Override
    public int getItemCount() {
        return listado.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre,textPrecio;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombre = itemView.findViewById(R.id.textNombre);
            textPrecio = itemView.findViewById(R.id.textPrecio);
        }
    }
}