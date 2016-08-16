/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta;

import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Alejandro Ortiz Corro
 */

public class Tratamiento {
    private StringProperty id_proce;
    private StringProperty nombre_proce;
    private StringProperty id_cons;

    Auxiliar aux = new Auxiliar();
    /**
     * constructor lleno
     * @param id_proce
     * @param nombre_proce
     * @param id_cons 
     */
    public Tratamiento(String id_proce, String nombre_proce, String id_cons) {
        this.id_proce = new SimpleStringProperty(id_proce);
        this.nombre_proce = new SimpleStringProperty(nombre_proce);
        this.id_cons = new SimpleStringProperty(id_cons);
    }

    /**
     * constructor vacio
     */
    public Tratamiento() {
    }
   
    /**
     * 
     * @param id_proce
     * @param nombre_proce
     * @param id_cons
     * @param conex 
     */
    public void agregaTratamiento(String id_proce, String nombre_proce, String id_cons, Connection conex){
        String sqlst =  "INSERT INTO `procedimiento`\n" +
                        "(`id_proce`,\n" +
                        "`nombre_proce`,\n" +
                        "`id_cons`)\n" +
                        "VALUES (?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_proce);
            sttm.setString  (2, nombre_proce);
            sttm.setString  (3, id_cons);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Tratamiento guardado", 
                            "Tratamiento guardado", 
                            "El Tratamiento ha sido guardado exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
   
    /**
     * lista de tratamientos de la consulta
     * @param conex
     * @param idDiagnostico
     * @return 
     */
    public ObservableList<Tratamiento> listaTratamientoConsulta(Connection conex, String idDiagnostico){
        ObservableList<Tratamiento> listaDiagnostico = FXCollections.observableArrayList();
        String sql = "SELECT `id_proce`,\n" +
                        "`nombre_proce`,\n" +
                        "`id_cons`\n" +
                     "FROM `procedimiento` WHERE id_cons = '"+idDiagnostico+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaDiagnostico.add(new Tratamiento( 
                                    res.getString("id_proce"), 
                                    res.getString("nombre_proce"),
                                    res.getString("id_cons")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaDiagnostico;
    }
    
    /**
     * 
     * @param idDiagnostico
     * @param conex
     * @return 
     */
    public Tratamiento cargaSoloUno(String idDiagnostico, Connection conex){
        Tratamiento somametropia = null;
        String sttm = "SELECT `id_proce`,\n" +
                        "`nombre_proce`,\n" +
                        "`id_cons`\n" +
                    "FROM procedimiento WHERE id_cons = '"+idDiagnostico+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                somametropia = new Tratamiento( 
                                    res.getString("id_proce"), 
                                    res.getString("nombre_proce"),
                                    res.getString("id_cons"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return somametropia;
    }
    
    /**
     * borra el tratamiento seleccionado
     * @param Dato
     * @param conex 
     */
    public void borrarTratamiento(String Dato, Connection conex){
        String sttm = "DELETE FROM procedimiento WHERE id_proce = '"+Dato+"'";
        try(PreparedStatement stta = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            stta.addBatch();
            stta.executeBatch();
            conex.commit();
            aux.informacionUs("El tratamiento ha sido borrado", 
                            "El tratamiento ha sido borrado", 
                            "El tratamiento ha sido borrado exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    
    
/****************************************************************************/
    //Setters and Getters
    public final void setId_proce(String value) {
        id_proce.set(value);
    }

    public final String getId_proce() {
        return id_proce.get();
    }

    public final StringProperty id_proceProperty() {
        return id_proce;
    }

    public final void setNombre_proce(String value) {
        nombre_proce.set(value);
    }

    public final String getNombre_proce() {
        return nombre_proce.get();
    }

    public final StringProperty nombre_proceProperty() {
        return nombre_proce;
    }

    public final void setId_cons(String value) {
        id_cons.set(value);
    }

    public final String getId_cons() {
        return id_cons.get();
    }

    public final StringProperty id_consProperty() {
        return id_cons;
    }
    
    
    

}
