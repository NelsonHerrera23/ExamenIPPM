package com.example.examenip;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.examenip.Registros.Datos;

public class ActivityAddCountry extends AppCompatActivity {

    EditText txtNombrePais, txtCodigoMarcado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_country);

        Button btnSalvarPais = (Button) findViewById(R.id.btnañadir);
        Button btnvolver = (Button) findViewById(R.id.btnvolverpais);
        txtNombrePais = (EditText) findViewById(R.id.txtañadirpais);
        txtCodigoMarcado = (EditText) findViewById(R.id.txtprefijo);
        btnvolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnSalvarPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarPais();
            }
        });
    }


        private void AgregarPais() {
            int numeros = 0;
            if(txtNombrePais.getText().toString().isEmpty() || txtCodigoMarcado.getText().toString().isEmpty()) {
                mostrarDialogoVacios();
            } else {
                for( int i = 0; i < txtNombrePais.getText().toString().length(); i++ ) {
                    if (Character.isDigit(txtNombrePais.getText().toString().charAt(i))) {
                        mostrarDialogoNumeros();
                        numeros = 1;
                        break;
                    }
                }

                if(numeros == 0) {
                    SQLiteConexion conexion = new SQLiteConexion(this, Datos.NameDataBase, null, 1);
                    SQLiteDatabase db = conexion.getWritableDatabase();

                    ContentValues valores = new ContentValues();
                    valores.put(Datos.nombrePais, txtNombrePais.getText().toString());
                    valores.put(Datos.codigoMarcado, txtCodigoMarcado.getText().toString());

                    Long resultado = db.insert(Datos.tablaPaises, Datos.idPais, valores);
                    Toast.makeText(getApplicationContext(), "País Añadido: " + resultado.toString(), Toast.LENGTH_LONG).show();
                    db.close();
                    ClearScreen();
                }
            }

        }

        private void ClearScreen() {
            txtNombrePais.setText("");
            txtCodigoMarcado.setText("");
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
                    .setMessage("No puede ingresar números en el campo de Nombre del País")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }


}