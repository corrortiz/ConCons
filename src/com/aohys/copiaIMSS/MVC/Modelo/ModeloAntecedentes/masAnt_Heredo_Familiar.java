/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Alejandro Ortiz Corro
 */

public class masAnt_Heredo_Familiar {
    //Variables de clase
    private StringProperty id_masHeredo;
    private StringProperty familiarResp_masHeredo;
    private BooleanProperty disfuncion_masHeredo;
    private StringProperty id_paciente;
    private StringProperty familiarInfor_masHeredo;

    /**
     * constructor lleno de la clase
     * @param id_masHeredo
     * @param familiarResp_masHeredo
     * @param disfuncion_masHeredo
     * @param id_paciente 
     * @param familiarInfor_masHeredo 
     */
    public masAnt_Heredo_Familiar(String id_masHeredo, String familiarResp_masHeredo, 
            boolean disfuncion_masHeredo, String id_paciente, String familiarInfor_masHeredo) {
        this.id_masHeredo = new SimpleStringProperty(id_masHeredo);
        this.familiarResp_masHeredo = new SimpleStringProperty(familiarResp_masHeredo);
        this.disfuncion_masHeredo = new SimpleBooleanProperty(disfuncion_masHeredo);
        this.id_paciente = new SimpleStringProperty(id_paciente);
        this.familiarInfor_masHeredo = new SimpleStringProperty(familiarInfor_masHeredo);
    }

    /**
     * constructor vacio de la clase
     */
    public masAnt_Heredo_Familiar() {
    }

    /**
     * Agrega mas antecedentes heredo familiares
     * @param id_masHeredo
     * @param familiarResp_masHeredo
     * @param disfuncion_masHeredo
     * @param id_paciente
     * @param conex
     * @param familiarInfor_masHeredo      */
    public void agregarMasHeredoFami(String id_masHeredo, String familiarResp_masHeredo, 
            boolean disfuncion_masHeredo, String id_paciente, Connection conex, String familiarInfor_masHeredo){
        String sqlst =  "INSERT INTO masant_heredo_familiar (id_masHeredo, familiarResp_masHeredo,\n"+
                        "disfuncion_masHeredo, id_paciente, familiarInfor_masHeredo)\n"+
                        "VALUES (?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_masHeredo);
            sttm.setString  (2, familiarResp_masHeredo);
            sttm.setBoolean (3, disfuncion_masHeredo);
            sttm.setString  (4, id_paciente);
            sttm.setString  (5, familiarInfor_masHeredo);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * carga los mas antecedentes heredo familiares del paciente seleccionado
     * @param idPaciente
     * @param conex
     * @return 
     */
    public masAnt_Heredo_Familiar cargaSoloUno(String idPaciente, Connection conex){
        masAnt_Heredo_Familiar ant_Heredo_Familiar = null;
        String sttm = "SELECT id_masHeredo, familiarResp_masHeredo,\n"+
                      "disfuncion_masHeredo, id_paciente, familiarInfor_masHeredo \n" +
                      "FROM masant_heredo_familiar WHERE id_paciente = '"+idPaciente+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                ant_Heredo_Familiar = new masAnt_Heredo_Familiar( 
                                        res.getString ("id_masHeredo"), 
                                        res.getString ("familiarResp_masHeredo"),
                                        res.getBoolean("disfuncion_masHeredo"), 
                                        res.getString ("id_paciente"),
                                        res.getString ("familiarInfor_masHeredo"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ant_Heredo_Familiar;
    }
    
    /**
     * actualiza los datos de mas antecedentes heredo del paciente seleccionado
     * @param id_masHeredo
     * @param familiarResp_masHeredo
     * @param disfuncion_masHeredo
     * @param id_paciente
     * @param conex 
     * @param familiarInfor_masHeredo 
     */
    public void actualizaMasAnteHeredo(String id_masHeredo, String familiarResp_masHeredo, 
            boolean disfuncion_masHeredo, String id_paciente, Connection conex, String familiarInfor_masHeredo){
        String sqlst = " UPDATE masant_heredo_familiar SET \n" +
                       " id_masHeredo=?, \n" +
                       " familiarResp_masHeredo=?, \n" +
                       " disfuncion_masHeredo=?, \n" +
                       " id_paciente=?, \n" +
                       " familiarInfor_masHeredo=? \n" +
                       " WHERE id_masHeredo=?";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_masHeredo);
            sttm.setString  (2, familiarResp_masHeredo);
            sttm.setBoolean (3, disfuncion_masHeredo);
            sttm.setString  (4, id_paciente);
            sttm.setString  (5, familiarInfor_masHeredo);
            sttm.setString  (6, id_masHeredo);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /***************************************************************/
    //Setters and getters
    public final void setId_masHeredo(String value) {
        id_masHeredo.set(value);
    }

    public final String getId_masHeredo() {
        return id_masHeredo.get();
    }

    public final StringProperty id_masHeredoProperty() {
        return id_masHeredo;
    }

    public final void setFamiliarResp_masHeredo(String value) {
        familiarResp_masHeredo.set(value);
    }

    public final String getFamiliarResp_masHeredo() {
        return familiarResp_masHeredo.get();
    }

    public final StringProperty familiarResp_masHeredoProperty() {
        return familiarResp_masHeredo;
    }

    public final void setDisfuncion_masHeredo(Boolean value) {
        disfuncion_masHeredo.set(value);
    }

    public final Boolean getDisfuncion_masHeredo() {
        return disfuncion_masHeredo.get();
    }

    public final BooleanProperty disfuncion_masHeredoProperty() {
        return disfuncion_masHeredo;
    }

    public final void setId_paciente(String value) {
        id_paciente.set(value);
    }

    public final String getId_paciente() {
        return id_paciente.get();
    }

    public final StringProperty id_pacienteProperty() {
        return id_paciente;
    }

    public final void setFamiliarInfor_masHeredo(String value) {
        familiarInfor_masHeredo.set(value);
    }

    public final String getFamiliarInfor_masHeredo() {
        return familiarInfor_masHeredo.get();
    }

    public final StringProperty familiarInfor_masHeredoProperty() {
        return familiarInfor_masHeredo;
    }
    
    
    
    
    
}
