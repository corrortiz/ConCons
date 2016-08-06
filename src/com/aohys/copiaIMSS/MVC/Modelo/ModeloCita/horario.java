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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * @author Alejandro Ortiz Corro
 */

public class horario {
    //Variables de clase
    private StringProperty id_horario;
    private Time entra_horario;
    private Time sale_horario;
    private IntegerProperty duacion_consul;
    private StringProperty id_medico;
    //Variables de clase
    private static final Logger logger = Logger.getLogger(peridoVacaMedico.class.getName());
    Hikari dbConn = new Hikari();
    Auxiliar aux = new Auxiliar();
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
     * Constructor lleno de la clase horario de medico
     * @param id_horario
     * @param entra_horario
     * @param sale_horario
     * @param duacion_consul
     * @param id_medico 
     */
    public horario(String id_horario, Time entra_horario, Time sale_horario, Integer duacion_consul, 
            String id_medico) {
        this.id_horario = new SimpleStringProperty(id_horario);
        this.entra_horario = entra_horario;
        this.sale_horario = sale_horario;
        this.duacion_consul = new SimpleIntegerProperty(duacion_consul);
        this.id_medico = new SimpleStringProperty(id_medico);
    }

    /**
     * Constructor vacio de la clase horario de medico
     */
    public horario() {
    }

    /**
     * agrega horario del medico seleccionado
     * @param id_horario
     * @param entra_horario
     * @param sale_horario
     * @param duacion_consul
     * @param id_medico
     * @param conex 
     */
    public void agregaHorario(String id_horario, Time entra_horario, Time sale_horario, 
            Integer duacion_consul, String id_medico, Connection conex){
        String sqlst =  "INSERT INTO horario (id_horario, entra_horario,\n"+
                        "sale_horario, duacion_consul, id_medico)\n"+
                        "VALUES (?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_horario);
            sttm.setTime    (2, entra_horario);
            sttm.setTime    (3, sale_horario);
            sttm.setInt     (4, duacion_consul);
            sttm.setString  (5, id_medico);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Carga un horario unico de medico seleccionado
     * @param idCit
     * @param conex
     * @return 
     */
    public horario cargaSoloUno(String idCit, Connection conex){
        horario horario = null;
        String sttm = "SELECT id_horario, entra_horario,\n"+
                      "sale_horario, duacion_consul, id_medico\n" +
                      "FROM horario WHERE id_medico = '"+idCit+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                horario = new horario( res.getString("id_horario"), 
                                        res.getTime ("entra_horario"),
                                        res.getTime ("sale_horario"), 
                                        res.getInt  ("duacion_consul"),
                                        res.getString ("id_medico"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return horario;
    }
    
    /**
     * Actualiza el horario del medico
     * @param id_horario
     * @param entra_horario
     * @param sale_horario
     * @param duacion_consul
     * @param id_medico
     * @param conex 
     */
    public void actualizaHorario(String id_horario, Time entra_horario, Time sale_horario, 
            Integer duacion_consul, String id_medico, Connection conex){
        String sqlst = " UPDATE horario SET \n" +
                       " id_horario=?, \n" +
                       " entra_horario=?, \n" +
                       " sale_horario=?, \n" +
                       " duacion_consul=?, \n" +
                       " id_medico=?\n" +
                       " WHERE id_horario=?";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_horario);
            sttm.setTime    (2, entra_horario);
            sttm.setTime    (3, sale_horario);
            sttm.setInt     (4, duacion_consul);
            sttm.setString  (5, id_medico);
            sttm.setString  (6, id_horario);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * regresa una lista de horarios disponibles para agendar
     */
    public class listaHorarioDisponibleTask extends DBTask<ObservableList<LocalTime>> {
        String idCit;
        /**
         * constructor lleno
         * @param idCit 
         */
        public listaHorarioDisponibleTask(String idCit) {
            this.idCit = idCit;
        }
        
        @Override
        protected ObservableList<LocalTime> call() throws Exception {
           ObservableList<LocalTime> horario = FXCollections.observableArrayList();
            String sttm = "SELECT entra_horario,\n"+
                          "sale_horario, duacion_consul\n" +
                          "FROM horario WHERE id_medico = '"+idCit+"';";
            try(Connection conex = dbConn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sttm);
                ResultSet res = stta.executeQuery(); ) {
                if (res.next()) {
                    LocalTime entrada = res.getTime ("entra_horario").toLocalTime();
                    LocalTime salida = res.getTime ("sale_horario").toLocalTime();
                    long tiempor = ChronoUnit.HOURS.between(entrada, salida);
                    int periodos = (int) ((60 / res.getInt("duacion_consul"))*tiempor);
                    for (int i = 0; i < periodos; i++) {
                        LocalTime agregar = entrada.plusMinutes(res.getInt("duacion_consul")*i);
                        horario.add(agregar);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return horario;
        }
        
    }
    
    
    /***********************************************************************************************/
    //Setters and Getters
    public final void setId_horario(String value) {
        id_horario.set(value);
    }

    public final String getId_horario() {
        return id_horario.get();
    }

    public final StringProperty id_horarioProperty() {
        return id_horario;
    }

    public final void setDuacion_consul(Integer value) {
        duacion_consul.set(value);
    }

    public final Integer getDuacion_consul() {
        return duacion_consul.get();
    }

    public final IntegerProperty duacion_consulProperty() {
        return duacion_consul;
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

    public Time getEntra_horario() {
        return entra_horario;
    }

    public void setEntra_horario(Time entra_horario) {
        this.entra_horario = entra_horario;
    }

    public Time getSale_horario() {
        return sale_horario;
    }

    public void setSale_horario(Time sale_horario) {
        this.sale_horario = sale_horario;
    }
    
    
    
    
}
