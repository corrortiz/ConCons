/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo.ModeloResultados;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.BaseDatos.MysqlConnectionSingle;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;

/**
 * @author Alejandro Ortiz Corro
 */

public class PDFLab {
    private String id_pdfLab;
    private File pdf_pdfLab;
    private String id_lab;

    public PDFLab(String id_pdfLab, File pdf_pdfLab, String id_lab) {
        this.id_pdfLab = id_pdfLab;
        this.pdf_pdfLab = pdf_pdfLab;
        this.id_lab = id_lab;
    }

    public PDFLab() {
    }
    
    //Variables de clase
    private static final Logger logger = Logger.getLogger(PDFLab.class.getName());
    Hikari dbConn = new Hikari();
    Auxiliar aux = new Auxiliar();
    MysqlConnectionSingle dbSingle = new MysqlConnectionSingle();

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
     * clase que agrega pdf a la base de datos
     */
    public class agregarPDFTask extends DBTask<Void> {
        String id_pdfLab; 
        File pdf_pdfLab; 
        String id_lab;
        /**
         * agrega pdf de resultados a la base de datos
         * @param id_pdfLab
         * @param pdf_pdfLab
         * @param id_lab 
         */
        public agregarPDFTask(String id_pdfLab, File pdf_pdfLab, String id_lab) {
            this.id_pdfLab = id_pdfLab;
            this.pdf_pdfLab = pdf_pdfLab;
            this.id_lab = id_lab;
        }
        
        @Override
        protected Void call() throws Exception {
            String sqlst =  "INSERT INTO `pdflab`\n" +
                        "(`id_pdfLab`,\n" +
                        "`pdf_pdfLab`,\n" +
                        "`id_lab`)\n" +
                        "VALUES (?,?,?)";    
            try (Connection conex = dbConn.conectarBD();
                    PreparedStatement sttm = conex.prepareStatement(sqlst)){
                conex.setAutoCommit(false);
                //Combierte a blob
                byte[] pdfData = new byte[(int) pdf_pdfLab.length()];
                DataInputStream dis = new DataInputStream(new FileInputStream(pdf_pdfLab));
                dis.readFully(pdfData);
                dis.close();
                //Termina y envia
                sttm.setString  (1, id_pdfLab);
                sttm.setBytes   (2, pdfData);
                sttm.setString  (3, id_lab);
                sttm.addBatch();
                sttm.executeBatch();
                conex.commit();
            } catch (SQLException | IOException ex ) {
                logger.log(Level.SEVERE, null, ex);
            }
            return null;
        }
    }
    
    /**
     * clase que solo carga un pdf seleccionado 
     */
    public class cargaSolaUnResultadoTask extends DBTask<PDFLab> {
        String dato;

        public cargaSolaUnResultadoTask(String dato) {
            this.dato = dato;
        }
        
        @Override
        protected PDFLab call() throws Exception {
            PDFLab im = null; 
            String sqlSt = "SELECT `id_pdfLab`,\n" +
                            "`pdf_pdfLab`,\n" +
                            "`id_lab` \n"+
                            "FROM pdflab\n" +
                           "WHERE id_lab = '"+dato+"';";
            try(Connection conex = dbConn.conectarBD();
                    PreparedStatement stta = conex.prepareStatement(sqlSt);
                ResultSet res = stta.executeQuery()){
                if (res.next()) {
                    byte[] data = res.getBytes("pdf_pdfLab");
                    String outputFileName = System.getenv("AppData")+"/AO Hys/Laboratoriales/"+aux.generaID()+".pdf";
                    try (FileOutputStream fos = new FileOutputStream(outputFileName)) {
                        fos.write(data);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    File pdf = new File(outputFileName);
                    Files.write(pdf.toPath(), data);
                    im = new PDFLab(  res.getString   ("id_pdfLab"),
                                        pdf, 
                                        res.getString   ("id_lab"));
                }
           } catch (SQLException | IOException ex) {
                ex.printStackTrace();
           }
        return im;
        }
        
    }
    
    /**************************************/
    //Setters and getters
    
    public String getId_pdfLab() {
        return id_pdfLab;
    }

    public void setId_pdfLab(String id_pdfLab) {
        this.id_pdfLab = id_pdfLab;
    }

    public File getPdf_pdfLab() {
        return pdf_pdfLab;
    }

    public void setPdf_pdfLab(File pdf_pdfLab) {
        this.pdf_pdfLab = pdf_pdfLab;
    }

    public String getId_lab() {
        return id_lab;
    }

    public void setId_lab(String id_lab) {
        this.id_lab = id_lab;
    }
    
    
    
}
