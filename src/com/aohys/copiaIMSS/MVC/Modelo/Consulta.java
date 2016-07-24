/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo;

import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Alejandro Ortiz Corro
 */

public class Consulta {
    //Variables de consulta
    private StringProperty id_cons;
    private StringProperty motivo_cons;
    private StringProperty exploracion_cons;
    private Date fecha_cons;
    private Time hora_cons;
    private BooleanProperty primeraVez_cons;
    private StringProperty higiDiete_cons;
    private StringProperty id_paciente;
    private StringProperty id_medico;
    //Axiliar
    Auxiliar aux = new Auxiliar();

    /**
     * constructtor lleno
     * @param id_cons
     * @param motivo_cons
     * @param exploracion_cons
     * @param fecha_cons
     * @param hora_cons
     * @param primeraVez_cons
     * @param higiDiete_cons
     * @param id_paciente
     * @param id_medico 
     */
    public Consulta(String id_cons, String motivo_cons, String exploracion_cons, Date fecha_cons, Time hora_cons, 
            boolean primeraVez_cons, String higiDiete_cons, String id_paciente, String id_medico) {
        this.id_cons = new SimpleStringProperty(id_cons);
        this.motivo_cons = new SimpleStringProperty(motivo_cons);
        this.exploracion_cons = new SimpleStringProperty(exploracion_cons);
        this.fecha_cons = fecha_cons;
        this.hora_cons = hora_cons;
        this.primeraVez_cons = new SimpleBooleanProperty(primeraVez_cons);
        this.higiDiete_cons = new SimpleStringProperty(higiDiete_cons);
        this.id_paciente = new SimpleStringProperty(id_paciente);
        this.id_medico = new SimpleStringProperty(id_medico);
    }

    /**
     * constructor vacio
     */
    public Consulta() {
    }

