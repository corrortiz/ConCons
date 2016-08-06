/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo.ModeloCita;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.BaseDatos.MysqlConnectionSingle;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * @author Alejandro Ortiz Corro
 */

public class peridoVacaMedico {
    //Variables de clase
    private StringProperty id_peridoVacaMedico;
    private Date inicia_pvm;
    private Date termina_pvm;
    private StringProperty id_medico;
    //Variables de clase
    private static final Logger logger = Logger.getLogger(peridoVacaMedico.class.getName());
    Hikari dbConn = new Hikari();
    Auxiliar aux = new Auxiliar();
    MysqlConnectionSingle singleDBConn = new MysqlConnectionSingle();
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
     * constructor de la clase de perido vacaional 
     * @param id_peridoVacaMedico
     * @param inicia_pvm
     * @param termina_pvm
     * @param id_medico 
     */
    public peridoVacaMedico(String id_peridoVacaMedico, Date inicia_pvm, Date termina_pvm, 
            String id_medico) {
        this.id_peridoVacaMedico = new SimpleStringProperty(id_peridoVacaMedico);
        this.inicia_pvm = inicia_pvm;
        this.termina_pvm = termina_pvm;
        this.id_medico = new SimpleStringProperty(id_medico);
    }

    /**
     * constructor vacio de la clase de perido vacaional
     */
    public peridoVacaMedico() {
    }

    /**
     * agrega periodo vacacional para el medico seleccionado 
     * @param id_peridoVacaMedico
     * @param inicia_pvm
     * @param termina_pvm
     * @param id_medico
     * @param conex 
     */
    public void agregaPeridoVacacional(String id_peridoVacaMedico, Date inicia_pvm, Date termina_pvm, 
            String id_medico, Connection conex){
        String sqlst =  "INSERT INTO peridovacamedico (id_peridoVacaMedico, inicia_pvm,\n"+
                        "termina_pvm, id_medico)"+
                        "VALUES (?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_peridoVacaMedico);
            sttm.setDate    (2, inicia_pvm);
            sttm.setDate    (3, termina_pvm);
            sttm.setString  (4, id_medico);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    /**
     * clase que realiza una lista de citas en esa fecha y con ese medico
     */
    public class listaPeridoVacacionalTask extends DBTask<ObservableList<peridoVacaMedico>> {
        String idUs;
        /**
         * constructor lleno
         * @param idUs 
         */
        public listaPeridoVacacionalTask(String idUs) {
            this.idUs = idUs;
        }
        
        @Override
        protected ObservableList<peridoVacaMedico> call() throws Exception {
            ObservableList<peridoVacaMedico> listaperidoVacaMedico = FXCollections.observableArrayList();
            String sql ="SELECT id_peridoVacaMedico, inicia_pvm,\n"+
                        "termina_pvm, id_medico\n" +
                        "FROM peridovacamedico WHERE id_medico = '"+idUs+"';";
            try(Connection conex = dbConn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
                while (res.next()) {
                    listaperidoVacaMedico.add(new peridoVacaMedico( 
                                            res.getString ("id_peridoVacaMedico"), 
                                            res.getDate   ("inicia_pvm"),
                                            res.getDate   ("termina_pvm"), 
                                            res.getString ("id_medico")));
                }
            }catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return listaperidoVacaMedico;
        }
        
    }
    
    /**
     * borra perido vacaional del medico selecionado
     * @param Dato
     * @param conex 
     */
    public void borraPeridoVacacional(String Dato, Connection conex){
        String sttm = "DELETE FROM peridoVacaMedico WHERE id_peridoVacaMedico = '"+Dato+"'";    
        try(PreparedStatement pttm = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            pttm.addBatch();
            pttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * actualiza perido vacaional del medico
     * @param id_peridoVacaMedico
     * @param inicia_pvm
     * @param termina_pvm
     * @param id_medico
     * @param conex 
     */
    public void actualizaPeridoVacaMedico(String id_peridoVacaMedico, Date inicia_pvm, Date termina_pvm, 
            String id_medico, Connection conex){
        String sqlst = " UPDATE peridoVacaMedico SET \n" +
                       " id_peridoVacaMedico=?, \n" +
                       " inicia_pvm=?, \n" +
                       " termina_pvm=?, \n" +
                       " id_medico=? \n" +
                       " WHERE id_peridoVacaMedico=?";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_peridoVacaMedico);
            sttm.setDate    (2, inicia_pvm);
            sttm.setDate    (3, termina_pvm);
            sttm.setString  (4, id_medico);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /************************************************************************************/
    //Getter and Setter
    public final void setId_peridoVacaMedico(String value) {
        id_peridoVacaMedico.set(value);
    }

    public final String getId_peridoVacaMedico() {
        return id_peridoVacaMedico.get();
    }

    public final StringProperty id_peridoVacaMedicoProperty() {
        return id_peridoVacaMedico;
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

    public Date getInicia_pvm() {
        return inicia_pvm;
    }

    public void setInicia_pvm(Date inicia_pvm) {
        this.inicia_pvm = inicia_pvm;
    }

    public Date getTermina_pvm() {
        return termina_pvm;
    }

    public void setTermina_pvm(Date termina_pvm) {
        this.termina_pvm = termina_pvm;
    }
    
    
    
    
    
}
