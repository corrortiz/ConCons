package com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta;

/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */


import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.sql.Connection;
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

public class Diagnostico {
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
    private StringProperty id_diag;
    private StringProperty diagnostico_diag;
    private StringProperty complemento_diag;
    private StringProperty id_cons;
    

    /**
     * constructor lleno
     * @param id_diag
     * @param diagnostico_diag
     * @param complemento_diag
     * @param id_cons 
     */
    public Diagnostico(String id_diag, String diagnostico_diag, String complemento_diag, String id_cons) {
        this.id_diag = new SimpleStringProperty(id_diag);
        this.diagnostico_diag = new SimpleStringProperty(diagnostico_diag);
        this.complemento_diag = new SimpleStringProperty(complemento_diag);
        this.id_cons = new SimpleStringProperty(id_cons);
    }

    /**
     * constructor vacio
     */
    public Diagnostico() {
    }

    /**
     * agrega diagnostico de la consulta
     * @param id_diag
     * @param diagnostico_diag
     * @param complemento_diag
     * @param id_cons
     * @param conex 
     */
    public void agregaDiagnostico(String id_diag, String diagnostico_diag, 
            String complemento_diag, String id_cons, Connection conex){
        String sqlst =  "INSERT INTO `diagnostico`\n" +
                        "(`id_diag`,\n" +
                        "`diagnostico_diag`,\n" +
                        "`complemento_diag`,\n" +
                        "`id_cons`)\n" +
                        "VALUES (?,?,?,?)";
        try(PreparedStatement sttm = conex.prepareStatement(sqlst)) {
            conex.setAutoCommit(false);
            sttm.setString  (1, id_diag);
            sttm.setString  (2, diagnostico_diag);
            sttm.setString  (3, complemento_diag);
            sttm.setString  (4, id_cons);
            sttm.addBatch();
            sttm.executeBatch();
            conex.commit();
            aux.informacionUs("Diagnóstico guardado", 
                        "Diagnóstico guardados", 
                    "El diagnóstico ha sido guardado exitosamente en la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * lista de diagnosticos de la consulta
     * @param conex
     * @param idDiagnostico
     * @return 
     */
    public ObservableList<Diagnostico> listaDiagnosticosConsulta(Connection conex, String idDiagnostico){
        ObservableList<Diagnostico> listaDiagnostico = FXCollections.observableArrayList();
        String sql = "SELECT `id_diag`,\n" +
                        "`diagnostico_diag`,\n" +
                        "`complemento_diag`,\n" +
                        "`id_cons`\n" +
                     "FROM `diagnostico` WHERE id_cons = '"+idDiagnostico+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaDiagnostico.add(new Diagnostico( 
                                    res.getString("id_diag"), 
                                    res.getString("diagnostico_diag"), 
                                    res.getString("complemento_diag"), 
                                    res.getString("id_cons")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaDiagnostico;
    }
    
    
    public ObservableList<Diagnostico> listaDiagnosticos(Connection conex, String idPaciente, String idDiagnostico){
        ObservableList<Diagnostico> listaDiagnostico = FXCollections.observableArrayList();
        String sql = "SELECT `id_diag`,\n" +
                        "`diagnostico_diag`,\n" +
                        "`complemento_diag`,\n" +
                        "`id_cons`\n" +
                     "FROM `diagnostico` WHERE id_paciente = '"+idPaciente+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaDiagnostico.add(new Diagnostico( 
                                    res.getString("id_diag"), 
                                    res.getString("diagnostico_diag"), 
                                    res.getString("complemento_diag"), 
                                    res.getString("id_cons")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        return listaDiagnostico;
    }
    
    /**
     * carga una lista de diagnosticos de persona sana
     * @param conex
     * @param idPaciente
     * @return 
     */
    public ObservableList<Diagnostico> listaDiagnosticosMasSano(Connection conex, String idPaciente){
        ObservableList<Diagnostico> listaDiagnostico = FXCollections.observableArrayList();
        String sql = "SELECT a.id_diag,\n" +
                    "a.diagnostico_diag,\n" +
                    "a.complemento_diag,\n" +
                    "a.id_cons, b.id_paciente\n" +
                    "FROM diagnostico a\n" +
                    "INNER JOIN consulta b\n" +
                    "ON a.id_cons = b.id_cons\n" +
                    "WHERE b.id_paciente = '"+idPaciente+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaDiagnostico.add(new Diagnostico( 
                                    res.getString("id_diag"), 
                                    res.getString("diagnostico_diag"), 
                                    res.getString("complemento_diag"), 
                                    res.getString("id_cons")));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        
         int cont = 0;
            for (Diagnostico diagnostico : listaDiagnostico) {
                if (diagnostico.getDiagnostico_diag().equals("PERSONA SANA")) {
                    cont++;
                }
            }
            if (cont == 0) {
                listaDiagnostico.add(new Diagnostico("1", "PERSONA SANA", "1234", "12345"));
            }
        
        return listaDiagnostico;
    }
    
    /**
     * clase que regresa una lista de diagnosticos y persona sana
     */
    public class listaDiagnosticosMasSanoTask extends DBTask<ObservableList<Diagnostico>> {
        String idPaciente;

        public listaDiagnosticosMasSanoTask(String idPaciente) {
            this.idPaciente = idPaciente;
        }
        
        @Override
        protected ObservableList<Diagnostico> call() throws Exception {
           ObservableList<Diagnostico> listaDiagnostico = FXCollections.observableArrayList();
            String sql = "SELECT a.id_diag,\n" +
                        "a.diagnostico_diag,\n" +
                        "a.complemento_diag,\n" +
                        "a.id_cons, b.id_paciente\n" +
                        "FROM diagnostico a\n" +
                        "INNER JOIN consulta b\n" +
                        "ON a.id_cons = b.id_cons\n" +
                        "WHERE b.id_paciente = '"+idPaciente+"';";
            try(Connection conex = dbConn.conectarBD();
                    PreparedStatement stta = conex.prepareStatement(sql);
                  ResultSet res = stta.executeQuery()) {
                while (res.next()) {
                    listaDiagnostico.add(new Diagnostico( 
                                        res.getString("id_diag"), 
                                        res.getString("diagnostico_diag"), 
                                        res.getString("complemento_diag"), 
                                        res.getString("id_cons")));
                   }
               } catch (SQLException ex) {
                   ex.printStackTrace();
               }
            int cont = 0;
            for (Diagnostico diagnostico : listaDiagnostico) {
                if (diagnostico.getDiagnostico_diag().equals("PERSONA SANA")) {
                    cont++;
                }
            }
            if (cont == 0) {
                listaDiagnostico.add(new Diagnostico("1", "PERSONA SANA", "1234", "12345"));
            }
            return listaDiagnostico;
        }
        
    }
    
    /**
     * regresa una lista de dx usados frecuentamente por el doctor 
     * @param conex
     * @param idMedico
     * @return 
     */
    public ObservableList<String> listaDxFrecuentes(Connection conex, String idMedico){
        ObservableList<String> listaDxFrec = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT a.diagnostico_diag\n" +
                    "FROM diagnostico a\n" +
                    "INNER JOIN consulta b ON a.id_cons = b.id_cons\n" +
                    "WHERE b.id_medico = '"+idMedico+"';";
        try(PreparedStatement stta = conex.prepareStatement(sql);
              ResultSet res = stta.executeQuery()) {
            while (res.next()) {
                listaDxFrec.add(res.getString("diagnostico_diag"));
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
        int conunt = 0;
        for (String string : listaDxFrec) {
            if (string.equals("PERSONA SANA")) {
                conunt++;
            }
        }
        if (conunt == 0) {
            listaDxFrec.add("PERSONA SANA");
        }
        
        return listaDxFrec;
    }
    
    public Diagnostico cargaSoloUno(String idDiagnostico, Connection conex){
        Diagnostico somametropia = null;
        String sttm = "SELECT `id_diag`,\n" +
                        "`diagnostico_diag`,\n" +
                        "`complemento_diag`,\n" +
                        "`id_cons`\n" +
                    "FROM diagnostico WHERE id_cons = '"+idDiagnostico+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                somametropia = new Diagnostico( 
                                    res.getString("id_diag"), 
                                    res.getString("diagnostico_diag"), 
                                    res.getString("complemento_diag"), 
                                    res.getString("id_cons"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return somametropia;
    }
    
    /**
     * verifica que por lo menos haya un diagnostico en la nota media
     * @param idConsult
     * @param conex
     * @return 
     */
    public boolean revisaDiagConsulta(String idConsult, Connection conex){
        boolean siNo = false;
        String sttm = "SELECT COUNT(a.diagnostico_diag) AS Resultado\n" +
                      "FROM diagnostico a\n" +
                      "WHERE a.id_cons ='"+idConsult+"';";
        try(PreparedStatement stta = conex.prepareStatement(sttm);
               ResultSet res = stta.executeQuery(); ) {
            if (res.next()) {
                siNo = (res.getInt("Resultado") != 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return siNo;
    }
    
    /**
     * borra el diagnostico seleccionado
     * @param Dato
     * @param conex 
     */
    public void borrarDiagnosticos(String Dato, Connection conex){
        String sttm = "DELETE FROM diagnostico WHERE id_diag = '"+Dato+"'";
        try(PreparedStatement stta = conex.prepareStatement(sttm)) {
            conex.setAutoCommit(false);
            stta.addBatch();
            stta.executeBatch();
            conex.commit();
            aux.informacionUs("El diagnóstico ha sido borrado", 
                            "El diagnóstico ha sido borrado", 
                            "El diagnóstico ha sido borrado exitosamente de la base de datos");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
/*************************************************************************************/
    //Setter and Getters
    public final void setId_diag(String value) {
        id_diag.set(value);
    }

    public final String getId_diag() {
        return id_diag.get();
    }

    public final StringProperty id_diagProperty() {
        return id_diag;
    }

    public final void setDiagnostico_diag(String value) {
        diagnostico_diag.set(value);
    }

    public final String getDiagnostico_diag() {
        return diagnostico_diag.get();
    }

    public final StringProperty diagnostico_diagProperty() {
        return diagnostico_diag;
    }

    public final void setComplemento_diag(String value) {
        complemento_diag.set(value);
    }

    public final String getComplemento_diag() {
        return complemento_diag.get();
    }

    public final StringProperty complemento_diagProperty() {
        return complemento_diag;
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
