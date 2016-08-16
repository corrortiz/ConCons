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

public class ant_Heredo_Familiar {
    //Variables de clase
    private StringProperty id_antHeredo;
    private StringProperty padecimiento_antHeredo;
    private StringProperty familiares_antHeredo;
    private BooleanProperty finado_antHeredo;
    private StringProperty id_paciente;

    //Variables Auxiliares
    Auxiliar aux = new Auxiliar();
    
    /**
     * Constructor lleno
     * @param id_antHeredo
     * @param padecimiento_antHeredo
     * @param familiares_antHeredo
     * @param finado_antHeredo
     * @param id_paciente 
     */
    public ant_Heredo_Familiar(String id_antHeredo, String padecimiento_antHeredo, String familiares_antHeredo, 
            boolean finado_antHeredo, String id_paciente) {
        this.id_antHeredo = new SimpleStringProperty(id_antHeredo);
        this.padecimiento_antHeredo = new SimpleStringProperty(padecimiento_antHeredo);
        this.familiares_antHeredo = new SimpleStringProperty(familiares_antHeredo);
        this.finado_antHeredo = new SimpleBooleanProperty(finado_antHeredo);
        this.id_paciente = new SimpleStringProperty(id_paciente);
    }

    /**
     * Constructor vacio
     */
    public ant_Heredo_Familiar() {
    }
    
    /**
     * agrega padecimiento a la base de datos
     * @param id_antHeredo
     * @param padecimiento_antHeredo
     * @param familiares_antHeredo
     * @param finado_antHeredo
     * @param id_paciente
     * @param conex 
     */
    public void agregarPadecimiento(String id_antHeredo, String padecimiento_antHeredo, String familiares_antHeredo, 
            boolean finado_antHeredo, String id_paciente, Connection conex){
        String sqlst =  "INSERT INTO ant_heredo_familiar  (id_antHeredo, padecimiento_antHeredo,\n"+
                        "familiares_antHeredo, finado_antHeredo, id_paciente)\n"+
                        "VALUES (?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_antHeredo);
            sttm.setString  (2, padecimiento_antHeredo);
            sttm.setString  (3, familiares_antHeredo);
            sttm.setBoolean (4, finado_antHeredo);
            sttm.setString  (5, id_paciente);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("El padecimiento ha sido guardado", 
                    "El padecimiento ha sido guardado", 
                    "El padecimiento ha sido guardado exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * carga una lista de padecimientos cronicos del paciente seleccionado
     * @param conex
     * @param idPaciente
     * @return 
     */
    public ObservableList<ant_Heredo_Familiar> listaPadecimientosAnte(Connection conex, String idPaciente){
        ObservableList<ant_Heredo_Familiar> listaPade = FXCollections.observableArrayList();
        String sql = "SELECT id_antHeredo, padecimiento_antHeredo,\n"+
                     "familiares_antHeredo, finado_antHeredo, id_paciente\n" +
                     "FROM ant_heredo_familiar  WHERE id_paciente = '"+idPaciente+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaPade.add(new ant_Heredo_Familiar( 
                                    res.getString("id_antHeredo"), 
                                    res.getString("padecimiento_antHeredo"), 
                                    res.getString("familiares_antHeredo"), 
                                    res.getBoolean("finado_antHeredo"), 
                                    res.getString("id_paciente")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaPade;
    }
    
    /**
     * borra el padecimiento seleccionado
     * @param Dato
     * @param conex 
     */
    public void BorrarPadecimiento(String Dato, Connection conex){
        String sttm = "DELETE FROM ant_heredo_familiar  WHERE id_antHeredo = '"+Dato+"'";
        try(PreparedStatement stta = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            stta.addBatch();
            stta.executeBatch();
            conex.commit();
            aux.informacionUs("El padecimiento ha sido borrado", 
                    "El padecimiento ha sido borrado", 
                    "El padecimiento ha sido borrado exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
/***********************************************************************/
    //Setters and Getters
    public final void setId_antHeredo(String value) {
        id_antHeredo.set(value);
    }

    public final String getId_antHeredo() {
        return id_antHeredo.get();
    }

    public final StringProperty id_antHeredoProperty() {
        return id_antHeredo;
    }

    public final void setPadecimiento_antHeredo(String value) {
        padecimiento_antHeredo.set(value);
    }

    public final String getPadecimiento_antHeredo() {
        return padecimiento_antHeredo.get();
    }

    public final StringProperty padecimiento_antHeredoProperty() {
        return padecimiento_antHeredo;
    }

    public final void setFamiliares_antHeredo(String value) {
        familiares_antHeredo.set(value);
    }

    public final String getFamiliares_antHeredo() {
        return familiares_antHeredo.get();
    }

    public final StringProperty familiares_antHeredoProperty() {
        return familiares_antHeredo;
    }

    public final void setFinado_antHeredo(Boolean value) {
        finado_antHeredo.set(value);
    }

    public final Boolean getFinado_antHeredo() {
        return finado_antHeredo.get();
    }

    public final BooleanProperty finado_antHeredoProperty() {
        return finado_antHeredo;
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
