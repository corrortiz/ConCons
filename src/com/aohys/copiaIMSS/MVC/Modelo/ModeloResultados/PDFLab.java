/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo.ModeloResultados;

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
    
    Auxiliar aux = new Auxiliar();

    /**
     * agraga el PDF
     * @param id_pdfLab
     * @param pdf_pdfLab
     * @param id_lab
     * @param conex 
     */
    public void agregarPDF(String id_pdfLab, File pdf_pdfLab, String id_lab, Connection conex){
        String sqlst =  "INSERT INTO `pdflab`\n" +
                        "(`id_pdfLab`,\n" +
                        "`pdf_pdfLab`,\n" +
                        "`id_lab`)\n" +
                        "VALUES (?,?,?)";    
        try (PreparedStatement sttm = conex.prepareStatement(sqlst)){
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    /**
     * carga solo uno
     * @param dato
     * @param conex
     * @return 
     */
    public PDFLab cargaSolaUnResultado(String dato, Connection conex){
        PDFLab im = null; 
        String sqlSt = "SELECT `id_pdfLab`,\n" +
                        "`pdf_pdfLab`,\n" +
                        "`id_lab` \n"+
                        "FROM pdflab\n" +
                       "WHERE id_lab = '"+dato+"';";
        try(PreparedStatement stta = conex.prepareStatement(sqlSt);
            ResultSet res = stta.executeQuery()){
            if (res.next()) {
                byte[] data = res.getBytes("pdf_pdfLab");
                String outputFileName = aux.generaID()+".pdf";
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
