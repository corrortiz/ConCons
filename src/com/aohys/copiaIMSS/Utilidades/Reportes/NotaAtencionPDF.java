/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */

package com.aohys.copiaIMSS.Utilidades.Reportes;


import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.MVC.Modelo.Consulta;
import com.aohys.copiaIMSS.MVC.Modelo.Laboratorial;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta.Diagnostico;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta.Tratamiento;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Rayos;
import com.aohys.copiaIMSS.MVC.Modelo.Receta;
import com.aohys.copiaIMSS.MVC.Modelo.Somametropia;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.IngresoController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
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
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.elements.VerticalSpacer;
import rst.pdfbox.layout.elements.render.ColumnLayout;
import rst.pdfbox.layout.elements.render.RenderContext;
import rst.pdfbox.layout.elements.render.RenderListener;
import rst.pdfbox.layout.elements.render.VerticalLayoutHint;
import rst.pdfbox.layout.text.Alignment;
import rst.pdfbox.layout.text.Constants;
import rst.pdfbox.layout.text.Position;
import rst.pdfbox.layout.text.TextFlow;
import rst.pdfbox.layout.text.TextFlowUtil;
import rst.pdfbox.layout.text.TextSequenceUtil;

/**
 * @author Alejandro Ortiz Corro
 */

public class NotaAtencionPDF {
      
    private Paciente paci;
    private Consulta consul;
    private final Diagnostico dias = new Diagnostico();
    Somametropia somametropia = new Somametropia();
    Somametropia soma;
    Tratamiento tratamiento = new Tratamiento();
    Receta receta = new Receta();
    Rayos ray = new Rayos();
    Laboratorial lab = new Laboratorial();
        
    //Conexion Base de datos
    Hikari dbConn = new Hikari();
    //Formato combobox
    ObservableList<Diagnostico> listaDiagnos = FXCollections.observableArrayList();
    ObservableList<Tratamiento> listaTratamientos = FXCollections.observableArrayList();
    ObservableList<Receta> listaDiria = FXCollections.observableArrayList();
    ObservableList<Rayos> listaRayos = FXCollections.observableArrayList();
    ObservableList<String> listaLaboratorial = FXCollections.observableArrayList();
    ObservableList<Laboratorial> listaLaboDia = FXCollections.observableArrayList();
    
    /**
     * Crea pdf 
     * @param consul
     */
    public void pasoPrincipal(Consulta consul) {
        this.paci = PrincipalController.pacienteAUsar;
        this.consul = consul;
        try(Connection conex = dbConn.conectarBD()) {
            listaDiagnos.addAll(dias.listaDiagnosticosConsulta(conex, consul.getId_cons()));
            listaTratamientos.addAll(tratamiento.listaTratamientoConsulta(conex, consul.getId_cons()));
            listaDiria.addAll(receta.listaRecetas(conex, paci.getId_paciente()));
            listaRayos.addAll(ray.listaRayosPacienteFecha(conex, consul.getId_paciente(), consul.getFecha_cons()));
            listaLaboDia.addAll(lab.listaLaboratorialDia(conex, consul.getId_paciente(), consul.getFecha_cons()));
            soma = somametropia.cargaSoloUno(paci.getId_paciente(), conex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    100f, 130f);

            String dir = "src/com/aohys/copiaIMSS/Utilidades/Fonts/";
           
            PDType0Font regularFont = PDType0Font.load(document.getPDDocument(), 
                    new File(dir + "OpenSans-Regular.ttf"));  
            PDType0Font obscuraFont = PDType0Font.load(document.getPDDocument(), 
                    new File(dir + "Roboto-Bold.ttf"));  
            PDType0Font italicaFont = PDType0Font.load(document.getPDDocument(), 
                    new File(dir + "OpenSans-Italic.ttf"));  
            PDType0Font italicaObscuraFont = PDType0Font.load(document.getPDDocument(), 
                    new File(dir + "OpenSans-SemiboldItalic.ttf")); 
           
            
            String outputFileName = System.getenv("AppData")+"/AO Hys/NotasMedicas/"+aux.generaID()+".pdf";
            Paragraph paragraph = new Paragraph();
            
            paragraph = new Paragraph();
            LocalDate curDateTime = consul.getFecha_cons().toLocalDate();
            String algo = "*Fecha:* " +curDateTime.format(
                DateTimeFormatter.ofPattern("EEEE',' d 'de' MMMM 'del' yyyy"));
            paragraph.addMarkup(algo, 11,
                    regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Right, 0, 0,
                    0, 0, true));
                        
            document.add(new VerticalSpacer(10));
            paragraph = new Paragraph();
            LocalTime curHour= consul.getHora_cons().toLocalTime();
            String algoAS = "*Hora:* " +curHour.format(
                DateTimeFormatter.ofPattern("hh:mm a"));
            paragraph.addMarkup(algoAS, 11,
                    regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Right, 0, 0,
                    0, 0, true));
            
