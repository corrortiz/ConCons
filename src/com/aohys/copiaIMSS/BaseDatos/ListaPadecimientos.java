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

public class ListaPadecimientos{
    //Base de datos
    SQLite conn = new SQLite();
    Connection connection;

    //Variables
    private String idListaCie10;
    private String nombre_cie10;
    private ListaPadecimientos pad;
    
    /**
     * Constructor lleno
     * @param idListaCie10
     * @param nombre_cie10 
     */
    public ListaPadecimientos(String idListaCie10, String nombre_cie10) {
        this.idListaCie10 = idListaCie10;
        this.nombre_cie10 = nombre_cie10;
    }
    
    /**
     * Constructor Vacio
     */
    public ListaPadecimientos() {
    }

    
    /**
    * Realiza la carga de la lista padecimientos
    * @return 
    */
    public ObservableList<String> cargaTabla(){
        ObservableList<String> listaPad = FXCollections.observableArrayList();
        String sql = "Select nombre_cie10, idListaCie10 FROM ListaCie10";
        try(Connection conex = conn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaPad.add((res.getString("idListaCie10")+" - "+res.getString("nombre_cie10"))); 
            }
       } catch (SQLException ex) {
           ex.printStackTrace();
       }
    return listaPad;
   }
    
    /**
    * Realiza la carga de la lista padecimientos
    * @return 
    */
    public ObservableList<ListaPadecimientos> listatodo(){
        ObservableList<ListaPadecimientos> listaCompleta = FXCollections.observableArrayList();
        String sql = "Select idListaCie10, nombre_cie10 FROM ListaCie10";
        try(Connection conex = conn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaCompleta.add(new ListaPadecimientos(    
                                    res.getString("idListaCie10"),
                                    res.getString("nombre_cie10")));
           }
       } catch (SQLException ex) {
           ex.printStackTrace();
       }
    return listaCompleta;
   }

    
    /**
     * Setters and Getters
     * @return 
     */
    public String getIdListaCie10() {
        return idListaCie10;
    }

    public void setIdListaCie10(String idListaCie10) {
        this.idListaCie10 = idListaCie10;
    }

    public String getNombre_cie10() {
        return nombre_cie10;
    }

    public void setNombre_cie10(String nombre_cie10) {
        this.nombre_cie10 = nombre_cie10;
    }

   
    
}
