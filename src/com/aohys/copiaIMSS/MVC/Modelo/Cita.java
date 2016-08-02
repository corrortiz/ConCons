/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo;

import com.aohys.copiaIMSS.BaseDatos.MysqlConnectionSingle;
import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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

public class Cita {
    //variables de clase
    private StringProperty id_cit;
    private Date fecha_cit;
    private Time hora_cit;
    private BooleanProperty primVis_cit;
    private StringProperty id_Usuario;
    private StringProperty id_Paciente;

    //Variables de clase
    private static final Logger logger = Logger.getLogger(Cita.class.getName());
    Vitro dbConn = new Vitro();
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
     * constructor vacio
     * @param id_cit
     * @param fecha_cit
     * @param hora_cit
     * @param primVis_cit
     * @param id_Usuario
     * @param id_Paciente 
     */
    public Cita(String id_cit, Date fecha_cit, Time hora_cit, 
            Boolean primVis_cit, String id_Usuario, String id_Paciente) {
        this.id_cit = new SimpleStringProperty(id_cit);
        this.fecha_cit = fecha_cit;
        this.hora_cit = hora_cit;
        this.primVis_cit = new SimpleBooleanProperty(primVis_cit);
        this.id_Usuario = new SimpleStringProperty(id_Usuario);
        this.id_Paciente = new SimpleStringProperty(id_Paciente);
    }

    /**
     * constructor vacio
     */
    public Cita() {
    }

    /**
     * clase para agregar citas a la base de datos 
     */
    public class agregaCitaCsl extends DBTask<Void> {
        String id_cit;
        Date fecha_cit; 
        Time hora_cit; 
        Boolean primVis_cit; 
        String id_Usuario;
        String id_Paciente;
        /**
         * constructor de la clase
         * @param id_cit
         * @param fecha_cit
         * @param hora_cit
         * @param primVis_cit
         * @param id_Usuario
         * @param id_Paciente 
         */
        public agregaCitaCsl(String id_cit, Date fecha_cit, Time hora_cit, 
                Boolean primVis_cit, String id_Usuario, String id_Paciente) {
            this.id_cit = id_cit;
            this.fecha_cit = fecha_cit;
            this.hora_cit = hora_cit;
            this.primVis_cit = primVis_cit;
            this.id_Usuario = id_Usuario;
            this.id_Paciente = id_Paciente;
        }
        
        @Override
        protected Void call() throws Exception {
            String sqlst =  "INSERT INTO Cita (id_cit, fecha_cit,\n"+
                        "hora_cit, primVis_cit, id_medico, id_Paciente)"+
                        "VALUES (?,?,?,?,?,?)";
            try(Connection conex = dbConn.conectarBD();
                PreparedStatement sttm = conex.prepareStatement(sqlst)) {
                conex.setAutoCommit(false);
                sttm.setString  (1, id_cit);
                sttm.setDate    (2, fecha_cit);
                sttm.setTime    (3, hora_cit);
                sttm.setBoolean (4, primVis_cit);
                sttm.setString  (5, id_Usuario);
                sttm.setString  (6, id_Paciente);
                sttm.addBatch();
                sttm.executeBatch();
                conex.commit();
                
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return null;
        }
    }
    
    /**
     * clase que regresa una lista de citar en la fecha con ese medico 
     */
    public class cargaCitasFechaUsuarioTask extends DBTask<ObservableList<Cita> > {
        Date fecha; 
        String idUs;
        /**
         * constructor de la clase
         * @param fecha
         * @param idUs 
         */
        public cargaCitasFechaUsuarioTask(Date fecha, String idUs) {
            this.fecha = fecha;
            this.idUs = idUs;
        }
        
        @Override
        protected ObservableList<Cita> call() throws Exception {
            ObservableList<Cita> listaCita = FXCollections.observableArrayList();
            String sql ="SELECT id_cit, fecha_cit,\n" +
                        "hora_cit, primVis_cit, id_medico, id_Paciente\n" +
                        "FROM Cita WHERE fecha_cit = '"+fecha+"' AND id_medico = '"+idUs+"'\n" +
                        "ORDER BY hora_cit ASC;";
            try(Connection conex = new MysqlConnectionSingle().conectarBDSingleConnection();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
                while (res.next()) {
                    listaCita.add(new Cita( res.getString ("id_cit"), 
                                            res.getDate   ("fecha_cit"),
                                            res.getTime   ("hora_cit"), 
                                            res.getBoolean("primVis_cit"),
                                            res.getString ("id_medico"),
                                            res.getString ("id_Paciente")));
                }
            }catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return listaCita;
        }
    }
   
    /**
     * clase que legresa lista de citas del usuario
     */
    public class listaCitasTotalUsuarioTask extends DBTask<ObservableList<Cita> > {
        String idUs;
        /**
         * constructor de clase 
         * @param idUs 
         */
        public listaCitasTotalUsuarioTask(String idUs) {
            this.idUs = idUs;
        }
        
