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
import java.time.LocalDate;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Alejandro Ortiz Corro
 */

public class Receta {
    private StringProperty id_rec;
    private StringProperty nombreMed_rec;
    private Date fecha_rec;
    private StringProperty viaAdmiMed_rec;
    private IntegerProperty indicaMed_rec;
    private StringProperty compleIndicaMed_rec;
    private IntegerProperty intevaloMed_rec;
    private StringProperty tipoIntervaloMed_rec;
    private IntegerProperty duacionMed_rec;
    private StringProperty tipoDucacionMed_rec;
    private StringProperty indiAdicionalesMed_rec;
    private StringProperty id_paciente;
    private StringProperty id_medico;

    
    Auxiliar aux = new Auxiliar();
    
    /**
     * constructor lleno
     * @param id_rec
     * @param nombreMed_rec
     * @param fecha_rec
     * @param viaAdmiMed_rec
     * @param indicaMed_rec
     * @param compleIndicaMed_rec
     * @param intevaloMed_rec
     * @param tipoIntervaloMed_rec
     * @param duacionMed_rec
     * @param tipoDucacionMed_rec
     * @param indiAdicionalesMed_rec
     * @param id_paciente
     * @param id_medico 
     */
    public Receta(String id_rec, String nombreMed_rec, Date fecha_rec, String viaAdmiMed_rec, 
            int indicaMed_rec, String compleIndicaMed_rec, int intevaloMed_rec, String tipoIntervaloMed_rec, 
            int duacionMed_rec, String tipoDucacionMed_rec, String indiAdicionalesMed_rec, String id_paciente,
            String id_medico) {
        this.id_rec = new SimpleStringProperty(id_rec);
        this.nombreMed_rec = new SimpleStringProperty(nombreMed_rec);
        this.fecha_rec = fecha_rec;
        this.viaAdmiMed_rec = new SimpleStringProperty(viaAdmiMed_rec);
        this.indicaMed_rec = new SimpleIntegerProperty(indicaMed_rec);
        this.compleIndicaMed_rec = new SimpleStringProperty(compleIndicaMed_rec);
        this.intevaloMed_rec = new SimpleIntegerProperty(intevaloMed_rec);
        this.tipoIntervaloMed_rec = new SimpleStringProperty(tipoIntervaloMed_rec);
        this.duacionMed_rec = new SimpleIntegerProperty(duacionMed_rec);
        this.tipoDucacionMed_rec = new SimpleStringProperty(tipoDucacionMed_rec);
        this.indiAdicionalesMed_rec = new SimpleStringProperty(indiAdicionalesMed_rec);
    }

    /**
     * constructor vacio
     */
    public Receta() {
    }

