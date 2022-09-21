package com.example.gymsoftware;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymsoftware.BaseDatos.Controller;
import com.example.gymsoftware.Objetos.Actividad;
import com.example.gymsoftware.Objetos.IngresoEgreso;
import com.example.gymsoftware.Objetos.Usuario;
import com.example.gymsoftware.Utils.AdapterActividad;
import com.example.gymsoftware.Utils.AdapterUsuarios;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UsuariosActivity extends AppCompatActivity {
    LinearLayout linearMain;
    RecyclerView recyclerUsuarios,recyclerActDisponibles,recyclerActUsuario;
    AdapterUsuarios adapterUsuarios;
    AdapterActividad adapterActividad,adapterActividadIng;
    List<Actividad> listadoActividadesIng;
    CardView cardGuardarUsuario,cardCondicion,cardEstado,cardPagarCuenta,cardImagenPersona,cardTodos,cardLimpiar,cardLimpiarSaldo;
    EditText textNombre,textApellido,textDNI,textDescuento,textTelefono;
    static TextView textFechaInscipcion;
    TextView textSaldo,textTipo;
    ImageView imagePersona;
    SearchView searchView;

    public static Usuario usuarioSeleccionado;
    private String currentPhotoPath="";
    private static final int COD_FOTO = 20;

    private Controller controller;
    private List<Usuario> usuarios;
    private List<Actividad> actividades;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //region widgets
        setContentView(R.layout.activity_usuarios);
        recyclerUsuarios = findViewById(R.id.recyclerUsuarios);
        recyclerActDisponibles = findViewById(R.id.recyclerActDisponibles);
        recyclerActUsuario = findViewById(R.id.recyclerActUsuario);
        cardGuardarUsuario = findViewById(R.id.cardGuardarUsuario);
        cardCondicion = findViewById(R.id.cardCondicion);
        cardEstado = findViewById(R.id.cardEstado);
        cardPagarCuenta = findViewById(R.id.cardPagarCuenta);
        cardImagenPersona = findViewById(R.id.cardImagenPersona);
        cardTodos = findViewById(R.id.cardTodos);
        cardLimpiarSaldo = findViewById(R.id.cardLimpiarSaldo);
        cardLimpiar = findViewById(R.id.cardLimpiar);
        searchView = findViewById(R.id.searchView);
        textNombre = findViewById(R.id.textNombre);
        textApellido = findViewById(R.id.textApellido);
        textDNI = findViewById(R.id.textDNI);
        textFechaInscipcion = findViewById(R.id.textFechaInscipcion);
        textDescuento = findViewById(R.id.textDescuento);
        textSaldo = findViewById(R.id.textSaldo);
        textTipo = findViewById(R.id.textTipo);
        textTelefono = findViewById(R.id.textTelefono);
        imagePersona = findViewById(R.id.imagePersona);
        linearMain = findViewById(R.id.linearMain);
        //endregion
        controller = new Controller(UsuariosActivity.this);

        //-------------------------------------------------------------------------------------------------------------

        Bundle bundle =  getIntent().getExtras();
        if(bundle!=null && bundle.getString("conUser").equals("si")){
            usuarioSeleccionado = MainActivity.usuarioSeleccionado;
            cargarUsuario();
            textTipo.setText("RENOVACION");
        }

        cargar();

        
        //-------------------------------------------------------------------------------------------------------------

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

        //-------------------------------------------------------------------------------------------------------------
        cardLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarCampos();
            }
        });
        cardCondicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarCondicion();
            }
        });
        cardEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarEstado();
            }
        });
        cardTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuarios.clear();
                for (Usuario user:controller.obtenerUsuario()) {
                    usuarios.add(user);
                }
                adapterUsuarios.notifyDataSetChanged();
            }
        });
        cardLimpiarSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UsuariosActivity.this);
                builder.setMessage("Desea limpiar Saldo de "+ usuarioSeleccionado.getApellido() + " "+usuarioSeleccionado.getNombre()+"?");
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
                            if (usuario.getDNI().equals(usuarioSeleccionado.getDNI())){
                                usuario.setSaldo(0);
                                usuario.setEstado("OK");
                                controller.guardarUsuario(usuario);
                                recargar();
                                Toast.makeText(getApplicationContext(), "SALDO ACTUALIZDO", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                });
                builder.create().show();
            }
        });
        cardImagenPersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto();
                controlador();
            }
        });
        adapterUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuarioSeleccionado = usuarios.get(recyclerUsuarios.getChildAdapterPosition(view));
                textTipo.setText("RENOVACION");
                cargarDatos();
            }
        });
        adapterUsuarios.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                usuarioSeleccionado = usuarios.get(recyclerUsuarios.getChildAdapterPosition(view));
                cargarDatos();
                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UsuariosActivity.this);
                builder.setMessage("Desea eliminar al usuario "+ usuarioSeleccionado.getApellido().toUpperCase()+", "+usuarioSeleccionado.getNombre()+"?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        controller.eliminarUsuario(usuarios.get(recyclerUsuarios.getChildAdapterPosition(view)));
                        recargar();
                        cartel("",false);
                    }
                });
                builder.create().show();
                return false;
            }
        });
        adapterActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listadoActividadesIng.add(actividades.get(recyclerActDisponibles.getChildAdapterPosition(view)));
                adapterActividadIng.notifyDataSetChanged();
                calcular();
            }
        });
        adapterActividadIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listadoActividadesIng.remove(recyclerActUsuario.getChildAdapterPosition(view));
                adapterActividadIng.notifyDataSetChanged();
                calcular();
            }
        });
        cardGuardarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardGuardarUsuario.startAnimation(MainActivity.scaleUp);
                if (!TextUtils.isEmpty(textDescuento.getText())){
                    if (Integer.parseInt(textDescuento.getText().toString())>0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(UsuariosActivity.this);
                        View viewInflated = LayoutInflater.from(UsuariosActivity.this).inflate(R.layout.contrasena, (ViewGroup) findViewById(android.R.id.content), false);
                        EditText textContraseña = viewInflated.findViewById(R.id.textContraseña);
                        CardView cardContinuar = viewInflated.findViewById(R.id.cardContinuar);
                        builder.setView(viewInflated);
                        cardContinuar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cardContinuar.startAnimation(MainActivity.scaleUp);
                                if (textContraseña.getText().toString().equals("power22")){
                                    validacion();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.show();
                    }
                }else{
                    validacion();
                }
            }
        });
        cardPagarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardPagarCuenta.startAnimation(MainActivity.scaleUp);
                Intent intent = new Intent(getApplicationContext(), CobrarActividad.class);
                intent.putExtra("usuario",usuarioSeleccionado.getDNI());
                startActivity(intent);
                finish();
            }
        });
        textTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textTipo.startAnimation(MainActivity.scaleUp);
                seleccionarTipo();
            }
        });
        textDescuento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calcular();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        textFechaInscipcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textFechaInscipcion.startAnimation(MainActivity.scaleUp);
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        textDNI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (Usuario user:usuarios) {
                    if (user.getDNI().equals(textDNI.getText().toString()) && textTipo.getText().toString().equals("NUEVO")){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(UsuariosActivity.this);
                        builder.setMessage("USUARIO EXISTENTE, Filtre y seleccione del Listado. No se puede cargar como NUEVO");
                        limpiarCampos();
                        builder.setCancelable(true);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
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
            if ((month+1)>9){
                textFechaInscipcion.setText(day+"/"+(month+1)+"/"+year);
            }else{
                textFechaInscipcion.setText(day+"/"+"0"+(month+1)+"/"+year);
            }
        }
    }
    //-----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------------
    private void validacion(){
        if(!TextUtils.isEmpty(textNombre.getText().toString()) && !TextUtils.isEmpty(textApellido.getText().toString()) && !TextUtils.isEmpty(textDNI.getText().toString()) && !TextUtils.isEmpty(textFechaInscipcion.getText().toString())){
            String actividades = "";
            for (Actividad actividad:listadoActividadesIng) {
                actividades = actividades + actividad.getNombre().toUpperCase()+", ";
            }
            final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UsuariosActivity.this);
            builder.setMessage("Desea guardar al usuario "+ textApellido.getText().toString().toUpperCase()+" como "+textTipo.getText().toString().toUpperCase() + " Actividades: "+ actividades+ " Saldo: $"+textSaldo.getText().toString());
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(textTipo.getText().toString().equals("REINSCRIPCION")){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(UsuariosActivity.this);
                        builder.setMessage("Es correcta la nueva Fecha de Inscripcion? "+textFechaInscipcion.getText().toString());
                        builder.setCancelable(true);
                        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    validacion2();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Toast.makeText(getApplicationContext(), "Seleccione Fecha de Inscripción", Toast.LENGTH_SHORT).show();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else{
                        try {
                            validacion2();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            builder.create().show();
        }else{
            Toast.makeText(getApplicationContext(), "Rellene Campos", Toast.LENGTH_SHORT).show();
        }
    }
    private void validacion2() throws ParseException {
        if (listadoActividadesIng.size()>0 && !textTipo.getText().toString().equals("MODIFICACION")){
            guardarUsuario();
        }else if (textTipo.getText().toString().equals("MODIFICACION")){
            guardarUsuario();
        }else{
            Toast.makeText(getApplicationContext(), "Agregar por lo menos una Actividad", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarUsuario() throws ParseException {
            Long timeStampFechaInsc,timeStampFechaVenc;
            String actividades = "";
            for (Actividad actividad:listadoActividadesIng) {
                actividades = actividades + actividad.getNombre().toUpperCase()+", ";
            }
            switch (textTipo.getText().toString()){
                case "NUEVO":
                    usuarioSeleccionado = new Usuario();
                    timeStampFechaInsc = (MainActivity.formatoddMMyyyy.parse(textFechaInscipcion.getText().toString())).getTime();
                    timeStampFechaVenc = usuarioSeleccionado.aumentarMes(timeStampFechaInsc);
                    usuarioSeleccionado = new Usuario(textNombre.getText().toString(),
                            textApellido.getText().toString(),
                            textDNI.getText().toString(),
                            currentPhotoPath,
                            textTelefono.getText().toString(),
                            "NUEVO - "+actividades,
                            "DEBIENDO",
                            timeStampFechaInsc,
                            timeStampFechaVenc,
                            Integer.parseInt(textSaldo.getText().toString()));

                            controller.nuevoUsuario(usuarioSeleccionado);
                            recargar();
                    cartel("agregado",true);

                    break;
                case "RENOVACION":
                    usuarioSeleccionado.setFechaVencimiento(usuarioSeleccionado.aumentarMes(usuarioSeleccionado.getFechaVencimiento()));
                    usuarioSeleccionado.setSaldo(usuarioSeleccionado.getSaldo()+Integer.parseInt(textSaldo.getText().toString()));
                    usuarioSeleccionado.setCondicion("RENOVACION - "+actividades);
                    usuarioSeleccionado.setEstado("DEBIENDO");
                    controller.guardarUsuario(usuarioSeleccionado);

                    recargar();
                    cartel("agregado",true);
                    break;
                case "REINSCRIPCION":
                    timeStampFechaInsc = (MainActivity.formatoddMMyyyy.parse(textFechaInscipcion.getText().toString())).getTime();
                    timeStampFechaVenc = usuarioSeleccionado.aumentarMes(timeStampFechaInsc);

                    usuarioSeleccionado.setNombre(textNombre.getText().toString());
                    usuarioSeleccionado.setApellido(textApellido.getText().toString());
                    usuarioSeleccionado.setDNI(textDNI.getText().toString());
                    usuarioSeleccionado.setNombre(textNombre.getText().toString());
                    usuarioSeleccionado.setPathfoto(currentPhotoPath);
                    usuarioSeleccionado.setTelefono(textTelefono.getText().toString());
                    usuarioSeleccionado.setCondicion("REINSCRIPCION - "+actividades);
                    usuarioSeleccionado.setEstado("DEBIENDO");
                    usuarioSeleccionado.setFechaVencimiento(timeStampFechaVenc);
                    usuarioSeleccionado.setFechaInscripcion(timeStampFechaInsc);
                    usuarioSeleccionado.setSaldo(usuarioSeleccionado.getSaldo()+Integer.parseInt(textSaldo.getText().toString()));

                    controller.guardarUsuario(usuarioSeleccionado);
                    recargar();
                    cartel("agregado",true);
                    break;
                case "MODIFICACION":
                    usuarioSeleccionado.setNombre(textNombre.getText().toString());
                    usuarioSeleccionado.setApellido(textApellido.getText().toString());
                    usuarioSeleccionado.setTelefono(textTelefono.getText().toString());
                    usuarioSeleccionado.setDNI(textDNI.getText().toString());
                    usuarioSeleccionado.setPathfoto(currentPhotoPath);
                    controller.guardarUsuario(usuarioSeleccionado);
                    recargar();
                    cartel("agregado",false);
                    break;
            }
            limpiarCampos();
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    private void seleccionarTipo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UsuariosActivity.this);
        final String[] tipo = new String[]{"NUEVO","RENOVACION","REINSCRIPCION","MODIFICACION"};
        builder.setTitle("Seleccioná Tipo").setItems(tipo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                textTipo.setText(tipo[which]);
                if(tipo[which].equals("REINSCRIPCION")){
                    textFechaInscipcion.setText(MainActivity.formatoddMMyyyy.format(System.currentTimeMillis()));
                    /*
                    final AlertDialog.Builder builder = new AlertDialog.Builder(UsuariosActivity.this);
                    builder.setMessage("Revise Nueva Fecha de Inscripción ");
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    */
                }


            }
        });
        builder.show();
    }
    private void seleccionarCondicion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UsuariosActivity.this);
        final String[] tipo = new String[]{"NUEVO","RENOVACION","REINSCRIPCION"};
        builder.setTitle("Seleccioná Tipo").setItems(tipo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                usuarios.clear();
                for (Usuario user:controller.obtenerUsuario()) {
                    try{
                        if (user.getCondicion().split(" - ")[0].equals(tipo[which])){
                            usuarios.add(user);
                        }
                    }catch (Exception es){

                    }
                }
                adapterUsuarios.notifyDataSetChanged();
            }
        });
        builder.show();
    }
    private void seleccionarEstado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UsuariosActivity.this);
        final String[] tipo = new String[]{"DEBIENDO","OK"};
        builder.setTitle("Seleccioná Tipo").setItems(tipo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                usuarios.clear();
                for (Usuario user:controller.obtenerUsuario()) {
                    if (user.getEstado().equals(tipo[which])){
                        usuarios.add(user);
                    }
                }
                adapterUsuarios.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    private void calcular(){
        int saldo = 0;
        for (int i=0;i<listadoActividadesIng.size();i++){
            saldo += listadoActividadesIng.get(i).getPrecio();
        }
        try{
            saldo = saldo - Integer.parseInt(textDescuento.getText().toString());
        }catch (Exception es){
        }
        textSaldo.setText(String.valueOf(saldo));
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------------------------------
    private void tomarFoto(){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePhotoIntent.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            try{
                photoFile = createPhotoFile();

                if(photoFile!=null){
                    Uri photoUri = FileProvider.getUriForFile(getApplicationContext(),"com.example.gymsoftware",photoFile);
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                    startActivityForResult(takePhotoIntent,COD_FOTO);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private File createPhotoFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile("photo",".jpg",storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void controlador(){
        CountDownTimer countDownTimer = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long l) {
                imagePersona.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath));
            }
            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    private void limpiarCampos(){
        textNombre.setText("");
        textApellido.setText("");
        textTelefono.setText("");
        textDNI.setText("");
        textSaldo.setText("0");
        listadoActividadesIng.clear();
        adapterActividadIng.notifyDataSetChanged();
    }
    //------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------------



    private void cargarUsuario(){
        try {
            if (usuarioSeleccionado!=null){
                if (!usuarioSeleccionado.getPathfoto().equals("")){
                    imagePersona.setImageBitmap(BitmapFactory.decodeFile(usuarioSeleccionado.getPathfoto()));
                    currentPhotoPath = usuarioSeleccionado.getPathfoto();
                }
            }
            textNombre.setText(usuarioSeleccionado.getNombre());
            textApellido.setText(usuarioSeleccionado.getApellido());
            textTelefono.setText(usuarioSeleccionado.getTelefono());
            textDNI.setText(usuarioSeleccionado.getDNI());
            textFechaInscipcion.setText(usuarioSeleccionado.getFechaInscripcionDD());
            textSaldo.setText(usuarioSeleccionado.getSaldo().toString());
        }catch (Exception es){
            System.out.println(es.getMessage());
        }

    }

    private void cargarDatos(){
        if (!usuarioSeleccionado.getPathfoto().equals("")){
            imagePersona.setImageBitmap(BitmapFactory.decodeFile(usuarioSeleccionado.getPathfoto()));
            currentPhotoPath = usuarioSeleccionado.getPathfoto();
        }
        textNombre.setText(usuarioSeleccionado.getNombre());
        textApellido.setText(usuarioSeleccionado.getApellido());
        textTelefono.setText(usuarioSeleccionado.getTelefono());
        textDNI.setText(usuarioSeleccionado.getDNI());
        textFechaInscipcion.setText(usuarioSeleccionado.getFechaInscripcionDD());
        textSaldo.setText(usuarioSeleccionado.getSaldo().toString());
    }


    private void recargar(){
        usuarios.clear();
        for (Usuario user:controller.obtenerUsuario()) {
            usuarios.add(user);
        }
        adapterUsuarios.notifyDataSetChanged();
    }

    private void cargar(){
        textFechaInscipcion.setText(MainActivity.formatoddMMyyyy.format(System.currentTimeMillis()));
        listadoActividadesIng = new ArrayList<>();
        actividades = controller.obtenerActividad();
        usuarios = new ArrayList<>();
        for (Usuario user:controller.obtenerUsuario()) {
            usuarios.add(user);
        }
        adapterUsuarios = new AdapterUsuarios(usuarios,getApplicationContext());
        recyclerUsuarios.setAdapter(adapterUsuarios);
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(this));

        adapterActividad = new AdapterActividad(actividades,getApplicationContext());
        recyclerActDisponibles.setAdapter(adapterActividad);
        recyclerActDisponibles.setLayoutManager(new LinearLayoutManager(this));

        adapterActividadIng = new AdapterActividad(listadoActividadesIng,getApplicationContext());
        recyclerActUsuario.setAdapter(adapterActividadIng);
        recyclerActUsuario.setLayoutManager(new LinearLayoutManager(this));
    }

    public void cartel(String tipo, Boolean band){
        AlertDialog.Builder builder = new AlertDialog.Builder(UsuariosActivity.this);
        View viewInflated = LayoutInflater.from(UsuariosActivity.this).inflate(R.layout.cartel, (ViewGroup) findViewById(android.R.id.content), false);
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
                if (band){
                    Intent intent = new Intent(getApplicationContext(), CobrarActividad.class);
                    intent.putExtra("usuario",usuarioSeleccionado.getDNI());
                    startActivity(intent);
                    dialogInterface.cancel();
                }else{
                    dialogInterface.cancel();
                }
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }
}