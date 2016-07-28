/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta.Diagnostico;
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

public class Rayos {
    private StringProperty id_rayos;
    private Date fecha_rayos;
    private StringProperty diagnostico_rayos;
    private StringProperty nombre_rayos;
    private StringProperty indicaciones_rayos;
    private StringProperty id_usuario;
    private StringProperty id_medico;
    private static final Logger logger = Logger.getLogger(Diagnostico.class.getName());
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
     * constructor lleno de la clase rayos x
     * @param id_rayos
     * @param fecha_rayos
     * @param diagnostico_rayos
     * @param nombre_rayos
     * @param indicaciones_rayos
     * @param id_usuario
     * @param id_medico 
     */
    public Rayos(String id_rayos, Date fecha_rayos, String diagnostico_rayos, String nombre_rayos, 
            String indicaciones_rayos, String id_usuario, String id_medico) {
        this.id_rayos = new SimpleStringProperty(id_rayos);
        this.fecha_rayos = fecha_rayos;
        this.diagnostico_rayos = new SimpleStringProperty(diagnostico_rayos);
        this.nombre_rayos = new SimpleStringProperty(nombre_rayos);
        this.indicaciones_rayos = new SimpleStringProperty(indicaciones_rayos);
        this.id_usuario = new SimpleStringProperty(id_usuario);
        this.id_medico = new SimpleStringProperty(id_medico);
    }

