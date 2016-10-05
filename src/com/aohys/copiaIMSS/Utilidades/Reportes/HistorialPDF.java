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
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.antNoPato;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.ant_Heredo_Familiar;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.masAnt_Heredo_Familiar;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoAdicciones;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoAlergias;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoMedicos;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoQuirugicos;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoTransfucion;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.patoTraumaticos;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta.Diagnostico;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloConsulta.Tratamiento;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.Modelo.Usuario;
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
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.elements.VerticalSpacer;
import rst.pdfbox.layout.elements.render.RenderContext;
import rst.pdfbox.layout.elements.render.RenderListener;
import rst.pdfbox.layout.elements.render.VerticalLayoutHint;
import rst.pdfbox.layout.text.Alignment;
import rst.pdfbox.layout.text.Constants;

/**
 * @author Alejandro Ortiz Corro
 */

public class HistorialPDF {
      
    private Paciente paci;
    antNoPato antNoPato = new antNoPato();
    antNoPato antNoPatoCargado;
    masAnt_Heredo_Familiar masAnteHere = new masAnt_Heredo_Familiar();
    masAnt_Heredo_Familiar ante;
    ant_Heredo_Familiar antHere = new ant_Heredo_Familiar();
    patoMedicos pMedicos = new patoMedicos();
    patoQuirugicos pQuiruquicos = new patoQuirugicos();    
    patoTraumaticos pTraumaticos = new patoTraumaticos();
    patoTransfucion pTransfucion = new patoTransfucion();
    patoAlergias patoAlergias = new patoAlergias();
    patoAdicciones pAdicciones = new patoAdicciones();
    Consulta consulta = new Consulta();
    Diagnostico diagnostico = new Diagnostico();
    Tratamiento tratamiento = new Tratamiento();
    
    //Conexion Base de datos
    Hikari dbConn = new Hikari();
    //Formato combobox
    ObservableList<ant_Heredo_Familiar> listaPFamiliars = FXCollections.observableArrayList();
    ObservableList<patoMedicos> listaMedicos = FXCollections.observableArrayList();
    ObservableList<patoQuirugicos> listaQuirugicoses = FXCollections.observableArrayList();
    ObservableList<patoTraumaticos> listaTrauma = FXCollections.observableArrayList();
    ObservableList<patoTransfucion> listaTransfu = FXCollections.observableArrayList();
    ObservableList<patoAlergias> listaAlergia = FXCollections.observableArrayList();
    ObservableList<patoAdicciones> listaAddicion= FXCollections.observableArrayList();
    ObservableList<Consulta> listaConsulta = FXCollections.observableArrayList();
    ObservableList<Diagnostico> listaDiagnos = FXCollections.observableArrayList();
    ObservableList<Tratamiento> listaTratamientos =FXCollections.observableArrayList();
    
    /**
     * Crea pdf 
     */
    public void pasoPrincipal() {
        this.paci = PrincipalController.pacienteAUsar;
        try(Connection conex = dbConn.conectarBD()) {
            antNoPatoCargado = antNoPato.cargaSoloUno(paci.getId_paciente(), conex);
            ante = masAnteHere.cargaSoloUno(paci.getId_paciente(), conex);
            listaPFamiliars.addAll(antHere.listaPadecimientosAnte(conex, paci.getId_paciente()));
            listaMedicos.addAll(pMedicos.listaAntePatoMedico(conex, paci.getId_paciente()));
            listaQuirugicoses.addAll(pQuiruquicos.listaAntePatoQuir(conex, paci.getId_paciente()));
            listaTrauma.addAll(pTraumaticos.listaAntePaTrauma(conex, paci.getId_paciente()));
            listaTransfu.addAll(pTransfucion.listaAntePaTransfucion(conex, paci.getId_paciente()));
            listaAlergia.addAll(patoAlergias.listaAntePaAlergias(conex, paci.getId_paciente()));
            listaAddicion.addAll(pAdicciones.listaAntePaAdicciones(conex, paci.getId_paciente()));
            listaConsulta.addAll(consulta.listaConsulPaciente(conex, paci.getId_paciente()));
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
            
            String outputFileName = System.getenv("AppData")+"/AO Hys/Historiales/"+aux.generaID()+".pdf";
            
            String dir = "src/com/aohys/copiaIMSS/Utilidades/Fonts/";
            PDType0Font regularFont = PDType0Font.load(document.getPDDocument(), 
                    new File(dir + "OpenSans-Regular.ttf"));  
            PDType0Font obscuraFont = PDType0Font.load(document.getPDDocument(), 
                    new File(dir + "Roboto-Bold.ttf"));  
            PDType0Font italicaFont = PDType0Font.load(document.getPDDocument(), 
                    new File(dir + "OpenSans-Italic.ttf"));  
            PDType0Font italicaObscuraFont = PDType0Font.load(document.getPDDocument(), 
                    new File(dir + "OpenSans-SemiboldItalic.ttf")); 
            
            Paragraph paragraph = new Paragraph();
            
            paragraph = new Paragraph();
            LocalDate curDateTime = LocalDate.now();
            String algo = "*Fecha:* " +curDateTime.format(
                DateTimeFormatter.ofPattern("EEEE',' d 'de' MMMM 'del' yyyy"));
            paragraph.addMarkup(algo, 11,
                    regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Right, 0, 0,
                    0, 0, true));
                        
            document.add(new VerticalSpacer(10));
            paragraph = new Paragraph();
            LocalTime curHour= LocalTime.now();
            String algoAS = "*Hora:* " +curHour.format(
                DateTimeFormatter.ofPattern("hh:mm a"));
            paragraph.addMarkup(algoAS, 11,
                    regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Right, 0, 0,
                    0, 0, true));
            
