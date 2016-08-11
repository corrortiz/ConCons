/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.Utilidades.Reportes;


import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.MVC.Modelo.Laboratorial;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.IngresoController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import rst.pdfbox.layout.elements.render.ColumnLayout;
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

public class OrdenLaboraPDF {
      
    private Paciente paci;
    Laboratorial lab = new Laboratorial();
        
    //Conexion Base de datos
    Hikari dbConn = new Hikari();
    //Formato combobox
    ObservableList<Laboratorial> listaLaboDia = FXCollections.observableArrayList();
    ObservableList<String> listaLaboratorial = FXCollections.observableArrayList();
    /**
     * Crea pdf 
     * @param listaLaboDia
     */
    public void pasoPrincipal(ObservableList<Laboratorial> listaLaboDia) {
        this.paci = PrincipalController.pacienteAUsar;
        this.listaLaboDia = listaLaboDia;
        creaPDF();
    }
    
    Auxiliar aux = new Auxiliar();
    
    /**
    * Crea pdf
    */
    public void creaPDF(){
        try {
            float hMargin = 15;
            float vMargin = 70;
            
            String outputFileName = System.getenv("AppData")+"/AO Hys/Laboratoriales/"+aux.generaID()+".pdf";
            
             PageFormat a5_landscape = 
                    PageFormat.with().mediaBox(
                            new PDRectangle( Constants.A5.getHeight(), Constants.A5.getWidth()))
                            .orientation(Orientation.Landscape)
                            .margins(hMargin, hMargin, 90f, 130f)
                            .build();
            
            Document document = new Document(a5_landscape);
            Paragraph paragraph = new Paragraph();
            
            
            
            paragraph = new Paragraph();
            LocalDate curDateTime = LocalDate.now();
            String algo = "*Fecha:* " +curDateTime.format(
                DateTimeFormatter.ofPattern("EEEE',' d 'de' MMMM 'del' yyyy"));
            paragraph.addMarkup(algo, 11,
                    BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Right, 0, 0,
                    0, 0, true));
                        
            document.add(new VerticalSpacer(12));
            paragraph = new Paragraph();
            LocalTime curHour= LocalTime.now();
            String algoAS = "*Hora:* " +curHour.format(
                DateTimeFormatter.ofPattern("hh:mm a"));
            paragraph.addMarkup(algoAS, 11,
                    BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Right, 0, 0,
                    0, 0, true));
            
            document.add(new VerticalSpacer(20));
            
