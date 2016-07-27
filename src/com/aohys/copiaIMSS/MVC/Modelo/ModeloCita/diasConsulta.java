/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo.ModeloCita;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * @author Alejandro Ortiz Corro
 */

public class diasConsulta {
    //Variables de clase
    private StringProperty id_diasConsul;
    private BooleanProperty lunes_c;
    private BooleanProperty martes_c;
    private BooleanProperty miercoles_c;
    private BooleanProperty jueves_c;
    private BooleanProperty viernes_c;
    private BooleanProperty sabado_c;
    private BooleanProperty domingo_c;
    private StringProperty id_medico;
    //Variables de clase
    private static final Logger logger = Logger.getLogger(diasConsulta.class.getName());
    Vitro dbConn = new Vitro();
    Auxiliar aux = new Auxiliar();
    /**
     * Constructor lleno de la clase dias de consulta
     * @param id_diasConsul
     * @param lunes_c
     * @param martes_c
     * @param miercoles_c
     * @param jueves_c
     * @param viernes_c
     * @param sabado_c
     * @param domingo_c
     * @param id_medico 
     */
    public diasConsulta(String id_diasConsul, Boolean lunes_c, Boolean martes_c, Boolean miercoles_c, 
            Boolean jueves_c, Boolean viernes_c, Boolean sabado_c, Boolean domingo_c, String id_medico) {
        this.id_diasConsul = new SimpleStringProperty(id_diasConsul);
        this.lunes_c = new SimpleBooleanProperty(lunes_c);
        this.martes_c = new SimpleBooleanProperty(martes_c);
        this.miercoles_c = new SimpleBooleanProperty(miercoles_c);
        this.jueves_c = new SimpleBooleanProperty(jueves_c);
        this.viernes_c = new SimpleBooleanProperty(viernes_c);
        this.sabado_c = new SimpleBooleanProperty(sabado_c);
        this.domingo_c = new SimpleBooleanProperty(domingo_c);
        this.id_medico = new SimpleStringProperty(id_medico);
    }

