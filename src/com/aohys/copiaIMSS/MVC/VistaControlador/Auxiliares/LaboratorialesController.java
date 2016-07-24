/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Auxiliares;

import com.aohys.copiaIMSS.BaseDatos.Vitro;
import com.aohys.copiaIMSS.MVC.Modelo.Laboratorial;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import com.aohys.copiaIMSS.Utilidades.Reportes.OrdenLaboraPDF;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class LaboratorialesController implements Initializable {
    //Variables de escena
    private PrincipalController cordi;
    Paciente paci;
    /**
     * Inicia la esecena 
     * @param cordi 
     * @param paci 
     */
    public void transmisor(PrincipalController cordi, Paciente paci) {
        this.cordi = cordi;
        this.paci = paci;
        // carga los componentes top
        cargaTop();
    }

    //Variables a que utiliza el controlador
    Auxiliar aux = new Auxiliar();
    Laboratorial laboratorial = new Laboratorial();
    //Conexion
    Vitro dbConn = new Vitro();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Button bttImprimir;
    //FXMl de chechbox
    @FXML private CheckBox chbBIOMETRÍAHEMÁTICACOMPLETA;
    @FXML private CheckBox chbFORMULAROJA;
    @FXML private CheckBox chbCOOMBS;
    @FXML private CheckBox chbTIEMPODECOAGULACIONTPINRTPTTT;
    @FXML private CheckBox chbGRUPOSANGUÍNEOYRH;
    @FXML private CheckBox chbQUÍMICASANGUINEACOMPLETA;
    @FXML private CheckBox chbQUÍMICASANGUINEA;
    @FXML private CheckBox chbGLUCOSASERICA;
    @FXML private CheckBox chbACURICO;
    @FXML private CheckBox chbCOLESTEROL;
    @FXML private CheckBox chbTRIGLICERIDOS;
    @FXML private CheckBox chbPERFILDELÍPIDOS;
    @FXML private CheckBox chbPRUEBASDEFUNCIONHEPÁTICA;
    @FXML private CheckBox chbELECTROLITOSSERICOS;
    @FXML private CheckBox chbECARDIACAS;
    @FXML private CheckBox chbOTRASENZIMASTROPONINAMIOGLOBINA;
    @FXML private CheckBox chbGENERALDEORINA;
    @FXML private CheckBox chbDEPURACIÓNDECREATININAORINA24HRS;
    @FXML private CheckBox chbPERFILTIROIDE;
    @FXML private CheckBox chbPAPANICOLAOU;
    @FXML private CheckBox chbGONADROTOFINACORIONICAHUM;
    @FXML private CheckBox chbPERFILHORMONAL;
    @FXML private CheckBox chbANTIGENOESPECIFICODEPRÓSTATA;
    @FXML private CheckBox chbPROTCREACTIVA;
    @FXML private CheckBox chbFACTORREUMATOIDE;
    @FXML private CheckBox chbANTIESTRESTOLISINAS;
    @FXML private CheckBox chbREACIONESFEBRILES;
    @FXML private CheckBox chbVIHELISAWesternBlot;
    @FXML private CheckBox chbUROCULTIVO;
    @FXML private CheckBox chbCOPROCULTIVO;
    @FXML private CheckBox chbCOPRLÓGICO;
    @FXML private CheckBox chbCOPROPARASITOSCÓPICO;
    @FXML private CheckBox chbCULTIVOFARINGEO;
    //Variables
    @FXML private TextField otroExamen;
    @FXML private TextField otroCultivo;
    @FXML private Button bttAgregar;
    //Boolean
    private boolean BIOMETRÍAHEMÁTICACOMPLETA;
    private boolean FORMULAROJA;
    private boolean COOMBS;
    private boolean TIEMPODECOAGULACIONTPINRTPTTT;
    private boolean GRUPOSANGUÍNEOYRH;
    private boolean QUÍMICASANGUINEACOMPLETA;
    private boolean QUÍMICASANGUINEA;
    private boolean GLUCOSASERICA;
    private boolean ACURICO;
    private boolean COLESTEROL;
    private boolean TRIGLICERIDOS;
    private boolean PERFILDELÍPIDOS;
    private boolean PRUEBASDEFUNCIONHEPÁTICA;
    private boolean ELECTROLITOSSERICOS;
    private boolean ECARDIACAS;
    private boolean OTRASENZIMASTROPONINAMIOGLOBINA;
    private boolean GENERALDEORINA;
    private boolean DEPURACIÓNDECREATININAORINA24HRS;
    private boolean PERFILTIROIDE;
    private boolean PAPANICOLAOU;
    private boolean GONADROTOFINACORIONICAHUM;
    private boolean PERFILHORMONAL;
    private boolean ANTIGENOESPECIFICODEPRÓSTATA;
    private boolean PROTCREACTIVA;
    private boolean FACTORREUMATOIDE;
    private boolean ANTIESTRESTOLISINAS;
    private boolean REACIONESFEBRILES;
    private boolean VIHELISAWesternBlot;
    private boolean UROCULTIVO;
    private boolean COPROCULTIVO;
    private boolean COPRLÓGICO;
    private boolean COPROPARASITOSCÓPICO;
    private boolean CULTIVOFARINGEO;
    
    Image impresora = new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/printer.png");
    Image aceptar = new Image("file:src/com/aohys/copiaIMSS/Utilidades/Logos/tick.png");
    ObservableList<Laboratorial> listaLaboDia = FXCollections.observableArrayList();
    /**
     * Carga componentes
     */
    public void cargaTop(){
        String nombre = paci.getNombre_paciente()+" "+paci.getApellido_paciente()+" "+paci.getApMaterno_paciente();
        lbNombre.setText(nombre);
    }    
   
    /**
     * formato de imagen
     */
    private void formatoImagen(){
    }
    
    /**
     * formato al botton de guardar
     */
    private void formatoBotones(){
        bttAgregar.setOnAction(evento->{
            try(Connection conex = dbConn.conectarBD()) {
                guardarLaborar(conex);
                cordi.lanzaHistoriaMedica(paci);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        
        bttImprimir.setOnAction((evento)->{
            ImprimeLaborar();
        });
        
        bttImprimir.setGraphic(new ImageView(impresora));
        bttAgregar.setGraphic(new ImageView(aceptar));
    }
    
    /**
     * formato de los texbox
     */
    private void formatoDeText(){
        otroExamen.setTextFormatter(new TextFormatter(aux.formato(500, 4)));
        otroCultivo.setTextFormatter(new TextFormatter(aux.formato(500, 4)));
        
        aux.toolTip(otroExamen, "campo opcional");
        aux.toolTip(otroCultivo, "campo opcional");
    }
    
    /**
     * regresa el resultado de los checbox
     * @param checkBox
     * @param bool
     * @return 
     */
    private boolean funcionalidadChech(CheckBox checkBox){
        if (checkBox.isSelected()) {
            return true;
        }else
            return false;
    }
    
    /**
     * guarda el estudio
     * @param conex 
     */
    private void guardarLaborar(Connection conex){
	BIOMETRÍAHEMÁTICACOMPLETA = funcionalidadChech (chbBIOMETRÍAHEMÁTICACOMPLETA);
	FORMULAROJA = funcionalidadChech (chbFORMULAROJA);
	COOMBS = funcionalidadChech (chbCOOMBS);
	TIEMPODECOAGULACIONTPINRTPTTT = funcionalidadChech (chbTIEMPODECOAGULACIONTPINRTPTTT);
	GRUPOSANGUÍNEOYRH = funcionalidadChech (chbGRUPOSANGUÍNEOYRH);
	QUÍMICASANGUINEACOMPLETA = funcionalidadChech (chbQUÍMICASANGUINEACOMPLETA);
	QUÍMICASANGUINEA = funcionalidadChech (chbQUÍMICASANGUINEA);
	GLUCOSASERICA = funcionalidadChech (chbGLUCOSASERICA);
	ACURICO = funcionalidadChech (chbACURICO);
	COLESTEROL = funcionalidadChech (chbCOLESTEROL);
	TRIGLICERIDOS = funcionalidadChech (chbTRIGLICERIDOS);
	PERFILDELÍPIDOS = funcionalidadChech (chbPERFILDELÍPIDOS);
	PRUEBASDEFUNCIONHEPÁTICA = funcionalidadChech (chbPRUEBASDEFUNCIONHEPÁTICA);
	ELECTROLITOSSERICOS = funcionalidadChech (chbELECTROLITOSSERICOS);
	ECARDIACAS = funcionalidadChech (chbECARDIACAS);
	OTRASENZIMASTROPONINAMIOGLOBINA = funcionalidadChech (chbOTRASENZIMASTROPONINAMIOGLOBINA);
	GENERALDEORINA = funcionalidadChech (chbGENERALDEORINA);
	DEPURACIÓNDECREATININAORINA24HRS = funcionalidadChech (chbDEPURACIÓNDECREATININAORINA24HRS);
	PERFILTIROIDE = funcionalidadChech (chbPERFILTIROIDE);
	PAPANICOLAOU = funcionalidadChech (chbPAPANICOLAOU);
	GONADROTOFINACORIONICAHUM = funcionalidadChech (chbGONADROTOFINACORIONICAHUM);
	PERFILHORMONAL = funcionalidadChech (chbPERFILHORMONAL);
	ANTIGENOESPECIFICODEPRÓSTATA = funcionalidadChech (chbANTIGENOESPECIFICODEPRÓSTATA);
	PROTCREACTIVA = funcionalidadChech (chbPROTCREACTIVA);
	FACTORREUMATOIDE = funcionalidadChech (chbFACTORREUMATOIDE);
	ANTIESTRESTOLISINAS = funcionalidadChech (chbANTIESTRESTOLISINAS);
	REACIONESFEBRILES = funcionalidadChech (chbREACIONESFEBRILES);
	VIHELISAWesternBlot = funcionalidadChech (chbVIHELISAWesternBlot);
	UROCULTIVO = funcionalidadChech (chbUROCULTIVO);
	COPROCULTIVO = funcionalidadChech (chbCOPROCULTIVO);
	COPRLÓGICO = funcionalidadChech (chbCOPRLÓGICO);
	COPROPARASITOSCÓPICO = funcionalidadChech (chbCOPROPARASITOSCÓPICO);
	CULTIVOFARINGEO = funcionalidadChech (chbCULTIVOFARINGEO);
        String id_lab = aux.generaID();
        String OTROEXAMEN = otroExamen.getText();
        String OTROCULTIVO = otroCultivo.getText();
        String id_paciente = paci.getId_paciente();
        Date fecha_lab = Date.valueOf(LocalDate.now());
        
        laboratorial.agregaLaboratorial(id_lab, BIOMETRÍAHEMÁTICACOMPLETA, FORMULAROJA, COOMBS, 
                TIEMPODECOAGULACIONTPINRTPTTT, GRUPOSANGUÍNEOYRH, QUÍMICASANGUINEACOMPLETA, QUÍMICASANGUINEA, 
                GLUCOSASERICA, ACURICO, COLESTEROL, TRIGLICERIDOS, PERFILDELÍPIDOS, PRUEBASDEFUNCIONHEPÁTICA, 
                ELECTROLITOSSERICOS, ECARDIACAS, OTRASENZIMASTROPONINAMIOGLOBINA, GENERALDEORINA, 
                DEPURACIÓNDECREATININAORINA24HRS, PERFILTIROIDE, PAPANICOLAOU, GONADROTOFINACORIONICAHUM, 
                PERFILHORMONAL, ANTIGENOESPECIFICODEPRÓSTATA, PROTCREACTIVA, FACTORREUMATOIDE, ANTIESTRESTOLISINAS, 
                REACIONESFEBRILES, VIHELISAWesternBlot, UROCULTIVO, COPROCULTIVO, COPRLÓGICO, COPROPARASITOSCÓPICO,
                CULTIVOFARINGEO, OTROEXAMEN, OTROCULTIVO, fecha_lab, id_paciente, conex);
    }
    
    /**
     * imprime la orden solicitada
     */
    private void ImprimeLaborar(){
	BIOMETRÍAHEMÁTICACOMPLETA = funcionalidadChech (chbBIOMETRÍAHEMÁTICACOMPLETA);
	FORMULAROJA = funcionalidadChech (chbFORMULAROJA);
	COOMBS = funcionalidadChech (chbCOOMBS);
	TIEMPODECOAGULACIONTPINRTPTTT = funcionalidadChech (chbTIEMPODECOAGULACIONTPINRTPTTT);
	GRUPOSANGUÍNEOYRH = funcionalidadChech (chbGRUPOSANGUÍNEOYRH);
	QUÍMICASANGUINEACOMPLETA = funcionalidadChech (chbQUÍMICASANGUINEACOMPLETA);
	QUÍMICASANGUINEA = funcionalidadChech (chbQUÍMICASANGUINEA);
	GLUCOSASERICA = funcionalidadChech (chbGLUCOSASERICA);
	ACURICO = funcionalidadChech (chbACURICO);
	COLESTEROL = funcionalidadChech (chbCOLESTEROL);
	TRIGLICERIDOS = funcionalidadChech (chbTRIGLICERIDOS);
	PERFILDELÍPIDOS = funcionalidadChech (chbPERFILDELÍPIDOS);
	PRUEBASDEFUNCIONHEPÁTICA = funcionalidadChech (chbPRUEBASDEFUNCIONHEPÁTICA);
	ELECTROLITOSSERICOS = funcionalidadChech (chbELECTROLITOSSERICOS);
	ECARDIACAS = funcionalidadChech (chbECARDIACAS);
	OTRASENZIMASTROPONINAMIOGLOBINA = funcionalidadChech (chbOTRASENZIMASTROPONINAMIOGLOBINA);
	GENERALDEORINA = funcionalidadChech (chbGENERALDEORINA);
	DEPURACIÓNDECREATININAORINA24HRS = funcionalidadChech (chbDEPURACIÓNDECREATININAORINA24HRS);
	PERFILTIROIDE = funcionalidadChech (chbPERFILTIROIDE);
	PAPANICOLAOU = funcionalidadChech (chbPAPANICOLAOU);
	GONADROTOFINACORIONICAHUM = funcionalidadChech (chbGONADROTOFINACORIONICAHUM);
	PERFILHORMONAL = funcionalidadChech (chbPERFILHORMONAL);
	ANTIGENOESPECIFICODEPRÓSTATA = funcionalidadChech (chbANTIGENOESPECIFICODEPRÓSTATA);
	PROTCREACTIVA = funcionalidadChech (chbPROTCREACTIVA);
	FACTORREUMATOIDE = funcionalidadChech (chbFACTORREUMATOIDE);
	ANTIESTRESTOLISINAS = funcionalidadChech (chbANTIESTRESTOLISINAS);
	REACIONESFEBRILES = funcionalidadChech (chbREACIONESFEBRILES);
	VIHELISAWesternBlot = funcionalidadChech (chbVIHELISAWesternBlot);
	UROCULTIVO = funcionalidadChech (chbUROCULTIVO);
	COPROCULTIVO = funcionalidadChech (chbCOPROCULTIVO);
	COPRLÓGICO = funcionalidadChech (chbCOPRLÓGICO);
	COPROPARASITOSCÓPICO = funcionalidadChech (chbCOPROPARASITOSCÓPICO);
	CULTIVOFARINGEO = funcionalidadChech (chbCULTIVOFARINGEO);
        String id_lab = aux.generaID();
        String OTROEXAMEN = otroExamen.getText();
        String OTROCULTIVO = otroCultivo.getText();
        String id_paciente = paci.getId_paciente();
        Date fecha_lab = Date.valueOf(LocalDate.now());
        listaLaboDia.clear();
        listaLaboDia.add(new Laboratorial(id_lab, BIOMETRÍAHEMÁTICACOMPLETA, FORMULAROJA, COOMBS, 
                TIEMPODECOAGULACIONTPINRTPTTT, GRUPOSANGUÍNEOYRH, QUÍMICASANGUINEACOMPLETA, 
                QUÍMICASANGUINEA, GLUCOSASERICA, ACURICO, COLESTEROL, TRIGLICERIDOS, PERFILDELÍPIDOS, 
                PRUEBASDEFUNCIONHEPÁTICA, ELECTROLITOSSERICOS, ECARDIACAS, OTRASENZIMASTROPONINAMIOGLOBINA, 
                GENERALDEORINA, DEPURACIÓNDECREATININAORINA24HRS, PERFILTIROIDE, PAPANICOLAOU, 
                GONADROTOFINACORIONICAHUM, PERFILHORMONAL, ANTIGENOESPECIFICODEPRÓSTATA, PROTCREACTIVA, 
                FACTORREUMATOIDE, ANTIESTRESTOLISINAS, REACIONESFEBRILES, VIHELISAWesternBlot, UROCULTIVO, 
                COPROCULTIVO, COPRLÓGICO, COPROPARASITOSCÓPICO, CULTIVOFARINGEO, OTROEXAMEN, 
                OTROCULTIVO, fecha_lab, id_paciente));
        
        OrdenLaboraPDF orPDF = new OrdenLaboraPDF();
        orPDF.pasoPrincipal(listaLaboDia);
    }
    
    /**
     * verifica que los campos esten llenos
     * @return 
     */
    private boolean continuaSINO(){
        String errorMessage = "";
        //errorMessage += aux.verificaTexField(txtPeso, "Peso");
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //formato botton guardar
        formatoBotones();
        //Le da formato a los imagenes
        formatoImagen();
        //formato de los texbox
        formatoDeText();
    } 
}
