package com.example.gymsoftware.BaseDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gymsoftware.MainActivity;
import com.example.gymsoftware.Objetos.Actividad;
import com.example.gymsoftware.Objetos.Arqueo;
import com.example.gymsoftware.Objetos.IngresoEgreso;
import com.example.gymsoftware.Objetos.Usuario;

import java.util.ArrayList;
import java.util.Date;

public class Controller {
    private DataBaseHelper dataBaseHelper;
    private String TABLA_INGRESOS = "IngresosEgresos";
    private String TABLA_ACTIVIDADES = "Actividades";
    private String TABLA_ARQUEOS = "Arqueos";
    private String TABLA_USUARIOS = "Usuarios";
    private String TABLA_BANDERA = "Bandera";


    public Controller(Context context){
        dataBaseHelper = new DataBaseHelper(context);
    }


    //------------------------INGRESOS ESGRESOS------------------------------------------------
    //------------------------INGRESOS ESGRESOS------------------------------------------------
    //------------------------INGRESOS ESGRESOS------------------------------------------------
    //------------------------INGRESOS ESGRESOS------------------------------------------------
    //------------------------INGRESOS ESGRESOS------------------------------------------------
    public int eliminarIngreso(IngresoEgreso ingresoEgreso){
        SQLiteDatabase baseDatos = dataBaseHelper.getWritableDatabase();
        String[] args = {String.valueOf(ingresoEgreso.getIdAut())};
        return baseDatos.delete(TABLA_INGRESOS,"idAut = ?",args);
    }
    public long nuevoIngreso(IngresoEgreso ingresoEgreso) {
        SQLiteDatabase baseDeDatos = dataBaseHelper.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();
        valoresParaInsertar.put("id", ingresoEgreso.getId());
        valoresParaInsertar.put("importe", ingresoEgreso.getImporte());
        valoresParaInsertar.put("formaPago", ingresoEgreso.getFormaPago());
        valoresParaInsertar.put("comentario", ingresoEgreso.getComentario());
        valoresParaInsertar.put("DNI", ingresoEgreso.getDNI());
        valoresParaInsertar.put("nombre", ingresoEgreso.getNombre());
        return baseDeDatos.insert(TABLA_INGRESOS, null, valoresParaInsertar);
    }