    /**
     * constructor vacio de la clase rayos x
     */
    public Rayos() {
    }
    
    
    public void agregaProcedimiento(String id_rayos, Date fecha_rayos, String diagnostico_rayos, String nombre_rayos, 
            String indicaciones_rayos, String id_usuario, String id_medico, Connection conex){
        String sqlst =  "INSERT INTO `rayos`\n" +
                            "(`id_rayos`,\n" +
                            "`fecha_rayos`,\n" +
                            "`diagnostico_rayos`,\n" +
                            "`nombre_rayos`,\n" +
                            "`indicaciones_rayos`,\n" +
                            "`is_usuario`,\n" +
                            "`id_medico`)\n" +
                        "VALUES (?,?,?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1,id_rayos);
            sttm.setDate    (2,fecha_rayos);
            sttm.setString  (3,diagnostico_rayos);
            sttm.setString  (4,nombre_rayos);
            sttm.setString  (5,indicaciones_rayos);
            sttm.setString  (6,id_usuario);
            sttm.setString  (7,id_medico);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("El procedimiento ha sido agregado", 
                    "El procedimiento ha sido agregado", 
                    "El procedimiento ha sido agregado exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public class agregaProcedimientoTask extends DBTask<Void> {
        String id_rayos; 
        Date fecha_rayos; 
        String diagnostico_rayos; 
        String nombre_rayos; 
        String indicaciones_rayos; 
        String id_usuario; 
        String id_medico;
        /**
         * constructor de clase
         * @param id_rayos
         * @param fecha_rayos
         * @param diagnostico_rayos
         * @param nombre_rayos
         * @param indicaciones_rayos
         * @param id_usuario
         * @param id_medico 
         */
        public agregaProcedimientoTask(String id_rayos, Date fecha_rayos, String diagnostico_rayos, 
                String nombre_rayos, String indicaciones_rayos, String id_usuario, String id_medico) {
            this.id_rayos = id_rayos;
            this.fecha_rayos = fecha_rayos;
            this.diagnostico_rayos = diagnostico_rayos;
            this.nombre_rayos = nombre_rayos;
            this.indicaciones_rayos = indicaciones_rayos;
            this.id_usuario = id_usuario;
            this.id_medico = id_medico;
        }
        
        @Override
        protected Void call() throws Exception {
            String sqlst =  "INSERT INTO `rayos`\n" +
                            "(`id_rayos`,\n" +
                            "`fecha_rayos`,\n" +
                            "`diagnostico_rayos`,\n" +
                            "`nombre_rayos`,\n" +
                            "`indicaciones_rayos`,\n" +
                            "`is_usuario`,\n" +
                            "`id_medico`)\n" +
                        "VALUES (?,?,?,?,?,?,?)";
            try(Connection conex = dbConn.conectarBD();
                    PreparedStatement sttm = conex.prepareStatement(sqlst)) {
                conex.setAutoCommit(false);
                sttm.setString  (1,id_rayos);
                sttm.setDate    (2,fecha_rayos);
                sttm.setString  (3,diagnostico_rayos);
                sttm.setString  (4,nombre_rayos);
                sttm.setString  (5,indicaciones_rayos);
                sttm.setString  (6,id_usuario);
                sttm.setString  (7,id_medico);
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
     * carga solo el procedimiento seleccionado
     * @param idProce
     * @param conex
     * @return 
     */
    public Rayos cargaSoloUno(String idProce, Connection conex){
        Rayos somametropia = null;
        String sttm = "SELECT `rayos`.`id_rayos`,\n" +
                    "    `rayos`.`fecha_rayos`,\n" +
                    "    `rayos`.`diagnostico_rayos`,\n" +
                    "    `rayos`.`nombre_rayos`,\n" +
                    "    `rayos`.`indicaciones_rayos`,\n" +
                    "    `rayos`.`is_usuario`,\n" +
                    "    `rayos`.`id_medico`\n" +
                    "FROM `rayos` WHERE is_usuario = '"+idProce+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                somametropia = new Rayos( 
                                    res.getString  ("id_rayos"),
                                    res.getDate    ("fecha_rayos"),
                                    res.getString  ("diagnostico_rayos"),
                                    res.getString  ("nombre_rayos"),
                                    res.getString  ("indicaciones_rayos"),
                                    res.getString  ("is_usuario"),
                                    res.getString  ("id_medico"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return somametropia;
    }

    
    /**
     * carga lista de imagenes de pacientes 
     */
    public class listaRayosPacienteTask extends DBTask<ObservableList<Rayos>> {
         String idPaciente;

        public listaRayosPacienteTask(String idPaciente) {
            this.idPaciente = idPaciente;
        }
         
        @Override
        protected ObservableList<Rayos> call() throws Exception {
            ObservableList<Rayos> listaRayos = FXCollections.observableArrayList();
            String sql = "SELECT `rayos`.`id_rayos`,\n" +
                        "    `rayos`.`fecha_rayos`,\n" +
                        "    `rayos`.`diagnostico_rayos`,\n" +
                        "    `rayos`.`nombre_rayos`,\n" +
                        "    `rayos`.`indicaciones_rayos`,\n" +
                        "    `rayos`.`is_usuario`,\n" +
                        "    `rayos`.`id_medico`\n" +
                        "FROM `rayos` WHERE is_usuario = '"+idPaciente+"'\n"+
                        "ORDER BY fecha_rayos ASC;";
            try(Connection conex = dbConn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
                while (res.next()) {
                    listaRayos.add(new Rayos( 
                                        res.getString  ("id_rayos"),
                                        res.getDate    ("fecha_rayos"),
                                        res.getString  ("diagnostico_rayos"),
                                        res.getString  ("nombre_rayos"),
                                        res.getString  ("indicaciones_rayos"),
                                        res.getString  ("is_usuario"),
                                        res.getString  ("id_medico")));
                   }
               } catch (SQLException ex) {
                   ex.printStackTrace();
            }
            return listaRayos;
        }
     }
    
    /**
     * carga una lista de rayos pedidos ese dia 
     * @param conex
     * @param idPaciente
     * @param dia
     * @return 
     */
    public ObservableList<Rayos> listaRayosPacienteFecha(Connection conex, String idPaciente, Date dia){
        ObservableList<Rayos> listaRayos = FXCollections.observableArrayList();
        String sql = "SELECT `rayos`.`id_rayos`,\n" +
                    "    `rayos`.`fecha_rayos`,\n" +
                    "    `rayos`.`diagnostico_rayos`,\n" +
                    "    `rayos`.`nombre_rayos`,\n" +
                    "    `rayos`.`indicaciones_rayos`,\n" +
                    "    `rayos`.`is_usuario`,\n" +
                    "    `rayos`.`id_medico`\n" +
                    "FROM `rayos` WHERE is_usuario = '"+idPaciente+"'\n"+
                    "AND fecha_rayos = '"+dia+"'\n"+
                    "ORDER BY fecha_rayos ASC;";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaRayos.add(new Rayos( 
                                    res.getString  ("id_rayos"),
                                    res.getDate    ("fecha_rayos"),
                                    res.getString  ("diagnostico_rayos"),
                                    res.getString  ("nombre_rayos"),
                                    res.getString  ("indicaciones_rayos"),
                                    res.getString  ("is_usuario"),
                                    res.getString  ("id_medico")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaRayos;
    } 
    
    
    /**
     * borra el procedimiento seleccionado
     * @param Dato
     * @param conex 
     */
    public void borrarProce(String Dato, Connection conex){
        String sttm = "DELETE FROM rayos WHERE id_rayos = '"+Dato+"'";
        try(PreparedStatement stta = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            stta.addBatch();
            stta.executeBatch();
            conex.commit();
            aux.informacionUs("El procedimimiento ha sido borrado", 
                            "El procedimimiento ha sido borrado", 
                            "El procedimimiento ha sido borrado exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    


/*******************************************************************************/
    //Setter and Getter
    public final void setId_rayos(String value) {
        id_rayos.set(value);
    }

    public final String getId_rayos() {
        return id_rayos.get();
    }

    public final StringProperty id_rayosProperty() {
        return id_rayos;
    }

    public final void setDiagnostico_rayos(String value) {
        diagnostico_rayos.set(value);
    }

    public final String getDiagnostico_rayos() {
        return diagnostico_rayos.get();
    }

    public final StringProperty diagnostico_rayosProperty() {
        return diagnostico_rayos;
    }

    public final void setNombre_rayos(String value) {
        nombre_rayos.set(value);
    }

    public final String getNombre_rayos() {
        return nombre_rayos.get();
    }

    public final StringProperty nombre_rayosProperty() {
        return nombre_rayos;
    }

    public final void setIndicaciones_rayos(String value) {
        indicaciones_rayos.set(value);
    }

    public final String getIndicaciones_rayos() {
        return indicaciones_rayos.get();
    }

    public final StringProperty indicaciones_rayosProperty() {
        return indicaciones_rayos;
    }

    public final void setId_usuario(String value) {
        id_usuario.set(value);
    }

    public final String getId_usuario() {
        return id_usuario.get();
    }

    public final StringProperty id_usuarioProperty() {
        return id_usuario;
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

    public Date getFecha_rayos() {
        return fecha_rayos;
    }

    public void setFecha_rayos(Date fecha_rayos) {
        this.fecha_rayos = fecha_rayos;
    }
    
    
    
    

}
