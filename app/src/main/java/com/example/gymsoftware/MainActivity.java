package com.example.gymsoftware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.gymsoftware.BaseDatos.Controller;
import com.example.gymsoftware.BaseDatos.DataBaseHelper;
import com.example.gymsoftware.Objetos.Actividad;
import com.example.gymsoftware.Objetos.Arqueo;
import com.example.gymsoftware.Objetos.IngresoEgreso;
import com.example.gymsoftware.Objetos.Pago;
import com.example.gymsoftware.Objetos.Usuario;
import com.example.gymsoftware.Objetos.Usuario2;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearMain,linearNav;

    public static Animation scaleUp;
    private FirebaseFirestore basedatos;
    private CardView card0,card1,card2,card3,card4,card5,card6,card7,card8,card9,cardBorrar,cardOK,
            cardCobrar,cardEstadisticas,cardArqueo,cardIngresoEgreso,cardUsuarios,
            cardPagar,cardConfiguracion;

    private LinearLayout linearDatos3,linearDatos;
    private CardView cardDatos2,cardDatos;
    private TextView textDNIBuscar,textDNI,textNombre,textEstado,textFechaVencimiento,textFechaInscr;
    private Button buttonCerrar;
    private ImageView imagePersona;

    public static SimpleDateFormat formatoddMMyyyyHHmmss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static SimpleDateFormat formatoddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat formatodd = new SimpleDateFormat("dd");
    public static SimpleDateFormat formatoMM = new SimpleDateFormat("MM");
    public static SimpleDateFormat formatoyyyy = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat formatoHHmm = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat formatoHH = new SimpleDateFormat("HH");
    public static SimpleDateFormat formatoEEEE = new SimpleDateFormat("EEEE");

    private String DNIIngresado="";
    public static Usuario usuarioSeleccionado;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //region Widgets
        scaleUp =  AnimationUtils.loadAnimation(this,R.anim.scale_up);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card6 = findViewById(R.id.card6);
        card7 = findViewById(R.id.card7);
        card8 = findViewById(R.id.card8);
        card9 = findViewById(R.id.card9);
        card0 = findViewById(R.id.card0);
        cardBorrar = findViewById(R.id.cardBorrar);
        cardOK = findViewById(R.id.cardOK);
        cardCobrar = findViewById(R.id.cardCobrar);
        cardEstadisticas = findViewById(R.id.cardEstadisticas);
        cardArqueo = findViewById(R.id.cardArqueo);
        cardIngresoEgreso = findViewById(R.id.cardIngresoEgreso);
        cardUsuarios = findViewById(R.id.cardUsuarios);
        cardConfiguracion = findViewById(R.id.cardConfiguracion);
        linearDatos3 = findViewById(R.id.linearDatos3);
        cardDatos2 = findViewById(R.id.cardDatos2);
        cardDatos = findViewById(R.id.cardDatos);
        linearDatos = findViewById(R.id.linearDatos);
        cardPagar = findViewById(R.id.cardPagar);
        imagePersona = findViewById(R.id.imagePersona);
        textDNIBuscar = findViewById(R.id.textDNIBuscar);
        textDNI = findViewById(R.id.textDNI);
        textNombre = findViewById(R.id.textNombre);
        textEstado = findViewById(R.id.textEstado);
        textFechaVencimiento = findViewById(R.id.textFechaVencimiento);
        textFechaInscr = findViewById(R.id.textFechaInscr);
        buttonCerrar = findViewById(R.id.buttonCerrar);

        linearMain = findViewById(R.id.linearMain);
        linearNav = findViewById(R.id.linearNav);

        cardDatos.setOnClickListener(onClickListener);
        cardArqueo.setOnClickListener(onClickListener);
        cardCobrar.setOnClickListener(onClickListener);
        cardEstadisticas.setOnClickListener(onClickListener);
        cardIngresoEgreso.setOnClickListener(onClickListener);
        cardUsuarios.setOnClickListener(onClickListener);
        cardPagar.setOnClickListener(onClickListener);
        cardConfiguracion.setOnClickListener(onClickListener);

        card0.setOnClickListener(onClickListenerDNI);
        card1.setOnClickListener(onClickListenerDNI);
        card2.setOnClickListener(onClickListenerDNI);
        card3.setOnClickListener(onClickListenerDNI);
        card4.setOnClickListener(onClickListenerDNI);
        card5.setOnClickListener(onClickListenerDNI);
        card6.setOnClickListener(onClickListenerDNI);
        card7.setOnClickListener(onClickListenerDNI);
        card8.setOnClickListener(onClickListenerDNI);
        card9.setOnClickListener(onClickListenerDNI);
        cardOK.setOnClickListener(onClickListenerDNI);
        cardBorrar.setOnClickListener(onClickListenerDNI);
        buttonCerrar.setOnClickListener(onClickListenerDNI);

        //endregion
        linearDatos3.setVisibility(View.INVISIBLE);
        cardDatos2.setVisibility(View.INVISIBLE);
        cardDatos.setVisibility(View.INVISIBLE);
        linearDatos.setBackgroundResource(R.drawable.power_max);


        controller = new Controller(MainActivity.this);


        exportacionAut();  //PARA TABLET ESTO DEJAR DESMARCADO




        //importacionAut(); //PARA CELULARES
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation=newConfig.orientation;
        switch(orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                linearMain.setOrientation(LinearLayout.HORIZONTAL);
                linearNav.setOrientation(LinearLayout.VERTICAL);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                linearNav.setOrientation(LinearLayout.HORIZONTAL);
                linearMain.setOrientation(LinearLayout.VERTICAL);
                break;
        }
    }
    View.OnClickListener onClickListenerDNI = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.card1:
                    card1.startAnimation(scaleUp);
                    DNIIngresado = DNIIngresado +"1";
                    break;
                case R.id.card2:
                    card2.startAnimation(scaleUp);
                    DNIIngresado = DNIIngresado +"2";
                    break;
                case R.id.card3:
                    card3.startAnimation(scaleUp);
                    DNIIngresado = DNIIngresado +"3";
                    break;
                case R.id.card4:
                    card4.startAnimation(scaleUp);
                    DNIIngresado = DNIIngresado +"4";
                    break;
                case R.id.card5:
                    card5.startAnimation(scaleUp);
                    DNIIngresado = DNIIngresado +"5";
                    break;
                case R.id.card6:
                    card6.startAnimation(scaleUp);
                    DNIIngresado = DNIIngresado +"6";
                    break;
                case R.id.card7:
                    card7.startAnimation(scaleUp);
                    DNIIngresado = DNIIngresado +"7";
                    break;
                case R.id.card8:
                    card8.startAnimation(scaleUp);
                    DNIIngresado = DNIIngresado +"8";
                    break;
                case R.id.card9:
                    card9.startAnimation(scaleUp);
                    DNIIngresado = DNIIngresado +"9";
                    break;
                case R.id.card0:
                    card0.startAnimation(scaleUp);
                    DNIIngresado = DNIIngresado +"0";
                    break;
                case R.id.cardOK:
                    for (Usuario usuario:controller.obtenerUsuario()) {
                        if (DNIIngresado.equals(usuario.getDNI())){
                            usuarioSeleccionado = usuario;

                            linearDatos3.setVisibility(View.VISIBLE);
                            cardDatos2.setVisibility(View.VISIBLE);
                            cardDatos.setVisibility(View.VISIBLE);
                            imagePersona.setImageBitmap(BitmapFactory.decodeFile(usuarioSeleccionado.getPathfoto()));
                            textDNI.setText(usuarioSeleccionado.getDNI().toString());
                            textNombre.setText(usuarioSeleccionado.getApellido().toString().toUpperCase()+", "+usuarioSeleccionado.getNombre().toString());
                            textEstado.setText(usuarioSeleccionado.getEstado().toString());
                            textFechaVencimiento.setText("Vencimiento: "+usuarioSeleccionado.getFechaVencimientoDD());
                            textFechaInscr.setText("Inscripcion: "+usuarioSeleccionado.getFechaInscripcionDD());
                            break;
                        }
                    }
                    break;
                case R.id.cardBorrar:
                    try{
                        DNIIngresado = DNIIngresado.substring(0,DNIIngresado.length()-1);
                    }catch (Exception es){
                    }
                    break;
                case R.id.buttonCerrar:
                    linearDatos.setBackgroundResource(R.drawable.power_max);
                    linearDatos3.setVisibility(View.INVISIBLE);
                    cardDatos2.setVisibility(View.INVISIBLE);
                    cardDatos.setVisibility(View.INVISIBLE);
                    textDNIBuscar.setText("DNI: 00000000");
                    break;
            }
            if (R.id.cardOK == view.getId()){
                textDNIBuscar.setText("DNI: 00000000");
            }else{
                textDNIBuscar.setText("DNI: "+DNIIngresado);
            }

        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()){
                case R.id.cardCobrar:
                    cardCobrar.startAnimation(scaleUp);
                    intent = new Intent(getApplicationContext(), CobrarActividad.class);
                    startActivity(intent);
                    break;
                case R.id.cardEstadisticas:
                    cardEstadisticas.startAnimation(scaleUp);
                    intent = new Intent(getApplicationContext(), EstadisticasActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cardArqueo:
                    cardArqueo.startAnimation(scaleUp);
                    intent = new Intent(getApplicationContext(), ArqueoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cardIngresoEgreso:
                    cardIngresoEgreso.startAnimation(scaleUp);
                    intent = new Intent(getApplicationContext(), IngresosEgresosActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cardConfiguracion:
                    cardConfiguracion.startAnimation(scaleUp);
                    intent = new Intent(getApplicationContext(), ConfiguracionActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cardUsuarios:
                    cardUsuarios.startAnimation(scaleUp);
                    intent = new Intent(getApplicationContext(), UsuariosActivity.class);
                    startActivity(intent);
                    break;
                case R.id.cardPagar:
                    cardPagar.startAnimation(scaleUp);
                    intent = new Intent(getApplicationContext(), CobrarActividad.class);
                    intent.putExtra("usuario",usuarioSeleccionado.getDNI());
                    startActivity(intent);
                    break;
                case R.id.cardDatos:
                    cardDatos.startAnimation(scaleUp);
                    intent = new Intent(getApplicationContext(), UsuariosActivity.class);
                    intent.putExtra("conUser","si");
                    startActivity(intent);
                    break;
            }
        }
    };




    public void exportarDatos(){
        try{
            basedatos = FirebaseFirestore.getInstance();
            basedatos.collection("Usuarios").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Usuario2 user = documentSnapshot.toObject(Usuario2.class);

                        basedatos.collection("Pagos").document(String.valueOf(user.getDNI())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    Pago pago = documentSnapshot.toObject(Pago.class);

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    Date parsedDate = null;
                                    Date parsedDate2 = null;
                                    try {
                                        parsedDate = dateFormat.parse(user.getFechaInscripcion());
                                        parsedDate2 = dateFormat.parse(user.getFechaVencimiento());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    long timestampInscr = parsedDate.getTime();
                                    long timestampInscr2 = parsedDate2.getTime();
                                    String estado = "";
                                    if (pago.getDebe()==0){
                                        estado = "OK";
                                    }else{
                                        estado = "DEBIENDO";
                                    }
                                    String path = user.getPathfoto().replace("com.gonzaloguaimas.powermaxgym","com.example.gymsoftware");
                                    Usuario usuarioNuev = new Usuario(user.getNombre(),user.getApellido(),user.getDNI().toString(),path,user.getTelefono(),user.getCondicion(),estado,timestampInscr,timestampInscr2,pago.getDebe());

                                    controller.nuevoUsuario(usuarioNuev);
                                }
                            }

                        });
                    }
                }
            });

        }catch (Exception es){

        }
    }

    private void exportacionAut(){
        if (controller.obtenerBandera().split("-")[0].equals("SI") &&
                !MainActivity.formatoddMMyyyy.format(System.currentTimeMillis()).equals(controller.obtenerBandera().split("-")[1])){
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
            Toast.makeText(getApplicationContext(), "Se exportaron "+String.valueOf(count)+" ingresos y Egresos", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "BACK UP AUTOMATICO REALIZADO", Toast.LENGTH_LONG).show();
            controller.guardaBandera("SI-"+MainActivity.formatoddMMyyyy.format(System.currentTimeMillis()));
        }
    }


    private void importacionAut(){
        if (controller.obtenerBandera().split("-")[0].equals("SI") &&
                !MainActivity.formatoddMMyyyy.format(System.currentTimeMillis()).equals(controller.obtenerBandera().split("-")[1])){
            FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
            DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
            dataBaseHelper.eliminarDatosTablas(dataBaseHelper.getWritableDatabase());
            basedatos.collection("BupActividades").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        controller.nuevaActividad(documentSnapshot.toObject(Actividad.class));
                    }
                }
            });

            basedatos.collection("BupUsuarios").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        controller.nuevoUsuario(documentSnapshot.toObject(Usuario.class));
                    }
                }
            });

            basedatos.collection("BupArqueo").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        controller.nuevoArqueo(documentSnapshot.toObject(Arqueo.class));
                    }
                }
            });

            basedatos.collection("BupIngresosEgresos").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        controller.nuevoIngreso(documentSnapshot.toObject(IngresoEgreso.class));
                    }
                }
            });
            Toast.makeText(getApplicationContext(), "DATOS IMPORTADOS", Toast.LENGTH_SHORT).show();

        }
    }
}