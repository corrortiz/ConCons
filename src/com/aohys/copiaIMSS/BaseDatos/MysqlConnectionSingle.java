/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.BaseDatos;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * @author Alejandro Ortiz Corro
 */

public class MysqlConnectionSingle {

    //Variables a utilizar
    
    private static final String DRIVER   =  "com.mysql.jdbc.Driver";
    private static final String JDBC_URL =  "jdbc:mysql://192.168.0.12:3306/copia?&useSSL=false";
    Connection coneccion;
    DataSource data;
    MysqlDataSource my = new MysqlDataSource();
    
  
   /**
     * Conecta a la base de datos
     * @return 
     */
    public Connection conectarBDSingleConnection(){
        try {
            Class.forName(DRIVER);
            this.coneccion = DriverManager.getConnection(JDBC_URL,"corrortiz","calixio2106");
            if (this.coneccion != null) {
                System.out.println("Conectado a Zefry");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MysqlConnectionSingle.class.getName()).log(Level.SEVERE, null, ex);
        }
       return coneccion;
    }
    
    /**
     * Desconecta la base de datos
     */
    public void desconectarBD(){
        try {
            coneccion.close();
            System.out.println("Desconeccion de Zefry");
        } catch (SQLException ex) {
            Logger.getLogger(MysqlConnectionSingle.class.getName()).log(Level.SEVERE, null, ex);
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
