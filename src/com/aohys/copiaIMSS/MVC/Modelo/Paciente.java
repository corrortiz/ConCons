/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * @author Alejandro Ortiz Corro
 */

public class Paciente {
    //Varibles de contol
    Paciente pacienteUnico;
    //Variables de clase
    private static final Logger logger = Logger.getLogger(Paciente.class.getName());
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
    //Variables
    private String id_paciente;
    private String nombre_paciente;
    private String apellido_paciente;
    private String apMaterno_paciente;
    private String sexo_paciente;
    private Date fechaNacimiento_paciente;
    private String curp_paciente;
    private int edad_paciente;
    private String telefono_paciente;
    private String correo_paciente;
    
    

    /**
     * Constructor lleno 
     * @param id_paciente
     * @param nombre_paciente
     * @param apellido_paciente
     * @param apMaterno_paciente
     * @param sexo_paciente
     * @param fechaNacimiento_paciente
     * @param curp_paciente
     * @param edad_paciente
     * @param telefono_paciente
     * @param correo_paciente 
     */
    public Paciente(String id_paciente, String nombre_paciente, 
                    String apellido_paciente, String apMaterno_paciente, 
                    String sexo_paciente, Date fechaNacimiento_paciente, 
                    String curp_paciente,int edad_paciente, String telefono_paciente, 
                    String correo_paciente) {
        this.id_paciente = id_paciente;
        this.nombre_paciente = nombre_paciente;
        this.apellido_paciente = apellido_paciente;
        this.apMaterno_paciente = apMaterno_paciente;
        this.sexo_paciente = sexo_paciente;
        this.fechaNacimiento_paciente = fechaNacimiento_paciente;
        this.curp_paciente = curp_paciente;
        this.edad_paciente = edad_paciente;
        this.telefono_paciente = telefono_paciente;
        this.correo_paciente = correo_paciente;
    }

