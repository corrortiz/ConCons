/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.Utilidades.Reportes;


import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Rayos;
import com.aohys.copiaIMSS.MVC.Modelo.Receta;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.IngresoController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.ImageElement;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.elements.VerticalSpacer;
import rst.pdfbox.layout.elements.render.RenderContext;
import rst.pdfbox.layout.elements.render.RenderListener;
import rst.pdfbox.layout.elements.render.VerticalLayoutHint;
import rst.pdfbox.layout.text.Alignment;
import rst.pdfbox.layout.text.BaseFont;
import rst.pdfbox.layout.text.Constants;
import rst.pdfbox.layout.text.Position;
import rst.pdfbox.layout.text.TextFlow;
import rst.pdfbox.layout.text.TextFlowUtil;
import rst.pdfbox.layout.text.TextSequenceUtil;

/**
 * @author Alejandro Ortiz Corro
 */

public class EstudioPDF {
      
    private Paciente paci;
    private Rayos rayos;
    ObservableList<Receta> listDira;
    
    /**
     * paso principal de escena
     * @param paci 
     * @param rayos 
     */
    public void pasoPrincipal(Paciente paci, Rayos rayos) {
        this.paci = paci;
        this.rayos = rayos;
        creaPDF();
    }
    
    Auxiliar aux = new Auxiliar();
    
    /**
    * Crea pdf
    */
    public void creaPDF(){
        try {
            float hMargin = 30;
            float vMargin = 30;
            
            Document document = new Document(Constants.A4, hMargin, hMargin,
                    5f, vMargin);
            
            String outputFileName = System.getenv("AppData")+"/AO Hys/Estudios/"+aux.generaID()+".pdf";
            Paragraph paragraph = new Paragraph();
            ImageElement image = 
                    new ImageElement("src/com/aohys/copiaIMSS/Utilidades/Imagenes/LogoSuperior.png");
            image.setWidth(image.getWidth()/4);
            image.setHeight(image.getHeight()/4);
            document.add(image, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0, true));
            document.add(new VerticalSpacer(30));
            paragraph = new Paragraph();
            LocalDate curDateTime = LocalDate.now();
            String algo = "*Fecha:* " +curDateTime.format(
                DateTimeFormatter.ofPattern("EEEE',' d 'de' MMMM 'del' yyyy"));
            paragraph.addMarkup(algo, 12,
                    BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Right, 0, 0,
                    0, 0, true));
                        
            document.add(new VerticalSpacer(70));
            paragraph = new Paragraph();
            String lugarQ = "*Solicitud de estudio*";
            paragraph.addMarkup(lugarQ, 18, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            
            document.add(new VerticalSpacer(10));

            paragraph = new Paragraph();
            String lugar = "*Paciente*: "+String.format("%s %s %s", 
                    paci.getNombre_paciente(), paci.getApellido_paciente(), paci.getApMaterno_paciente());
            paragraph.addMarkup(lugar, 14, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugarS = "*Edad*: "+aux.edadConMes(paci.getFechaNacimiento_paciente());
            paragraph.addMarkup(lugarS, 14, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugarSexo = "*Sexo*: "+paci.getSexo_paciente();
            paragraph.addMarkup(lugarSexo, 14, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            document.add(new VerticalSpacer(30));
            
            paragraph = new Paragraph();
            String lugarx = "*Procedimiento:* "+String.format("%s", 
                    rayos.getNombre_rayos());
            paragraph.addMarkup(lugarx, 12, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            document.add(new VerticalSpacer(10));
            
            if (!rayos.getIndicaciones_rayos().isEmpty()) {
                paragraph = new Paragraph();
                String lugarx1 = "*Indicaciones*: "+String.format("%s", 
                        rayos.getIndicaciones_rayos());
                paragraph.addMarkup(lugarx1, 12, BaseFont.Helvetica);
                document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                        0, 0));
                document.add(new VerticalSpacer(10));
            }
            
            document.addRenderListener(new RenderListener() {
                @Override
                public void beforePage(RenderContext renderContext){
                }

                @Override
                public void afterPage(RenderContext renderContext)
                    throws IOException {
                    
                    String str = String.format("Medico: %s %s %s                                Firma:", 
                                IngresoController.usua.getNombre_medico(),IngresoController.usua.getApellido_medico(),
                                IngresoController.usua.getApMaterno_medico());
                    TextFlow textSt = TextFlowUtil.createTextFlow(str, 11,
                        PDType1Font.HELVETICA);
                    float offsetS = renderContext.getDocument().getMarginLeft()
                        + TextSequenceUtil.getOffset(textSt,
                            renderContext.getWidth(), Alignment.Left);
                    textSt.drawText(renderContext.getContentStream(), new Position(
                        offsetS, 60), Alignment.Right);


                    String stra = String.format("Cedula: %s", 
                            IngresoController.usua.getCedulaProfecional_medico());
                    TextFlow textStA = TextFlowUtil.createTextFlow(stra, 11,
                        PDType1Font.HELVETICA);
                    float offsetSA = renderContext.getDocument().getMarginLeft()
                        + TextSequenceUtil.getOffset(textStA,
                            renderContext.getWidth(), Alignment.Left);
                    textStA.drawText(renderContext.getContentStream(), new Position(
                        offsetSA, 45), Alignment.Right);
                    
                    String content = String.format("Pagina %s",
                        renderContext.getPageIndex() + 1);
                    TextFlow text = TextFlowUtil.createTextFlow(content, 11,
                        PDType1Font.HELVETICA);
                    float offset = renderContext.getDocument().getMarginLeft()
                        + TextSequenceUtil.getOffset(text,
                            renderContext.getWidth(), Alignment.Left);
                    text.drawText(renderContext.getContentStream(), new Position(
                        offset, 30), Alignment.Right);
                    PDImageXObject pdImage = PDImageXObject.createFromFile(
                            "src/com/aohys/copiaIMSS/Utilidades/Imagenes/Direccion.png", renderContext.getPdDocument());
                    renderContext.getContentStream().drawImage(pdImage, Constants.A5.getWidth()-(pdImage.getHeight()/5) , 10,  pdImage.getWidth()/6, pdImage.getHeight()/6);
                }
            });
            
            final OutputStream outputStream = new FileOutputStream(outputFileName);
            document.save(outputStream);
            File file = new File(outputFileName);
            Desktop dt = Desktop.getDesktop();
            dt.open(file);
            
        } catch (IOException ex) {
            Logger.getLogger(EstudioPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