            document.add(new VerticalSpacer(80));
            
            paragraph = new Paragraph();
            String lugarQ = "*Nota de Atención Médica*";
            paragraph.addMarkup(lugarQ, 18, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugarSexoA;
            if (consul.getPrimeraVez_cons()) {
                lugarSexoA = "*Primera Vez*";
            }else
                lugarSexoA = "*Subsecuente*";
            paragraph.addMarkup(lugarSexoA, 12, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugar = "*Paciente*: "+String.format("%s %s %s", 
                    paci.getNombre_paciente(), paci.getApellido_paciente(), paci.getApMaterno_paciente());
            paragraph.addMarkup(lugar, 14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugarS = "*Edad*: "+aux.edadConMes(paci.getFechaNacimiento_paciente());
            paragraph.addMarkup(lugarS, 14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugarSexo = "*Sexo*: "+paci.getSexo_paciente();
            paragraph.addMarkup(lugarSexo, 14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            document.add(new VerticalSpacer(10));
            
            String peso = String.valueOf(soma.getPeso_soma());
            String talla = String.valueOf(soma.getTalla_soma());
            String sistolica = String.valueOf(soma.getSistolica_soma());
            String distolica = String.valueOf(soma.getDiastolica_soma());
            String cardiaca = String.valueOf(soma.getFrecCardia_soma());
            String respiratoria = String.valueOf(soma.getFrecRespiratoria_soma());
            String temperatura = String.valueOf(soma.getTemperatura_soma());
            
            paragraph = new Paragraph();
            String textSoma = String.format("*Talla* %s cm *Peso* %s kg, *Temperatura* %s°C *Tensión Arterial* %s/%s mmHg \n "
                    + "*Frecuencia Cardíaca* %s latidos/min *Frecuencia Respiratoria* %s resp/min", 
                    peso, talla, temperatura, sistolica, distolica, cardiaca, respiratoria);
            paragraph.addMarkup(textSoma,12, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph);
            document.add(new VerticalSpacer(20));
            
            
            if (!listaDiagnos.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Diagnóstico(S)*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                for (Diagnostico e : listaDiagnos) {
                    paragraph = new Paragraph();
                    String text = String.format("*%s*", e.getDiagnostico_diag());
                    paragraph.addMarkup(text,10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph);
                    if (!e.getComplemento_diag().isEmpty()) {
                        paragraph = new Paragraph();
                        String textint = String.format("Complemento de Dx: %s", 
                                e.getComplemento_diag());
                        paragraph.addMarkup(textint,10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                        document.add(paragraph);
                    }
                    document.add(new VerticalSpacer(5));
                }
            }
            
            document.add(new VerticalSpacer(10));
            
            paragraph = new Paragraph();
            String lugarResumen = "*Resumen Clínico*\n "+consul.getMotivo_cons();
            paragraph.addMarkup(lugarResumen, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugarExplora = "*Comentarios*\n "+consul.getExploracion_cons();
            paragraph.addMarkup(lugarExplora, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            if (!consul.getHigiDiete_cons().isEmpty()) {
                paragraph = new Paragraph();
                String textint = String.format("*Indicaciones Higiénico-Dietéticas*\n %s", 
                        consul.getHigiDiete_cons());
                paragraph.addMarkup(textint,10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
            }
            
            document.add(new VerticalSpacer(10));
            
            if (!listaTratamientos.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Procedimiento(s)*", 
                    12, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                for (Tratamiento e : listaTratamientos) {
                    paragraph = new Paragraph();
                    String text = String.format("%s", e.getNombre_proce());
                    paragraph.addMarkup(text,10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph);
                    document.add(new VerticalSpacer(5));
                }
            }
            document.add(new VerticalSpacer(10));
            
            if (!listaDiria.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Medicamentos*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                for (Receta e : listaDiria) {
                    paragraph = new Paragraph();
                    String text = String.format("*%s*\n %d %s cada %d %s durante %d %s", 
                            e.getNombreMed_rec(), e.getIndicaMed_rec(), e.getCompleIndicaMed_rec(), 
                            e.getIntevaloMed_rec(), e.getTipoIntervaloMed_rec(), 
                            e.getDuacionMed_rec(), e.getTipoDucacionMed_rec());
                    paragraph.addMarkup(text,8, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph);
                    if (!e.getIndiAdicionalesMed_rec().isEmpty()) {
                        paragraph = new Paragraph();
                        String textint = String.format("Indicaciones adicionales: %s", 
                                e.getIndiAdicionalesMed_rec());
                        paragraph.addMarkup(textint,8, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                        document.add(paragraph);
                    }
                    document.add(new VerticalSpacer(5));
                }
            }
            
            document.add(new VerticalSpacer(10));
            
            if (!listaRayos.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Estudios de Radiodiagnóstico*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                for (Rayos rayos : listaRayos) {
                    paragraph = new Paragraph();
                    String lugarx = String.format("%s", 
                            rayos.getNombre_rayos());
                    paragraph.addMarkup(lugarx, 8, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                    if (!rayos.getIndicaciones_rayos().isEmpty()) {
                        paragraph = new Paragraph();
                        String lugarx1 = "*Indicaciones*: "+String.format("%s", 
                                rayos.getIndicaciones_rayos());
                        paragraph.addMarkup(lugarx1, 8, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                        document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                                0, 0));
                    }
                    document.add(new VerticalSpacer(5));
                }
            }
            
            document.add(new VerticalSpacer(10));
            
            if (!listaLaboDia.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Estudios de laboratorio*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                document.add(new ColumnLayout(2, 10));
                listaLaboDia.stream().forEach((laba) -> {
                    verificaLabora(laba);
                });
                if (!listaLaboratorial.isEmpty()) {
                    for (String str : listaLaboratorial) {
                        paragraph = new Paragraph();
                        String lugarx = "*Estudio*: "+str;
                        paragraph.addMarkup(lugarx, 8, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                        document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                                0, 0));
                        document.add(new VerticalSpacer(5));
                    }
                }
            }
            document.add(new VerticalSpacer(10));
            
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
            
            
            
            try(final OutputStream outputStream = new FileOutputStream(outputFileName)) {
                document.save(outputStream);
                File file = new File(outputFileName);
                creaFondo(file);
            } catch (Exception ex) {
                Logger.getLogger(NotaAtencionPDF.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(NotaAtencionPDF.class.getName()).log(Level.SEVERE, null, ex);
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
    
    
    public void creaFondo(File file) throws Exception{
        try(PDDocument realDoc = PDDocument.load(file)) {
            //the above is the document you want to watermark
            //for all the pages, you can add overlay guide, indicating watermark the original pages with the watermark document.

            HashMap<Integer, String> overlayGuide = new HashMap<Integer, String>();
            for(int i=0; i<realDoc.getNumberOfPages(); i++){
                overlayGuide.put(i+1, 
                        "src/com/aohys/copiaIMSS/Utilidades/Reportes/Fondos/fondo.pdf");
                //watermark.pdf is the document which is a one page PDF with your watermark image in it. 
                //Notice here, you can skip pages from being watermarked.
            }
            Overlay overlay = new Overlay();
            overlay.setInputPDF(realDoc);
            overlay.setOverlayPosition(Overlay.Position.BACKGROUND);
            PDDocument otrDDocument = overlay.overlay(overlayGuide);
            String outputFileName = System.getenv("AppData")+"/AO Hys/NotasMedicas/"+aux.generaID()+".pdf";
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
