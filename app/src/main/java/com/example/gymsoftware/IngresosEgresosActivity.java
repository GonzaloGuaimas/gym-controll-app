package com.example.gymsoftware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymsoftware.BaseDatos.Controller;
import com.example.gymsoftware.Objetos.IngresoEgreso;
import com.example.gymsoftware.Objetos.Usuario;
import com.example.gymsoftware.Utils.AdapterIngresoEgreso;
import com.example.gymsoftware.Utils.AdapterUsuarios;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class IngresosEgresosActivity extends AppCompatActivity {
    LinearLayout linearMain;
    CardView cardGuardar,cardEgresoIngresoSelec,cardFormaPago,cardFecha,cardFechaHasta,cardMes,cardAño,cardTodos;
    TextView textSelector,textImporte,textFormaPago,textComentario,
            textEfectivo,textDebito,textCredito,textTransferencia,textGastos,textTotal,textMes,textAño,textTipo;
    static TextView textFecha,textFechaHasta;
    RecyclerView recyclerIngresosEgresos;
    SearchView searchView;
    AdapterIngresoEgreso adapterIngresoEgreso;
    private Controller controller;
    private List<IngresoEgreso> listado;
    private List<Usuario> usuarios;
    static String fecha = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //region widgets
        setContentView(R.layout.activity_ingresos_egresos);
        cardGuardar = findViewById(R.id.cardGuardar);
        cardEgresoIngresoSelec = findViewById(R.id.cardEgresoIngresoSelec);
        cardFormaPago = findViewById(R.id.cardFormaPago);
        cardFecha = findViewById(R.id.cardFecha);
        cardFechaHasta = findViewById(R.id.cardFechaHasta);
        cardMes = findViewById(R.id.cardMes);
        cardAño = findViewById(R.id.cardAño);
        cardTodos = findViewById(R.id.cardTodos);
        textSelector = findViewById(R.id.textSelector);
        textImporte = findViewById(R.id.textImporte);
        textFormaPago = findViewById(R.id.textFormaPago);
        textComentario = findViewById(R.id.textComentario);
        textFecha = findViewById(R.id.textFecha);
        textFechaHasta = findViewById(R.id.textFechaHasta);
        textMes = findViewById(R.id.textMes);
        textAño = findViewById(R.id.textAño);
        textTipo = findViewById(R.id.textTipo);
        recyclerIngresosEgresos = findViewById(R.id.recyclerIngresosEgresos);
        textEfectivo = findViewById(R.id.textEfectivo);
        textDebito = findViewById(R.id.textDebito);
        textCredito = findViewById(R.id.textCredito);
        textTransferencia = findViewById(R.id.textTransferencia);
        textGastos = findViewById(R.id.textGastos);
        textTotal = findViewById(R.id.textTotal);
        linearMain = findViewById(R.id.linearMain);
        searchView = findViewById(R.id.searchView);
        //endregion

        controller = new Controller(IngresosEgresosActivity.this);
        textFecha.setText(MainActivity.formatoddMMyyyy.format(System.currentTimeMillis()));
        textFechaHasta.setText(MainActivity.formatoddMMyyyy.format(System.currentTimeMillis()));
        try {
            cargar();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        adapterIngresoEgreso.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                IngresoEgreso ingresoEgreso = listado.get(recyclerIngresosEgresos.getChildAdapterPosition(view));
                eliminar(ingresoEgreso);
                return false;
            }
        });
        cardGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textImporte.getText().toString().equals("")){
                    IngresoEgreso ingresoEgreso = new IngresoEgreso(System.currentTimeMillis(),Integer.parseInt(textImporte.getText().toString()),textFormaPago.getText().toString(),textComentario.getText().toString(),"","");
                    if (!textFormaPago.getText().toString().equals("Selec Forma")){
                        if (textSelector.getText().equals("Ingreso")){
                            controller.nuevoIngreso(ingresoEgreso);
                            //recargar();
                            try {
                                filtrarFecha();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), textSelector.getText().toString()+" guardado", Toast.LENGTH_SHORT).show();
                            limpiarCampos();
                        }else if (textSelector.getText().equals("Egreso")){
                            ingresoEgreso.setImporte(ingresoEgreso.getImporte()*-1);
                            controller.nuevoIngreso(ingresoEgreso);
                            //recargar();
                            try {
                                filtrarFecha();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            cartel("agregado");
                            limpiarCampos();
                        }else{
                            Toast.makeText(getApplicationContext(), "Seleccione Tipo: Egreso/Ingreso", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Seleccione Forma Pago", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Ingrese Importe", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cardEgresoIngresoSelec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IngresosEgresosActivity.this);
                final String[] tipo = new String[]{"Ingreso","Egreso"};
                builder.setTitle("Seleccioná Tipo").setItems(tipo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textSelector.setText(tipo[which]);
                    }
                });
                builder.show();
            }
        });
        cardFormaPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IngresosEgresosActivity.this);
                final String[] tipo = new String[]{"EFECTIVO","DEBITO","CREDITO","TRANSFERENCIA"};
                builder.setTitle("Seleccioná Tipo").setItems(tipo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textFormaPago.setText(tipo[which]);
                    }
                });
                builder.show();
            }
        });
        cardFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardFecha.startAnimation(MainActivity.scaleUp);
                fecha = "ini";
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        cardFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardFechaHasta.startAnimation(MainActivity.scaleUp);
                fecha = "hast";
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        cardMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IngresosEgresosActivity.this);
                final String[] tipo = new String[]{"mes","01","02","03","04","05","06","07","08","09","10","11","12"};
                builder.setTitle("Seleccionar Mes").setItems(tipo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textMes.setText(tipo[which]);
                        listado.clear();
                        for (IngresoEgreso ingresoEgreso:controller.obtenerIngresoEgreso()) {
                            if (MainActivity.formatoMM.format(new Date(ingresoEgreso.getId())).equals(tipo[which])){
                                listado.add(ingresoEgreso);
                            }
                        }
                        adapterIngresoEgreso.notifyDataSetChanged();
                        calcularDatos();
                    }
                });
                builder.show();
            }
        });
        cardAño.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(IngresosEgresosActivity.this);
                final String[] tipo = new String[]{"año","2021","2022","2023","2024","2025"};
                builder.setTitle("Seleccionar año").setItems(tipo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textAño.setText(tipo[which]);
                        listado.clear();
                        for (IngresoEgreso ingresoEgreso:controller.obtenerIngresoEgreso()) {
                            if (MainActivity.formatoyyyy.format(new Date(ingresoEgreso.getId())).equals(textAño.getText().toString())){
                                listado.add(ingresoEgreso);
                            }
                        }
                        adapterIngresoEgreso.notifyDataSetChanged();
                        calcularDatos();
                    }
                });
                builder.show();
            }
        });
        cardTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recargar();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String texto) {
                adapterIngresoEgreso.getFilter().filter(texto);
                return false;
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
            if (fecha.equals("ini")){
                if ((month+1)>9){
                    textFecha.setText(day+"/"+(month+1)+"/"+year);
                }else{
                    textFecha.setText(day+"/"+"0"+(month+1)+"/"+year);
                }
            }else{
                if ((month+1)>9){
                    textFechaHasta.setText(day+"/"+(month+1)+"/"+year);
                }else{
                    textFechaHasta.setText(day+"/"+"0"+(month+1)+"/"+year);
                }
            }

        }
    }
    //-----------------------------------------------------------------------------------------------


    private void eliminar(IngresoEgreso ingresoEgreso){
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(IngresosEgresosActivity.this);
        builder.setMessage("Desea eliminar el Ingreso/Egreso de fecha "+ MainActivity.formatoddMMyyyy.format(ingresoEgreso.getId())+"?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ingresoEgreso.getDNI().equals("")){
                    controller.eliminarIngreso(ingresoEgreso);
                    Toast.makeText(getApplicationContext(), "Ingreso/Egreso Eliminado", Toast.LENGTH_SHORT).show();
                    recargar();
                }else{
                    for (Usuario usuario:usuarios) {
                        if (usuario.getDNI().equals(ingresoEgreso.getDNI())){
                            usuario.setSaldo(usuario.getSaldo()+ingresoEgreso.getImporte());
                            if(usuario.getSaldo()+ingresoEgreso.getImporte()<=0){
                                usuario.setEstado("OK");
                            }else{
                                usuario.setEstado("DEBIENDO");
                            }
                            controller.guardarUsuario(usuario);
                            controller.eliminarIngreso(ingresoEgreso);
                            cartel("");
                            recargar();
                            break;
                        }
                    }
                }
            }
        });
        builder.create().show();
    }
    private void recargar(){
        listado.clear();
        for (IngresoEgreso ingresoEgreso:controller.obtenerIngresoEgreso()) {
            listado.add(ingresoEgreso);
        }
        adapterIngresoEgreso.notifyDataSetChanged();
        calcularDatos();
    }
    private void calcularDatos(){
        int efectivo = 0;
        int debito = 0;
        int credito = 0;
        int transferencia = 0;
        int gasto = 0;
        int total = 0;
        for (IngresoEgreso ingEg:listado) {
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
        textEfectivo.setText("$"+(efectivo));
        textDebito.setText("$"+(debito));
        textCredito.setText("$"+(credito));
        textTransferencia.setText("$"+(transferencia));
        textGastos.setText("-$"+(gasto));
        textTotal.setText("$"+(total+gasto));
    }

    //-----------------
    //
    // ------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------

    private void cargar() throws ParseException {
        listado = new ArrayList<>();
        usuarios = controller.obtenerUsuario();
        for (IngresoEgreso ingresoEgreso:controller.obtenerIngresoEgreso()) {
            listado.add(ingresoEgreso);
        }
        adapterIngresoEgreso = new AdapterIngresoEgreso(listado,getApplicationContext(),"");
        recyclerIngresosEgresos.setAdapter(adapterIngresoEgreso);
        recyclerIngresosEgresos.setLayoutManager(new LinearLayoutManager(this));

        filtrarFecha();
    }

    private void filtrarFecha() throws ParseException {
        Date fechaInicial = new Date((MainActivity.formatoddMMyyyy.parse(textFecha.getText().toString())).getTime());
        Date fechaFin = new Date((MainActivity.formatoddMMyyyy.parse(textFechaHasta.getText().toString())).getTime());
        Calendar fechaIni = Calendar.getInstance();
        Calendar fechaAux = Calendar.getInstance();
        Calendar fechaFi = Calendar.getInstance();
        fechaIni.setTime(fechaInicial);
        fechaFi.setTime(fechaFin);
        fechaFi.add(Calendar.DAY_OF_MONTH,1);

        listado.clear();
        for (IngresoEgreso ingEg:controller.obtenerIngresoEgreso()) {
            fechaAux.setTime(fechaInicial);
            while (!fechaAux.equals(fechaFi)){
                if (fechaAux.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(MainActivity.formatodd.format(ingEg.getId())) &&
                        fechaAux.get(Calendar.MONTH)+1 == Integer.parseInt(MainActivity.formatoMM.format(ingEg.getId())) &&
                        fechaAux.get(Calendar.YEAR) == Integer.parseInt(MainActivity.formatoyyyy.format(ingEg.getId())))   {
                    listado.add(ingEg);
                }
                fechaAux.add(Calendar.DAY_OF_MONTH,1);
            }
        }
        adapterIngresoEgreso.notifyDataSetChanged();
        calcularDatos();
    }
    private void limpiarCampos(){
        textImporte.setText("");
        textComentario.setText("");
    }


    public void cartel(String tipo){
        AlertDialog.Builder builder = new AlertDialog.Builder(IngresosEgresosActivity.this);
        View viewInflated = LayoutInflater.from(IngresosEgresosActivity.this).inflate(R.layout.cartel, (ViewGroup) findViewById(android.R.id.content), false);
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
}