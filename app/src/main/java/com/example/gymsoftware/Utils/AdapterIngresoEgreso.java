package com.example.gymsoftware.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymsoftware.MainActivity;
import com.example.gymsoftware.Objetos.Actividad;
import com.example.gymsoftware.Objetos.IngresoEgreso;
import com.example.gymsoftware.Objetos.Usuario;
import com.example.gymsoftware.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class AdapterIngresoEgreso extends RecyclerView.Adapter<AdapterIngresoEgreso.MyViewHolder> implements  View.OnClickListener,View.OnLongClickListener, Filterable {
    private View.OnClickListener listener;
    private View.OnLongClickListener listener2;
    private List<IngresoEgreso> listado;
    private List<IngresoEgreso> listadoAll;
    Context context;
    String tipo;

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
    public AdapterIngresoEgreso(List<IngresoEgreso> listado, Context context,String tipo) {
        this.listado = listado;
        this.listadoAll = new ArrayList<>();
        this.listadoAll.addAll(listado);
        this.context = context;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public AdapterIngresoEgreso.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_ing_egr_cob,parent,false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new AdapterIngresoEgreso.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIngresoEgreso.MyViewHolder holder, int position) {
        holder.textFecha.setText(MainActivity.formatoddMMyyyy.format(new Date(listado.get(position).getId() )));
        holder.textDia.setText(MainActivity.formatoEEEE.format(new Date(listado.get(position).getId() )));
        holder.textHora.setText(MainActivity.formatoHHmm.format(new Date(listado.get(position).getId() )));
        holder.textImporte.setText("$"+listado.get(position).getImporte());
        holder.textFormaPago.setText(listado.get(position).getFormaPago());
        holder.textComentario.setText(listado.get(position).getComentario());
        holder.textDNI.setText(listado.get(position).getDNI());
        holder.textNombre.setText(listado.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return listado.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<IngresoEgreso> listadoFiltrado = new ArrayList<>();
            if (charSequence==null || charSequence.length()==0){
                listadoFiltrado.addAll(listadoAll);
            }else {
                for (IngresoEgreso ingresoEgreso:listadoAll){
                    if (ingresoEgreso.getDNI().toString().contains(charSequence.toString()) || ingresoEgreso.getNombre().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        listadoFiltrado.add(ingresoEgreso);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = listadoFiltrado;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listado.clear();
            listado.addAll((Collection<? extends IngresoEgreso>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textFecha,textDia,textHora,textImporte,textFormaPago,textComentario,textDNI,textNombre;
        CardView cardView;
        LinearLayout linearDatos;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textFecha = itemView.findViewById(R.id.textFecha);
            textDia = itemView.findViewById(R.id.textDia);
            textHora = itemView.findViewById(R.id.textHora);
            textImporte = itemView.findViewById(R.id.textImporte);
            textFormaPago = itemView.findViewById(R.id.textFormaPago);
            textComentario = itemView.findViewById(R.id.textComentario);
            textDNI = itemView.findViewById(R.id.textDNI);
            textNombre = itemView.findViewById(R.id.textNombre);
            cardView = itemView.findViewById(R.id.cardView);
            linearDatos = itemView.findViewById(R.id.linearDatos);
        }
    }
}
