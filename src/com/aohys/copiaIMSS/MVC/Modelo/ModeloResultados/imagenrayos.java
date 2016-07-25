/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.MVC.Modelo.ModeloResultados;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 * Clase de imagenes de resultado de laboratorio 
 * @author CorrO
 */
public class imagenrayos {
    private String id_imaRay;
    private Image ima__imaRay;
    private String id_rayos;
    
    /**
     * constructor lleno 
     * @param id_imaRay
     * @param ima__imaRay
     * @param id_rayos 
     */
    public imagenrayos(String id_imaRay, Image ima__imaRay, String id_rayos) {
        this.id_imaRay = id_imaRay;
        this.ima__imaRay = ima__imaRay;
        this.id_rayos = id_rayos;
    }

    /**
     * constructor vacio 
     */
    public imagenrayos() {
    }
    
    //Variables de clase
    private static final Logger logger = Logger.getLogger(imagenrayos.class.getName());
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
     * clase que se utiliza para agregar a la base de datos la imagen 
     */
    public class cargaUnaImagen extends DBTask<imagenrayos> {
        String dato;
        /**
         * constructor de la clase 
         * @param dato 
         */
        public cargaUnaImagen(String dato) {
            this.dato = dato;
        }
        
        @Override
        protected imagenrayos call() throws Exception {
            imagenrayos im = null; 
            String sqlSt = "SELECT `id_imaRay`,\n" +
                            "`ima__imaRay`,\n" +
                            "`id_rayos` \n"+
                            "FROM imagenrayos\n" +
                           "WHERE id_rayos = '"+dato+"';";
            try(Connection conex = dbConn.conectarBD();
                PreparedStatement stta = conex.prepareStatement(sqlSt);
                ResultSet res = stta.executeQuery()){
                if (res.next()) {
                    Image image = new Image(res.getBinaryStream("ima__imaRay"));
                    im = new imagenrayos(  res.getString   ("id_imaRay"),
                                        image, 
                                        res.getString   ("id_rayos"));
                }
           } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
           }
            return im;
        }
    }
    
    /**
     * clase que se utiliza para agregar a labace de datos una imagen
     */
    public class subeImagen extends DBTask<Void> {
        String id_imaRay;
        Image ima__imaRay; 
        String id_rayos;
        /**
         * constructo vacio de la clase
         * @param id_imaRay
         * @param ima__imaRay
         * @param id_rayos 
         */
        public subeImagen(String id_imaRay, Image ima__imaRay, String id_rayos) {
            this.id_imaRay = id_imaRay;
            this.ima__imaRay = ima__imaRay;
            this.id_rayos = id_rayos;
        }
        
        @Override
        protected Void call() throws Exception {
            String sqlst =  "INSERT INTO `imagenrayos`\n" +
                            "(`id_imaRay`,\n" +
                            "`ima__imaRay`,\n" +
                            "`id_rayos`)\n" +
                            "VALUES (?,?,?)";    
            try (   Connection conex = dbConn.conectarBD();
                    PreparedStatement sttm = conex.prepareStatement(sqlst)){
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
                 logger.log(Level.SEVERE, null, e);
             }
         } catch (SQLException | IOException ex) {
            logger.log(Level.SEVERE, null, ex);
         }
           return null;
        }
    }
    
    
    /***********************************************************************************/
    /**Setters and getters**/
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
