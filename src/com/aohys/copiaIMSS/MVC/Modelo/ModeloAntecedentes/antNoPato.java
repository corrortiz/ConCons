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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Alejandro Ortiz Corro
 */

public class antNoPato {
    //Variables de clase
    private StringProperty id_antNP;
    private StringProperty religion_antNP;
    private StringProperty lugarNaci_antNP;
    private StringProperty estaCivil_antNP;
    private StringProperty escolaridad_antNP;
    private StringProperty higiene_antNP;
    private StringProperty actividadFisica_antNP;
    private IntegerProperty frecuencia_antNP;
    private StringProperty sexualidad_antNP;
    private IntegerProperty numParejas_antNP;
    private StringProperty sangre_antNP;
    private StringProperty alimentacion_antNP;
    private StringProperty id_paciente;
    private BooleanProperty escoCompInco_antNP;
    private StringProperty frecVeces_antNP;
    //Variables axuliares de clase
    Auxiliar aux = new Auxiliar();

    /**
     * Constructor lleno
     * @param id_antNP
     * @param religion_antNP
     * @param lugarNaci_antNP
     * @param estaCivil_antNP
     * @param escolaridad_antNP
     * @param higiene_antNP
     * @param actividadFisica_antNP
     * @param frecuencia_antNP
     * @param sexualidad_antNP
     * @param numParejas_antNP
     * @param sangre_antNP
     * @param alimentacion_antNP
     * @param id_paciente 
     * @param escoCompInco_antNP 
     * @param frecVeces_antNP 
     */
    public antNoPato(String id_antNP, String religion_antNP, String lugarNaci_antNP, String estaCivil_antNP, 
            String escolaridad_antNP, String higiene_antNP, String actividadFisica_antNP, int frecuencia_antNP, 
            String sexualidad_antNP, int numParejas_antNP, String sangre_antNP, String alimentacion_antNP, String id_paciente,
            boolean escoCompInco_antNP, String frecVeces_antNP) {
        this.id_antNP = new SimpleStringProperty(id_antNP);
        this.religion_antNP = new SimpleStringProperty(religion_antNP);
        this.lugarNaci_antNP = new SimpleStringProperty(lugarNaci_antNP);
        this.estaCivil_antNP = new SimpleStringProperty(estaCivil_antNP);
        this.escolaridad_antNP = new SimpleStringProperty(escolaridad_antNP);
        this.higiene_antNP = new SimpleStringProperty(higiene_antNP);
        this.actividadFisica_antNP = new SimpleStringProperty(actividadFisica_antNP);
        this.frecuencia_antNP = new SimpleIntegerProperty(frecuencia_antNP);
        this.sexualidad_antNP = new SimpleStringProperty(sexualidad_antNP);
        this.numParejas_antNP = new SimpleIntegerProperty(numParejas_antNP);
        this.sangre_antNP = new SimpleStringProperty(sangre_antNP);
        this.alimentacion_antNP = new SimpleStringProperty(alimentacion_antNP);
        this.id_paciente = new SimpleStringProperty(id_paciente);
        this.escoCompInco_antNP = new SimpleBooleanProperty(escoCompInco_antNP);
        this.frecVeces_antNP = new SimpleStringProperty(frecVeces_antNP);
    }

    /**
     * constructor vacio
     */
    public antNoPato() {
    }

