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

public class patoQuirugicos {
    //Variables de clase
    private StringProperty id_pQuir;
    private IntegerProperty edad_pQuir;
    private StringProperty duracion_pQuir;
    private StringProperty cirugia_pQuir;
    private StringProperty id_paciente;
    //Variables auxiliares
    Auxiliar aux = new Auxiliar();

    /**
     * constructor lleno
     * @param id_pQuir
     * @param edad_pQuir
     * @param duracion_pQuir
     * @param cirugia_pQuir
     * @param id_paciente 
     */
    public patoQuirugicos(String id_pQuir, int edad_pQuir, 
            String duracion_pQuir, String cirugia_pQuir, String id_paciente) {
        this.id_pQuir = new SimpleStringProperty(id_pQuir);
        this.edad_pQuir = new SimpleIntegerProperty(edad_pQuir);
        this.duracion_pQuir = new SimpleStringProperty(duracion_pQuir);
        this.cirugia_pQuir = new SimpleStringProperty(cirugia_pQuir);
        this.id_paciente = new SimpleStringProperty(id_paciente);
    }

    /**
     * construcor vacio
     */
    public patoQuirugicos() {
    }

    
    /**
     * agrega a la base de datos antecedentes patologicos medicos
     * @param id_pQuir
     * @param edad_pQuir
     * @param duracion_pQuir
     * @param cirugia_pQuir
     * @param id_paciente
     * @param conex 
     */
    public void agregaPatoQuir(String id_pQuir, int edad_pQuir, 
            String duracion_pQuir, String cirugia_pQuir, String id_paciente, Connection conex){
        String sqlst =  "INSERT INTO `patoQuirugicos`\n" +
                        "(`id_pQuir`,\n" +
                        "`edad_pQuir`,\n" +
                        "`duracion_pQuir`,\n" +
                        "`cirugia_pQuir`,\n" +
                        "`id_paciente`)\n" +
                        "VALUES (?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_pQuir);
            sttm.setInt     (2, edad_pQuir);
            sttm.setString  (3, duracion_pQuir);
            sttm.setString  (4, cirugia_pQuir);
            sttm.setString  (5, id_paciente);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Antecedentes Quirúrgicos guardados", 
                        "Antecedentes Quirúrgicos guardados", 
                    "Antecedentes Quirúrgicos han sido guardados exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * actualiza antecedentes patologicos medicos del paciente seleccionado
     * @param id_pQuir
     * @param edad_pQuir
     * @param duracion_pQuir
     * @param cirugia_pQuir
     * @param id_paciente
     * @param conex 
     */
    public void actualizaPatoQuir(String id_pQuir, int edad_pQuir, 
            String duracion_pQuir, String cirugia_pQuir, String id_paciente, Connection conex){
        String sqlst =  "UPDATE `patoQuirugicos`\n" +
                        "SET\n" +
                        "`id_pQuir` = ?,\n" +
                        "`edad_pQuir` = ?,\n" +
                        "`duracion_pQuir` = ?,\n" +
                        "`cirugia_pQuir` = ?,\n" +
                        "`id_paciente` = ?,\n" +
                        "WHERE `id_pQuir` = ?;";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_pQuir);
            sttm.setInt     (2, edad_pQuir);
            sttm.setString  (3, duracion_pQuir);
            sttm.setString  (4, cirugia_pQuir);
            sttm.setString  (5, id_paciente);
            sttm.setString  (5, id_pQuir);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Antecedentes Quirúrgicos modificados", 
                    "Antecedentes Quirúrgicos modificados", 
                    "Antecedentes Quirúrgicos han sido modificados exitosamente en la base de datos");
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
    public ObservableList<patoQuirugicos> listaAntePatoQuir(Connection conex, String idPaciente){
        ObservableList<patoQuirugicos> listaPatoMedica = FXCollections.observableArrayList();
        String sql = "SELECT `id_pQuir`,\n" +
                        "`edad_pQuir`,\n" +
                        "`duracion_pQuir`,\n" +
                        "`cirugia_pQuir`,\n" +
                        "`id_paciente`\n" +
                     "FROM `patoQuirugicos` WHERE id_paciente = '"+idPaciente+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaPatoMedica.add(new patoQuirugicos( 
                                    res.getString("id_pQuir"), 
                                    res.getInt("edad_pQuir"), 
                                    res.getString("duracion_pQuir"), 
                                    res.getString("cirugia_pQuir"), 
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
    public void borrarAntePatoQuir(String Dato, Connection conex){
        String sttm = "DELETE FROM patoQuirugicos WHERE id_pQuir = '"+Dato+"'";
        try(PreparedStatement stta = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            stta.addBatch();
            stta.executeBatch();
            conex.commit();
            aux.informacionUs("La cirugía ha sido borrada", 
                            "La cirugía ha sido borrada", 
                            "La cirugía ha sido borrada exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


/*************************************************************************/
    //Setters and Getters
   
    public final void setId_pQuir(String value) {
        id_pQuir.set(value);
    }

    public final String getId_pQuir() {
        return id_pQuir.get();
    }

    public final StringProperty id_pQuirProperty() {
        return id_pQuir;
    }

    public final void setEdad_pQuir(Integer value) {
        edad_pQuir.set(value);
    }

    public final Integer getEdad_pQuir() {
        return edad_pQuir.get();
    }

    public final IntegerProperty edad_pQuirProperty() {
        return edad_pQuir;
    }

    public final void setDuracion_pQuir(String value) {
        duracion_pQuir.set(value);
    }

    public final String getDuracion_pQuir() {
        return duracion_pQuir.get();
    }

    public final StringProperty duracion_pQuirProperty() {
        return duracion_pQuir;
    }

    public final void setCirugia_pQuir(String value) {
        cirugia_pQuir.set(value);
    }

    public final String getCirugia_pQuir() {
        return cirugia_pQuir.get();
    }

    public final StringProperty cirugia_pQuirProperty() {
        return cirugia_pQuir;
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
