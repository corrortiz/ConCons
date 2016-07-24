/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo.ModeloResultados;

import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 * @author Alejandro Ortiz Corro
 */

public class imagenrayos {
    private String id_imaRay;
    private Image ima__imaRay;
    private String id_rayos;

    public imagenrayos(String id_imaRay, Image ima__imaRay, String id_rayos) {
        this.id_imaRay = id_imaRay;
        this.ima__imaRay = ima__imaRay;
        this.id_rayos = id_rayos;
    }

    public imagenrayos() {
    }
    
    Auxiliar aux = new Auxiliar();

    /**
     * agraga el PDF
     * @param id_imaRay
     * @param ima__imaRay
     * @param id_rayos
     * @param conex 
     */
    public void agregarPDF(String id_imaRay, Image ima__imaRay, String id_rayos, Connection conex){
        String sqlst =  "INSERT INTO `imagenrayos`\n" +
                        "(`id_imaRay`,\n" +
                        "`ima__imaRay`,\n" +
                        "`id_rayos`)\n" +
                        "VALUES (?,?,?)";    
        try (PreparedStatement sttm = conex.prepareStatement(sqlst)){
            conex.setAutoCommit(false);
            File imageFile = new File("test.png");
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(ima__imaRay, null);
            ImageIO.write(renderedImage, "png", imageFile);
            try(FileInputStream fis = new FileInputStream(imageFile)) {
                sttm.setString          (1, id_imaRay);
                sttm.setBinaryStream    (2, (InputStream)fis, (int)(imageFile.length()));
                sttm.setString          (3, id_rayos);
                sttm.addBatch();
                sttm.executeBatch();
                conex.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public imagenrayos cargaSolaUnResultado(String dato, Connection conex){
        imagenrayos im = null; 
        String sqlSt = "SELECT `id_imaRay`,\n" +
                        "`ima__imaRay`,\n" +
                        "`id_rayos` \n"+
                        "FROM imagenrayos\n" +
                       "WHERE id_rayos = '"+dato+"';";
        try(PreparedStatement stta = conex.prepareStatement(sqlSt);
            ResultSet res = stta.executeQuery()){
            if (res.next()) {
                //byte[] data = res.getBytes("ima__imaRay");
                //BufferedImage img = ImageIO.read(new ByteArrayInputStream(data));
                //Image image = SwingFXUtils.toFXImage(img, null);
                Image image = new Image(res.getBinaryStream("ima__imaRay"));
                im = new imagenrayos(  res.getString   ("id_imaRay"),
                                    image, 
                                    res.getString   ("id_rayos"));
            }
       } catch (SQLException ex) {
            ex.printStackTrace();
       }
    return im;
   }

    public String getId_imaRay() {
        return id_imaRay;
    }

    public void setId_imaRay(String id_imaRay) {
        this.id_imaRay = id_imaRay;
    }

    public Image getIma__imaRay() {
        return ima__imaRay;
    }

    public void setIma__imaRay(Image ima__imaRay) {
        this.ima__imaRay = ima__imaRay;
    }

    public String getId_rayos() {
        return id_rayos;
    }

    public void setId_rayos(String id_rayos) {
        this.id_rayos = id_rayos;
    }
    
    
    
   
    
    
    
}