            document.add(new VerticalSpacer(80));
            
            paragraph = new Paragraph();
            String lugarQ = "*Historial Médico*";
            paragraph.addMarkup(lugarQ, 18, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
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
            LocalDate bridDate = paci.getFechaNacimiento_paciente().toLocalDate();
            String cumple = "*Fecha de nacimiento*: " +bridDate.format(
                DateTimeFormatter.ofPattern("EEEE',' d 'de' MMMM 'del' yyyy"));
            paragraph.addMarkup(cumple, 14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugarSexo = "*Sexo*: "+paci.getSexo_paciente();
            paragraph.addMarkup(lugarSexo, 14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            
            paragraph = new Paragraph();
            String lugarCURP = "*CURP*: "+paci.getCurp_paciente();
            paragraph.addMarkup(lugarCURP, 14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
            document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                    0, 0));
            document.add(new VerticalSpacer(10));
            
            if (antNoPatoCargado != null) {
                String religion = antNoPatoCargado.getReligion_antNP();
                String lugarNacimiento = antNoPatoCargado.getLugarNaci_antNP();
                String estadoCivil = antNoPatoCargado.getEstaCivil_antNP();
                String escolaridad = antNoPatoCargado.getEscolaridad_antNP();
                String completaImcompleta;
                if (antNoPatoCargado.getEscoCompInco_antNP()) {
                    completaImcompleta = "Completa";
                }else
                    completaImcompleta = "Incompleta";
                String higiene = antNoPatoCargado.getHigiene_antNP();
                String tipoActividad;
                String frecuenciaActividad = "";
                String vecesActividad = "";
                if (antNoPatoCargado.getFrecuencia_antNP() == 0) {
                    tipoActividad = "No realiza actividad física";
                }else{
                    tipoActividad = antNoPatoCargado.getActividadFisica_antNP();
                    frecuenciaActividad = Integer.toString(antNoPatoCargado.getFrecuencia_antNP());
                    vecesActividad =  antNoPatoCargado.getFrecVeces_antNP();
                }
                String sexualidad = antNoPatoCargado.getSexualidad_antNP();
                String numeroParejas;
                if (antNoPatoCargado.getSexualidad_antNP().equals("No aplica")) {
                    numeroParejas = "No aplica";
                }else
                    numeroParejas = Integer.toString(antNoPatoCargado.getNumParejas_antNP());
                
                String tipoSangre = antNoPatoCargado.getSangre_antNP();
                String tipoAlimentacion = antNoPatoCargado.getAlimentacion_antNP();
                
                paragraph = new Paragraph();
                paragraph.addMarkup("*Información personal*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                paragraph = new Paragraph();
                String hreligion = "*Religión:* "+religion;
                paragraph.addMarkup(hreligion, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                        0, 0));
                paragraph = new Paragraph();
                String hLugarNaci = "*Lugar de nacimiento:* "+lugarNacimiento;
                paragraph.addMarkup(hLugarNaci, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                        0, 0));
                paragraph = new Paragraph();
                String hEstadoCivil = "*Estado Civil:* "+estadoCivil;
                paragraph.addMarkup(hEstadoCivil, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                        0, 0));
                paragraph = new Paragraph();
                String hEscolaridad = String.format("*Escolaridad:* %s %s", escolaridad, completaImcompleta);
                paragraph.addMarkup(hEscolaridad, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                        0, 0));
                paragraph = new Paragraph();
                String hHigiene = "*Higiene personal:* "+higiene;
                paragraph.addMarkup(hHigiene, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                        0, 0));
                
                if (antNoPatoCargado.getFrecuencia_antNP() == 0) {
                    paragraph = new Paragraph();
                    String hActividadFisica = "*Actividad física:* "+tipoActividad;
                    paragraph.addMarkup(hActividadFisica, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                }else{
                    paragraph = new Paragraph();
                    String hFrecuencia = String.format("*Actividad física:* %s con una frecuencia de %s veces a(l) %s", 
                            tipoActividad,frecuenciaActividad,vecesActividad );
                    paragraph.addMarkup(hFrecuencia, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                }
                
                if (!sexualidad.equals("No aplica")) {
                    paragraph = new Paragraph();
                    String hSexualidad = String.format("*Preferencia sexual:* %s con %s pareja(s)", 
                        sexualidad, numeroParejas);
                    paragraph.addMarkup(hSexualidad, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                }
                
                paragraph = new Paragraph();
                String hSangre = "*Grupo sanguíneo:* "+tipoSangre;
                paragraph.addMarkup(hSangre, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                        0, 0));
                
                paragraph = new Paragraph();
                String hAlime = "*Calidad de la alimentación:* "+tipoAlimentacion;
                paragraph.addMarkup(hAlime, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                        0, 0));
                
                document.add(new VerticalSpacer(10));
            }
            
            if (ante != null) {
                String familiarResp_masHeredo = ante.getFamiliarResp_masHeredo();
                String disfuncion;
                if (ante.getDisfuncion_masHeredo()) {
                    disfuncion = "SI";
                }else
                    disfuncion = "NO";
                String familiarInfor_masHeredo = ante.getFamiliarInfor_masHeredo();
                
                paragraph = new Paragraph();
                paragraph.addMarkup("*Antecedentes Heredo - Familiares*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                
                paragraph = new Paragraph();
                String aInfor = "*Familiar informante:* "+familiarInfor_masHeredo;
                paragraph.addMarkup(aInfor, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                        0, 0));
                
                paragraph = new Paragraph();
                String aRespo = "*Familiar responsable del paciente:* "+familiarResp_masHeredo;
                paragraph.addMarkup(aRespo, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                        0, 0));
                
                paragraph = new Paragraph();
                String aDiscuncion = "*Disfunción familiar:* "+disfuncion;
                paragraph.addMarkup(aDiscuncion, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                        0, 0));
                
                document.add(new VerticalSpacer(5));
            }
            
            if (!listaPFamiliars.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Padecimientos hereditarios y familiares*", 
                    12, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                for (ant_Heredo_Familiar ant : listaPFamiliars) {
                    paragraph = new Paragraph();
                    String hFrecuencia = String.format("*%s* padecido por el(la) %s del paciente quien se encuentra %s", 
                            ant.getPadecimiento_antHeredo(),ant.getFamiliares_antHeredo(), 
                            (ant.getFinado_antHeredo())?"finado":"vivo");
                    paragraph.addMarkup(hFrecuencia, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                }
                document.add(new VerticalSpacer(10));
            }
            
            if (!listaMedicos.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Padecimientos previos*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                for (patoMedicos ant : listaMedicos) {
                    paragraph = new Paragraph();
                    String hFrecuencia = String.format("*%s* padecido por el paciente cuando tenia %d %s", 
                            ant.getEnfermedad_pMed(),ant.getEdad_pMed(),ant.getDuracion_pMed());
                    paragraph.addMarkup(hFrecuencia, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                }
                document.add(new VerticalSpacer(10));
            }
                
            if (!listaQuirugicoses.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Antecedentes Quirúrgicos*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                for (patoQuirugicos ant : listaQuirugicoses) {
                    paragraph = new Paragraph();
                    String hFrecuencia = String.format("Cirugía de *%s* a la cual el paciente fue sometido a la edad de %d %s", 
                            ant.getCirugia_pQuir(),ant.getEdad_pQuir(),ant.getDuracion_pQuir());
                    paragraph.addMarkup(hFrecuencia, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                }
                document.add(new VerticalSpacer(10));
            }
            
            if (!listaTrauma.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Antecedentes Traumáticos*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                for (patoTraumaticos ant : listaTrauma) {
                    paragraph = new Paragraph();
                    String hFrecuencia = String.format("*%s* %sa la edad de %d %s %s", 
                            ant.getTrauma_pTrauma(),
                            (ant.getLado_pTrauma().equals("NO APLICA"))?"":"sufrido del lado "+ant.getLado_pTrauma()+" ",
                            ant.getEdad_pTrauma(), ant.getDuracion_pTrauma(), 
                            (ant.getSecuelas_pTrauma().equals(""))?"":"con las siguientes secuelas "+ant.getSecuelas_pTrauma());
                    paragraph.addMarkup(hFrecuencia, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                }
                document.add(new VerticalSpacer(10));
            }
            
            if (!listaTransfu.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Antecedentes Transfuncionales*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                for (patoTransfucion ant : listaTransfu) {
                    paragraph = new Paragraph();
                    String hFrecuencia = String.format("A los *%d %s* se recibió una %s %s", 
                            ant.getEdad_pTrans(),ant.getDuracion_pTrans(),
                            (ant.getTipoTrasn_pTrans().equals("No lo sabe"))?"transfusión pero no sabe de que tipo fue":
                                    ant.getTipoTrasn_pTrans(),
                            (ant.getReacciones_pTrans())?"en la cual se produjeron las siguientes reacciones: "+ant.getTipoReacion_pTrans():"");
                    paragraph.addMarkup(hFrecuencia, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                }
                document.add(new VerticalSpacer(10));
            }
            
            if (!listaAlergia.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Alergias*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                for (patoAlergias ant : listaAlergia) {
                    paragraph = new Paragraph();
                    String hFrecuencia = String.format("a *%s*", 
                            ant.getDescripcion_pAler());
                    paragraph.addMarkup(hFrecuencia, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                }
                document.add(new VerticalSpacer(10));
            }
            
            if (!listaAddicion.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Adicciones*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                for (patoAdicciones ant : listaAddicion) {
                    paragraph = new Paragraph();
                    String hFrecuencia = String.format("a *%s* la cual inicio a los %d años %s de la cual %s es dependiente", 
                            ant.getAdiccion_pAdicc(), ant.getEdInicio_pAdicc(),
                            (ant.getEdFinal_pAdicc() == aux.edadNumerico(paci.getFechaNacimiento_paciente().toLocalDate()))?
                                    "y hasta la fecha continua":"y termina a los "+ant.getEdFinal_pAdicc()+" años",
                            (ant.getDependencia_pAdicc())?"si":"no");
                    paragraph.addMarkup(hFrecuencia, 10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                }
                document.add(new VerticalSpacer(10));
            }
            
            if (!listaConsulta.isEmpty()) {
                paragraph = new Paragraph();
                paragraph.addMarkup("*Consulta(s) médicas*", 
                    14, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                document.add(paragraph);
                document.add(new VerticalSpacer(5));
                for (Consulta ant : listaConsulta) {
                    Usuario usa = new Usuario();
                    usa = usa.CargaSoloUno(ant.getId_medico());
                    paragraph = new Paragraph();
                    String hFrecuencia = String.format("El *%s* con el medico *%s %s %s* especialista en *%s*", 
                            ant.getFecha_cons(), usa.getNombre_medico(), usa.getApellido_medico(), usa.getApMaterno_medico(),
                            usa.getEspecialidad_medico());
                    paragraph.addMarkup(hFrecuencia, 11, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                    document.add(paragraph, new VerticalLayoutHint(Alignment.Left, 0, 0,
                            0, 0));
                    
                    listaDiagnos.clear();
                    listaTratamientos.clear();
                    try(Connection conex = dbConn.conectarBD()) {
                        listaDiagnos.addAll(
                            diagnostico.listaDiagnosticosConsulta(conex, ant.getId_cons()));
                        listaTratamientos.addAll(
                                tratamiento.listaTratamientoConsulta(conex, ant.getId_cons()));
                    } catch (SQLException e) {
                        Logger.getLogger(HistorialPDF.class.getName()).log(Level.SEVERE, null, e);
                    }
                    
                    if (!listaDiagnos.isEmpty()) {
                        paragraph = new Paragraph();
                        paragraph.addMarkup("*Diagnóstico(S)*", 
                            11, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
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
                            
                        }
                    }
                    
                    if (!listaTratamientos.isEmpty()) {
                        paragraph = new Paragraph();
                        paragraph.addMarkup("*Procedimiento(s)*", 
                            11, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                        document.add(paragraph);
                        for (Tratamiento e : listaTratamientos) {
                            paragraph = new Paragraph();
                            String text = String.format("%s", e.getNombre_proce());
                            paragraph.addMarkup(text,10, regularFont,obscuraFont,italicaFont,italicaObscuraFont);
                            document.add(paragraph);
                        }
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
                       
                }
            });
            
            try(final OutputStream outputStream = new FileOutputStream(outputFileName);) {
                document.save(outputStream);
                File file = new File(outputFileName);
                creaFondo(file);
            } catch (Exception e) {
                Logger.getLogger(HistorialPDF.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (IOException ex) {
            Logger.getLogger(HistorialPDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * metodo para agregar las imagenes de fondo 
     * @param file
     * @throws Exception 
     */
    public void creaFondo(File file) throws Exception{
        try(PDDocument realDoc = PDDocument.load(file);){
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
            String outputFileName = System.getenv("AppData")+"/AO Hys/Historiales/"+aux.generaID()+".pdf";
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
