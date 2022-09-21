package com.example.gymsoftware.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gymsoftware.Objetos.Usuario;
import com.example.gymsoftware.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class AdapterUsuarios extends RecyclerView.Adapter<AdapterUsuarios.MyViewHolder> implements  View.OnClickListener,View.OnLongClickListener, Filterable {
    private View.OnClickListener listener;
    private View.OnLongClickListener listener2;
    List<Usuario> listado;
    List<Usuario> listadoAll;

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
    public AdapterUsuarios(List<Usuario> listado, Context context) {
        this.listado = listado;
        this.listadoAll = new ArrayList<>();
        this.listadoAll.addAll(listado);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_usuario,parent,false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*
        if (listado.get(position).getPathfoto()!=null){
            if (!listado.get(position).getPathfoto().equals("")){
                holder.imageFoto.setVisibility(View.INVISIBLE);
            }
        }
        Glide.with(context).load(listado.get(position).getPathfoto()).into(holder.imageFoto);*/
       try{
           Bitmap resizedBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(listado.get(position).getPathfoto()), 100 /*Ancho*/, 100 /*Alto*/, false /* filter*/);
           holder.imageFoto.setImageBitmap(resizedBitmap);
       }catch (Exception e){
           holder.imageFoto.setImageBitmap(BitmapFactory.decodeFile(listado.get(position).getPathfoto()));
       }


        //Bitmap image = ((BitmapDrawable) this.image.getDrawable()).getBitmap();

        //Bitmap imageScaled = Bitmap.createScaledBitmap(image, newWidth, newHeight, false);

        //holder.imageFoto.setImageBitmap(BitmapFactory.decodeFile(listado.get(position).getPathfoto()));

        holder.textNombre.setText(listado.get(position).getApellido()+", "+listado.get(position).getNombre());
        holder.textDNI.setText(listado.get(position).getDNI());
        holder.textCondicion.setText(listado.get(position).getCondicion());
        holder.textFechaInscr.setText("Inscripción: "+listado.get(position).getFechaInscripcionDD());
        holder.textFechaVencimiento.setText("Vencimiento: "+listado.get(position).getFechaVencimientoDD());
        holder.textTelefono.setText("Tel: "+listado.get(position).getTelefono());
        holder.textEstado.setText(listado.get(position).getEstado());
        holder.textSaldo.setText("Saldo: $"+listado.get(position).getSaldo());


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
            List<Usuario> listadoFiltrado = new ArrayList<>();
            if (charSequence==null || charSequence.length()==0){
                listadoFiltrado.addAll(listadoAll);
            }else {
                for (Usuario usuario:listadoAll){
                    if (usuario.getDNI().toString().contains(charSequence.toString()) || usuario.getNombre().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || usuario.getApellido().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        listadoFiltrado.add(usuario);
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
            listado.addAll((Collection<? extends Usuario>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre,textDNI,textCondicion,textFechaInscr,textFechaVencimiento,textTelefono,textEstado,textSaldo;
        ImageView imageFoto;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textNombre = itemView.findViewById(R.id.textNombre);
            textDNI = itemView.findViewById(R.id.textDNI);
            textCondicion = itemView.findViewById(R.id.textCondicion);
            textFechaInscr = itemView.findViewById(R.id.textFechaInscr);
            textFechaVencimiento = itemView.findViewById(R.id.textFechaVencimiento);
            textTelefono = itemView.findViewById(R.id.textTelefono);
            textEstado = itemView.findViewById(R.id.textEstado);
            textSaldo = itemView.findViewById(R.id.textSaldo);

            imageFoto = itemView.findViewById(R.id.imageFoto);

            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}