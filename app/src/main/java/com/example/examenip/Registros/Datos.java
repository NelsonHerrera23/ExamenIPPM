package com.example.examenip.Registros;

public class Datos {

    /* Tablas */
    public static final String tablaPaises = "paises";
    public static final String tablaContactos = "contactos";

    /* Campos */
    public static final String idPais = "idPais";
    public static final String nombrePais = "nombrePais";
    public static final String codigoMarcado = "codigoMarcado";

    public static final String idContacto = "idContacto";
    public static final String nombreContacto = "nombreContacto";
    public static final String telefonoContacto = "telefonoContacto";
    public static final String pais = "pais";
    public static final String nota = "nota";

    /* CRUD */
    public static final String CreateTablePaises =
            "CREATE TABLE paises(idPais INTEGER PRIMARY KEY AUTOINCREMENT, nombrePais TEXT, codigoMarcado INTEGER)";
    public static final String DropeTablePaises =
            "DROPE TABLE IF EXISTS paises";

    public static final String CreateTableContactos =
            "CREATE TABLE contactos( idContacto INTEGER PRIMARY KEY AUTOINCREMENT, nombreContacto TEXT, telefonoContacto INTEGER, pais TEXT, nota TEXT)";

    public static final String DropTableContactos =
            "DROP TABLE IF EXITS contactos";

    /* Base de Datos */
    public static final String NameDataBase = "DBExamen1";
}
