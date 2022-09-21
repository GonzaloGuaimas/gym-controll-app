package com.example.gymsoftware.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymsoftware.MainActivity;
import com.example.gymsoftware.Objetos.Actividad;
import com.example.gymsoftware.Objetos.Arqueo;
import com.example.gymsoftware.R;

import java.util.List;

public class AdapterArqueo extends RecyclerView.Adapter<AdapterArqueo.MyViewHolder> implements  View.OnClickListener,View.OnLongClickListener{
    private View.OnClickListener listener;
    private View.OnLongClickListener listener2;
    private List<Arqueo> listado;
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
    public AdapterArqueo(List<Arqueo> listado, Context context) {
        this.listado = listado;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterArqueo.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_arqueo,parent,false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new AdapterArqueo.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterArqueo.MyViewHolder holder, int position) {
        holder.textFecha.setText(MainActivity.formatoddMMyyyy.format(listado.get(position).getId()));
        holder.textDia.setText(MainActivity.formatoEEEE.format(listado.get(position).getId()));
        holder.textHora.setText(MainActivity.formatoHHmm.format(listado.get(position).getId()));
        holder.textEfectivo.setText("E$"+listado.get(position).getEfectivo_sistema());
        holder.textEfectivoReal.setText("ER$"+listado.get(position).getEfectivo_real());
        holder.textDebito.setText("D$"+listado.get(position).getDebito());
        holder.textCredito.setText("C$"+listado.get(position).getCredito());
        holder.textTransferencia.setText("T$"+listado.get(position).getTransferencia());
        holder.textGastos.setText("G$"+listado.get(position).getGastos());
        holder.textTotal.setText("T$"+listado.get(position).getTotal());
        holder.textComentario.setText(listado.get(position).getComentario());
    }

    @Override
    public int getItemCount() {
        return listado.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textFecha,textDia,textHora,textEfectivo,textEfectivoReal,textDebito,textCredito,textTransferencia,textGastos,textTotal,textComentario;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textFecha = itemView.findViewById(R.id.textFecha);
            textDia = itemView.findViewById(R.id.textDia);
            textHora = itemView.findViewById(R.id.textHora);
            textEfectivo = itemView.findViewById(R.id.textEfectivo);
            textEfectivoReal = itemView.findViewById(R.id.textEfectivoReal);
            textDebito = itemView.findViewById(R.id.textDebito);
            textCredito = itemView.findViewById(R.id.textCredito);
            textTransferencia = itemView.findViewById(R.id.textTransferencia);
            textGastos = itemView.findViewById(R.id.textGastos);
            textTotal = itemView.findViewById(R.id.textTotal);
            textComentario = itemView.findViewById(R.id.textComentario);
        }
    }
}