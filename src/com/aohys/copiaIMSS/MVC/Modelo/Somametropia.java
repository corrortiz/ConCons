/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Alejandro Ortiz Corro
 */

public class Somametropia {
    //Variables de clase
    private StringProperty id_soma;
    private IntegerProperty peso_soma;
    private IntegerProperty talla_soma;
    private IntegerProperty glucosa_soma;
    private IntegerProperty sistolica_soma;
    private IntegerProperty diastolica_soma;
    private IntegerProperty frecCardia_soma;
    private IntegerProperty frecRespiratoria_soma;
    private IntegerProperty temperatura_soma;
    private StringProperty id_paciente;

    /**
     * constructor lleno 
     * @param id_soma
     * @param peso_soma
     * @param talla_soma
     * @param glucosa_soma
     * @param sistolica_soma
     * @param diastolica_soma
     * @param frecCardia_soma
     * @param frecRespiratoria_soma
     * @param temperatura_soma
     * @param id_paciente 
     */
    public Somametropia(String id_soma, int peso_soma, int talla_soma, int glucosa_soma, int sistolica_soma, 
            int diastolica_soma, int frecCardia_soma, int frecRespiratoria_soma, int temperatura_soma, String id_paciente) {
        this.id_soma = new SimpleStringProperty(id_soma);
        this.peso_soma = new SimpleIntegerProperty(peso_soma);
        this.talla_soma = new SimpleIntegerProperty(talla_soma);
        this.glucosa_soma = new SimpleIntegerProperty(glucosa_soma);
        this.sistolica_soma = new SimpleIntegerProperty(sistolica_soma);
        this.diastolica_soma = new SimpleIntegerProperty(diastolica_soma);
        this.frecCardia_soma = new SimpleIntegerProperty(frecCardia_soma);
        this.frecRespiratoria_soma = new SimpleIntegerProperty(frecRespiratoria_soma);
        this.temperatura_soma = new SimpleIntegerProperty(temperatura_soma);
        this.id_paciente = new SimpleStringProperty(id_paciente);
    }

    /**
     * constructor vacio
     */
    public Somametropia() {
    }

