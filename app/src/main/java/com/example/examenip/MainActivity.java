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

public class MainActivity extends AppCompatActivity {

    SQLiteConexion conexion;
    Spinner cmbPaises;
    EditText txtNombreContacto, txtTelefono, txtNota;
    ArrayList<String> listaPaises;
    ArrayList<Country> paises;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conexion = new SQLiteConexion(this, Datos.NameDataBase, null, 1);
        cmbPaises = (Spinner) findViewById(R.id.cmbPaisesBuscado);

        //LLENADO DE CMB
        ObtenerListaPaises();
        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaPaises);
        cmbPaises.setAdapter(adp);



        Button btnListaContactos = (Button) findViewById(R.id.btnlista);
        btnListaContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityListaContactos.class);
                startActivity(intent);
            }
        });

        Button btnagregarpais= (Button) findViewById(R.id.btnpais);
        btnagregarpais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityAddCountry.class);
                startActivity(intent);
            }
        });

        //BOTON SALVAR CONTACTO
        Button btnSalvarContacto = (Button) findViewById(R.id.btnguardar);
        txtNombreContacto = (EditText) findViewById(R.id.txtnombre);
        txtTelefono = (EditText) findViewById(R.id.txttelefono);
        txtNota = (EditText) findViewById(R.id.txtnota);
        btnSalvarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarContacto();
            }
        });
    }

    private void AgregarContacto() {
        int numeros = 0;
        if(txtNombreContacto.getText().toString().isEmpty() || txtTelefono.getText().toString().isEmpty()) {
            mostrarDialogoVacios();
        } else {
            for (int i = 0; i < txtNombreContacto.getText().toString().length(); i++) {
                if (Character.isDigit(txtNombreContacto.getText().toString().charAt(i))) {
                    mostrarDialogoNumeros();
                    numeros = 1;
                    break;
                }
            }

            if (numeros == 0) {
                SQLiteConexion conexion = new SQLiteConexion(this, Datos.NameDataBase, null, 1);
                SQLiteDatabase db = conexion.getWritableDatabase();

                ContentValues valores = new ContentValues();
                valores.put(Datos.nombreContacto, txtNombreContacto.getText().toString());
                valores.put(Datos.telefonoContacto, txtTelefono.getText().toString());
                valores.put(Datos.pais, (cmbPaises.getSelectedItemId() + 1));
                valores.put(Datos.nota, txtNota.getText().toString());

                Long resultado = db.insert(Datos.tablaContactos, Datos.idContacto, valores);
                Toast.makeText(getApplicationContext(), "Contacto Guardado: " + resultado.toString(), Toast.LENGTH_LONG).show();
                db.close();
                ClearScreen();
            }
        }
    }

    private void ClearScreen() {
        txtNombreContacto.setText("");
        txtTelefono.setText("");
        txtNota.setText("");
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

}