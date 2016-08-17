/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo.ModeloCita;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Alejandro Ortiz Corro
 */

public class diaLibre {
    //Variables de clase
    private StringProperty id_diaLibre;
    private Date fecha_diaLibre;
    private StringProperty id_medico;
    //Variables auxiliares
    Auxiliar aux = new Auxiliar();
    Hikari dbConn = new Hikari();
    /**
     * Constructor lleno de la clase dia libre de medico
     * @param id_diaLibre
     * @param fecha_diaLibre
     * @param id_medico 
     */
    public diaLibre(String id_diaLibre, Date fecha_diaLibre, String id_medico) {
        this.id_diaLibre = new SimpleStringProperty(id_diaLibre);
        this.fecha_diaLibre = fecha_diaLibre;
        this.id_medico = new SimpleStringProperty(id_medico);
    }

    /**
     * Constuctor vacio de la clase dia libre de medico
     */
    public diaLibre() {
    }

    /**
     * Agrega dia libre de medico
     * @param id_diaLibre
     * @param fecha_diaLibre
     * @param id_medico
     * @param conex 
     */
    public void agregaDiaLibre(String id_diaLibre, Date fecha_diaLibre, String id_medico, 
            Connection conex){
        String sqlst =  "INSERT INTO dialibre (id_diaLibre, fecha_diaLibre,\n"+
                        "id_medico)"+
                        "VALUES (?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_diaLibre);
            sttm.setDate    (2, fecha_diaLibre);
            sttm.setString  (3, id_medico);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Carga lista de dias libres del medico seleccionado 
     * @param idUs
     * @return 
     */
    public ObservableList<diaLibre> listaDiasLibresMedico(String idUs){
        ObservableList<diaLibre> listaDiaLibre = FXCollections.observableArrayList();
        String sql ="SELECT id_diaLibre, fecha_diaLibre,\n"+
                    "id_medico\n" +
                    "FROM dialibre WHERE id_medico = '"+idUs+"'" +
                    "ORDER BY fecha_diaLibre ASC;";
        try(Connection conex = dbConn.conectarBD();
            PreparedStatement stta = conex.prepareStatement(sql);
            ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaDiaLibre.add(new diaLibre( res.getString ("id_diaLibre"), 
                                                res.getDate   ("fecha_diaLibre"),
                                                res.getString("id_medico")));
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listaDiaLibre;
    }
    
    /**
     * borra el dia libre seleccionado
     * @param Dato
     * @param conex 
    */
    public void borrarDiaLibre(String Dato, Connection conex){
        String sttm = "DELETE FROM dialibre WHERE id_diaLibre = '"+Dato+"'";    
        try(PreparedStatement pttm = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            pttm.addBatch();
            pttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /*****************************************/
    //Setters and Getters
    public Date getFecha_diaLibre() {    
        return fecha_diaLibre;
    }

    public void setFecha_diaLibre(Date fecha_diaLibre) {
        this.fecha_diaLibre = fecha_diaLibre;
    }

    public final void setId_diaLibre(String value) {
        id_diaLibre.set(value);
    }

    public final String getId_diaLibre() {
        return id_diaLibre.get();
    }

    public final StringProperty id_diaLibreProperty() {
        return id_diaLibre;
    }

    public final void setId_medico(String value) {
        id_medico.set(value);
    }

    public final String getId_medico() {
        return id_medico.get();
    }

    public final StringProperty id_medicoProperty() {
        return id_medico;
    }

}
