/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.BaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 * @author Alejandro Ortiz Corro
 */

public class SQLite {
    
    //Variables a utilizar
    private static final String DRIVER   =  "org.sqlite.JDBC";
    private static final String JDBC_URL =  "jdbc:sqlite:"
            + "src/com/aohys/copiaIMSS/BaseDatos/JoonOliver.db";
    Connection coneccion;
    Alert alet = new Alert(Alert.AlertType.INFORMATION);
    
    /**
     * Conecta a la base de datos
     * @return 
     */
    public Connection conectarBD(){
        try {
            Class.forName(DRIVER);
            this.coneccion = DriverManager.getConnection(JDBC_URL);
        } catch (SQLException ex) {
            Logger.getLogger(SQLite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
       return coneccion;
    }
    
    /**
     * Desconecta la base de datos
     */
    public void desconectarBD(){
        try {
            coneccion.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Regresa la coneccion
     * @return 
     */
    public Connection getConnection(){
        return coneccion;
    }
    
    /**
     * Selecciona la coneccion 
     * @param coneccion 
     */
    public void setConnection(Connection coneccion){
        this.coneccion = coneccion;
    }

    
}
