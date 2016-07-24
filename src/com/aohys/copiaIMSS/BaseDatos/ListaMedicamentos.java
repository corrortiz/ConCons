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

public class ListaMedicamentos {
    //Base de datos
    SQLite conn = new SQLite();
    Connection connection;

    //Variables
    private String idListaMedicamentos;
    private String nombre_medicamento;
    private String sustanciaActiva_medicamento;

    /**
     * Constructor lleno
     * @param idListaMedicamentos
     * @param nombre_medicamento
     * @param sustanciaActiva_medicamento 
     */
    public ListaMedicamentos(String idListaMedicamentos, String nombre_medicamento, 
            String sustanciaActiva_medicamento) {
        this.idListaMedicamentos = idListaMedicamentos;
        this.nombre_medicamento = nombre_medicamento;
        this.sustanciaActiva_medicamento = sustanciaActiva_medicamento;
    }

    /**
     * Constructor vacio
     */
    public ListaMedicamentos() {
    }

    /**
    * Realiza la carga de la lista padecimientos
    * @return 
    */
    public ObservableList<String> cargaListaMedNom(){
        ObservableList<String> listamedNom = FXCollections.observableArrayList();
        String sqlS = "Select idListaMedicamentos, nombre_medicamento"
                + ", sustanciaActiva_medicamento FROM ListaMedicamentos";
        try(Connection conex = conn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sqlS);
                ResultSet res = stta.executeQuery();) {
           while (res.next()) {
               listamedNom.add(new String(res.getString("nombre_medicamento"))); 
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    return listamedNom;
   }
    
    /**
     * Carla lo otro
     * @param Dato
     * @return 
     */
    public String cargaListMetSus(String Dato){
        String susAct;
        String sql = "select sustanciaActiva_medicamento from ListaMedicamentos\n" +
                    "where nombre_medicamento = '"+Dato+"'";
        try(Connection conex = conn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
            if (res!= null) {
                susAct = res.getString("sustanciaActiva_medicamento");
            }else
                susAct = "";
            return susAct;
       } catch (SQLException ex) {
           ex.printStackTrace();
           return susAct="";
       }
   }
    
    //Setter and getters
    public String getIdListaMedicamentos() {
        return idListaMedicamentos;
    }

    public void setIdListaMedicamentos(String idListaMedicamentos) {
        this.idListaMedicamentos = idListaMedicamentos;
    }

    public String getNombre_medicamento() {
        return nombre_medicamento;
    }

    public void setNombre_medicamento(String nombre_medicamento) {
        this.nombre_medicamento = nombre_medicamento;
    }

    public String getSustanciaActiva_medicamento() {
        return sustanciaActiva_medicamento;
    }

    public void setSustanciaActiva_medicamento(String sustanciaActiva_medicamento) {
        this.sustanciaActiva_medicamento = sustanciaActiva_medicamento;
    }
    
    
}