    /**
     * agrega consulta
     * @param id_cons
     * @param motivo_cons
     * @param exploracion_cons
     * @param fecha_cons
     * @param hora_cons
     * @param primeraVez_cons
     * @param higiDiete_cons
     * @param id_paciente
     * @param id_medico
     * @param conex 
     */
    public void agregaConsulta(String id_cons, String motivo_cons, String exploracion_cons, 
            Date fecha_cons, Time hora_cons,boolean primeraVez_cons, String higiDiete_cons, 
            String id_paciente, String id_medico, Connection conex){
        String sqlst =  "INSERT INTO `consulta`\n" +
                            "(`id_cons`,\n" +
                            "`motivo_cons`,\n" +
                            "`exploracion_cons`,\n" +
                            "`fecha_cons`,\n" +
                            "`hora_cons`,\n" +
                            "`primeraVez_cons`,\n" +
                            "`higiDiete_cons`,\n" +
                            "`id_paciente`,\n" +
                            "`id_medico`)\n" +
                        "VALUES (?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_cons);
            sttm.setString  (2, motivo_cons);
            sttm.setString  (3, exploracion_cons);
            sttm.setDate    (4, fecha_cons);
            sttm.setTime    (5, hora_cons);
            sttm.setBoolean (6, primeraVez_cons);
            sttm.setString  (7, higiDiete_cons);
            sttm.setString  (8, id_paciente);
            sttm.setString  (9, id_medico);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Consulta guardada", 
                        "Consulta guardada", 
                    "La consulta ha sido guardada exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * lista de diagnosticos de la consulta
     * @param conex
     * @param idPaciente
     * @return 
     */
    public ObservableList<Consulta> listaConsulPaciente(Connection conex, String idPaciente){
        ObservableList<Consulta> listaConsul = FXCollections.observableArrayList();
        String sql = "SELECT `id_cons`,\n" +
                            "`motivo_cons`,\n" +
                            "`exploracion_cons`,\n" +
                            "`fecha_cons`,\n" +
                            "`hora_cons`,\n" +
                            "`primeraVez_cons`,\n" +
                            "`higiDiete_cons`,\n" +
                            "`id_paciente`,\n" +
                            "`id_medico`\n" +
                     "FROM `consulta` WHERE id_paciente = '"+idPaciente+"'"
                +    "ORDER BY fecha_cons desc;";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaConsul.add(new Consulta( 
                                    res.getString("id_cons"), 
                                    res.getString("motivo_cons"), 
                                    res.getString("exploracion_cons"), 
                                    res.getDate  ("fecha_cons"), 
                                    res.getTime  ("hora_cons"), 
                                    res.getBoolean("primeraVez_cons"), 
                                    res.getString("higiDiete_cons"), 
                                    res.getString("id_paciente"), 
                                    res.getString("id_medico")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaConsul;
    }
    
    /**
     * lista de consulta de paciente con medico
     * @param conex
     * @param idPaciente
     * @param idMedico
     * @return 
     */
    public ObservableList<Consulta> listaConsulPacienteConMedico(Connection conex, String idPaciente, String idMedico){
        ObservableList<Consulta> listaConsul = FXCollections.observableArrayList();
        String sql = "SELECT `id_cons`,\n" +
                            "`motivo_cons`,\n" +
                            "`exploracion_cons`,\n" +
                            "`fecha_cons`,\n" +
                            "`hora_cons`,\n" +
                            "`primeraVez_cons`,\n" +
                            "`higiDiete_cons`,\n" +
                            "`id_paciente`,\n" +
                            "`id_medico`\n" +
                     "FROM `consulta` WHERE id_paciente = '"+idPaciente+"'\n"+
                     "AND id_medico = '"+idMedico+"'"
                +   "ORDER BY fecha_cons desc;";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaConsul.add(new Consulta( 
                                    res.getString("id_cons"), 
                                    res.getString("motivo_cons"), 
                                    res.getString("exploracion_cons"), 
                                    res.getDate  ("fecha_cons"), 
                                    res.getTime  ("hora_cons"), 
                                    res.getBoolean("primeraVez_cons"), 
                                    res.getString("higiDiete_cons"), 
                                    res.getString("id_paciente"), 
                                    res.getString("id_medico")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaConsul;
    }
    
    /**
     * carga solo una consulta
     * @param idConsulta
     * @param conex
     * @return 
     */
    public Consulta cargaSoloUno(String idConsulta, Connection conex){
        Consulta somametropia = null;
        String sttm = "SELECT `id_cons`,\n" +
                            "`motivo_cons`,\n" +
                            "`exploracion_cons`,\n" +
                            "`fecha_cons`,\n" +
                            "`hora_cons`,\n" +
                            "`primeraVez_cons`,\n" +
                            "`higiDiete_cons`,\n" +
                            "`id_paciente`,\n" +
                            "`id_medico`\n" +
                    "FROM consulta WHERE id_cons = '"+idConsulta+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                somametropia = new Consulta( 
                                    res.getString("id_cons"), 
                                    res.getString("motivo_cons"), 
                                    res.getString("exploracion_cons"), 
                                    res.getDate  ("fecha_cons"), 
                                    res.getTime  ("hora_cons"), 
                                    res.getBoolean("primeraVez_cons"), 
                                    res.getString("higiDiete_cons"), 
                                    res.getString("id_paciente"), 
                                    res.getString("id_medico"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return somametropia;
    }
    
/**************************************************************************************/
    //Setters and getters
    public final void setId_cons(String value) {
        id_cons.set(value);
    }

    public final String getId_cons() {
        return id_cons.get();
    }

    public final StringProperty id_consProperty() {
        return id_cons;
    }

    public final void setMotivo_cons(String value) {
        motivo_cons.set(value);
    }

    public final String getMotivo_cons() {
        return motivo_cons.get();
    }

    public final StringProperty motivo_consProperty() {
        return motivo_cons;
    }

    public final void setExploracion_cons(String value) {
        exploracion_cons.set(value);
    }

    public final String getExploracion_cons() {
        return exploracion_cons.get();
    }

    public final StringProperty exploracion_consProperty() {
        return exploracion_cons;
    }

    public final void setPrimeraVez_cons(Boolean value) {
        primeraVez_cons.set(value);
    }

    public final Boolean getPrimeraVez_cons() {
        return primeraVez_cons.get();
    }

    public final BooleanProperty primeraVez_consProperty() {
        return primeraVez_cons;
    }

    public final void setHigiDiete_cons(String value) {
        higiDiete_cons.set(value);
    }

    public final String getHigiDiete_cons() {
        return higiDiete_cons.get();
    }

    public final StringProperty higiDiete_consProperty() {
        return higiDiete_cons;
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

    public final void setId_medico(String value) {
        id_medico.set(value);
    }

    public final String getId_medico() {
        return id_medico.get();
    }

    public final StringProperty id_medicoProperty() {
        return id_medico;
    }

    public Date getFecha_cons() {
        return fecha_cons;
    }

    public void setFecha_cons(Date fecha_cons) {
        this.fecha_cons = fecha_cons;
    }

    public Time getHora_cons() {
        return hora_cons;
    }

    public void setHora_cons(Time hora_cons) {
        this.hora_cons = hora_cons;
    }

    
    
    
    
    
    
}
