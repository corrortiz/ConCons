/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo.ModeloCita;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario;
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

public class DiasFestivos {

   private StringProperty id_DiasFes; 
   private Date fecha_DiasFes;
   //Variables de clase
    private static final Logger logger = Logger.getLogger(Usuario.class.getName());
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
    * Constructor lleno
    * @param id_DiasFes
    * @param fecha_DiasFes 
    */
    public DiasFestivos(String id_DiasFes, Date fecha_DiasFes) {
        this.id_DiasFes = new SimpleStringProperty(id_DiasFes);
        this.fecha_DiasFes = fecha_DiasFes;
    }

    public DiasFestivos() {
    }

    /**
     * Agrega dia festivo 
     * @param id_DiasFes
     * @param fecha_DiasFes
     * @param conex 
     */
    public void agregaDiaFes(String id_DiasFes, Date fecha_DiasFes, Connection conex){
        String sqlst = "INSERT INTO diasfestivos "+
                        "(id_DiasFes, fecha_DiasFes)"+
                        "VALUES (?,?)";    
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_DiasFes);
            sttm.setDate    (2, fecha_DiasFes);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

   /**
    * Elimina el dia festivo 
    * @param ID
    * @param conex 
    */ 
   public void eliminar(String ID, Connection conex){
        String sqlst =  "DELETE FROM diasfestivos \n" +
                        "WHERE id_DiasFes = '"+ID+"'";
        try (PreparedStatement sttm = conex.prepareStatement(sqlst)){
            conex.setAutoCommit(false);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
    }
   
   /**
    * clase que Regresa lista de dias festivos
    */
   public class listaDiasFestivosTask extends DBTask<ObservableList<DiasFestivos>> {

        @Override
        protected ObservableList<DiasFestivos> call() throws Exception {
            ObservableList<DiasFestivos> listaFes = FXCollections.observableArrayList();
            String sql = "SELECT id_DiasFes, fecha_DiasFes \n" +
                        "FROM diasfestivos \n"+
                        "ORDER BY fecha_DiasFes ASC;";
            try(Connection conex = dbConn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sql);
                 ResultSet res = stta.executeQuery()) {
               while (res.next()) {
                   listaFes.add(new DiasFestivos(res.getString("id_DiasFes"), 
                                                 res.getDate("fecha_DiasFes")));
               }
            }catch (SQLException ex) {
               ex.printStackTrace();
            }
           return listaFes;
        }
       
   }
   
   /*******************************************************************************/
   //Setters and Getters
    public final void setId_DiasFes(String value) {
        id_DiasFes.set(value);
    }

    public final String getId_DiasFes() {
        return id_DiasFes.get();
    }

    public final StringProperty id_DiasFesProperty() {
        return id_DiasFes;
    }
    public Date getFecha_DiasFes() {
        return fecha_DiasFes;
    }

    public void setFecha_DiasFes(Date fecha_DiasFes) {
        this.fecha_DiasFes = fecha_DiasFes;
    }
   
   
}
