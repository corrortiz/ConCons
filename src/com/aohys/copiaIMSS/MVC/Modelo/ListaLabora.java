/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Alejandro Ortiz Corro
 */

public class ListaLabora {
    private String id_lab;
    private String id_paciente;
    private Date fecha_lab;

    public ListaLabora(String id_lab, String id_paciente, Date fecha_lab) {
        this.id_lab = id_lab;
        this.id_paciente = id_paciente;
        this.fecha_lab = fecha_lab;
    }

    public ListaLabora() {
    }
    
    Hikari dbConn = new Hikari();
    
    /**
     * regresa una lista de laboratoriales para este paciente
     * @param idPaci
     * @return 
     */
    public ObservableList<ListaLabora> listaLab(String idPaci){
        ObservableList<ListaLabora> lista = FXCollections.observableArrayList();
        String sql ="SELECT id_lab, id_paciente,fecha_lab\n"+
                    "FROM laboratorial WHERE id_paciente = '"+idPaci+"'\n"+
                    "ORDER BY fecha_lab DESC;";
        try(Connection conex = dbConn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                lista.add(new ListaLabora(  res.getString ("id_lab"), 
                                            res.getString("fecha_lab"),
                                            res.getDate("fecha_lab")));
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }
    
    
    

    public String getId_lab() {
        return id_lab;
    }

    public void setId_lab(String id_lab) {
        this.id_lab = id_lab;
    }

    public String getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(String id_paciente) {
        this.id_paciente = id_paciente;
    }

    public Date getFecha_lab() {
        return fecha_lab;
    }

    public void setFecha_lab(Date fecha_lab) {
        this.fecha_lab = fecha_lab;
    }
    
    
}
