/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo;

/* Java Bean
* Clase: Medico  */
import com.aohys.copiaIMSS.BaseDatos.MysqlConnectionSingle;
import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class Usuario{
    
    //Base de datos
    Connection connection;
  
    public Usuario medicoUnico;
    //Variables de clase
    private static final Logger logger = Logger.getLogger(Usuario.class.getName());
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
     * Variables
     */
	private StringProperty id_medico;
	private StringProperty contraseña_medico;
	private StringProperty nombre_medico;
	private StringProperty apellido_medico;
	private StringProperty apMaterno_medico;
	private StringProperty cedulaProfecional_medico;
	private StringProperty especialidad_medico;
	private StringProperty telefono_medico;
	private StringProperty correo_medico;
        private String tipo_medico;
        
        /**
         * constructor lleno 
         * @param id_medico
         * @param contraseña_medico
     * @param nombre_medico
     * @param apellido_medico
         * @param apMaterno_medico
         * @param cedulaProfecional_medico
         * @param especialidad_medico
         * @param telefono_medico
         * @param correo_medico
         * @param tipo_medico 
         */
	public Usuario(String id_medico, String contraseña_medico, 
                String nombre_medico, String apellido_medico, 
                String apMaterno_medico, String cedulaProfecional_medico, 
                String especialidad_medico, String telefono_medico, String correo_medico,
                String tipo_medico){
		this.id_medico = new SimpleStringProperty(id_medico);
		this.contraseña_medico = new SimpleStringProperty(contraseña_medico);
		this.nombre_medico = new SimpleStringProperty(nombre_medico);
		this.apellido_medico = new SimpleStringProperty(apellido_medico);
		this.apMaterno_medico = new SimpleStringProperty(apMaterno_medico);
		this.cedulaProfecional_medico = new SimpleStringProperty(cedulaProfecional_medico);
		this.especialidad_medico = new SimpleStringProperty(especialidad_medico);
		this.telefono_medico = new SimpleStringProperty(telefono_medico);
		this.correo_medico = new SimpleStringProperty(correo_medico);
                this.tipo_medico = tipo_medico;
	}
        
        /**
         * Constructor Vacio
         */
        public Usuario(){
        }

        /**
        * Agrega Medico
        * 
        * @param id_medico
        * @param contraseña_medico
        * @param nombre_medico
        * @param apellido_medico
        * @param apMaterno_medico
        * @param cedulaProfecional_medico
        * @param especialidad_medico
        * @param telefono_medico
        * @param correo_medico 
        * @param tipo_medico 
        * @param conex 
        */
        public void agregarMedico( 
                String id_medico, String contraseña_medico, 
                                    String nombre_medico, String apellido_medico, 
                                    String apMaterno_medico, String cedulaProfecional_medico, 
                                    String especialidad_medico, String telefono_medico, 
                                    String correo_medico, String tipo_medico,
                                    Connection conex){
            String sqlst = "INSERT INTO Medico (id_medico, contraseña_medico,"+
                                "nombre_medico, apellido_medico," +
                                "apMaterno_medico, cedulaProfecional_medico,"+
                                "especialidad_medico, telefono_medico,"+
                                "correo_medico, tipo_medico)"+
                                "VALUES (?,?,?,?,?,?,?,?,?,?)";
            try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
                conex.setAutoCommit(false);
                sttm.setString  (1, id_medico);
                sttm.setString  (2, contraseña_medico);
                sttm.setString  (3, nombre_medico);
                sttm.setString  (4, apellido_medico);
                sttm.setString  (5, apMaterno_medico);
                sttm.setString  (6, cedulaProfecional_medico);
                sttm.setString  (7, especialidad_medico);
                sttm.setString  (8, telefono_medico);
                sttm.setString  (9, correo_medico);
                sttm.setString  (10, tipo_medico);
                sttm.addBatch();
                sttm.executeBatch();
                conex.commit();
                aux.informacionUs("El usuario ha sido guardado", 
                        "El usuario ha sido guardado", 
                        "El usuario ha sido guardado exitosamente en la base de datos");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
    /**
    * Realiza la carga de la lista medico
    * @param conex
    * @return 
    */
    public ObservableList<Usuario> cargaTabla(){
        ObservableList<Usuario> listaMed = FXCollections.observableArrayList();
        String sql = "SELECT  id_medico, contraseña_medico,\n" +
                "        nombre_medico, apellido_medico,\n" +
                "        apMaterno_medico, cedulaProfecional_medico,\n" +
                "        especialidad_medico, telefono_medico,\n" +
                "        correo_medico, tipo_medico\n" +
                "FROM Medico;";
        try(Connection conex = new MysqlConnectionSingle().conectarBDSingleConnection();
                PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaMed.add(new Usuario( res.getString("id_medico"), 
                                         res.getString("contraseña_medico"), 
                                         res.getString("nombre_medico"), 
                                         res.getString("apellido_medico"), 
                                         res.getString("apMaterno_medico"), 
                                         res.getString("cedulaProfecional_medico"), 
                                         res.getString("especialidad_medico"), 
                                         res.getString("telefono_medico"), 
                                         res.getString("correo_medico"),
                                         res.getString("tipo_medico")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
         return listaMed;
        }
    
    /**
     * clase que carga Lista de medicos
     */
    public class cargaListaMedTask extends DBTask<ObservableList<Usuario>> {

        @Override
        protected ObservableList<Usuario> call() throws Exception {
           ObservableList<Usuario> listaMed = FXCollections.observableArrayList();
            String sql = "SELECT  id_medico, contraseña_medico,\n" +
                    "        nombre_medico, apellido_medico,\n" +
                    "        apMaterno_medico, cedulaProfecional_medico,\n" +
                    "        especialidad_medico, telefono_medico,\n" +
                    "        correo_medico, tipo_medico\n" +
                    "FROM Medico WHERE tipo_medico = 'Medico';";
            try(Connection conex = new MysqlConnectionSingle().conectarBDSingleConnection();
                    PreparedStatement stta = conex.prepareStatement(sql);
                  ResultSet res = stta.executeQuery()) {
                while (res.next()) {
                    listaMed.add(new Usuario( res.getString("id_medico"), 
                                             res.getString("contraseña_medico"), 
                                             res.getString("nombre_medico"), 
                                             res.getString("apellido_medico"), 
                                             res.getString("apMaterno_medico"), 
                                             res.getString("cedulaProfecional_medico"), 
                                             res.getString("especialidad_medico"), 
                                             res.getString("telefono_medico"), 
                                             res.getString("correo_medico"),
                                             res.getString("tipo_medico")));
                   }
               } catch (SQLException ex) {
                   logger.log(Level.SEVERE, null, ex);
               }
            return listaMed;
        }
         
     }
    
    /**
     * carga la lista con la clse especial resumen
     * @param conex
     * @return 
     */
    public ObservableList<Usuario> cargaListaMedResumen(Connection conex){
        ObservableList<Usuario> listaMed = FXCollections.observableArrayList();
        String sql = "SELECT  id_medico, contraseña_medico,\n" +
                "        nombre_medico, apellido_medico,\n" +
                "        apMaterno_medico, cedulaProfecional_medico,\n" +
                "        especialidad_medico, telefono_medico,\n" +
                "        correo_medico, tipo_medico\n" +
                "FROM Medico WHERE tipo_medico = 'Medico';";
        listaMed.add(new Usuario("", "", "Mostrar todas las consultas", "", "", "", "", "", "", ""));
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaMed.add(new Usuario( res.getString("id_medico"), 
                                         res.getString("contraseña_medico"), 
                                         res.getString("nombre_medico"), 
                                         res.getString("apellido_medico"), 
                                         res.getString("apMaterno_medico"), 
                                         res.getString("cedulaProfecional_medico"), 
                                         res.getString("especialidad_medico"), 
                                         res.getString("telefono_medico"), 
                                         res.getString("correo_medico"),
                                         res.getString("tipo_medico")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaMed;
    }
    
    /**
     * carga lista de medicos segun la especialidad
     */
    public class cargaListaMedEspecialidadTask extends DBTask<ObservableList<Usuario>> {
        String dato;
        /**
         * constructor de clase 
         * @param dato 
         */
        public cargaListaMedEspecialidadTask(String dato) {
            this.dato = dato;
        }
        
        
        @Override
        protected ObservableList<Usuario> call() throws Exception {
            ObservableList<Usuario> listaMed = FXCollections.observableArrayList();
            String sql ="SELECT  id_medico, contraseña_medico,\n" +
                        "nombre_medico, apellido_medico,\n" +
                        "apMaterno_medico, cedulaProfecional_medico,\n" +
                        "especialidad_medico, telefono_medico,\n" +
                        "correo_medico, tipo_medico\n" +
                        "FROM Medico WHERE tipo_medico = 'Medico' \n" +
                        "AND especialidad_medico = '"+dato+"';";
            try(Connection conex = new MysqlConnectionSingle().conectarBDSingleConnection();
                PreparedStatement stta = conex.prepareStatement(sql);
                ResultSet res = stta.executeQuery()) {
                while (res.next()) {
                    listaMed.add(new Usuario( res.getString("id_medico"), 
                                             res.getString("contraseña_medico"), 
                                             res.getString("nombre_medico"), 
                                             res.getString("apellido_medico"), 
                                             res.getString("apMaterno_medico"), 
                                             res.getString("cedulaProfecional_medico"), 
                                             res.getString("especialidad_medico"), 
                                             res.getString("telefono_medico"), 
                                             res.getString("correo_medico"),
                                             res.getString("tipo_medico")));
                   }
               } catch (SQLException ex) {
                   logger.log(Level.SEVERE, null ,ex);
               }
            return listaMed;
        }
        
    }
    
    
    
    /**
     * clase que sirve para carga lista de especialidades medicas
     */
    public class listaEspeciTask extends DBTask<ObservableList<Usuario>> {

        @Override
        protected ObservableList<Usuario> call() throws Exception {
            ObservableList<Usuario> listaMed = FXCollections.observableArrayList();
            String sql ="SELECT DISTINCT id_medico, contraseña_medico,\n" +
                        "nombre_medico, apellido_medico,\n" +
                        "apMaterno_medico, cedulaProfecional_medico,\n" +
                        "especialidad_medico, telefono_medico,\n" +
                        "correo_medico, tipo_medico\n" +
                        "FROM Medico WHERE tipo_medico = 'Medico'\n" +
                        "GROUP BY especialidad_medico;";
            try(Connection conex = new MysqlConnectionSingle().conectarBDSingleConnection();
                    PreparedStatement stta = conex.prepareStatement(sql);
                  ResultSet res = stta.executeQuery()) {
                while (res.next()) {
                    listaMed.add(new Usuario( res.getString("id_medico"), 
                                             res.getString("contraseña_medico"), 
                                             res.getString("nombre_medico"), 
                                             res.getString("apellido_medico"), 
                                             res.getString("apMaterno_medico"), 
                                             res.getString("cedulaProfecional_medico"), 
                                             res.getString("especialidad_medico"), 
                                             res.getString("telefono_medico"), 
                                             res.getString("correo_medico"),
                                             res.getString("tipo_medico")));
                   }
               } catch (SQLException ex) {
                   logger.log(Level.SEVERE, null, ex);
               }
            return listaMed;
        }
    
    }
         
        /**
         * Borra Medico
         * @param Dato 
         * @param conex 
         */
        public void BorrarMedico(String Dato, Connection conex){
            String sttm = "delete from Medico where id_medico = '"+Dato+"'";
            try(PreparedStatement stta = conex.prepareStatement(sttm)) {
                conex.setAutoCommit(false);
                stta.addBatch();
                stta.executeBatch();
                conex.commit();
                aux.informacionUs("Usuario borrado", 
                        "Usuario borrado", 
                        "El usuario ha sido borrado de la base de datos");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        /**
         * Checa que la ID no sea repetida
         * @param Dato
         * @param conex
         * @return 
         */
        public boolean verificarID(String Dato, Connection conex){
            boolean yaExiste = false;
            String sttm = "SELECT id_medico FROM Medico";
            try(PreparedStatement stta = conex.prepareStatement(sttm);
                  ResultSet rest = stta.executeQuery()) {
                while (rest.next()) {
                    String compara = rest.getString("id_medico");
                    if (compara.equals(Dato)) {
                        yaExiste = true;
                        break;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return yaExiste;
        }
        
        /**
         * Compara usuario con contraseña
         * @param usuario
         * @param contra
         * @param conex
         * @return 
         */
        public boolean verificarContraseña(String usuario, String contra, Connection conex){
            boolean concuerdan = false;
            boolean hayusuario = false;
            String sttm = "SELECT id_medico FROM Medico;";
            String cont = "SELECT contraseña_medico, nombre_medico, apellido_medico, apMaterno_medico \n"
                    + "FROM Medico WHERE id_medico ='"+usuario+"';";
            try(PreparedStatement stta = conex.prepareStatement(sttm);
                  ResultSet rest = stta.executeQuery()) {
                while (rest.next()) {
                    String compara = rest.getString("id_medico");
                    if (compara.equals(usuario)) {
                        hayusuario = true;
                        if (true) {
                            try(PreparedStatement st2 = conex.prepareStatement(cont);
                                  ResultSet rest2 = st2.executeQuery()  ) {
                                if (rest2.next()) {
                                    if (contra.equals(rest2.getString("contraseña_medico"))) {
                                        aux.informacionUs("Bienvenido",String.format("Bienvenido %s", usuario), 
                                                String.format("Bienvenido %s %s %s", 
                                                        rest2.getString("nombre_medico"),
                                                        rest2.getString("apellido_medico"),
                                                        rest2.getString("apMaterno_medico")));
                                        concuerdan = true;
                                }else
                                    aux.alertaError("La contraseña no concuerda", 
                                                    "La contraseña no concuerda", 
                                                    "La contraseña de este usuario no concuerda con la ingresada");
                            }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }else
                        hayusuario = false;
                }
                
                if (!hayusuario) {
                    aux.alertaError("El usuario no existe", 
                                    "El usuario no existe", 
                                    "El usuario no se encuentra en la base de datos");
                }
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return concuerdan;
        }
        
        /**
         * Carga solo el medico seleccionado 
         * @param Dato
         * @param conex
         * @return Medico
         */
        public Usuario CargaSoloUno(String Dato){
            String sttm = "SELECT  id_medico, contraseña_medico,\n" +
                            "        nombre_medico, apellido_medico,\n" +
                            "        apMaterno_medico, cedulaProfecional_medico,\n" +
                            "        especialidad_medico, telefono_medico,\n" +
                            "        correo_medico, tipo_medico\n" +
                            "        FROM Medico\n "+
                            "        WHERE id_medico = '"+Dato+"'";
            try(Connection conex = new MysqlConnectionSingle().conectarBDSingleConnection();
                    PreparedStatement stta = conex.prepareStatement(sttm);
                   ResultSet res = stta.executeQuery(); ) {
                if (res.next()) {
                    medicoUnico = new Usuario(   res.getString("id_medico"), 
                                            res.getString("contraseña_medico"), 
                                            res.getString("nombre_medico"), 
                                            res.getString("apellido_medico"), 
                                            res.getString("apMaterno_medico"), 
                                            res.getString("cedulaProfecional_medico"), 
                                            res.getString("especialidad_medico"), 
                                            res.getString("telefono_medico"), 
                                            res.getString("correo_medico"), 
                                            res.getString("tipo_medico"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return medicoUnico;
        }
        
        /**
         * Modifica el medico
         * @param id_medico
         * @param contraseña_medico
         * @param nombre_medico
         * @param apellido_medico
         * @param apMaterno_medico
         * @param cedulaProfecional_medico
         * @param especialidad_medico
         * @param telefono_medico
         * @param correo_medico
         * @param IdModificar
         * @param tipo_medico 
     * @param conex 
         */
        public void udateMedico(String id_medico,           String contraseña_medico, 
                                String nombre_medico,       String apellido_medico, 
                                String apMaterno_medico,    String cedulaProfecional_medico, 
                                String especialidad_medico, String telefono_medico, 
                                String correo_medico,       String IdModificar, 
                                String tipo_medico, Connection conex){
            String sqlst = " UPDATE Medico SET \n" +
                                "id_medico=?, \n" +
                                "contraseña_medico=?, \n" +
                                "nombre_medico=?, \n" +
                                "apellido_medico=?, \n" +
                                "apMaterno_medico=?, \n" +
                                "cedulaProfecional_medico=?, \n" +
                                "especialidad_medico=?, \n" +
                                "telefono_medico=?, \n" +
                                "correo_medico=?, \n" +
                                "tipo_medico=? \n" +
                                "WHERE id_medico=?";
            try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
                conex.setAutoCommit(false);
                sttm.setString  (1, id_medico);
                sttm.setString  (2, contraseña_medico);
                sttm.setString  (3, nombre_medico);
                sttm.setString  (4, apellido_medico);
                sttm.setString  (5, apMaterno_medico);
                sttm.setString  (6, cedulaProfecional_medico);
                sttm.setString  (7, especialidad_medico);
                sttm.setString  (8, telefono_medico);
                sttm.setString  (9, correo_medico);
                sttm.setString  (10, tipo_medico);
                sttm.setString  (11, IdModificar);
                sttm.addBatch();
                sttm.executeBatch();
                conex.commit();
                aux.informacionUs("El usuario ha sido modificado", 
                        "El usuario ha sido modificado", 
                        "El usuario ha sido modificado exitosamente en la base de datos");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    @Override
    public String toString() {
        return nombre_medico+" "+apellido_medico+" "+apMaterno_medico;
    }
        
    
    
    /***************************************************************************************************/
    
        /**
         * Setter, Getters y Propietis
         * @return 
         */
	public String getId_medico(){
		return id_medico.get();
	}

	public void setId_medico(String id_medico){
		this.id_medico = new SimpleStringProperty(id_medico);
	}

	public String getContraseña_medico(){
		return contraseña_medico.get();
	}

	public void setContraseña_medico(String contraseña_medico){
		this.contraseña_medico = new SimpleStringProperty(contraseña_medico);
	}

	public String getNombre_medico(){
		return nombre_medico.get();
	}

	public void setNombre_medico(String nombre_medico){
		this.nombre_medico = new SimpleStringProperty(nombre_medico);
	}

	public String getApellido_medico(){
		return apellido_medico.get();
	}

	public void setApellido_medico(String apellido_medico){
		this.apellido_medico = new SimpleStringProperty(apellido_medico);
	}

	public String getApMaterno_medico(){
		return apMaterno_medico.get();
	}

	public void setApMaterno_medico(String apMaterno_medico){
		this.apMaterno_medico = new SimpleStringProperty(apMaterno_medico);
	}

	public String getCedulaProfecional_medico(){
		return cedulaProfecional_medico.get();
	}

	public void setCedulaProfecional_medico(String cedulaProfecional_medico){
		this.cedulaProfecional_medico = new SimpleStringProperty(cedulaProfecional_medico);
	}

	public String getEspecialidad_medico(){
		return especialidad_medico.get();
	}

	public void setEspecialidad_medico(String especialidad_medico){
		this.especialidad_medico = new SimpleStringProperty(especialidad_medico);
	}

	public String getTelefono_medico(){
		return telefono_medico.get();
	}

	public void setTelefono_medico(String telefono_medico){
		this.telefono_medico = new SimpleStringProperty(telefono_medico);
	}

	public String getCorreo_medico(){
		return correo_medico.get();
	}

	public void setCorreo_medico(String correo_medico){
		this.correo_medico = new SimpleStringProperty(correo_medico);
	}

	public StringProperty id_medicoProperty(){
		return id_medico;
	}

	public StringProperty contraseña_medicoProperty(){
		return contraseña_medico;
	}

	public StringProperty nombre_medicoProperty(){
		return nombre_medico;
	}

	public StringProperty apellido_medicoProperty(){
		return apellido_medico;
	}

	public StringProperty apMaterno_medicoProperty(){
		return apMaterno_medico;
	}

	public StringProperty cedulaProfecional_medicoProperty(){
		return cedulaProfecional_medico;
	}

	public StringProperty especialidad_medicoProperty(){
		return especialidad_medico;
	}

	public StringProperty telefono_medicoProperty(){
		return telefono_medico;
	}

	public StringProperty correo_medicoProperty(){
		return correo_medico;
	}

        public String getTipo_medico() {
            return tipo_medico;
        }

        public void setTipo_medico(String tipo_medico) {
            this.tipo_medico = tipo_medico;
        }

        
     
    

}