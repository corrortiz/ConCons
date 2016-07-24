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
public class patoTraumaticos {
    //Variables de clase
    private StringProperty id_pTrauma;
    private IntegerProperty edad_pTrauma;
    private StringProperty duracion_pTrauma;
    private StringProperty trauma_pTrauma;
    private StringProperty lado_pTrauma;
    private StringProperty secuelas_pTrauma;
    private StringProperty id_paciente;
    //Variables
    Auxiliar aux = new Auxiliar();

    /**
     * constructor vacio
     * @param id_pTrauma
     * @param edad_pTrauma
     * @param duracion_pTrauma
     * @param trauma_pTrauma
     * @param lado_pTrauma
     * @param secuelas_pTrauma
     * @param id_paciente 
     */
    public patoTraumaticos(String id_pTrauma, int edad_pTrauma, String duracion_pTrauma, String trauma_pTrauma, 
            String lado_pTrauma, String secuelas_pTrauma, String id_paciente) {
        this.id_pTrauma = new SimpleStringProperty(id_pTrauma);
        this.edad_pTrauma = new SimpleIntegerProperty(edad_pTrauma);
        this.duracion_pTrauma = new SimpleStringProperty(duracion_pTrauma);
        this.trauma_pTrauma = new SimpleStringProperty(trauma_pTrauma);
        this.lado_pTrauma = new SimpleStringProperty(lado_pTrauma);
        this.secuelas_pTrauma = new SimpleStringProperty(secuelas_pTrauma);
        this.id_paciente = new SimpleStringProperty(id_paciente);
    }

    /**
     * constructor vacio
     */
    public patoTraumaticos() {
    }

    /**
     * agrega antecedentes traumaticos 
     * @param id_pTrauma
     * @param edad_pTrauma
     * @param duracion_pTrauma
     * @param trauma_pTrauma
     * @param lado_pTrauma
     * @param secuelas_pTrauma
     * @param id_paciente
     * @param conex 
     */
    public void agregaPatoTrauma(String id_pTrauma, int edad_pTrauma, String duracion_pTrauma, String trauma_pTrauma, 
            String lado_pTrauma, String secuelas_pTrauma, String id_paciente, Connection conex){
        String sqlst =  "INSERT INTO `patotraumaticos`\n" +
                            "(`id_pTrauma`,\n" +
                            "`edad_pTrauma`,\n" +
                            "`duracion_pTrauma`,\n" +
                            "`trauma_pTrauma`,\n" +
                            "`lado_pTrauma`,\n" +
                            "`secuelas_pTrauma`,\n" +
                            "`id_paciente`)\n" +
                        "VALUES (?,?,?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_pTrauma);
            sttm.setInt     (2, edad_pTrauma);
            sttm.setString  (3, duracion_pTrauma);
            sttm.setString  (4, trauma_pTrauma);
            sttm.setString  (5, lado_pTrauma);
            sttm.setString  (6, secuelas_pTrauma);
            sttm.setString  (7, id_paciente);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Antecedentes traumáticos guardados", 
                        "Antecedentes traumáticos guardados", 
                    "Antecedentes traumáticos han sido guardados exitosamente en la base de datos");
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
    public ObservableList<patoTraumaticos> listaAntePaTrauma(Connection conex, String idPaciente){
        ObservableList<patoTraumaticos> listaPatoTraumaticos = FXCollections.observableArrayList();
        String sql = "SELECT `id_pTrauma`,\n" +
                            "`edad_pTrauma`,\n" +
                            "`duracion_pTrauma`,\n" +
                            "`trauma_pTrauma`,\n" +
                            "`lado_pTrauma`,\n" +
                            "`secuelas_pTrauma`,\n" +
                            "`id_paciente`\n" +
                     "FROM `patotraumaticos` WHERE id_paciente = '"+idPaciente+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaPatoTraumaticos.add(new patoTraumaticos( 
                                        res.getString("id_pTrauma"), 
                                        res.getInt   ("edad_pTrauma"), 
                                        res.getString("duracion_pTrauma"), 
                                        res.getString("trauma_pTrauma"), 
                                        res.getString("lado_pTrauma"), 
                                        res.getString("secuelas_pTrauma"), 
                                        res.getString("id_paciente")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaPatoTraumaticos;
    }
    
    /**
     * borra antecedentes traumaticos
     * @param Dato
     * @param conex 
     */
    public void borrarAntePatoTrauma(String Dato, Connection conex){
        String sttm = "DELETE FROM patotraumaticos WHERE id_pTrauma = '"+Dato+"'";
        try(PreparedStatement stta = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            stta.addBatch();
            stta.executeBatch();
            conex.commit();
            aux.informacionUs("El traumatismo ha sido borrada", 
                            "El traumatismo ha sido borrado", 
                            "El traumatismo ha sido borrado ha sido borrada exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
/****************************************************************************************************/
    //Getters and Setters
    public final void setId_pTrauma(String value) {
        id_pTrauma.set(value);
    }

    public final String getId_pTrauma() {
        return id_pTrauma.get();
    }

    public final StringProperty id_pTraumaProperty() {
        return id_pTrauma;
    }

    public final void setEdad_pTrauma(Integer value) {
        edad_pTrauma.set(value);
    }

    public final Integer getEdad_pTrauma() {
        return edad_pTrauma.get();
    }

    public final IntegerProperty edad_pTraumaProperty() {
        return edad_pTrauma;
    }

    public final void setDuracion_pTrauma(String value) {
        duracion_pTrauma.set(value);
    }

    public final String getDuracion_pTrauma() {
        return duracion_pTrauma.get();
    }

    public final StringProperty duracion_pTraumaProperty() {
        return duracion_pTrauma;
    }

    public final void setTrauma_pTrauma(String value) {
        trauma_pTrauma.set(value);
    }

    public final String getTrauma_pTrauma() {
        return trauma_pTrauma.get();
    }

    public final StringProperty trauma_pTraumaProperty() {
        return trauma_pTrauma;
    }

    public final void setLado_pTrauma(String value) {
        lado_pTrauma.set(value);
    }

    public final String getLado_pTrauma() {
        return lado_pTrauma.get();
    }

    public final StringProperty lado_pTraumaProperty() {
        return lado_pTrauma;
    }

    public final void setSecuelas_pTrauma(String value) {
        secuelas_pTrauma.set(value);
    }

    public final String getSecuelas_pTrauma() {
        return secuelas_pTrauma.get();
    }

    public final StringProperty secuelas_pTraumaProperty() {
        return secuelas_pTrauma;
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
