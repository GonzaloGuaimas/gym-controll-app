package com.example.gymsoftware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gymsoftware.BaseDatos.Controller;
import com.example.gymsoftware.Objetos.Arqueo;
import com.example.gymsoftware.Objetos.IngresoEgreso;
import com.example.gymsoftware.Objetos.Usuario;
import com.example.gymsoftware.Utils.AdapterActividad;
import com.example.gymsoftware.Utils.AdapterIngresoEgreso;
import com.example.gymsoftware.Utils.AdapterUsuarios;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CobrarActividad extends AppCompatActivity {
    LinearLayout linearMain;
    TextView textNombre;
    TextView textDNI;
    TextView textSaldo;
    TextView textFormaPago;
    static TextView textFecha;
    static TextView textHora;
    EditText textDNIBusqueda,textImporte,textComentario;
    CardView cardCobrar,cardFormaPago,cardTodos,cardFecha,cardHora;
    RecyclerView recyclerCobros;
    SearchView searchView;
    AdapterIngresoEgreso adapterIngresoEgreso;
    private Usuario usuarioSeleccionado;
    private Controller controller;
    private List<IngresoEgreso> listadoCobros;
    private List<Usuario> usuarios;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobrar_actividad);

        //region widgets
        textFecha = findViewById(R.id.textFecha);
        textHora = findViewById(R.id.textHora);
        cardFecha = findViewById(R.id.cardFecha);
        cardHora = findViewById(R.id.cardHora);
        textNombre = findViewById(R.id.textNombre);
        textDNI = findViewById(R.id.textDNI);
        textSaldo = findViewById(R.id.textSaldo);
        textDNIBusqueda = findViewById(R.id.textDNIBusqueda);
        textImporte = findViewById(R.id.textImporte);
        textFormaPago = findViewById(R.id.textFormaPago);
        textComentario = findViewById(R.id.textComentario);
        cardCobrar = findViewById(R.id.cardCobrar);
        recyclerCobros = findViewById(R.id.recyclerCobros);
        cardFormaPago = findViewById(R.id.cardFormaPago);
        linearMain = findViewById(R.id.linearMain);
        searchView = findViewById(R.id.searchView);
        cardTodos = findViewById(R.id.cardTodos);
        //endregion
        controller = new Controller(CobrarActividad.this);

        bundle =  getIntent().getExtras();
        if(bundle!=null){
            cargarUsuario();
        }
        cargar();

        cardCobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textNombre.getText().toString().equals("Apellido ,Nombre")){
                    if (!TextUtils.isEmpty(textImporte.getText())){
                        if (!textFormaPago.getText().toString().equals("Selec Forma")){
                            //IngresoEgreso ingresoEgreso = new IngresoEgreso(System.currentTimeMillis(),Integer.parseInt(textImporte.getText().toString()),textFormaPago.getText().toString(),textComentario.getText().toString(),textDNI.getText().toString().replace("DNI: ",""),textNombre.getText().toString());
                            IngresoEgreso ingresoEgreso = null;
                            try {
                                ingresoEgreso = new IngresoEgreso(MainActivity.formatoddMMyyyyHHmmss.parse(textFecha.getText().toString()+" "+textHora.getText().toString()+":"+Calendar.getInstance().get(Calendar.SECOND)).getTime(),Integer.parseInt(textImporte.getText().toString()),textFormaPago.getText().toString(),textComentario.getText().toString(),textDNI.getText().toString().replace("DNI: ",""),textNombre.getText().toString());
                            } catch (ParseException e) {
                                Toast.makeText(getApplicationContext(), "Error en fecha / Hora", Toast.LENGTH_SHORT).show();
                            }

                            for (Usuario usuario:usuarios) {
                                if (usuario.getDNI().equals(textDNI.getText().toString().replace("DNI: ",""))){
                                    if (usuario.getSaldo()-Integer.parseInt(textImporte.getText().toString())<1){
                                        usuario.setEstado("OK");
                                    }
                                    usuario.setSaldo(usuario.getSaldo()-Integer.parseInt(textImporte.getText().toString()));
                                    controller.guardarUsuario(usuario);
                                    break;
                                }
                            }
                            controller.nuevoIngreso(ingresoEgreso);
                            recargar();
                            cartel("agregado");

                        }else{
                            Toast.makeText(getApplicationContext(), "Seleccionar Forma de Pago", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Agregue Importe", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Ingrese Usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cardFormaPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CobrarActividad.this);
                final String[] tipo = new String[]{"EFECTIVO","DEBITO","CREDITO","TRANSFERENCIA"};
                builder.setTitle("Seleccioná Tipo").setItems(tipo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textFormaPago.setText(tipo[which]);
                    }
                });
                builder.show();
            }
        });
        textFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textFecha.startAnimation(MainActivity.scaleUp);
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        textHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textHora.startAnimation(MainActivity.scaleUp);
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
        textDNIBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (Usuario user:usuarios) {
                    if (user.getDNI().equals(textDNIBusqueda.getText().toString())){
                        usuarioSeleccionado = user;
                        cargarDatos();
                        break;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        adapterIngresoEgreso.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CobrarActividad.this);
                IngresoEgreso ingresoEgreso = listadoCobros.get(recyclerCobros.getChildAdapterPosition(view));
                builder.setMessage("Desea eliminar el Ingreso/Egreso de fecha "+ MainActivity.formatoddMMyyyy.format(listadoCobros.get(recyclerCobros.getChildAdapterPosition(view)).getId())+"?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
                });
                builder.create().show();
                return false;
            }
        });
        cardTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listadoCobros.clear();
                for (IngresoEgreso ingEg:controller.obtenerIngresoEgreso()) {
                    if (!ingEg.getDNI().equals(""))
                        listadoCobros.add(ingEg);
                }
                adapterIngresoEgreso.notifyDataSetChanged();
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
            if ((month+1)>9){
                textFecha.setText(day+"/"+(month+1)+"/"+year);
            }else{
                textFecha.setText(day+"/"+"0"+(month+1)+"/"+year);
            }
        }
    }
    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute,DateFormat.is24HourFormat(getActivity()));
        }
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            textHora.setText(hourOfDay+":"+minute);
        }
    }

    private void limpiarCampos(){
        textDNI.setText("DNI: 00000000");
        textNombre.setText("Apellido ,Nombre");
        textDNIBusqueda.setText("");
        textSaldo.setText("$0");
        textImporte.setText("");
        textComentario.setText("");
        textFormaPago.setText("Selec Forma");
    }
    //-----------------------------------------------------------------------------------------------
    private void cargarUsuario(){
        for (Usuario user:controller.obtenerUsuario()) {
            if (user.getDNI().equals(bundle.getString("usuario"))){
                textNombre.setText(user.getApellido().toUpperCase()+" ,"+user.getNombre());
                textDNI.setText(user.getDNI());
                textSaldo.setText(String.valueOf(user.getSaldo()));
                break;
            }
        }
    }

    private void cargarDatos(){
        try{
            textNombre.setText(usuarioSeleccionado.getApellido().toUpperCase()+" ,"+usuarioSeleccionado.getNombre());
            textDNI.setText(usuarioSeleccionado.getDNI());
            textSaldo.setText(String.valueOf(usuarioSeleccionado.getSaldo()));
        }catch (Exception es){
        }

    }

    private void recargar(){
        listadoCobros.clear();
        for (IngresoEgreso ingEg:controller.obtenerIngresoEgreso()) {
            if (!ingEg.getDNI().equals(""))
                listadoCobros.add(ingEg);
        }
        adapterIngresoEgreso.notifyDataSetChanged();
    }
    private void cargar(){
        textFecha.setText(MainActivity.formatoddMMyyyy.format(System.currentTimeMillis()));
        textHora.setText(MainActivity.formatoHHmm.format(System.currentTimeMillis()));

        usuarios = new ArrayList<>();
        for (Usuario user:controller.obtenerUsuario()) {
            usuarios.add(user);
        }

        listadoCobros = new ArrayList<>();
        for (IngresoEgreso ingEg:controller.obtenerIngresoEgreso()) {
            if (!ingEg.getDNI().equals(""))
                listadoCobros.add(ingEg);
        }
        adapterIngresoEgreso = new AdapterIngresoEgreso(listadoCobros,getApplicationContext(),"");
        recyclerCobros.setAdapter(adapterIngresoEgreso);
        recyclerCobros.setLayoutManager(new LinearLayoutManager(this));

    }

    public void cartel(String tipo){
        AlertDialog.Builder builder = new AlertDialog.Builder(CobrarActividad.this);
        View viewInflated = LayoutInflater.from(CobrarActividad.this).inflate(R.layout.cartel, (ViewGroup) findViewById(android.R.id.content), false);
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
                limpiarCampos();
                dialogInterface.cancel();

            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

}