    /**
     * Consturctor vacio de la clase dias de consulta
     */
    public diasConsulta() {
    }
    /**
     * clase astracta de task
     * @param <T> 
     */
    abstract class DBTask<T> extends Task<T> {
        DBTask() {
          setOnFailed(t -> logger.log(Level.SEVERE, null, getException()));
        }
    }
    /**
     * agrega a la base de datos los dias que da consulta el medico seleccionado 
     * @param id_diasConsul
     * @param lunes_c
     * @param martes_c
     * @param miercoles_c
     * @param jueves_c
     * @param viernes_c
     * @param sabado_c
     * @param domingo_c
     * @param id_medico
     * @param conex 
     */
    public void agregaDiasConsulta(String id_diasConsul, Boolean lunes_c, Boolean martes_c, Boolean miercoles_c, 
            Boolean jueves_c, Boolean viernes_c, Boolean sabado_c, Boolean domingo_c, String id_medico,
            Connection conex){
        String sqlst =  "INSERT INTO diasconsulta (id_diasConsul, lunes_c,\n"+
                        "martes_c, miercoles_c, jueves_c, viernes_c, sabado_c, domingo_c, id_medico)"+
                        "VALUES (?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_diasConsul);
            sttm.setBoolean (2, lunes_c);
            sttm.setBoolean (3, martes_c);
            sttm.setBoolean (4, miercoles_c);
            sttm.setBoolean (5, jueves_c);
            sttm.setBoolean (6, viernes_c);
            sttm.setBoolean (7, sabado_c);
            sttm.setBoolean (8, domingo_c);
            sttm.setString  (9, id_medico);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * actualiza los dias de consulta del medico seleccionado
     * @param id_diasConsul
     * @param lunes_c
     * @param martes_c
     * @param miercoles_c
     * @param jueves_c
     * @param viernes_c
     * @param sabado_c
     * @param domingo_c
     * @param id_medico
     * @param conex 
     */
    public void actualizaDiasConsulta(String id_diasConsul, Boolean lunes_c, Boolean martes_c, Boolean miercoles_c, 
            Boolean jueves_c, Boolean viernes_c, Boolean sabado_c, Boolean domingo_c, String id_medico,
            Connection conex){
        String sqlst = " UPDATE diasConsulta SET \n" +
                       " id_diasConsul=?, \n" +
                       " lunes_c=?, \n" +
                       " martes_c=?, \n" +
                       " miercoles_c=?, \n" +
                       " jueves_c=?, \n" +
                       " viernes_c=?, \n" +
                       " sabado_c=?, \n" +
                       " domingo_c=?, \n" +
                       " id_medico=? \n" +
                       " WHERE id_diasConsul=?";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_diasConsul);
            sttm.setBoolean (2, lunes_c);
            sttm.setBoolean (3, martes_c);
            sttm.setBoolean (4, miercoles_c);
            sttm.setBoolean (5, jueves_c);
            sttm.setBoolean (6, viernes_c);
            sttm.setBoolean (7, sabado_c);
            sttm.setBoolean (8, domingo_c);
            sttm.setString  (9, id_medico);
            sttm.setString  (10,id_diasConsul);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * clase que regresa lista de dias que trabaja o no el medico
     */
    public class listaDiasConsultaMedicoTask extends DBTask<ObservableList<Integer>> {
        String idUs;

        /**
         * constructor de clase 
         * @param idUs 
         */
        public listaDiasConsultaMedicoTask(String idUs) {
            this.idUs = idUs;
        }
        
        @Override
        protected ObservableList<Integer> call() throws Exception {
            ObservableList<Integer> listadiasConsulta = FXCollections.observableArrayList();
            String sql ="SELECT id_diasConsul, lunes_c,\n"+
                        "martes_c, miercoles_c, jueves_c, viernes_c, sabado_c, domingo_c, id_medico\n"+
                        "FROM diasconsulta WHERE id_medico = '"+idUs+"';";
            try(Connection conex = dbConn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
                if (res.next()) {
                    if (res.getBoolean("lunes_c")) {
                        listadiasConsulta.add(1);
                    }
                    if (res.getBoolean("martes_c")) {
                        listadiasConsulta.add(2);
                    }
                    if (res.getBoolean("miercoles_c")) {
                        listadiasConsulta.add(3);
                    }
                    if (res.getBoolean("jueves_c")) {
                        listadiasConsulta.add(4);
                    }
                    if (res.getBoolean("viernes_c")) {
                        listadiasConsulta.add(5);
                    }
                    if (res.getBoolean("sabado_c")) {
                        listadiasConsulta.add(6);
                    }
                    if (res.getBoolean("domingo_c")) {
                        listadiasConsulta.add(7);
                    }
                }
                
            }catch (SQLException ex) {
                logger.log(Level.SEVERE, sql, ex);
            }
            return listadiasConsulta;
        }
        
    }
    
    /**
     * Regresa los dias que da consulta el medico seleccionado
     * @param conex
     * @param idUs
     * @return 
     */
    public diasConsulta unSoloDiasConsultaMedico(Connection conex, String idUs){
        diasConsulta diasConsul = null;
        String sql ="SELECT id_diasConsul, lunes_c,\n"+
                    "martes_c, miercoles_c, jueves_c, viernes_c, sabado_c, domingo_c, id_medico\n"+
                    "FROM diasconsulta WHERE id_medico = '"+idUs+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            if (res.next()) {
                diasConsul = new diasConsulta(  res.getString ("id_diasConsul"),
                                                res.getBoolean("lunes_c"),
                                                res.getBoolean("martes_c"),
                                                res.getBoolean("miercoles_c"),
                                                res.getBoolean("jueves_c"),
                                                res.getBoolean("viernes_c"),
                                                res.getBoolean("sabado_c"),
                                                res.getBoolean("domingo_c"),
                                                res.getString ("id_medico"));
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return diasConsul;
    }
    
    
    /***********************************************************************************/
    //Setters and Getters de clase
    public final void setId_diasConsul(String value) {
        id_diasConsul.set(value);
    }

    public final String getId_diasConsul() {
        return id_diasConsul.get();
    }

    public final StringProperty id_diasConsulProperty() {
        return id_diasConsul;
    }

    public final void setLunes_c(Boolean value) {
        lunes_c.set(value);
    }

    public final Boolean getLunes_c() {
        return lunes_c.get();
    }

    public final BooleanProperty lunes_cProperty() {
        return lunes_c;
    }

    public final void setMartes_c(Boolean value) {
        martes_c.set(value);
    }

    public final Boolean getMartes_c() {
        return martes_c.get();
    }

    public final BooleanProperty martes_cProperty() {
        return martes_c;
    }

    public final void setMiercoles_c(Boolean value) {
        miercoles_c.set(value);
    }

    public final Boolean getMiercoles_c() {
        return miercoles_c.get();
    }

    public final BooleanProperty miercoles_cProperty() {
        return miercoles_c;
    }

    public final void setJueves_c(Boolean value) {
        jueves_c.set(value);
    }

    public final Boolean getJueves_c() {
        return jueves_c.get();
    }

    public final BooleanProperty jueves_cProperty() {
        return jueves_c;
    }

    public final void setViernes_c(Boolean value) {
        viernes_c.set(value);
    }

    public final Boolean getViernes_c() {
        return viernes_c.get();
    }

    public final BooleanProperty viernes_cProperty() {
        return viernes_c;
    }

    public final void setSabado_c(Boolean value) {
        sabado_c.set(value);
    }

    public final Boolean getSabado_c() {
        return sabado_c.get();
    }

    public final BooleanProperty sabado_cProperty() {
        return sabado_c;
    }

    public final void setDomingo_c(Boolean value) {
        domingo_c.set(value);
    }

    public final Boolean getDomingo_c() {
        return domingo_c.get();
    }

    public final BooleanProperty domingo_cProperty() {
        return domingo_c;
    }

    public final void setId_medico(String value) {
        id_medico.set(value);
    }

    public final String getId_medico() {
        return id_medico.get();
    }

    public final StringProperty id_medicoProperty() {
        return id_medico;
    }
    
    
    
    
}
