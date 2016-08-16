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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Alejandro Ortiz Corro
 */

public class patoAlergias {
    //Variables de clases
    private StringProperty id_pAler;
    private BooleanProperty medOtros_pAler;
    private StringProperty descripcion_pAler;
    private StringProperty id_paciente;
    //Auxiliares
    Auxiliar aux = new Auxiliar();

    /**
     * constructor lleno
     * @param id_pAler
     * @param medOtros_pAler
     * @param descripcion_pAler
     * @param id_paciente 
     */
    public patoAlergias(String id_pAler, boolean medOtros_pAler, String descripcion_pAler, String id_paciente) {
        this.id_pAler = new SimpleStringProperty(id_pAler);
        this.medOtros_pAler = new SimpleBooleanProperty(medOtros_pAler);
        this.descripcion_pAler = new SimpleStringProperty(descripcion_pAler);
        this.id_paciente = new SimpleStringProperty(id_paciente);
    }

    /**
     * constructor vacio
     */
    public patoAlergias() {
    }

    
    /**
     * agrega alergia 
     * @param id_pAler
     * @param medOtros_pAler
     * @param descripcion_pAler
     * @param id_paciente
     * @param conex 
     */
    public void agregaPatolergia(String id_pAler, boolean medOtros_pAler, 
            String descripcion_pAler, String id_paciente, Connection conex){
        String sqlst =  "INSERT INTO `patoalergias`\n" +
                            "(`id_pAler`,\n" +
                            "`medOtros_pAler`,\n" +
                            "`descripcion_pAler`,\n" +
                            "`id_paciente`)\n" +
                        "VALUES (?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_pAler);
            sttm.setBoolean (2, medOtros_pAler);
            sttm.setString  (3, descripcion_pAler);
            sttm.setString  (4, id_paciente);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Antecedentes de alergias guardados", 
                        "Antecedentes de alergias guardados", 
                    "Antecedentes de alergias han sido guardados exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }  
    
    /**
     * lista de antecdentes de alergias 
     * @param conex
     * @param idPaciente
     * @return 
     */
    public ObservableList<patoAlergias> listaAntePaAlergias(Connection conex, String idPaciente){
        ObservableList<patoAlergias> listapatoAlergias = FXCollections.observableArrayList();
        String sql = "SELECT `id_pAler`,\n" +
                            "`medOtros_pAler`,\n" +
                            "`descripcion_pAler`,\n" +
                            "`id_paciente`\n" +
                     "FROM `patoalergias` WHERE id_paciente = '"+idPaciente+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listapatoAlergias.add(new patoAlergias( 
                                        res.getString ("id_pAler"), 
                                        res.getBoolean("medOtros_pAler"), 
                                        res.getString ("descripcion_pAler"), 
                                        res.getString ("id_paciente")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listapatoAlergias;
    } 

    /**
     * borra alergia seleccionada
     * @param Dato
     * @param conex 
     */
    public void borrarAntePatoAlergias(String Dato, Connection conex){
        String sttm = "DELETE FROM patoalergias WHERE id_pAler = '"+Dato+"'";
        try(PreparedStatement stta = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            stta.addBatch();
            stta.executeBatch();
            conex.commit();
            aux.informacionUs("La alergia ha sido borrada", 
                            "La alergia ha sido borrada", 
                            "La alergia ha sido borrada ha sido borrada exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
/*********************************************************************************/
//Setters and Getters    
    public final void setId_pAler(String value) {
        id_pAler.set(value);
    }

    public final String getId_pAler() {
        return id_pAler.get();
    }

    public final StringProperty id_pAlerProperty() {
        return id_pAler;
    }

    public final void setMedOtros_pAler(Boolean value) {
        medOtros_pAler.set(value);
    }

    public final Boolean getMedOtros_pAler() {
        return medOtros_pAler.get();
    }

    public final BooleanProperty medOtros_pAlerProperty() {
        return medOtros_pAler;
    }

    public final void setDescripcion_pAler(String value) {
        descripcion_pAler.set(value);
    }

    public final String getDescripcion_pAler() {
        return descripcion_pAler.get();
    }

    public final StringProperty descripcion_pAlerProperty() {
        return descripcion_pAler;
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
