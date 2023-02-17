package com.example.examenip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.examenip.Registros.Datos;

public class SQLiteConexion extends SQLiteOpenHelper{



    public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Datos.CreateTablePaises);
        db.execSQL(Datos.CreateTableContactos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Datos.DropeTablePaises);
        onCreate(db);
        db.execSQL(Datos.DropTableContactos);
        onCreate(db);
    }
}