    /**
     * agrega soma metropia
     * @param id_soma
     * @param peso_soma
     * @param talla_soma
     * @param glucosa_soma
     * @param sistolica_soma
     * @param diastolica_soma
     * @param frecCardia_soma
     * @param frecRespiratoria_soma
     * @param temperatura_soma
     * @param id_paciente
     * @param conex 
     */
    public void agregaSomametro(String id_soma, int peso_soma, int talla_soma,int glucosa_soma,
            int sistolica_soma, int diastolica_soma, int frecCardia_soma, int frecRespiratoria_soma, 
            int temperatura_soma, String id_paciente, Connection conex){
        String sqlst =  "INSERT INTO `somametropia`\n" +
                                "(`id_soma`,\n" +
                                "`peso_soma`,\n" +
                                "`talla_soma`,\n" +
                                "`glucosa_soma`,\n" +
                                "`sistolica_soma`,\n" +
                                "`diastolica_soma`,\n" +
                                "`frecCardia_soma`,\n" +
                                "`frecRespiratoria_soma`,\n" +
                                "`temperatura_soma`,\n" +
                                "`id_paciente`)\n"+
                        "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_soma);
            sttm.setInt     (2, peso_soma);
            sttm.setInt     (3, talla_soma);
            sttm.setInt     (4, glucosa_soma);
            sttm.setInt     (5, sistolica_soma);
            sttm.setInt     (6, diastolica_soma);
            sttm.setInt     (7, frecCardia_soma);
            sttm.setInt     (8, frecRespiratoria_soma);
            sttm.setInt     (9, temperatura_soma);
            sttm.setString  (10, id_paciente);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * carga la somatropia seleccionada
     * @param idPaciente
     * @param conex
     * @return 
     */
    public Somametropia cargaSoloUno(String idPaciente, Connection conex){
        Somametropia somametropia = null;
        String sttm = "SELECT `id_soma`,\n" +
                        "`peso_soma`,\n" +
                        "`talla_soma`,\n" +
                        "`glucosa_soma`,\n" +
                        "`sistolica_soma`,\n" +
                        "`diastolica_soma`,\n" +
                        "`frecCardia_soma`,\n" +
                        "`frecRespiratoria_soma`,\n" +
                        "`temperatura_soma`,\n" +
                        "`id_paciente`\n" +
                    "FROM somametropia WHERE id_paciente = '"+idPaciente+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                somametropia = new Somametropia(  res.getString ("id_soma"), 
                                                  res.getInt("peso_soma"),
                                                  res.getInt("talla_soma"),
                                                  res.getInt("glucosa_soma"),
                                                  res.getInt("sistolica_soma"),
                                                  res.getInt("diastolica_soma"),
                                                  res.getInt("frecCardia_soma"),
                                                  res.getInt("frecRespiratoria_soma"),
                                                  res.getInt("temperatura_soma"),
                                                  res.getString ("id_paciente"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return somametropia;
    }
    
    /**
     * actualiza la somatometria del paciente
     * @param id_soma
     * @param peso_soma
     * @param talla_soma
     * @param glucosa_soma
     * @param sistolica_soma
     * @param diastolica_soma
     * @param frecCardia_soma
     * @param frecRespiratoria_soma
     * @param temperatura_soma
     * @param id_paciente
     * @param conex 
     */
    public void actualizaSomatometria(String id_soma, int peso_soma, int talla_soma,int glucosa_soma,
            int sistolica_soma, int diastolica_soma, int frecCardia_soma, int frecRespiratoria_soma, 
            int temperatura_soma, String id_paciente, Connection conex){
        String sqlst =  " UPDATE `somametropia`\n" +
                        "SET\n" +
                        "`id_soma` = ?,\n" +
                        "`peso_soma` = ?,\n" +
                        "`talla_soma` = ?,\n" +
                        "`glucosa_soma` = ?,\n" +
                        "`sistolica_soma` = ?,\n" +
                        "`diastolica_soma` = ?,\n" +
                        "`frecCardia_soma` = ?,\n" +
                        "`frecRespiratoria_soma` = ?,\n" +
                        "`temperatura_soma` = ?,\n" +
                        "`id_paciente` = ?\n" +
                        "WHERE `id_soma` = ?;";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_soma);
            sttm.setInt     (2, peso_soma);
            sttm.setInt     (3, talla_soma);
            sttm.setInt     (4, glucosa_soma);
            sttm.setInt     (5, sistolica_soma);
            sttm.setInt     (6, diastolica_soma);
            sttm.setInt     (7, frecCardia_soma);
            sttm.setInt     (8, frecRespiratoria_soma);
            sttm.setInt     (9, temperatura_soma);
            sttm.setString  (10, id_paciente);
            sttm.setString  (11, id_soma);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
/***************************************************************************************/
    //Setters and Getters
    public final void setId_soma(String value) {
        id_soma.set(value);
    }

    public final String getId_soma() {
        return id_soma.get();
    }

    public final StringProperty id_somaProperty() {
        return id_soma;
    }

    public final void setPeso_soma(Integer value) {
        peso_soma.set(value);
    }

    public final Integer getPeso_soma() {
        return peso_soma.get();
    }

    public final IntegerProperty peso_somaProperty() {
        return peso_soma;
    }

    public final void setTalla_soma(Integer value) {
        talla_soma.set(value);
    }

    public final Integer getTalla_soma() {
        return talla_soma.get();
    }

    public final IntegerProperty talla_somaProperty() {
        return talla_soma;
    }

    public final void setGlucosa_soma(Integer value) {
        glucosa_soma.set(value);
    }

    public final Integer getGlucosa_soma() {
        return glucosa_soma.get();
    }

    public final IntegerProperty glucosa_somaProperty() {
        return glucosa_soma;
    }

    public final void setSistolica_soma(Integer value) {
        sistolica_soma.set(value);
    }

    public final Integer getSistolica_soma() {
        return sistolica_soma.get();
    }

    public final IntegerProperty sistolica_somaProperty() {
        return sistolica_soma;
    }

    public final void setDiastolica_soma(Integer value) {
        diastolica_soma.set(value);
    }

    public final Integer getDiastolica_soma() {
        return diastolica_soma.get();
    }

    public final IntegerProperty diastolica_somaProperty() {
        return diastolica_soma;
    }

    public final void setFrecCardia_soma(Integer value) {
        frecCardia_soma.set(value);
    }

    public final Integer getFrecCardia_soma() {
        return frecCardia_soma.get();
    }

    public final IntegerProperty frecCardia_somaProperty() {
        return frecCardia_soma;
    }

    public final void setFrecRespiratoria_soma(Integer value) {
        frecRespiratoria_soma.set(value);
    }

    public final Integer getFrecRespiratoria_soma() {
        return frecRespiratoria_soma.get();
    }

    public final IntegerProperty frecRespiratoria_somaProperty() {
        return frecRespiratoria_soma;
    }

    public final void setTemperatura_soma(Integer value) {
        temperatura_soma.set(value);
    }

    public final Integer getTemperatura_soma() {
        return temperatura_soma.get();
    }

    public final IntegerProperty temperatura_somaProperty() {
        return temperatura_soma;
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
