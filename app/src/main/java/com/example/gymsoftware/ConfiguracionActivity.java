package com.example.gymsoftware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.example.gymsoftware.Utils.AdapterActividad;
import com.example.gymsoftware.Utils.AdapterIngresoEgreso;
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

public class ConfiguracionActivity extends AppCompatActivity {
    LinearLayout linearMain;
    EditText textNombreActividad,textPrecioActividad;
    TextView textInformacion;
    CardView cardAgregarActividad,cardRedes,cardImportar,cardExportar;
    RecyclerView recyclerActividades;
    AdapterActividad adapterActividad;
    List<Actividad> actividades;
    Controller controller;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        textNombreActividad = findViewById(R.id.textNombreActividad);
        textPrecioActividad = findViewById(R.id.textPrecioActividad);
        cardAgregarActividad = findViewById(R.id.cardAgregarActividad);
        recyclerActividades = findViewById(R.id.recyclerActividades);
        linearMain = findViewById(R.id.linearMain);
        textInformacion = findViewById(R.id.textInformacion);
        cardRedes = findViewById(R.id.cardRedes);
        cardImportar = findViewById(R.id.cardImportar);
        cardExportar = findViewById(R.id.cardExportar);
        checkBox = findViewById(R.id.checkBox);

        controller = new Controller(ConfiguracionActivity.this);

        cargar();

        cardAgregarActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(textNombreActividad.getText()) && !TextUtils.isEmpty(textPrecioActividad.getText())){
                    Actividad actividad = new Actividad(textNombreActividad.getText().toString(),Integer.parseInt(textPrecioActividad.getText().toString()));
                    controller.nuevaActividad(actividad);
                    recargar();
                    cartel("agregado");
                    limpiarCampos();
                }else{
                    Toast.makeText(getApplicationContext(), "Rellene Campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        adapterActividad.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ConfiguracionActivity.this);
                builder.setMessage("Desea eliminar la actividaad "+ actividades.get(recyclerActividades.getChildAdapterPosition(view)).getNombre()+"?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        controller.eliminarActividad(actividades.get(recyclerActividades.getChildAdapterPosition(view)));
                        cartel("");
                        recargar();
                    }
                });
                builder.create().show();
                return false;
            }
        });
        cardRedes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardRedes.startAnimation(MainActivity.scaleUp);
                Uri uri = Uri.parse("https://www.instagram.com/omegaa.si/");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });
        cardImportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardImportar.startAnimation(MainActivity.scaleUp);
                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ConfiguracionActivity.this);
                builder.setMessage("Desea importar datos? Se sobrescribiran los existentes ");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       // importar();
                    }
                });
                builder.create().show();
            }
        });
        cardExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardExportar.startAnimation(MainActivity.scaleUp);
                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ConfiguracionActivity.this);
                builder.setMessage("Desea exportar datos? No ocurrirá ningun cambio");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exportar();
                        Toast.makeText(getApplicationContext(), "Opcion deshabilitada.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox.isChecked()){
                    controller.guardaBandera("SI-nulo");
                }else{
                    controller.guardaBandera("NO-nulo");
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
    private void recargar(){
        actividades.clear();
        for (Actividad actividad: controller.obtenerActividad()) {
            actividades.add(actividad);
        }
        adapterActividad.notifyDataSetChanged();
    }
    private void cargar(){
        actividades = new ArrayList<>();
        for (Actividad actividad: controller.obtenerActividad()) {
            actividades.add(actividad);
        }
        adapterActividad = new AdapterActividad(actividades,getApplicationContext());
        recyclerActividades.setAdapter(adapterActividad);
        recyclerActividades.setLayoutManager(new LinearLayoutManager(this));

        if (controller.obtenerBandera().split("-")[0].equals("SI")){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }
    }
    private void limpiarCampos(){
        textPrecioActividad.setText("");
        textNombreActividad.setText("");
    }













    public void cartel(String tipo){
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfiguracionActivity.this);
        View viewInflated = LayoutInflater.from(ConfiguracionActivity.this).inflate(R.layout.cartel, (ViewGroup) findViewById(android.R.id.content), false);
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













    private void importar(){
        FirebaseFirestore basedatos = FirebaseFirestore.getInstance();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(ConfiguracionActivity.this);
        dataBaseHelper.eliminarDatosTablas(dataBaseHelper.getWritableDatabase());
        textInformacion.setText("ESPERE PORFAVOR...");
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
                    textInformacion.setText("DATOS IMPORTADOS");
                }
            }
        });

    }

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
        Toast.makeText(getApplicationContext(), "Se exportaron "+String.valueOf(count)+" ingresos y Egresos", Toast.LENGTH_SHORT).show();
        textInformacion.setText("DATOS EXPORTADOS");
    }
}