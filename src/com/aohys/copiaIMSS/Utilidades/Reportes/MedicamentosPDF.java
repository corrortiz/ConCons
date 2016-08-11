/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.Utilidades.Reportes;


import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Orientation;
import rst.pdfbox.layout.elements.PageFormat;
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

public class MedicamentosPDF {
      
    private Paciente paci;
    ObservableList<Receta> listDira;
    
    /**
     * Crea pdf 
     * @param paci
     * @param listDira
     */
    public void pasoPrincipal(Paciente paci, ObservableList<Receta> listDira) {
        this.paci = paci;
        this.listDira = listDira;
        creaPDF();
    }
    
    Auxiliar aux = new Auxiliar();
    
    /**
    * Crea pdf
    */
    public void creaPDF(){
        try {
            float hMargin = 30;
            float vMargin = 70;
            
            PageFormat a5_landscape = 
                    PageFormat.with().mediaBox(
                            new PDRectangle( Constants.A5.getHeight(), Constants.A5.getWidth()))
                            .orientation(Orientation.Landscape)
                            .margins(hMargin, hMargin, 100f, 130f)
                            .build();
            
            Document document = new Document(a5_landscape);
           
            String outputFileName = System.getenv("AppData")+"/AO Hys/Recetas/"+aux.generaID()+".pdf";
            Paragraph paragraph = new Paragraph();
            
            LocalDate curDateTime = LocalDate.now();
            String algo = "*Fecha:* " +curDateTime.format(
                DateTimeFormatter.ofPattern("EEEE',' d 'de' MMMM 'del' yyyy"));
            paragraph.addMarkup(algo, 12,
                    BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Right, 0, 0, 0, 0));
            
            paragraph = new Paragraph();
            String lugarQ = "*Receta*";
            paragraph.addMarkup(lugarQ, 22, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            document.add(new VerticalSpacer(10));
            paragraph = new Paragraph();
            String lugar = "*Paciente*: "+String.format("%s %s %s", 
                    paci.getNombre_paciente(), paci.getApellido_paciente(), paci.getApMaterno_paciente());
            paragraph.addMarkup(lugar, 10, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugarS = "*Edad*: "+aux.edadConMes(paci.getFechaNacimiento_paciente());
            paragraph.addMarkup(lugarS, 10, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            document.add(new VerticalSpacer(10));
            
            if (!listDira.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Medicamentos*", 
                    12, BaseFont.Helvetica);
                document.add(paragraph);
                for (Receta e : listDira) {
                    paragraph = new Paragraph();
                    String text = String.format("*%s*\n %d %s cada %d %s durante %d %s", 
                            e.getNombreMed_rec(), e.getIndicaMed_rec(), e.getCompleIndicaMed_rec(), 
                            e.getIntevaloMed_rec(), e.getTipoIntervaloMed_rec(), 
                            e.getDuacionMed_rec(), e.getTipoDucacionMed_rec());
                    paragraph.addMarkup(text,8, BaseFont.Helvetica);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 20, 0, 0, 0));
                    if (!e.getIndiAdicionalesMed_rec().isEmpty()) {
                        paragraph = new Paragraph();
                        String textint = String.format("Indicaciones adicionales: %s", 
                                e.getIndiAdicionalesMed_rec());
                        paragraph.addMarkup(textint,8, BaseFont.Helvetica);
                        document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 20, 0, 0, 0));
                    }
                    document.add(new VerticalSpacer(5));
                }
            }
            
            document.addRenderListener(new RenderListener() {
                @Override
                public void beforePage(RenderContext renderContext){
                }

                @Override
                public void afterPage(RenderContext renderContext)
                    throws IOException {
                        String firma = String.format("Firma:  ___________________________");
                        TextFlow texflowFirma = TextFlowUtil.createTextFlow(firma, 11,
                            PDType1Font.HELVETICA);
                        float offsetFirma = renderContext.getDocument().getMarginLeft()
                            + TextSequenceUtil.getOffset(texflowFirma,
                                renderContext.getWidth(), Alignment.Left);
                        texflowFirma.drawText(renderContext.getContentStream(), new Position(
                            offsetFirma, 120), Alignment.Right);
                    
                        String str = String.format("Medico: %s %s %s", 
                                IngresoController.usua.getNombre_medico(),IngresoController.usua.getApellido_medico(),
                                IngresoController.usua.getApMaterno_medico());
                        TextFlow textSt = TextFlowUtil.createTextFlow(str, 11,
                            PDType1Font.HELVETICA);
                        float offsetS = renderContext.getDocument().getMarginLeft()
                            + TextSequenceUtil.getOffset(textSt,
                                renderContext.getWidth(), Alignment.Left);
                        textSt.drawText(renderContext.getContentStream(), new Position(
                            offsetS, 100), Alignment.Right);

                        String stra = String.format("Cedula: %s", 
                                IngresoController.usua.getCedulaProfecional_medico());
                        TextFlow textStA = TextFlowUtil.createTextFlow(stra, 11,
                            PDType1Font.HELVETICA);
                        float offsetSA = renderContext.getDocument().getMarginLeft()
                            + TextSequenceUtil.getOffset(textStA,
                                renderContext.getWidth(), Alignment.Left);
                        textStA.drawText(renderContext.getContentStream(), new Position(
                            offsetSA, 80), Alignment.Right);
                }
            });
            
            try(final OutputStream outputStream = new FileOutputStream(outputFileName);) {
                document.save(outputStream);
                File file = new File(outputFileName);
                creaFondo(file);
            } catch (Exception ex) {
                Logger.getLogger(MedicamentosPDF.class.getName()).log(Level.SEVERE, null, ex);
            }
             
        } catch (IOException ex) {
            Logger.getLogger(MedicamentosPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * crea el overlay del fondo 
     * @param file
     * @throws Exception 
     */
    public void creaFondo(File file) throws Exception{        
        PDDocument realDoc = PDDocument.load(file);
        //the above is the document you want to watermark
        //for all the pages, you can add overlay guide, 
        //indicating watermark the original pages with the watermark document.
        
        HashMap<Integer, String> overlayGuide = new HashMap<Integer, String>();
        for(int i=0; i<realDoc.getNumberOfPages(); i++){
            overlayGuide.put(i+1, 
                    "src/com/aohys/copiaIMSS/Utilidades/Reportes/Fondos/FormatoReporteMediaCarta.pdf");
            //this is the document which is a one page PDF with your watermark image in it. 
        }
        Overlay overlay = new Overlay();
        overlay.setInputPDF(realDoc);
        overlay.setOverlayPosition(Overlay.Position.BACKGROUND);
        PDDocument otrDDocument = overlay.overlay(overlayGuide);
        String outputFileName = System.getenv("AppData")+"/AO Hys/Recetas/"+aux.generaID()+".pdf";
        try(final OutputStream outputStream = 
                new FileOutputStream(outputFileName);) {
            otrDDocument.save(outputStream);
        } catch (Exception e) {
            Logger.getLogger(NotaAtencionPDF.class.getName()).log(Level.SEVERE, null, e);
        }
        File files = new File(outputFileName);
        Desktop dt = Desktop.getDesktop();
        dt.open(files);
    }
    
}