        @Override
        protected ObservableList<Cita> call() throws Exception {
            Date dia = Date.valueOf(LocalDate.now());
            ObservableList<Cita> listaCita = FXCollections.observableArrayList();
            String sql ="SELECT id_cit, fecha_cit,\n" +
                    "hora_cit, primVis_cit, id_medico, id_Paciente\n" +
                    "FROM Cita WHERE id_Paciente = '"+idUs+"'\n" +
                    "AND fecha_cit >= '"+dia+"'\n" +
                    "ORDER BY fecha_cit DESC;";
            try(Connection conex = new MysqlConnectionSingle().conectarBDSingleConnection();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
                while (res.next()) {
                    listaCita.add(new Cita( res.getString ("id_cit"), 
                                            res.getDate   ("fecha_cit"),
                                            res.getTime   ("hora_cit"), 
                                            res.getBoolean("primVis_cit"),
                                            res.getString ("id_medico"),
                                            res.getString ("id_Paciente")));
                }
            }catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return listaCita;
        }
    }
    
    
    /**
     * Borrar cita seleccionada 
     * @param Dato
     * @param conex 
     */
    public void borrarCita(String Dato, Connection conex){
        String sttm = "DELETE FROM Cita WHERE id_cit = '"+Dato+"'";    
        try(PreparedStatement pttm = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            pttm.addBatch();
            pttm.executeBatch();
            conex.commit();
            aux.informacionUs("La cita fue borrada", "la cita fue borrada", 
                    "La cita fue borrada exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * borra todas las citas del paciente
     * @param idPaciente
     * @param conex 
     * @return  
     */
    public boolean borrarCitasPaciente(String idPaciente, Connection conex){
        String sttm = "DELETE FROM Cita WHERE id_Paciente = '"+idPaciente+"'";    
        try(PreparedStatement pttm = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            pttm.addBatch();
            pttm.executeBatch();
            conex.commit();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * actualiza la cita seleccionada 
     * @param id_cit
     * @param fecha_cit
     * @param hora_cit
     * @param primVis_cit
     * @param id_Usuario
     * @param id_Paciente
     * @param conex 
     */
    public void actualizaCita(String id_cit, Date fecha_cit, Time hora_cit, 
        Boolean primVis_cit, String id_Usuario, String id_Paciente, Connection conex){
        String sqlst = " UPDATE Cita SET \n" +
                       " id_cit=?, \n" +
                       " fecha_cit=?, \n" +
                       " hora_cit=?, \n" +
                       " primVis_cit=?, \n" +
                       " id_medico=?, \n" +
                       " id_Paciente=? \n" +
                       " WHERE id_cit=?";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_cit);
            sttm.setDate    (2, fecha_cit);
            sttm.setTime    (3, hora_cit);
            sttm.setBoolean (4, primVis_cit);
            sttm.setString  (5, id_Usuario);
            sttm.setString  (6, id_Paciente);
            sttm.setString  (7, id_cit);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("La cita ha sido modificada", 
                    "La cita ha sido modificada", 
                    "La cita ha sido modificada en la base de datos exitosamente");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * carga la seleccionada 
     * @param idCit
     * @param conex
     * @return 
     */
    public Cita cargaSoloUno(String idCit, Connection conex){
        Cita cit = null;
        String sttm = "SELECT id_cit, fecha_cit,\n" +
                      "hora_cit, primVis_cit, id_medico, id_Paciente\n" +
                      "FROM Cita WHERE id_cit = '"+idCit+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                cit = new Cita( res.getString ("id_cit"), 
                                        res.getDate   ("fecha_cit"),
                                        res.getTime   ("hora_cit"), 
                                        res.getBoolean("primVis_cit"),
                                        res.getString ("id_medico"),
                                        res.getString ("id_Paciente"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cit;
    }
    
    /**
     * clase que regresa una lista de las fechas validas
     */
    public class horariosCitasFechaUsuarioTask extends DBTask<ObservableList<LocalTime>> {
        Date fecha;
        String idUs;
        /**
         * constructor lleno de la clase 
         * @param fecha
         * @param idUs 
         */
        public horariosCitasFechaUsuarioTask(Date fecha, String idUs) {
            this.fecha = fecha;
            this.idUs = idUs;
        }
        
        @Override
        protected ObservableList<LocalTime> call() throws Exception {
            ObservableList<LocalTime> listaCita = FXCollections.observableArrayList();
            String sql ="SELECT hora_cit\n"+
                        "FROM Cita WHERE fecha_cit = '"+fecha+"' AND id_medico = '"+idUs+"'\n" +
                        "ORDER BY hora_cit ASC;";
            try(Connection conex = new MysqlConnectionSingle().conectarBDSingleConnection();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
                while (res.next()) {
                    listaCita.add(res.getTime("hora_cit").toLocalTime());
                }
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
            return listaCita;
        }
        
    }
    
    
    /**********************************************************************************************************/
    //Setters and getters
    public final void setId_cit(String value) {
        id_cit.set(value);
    }

    public final String getId_cit() {
        return id_cit.get();
    }

    public final StringProperty id_citProperty() {
        return id_cit;
    }

    public final void setPrimVis_cit(Boolean value) {
        primVis_cit.set(value);
    }

    public final Boolean getPrimVis_cit() {
        return primVis_cit.get();
    }

    public final BooleanProperty primVis_citProperty() {
        return primVis_cit;
    }

    public final void setId_Usuario(String value) {
        id_Usuario.set(value);
    }

    public final String getId_Usuario() {
        return id_Usuario.get();
    }

    public final StringProperty id_UsuarioProperty() {
        return id_Usuario;
    }

    public final void setId_Paciente(String value) {
        id_Paciente.set(value);
    }

    public final String getId_Paciente() {
        return id_Paciente.get();
    }

    public final StringProperty id_PacienteProperty() {
        return id_Paciente;
    }

    public Date getFecha_cit() {
        return fecha_cit;
    }

    public void setFecha_cit(Date fecha_cit) {
        this.fecha_cit = fecha_cit;
    }

    public Time getHora_cit() {
        return hora_cit;
    }

    public void setHora_cit(Time hora_cit) {
        this.hora_cit = hora_cit;
    }
    
    
    
    
	
}
