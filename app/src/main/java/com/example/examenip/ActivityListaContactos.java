package com.example.examenip;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.examenip.Registros.Datos;
import com.example.examenip.Tablas.Contactos;

import java.util.ArrayList;

public class ActivityListaContactos extends AppCompatActivity {

    //VARIABLES GLOBALES DE LA ACTIVIDAD
    SQLiteConexion conexion;
    ListView listacontactos;
    ArrayList<Contactos> lista;
    ArrayList<String> arregloContactos;
    private String  idCont, telefono, nombre, pais, nota;

    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        Button btnLlamar = (Button) findViewById(R.id.btnllamar);
        Button btnvolver = (Button) findViewById(R.id.btnvolverlista);
        EditText txtBuscar = (EditText) findViewById(R.id.txtbuscar);
        listacontactos = (ListView) findViewById(R.id.lstContactos);

        conexion = new SQLiteConexion(this, Datos.NameDataBase, null, 1);
        listacontactos = (ListView) findViewById(R.id.lstContactos);

        //OBTENER LISTADO
        ObtenerListaContactos();
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arregloContactos);
        listacontactos.setAdapter(adp);

        //BUSCADOR ONCHANGE
        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adp.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //BOTON LLAMAR
        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idCont != null){
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Confirmación de Llamada")
                            .setMessage("¿Desea llamar a " + nombre + "?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(ActivityCompat.checkSelfPermission(ActivityListaContactos.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        Toast.makeText(getApplicationContext(), "Llamando en breve ", Toast.LENGTH_LONG).show();
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:+" + telefono));
                                        startActivity(callIntent);
                                    }else{
                                        ActivityCompat.requestPermissions(ActivityListaContactos.this, new String[]{ Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }else{
                    mostrarDialogoSeleccion();
                }

            }
        });

        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //BOTON EDITAR CONTACTO
        Button btnactualizar = (Button) findViewById(R.id.btnactualizar);
        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(idCont != null){
                    Intent intent = new Intent(getApplicationContext(), ActivityModificar.class);
                    intent.putExtra("idCont", String.valueOf(idCont));
                    startActivity(intent);
                }else{
                    mostrarDialogoSeleccion();
                }
            }
        });

        listacontactos.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                idCont = lista.get(position).getIdContacto().toString();
                nombre = lista.get(position).getNombreContacto();
                pais = lista.get(position).getPais().toString();

                telefono = obtenerCodigoMarcado(pais);

                telefono = telefono + lista.get(position).getTelefonoContacto().toString();

                Toast.makeText(getApplicationContext(), "Ha seleccionado a: "+nombre, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void ObtenerListaContactos(){
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos listaContactos = null;
        lista = new ArrayList<Contactos>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+Datos.tablaContactos, null);

        while (cursor.moveToNext()){
            listaContactos = new Contactos();
            listaContactos.setIdContacto(cursor.getInt(0));
            listaContactos.setNombreContacto(cursor.getString(1));
            listaContactos.setTelefonoContacto(cursor.getInt(2));
            listaContactos.setPais(cursor.getInt(3));
            listaContactos.setNota(cursor.getString(4));

            lista.add(listaContactos);
        }
        fillList();
    }

    private void fillList(){
        arregloContactos = new ArrayList<String>();

        SQLiteDatabase db = conexion.getReadableDatabase();
        for(int i = 0; i<lista.size(); i++){

            String [] params = {lista.get(i).getPais().toString()}; //Parametro de Busqueda
            String [] fields = {Datos.idPais,Datos.nombrePais};
            String whereCon = Datos.idPais + "=?";

            Cursor cData = db.query(Datos.tablaPaises, fields, whereCon, params, null, null, null, null);
            cData.moveToFirst();

            arregloContactos.add(cData.getString(1)+" | "+
                    lista.get(i).getNombreContacto()+" | "+
                    lista.get(i).getTelefonoContacto());
            cData.close();
        }
    }

    private void mostrarDialogoSeleccion() {
        new AlertDialog.Builder(this)
                .setTitle("Alerta de Selección")
                .setMessage("Seleccione un contacto de la lista")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private String obtenerCodigoMarcado(String idPais) {
        String codigoMarcado;

        SQLiteDatabase db = conexion.getReadableDatabase();

        String [] params = {idPais}; //Parametro de Busqueda
        String [] fields = {Datos.idPais,Datos.codigoMarcado};
        String whereCon = Datos.idPais + "=?";

        Cursor cData = db.query(Datos.tablaPaises, fields, whereCon, params, null, null, null, null);
        cData.moveToFirst();

        codigoMarcado = String.valueOf(cData.getInt(1));

        cData.close();

        return codigoMarcado;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Llamando en breve ", Toast.LENGTH_LONG).show();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+" + telefono));
                    startActivity(callIntent);
                } else {

                }
                return;
            }
        }
    }
}


