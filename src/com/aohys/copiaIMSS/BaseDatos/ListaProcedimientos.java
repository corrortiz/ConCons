/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.BaseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Alejandro Ortiz Corro
 */

public class ListaProcedimientos {

    //Base de datos
    SQLite conn = new SQLite();
    Connection connection;
    
    //Variables
    private String idListaCie9MC;
    private String nombre_cie9MC;

    /**
     * Constructor lleno
     * @param idListaCie9MC
     * @param nombre_cie9MC 
     */
    public ListaProcedimientos(String idListaCie9MC, String nombre_cie9MC) {
        this.idListaCie9MC = idListaCie9MC;
        this.nombre_cie9MC = nombre_cie9MC;
    }

    /**
    * Realiza la carga de la lista procedimientos
    * @return 
    */
    public ObservableList<String> cargaProcedimientos(){
        ObservableList<String> listaPad = FXCollections.observableArrayList();
        String sql = "Select nombre_cie9MC FROM ListaCie9MC";
        try(Connection conex = conn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
           while (res.next()) {
               listaPad.add(res.getString("nombre_cie9MC")); 
           }
       } catch (SQLException ex) {
           ex.printStackTrace();
       }
    return listaPad;
   }
    
    /**
     * Carga la clave cie09
     * @param Dato
     * @return 
     */
     public String cargaCie9(String Dato){
        String susAct;
        String sql = "select idListaCie9MC from ListaCie9MC\n" +
                    "where nombre_cie9MC = '"+Dato+"'";
        try(Connection conex = conn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
            if (res != null) {
                susAct = res.getString("idListaCie9MC");
                return susAct;
            }else
                return susAct = "";
       } catch (SQLException ex) {
           ex.printStackTrace();
           return susAct = "";
       }
   }
    
    /**
     * Constuctor vacio
     */
    public ListaProcedimientos() {
    }
    
    //Setter and Getters de la clase
    public String getIdListaCie9MC() {
        return idListaCie9MC;
    }

    public void setIdListaCie9MC(String idListaCie9MC) {
        this.idListaCie9MC = idListaCie9MC;
    }

    public String getNombre_cie9MC() {
        return nombre_cie9MC;
    }

    public void setNombre_cie9MC(String nombre_cie9MC) {
        this.nombre_cie9MC = nombre_cie9MC;
    }
    
    
}