    /**
     * Agrega antNoPato del paciente seleccionado
     * @param id_antNP
     * @param religion_antNP
     * @param lugarNaci_antNP
     * @param estaCivil_antNP
     * @param escolaridad_antNP
     * @param higiene_antNP
     * @param actividadFisica_antNP
     * @param frecuencia_antNP
     * @param sexualidad_antNP
     * @param numParejas_antNP
     * @param sangre_antNP
     * @param alimentacion_antNP
     * @param id_paciente
     * @param escoCompInco_antNP
     * @param frecVeces_antNP
     * @param conex 
     */
    public void agregaAntNoPato(String id_antNP, String religion_antNP, String lugarNaci_antNP, String estaCivil_antNP, 
            String escolaridad_antNP, String higiene_antNP, String actividadFisica_antNP, int frecuencia_antNP, 
            String sexualidad_antNP, int numParejas_antNP, String sangre_antNP, String alimentacion_antNP, String id_paciente,
            boolean escoCompInco_antNP, String frecVeces_antNP, Connection conex){
        String sqlst =  "INSERT INTO antnopato\n "+
                            "(`id_antNP`,\n" +
                            "`religion_antNP`,\n" +
                            "`lugarNaci_antNP`,\n" +
                            "`estaCivil_antNP`,\n" +
                            "`escolaridad_antNP`,\n" +
                            "`higiene_antNP`,\n" +
                            "`actividadFisica_antNP`,\n" +
                            "`frecuencia_antNP`,\n" +
                            "`sexualidad_antNP`,\n" +
                            "`numParejas_antNP`,\n" +
                            "`sangre_antNP`,\n" +
                            "`alimentacion_antNP`,\n" +
                            "`id_paciente`,\n"+
                            "`escoCompInco_antNP`,\n"+
                            "`frecVeces_antNP`)\n"+
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_antNP);
            sttm.setString  (2, religion_antNP);
            sttm.setString  (3, lugarNaci_antNP);
            sttm.setString  (4, estaCivil_antNP);
            sttm.setString  (5, escolaridad_antNP);
            sttm.setString  (6, higiene_antNP);
            sttm.setString  (7, actividadFisica_antNP);
            sttm.setInt     (8, frecuencia_antNP);
            sttm.setString  (9, sexualidad_antNP);
            sttm.setInt     (10, numParejas_antNP);
            sttm.setString  (11, sangre_antNP);
            sttm.setString  (12, alimentacion_antNP);
            sttm.setString  (13, id_paciente);
            sttm.setBoolean (14, escoCompInco_antNP);
            sttm.setString  (15, frecVeces_antNP);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Antecedentes personales no patológicos guardados", 
                    "Antecedentes personales no patológicos guardados", 
                    "Antecedentes personales no patológicos han sido guardados exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public antNoPato cargaSoloUno(String idPaciente, Connection conex){
        antNoPato antNoPato = null;
        String sttm = "SELECT `id_antNP`,\n" +
                            "`religion_antNP`,\n" +
                            "`lugarNaci_antNP`,\n" +
                            "`estaCivil_antNP`,\n" +
                            "`escolaridad_antNP`,\n" +
                            "`higiene_antNP`,\n" +
                            "`actividadFisica_antNP`,\n" +
                            "`frecuencia_antNP`,\n" +
                            "`sexualidad_antNP`,\n" +
                            "`numParejas_antNP`,\n" +
                            "`sangre_antNP`,\n" +
                            "`alimentacion_antNP`,\n" +
                            "`id_paciente`,\n" +
                            "`escoCompInco_antNP`,\n" +
                            "`frecVeces_antNP`\n" +
                        "FROM antnopato WHERE id_paciente = '"+idPaciente+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                antNoPato = new antNoPato(  res.getString ("id_antNP"), 
                                            res.getString ("religion_antNP"),
                                            res.getString ("lugarNaci_antNP"),
                                            res.getString ("estaCivil_antNP"),
                                            res.getString ("escolaridad_antNP"),
                                            res.getString ("higiene_antNP"),
                                            res.getString ("actividadFisica_antNP"),
                                            res.getInt    ("frecuencia_antNP"),
                                            res.getString ("sexualidad_antNP"),
                                            res.getInt    ("numParejas_antNP"),
                                            res.getString ("sangre_antNP"),
                                            res.getString ("alimentacion_antNP"),
                                            res.getString ("id_paciente"),
                                            res.getBoolean("escoCompInco_antNP"),
                                            res.getString ("frecVeces_antNP"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return antNoPato;
    }
    
    /**
     * Actualiza los antecedentes no patologicos del paciente seleccionado
     * @param id_antNP
     * @param religion_antNP
     * @param lugarNaci_antNP
     * @param estaCivil_antNP
     * @param escolaridad_antNP
     * @param higiene_antNP
     * @param actividadFisica_antNP
     * @param frecuencia_antNP
     * @param sexualidad_antNP
     * @param numParejas_antNP
     * @param sangre_antNP
     * @param alimentacion_antNP
     * @param id_paciente
     * @param escoCompInco_antNP
     * @param frecVeces_antNP
     * @param conex      */
    public void actualizaAntNoPato(String id_antNP, String religion_antNP, String lugarNaci_antNP, String estaCivil_antNP, 
            String escolaridad_antNP, String higiene_antNP, String actividadFisica_antNP, int frecuencia_antNP, 
            String sexualidad_antNP, int numParejas_antNP, String sangre_antNP, String alimentacion_antNP, String id_paciente,
            boolean escoCompInco_antNP, String frecVeces_antNP, Connection conex){
        String sqlst =  " UPDATE `antnopato`\n" +
                        "SET\n" +
                        "`id_antNP` = ?,\n" +
                        "`religion_antNP` = ?,\n" +
                        "`lugarNaci_antNP` = ?,\n" +
                        "`estaCivil_antNP` = ?,\n" +
                        "`escolaridad_antNP` = ?,\n" +
                        "`higiene_antNP` = ?,\n" +
                        "`actividadFisica_antNP` = ?,\n" +
                        "`frecuencia_antNP` = ?,\n" +
                        "`sexualidad_antNP` = ?,\n" +
                        "`numParejas_antNP` = ?,\n" +
                        "`sangre_antNP` = ?,\n" +
                        "`alimentacion_antNP` = ?,\n" +
                        "`id_paciente` = ?,\n" +
                        "`escoCompInco_antNP` = ?,\n" +
                        "`frecVeces_antNP` = ?\n" +
                        "WHERE `id_antNP` = ?;";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_antNP);
            sttm.setString  (2, religion_antNP);
            sttm.setString  (3, lugarNaci_antNP);
            sttm.setString  (4, estaCivil_antNP);
            sttm.setString  (5, escolaridad_antNP);
            sttm.setString  (6, higiene_antNP);
            sttm.setString  (7, actividadFisica_antNP);
            sttm.setInt     (8, frecuencia_antNP);
            sttm.setString  (9, sexualidad_antNP);
            sttm.setInt     (10, numParejas_antNP);
            sttm.setString  (11, sangre_antNP);
            sttm.setString  (12, alimentacion_antNP);
            sttm.setString  (13, id_paciente);
            sttm.setBoolean (14, escoCompInco_antNP);
            sttm.setString  (15, frecVeces_antNP);
            sttm.setString  (16, id_antNP);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Antecedentes personales no patológicos modificados", 
                    "Antecedentes personales no patológicos modificados", 
                    "Antecedentes personales no patológicos han sido modificados exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
/*************************************************************************************************/
    //Setters and Getters
    public final void setId_antNP(String value) {
        id_antNP.set(value);
    }

    public final String getId_antNP() {
        return id_antNP.get();
    }

    public final StringProperty id_antNPProperty() {
        return id_antNP;
    }

    public final void setReligion_antNP(String value) {
        religion_antNP.set(value);
    }

    public final String getReligion_antNP() {
        return religion_antNP.get();
    }

    public final StringProperty religion_antNPProperty() {
        return religion_antNP;
    }

    public final void setLugarNaci_antNP(String value) {
        lugarNaci_antNP.set(value);
    }

    public final String getLugarNaci_antNP() {
        return lugarNaci_antNP.get();
    }

    public final StringProperty lugarNaci_antNPProperty() {
        return lugarNaci_antNP;
    }

    public final void setEstaCivil_antNP(String value) {
        estaCivil_antNP.set(value);
    }

    public final String getEstaCivil_antNP() {
        return estaCivil_antNP.get();
    }

    public final StringProperty estaCivil_antNPProperty() {
        return estaCivil_antNP;
    }

    public final void setEscolaridad_antNP(String value) {
        escolaridad_antNP.set(value);
    }

    public final String getEscolaridad_antNP() {
        return escolaridad_antNP.get();
    }

    public final StringProperty escolaridad_antNPProperty() {
        return escolaridad_antNP;
    }

    public final void setHigiene_antNP(String value) {
        higiene_antNP.set(value);
    }

    public final String getHigiene_antNP() {
        return higiene_antNP.get();
    }

    public final StringProperty higiene_antNPProperty() {
        return higiene_antNP;
    }

    public final void setActividadFisica_antNP(String value) {
        actividadFisica_antNP.set(value);
    }

    public final String getActividadFisica_antNP() {
        return actividadFisica_antNP.get();
    }

    public final StringProperty actividadFisica_antNPProperty() {
        return actividadFisica_antNP;
    }

    public final void setFrecuencia_antNP(Integer value) {
        frecuencia_antNP.set(value);
    }

    public final Integer getFrecuencia_antNP() {
        return frecuencia_antNP.get();
    }

    public final IntegerProperty frecuencia_antNPProperty() {
        return frecuencia_antNP;
    }

    public final void setSexualidad_antNP(String value) {
        sexualidad_antNP.set(value);
    }

    public final String getSexualidad_antNP() {
        return sexualidad_antNP.get();
    }

    public final StringProperty sexualidad_antNPProperty() {
        return sexualidad_antNP;
    }

    public final void setNumParejas_antNP(Integer value) {
        numParejas_antNP.set(value);
    }

    public final Integer getNumParejas_antNP() {
        return numParejas_antNP.get();
    }

    public final IntegerProperty numParejas_antNPProperty() {
        return numParejas_antNP;
    }

    public final void setSangre_antNP(String value) {
        sangre_antNP.set(value);
    }

    public final String getSangre_antNP() {
        return sangre_antNP.get();
    }

    public final StringProperty sangre_antNPProperty() {
        return sangre_antNP;
    }

    public final void setAlimentacion_antNP(String value) {
        alimentacion_antNP.set(value);
    }

    public final String getAlimentacion_antNP() {
        return alimentacion_antNP.get();
    }

    public final StringProperty alimentacion_antNPProperty() {
        return alimentacion_antNP;
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

    public final void setEscoCompInco_antNP(Boolean value) {
        escoCompInco_antNP.set(value);
    }

    public final Boolean getEscoCompInco_antNP() {
        return escoCompInco_antNP.get();
    }

    public final BooleanProperty escoCompInco_antNPProperty() {
        return escoCompInco_antNP;
    }

    public final void setFrecVeces_antNP(String value) {
        frecVeces_antNP.set(value);
    }

    public final String getFrecVeces_antNP() {
        return frecVeces_antNP.get();
    }

    public final StringProperty frecVeces_antNPProperty() {
        return frecVeces_antNP;
    }
    
    
    
}
