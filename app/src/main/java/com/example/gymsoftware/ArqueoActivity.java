package com.example.gymsoftware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymsoftware.BaseDatos.Controller;
import com.example.gymsoftware.Objetos.Actividad;
import com.example.gymsoftware.Objetos.Arqueo;
import com.example.gymsoftware.Objetos.IngresoEgreso;
import com.example.gymsoftware.Objetos.Usuario;
import com.example.gymsoftware.Utils.AdapterArqueo;
import com.example.gymsoftware.Utils.AdapterIngresoEgreso;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ArqueoActivity extends AppCompatActivity {

    LinearLayout linearMain;
    TextView textEfectivo;
    TextView textCredito;
    TextView textDebito;
    TextView textTransferencia;
    TextView textComentario;
    TextView textGastos;
    TextView textTotal;
    static TextView textFecha,textFechaDesde,textFechaHasta;
    CardView cardFecha;
    EditText textEfectivoReal;
    RecyclerView recyclerArqueos;
    AdapterArqueo adapterArqueo;
    CardView cardGuardarArqueo,cardTodos;
    private Controller controller;
    private Arqueo arqueoSeleccionada;
    private int efectivo,debito,credito,transferencia,gasto,total;
    private List<Arqueo> arqueos;
    static String fecha = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //region widgets
        setContentView(R.layout.activity_arqueo);
        textEfectivo = findViewById(R.id.textEfectivo);
        textEfectivoReal = findViewById(R.id.textEfectivoReal);
        textCredito = findViewById(R.id.textCredito);
        textDebito = findViewById(R.id.textDebito);
        textTransferencia = findViewById(R.id.textTransferencia);
        textComentario = findViewById(R.id.textComentario);
        recyclerArqueos = findViewById(R.id.recyclerArqueos);
        cardGuardarArqueo = findViewById(R.id.cardGuardarArqueo);
        textGastos = findViewById(R.id.textGastos);
        textTotal = findViewById(R.id.textTotal);
        cardFecha = findViewById(R.id.cardFecha);
        textFecha = findViewById(R.id.textFecha);
        linearMain = findViewById(R.id.linearMain);
        textFechaDesde = findViewById(R.id.textFechaDesde);
        textFechaHasta = findViewById(R.id.textFechaHasta);
        cardTodos = findViewById(R.id.cardTodos);
        //endregion

        controller = new Controller(ArqueoActivity.this);
        cargar();

        cardGuardarArqueo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ArqueoActivity.this);
                builder.setMessage("Está seguro de guardar el Arqueo? Se exportarán datos también.");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            guardarArqueo();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.create().show();


            }
        });
        adapterArqueo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                arqueoSeleccionada = arqueos.get(recyclerArqueos.getChildAdapterPosition(view));
                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ArqueoActivity.this);
                builder.setMessage("Desea eliminar arqueo de fecha "+ MainActivity.formatoddMMyyyy.format(arqueoSeleccionada.getId()));
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        controller.eliminarArqueo(arqueos.get(recyclerArqueos.getChildAdapterPosition(view)));
                        arqueos.clear();
                        for (Arqueo arqueo:controller.obtenerArqueo()) {
                            arqueos.add(arqueo);
                        }
                        adapterArqueo.notifyDataSetChanged();
                        cartel("");
                    }
                });
                builder.create().show();
                return false;
            }
        });
        cardFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardFecha.startAnimation(MainActivity.scaleUp);
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
                fecha = "fecha";
            }
        });
        textFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fecha = "desde";
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");

            }
        });
        textFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fecha = "hasta";
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");

            }
        });
        textFecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                cargarDatos();
            }
        });
        textFecha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    filtrarFecha();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        textFechaHasta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    filtrarFecha();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        cardTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardTodos.startAnimation(MainActivity.scaleUp);
                arqueos.clear();
                for (Arqueo arqueo:controller.obtenerArqueo()) {
                    arqueos.add(arqueo);
                }
                adapterArqueo.notifyDataSetChanged();
            }
        });

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation=newConfig.orientation;
        switch(orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                linearMain.setOrientation(LinearLayout.HORIZONTAL);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                linearMain.setOrientation(LinearLayout.VERTICAL);
                break;
        }
    }
    //-----------------------------------------------------------------------------------------------

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            switch (fecha){
                case "fecha":
                    if ((month+1)>9){
                        if (day>9){
                            textFecha.setText(day+"/"+(month+1)+"/"+year);
                        }else{
                            textFecha.setText("0"+day+"/"+(month+1)+"/"+year);
                        }

                    }else{
                        if (day>9){
                            textFecha.setText(day+"/"+"0"+(month+1)+"/"+year);
                        }else{
                            textFecha.setText("0"+day+"/"+"0"+(month+1)+"/"+year);
                        }

                    }
                    break;
                case "desde":
                    if ((month+1)>9){
                        textFechaDesde.setText(day+"/"+(month+1)+"/"+year);
                    }else{
                        textFechaDesde.setText(day+"/"+"0"+(month+1)+"/"+year);
                    }
                    break;
                case "hasta":
                    if ((month+1)>9){
                        textFechaHasta.setText(day+"/"+(month+1)+"/"+year);
                    }else{
                        textFechaHasta.setText(day+"/"+"0"+(month+1)+"/"+year);
                    }
                    break;
            }

        }
    }
    //-----------------------------------------------------------------------------------------------
    private void guardarArqueo() throws ParseException {
        int cant =0;
        if (!TextUtils.isEmpty(textEfectivoReal.getText())){
            cant = Integer.parseInt(textEfectivoReal.getText().toString());
        }
        Arqueo arqueo = new Arqueo((MainActivity.formatoddMMyyyy.parse(textFecha.getText().toString())).getTime(),efectivo,cant,debito,credito,transferencia,gasto,total+gasto,textComentario.getText().toString());
        controller.nuevoArqueo(arqueo);
        arqueos.clear();
        for (Arqueo arqu:controller.obtenerArqueo()) {
            arqueos.add(arqu);
        }
        adapterArqueo.notifyDataSetChanged();
        cartel("agregado");
        exportar();
        limpiarCampos();
    }
    private void limpiarCampos(){
        textEfectivoReal.setText("");
        textComentario.setText("");
    }


    //-----------------------------------------------------------------------------------------------
    private void cargar(){
        arqueos = new ArrayList<>();
        for (Arqueo arqueo:controller.obtenerArqueo()) {
            arqueos.add(arqueo);
        }
        adapterArqueo = new AdapterArqueo(arqueos,getApplicationContext());
        recyclerArqueos.setAdapter(adapterArqueo);
        recyclerArqueos.setLayoutManager(new LinearLayoutManager(this));
        textFecha.setText(MainActivity.formatoddMMyyyy.format(System.currentTimeMillis()));
        cargarDatos();
    }
    private void filtrarFecha() throws ParseException {
        Date fechaInicial = new Date((MainActivity.formatoddMMyyyy.parse(textFechaDesde.getText().toString())).getTime());
        Date fechaFin = new Date((MainActivity.formatoddMMyyyy.parse(textFechaHasta.getText().toString())).getTime());
        Calendar fechaIni = Calendar.getInstance();
        Calendar fechaAux = Calendar.getInstance();
        Calendar fechaFi = Calendar.getInstance();
        fechaIni.setTime(fechaInicial);
        fechaFi.setTime(fechaFin);
        fechaFi.add(Calendar.DAY_OF_MONTH,1);

        arqueos.clear();
        for (Arqueo arqueo:controller.obtenerArqueo()) {
            fechaAux.setTime(fechaInicial);
            while (!fechaAux.equals(fechaFi)){
                if (fechaAux.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(MainActivity.formatodd.format(arqueo.getId())) &&
                        fechaAux.get(Calendar.MONTH)+1 == Integer.parseInt(MainActivity.formatoMM.format(arqueo.getId())) &&
                        fechaAux.get(Calendar.YEAR) == Integer.parseInt(MainActivity.formatoyyyy.format(arqueo.getId())))   {
                    arqueos.add(arqueo);
                }
                fechaAux.add(Calendar.DAY_OF_MONTH,1);
            }
        }
        adapterArqueo.notifyDataSetChanged();
    }

    private void cargarDatos(){
        efectivo = 0;
        debito = 0;
        credito = 0;
        transferencia = 0;
        gasto = 0;
        total = 0;
        for (IngresoEgreso ingEg:controller.obtenerIngresoEgreso()) {
            if (textFecha.getText().toString().equals(MainActivity.formatoddMMyyyy.format(ingEg.getId()))){
                if (ingEg.getImporte()<0){
                    gasto +=ingEg.getImporte();
                }else{
                    switch (ingEg.getFormaPago()){
                        case "EFECTIVO":
                            efectivo+=ingEg.getImporte();
                            break;
                        case "DEBITO":
                            debito+=ingEg.getImporte();
                            break;
                        case "CREDITO":
                            credito+=ingEg.getImporte();
                            break;
                        case "TRANSFERENCIA":
                            transferencia+=ingEg.getImporte();
                            break;
                    }
                    total+= ingEg.getImporte();
                }

            }
        }
        textEfectivo.setText("$"+(efectivo));
        textDebito.setText("$"+(debito));
        textCredito.setText("$"+(credito));
        textTransferencia.setText("$"+(transferencia));
        textGastos.setText("-$"+(gasto));
        textTotal.setText("TOTAL: $"+(total+gasto));
    }


    public void cartel(String tipo){
        AlertDialog.Builder builder = new AlertDialog.Builder(ArqueoActivity.this);
        View viewInflated = LayoutInflater.from(ArqueoActivity.this).inflate(R.layout.cartel, (ViewGroup) findViewById(android.R.id.content), false);
        ImageView imagen = viewInflated.findViewById(R.id.imagen);
        if(tipo.equals("agregado")){
            imagen.setBackgroundResource(R.drawable.agregado);
        }else{
            imagen.setBackgroundResource(R.drawable.eliminado);
        }
        builder.setView(viewInflated);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


    //-------------------------------------------------------------------------------------------------------------
    private void exportar(){
        FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
        int count = 0;
        for (Actividad actividad:controller.obtenerActividad()) {
            basedatos.collection("BupActividades").document(actividad.getNombre()).set(actividad);
            count++;
        }
        Toast.makeText(getApplicationContext(), "Se exportaron "+String.valueOf(count)+" actividades", Toast.LENGTH_SHORT).show();
        count = 0;
        for (Usuario usuario:controller.obtenerUsuario()) {
            basedatos.collection("BupUsuarios").document(usuario.getDNI()).set(usuario);
            count++;
        }
        Toast.makeText(getApplicationContext(), "Se exportaron "+String.valueOf(count)+" usuarios", Toast.LENGTH_SHORT).show();
        count = 0;
        for (Arqueo arqueo:controller.obtenerArqueo()) {
            basedatos.collection("BupArqueo").document(String.valueOf(arqueo.getId())).set(arqueo);
            count++;
        }
        Toast.makeText(getApplicationContext(), "Se exportaron "+String.valueOf(count)+" arqueos", Toast.LENGTH_SHORT).show();
        count = 0;
        for (IngresoEgreso ingresoEgreso:controller.obtenerIngresoEgreso()) {
            basedatos.collection("BupIngresosEgresos").document(String.valueOf(ingresoEgreso.getId())).set(ingresoEgreso);
            count++;
        }
        Toast.makeText(getApplicationContext(), "DATOS EXPORTADOS", Toast.LENGTH_SHORT).show();
    }
}