            paragraph = new Paragraph();
            String lugarQ = "*Solicitud de Estudios de Laboratorio*";
            paragraph.addMarkup(lugarQ, 16, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            

            paragraph = new Paragraph();
            String lugar = "*Paciente*: "+String.format("%s %s %s", 
                    paci.getNombre_paciente(), paci.getApellido_paciente(), paci.getApMaterno_paciente());
            paragraph.addMarkup(lugar, 12, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugarS = "*Edad*: "+aux.edadConMes(paci.getFechaNacimiento_paciente());
            paragraph.addMarkup(lugarS, 12, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugarSexo = "*Sexo*: "+paci.getSexo_paciente();
            paragraph.addMarkup(lugarSexo, 12, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            document.add(new VerticalSpacer(5));
            
            
            if (!listaLaboDia.isEmpty()) {
                document.add(new ColumnLayout(2, 10));
                paragraph = new Paragraph();
                paragraph.addMarkup("*Estudios Solicitados*", 
                    14, BaseFont.Helvetica);
                document.add(paragraph);
                document.add(new VerticalSpacer(5));
                for (Laboratorial laba : listaLaboDia) {
                    verificaLabora(laba);
                }
                if (!listaLaboratorial.isEmpty()) {
                    for (String str : listaLaboratorial) {
                        paragraph = new Paragraph();
                        String lugarx = String.format("%s", str);
                        paragraph.addMarkup(lugarx, 10, BaseFont.Helvetica);
                        document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                                0, 0));
                        document.add(new VerticalSpacer(5));
                    }
                }
            }
            
            
            document.addRenderListener(new RenderListener() {
                @Override
                public void beforePage(RenderContext renderContext){
                }

                @Override
                public void afterPage(RenderContext renderContext)throws IOException {
                    String firma = String.format("Firma:  ___________________________");
                    TextFlow texflowFirma = TextFlowUtil.createTextFlow(firma, 10,
                        PDType1Font.HELVETICA);
                    float offsetFirma = renderContext.getDocument().getMarginLeft()
                        + TextSequenceUtil.getOffset(texflowFirma,
                            renderContext.getWidth(), Alignment.Left);
                    texflowFirma.drawText(renderContext.getContentStream(), new Position(
                        offsetFirma, 120), Alignment.Right);

                    String str = String.format("Medico: %s %s %s", 
                            IngresoController.usua.getNombre_medico(),IngresoController.usua.getApellido_medico(),
                            IngresoController.usua.getApMaterno_medico());
                    TextFlow textSt = TextFlowUtil.createTextFlow(str, 10,
                        PDType1Font.HELVETICA);
                    float offsetS = renderContext.getDocument().getMarginLeft()
                        + TextSequenceUtil.getOffset(textSt,
                            renderContext.getWidth(), Alignment.Left);
                    textSt.drawText(renderContext.getContentStream(), new Position(
                        offsetS, 100), Alignment.Right);

                    String stra = String.format("Cedula: %s", 
                            IngresoController.usua.getCedulaProfecional_medico());
                    TextFlow textStA = TextFlowUtil.createTextFlow(stra, 10,
                        PDType1Font.HELVETICA);
                    float offsetSA = renderContext.getDocument().getMarginLeft()
                        + TextSequenceUtil.getOffset(textStA,
                            renderContext.getWidth(), Alignment.Left);
                    textStA.drawText(renderContext.getContentStream(), new Position(
                        offsetSA, 80), Alignment.Right);
                }
            });
            
            try(final OutputStream outputStream = new FileOutputStream(outputFileName)) {
                document.save(outputStream);
                File file = new File(outputFileName);
                creaFondo(file);
            } catch (Exception ex) {
                Logger.getLogger(MedicamentosPDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(OrdenLaboraPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * agrega la lista lab 
     * @param str
     * @param bol 
     */
    private void agregarListaLab(String str, boolean bol){
        if (bol && !listaLaboratorial.contains(str)) {
            listaLaboratorial.addAll(str);
        }
    }
    
    private void verificaLabora(Laboratorial lab){
        agregarListaLab("BIOMETRÍA HEMÁTICA COMPLETA",lab.getBIOMETRÍAHEMÁTICACOMPLETA());
        agregarListaLab("FORMULA ROJA",lab.getFORMULAROJA());
        agregarListaLab("COOMBS",lab.getCOOMBS());
        agregarListaLab("TIEMPO DE COAGULACION TP INR TPT TT",lab.getTIEMPODECOAGULACIONTPINRTPTTT());
        agregarListaLab("GRUPO SANGUÍNEO Y RH ",lab.getGRUPOSANGUÍNEOYRH());
        agregarListaLab("QUÍMICA SANGUINEA COMPLETA(5)",lab.getQUÍMICASANGUINEACOMPLETA());
        agregarListaLab("QUÍMICA SANGUINEA (GLUC,BUN,CRET)",lab.getQUÍMICASANGUINEA());
        agregarListaLab("GLUCOSA SERICA",lab.getGLUCOSASERICA());
        agregarListaLab("AC. URICO",lab.getACURICO());
        agregarListaLab("COLESTEROL",lab.getCOLESTEROL());
        agregarListaLab("TRIGLICERIDOS",lab.getTRIGLICERIDOS());
        agregarListaLab("PERFIL DE LÍPIDOS(HDL, LDL, VLDL)",lab.getPERFILDELÍPIDOS());
        agregarListaLab("PRUEBAS DE FUNCION HEPÁTICA",lab.getPRUEBASDEFUNCIONHEPÁTICA());
        agregarListaLab("ELECTROLITOS SERICOS(Na,K,CI)",lab.getELECTROLITOSSERICOS());
        agregarListaLab("E. CARDIACAS(CPK MB,CK TOTAL, TGO, DHL)",lab.getECARDIACAS());
        agregarListaLab("OTRAS ENZIMAS TROPONINA, MIOGLOBINA",lab.getOTRASENZIMASTROPONINAMIOGLOBINA());
        agregarListaLab("GENERAL DE ORINA (EGO.)",lab.getGENERALDEORINA());
        agregarListaLab("DEPURACIÓN DE CREATININA ORINA 24 HRS.",lab.getDEPURACIÓNDECREATININAORINA24HRS());
        agregarListaLab("PERFILTIROIDE",lab.getPERFILTIROIDE());
        agregarListaLab("PAPANICOLAOU",lab.getPAPANICOLAOU());
        agregarListaLab("GONADROTOFINA CORIONICA HUM,(FRACC,B)",lab.getGONADROTOFINACORIONICAHUM());
        agregarListaLab("PERFIL HORMONAL",lab.getPERFILHORMONAL());
        agregarListaLab("ANTIGENO ESPECIFICO DE PRÓSTATA(PSA)",lab.getANTIGENOESPECIFICODEPRÓSTATA());
        agregarListaLab("PROT. C REACTIVA",lab.getPROTCREACTIVA());
        agregarListaLab("FACTOR REUMATOIDE",lab.getFACTORREUMATOIDE());
        agregarListaLab("ANTIESTRESTOLISINAS",lab.getANTIESTRESTOLISINAS());
        agregarListaLab("REACIONES FEBRILES",lab.getREACIONESFEBRILES());
        agregarListaLab("V.I.H. E.L.I.S.A Western Blot",lab.getVIHELISAWesternBlot());
        agregarListaLab("UROCULTIVO",lab.getUROCULTIVO());
        agregarListaLab("COPROCULTIVO",lab.getCOPROCULTIVO());
        agregarListaLab("COPROLÓGICO",lab.getCOPRLÓGICO());
        agregarListaLab("COPROPARASITOSCÓPICO",lab.getCOPROPARASITOSCÓPICO());
        agregarListaLab("CULTIVO FARINGEO",lab.getCULTIVOFARINGEO());
        boolean otroLab;
        boolean otroCul;
        
        if (!lab.getOTROEXAMEN().isEmpty()) {
            otroLab = true;
            agregarListaLab(lab.getOTROEXAMEN(),otroLab);
        }
        
        if (!lab.getOTROCULTIVO().isEmpty()) {
            otroCul = true;
            agregarListaLab(lab.getOTROCULTIVO(),otroCul);
        }
    }
    
    /**
     * crea el overlay del fondo 
     * @param file
     * @throws Exception 
     */
    public void creaFondo(File file) throws Exception{        
        try(PDDocument realDoc = PDDocument.load(file)) {
        
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
        String outputFileName = System.getenv("AppData")+"/AO Hys/Laboratoriales/"+aux.generaID()+".pdf";
        try(final OutputStream outputStream = 
                new FileOutputStream(outputFileName);) {
            otrDDocument.save(outputStream);
        } catch (Exception e) {
            Logger.getLogger(NotaAtencionPDF.class.getName()).log(Level.SEVERE, null, e);
        }
        File files = new File(outputFileName);
        Desktop dt = Desktop.getDesktop();
        dt.open(files);
        } catch (Exception e) {
            Logger.getLogger(NotaAtencionPDF.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
