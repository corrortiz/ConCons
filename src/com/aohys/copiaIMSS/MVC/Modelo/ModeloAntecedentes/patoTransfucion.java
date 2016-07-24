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

public class patoTransfucion {
    //Variables de clase
    private StringProperty id_pTrans;
    private IntegerProperty edad_pTrans;
    private StringProperty duracion_pTrans;
    private StringProperty tipoTrasn_pTrans;
    private BooleanProperty reacciones_pTrans;
    private StringProperty tipoReacion_pTrans;
    private StringProperty id_paciente;
    //Axuliar
    Auxiliar aux = new Auxiliar();

    /**
     * constructor lleno 
     * @param id_pTrans
     * @param edad_pTrans
     * @param duracion_pTrans
     * @param tipoTrasn_pTrans
     * @param reacciones_pTrans
     * @param tipoReacion_pTrans
     * @param id_paciente 
     */
    public patoTransfucion(String id_pTrans, int edad_pTrans, String duracion_pTrans, String tipoTrasn_pTrans, 
            boolean reacciones_pTrans, String tipoReacion_pTrans, String id_paciente) {
        this.id_pTrans = new SimpleStringProperty(id_pTrans);
        this.edad_pTrans = new SimpleIntegerProperty(edad_pTrans);
        this.duracion_pTrans = new SimpleStringProperty(duracion_pTrans);
        this.tipoTrasn_pTrans = new SimpleStringProperty(tipoTrasn_pTrans);
        this.reacciones_pTrans = new SimpleBooleanProperty(reacciones_pTrans);
        this.tipoReacion_pTrans = new SimpleStringProperty(tipoReacion_pTrans);
        this.id_paciente = new SimpleStringProperty(id_paciente);
    }

    /**
     * constructor vacio 
     */
    public patoTransfucion() {
    }
    
    /**
     * agrega antecedentes de transfuncion
     * @param id_pTrans
     * @param edad_pTrans
     * @param duracion_pTrans
     * @param tipoTrasn_pTrans
     * @param reacciones_pTrans
     * @param tipoReacion_pTrans
     * @param id_paciente
     * @param conex 
     */
    public void agregaPatoTransfucion(String id_pTrans, int edad_pTrans, String duracion_pTrans, String tipoTrasn_pTrans, 
            boolean reacciones_pTrans, String tipoReacion_pTrans, String id_paciente, Connection conex){
        String sqlst =  "INSERT INTO `patotransfucion`\n" +
                            "(`id_pTrans`,\n" +
                            "`edad_pTrans`,\n" +
                            "`duracion_pTrans`,\n" +
                            "`tipoTrasn_pTrans`,\n" +
                            "`reacciones_pTrans`,\n" +
                            "`tipoReacion_pTrans`,\n" +
                            "`id_paciente`)\n" +
                        "VALUES (?,?,?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_pTrans);
            sttm.setInt     (2, edad_pTrans);
            sttm.setString  (3, duracion_pTrans);
            sttm.setString  (4, tipoTrasn_pTrans);
            sttm.setBoolean (5, reacciones_pTrans);
            sttm.setString  (6, tipoReacion_pTrans);
            sttm.setString  (7, id_paciente);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Antecedentes de transfusión guardados", 
                        "Antecedentes de transfusión guardados", 
                    "Antecedentes de transfusión han sido guardados exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }  
    
    /**
     * regresa la lista de antecedentes 
     * @param conex
     * @param idPaciente
     * @return 
     */
    public ObservableList<patoTransfucion> listaAntePaTransfucion(Connection conex, String idPaciente){
        ObservableList<patoTransfucion> listaPatoTransfucion = FXCollections.observableArrayList();
        String sql = "SELECT `id_pTrans`,\n" +
                            "`edad_pTrans`,\n" +
                            "`duracion_pTrans`,\n" +
                            "`tipoTrasn_pTrans`,\n" +
                            "`reacciones_pTrans`,\n" +
                            "`tipoReacion_pTrans`,\n" +
                            "`id_paciente`\n" +
                     "FROM `patotransfucion` WHERE id_paciente = '"+idPaciente+"'\n" +
                     "ORDER BY edad_pTrans ASC\n";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaPatoTransfucion.add(new patoTransfucion( 
                                        res.getString ("id_pTrans"), 
                                        res.getInt    ("edad_pTrans"), 
                                        res.getString ("duracion_pTrans"), 
                                        res.getString ("tipoTrasn_pTrans"), 
                                        res.getBoolean("reacciones_pTrans"), 
                                        res.getString ("tipoReacion_pTrans"), 
                                        res.getString ("id_paciente")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaPatoTransfucion;
    } 
    
    /**
     * borra la trasnfuncion seleccionada 
     * @param Dato
     * @param conex 
     */
    public void borrarAntePatoTrausnfuncion(String Dato, Connection conex){
        String sttm = "DELETE FROM patotransfucion WHERE id_pTrans = '"+Dato+"'";
        try(PreparedStatement stta = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            stta.addBatch();
            stta.executeBatch();
            conex.commit();
            aux.informacionUs("La transfusión ha sido borrada", 
                            "La transfusión ha sido borrado", 
                            "La transfusión ha sido borrado ha sido borrada exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /*************************************************************************************/
    //Setters and Getters

    public final void setId_pTrans(String value) {
        id_pTrans.set(value);
    }

    public final String getId_pTrans() {
        return id_pTrans.get();
    }

    public final StringProperty id_pTransProperty() {
        return id_pTrans;
    }

    public final void setEdad_pTrans(Integer value) {
        edad_pTrans.set(value);
    }

    public final Integer getEdad_pTrans() {
        return edad_pTrans.get();
    }

    public final IntegerProperty edad_pTransProperty() {
        return edad_pTrans;
    }

    public final void setDuracion_pTrans(String value) {
        duracion_pTrans.set(value);
    }

    public final String getDuracion_pTrans() {
        return duracion_pTrans.get();
    }

    public final StringProperty duracion_pTransProperty() {
        return duracion_pTrans;
    }

    public final void setTipoTrasn_pTrans(String value) {
        tipoTrasn_pTrans.set(value);
    }

    public final String getTipoTrasn_pTrans() {
        return tipoTrasn_pTrans.get();
    }

    public final StringProperty tipoTrasn_pTransProperty() {
        return tipoTrasn_pTrans;
    }

    public final void setReacciones_pTrans(Boolean value) {
        reacciones_pTrans.set(value);
    }

    public final Boolean getReacciones_pTrans() {
        return reacciones_pTrans.get();
    }

    public final BooleanProperty reacciones_pTransProperty() {
        return reacciones_pTrans;
    }

    public final void setTipoReacion_pTrans(String value) {
        tipoReacion_pTrans.set(value);
    }

    public final String getTipoReacion_pTrans() {
        return tipoReacion_pTrans.get();
    }

    public final StringProperty tipoReacion_pTransProperty() {
        return tipoReacion_pTrans;
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