    public int guardarCambiosIngreso(IngresoEgreso ingresoEgreso) {
        SQLiteDatabase baseDeDatos = dataBaseHelper.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();
        valoresParaActualizar.put("id", ingresoEgreso.getId());
        valoresParaActualizar.put("importe", ingresoEgreso.getImporte());
        valoresParaActualizar.put("formaPago", ingresoEgreso.getFormaPago());
        valoresParaActualizar.put("comentario", ingresoEgreso.getComentario());
        valoresParaActualizar.put("DNI", ingresoEgreso.getDNI());
        valoresParaActualizar.put("nombre", ingresoEgreso.getNombre());
        String campoParaActualizar = "idAut = ?";
        String[] argumentosParaActualizar = {String.valueOf(ingresoEgreso.getIdAut())};
        return baseDeDatos.update(TABLA_INGRESOS, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }

    public ArrayList<IngresoEgreso> obtenerIngresoEgreso() {
        ArrayList<IngresoEgreso> ingresoEgresos = new ArrayList<>();
        SQLiteDatabase baseDeDatos = dataBaseHelper.getReadableDatabase();
        String[] columnasAConsultar = {"id", "importe", "formaPago","comentario","DNI","nombre","idAut"};
        Cursor cursor = baseDeDatos.query(TABLA_INGRESOS,columnasAConsultar,null,null,null,null,null);
        if (cursor == null) {
            return ingresoEgresos;
        }
        if (!cursor.moveToFirst()) return ingresoEgresos;
        do {
            IngresoEgreso ingresoEgreso = new IngresoEgreso(cursor.getLong(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getLong(6));
            ingresoEgresos.add(ingresoEgreso);
        } while (cursor.moveToNext());
        cursor.close();
        return ingresoEgresos;
    }











    //------------------------ACTIVIDADES------------------------------------------------
    //------------------------ACTIVIDADES------------------------------------------------
    //------------------------ACTIVIDADES------------------------------------------------
    //------------------------ACTIVIDADES------------------------------------------------
    public int eliminarActividad(Actividad actividad){
        SQLiteDatabase baseDatos = dataBaseHelper.getWritableDatabase();
        String[] args = {String.valueOf(actividad.getId())};
        return baseDatos.delete(TABLA_ACTIVIDADES,"id = ?",args);
    }
    public long nuevaActividad(Actividad actividad) {
        SQLiteDatabase baseDeDatos = dataBaseHelper.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();
        valoresParaInsertar.put("nombre", actividad.getNombre());
        valoresParaInsertar.put("precio", actividad.getPrecio());
        return baseDeDatos.insert(TABLA_ACTIVIDADES, null, valoresParaInsertar);
    }

    public int guardarCambiosActividad(Actividad actividad) {
        SQLiteDatabase baseDeDatos = dataBaseHelper.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();
        valoresParaActualizar.put("nombre", actividad.getNombre());
        valoresParaActualizar.put("precio", actividad.getPrecio());
        String campoParaActualizar = "id = ?";
        String[] argumentosParaActualizar = {String.valueOf(actividad.getId())};
        return baseDeDatos.update(TABLA_ACTIVIDADES, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }

    public ArrayList<Actividad> obtenerActividad() {
        ArrayList<Actividad> actividades = new ArrayList<>();
        SQLiteDatabase baseDeDatos = dataBaseHelper.getReadableDatabase();
        String[] columnasAConsultar = {"nombre", "precio","id"};
        Cursor cursor = baseDeDatos.query(TABLA_ACTIVIDADES,columnasAConsultar,null,null,null,null,null);
        if (cursor == null) {
            return actividades;
        }
        if (!cursor.moveToFirst()) return actividades;
        do {
            Actividad actividad = new Actividad(cursor.getString(0),cursor.getInt(1),cursor.getLong(2));
            actividades.add(actividad);
        } while (cursor.moveToNext());
        cursor.close();
        return actividades;
    }







    //------------------------INGRESOS ESGRESOS------------------------------------------------
    //------------------------INGRESOS ESGRESOS------------------------------------------------
    //------------------------INGRESOS ESGRESOS------------------------------------------------
    //------------------------INGRESOS ESGRESOS------------------------------------------------
    //------------------------INGRESOS ESGRESOS------------------------------------------------
    public int eliminarArqueo(Arqueo arqueo){
        SQLiteDatabase baseDatos = dataBaseHelper.getWritableDatabase();
        String[] args = {String.valueOf(arqueo.getIdAut())};
        return baseDatos.delete(TABLA_ARQUEOS,"idAut = ?",args);
    }
    public long nuevoArqueo(Arqueo arqueo) {
        SQLiteDatabase baseDeDatos = dataBaseHelper.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();
        valoresParaInsertar.put("id", arqueo.getId());
        valoresParaInsertar.put("efectivo_sistema", arqueo.getEfectivo_sistema());
        valoresParaInsertar.put("efectivo_real", arqueo.getEfectivo_real());
        valoresParaInsertar.put("debito", arqueo.getDebito());
        valoresParaInsertar.put("credito", arqueo.getCredito());
        valoresParaInsertar.put("transferencia", arqueo.getTransferencia());
        valoresParaInsertar.put("gastos", arqueo.getGastos());
        valoresParaInsertar.put("total", arqueo.getTotal());
        valoresParaInsertar.put("comentario", arqueo.getComentario());
        return baseDeDatos.insert(TABLA_ARQUEOS, null, valoresParaInsertar);
    }

    public int guardarCambiosArqueo(Arqueo arqueo) {
        SQLiteDatabase baseDeDatos = dataBaseHelper.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();
        valoresParaActualizar.put("id", arqueo.getId());
        valoresParaActualizar.put("efectivo_sistema", arqueo.getEfectivo_sistema());
        valoresParaActualizar.put("efectivo_real", arqueo.getEfectivo_real());
        valoresParaActualizar.put("debito", arqueo.getDebito());
        valoresParaActualizar.put("credito", arqueo.getCredito());
        valoresParaActualizar.put("transferencia", arqueo.getTransferencia());
        valoresParaActualizar.put("gastos", arqueo.getGastos());
        valoresParaActualizar.put("total", arqueo.getTotal());
        valoresParaActualizar.put("comentario", arqueo.getComentario());
        String campoParaActualizar = "idAut = ?";
        String[] argumentosParaActualizar = {String.valueOf(arqueo.getIdAut())};
        return baseDeDatos.update(TABLA_ARQUEOS, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }

    public ArrayList<Arqueo> obtenerArqueo() {
        ArrayList<Arqueo> arqueos = new ArrayList<>();
        SQLiteDatabase baseDeDatos = dataBaseHelper.getReadableDatabase();
        String[] columnasAConsultar = {"id", "efectivo_sistema", "efectivo_real","debito","credito","transferencia","gastos","total","comentario","idAut"};
        Cursor cursor = baseDeDatos.query(TABLA_ARQUEOS,columnasAConsultar,null,null,null,null,null);
        if (cursor == null) {
            return arqueos;
        }
        if (!cursor.moveToFirst()) return arqueos;
        do {
            Arqueo arqueo = new Arqueo(cursor.getLong(0),cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6),cursor.getInt(7),cursor.getString(8),cursor.getLong(9));
            arqueos.add(arqueo);
        } while (cursor.moveToNext());
        cursor.close();
        return arqueos;
    }






    //------------------------USUARIOS---------------------------------------------------
    //------------------------USUARIOS---------------------------------------------------
    //------------------------USUARIOS---------------------------------------------------
    //------------------------USUARIOS---------------------------------------------------
    //------------------------USUARIOS---------------------------------------------------

    public int eliminarUsuario(Usuario usuario){
        SQLiteDatabase baseDatos = dataBaseHelper.getWritableDatabase();
        String[] args = {String.valueOf(usuario.getIdAut())};
        return baseDatos.delete(TABLA_USUARIOS,"idAut = ?",args);
    }
    public long nuevoUsuario(Usuario usuario) {
        SQLiteDatabase baseDeDatos = dataBaseHelper.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();
        valoresParaInsertar.put("nombre", usuario.getNombre());
        valoresParaInsertar.put("apellido", usuario.getApellido());
        valoresParaInsertar.put("DNI", usuario.getDNI());
        valoresParaInsertar.put("pathfoto", usuario.getPathfoto());
        valoresParaInsertar.put("telefono", usuario.getTelefono());
        valoresParaInsertar.put("condicion", usuario.getCondicion());
        valoresParaInsertar.put("estado", usuario.getEstado());
        valoresParaInsertar.put("fechaInscripcion", usuario.getFechaInscripcion());
        valoresParaInsertar.put("fechaVencimiento", usuario.getFechaVencimiento());
        valoresParaInsertar.put("saldo", usuario.getSaldo());
        return baseDeDatos.insert(TABLA_USUARIOS, null, valoresParaInsertar);
    }

    public int guardarUsuario(Usuario usuario) {
        SQLiteDatabase baseDeDatos = dataBaseHelper.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();
        valoresParaActualizar.put("nombre", usuario.getNombre());
        valoresParaActualizar.put("apellido", usuario.getApellido());
        valoresParaActualizar.put("DNI", usuario.getDNI());
        valoresParaActualizar.put("pathfoto", usuario.getPathfoto());
        valoresParaActualizar.put("telefono", usuario.getTelefono());
        valoresParaActualizar.put("condicion", usuario.getCondicion());
        valoresParaActualizar.put("estado", usuario.getEstado());
        valoresParaActualizar.put("fechaInscripcion", usuario.getFechaInscripcion());
        valoresParaActualizar.put("fechaVencimiento", usuario.getFechaVencimiento());
        valoresParaActualizar.put("saldo", usuario.getSaldo());
        String campoParaActualizar = "idAut = ?";
        String[] argumentosParaActualizar = {String.valueOf(usuario.getIdAut())};
        return baseDeDatos.update(TABLA_USUARIOS, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }

    public ArrayList<Usuario> obtenerUsuario() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        SQLiteDatabase baseDeDatos = dataBaseHelper.getReadableDatabase();
        String[] columnasAConsultar = {"nombre", "apellido", "DNI","pathfoto","telefono","condicion","estado","fechaInscripcion","fechaVencimiento","saldo","idAut"};
        Cursor cursor = baseDeDatos.query(TABLA_USUARIOS,columnasAConsultar,null,null,null,null,null);
        if (cursor == null) {
            return usuarios;
        }
        if (!cursor.moveToFirst()) return usuarios;
        do {
            Usuario usuario = new Usuario(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getLong(7),cursor.getLong(8),cursor.getInt(9),cursor.getLong(10));
            usuarios.add(usuario);
        } while (cursor.moveToNext());
        cursor.close();
        return usuarios;
    }
    //------------------------BACKUPBANDERA---------------------------------------------------

    public int guardaBandera(String valor) {
        SQLiteDatabase baseDeDatos = dataBaseHelper.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();
        valoresParaActualizar.put("bandera",valor);
        String campoParaActualizar = "id = ?";
        String[] argumentosParaActualizar = {String.valueOf("1")};
        return baseDeDatos.update(TABLA_BANDERA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }

    public String obtenerBandera() {
        String  band = "";
        SQLiteDatabase baseDeDatos = dataBaseHelper.getReadableDatabase();
        String[] columnasAConsultar = {"bandera"};
        Cursor cursor = baseDeDatos.query(TABLA_BANDERA,columnasAConsultar,null,null,null,null,null);
        if (!cursor.moveToFirst()) return band;
        do {
            band = cursor.getString(0);
        } while (cursor.moveToNext());
        cursor.close();
        return band;
    }
}
