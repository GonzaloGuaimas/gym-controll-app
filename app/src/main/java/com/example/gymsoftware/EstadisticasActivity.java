package com.example.gymsoftware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
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

public class EstadisticasActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    LinearLayout linearMain;
    CardView cardFecha,cardFechaHasta;
    TextView textEfectivo,textDebito,textCredito,textTransferencia,textGastos,textTotal,textMes,textAño,textTipo,
            textNuevos,textRenovacion,textReinscripcion;
    static TextView textFecha,textFechaHasta;
    SearchView searchView;
    RecyclerView recyclerIngresosEgresos,recyclerUsuarios;
    AdapterIngresoEgreso adapterIngresoEgreso;
    AdapterUsuarios adapterUsuarios;
    private Controller controller;
    private List<IngresoEgreso> ingresoEgresos;
    private List<Usuario> usuarios;
    static String fecha = "";

    Spinner spinnerTurno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        //region widgets
        cardFecha = findViewById(R.id.cardFecha);
        cardFechaHasta = findViewById(R.id.cardFechaHasta);
        textEfectivo = findViewById(R.id.textEfectivo);
        textDebito = findViewById(R.id.textDebito);
        textCredito = findViewById(R.id.textCredito);
        textTransferencia = findViewById(R.id.textTransferencia);
        textGastos = findViewById(R.id.textGastos);
        textTotal = findViewById(R.id.textTotal);
        textMes = findViewById(R.id.textMes);
        textAño = findViewById(R.id.textAño);
        textTipo = findViewById(R.id.textTipo);
        textFecha = findViewById(R.id.textFecha);
        textFechaHasta = findViewById(R.id.textFechaHasta);
        searchView = findViewById(R.id.searchView);
        recyclerIngresosEgresos = findViewById(R.id.recyclerIngresosEgresos);
        recyclerUsuarios = findViewById(R.id.recyclerUsuarios);
        textNuevos = findViewById(R.id.textNuevos);
        textRenovacion = findViewById(R.id.textRenovacion);
        textReinscripcion = findViewById(R.id.textReinscripcion);
        linearMain = findViewById(R.id.linearMain);
        spinnerTurno = findViewById(R.id.spinnerTurno);
        //endregion

        controller = new Controller(EstadisticasActivity.this);
        textFecha.setText(MainActivity.formatoddMMyyyy.format(System.currentTimeMillis()));
        textFechaHasta.setText(MainActivity.formatoddMMyyyy.format(System.currentTimeMillis()));
        cargar();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.turnos, android.R.layout.simple_spinner_item);
        spinnerTurno.setAdapter(adapter);
        spinnerTurno.setOnItemSelectedListener(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String texto) {
                adapterUsuarios.getFilter().filter(texto);
                return false;
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            filtrarFecha();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    private void calcularDatos(){
        int efectivo = 0;
        int debito = 0;
        int credito = 0;
        int transferencia = 0;
        int gasto = 0;
        int total = 0;

        for (IngresoEgreso ingEg:ingresoEgresos) {
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

        //-----------------------------------------------------------------
        //-----------------------------------------------------------------
        //-----------------------------------------------------------------


        int nuevo = 0;
        int renovacion = 0;
        int reinscripcion = 0;
        for (Usuario usuario:usuarios) {
            switch (usuario.getCondicion().split(" - ")[0]){
                case "NUEVO":
                    nuevo++;
                    break;
                case "RENOVACION":
                    renovacion++;
                    break;
                case "REINSCRIPCION":
                    reinscripcion++;
                    break;
            }
        }
        textNuevos.setText(String.valueOf(nuevo));
        textRenovacion.setText(String.valueOf(renovacion));
        textReinscripcion.setText(String.valueOf(reinscripcion));

    }
    private void cargar(){
        ingresoEgresos = new ArrayList<>();
        usuarios = new ArrayList<>();

        for (IngresoEgreso ingresoEgreso:controller.obtenerIngresoEgreso()) {
            ingresoEgresos.add(ingresoEgreso);
        }
        for (Usuario usuario:controller.obtenerUsuario()) {
            usuarios.add(usuario);
        }

        adapterIngresoEgreso = new AdapterIngresoEgreso(ingresoEgresos,getApplicationContext(),"");
        recyclerIngresosEgresos.setAdapter(adapterIngresoEgreso);
        recyclerIngresosEgresos.setLayoutManager(new LinearLayoutManager(this));

        adapterUsuarios = new AdapterUsuarios(usuarios,getApplicationContext());
        recyclerUsuarios.setAdapter(adapterUsuarios);
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(this));

        calcularDatos();
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

        ingresoEgresos.clear();

        System.out.println(spinnerTurno.getSelectedItem().toString());
        System.out.println("XD");

        for (IngresoEgreso ingEg:controller.obtenerIngresoEgreso()) {
            fechaAux.setTime(fechaInicial);
            while (!fechaAux.equals(fechaFi)){
                if (fechaAux.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(MainActivity.formatodd.format(ingEg.getId())) &&
                        fechaAux.get(Calendar.MONTH)+1 == Integer.parseInt(MainActivity.formatoMM.format(ingEg.getId())) &&
                        fechaAux.get(Calendar.YEAR) == Integer.parseInt(MainActivity.formatoyyyy.format(ingEg.getId())))   {

                    if (spinnerTurno.getSelectedItem().toString().equals("Ambos")){
                        ingresoEgresos.add(ingEg);
                    }else if (spinnerTurno.getSelectedItem().toString().equals("TM") && Integer.parseInt(ingEg.getHora())<14 ){
                        ingresoEgresos.add(ingEg);
                    }else if ( spinnerTurno.getSelectedItem().toString().equals("TT") && Integer.parseInt(ingEg.getHora())>=14 ){
                        ingresoEgresos.add(ingEg);
                    }


                }
                fechaAux.add(Calendar.DAY_OF_MONTH,1);
            }
        }
        adapterIngresoEgreso.notifyDataSetChanged();

        usuarios.clear();
        for (Usuario usuario:controller.obtenerUsuario()) {
            fechaAux.setTime(fechaInicial);
            while (!fechaAux.equals(fechaFi)){
                if (fechaAux.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(MainActivity.formatodd.format(usuario.getFechaVencimiento())) &&
                        fechaAux.get(Calendar.MONTH)+1 == (Integer.parseInt(MainActivity.formatoMM.format(usuario.getFechaVencimiento())) -1)&&
                        fechaAux.get(Calendar.YEAR) == Integer.parseInt(MainActivity.formatoyyyy.format(usuario.getFechaVencimiento())))   {
                    usuarios.add(usuario);
                }
                fechaAux.add(Calendar.DAY_OF_MONTH,1);
            }
        }
        adapterUsuarios.notifyDataSetChanged();

        calcularDatos();

    }
}

