/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes;

import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Alejandro Ortiz Corro
 */

public class patoMedicos {
    //Variables de clase
    private StringProperty id_pMed;
    private IntegerProperty edad_pMed;
    private StringProperty duracion_pMed;
    private StringProperty enfermedad_pMed;
    private StringProperty id_paciente;
    //Variables auxiliares
    Auxiliar aux = new Auxiliar();

    /**
     * constructor lleno
     * @param id_pMed
     * @param edad_pMed
     * @param duracion_pMed
     * @param enfermedad_pMed
     * @param id_paciente 
     */
    public patoMedicos(String id_pMed, int edad_pMed, 
            String duracion_pMed, String enfermedad_pMed, String id_paciente) {
        this.id_pMed = new SimpleStringProperty(id_pMed);
        this.edad_pMed = new SimpleIntegerProperty(edad_pMed);
        this.duracion_pMed = new SimpleStringProperty(duracion_pMed);
        this.enfermedad_pMed = new SimpleStringProperty(enfermedad_pMed);
        this.id_paciente = new SimpleStringProperty(id_paciente);
    }

    /**
     * construcor vacio
     */
    public patoMedicos() {
    }

    
    /**
     * agrega a la base de datos antecedentes patologicos medicos
     * @param id_pMed
     * @param edad_pMed
     * @param duracion_pMed
     * @param enfermedad_pMed
     * @param id_paciente
     * @param conex 
     */
    public void agregaPatoMedico(String id_pMed, int edad_pMed, 
            String duracion_pMed, String enfermedad_pMed, String id_paciente, Connection conex){
        String sqlst =  "INSERT INTO `patomedicos`\n" +
                        "(`id_pMed`,\n" +
                        "`edad_pMed`,\n" +
                        "`duracion_pMed`,\n" +
                        "`enfermedad_pMed`,\n" +
                        "`id_paciente`)\n" +
                        "VALUES (?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_pMed);
            sttm.setInt     (2, edad_pMed);
            sttm.setString  (3, duracion_pMed);
            sttm.setString  (4, enfermedad_pMed);
            sttm.setString  (5, id_paciente);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Antecedentes médicos guardados", 
                        "Antecedentes médicos guardados", 
                    "Antecedentes médicos han sido guardados exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * actualiza antecedentes patologicos medicos del paciente seleccionado
     * @param id_pMed
     * @param edad_pMed
     * @param duracion_pMed
     * @param enfermedad_pMed
     * @param id_paciente
     * @param conex 
     */
    public void actualizaPatoMedico(String id_pMed, int edad_pMed, 
            String duracion_pMed, String enfermedad_pMed, String id_paciente, Connection conex){
        String sqlst =  "UPDATE `patomedicos`\n" +
                        "SET\n" +
                        "`id_pMed` = ?,\n" +
                        "`edad_pMed` = ?,\n" +
                        "`duracion_pMed` = ?,\n" +
                        "`enfermedad_pMed` = ?,\n" +
                        "`id_paciente` = ?,\n" +
                        "WHERE `id_pMed` = ?;";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_pMed);
            sttm.setInt     (2, edad_pMed);
            sttm.setString  (3, duracion_pMed);
            sttm.setString  (4, enfermedad_pMed);
            sttm.setString  (5, id_paciente);
            sttm.setString  (5, id_pMed);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Antecedentes médicos modificados", 
                    "Antecedentes médicos modificados", 
                    "Antecedentes médicos han sido modificados exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * regresa una lista de padecimientos patologicos medicos del paciente seleccionado
     * @param conex
     * @param idPaciente
     * @return 
     */
    public ObservableList<patoMedicos> listaAntePatoMedico(Connection conex, String idPaciente){
        ObservableList<patoMedicos> listaPatoMedica = FXCollections.observableArrayList();
        String sql = "SELECT `id_pMed`,\n" +
                        "`edad_pMed`,\n" +
                        "`duracion_pMed`,\n" +
                        "`enfermedad_pMed`,\n" +
                        "`id_paciente`\n" +
                     "FROM `patomedicos` WHERE id_paciente = '"+idPaciente+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaPatoMedica.add(new patoMedicos( 
                                    res.getString("id_pMed"), 
                                    res.getInt("edad_pMed"), 
                                    res.getString("duracion_pMed"), 
                                    res.getString("enfermedad_pMed"), 
                                    res.getString("id_paciente")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaPatoMedica;
    }
    
    /**
     * borra la enfermedad seleccionada 
     * @param Dato
     * @param conex 
     */
    public void borrarAntePatoMedico(String Dato, Connection conex){
        String sttm = "DELETE FROM patomedicos WHERE id_pMed = '"+Dato+"'";
        try(PreparedStatement stta = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            stta.addBatch();
            stta.executeBatch();
            conex.commit();
            aux.informacionUs("La enfermedad ha sido borrada", 
                            "La enfermedad ha sido borrada", 
                            "La enfermedad ha sido borrada ha sido borrada exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
/*************************************************************************/
    //Setters and Getters
    public final void setId_pMed(String value) {
        id_pMed.set(value);
    }

    public final String getId_pMed() {
        return id_pMed.get();
    }

    public final StringProperty id_pMedProperty() {
        return id_pMed;
    }

    public final void setEdad_pMed(Integer value) {
        edad_pMed.set(value);
    }

    public final Integer getEdad_pMed() {
        return edad_pMed.get();
    }

    public final IntegerProperty edad_pMedProperty() {
        return edad_pMed;
    }

    public final void setDuracion_pMed(String value) {
        duracion_pMed.set(value);
    }

    public final String getDuracion_pMed() {
        return duracion_pMed.get();
    }

    public final StringProperty duracion_pMedProperty() {
        return duracion_pMed;
    }

    public final void setEnfermedad_pMed(String value) {
        enfermedad_pMed.set(value);
    }

    public final String getEnfermedad_pMed() {
        return enfermedad_pMed.get();
    }

    public final StringProperty enfermedad_pMedProperty() {
        return enfermedad_pMed;
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
    
    
    
    
}