   /**
    * agrega pacientes a la base de datos
    * @param id_paciente
    * @param nombre_paciente
    * @param apellido_paciente
    * @param apMaterno_paciente
    * @param sexo_paciente
    * @param fechaNacimiento_paciente
    * @param curp_paciente
    * @param edad_paciente
    * @param telefono_paciente
    * @param correo_paciente
    * @param conex 
    */
    public void agregarPacientes(     
                    String id_paciente, String nombre_paciente, 
                    String apellido_paciente, String apMaterno_paciente, 
                    String sexo_paciente, Date fechaNacimiento_paciente, 
                    String curp_paciente,int edad_paciente, String telefono_paciente, 
                    String correo_paciente, Connection conex){
        String sqlst = "INSERT INTO Paciente "+
                       " (id_paciente,  nombre_paciente, \n" +
                       " apellido_paciente,  apMaterno_paciente, \n" +
                       " sexo_paciente,  fechaNacimiento_paciente, \n" +
                       " curp_paciente, edad_paciente, \n" +
                       " telefono_paciente, correo_paciente)\n"+
                       " VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement sttm = conex.prepareStatement(sqlst)){
            conex.setAutoCommit(false);
            sttm.setString  (1, id_paciente);
            sttm.setString  (2, nombre_paciente);
            sttm.setString  (3, apellido_paciente);
            sttm.setString  (4, apMaterno_paciente);
            sttm.setString  (5, sexo_paciente);
            sttm.setDate    (6, fechaNacimiento_paciente);
            sttm.setString  (7, curp_paciente);
            sttm.setInt     (8, edad_paciente);
            sttm.setString  (9, telefono_paciente);
            sttm.setString  (10, correo_paciente);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Paciente guardado", "Paciente guardado", 
                    "El paciente ha sido agregado a la base de datos de manera exitosa");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * BorraPaciente
     * @param Dato 
     * @param conex 
     */
    public void borrarPaciente(String Dato, Connection conex){
        if (new Cita().borrarCitasPaciente(Dato, conex)) {
            String sqlst = "DELETE FROM Paciente WHERE id_paciente = '"+Dato+"'";    
            try (PreparedStatement sttm = conex.prepareStatement(sqlst)){
                conex.setAutoCommit(false);
                sttm.addBatch();
                sttm.executeBatch();
                conex.commit();
                aux.informacionUs("Paciente borrado", "Paciente borrado", 
                        "El paciente ha sido borrado de la base de datos de manera exitosa");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Para modificar
     * @param Dato
     * @param conex
     * @return 
     */
    public Paciente cargaSoloUno(String Dato){
        String sttm = "SELECT id_paciente,  nombre_paciente, \n" +
                       " apellido_paciente,  apMaterno_paciente, \n" +
                       " sexo_paciente,  fechaNacimiento_paciente, \n" +
                       " curp_paciente, edad_paciente, \n" +
                       " telefono_paciente, correo_paciente \n" +
                        "FROM Paciente WHERE id_paciente = '"+Dato+"'"; 
        try (   Connection conex = dbConn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sttm);
                ResultSet res = stta.executeQuery(sttm)){
            if (res.next()) {
               pacienteUnico = new Paciente(res.getString("id_paciente"), 
                                            res.getString("nombre_paciente"), 
                                            res.getString("apellido_paciente"), 
                                            res.getString("apMaterno_paciente"), 
                                            res.getString("sexo_paciente"), 
                                            res.getDate  ("fechaNacimiento_paciente"),
                                            res.getString("curp_paciente"),
                                            res.getInt   ("edad_paciente"), 
                                            res.getString("telefono_paciente"), 
                                            res.getString("correo_paciente")); 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pacienteUnico;
    }
    
    /**
     * clase que carga un solo paciente 
     */
    public class cargaSoloUnPacienteTask extends DBTask<Paciente> {
        String idAs;
        /**
         * constructor lleno de clase
         * @param idPaciente 
         */
        public cargaSoloUnPacienteTask(String idPaciente) {
            idAs = idPaciente;
        }
        
        @Override
        protected Paciente call() throws Exception {
            Paciente paciRegrear = null;
            String sttm = "SELECT id_paciente,  nombre_paciente, \n" +
                       " apellido_paciente,  apMaterno_paciente, \n" +
                       " sexo_paciente,  fechaNacimiento_paciente, \n" +
                       " curp_paciente, edad_paciente, \n" +
                       " telefono_paciente, correo_paciente \n" +
                        "FROM Paciente WHERE id_paciente = '"+idAs+"'"; 
            try (   Connection conex = dbConn.conectarBD();
                    PreparedStatement stta = conex.prepareStatement(sttm);
                    ResultSet res = stta.executeQuery(sttm)){
                if (res.next()) {
                   paciRegrear = new Paciente(res.getString("id_paciente"), 
                                                res.getString("nombre_paciente"), 
                                                res.getString("apellido_paciente"), 
                                                res.getString("apMaterno_paciente"), 
                                                res.getString("sexo_paciente"), 
                                                res.getDate  ("fechaNacimiento_paciente"),
                                                res.getString("curp_paciente"),
                                                res.getInt   ("edad_paciente"), 
                                                res.getString("telefono_paciente"), 
                                                res.getString("correo_paciente")); 
                }
            } catch (SQLException ex) {
               logger.log(Level.SEVERE, null, ex);
            }
            return paciRegrear;
        }
        
    }
    
    /**
     * calse que carga solo el nombre del paciente
     */
    public class cargaNombrePacienteTask extends DBTask<String> {
        String idAs;
        /**
         * constructor lleno de clase
         * @param idPaciente 
         */
        public cargaNombrePacienteTask(String idPaciente) {
            idAs = idPaciente;
        }
        
        @Override
        protected String call() throws Exception {
            String paciRegrear = null;
            String sttm = "SELECT id_paciente,  nombre_paciente, \n" +
                       " apellido_paciente,  apMaterno_paciente\n" +
                        "FROM Paciente WHERE id_paciente = '"+idAs+"'"; 
            try (   Connection conex = dbConn.conectarBD();
                    PreparedStatement stta = conex.prepareStatement(sttm);
                    ResultSet res = stta.executeQuery(sttm)){
                if (res.next()) {
                   paciRegrear = String.format("%s %s %s", res.getString("nombre_paciente"), 
                                                res.getString("apellido_paciente"), 
                                                res.getString("apMaterno_paciente")); 
                }
            } catch (SQLException ex) {
               logger.log(Level.SEVERE, null, ex);
            }
            return paciRegrear;
        }
        
    }
    
    /**
     * Carga la tabla
     * @param conex
     * @return 
     */
    public ObservableList<Paciente> cargaTabla(Connection conex){
        ObservableList<Paciente> pacientes = FXCollections.observableArrayList();
        String sttm = "SELECT id_paciente,  nombre_paciente, \n" +
                       " apellido_paciente,  apMaterno_paciente, \n" +
                       " sexo_paciente,  fechaNacimiento_paciente, \n" +
                       " curp_paciente, edad_paciente, \n" +
                       " telefono_paciente, correo_paciente \n" +
                        "FROM Paciente ;"; 
        try (   PreparedStatement stta = conex.prepareStatement(sttm);
                ResultSet res = stta.executeQuery(sttm)){
            while(res.next()) {
               pacientes.add(new Paciente(res.getString("id_paciente"), 
                                            res.getString("nombre_paciente"), 
                                            res.getString("apellido_paciente"), 
                                            res.getString("apMaterno_paciente"), 
                                            res.getString("sexo_paciente"), 
                                            res.getDate  ("fechaNacimiento_paciente"),
                                            res.getString("curp_paciente"),
                                            res.getInt   ("edad_paciente"),
                                            res.getString("telefono_paciente"), 
                                            res.getString("correo_paciente"))); 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pacientes;
    }
    
    
    /**
     * regresa lista de pacientes con like 
     * @param conex
     * @param dato
     * @return 
     */
    public ObservableList<Paciente> buscaNombre(Connection conex, String dato){
        ObservableList<Paciente> pacientes = FXCollections.observableArrayList();
        String sttm = "SELECT id_paciente,  nombre_paciente,\n" +
                      "apellido_paciente,  apMaterno_paciente,\n" +
                      "sexo_paciente,  fechaNacimiento_paciente,\n" +
                      "curp_paciente, edad_paciente,\n" +
                      "telefono_paciente, correo_paciente\n" +
                      "FROM Paciente WHERE concat_ws(' ', nombre_paciente, apellido_paciente, apMaterno_paciente)\n" +
                      "LIKE '%"+dato+"%';"; 
        try (   PreparedStatement stta = conex.prepareStatement(sttm);
                ResultSet res = stta.executeQuery(sttm)){
            while(res.next()) {
               pacientes.add(new Paciente(res.getString("id_paciente"), 
                                            res.getString("nombre_paciente"), 
                                            res.getString("apellido_paciente"), 
                                            res.getString("apMaterno_paciente"), 
                                            res.getString("sexo_paciente"), 
                                            res.getDate  ("fechaNacimiento_paciente"),
                                            res.getString("curp_paciente"),
                                            res.getInt   ("edad_paciente"),
                                            res.getString("telefono_paciente"), 
                                            res.getString("correo_paciente"))); 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pacientes;
    }
    
    /**
     * regresa la lista de datos con CURP
     * @param conex
     * @param dato
     * @return 
     */
    public ObservableList<Paciente> buscaCURP(Connection conex, String dato){
        ObservableList<Paciente> pacientes = FXCollections.observableArrayList();
        String sttm = "SELECT id_paciente,  nombre_paciente,\n" +
                      "apellido_paciente,  apMaterno_paciente,\n" +
                      "sexo_paciente,  fechaNacimiento_paciente,\n" +
                      "curp_paciente, edad_paciente,\n" +
                      "telefono_paciente, correo_paciente\n" +
                      "FROM Paciente WHERE curp_paciente\n" +
                      "LIKE '%"+dato+"%';"; 
        try (   PreparedStatement stta = conex.prepareStatement(sttm);
                ResultSet res = stta.executeQuery(sttm)){
            while(res.next()) {
               pacientes.add(new Paciente(res.getString("id_paciente"), 
                                            res.getString("nombre_paciente"), 
                                            res.getString("apellido_paciente"), 
                                            res.getString("apMaterno_paciente"), 
                                            res.getString("sexo_paciente"), 
                                            res.getDate  ("fechaNacimiento_paciente"),
                                            res.getString("curp_paciente"),
                                            res.getInt   ("edad_paciente"),
                                            res.getString("telefono_paciente"), 
                                            res.getString("correo_paciente"))); 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pacientes;
    }
    
    
    /**
     * modifica en la base de datos al paciente
     * @param id_paciente
     * @param nombre_paciente
     * @param apellido_paciente
     * @param apMaterno_paciente
     * @param sexo_paciente
     * @param fechaNacimiento_paciente
     * @param curp_paciente
     * @param edad_paciente
     * @param telefono_paciente
     * @param correo_paciente
     * @param conex 
     */
    public void udatePaciente  (
                    String id_paciente, String nombre_paciente, 
                    String apellido_paciente, String apMaterno_paciente, 
                    String sexo_paciente, Date fechaNacimiento_paciente, 
                    String curp_paciente,int edad_paciente, String telefono_paciente, 
                    String correo_paciente, Connection conex){
        String sqlst = " UPDATE Paciente SET \n" +
                                "id_paciente=?, \n" +
                                "nombre_paciente=?, \n" +
                                "apellido_paciente=?, \n" +
                                "apMaterno_paciente=?, \n" +
                                "sexo_paciente=?, \n" +
                                "fechaNacimiento_paciente=?, \n" +
                                "curp_paciente=?, \n" +
                                "edad_paciente=?, \n" +
                                "telefono_paciente=?, \n" +
                                "correo_paciente=? \n" +
                                "WHERE id_paciente=?";    
        try (PreparedStatement sttm = conex.prepareStatement(sqlst)){
            conex.setAutoCommit(false);
            sttm.setString  (1, id_paciente);
            sttm.setString  (2, nombre_paciente);
            sttm.setString  (3, apellido_paciente);
            sttm.setString  (4, apMaterno_paciente);
            sttm.setString  (5, sexo_paciente);
            sttm.setDate    (6, fechaNacimiento_paciente);
            sttm.setString  (7, curp_paciente);
            sttm.setInt     (8, edad_paciente);
            sttm.setString  (9, telefono_paciente);
            sttm.setString  (10, correo_paciente);
            sttm.setString  (11, id_paciente);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Paciente modificado", "Paciente modificado", 
                    "El paciente ha sido modificado en la base de datos de manera exitosa");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Constructor Vacio
     */
    public Paciente() {
    }
 
/********************************************************************************************************/
    //Seters and geters
    
    
    public String getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(String id_paciente) {
        this.id_paciente = id_paciente;
    }

    public String getNombre_paciente() {
        return nombre_paciente;
    }

    public void setNombre_paciente(String nombre_paciente) {
        this.nombre_paciente = nombre_paciente;
    }

    public String getApellido_paciente() {
        return apellido_paciente;
    }

    public void setApellido_paciente(String apellido_paciente) {
        this.apellido_paciente = apellido_paciente;
    }

    public String getApMaterno_paciente() {
        return apMaterno_paciente;
    }

    public void setApMaterno_paciente(String apMaterno_paciente) {
        this.apMaterno_paciente = apMaterno_paciente;
    }

    public String getSexo_paciente() {
        return sexo_paciente;
    }

    public void setSexo_paciente(String sexo_paciente) {
        this.sexo_paciente = sexo_paciente;
    }

    public Date getFechaNacimiento_paciente() {
        return fechaNacimiento_paciente;
    }

    public void setFechaNacimiento_paciente(Date fechaNacimiento_paciente) {
        this.fechaNacimiento_paciente = fechaNacimiento_paciente;
    }

    public String getCurp_paciente() {
        return curp_paciente;
    }

    public void setCurp_paciente(String curp_paciente) {
        this.curp_paciente = curp_paciente;
    }

    public int getEdad_paciente() {
        return edad_paciente;
    }

    public void setEdad_paciente(int edad_paciente) {
        this.edad_paciente = edad_paciente;
    }
  
    public String getTelefono_paciente() {
        return telefono_paciente;
    }

    public void setTelefono_paciente(String telefono_paciente) {
        this.telefono_paciente = telefono_paciente;
    }

    public String getCorreo_paciente() {
        return correo_paciente;
    }

    public void setCorreo_paciente(String correo_paciente) {
        this.correo_paciente = correo_paciente;
    }

    
    
}