    /**
     * agrega un medicamento al paciente
     * @param id_rec
     * @param nombreMed_rec
     * @param fecha_rec
     * @param viaAdmiMed_rec
     * @param indicaMed_rec
     * @param compleIndicaMed_rec
     * @param intevaloMed_rec
     * @param tipoIntervaloMed_rec
     * @param duacionMed_rec
     * @param tipoDucacionMed_rec
     * @param indiAdicionalesMed_rec
     * @param id_paciente
     * @param id_medico
     * @param conex 
     */
    public void agregaMedicamento(String id_rec, String nombreMed_rec, Date fecha_rec, String viaAdmiMed_rec, 
            int indicaMed_rec, String compleIndicaMed_rec, int intevaloMed_rec, String tipoIntervaloMed_rec, 
            int duacionMed_rec, String tipoDucacionMed_rec, String indiAdicionalesMed_rec, String id_paciente,
            String id_medico, Connection conex){
        String sqlst =  "INSERT INTO `receta`\n" +
                                "(`id_rec`,\n" +
                                "`nombreMed_rec`,\n" +
                                "`fecha_rec`,\n" +
                                "`viaAdmiMed_rec`,\n" +
                                "`indicaMed_rec`,\n" +
                                "`compleIndicaMed_rec`,\n" +
                                "`intevaloMed_rec`,\n" +
                                "`tipoIntervaloMed_rec`,\n" +
                                "`duacionMed_rec`,\n" +
                                "`tipoDucacionMed_rec`,\n" +
                                "`indiAdicionalesMed`,\n" +
                                "`id_paciente`,\n" +
                                "`id_medico`)\n" +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1,id_rec);
            sttm.setString  (2,nombreMed_rec);
            sttm.setDate    (3,fecha_rec);
            sttm.setString  (4,viaAdmiMed_rec);
            sttm.setInt     (5,indicaMed_rec);
            sttm.setString  (6,compleIndicaMed_rec);
            sttm.setInt     (7,intevaloMed_rec);
            sttm.setString  (8,tipoIntervaloMed_rec);
            sttm.setInt     (9,duacionMed_rec);
            sttm.setString  (10,tipoDucacionMed_rec);
            sttm.setString  (11,indiAdicionalesMed_rec);
            sttm.setString  (12,id_paciente);
            sttm.setString  (13,id_medico);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("El medicamento ha sido agregado", 
                    "El medicamento ha sido agregado", 
                    "El medicamento ha sido agregado exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * carga solo un medicamento 
     * @param idDiagnostico
     * @param conex
     * @return 
     */
    public Receta cargaSoloUno(String idDiagnostico, Connection conex){
        Receta somametropia = null;
        String sttm = "SELECT `receta`.`id_rec`,\n" +
                        "    `receta`.`nombreMed_rec`,\n" +
                        "    `receta`.`fecha_rec`,\n" +
                        "    `receta`.`viaAdmiMed_rec`,\n" +
                        "    `receta`.`indicaMed_rec`,\n" +
                        "    `receta`.`compleIndicaMed_rec`,\n" +
                        "    `receta`.`intevaloMed_rec`,\n" +
                        "    `receta`.`tipoIntervaloMed_rec`,\n" +
                        "    `receta`.`duacionMed_rec`,\n" +
                        "    `receta`.`tipoDucacionMed_rec`,\n" +
                        "    `receta`.`indiAdicionalesMed`,\n" +
                        "    `receta`.`id_paciente`,\n" +
                        "    `receta`.`id_medico`\n" +
                    "FROM receta WHERE id_rec = '"+idDiagnostico+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                somametropia = new Receta( 
                                    res.getString  ("id_rec"),
                                    res.getString  ("nombreMed_rec"),
                                    res.getDate    ("fecha_rec"),
                                    res.getString  ("viaAdmiMed_rec"),
                                    res.getInt     ("indicaMed_rec"),
                                    res.getString  ("compleIndicaMed_rec"),
                                    res.getInt     ("intevaloMed_rec"),
                                    res.getString  ("tipoIntervaloMed_rec"),
                                    res.getInt     ("duacionMed_rec"),
                                    res.getString  ("tipoDucacionMed_rec"),
                                    res.getString  ("indiAdicionalesMed"),
                                    res.getString  ("id_paciente"),
                                    res.getString  ("id_medico"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return somametropia;
    }
    
    /**
     * regresa lista de medicamentos del paciente
     * @param conex
     * @param idPaciente
     * @return 
     */
    public ObservableList<Receta> listaRecetas(Connection conex, String idPaciente){
        ObservableList<Receta> listaReceta = FXCollections.observableArrayList();
        Date dia = Date.valueOf(LocalDate.now());
        String sql = "SELECT `receta`.`id_rec`,\n" +
                        "    `receta`.`nombreMed_rec`,\n" +
                        "    `receta`.`fecha_rec`,\n" +
                        "    `receta`.`viaAdmiMed_rec`,\n" +
                        "    `receta`.`indicaMed_rec`,\n" +
                        "    `receta`.`compleIndicaMed_rec`,\n" +
                        "    `receta`.`intevaloMed_rec`,\n" +
                        "    `receta`.`tipoIntervaloMed_rec`,\n" +
                        "    `receta`.`duacionMed_rec`,\n" +
                        "    `receta`.`tipoDucacionMed_rec`,\n" +
                        "    `receta`.`indiAdicionalesMed`,\n" +
                        "    `receta`.`id_paciente`,\n" +
                        "    `receta`.`id_medico`\n" +
                    "FROM receta WHERE id_paciente = '"+idPaciente+"'\n"+
                    "AND fecha_rec = '"+dia+"'\n"+
                    "ORDER BY fecha_rec ASC;";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaReceta.add(new Receta( 
                                    res.getString  ("id_rec"),
                                    res.getString  ("nombreMed_rec"),
                                    res.getDate    ("fecha_rec"),
                                    res.getString  ("viaAdmiMed_rec"),
                                    res.getInt     ("indicaMed_rec"),
                                    res.getString  ("compleIndicaMed_rec"),
                                    res.getInt     ("intevaloMed_rec"),
                                    res.getString  ("tipoIntervaloMed_rec"),
                                    res.getInt     ("duacionMed_rec"),
                                    res.getString  ("tipoDucacionMed_rec"),
                                    res.getString  ("indiAdicionalesMed"),
                                    res.getString  ("id_paciente"),
                                    res.getString  ("id_medico")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaReceta;
    }
    
    /**
     * regresa lista de recetas con fechas anteriores
     * @param conex
     * @param idPaciente
     * @return 
     */
    public ObservableList<Receta> listaRecetasAnteriores(Connection conex, String idPaciente){
        ObservableList<Receta> listaReceta = FXCollections.observableArrayList();
        Date dia = Date.valueOf(LocalDate.now());
        String sql = "SELECT `receta`.`id_rec`,\n" +
                        "    `receta`.`nombreMed_rec`,\n" +
                        "    `receta`.`fecha_rec`,\n" +
                        "    `receta`.`viaAdmiMed_rec`,\n" +
                        "    `receta`.`indicaMed_rec`,\n" +
                        "    `receta`.`compleIndicaMed_rec`,\n" +
                        "    `receta`.`intevaloMed_rec`,\n" +
                        "    `receta`.`tipoIntervaloMed_rec`,\n" +
                        "    `receta`.`duacionMed_rec`,\n" +
                        "    `receta`.`tipoDucacionMed_rec`,\n" +
                        "    `receta`.`indiAdicionalesMed`,\n" +
                        "    `receta`.`id_paciente`,\n" +
                        "    `receta`.`id_medico`\n" +
                    "FROM receta WHERE id_paciente = '"+idPaciente+"'\n"+
                    "AND fecha_rec < '"+dia+"'\n"+
                    "ORDER BY fecha_rec ASC;";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaReceta.add(new Receta( 
                                    res.getString  ("id_rec"),
                                    res.getString  ("nombreMed_rec"),
                                    res.getDate    ("fecha_rec"),
                                    res.getString  ("viaAdmiMed_rec"),
                                    res.getInt     ("indicaMed_rec"),
                                    res.getString  ("compleIndicaMed_rec"),
                                    res.getInt     ("intevaloMed_rec"),
                                    res.getString  ("tipoIntervaloMed_rec"),
                                    res.getInt     ("duacionMed_rec"),
                                    res.getString  ("tipoDucacionMed_rec"),
                                    res.getString  ("indiAdicionalesMed"),
                                    res.getString  ("id_paciente"),
                                    res.getString  ("id_medico")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaReceta;
    }
    
    
   /**
    * borra el medicamento seleccioando
    * @param Dato
    * @param conex 
    */
    public void borrarMedicamento(String Dato, Connection conex){
        String sttm = "DELETE FROM receta WHERE id_rec = '"+Dato+"'";
        try(PreparedStatement stta = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            stta.addBatch();
            stta.executeBatch();
            conex.commit();
            aux.informacionUs("El medicamento ha sido borrado", 
                            "El medicamento ha sido borrado", 
                            "El medicamento ha sido borrado exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
/****************************************************************************/
    //Setters and getters
    public final void setId_rec(String value) {
        id_rec.set(value);
    }

    public final String getId_rec() {
        return id_rec.get();
    }

    public final StringProperty id_recProperty() {
        return id_rec;
    }

    public final void setNombreMed_rec(String value) {
        nombreMed_rec.set(value);
    }

    public final String getNombreMed_rec() {
        return nombreMed_rec.get();
    }

    public final StringProperty nombreMed_recProperty() {
        return nombreMed_rec;
    }

    public final void setViaAdmiMed_rec(String value) {
        viaAdmiMed_rec.set(value);
    }

    public final String getViaAdmiMed_rec() {
        return viaAdmiMed_rec.get();
    }

    public final StringProperty viaAdmiMed_recProperty() {
        return viaAdmiMed_rec;
    }

    public final void setIndicaMed_rec(Integer value) {
        indicaMed_rec.set(value);
    }

    public final Integer getIndicaMed_rec() {
        return indicaMed_rec.get();
    }

    public final IntegerProperty indicaMed_recProperty() {
        return indicaMed_rec;
    }

    public final void setCompleIndicaMed_rec(String value) {
        compleIndicaMed_rec.set(value);
    }

    public final String getCompleIndicaMed_rec() {
        return compleIndicaMed_rec.get();
    }

    public final StringProperty compleIndicaMed_recProperty() {
        return compleIndicaMed_rec;
    }

    public final void setIntevaloMed_rec(Integer value) {
        intevaloMed_rec.set(value);
    }

    public final Integer getIntevaloMed_rec() {
        return intevaloMed_rec.get();
    }

    public final IntegerProperty intevaloMed_recProperty() {
        return intevaloMed_rec;
    }

    public final void setTipoIntervaloMed_rec(String value) {
        tipoIntervaloMed_rec.set(value);
    }

    public final String getTipoIntervaloMed_rec() {
        return tipoIntervaloMed_rec.get();
    }

    public final StringProperty tipoIntervaloMed_recProperty() {
        return tipoIntervaloMed_rec;
    }

    public final void setDuacionMed_rec(Integer value) {
        duacionMed_rec.set(value);
    }

    public final Integer getDuacionMed_rec() {
        return duacionMed_rec.get();
    }

    public final IntegerProperty duacionMed_recProperty() {
        return duacionMed_rec;
    }

    public final void setTipoDucacionMed_rec(String value) {
        tipoDucacionMed_rec.set(value);
    }

    public final String getTipoDucacionMed_rec() {
        return tipoDucacionMed_rec.get();
    }

    public final StringProperty tipoDucacionMed_recProperty() {
        return tipoDucacionMed_rec;
    }

    public final void setIndiAdicionalesMed_rec(String value) {
        indiAdicionalesMed_rec.set(value);
    }

    public final String getIndiAdicionalesMed_rec() {
        return indiAdicionalesMed_rec.get();
    }

    public final StringProperty indiAdicionalesMed_recProperty() {
        return indiAdicionalesMed_rec;
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

    public Date getFecha_rec() {
        return fecha_rec;
    }

    public void setFecha_rec(Date fecha_rec) {
        this.fecha_rec = fecha_rec;
    }
    
    
    
    
    
    
}
