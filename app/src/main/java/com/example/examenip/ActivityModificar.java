package com.example.examenip;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.examenip.Registros.Datos;
import com.example.examenip.Tablas.Country;

import java.util.ArrayList;

public class ActivityModificar extends AppCompatActivity {

    SQLiteConexion conexion;
    private String idCont, nombre, telefono, pais, notas;
    EditText txtIdBuscado, txtNombre, txtTelefono, txtNota;
    Spinner cmbPaisBuscado;
    ArrayList<String> listaPaises;
    ArrayList<Country> paises;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        conexion = new SQLiteConexion(this, Datos.NameDataBase, null, 1);

        Button btncontactoElim = (Button) findViewById(R.id.btneliminarcontacto);
        Button btnContactoActualizar = (Button) findViewById(R.id.btnactualizarcontacto);

        txtIdBuscado = (EditText) findViewById(R.id.txtidactu);
        txtNombre = (EditText) findViewById(R.id.txtnombreactu);
        txtTelefono = (EditText) findViewById(R.id.txttelefonoactu);
        txtNota = (EditText) findViewById(R.id.txtnotaactu);
        cmbPaisBuscado = (Spinner) findViewById(R.id.cmbPaisesBuscado);

        //LLENADO DE COMBOBOX
        ObtenerListaPaises();
        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaPaises);
        cmbPaisBuscado.setAdapter(adp);

        Intent intent = getIntent();
        idCont = intent.getStringExtra("idCont");
        txtIdBuscado.setText(idCont);
        txtIdBuscado.setKeyListener(null);
        BuscarContacto();

        btncontactoElim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarContacto();
            }
        });

        btnContactoActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarContacto();
            }
        });

        Button btnvolver = (Button) findViewById(R.id.btnvolvermodificar);
        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityListaContactos.class);
                startActivity(intent);
            }
        });
    }

    private void BuscarContacto() {
        SQLiteDatabase db = conexion.getWritableDatabase();

        //PARAMETROS DE CONFIGURACIÓN DE LA SENTENCIA SELECT
        String [] params = {idCont}; //PARAMETRO DE LA BÚSQUEDA
        String [] fields = {Datos.nombreContacto,
                Datos.telefonoContacto,
                Datos.pais,
                Datos.nota};
        String wherecond = Datos.idContacto + "=?";

        try{
            Cursor cdata = db.query(Datos.tablaContactos, fields, wherecond, params, null, null, null);
            cdata.moveToFirst();

            txtNombre.setText(cdata.getString(0));
            txtTelefono.setText(cdata.getString(1));
            cmbPaisBuscado.setSelection(Integer.valueOf(cdata.getString(2)) - 1);
            txtNota.setText(cdata.getString(3));

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Elemento no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void ActualizarContacto() {
        int numeros = 0;
        if(txtNombre.getText().toString().isEmpty() || txtTelefono.getText().toString().isEmpty()) {
            mostrarDialogoVacios();
        } else {
            for (int i = 0; i < txtNombre.getText().toString().length(); i++) {
                if (Character.isDigit(txtNombre.getText().toString().charAt(i))) {
                    mostrarDialogoNumeros();
                    numeros = 1;
                    break;
                }
            }

            if (numeros == 0) {
                SQLiteDatabase db = conexion.getWritableDatabase();
                String [] params = {idCont}; //Parametro de Busqueda

                ContentValues valores = new ContentValues();
                valores.put(Datos.nombreContacto, txtNombre.getText().toString());
                valores.put(Datos.telefonoContacto, txtTelefono.getText().toString());
                valores.put(Datos.pais, (cmbPaisBuscado.getSelectedItemId() + 1));
                valores.put(Datos.nota, txtNota.getText().toString());

                db.update(Datos.tablaContactos, valores, Datos.idContacto + "=?", params);
                Toast.makeText(getApplicationContext(), "Dato Actualizado", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), ActivityListaContactos.class);
                startActivity(intent);
            }
        }

    }

    private void EliminarContacto() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación de Eliminación")
                .setMessage("¿Desea eliminar el contacto de " + txtNombre.getText() + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase db = conexion.getWritableDatabase();
                        String [] params = {idCont}; //Parametro de Busqueda

                        db.delete(Datos.tablaContactos, Datos.idContacto + "=?", params);
                        Toast.makeText(getApplicationContext(), "Dato Eliminado", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), ActivityListaContactos.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Se canceló la eliminación", Toast.LENGTH_LONG).show();
                    }
                }).show();
    }

    private void ObtenerListaPaises() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Country pais = null;

        paises = new ArrayList<Country>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Datos.tablaPaises, null);

        while (cursor.moveToNext()){
            pais = new Country();
            pais.setIdPais(cursor.getInt(0));
            pais.setNombrePais(cursor.getString(1));
            pais.setCodigoMarcado(cursor.getInt(2));

            paises.add(pais);
        }

        fillComb();
        cursor.close();
    }
    private void fillComb() {
        listaPaises = new ArrayList<String>();
        for(int i = 0; i < paises.size(); i++){
            listaPaises.add(paises.get(i).getNombrePais() + "  ("
                    + paises.get(i).getCodigoMarcado() + ")");
        }
    }

    private void mostrarDialogoVacios() {
        new AlertDialog.Builder(this)
                .setTitle("Alerta de Vacíos")
                .setMessage("No puede dejar ningún campo vacío")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void mostrarDialogoNumeros() {
        new AlertDialog.Builder(this)
                .setTitle("Alerta de Números")
                .setMessage("No puede ingresar números en el campo de Nombre")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }
}