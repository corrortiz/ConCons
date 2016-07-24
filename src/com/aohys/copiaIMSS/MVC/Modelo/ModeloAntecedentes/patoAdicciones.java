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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Alejandro Ortiz Corro
 */

public class patoAdicciones {
    //Variables de clase
    private StringProperty id_pAdicc;
    private StringProperty adiccion_pAdicc;
    private IntegerProperty edInicio_pAdicc;
    private IntegerProperty edFinal_pAdicc;
    private BooleanProperty dependencia_pAdicc;
    private StringProperty id_paciente;
    //Auxiliares
    Auxiliar aux = new Auxiliar();

    /**
     * construccion lleno
     * @param id_pAdicc
     * @param adiccion_pAdicc
     * @param edInicio_pAdicc
     * @param edFinal_pAdicc
     * @param dependencia_pAdicc
     * @param id_paciente 
     */
    public patoAdicciones(String id_pAdicc, String adiccion_pAdicc, int edInicio_pAdicc, int edFinal_pAdicc,
            boolean dependencia_pAdicc, String id_paciente) {
        this.id_pAdicc = new SimpleStringProperty(id_pAdicc);
        this.adiccion_pAdicc = new SimpleStringProperty(adiccion_pAdicc);
        this.edInicio_pAdicc = new SimpleIntegerProperty(edInicio_pAdicc);
        this.edFinal_pAdicc = new SimpleIntegerProperty(edFinal_pAdicc);
        this.dependencia_pAdicc = new SimpleBooleanProperty(dependencia_pAdicc);
        this.id_paciente = new SimpleStringProperty(id_paciente);
    }

    /**
     * constructor vacio
     */
    public patoAdicciones() {
    }

    /**
     * agrega antecedentes de adiccion 
     * @param id_pAdicc
     * @param adiccion_pAdicc
     * @param edInicio_pAdicc
     * @param edFinal_pAdicc
     * @param dependencia_pAdicc
     * @param id_paciente
     * @param conex 
     */
    public void agregaPatoAdiccion(String id_pAdicc, String adiccion_pAdicc, int edInicio_pAdicc, int edFinal_pAdicc,
            boolean dependencia_pAdicc, String id_paciente, Connection conex){
        String sqlst =  "INSERT INTO `patoadicciones`\n" +
                                "(`id_pAdicc`,\n" +
                                "`adiccion_pAdicc`,\n" +
                                "`edInicio_pAdicc`,\n" +
                                "`edFinal_pAdicc`,\n" +
                                "`dependencia_pAdicc`,\n" +
                                "`id_paciente`)\n" +
                        "VALUES (?,?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_pAdicc);
            sttm.setString  (2, adiccion_pAdicc);
            sttm.setInt     (3, edInicio_pAdicc);
            sttm.setInt     (4, edFinal_pAdicc);
            sttm.setBoolean (5, dependencia_pAdicc);
            sttm.setString  (6, id_paciente);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Antecedentes de adicciones guardado", 
                        "Antecedentes de adicciones guardado", 
                    "Antecedentes de adicciones han sido guardado exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    } 
    
    /**
     * lista de antecedentes adicciones del usuario
     * @param conex
     * @param idPaciente
     * @return 
     */
    public ObservableList<patoAdicciones> listaAntePaAdicciones(Connection conex, String idPaciente){
        ObservableList<patoAdicciones> listaPatoAdicciones = FXCollections.observableArrayList();
        String sql = "SELECT `id_pAdicc`,\n" +
                            "`adiccion_pAdicc`,\n" +
                            "`edInicio_pAdicc`,\n" +
                            "`edFinal_pAdicc`,\n" +
                            "`dependencia_pAdicc`,\n" +
                            "`id_paciente`\n" +
                     "FROM `patoadicciones` WHERE id_paciente = '"+idPaciente+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaPatoAdicciones.add(new patoAdicciones( 
                                        res.getString ("id_pAdicc"), 
                                        res.getString ("adiccion_pAdicc"), 
                                        res.getInt    ("edInicio_pAdicc"), 
                                        res.getInt    ("edFinal_pAdicc"), 
                                        res.getBoolean("dependencia_pAdicc"), 
                                        res.getString ("id_paciente")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaPatoAdicciones;
    } 
    
    /**
     * borra antecedentes de adicciones 
     * @param Dato
     * @param conex 
     */
    public void borrarAntePatoAdicciones(String Dato, Connection conex){
        String sttm = "DELETE FROM patoadicciones WHERE id_pAdicc = '"+Dato+"'";
        try(PreparedStatement stta = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            stta.addBatch();
            stta.executeBatch();
            conex.commit();
            aux.informacionUs("La adicción ha sido borrada", 
                            "La adicción ha sido borrada", 
                            "La adicción ha sido borrada ha sido borrada exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
/**********************************************************************************/
    //Setters and Getters
    public final void setId_pAdicc(String value) {
        id_pAdicc.set(value);
    }

    public final String getId_pAdicc() {
        return id_pAdicc.get();
    }

    public final StringProperty id_pAdiccProperty() {
        return id_pAdicc;
    }

    public final void setAdiccion_pAdicc(String value) {
        adiccion_pAdicc.set(value);
    }

    public final String getAdiccion_pAdicc() {
        return adiccion_pAdicc.get();
    }

    public final StringProperty adiccion_pAdiccProperty() {
        return adiccion_pAdicc;
    }

    public final void setEdInicio_pAdicc(Integer value) {
        edInicio_pAdicc.set(value);
    }

    public final Integer getEdInicio_pAdicc() {
        return edInicio_pAdicc.get();
    }

    public final IntegerProperty edInicio_pAdiccProperty() {
        return edInicio_pAdicc;
    }

    public final void setEdFinal_pAdicc(Integer value) {
        edFinal_pAdicc.set(value);
    }

    public final Integer getEdFinal_pAdicc() {
        return edFinal_pAdicc.get();
    }

    public final IntegerProperty edFinal_pAdiccProperty() {
        return edFinal_pAdicc;
    }

    public final void setDependencia_pAdicc(Boolean value) {
        dependencia_pAdicc.set(value);
    }

    public final Boolean getDependencia_pAdicc() {
        return dependencia_pAdicc.get();
    }

    public final BooleanProperty dependencia_pAdiccProperty() {
        return dependencia_pAdicc;
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
