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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
            
            Document document = new Document(Constants.A4, hMargin, hMargin,
                    5f, vMargin);
            
            String outputFileName = System.getenv("AppData")+"/AO Hys/Laboratoriales/"+aux.generaID()+".pdf";
            
            ImageElement image = 
                    new ImageElement("src/com/aohys/copiaIMSS/Utilidades/Imagenes/LogoSuperior.png");
            image.setWidth(image.getWidth()/4);
            image.setHeight(image.getHeight()/4);
            document.add(image, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0, true));
            
            document.add(new VerticalSpacer(100));
            
            
            Paragraph paragraph = new Paragraph();
            String lugarQ = "*Solicitud de Estudios de Laboratorio*";
            paragraph.addMarkup(lugarQ, 18, BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            LocalDate curDateTime = LocalDate.now();
            String algo = "*Fecha:* " +curDateTime.format(
                DateTimeFormatter.ofPattern("EEEE',' d 'de' MMMM 'del' yyyy"));
            paragraph.addMarkup(algo, 11,
                    BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Right, 0, 0,
                    0, 0, true));
                        
            document.add(new VerticalSpacer(10));
            paragraph = new Paragraph();
            LocalTime curHour= LocalTime.now();
            String algoAS = "*Hora:* " +curHour.format(
                DateTimeFormatter.ofPattern("hh:mm a"));
            paragraph.addMarkup(algoAS, 11,
                    BaseFont.Helvetica);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Right, 0, 0,
                    0, 0, true));

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
            
            document.add(new VerticalSpacer(5));
            
            
            if (!listaLaboDia.isEmpty()) {
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
                            offset, 20), Alignment.Right);


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
}
