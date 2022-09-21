package com.example.gymsoftware.BaseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.core.app.NavUtils;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DE_DATOS = "gimnasio",
            TABLA_INGRESOS = "IngresosEgresos",
            TABLA_USUARIOS = "Usuarios",
            TABLA_ARQUEO = "Arqueos",
            TABLA_ACTIVIDAD = "Actividades",
            TABLA_BANDERA = "Bandera";
    private static final int VERSION_BASE_DE_DATOS = 1;

    public DataBaseHelper(Context context) {
        super(context, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(id integer primary key autoincrement, bandera text)", TABLA_BANDERA));
        db.execSQL(String.format("INSERT INTO " + TABLA_BANDERA+ "(bandera) VALUES ('SI-nulo')"));
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(id integer primary key autoincrement, nombre text, precio int)", TABLA_ACTIVIDAD));
        System.out.println("tabla ingresos actividad");
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(idAut integer primary key autoincrement, id long, importe int, formaPago text, comentario text, DNI text, nombre text)", TABLA_INGRESOS));
        System.out.println("tabla ingresos creada");
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(idAut integer primary key autoincrement, nombre text, apellido text, DNI int, pathfoto text, telefono text, condicion text, estado text, fechaInscripcion long, fechaVencimiento long, saldo int)", TABLA_USUARIOS));
        System.out.println("tabla ingresos usuario");
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(idAut integer primary key autoincrement, id long, efectivo_sistema int, efectivo_real int, debito int, credito int, transferencia int, gastos int, total int,comentario text)", TABLA_ARQUEO));
        System.out.println("tabla ingresos arqueo");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void eliminarDatosTablas(SQLiteDatabase db){
        db.delete(TABLA_ACTIVIDAD, null,null);
        db.delete(TABLA_INGRESOS, null,null);
        db.delete(TABLA_USUARIOS, null,null);
        db.delete(TABLA_ARQUEO, null,null);
    }
}
