/*
 * Este programa es propiedad de Alejandro Ortiz Corro 
 * y su uso está regulado por contrato de licencia para el usuario final(“CLUF”)
 * y las leyes de derecho de autor de las autoridades mexicanas. 
 * Usted queda obligado a las condiciones establecidas por el “CLUF”  al instalar, copiar o utilizar el software.
 * Si no acepta los términos de este del “CLUF”, no instale, copie ni use el software; 
 */
package com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.NoPatologicos;

import com.aohys.copiaIMSS.BaseDatos.Hikari;
import com.aohys.copiaIMSS.MVC.Modelo.ModeloAntecedentes.antNoPato;
import com.aohys.copiaIMSS.MVC.Modelo.Paciente;
import com.aohys.copiaIMSS.MVC.VistaControlador.Paciente.Antecedentes.PersonalesNoPatologicosController;
import com.aohys.copiaIMSS.MVC.VistaControlador.Principal.PrincipalController;
import com.aohys.copiaIMSS.Utilidades.ClasesAuxiliares.Auxiliar;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author CorrO
 */
public class PersonalesController implements Initializable {

    
    //Variables de escena
    private PersonalesNoPatologicosController cordi;
    Paciente paci;
    
    
    /**
     * Inicia la esecena 
     * @param cordi 
     */
    public void transmisor(PersonalesNoPatologicosController cordi) {
        this.cordi = cordi;
        this.paci = PrincipalController.pacienteAUsar;
        // carga los componentes top
        cargaTop();
        try(Connection conex = dbConn.conectarBD()) {
            antNoPatoCargado = antNoPato.cargaSoloUno(paci.getId_paciente(), conex);
            cargaAntNoPato(antNoPatoCargado);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    //Variables a que utiliza el controlador
    Auxiliar aux = new Auxiliar();
    antNoPato antNoPato = new antNoPato();
    antNoPato antNoPatoCargado;
    //Conexion
    Hikari dbConn = new Hikari();
    
    //FXML de arriba
    @FXML private Label lbNombre;
    @FXML private Label lbFechaNacimiento;
    //FXML ComboBox
    @FXML private ComboBox<String> cbbReligion;
    @FXML private ComboBox<String> cbbLugarNaci;
    @FXML private ComboBox<String> cbbEstadoCivil;
    @FXML private ComboBox<String> cbbEsColaridad;
    @FXML private ComboBox<String> cbbHigienePersonal;
    @FXML private ComboBox<String> cbbTipoActividad;
    @FXML private ComboBox<String> cbbVecesAl;
    @FXML private ComboBox<String> cbbPreferenciaSex;
    @FXML private ComboBox<String> cbbSangre;
    @FXML private ComboBox<String> cbbAlimentacion;
    //RadioButos
    @FXML private RadioButton rbCompleta;
    @FXML private RadioButton rbIncompleta;
    //Actividad si no
    @FXML private RadioButton rbSIActividad;
    @FXML private RadioButton rbNoActividad;
    //Texfields
    @FXML private TextField txtFrecuencia;
    @FXML private TextField txtParejas;
    //Button Aceptar
    @FXML private Button bttAceptar;
   
    
    
    //Lista para combobox
    ObservableList<String> listaReligion = FXCollections.observableArrayList();
    ObservableList<String> listaEstados = FXCollections.observableArrayList();
    ObservableList<String> listaEstadoCivil = FXCollections.observableArrayList();
    ObservableList<String> listaEsColaridad = FXCollections.observableArrayList();
    ObservableList<String> listaHigiene = FXCollections.observableArrayList();
    ObservableList<String> listaActividad = FXCollections.observableArrayList();
    ObservableList<String> listaVecesAL = FXCollections.observableArrayList();
    ObservableList<String> listaPreferencia = FXCollections.observableArrayList();
    ObservableList<String> listaSangre = FXCollections.observableArrayList();
    ObservableList<String> listaAlimentacion = FXCollections.observableArrayList();
    
    //Varaible de chechkBox
    private boolean actualizaSiNoMasAnte = false;
    private boolean actividadSiNo = false;
    private boolean escolaridadCompletaSiNo = true;
    Image guardar = new Image("com/aohys/copiaIMSS/Utilidades/Logos/computing-cloud.png");
    /**
     * Carga componentes
     */
    public void cargaTop(){
        String nombre = paci.getNombre_paciente()+" "+paci.getApellido_paciente()+" "+paci.getApMaterno_paciente();
        lbNombre.setText(nombre);
        lbFechaNacimiento.setText(paci.getFechaNacimiento_paciente().toString());
    }    
    
    /**
     * formato al comboxbox de religion
     */
    public void formatoReligion(){
        listaReligion.addAll("Católica","Protestante","Evangélica","Históricas","Pentecostales",
                            "Neopentecostales","Iglesia del dios vivo","Columna de apoyo de la verdad",
                            "La luz del mundo","Otras evangélicas","Bíblicas no evangélicas",
                            "Adventista del séptimo día","Mormones","Testigos de jehová","Judaica",
                            "Ninguna", "Otra");
        cbbReligion.setItems(listaReligion);
    }
    
    /**
     * formato de estados
     */
    private void formatoEstados(){
        listaEstados.addAll("Aguascalientes","Baja California","Baja California Sur","Campeche",
                            "Chiapas","Chihuahua","Coahuila","Colima","Distrito Federal","Durango",
                            "Estado de México","Guanajuato","Guerrero","Hidalgo","Jalisco","Morelos",
                            "Nayarit","Nuevo León","Oaxaca","Puebla","Querétaro","Quintana Roo",
                            "San Luis Potosí","Sinaloa","Sonora","Tabasco","Tamaulipas","Tlaxcala",
                            "Veracruz","Yucatán","Zacatecas");
        cbbLugarNaci.setItems(listaEstados);
    }
    
    /**
     * formato estado civil
     */
    private void formatoEstadoCivil(){
        listaEstadoCivil.addAll("SOLTERO(A)","CASADO(A)","DIVORCIADO(A)","VIUDO(A)",
                            "UNIÓN LIBRE","MADRE(PADRE) SOLTERA(O)","CONCUBINO(A)",
                            "SEPARADO(A)","NO APLICA");
        cbbEstadoCivil.setItems(listaEstadoCivil);
    }
    
    /**
     * formato de escolaridad
     */
    private void formatoEscolaridad(){
        listaEsColaridad.addAll("Analfabeta",
                                "Sabe leer y escribir",
                                "Preescolar",
                                "Primaria",
                                "Secundaria",
                                "Bachillerato",
                                "Técnico",
                                "Licenciatura",
                                "Maestría",
                                "Doctorado",
                                "No aplica",
                                "Ninguna",
                                "Otra");
        cbbEsColaridad.setItems(listaEsColaridad);
    }
    
    /**
     * da formato al combo de higiene
     */
    private void formatoHigiene(){
        listaHigiene.addAll("Buena",
                            "Deficiente",
                            "Mala");
        cbbHigienePersonal.setItems(listaHigiene);
    }
    
    /**
     * da formato al combo de tipo de actividad
     */
    private void formatoTipoActividad(){
        listaActividad.addAll(  "Caminata",
                                "Carrera",
                                "Gimnasia",
                                "Aerobics",
                                "Baile",
                                "Natación",
                                "Fútbol",
                                "Voleibol",
                                "Basquetbol",
                                "Ciclismo",
                                "Otros");
        cbbTipoActividad.setItems(listaActividad);
    }
    
    /**
     * formato de veces al dia de actividad 
     */
    private void formatoVecesAl(){
        listaVecesAL.addAll("Hora(s)",
                            "Día(s)",
                            "Semana(s)",
                            "Mes(es)",
                            "Año(s)");
        cbbVecesAl.setItems(listaVecesAL);
    }
    
    /**
     * le da formato de preferencia sexual
     */
    private void  formatoPreferenciaSexual(){
        listaPreferencia.addAll("Heterosexual",
                                "Homosexual",
                                "Bisexual",
                                "No aplica");
        cbbPreferenciaSex.setItems(listaPreferencia);
    }
    
    /**
     * formato de sangre
     */
    private void  formatoSangre(){
        listaSangre.addAll("A Positivo",
                            "A Negativo",
                            "B Positivo",
                            "B Negativo",
                            "AB Positivo",
                            "AB Negativo",
                            "O Positivo",
                            "O Negativo",
                            "No lo sabe");
        cbbSangre.setItems(listaSangre);
    }
    
    private void formatoCalidadAli(){
        listaAlimentacion.addAll("Suficiente",
                        "Insuficiente",
                        "Balanceada",
                        "No balanceada");
        cbbAlimentacion.setItems(listaAlimentacion);
    }
    
    /**
     * le da formato a los comboboxes
     */
    private void formatoCombobox(){
        formatoReligion();
        formatoEstados();
        formatoEstadoCivil();
        formatoEscolaridad();
        formatoHigiene();
        formatoTipoActividad();
        formatoVecesAl();
        formatoPreferenciaSexual();
        formatoSangre();
        formatoCalidadAli();
    }
    
    /**
     * formato radio buttons
     */
    public void radioFormato(){
        ToggleGroup escolaridad = new ToggleGroup();
        escolaridad.getToggles().addAll(rbCompleta,rbIncompleta);
        rbCompleta.fire();
        escolaridad.selectedToggleProperty().addListener((observable,viejo,nuevo)->{
            if (rbCompleta.isSelected()) {
                escolaridadCompletaSiNo = true;
            }else
                escolaridadCompletaSiNo = false;
        });
        
        ToggleGroup actividadFisi = new ToggleGroup();
        actividadFisi.getToggles().addAll(rbSIActividad,rbNoActividad);
        rbNoActividad.fire();
        desArmaBotones();
        actividadFisi.selectedToggleProperty().addListener((observable,viejo,nuevo)->{
            if (rbSIActividad.isSelected()) {
                actividadSiNo = true;
                armaBotones();
            }else{
                desArmaBotones();
                actividadSiNo = false;
            }
        });
    }
    
    /**
     * desarma los botones de actividad fisica
     */
    public void desArmaBotones(){
        txtFrecuencia.setText("");
        cbbTipoActividad.setValue(null);
        cbbVecesAl.setValue(null);
        
        txtFrecuencia.setDisable(true);
        cbbTipoActividad.setDisable(true);
        cbbVecesAl.setDisable(true);
    }
    
    /**
     * arma los botones
     */
    public void armaBotones(){
        txtFrecuencia.setDisable(false);
        cbbTipoActividad.setDisable(false);
        cbbVecesAl.setDisable(false);
    }
    
    /**
     * le da formato al boton 
     */
    private void formatoButton(){
        bttAceptar.setOnAction(evento->{
            if (continuaSINO()) {
                try(Connection conex = dbConn.conectarBD()) {
                    if (actualizaSiNoMasAnte) {
                        actualizaAntNoPato(conex);
                        PersonalesNoPatologicosController.cordi.lanzaHistoriaMedica(paci);
                    }else{
                        guardaAntNoPato(conex);
                        PersonalesNoPatologicosController.cordi.lanzaHistoriaMedica(paci);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        bttAceptar.setGraphic(new ImageView(guardar));
    }
    
    /**
     * guarda antecedentes no patologicos
     * @param conex 
     */
    private void guardaAntNoPato(Connection conex){
        String id_antNP = aux.generaID();
        String religion_antNP = cbbReligion.getValue();
        String lugarNaci_antNP = cbbLugarNaci.getValue();
        String estaCivil_antNP = cbbEstadoCivil.getValue();
        String escolaridad_antNP = cbbEsColaridad.getValue();
        String higiene_antNP = cbbHigienePersonal.getValue();
        
        String actividadFisica_antNP;
        int frecuencia_antNP;
        String frecVeces_antNP;
        
        if (actividadSiNo) {
            actividadFisica_antNP = cbbTipoActividad.getValue();
            frecuencia_antNP = Integer.valueOf(txtFrecuencia.getText());
            frecVeces_antNP = cbbVecesAl.getValue();
        }else{
            actividadFisica_antNP = "";
            frecuencia_antNP = 0;
            frecVeces_antNP = "";
        }
        
        String sexualidad_antNP = cbbPreferenciaSex.getValue();
        int numParejas_antNP = Integer.valueOf(txtParejas.getText());;
        String sangre_antNP = cbbSangre.getValue();
        String alimentacion_antNP = cbbAlimentacion.getValue();
        String id_paciente = paci.getId_paciente();
        boolean escoCompInco_antNP = escolaridadCompletaSiNo;
        
        antNoPato.agregaAntNoPato(id_antNP, religion_antNP, lugarNaci_antNP, estaCivil_antNP, 
                escolaridad_antNP, higiene_antNP, actividadFisica_antNP, frecuencia_antNP, 
                sexualidad_antNP, numParejas_antNP, sangre_antNP, alimentacion_antNP, id_paciente, 
                escoCompInco_antNP, frecVeces_antNP, conex);
    }
    
    /**
     *carga los componentes si existen con anterioridad 
     * @param pato 
     */
    public void cargaAntNoPato(antNoPato pato){
        if (pato!= null) {
            cbbReligion.setValue(pato.getReligion_antNP());
            cbbLugarNaci.setValue(pato.getLugarNaci_antNP());
            cbbEstadoCivil.setValue(pato.getEstaCivil_antNP());
            cbbEsColaridad.setValue(pato.getEscolaridad_antNP());
            cbbHigienePersonal.setValue(pato.getHigiene_antNP());
            if (pato.getFrecuencia_antNP() == 0) {
                cbbTipoActividad.setValue(null);
                txtFrecuencia.setText("");
                cbbVecesAl.setValue(null);
            }else{
                rbSIActividad.fire();
                cbbTipoActividad.setValue(pato.getActividadFisica_antNP());
                txtFrecuencia.setText(Integer.toString(pato.getFrecuencia_antNP()));
                cbbVecesAl.setValue(pato.getFrecVeces_antNP());
            }
            cbbPreferenciaSex.setValue(pato.getSexualidad_antNP());
            txtParejas.setText(Integer.toString(pato.getNumParejas_antNP()));
            cbbSangre.setValue(pato.getSangre_antNP());
            cbbAlimentacion.setValue(pato.getAlimentacion_antNP());
            if (pato.getEscoCompInco_antNP()) {
                rbCompleta.fire();
            }else
                rbIncompleta.fire();
            actualizaSiNoMasAnte = true;
        }
        
    }
    
    /**
     * actualiza ante no patologicos del paciente
     * @param conex 
     */
    private void actualizaAntNoPato(Connection conex){
        String id_antNP = antNoPatoCargado.getId_antNP();
        String religion_antNP = cbbReligion.getValue();
        String lugarNaci_antNP = cbbLugarNaci.getValue();
        String estaCivil_antNP = cbbEstadoCivil.getValue();
        String escolaridad_antNP = cbbEsColaridad.getValue();
        String higiene_antNP = cbbHigienePersonal.getValue();
        
        String actividadFisica_antNP;
        int frecuencia_antNP;
        String frecVeces_antNP;
        
        if (actividadSiNo) {
            actividadFisica_antNP = cbbTipoActividad.getValue();
            frecuencia_antNP = Integer.valueOf(txtFrecuencia.getText());
            frecVeces_antNP = cbbVecesAl.getValue();
        }else{
            actividadFisica_antNP = "";
            frecuencia_antNP = 0;
            frecVeces_antNP = "";
        }
        
        String sexualidad_antNP = cbbPreferenciaSex.getValue();
        int numParejas_antNP = Integer.valueOf(txtParejas.getText());;
        String sangre_antNP = cbbSangre.getValue();
        String alimentacion_antNP = cbbAlimentacion.getValue();
        String id_paciente = paci.getId_paciente();
        boolean escoCompInco_antNP = escolaridadCompletaSiNo;
        
        antNoPato.actualizaAntNoPato(id_antNP, religion_antNP, lugarNaci_antNP, estaCivil_antNP, 
                escolaridad_antNP, higiene_antNP, actividadFisica_antNP, frecuencia_antNP, 
                sexualidad_antNP, numParejas_antNP, sangre_antNP, alimentacion_antNP, id_paciente, 
                escoCompInco_antNP, frecVeces_antNP, conex);
    }
    
    /**
     * le da formato a los texfields
     */
    private void formatoTexfields(){
        txtFrecuencia.setTextFormatter  (new TextFormatter (aux.formato (3, 3)));
        txtParejas.setTextFormatter     (new TextFormatter (aux.formato (3, 3)));
        //Toltips
        aux.toolTipSuperior(txtFrecuencia, "campo numérico");
        aux.toolTipSuperior(txtParejas, "campo numérico");
    }
    
    public boolean continuaSINO(){
        String errorMessage = "";
        errorMessage += aux.verificaValufield(cbbReligion, "Religión");
        errorMessage += aux.verificaValufield(cbbLugarNaci, "Lugar de nacimiento");
        errorMessage += aux.verificaValufield(cbbEstadoCivil, "Estado Civil");
        errorMessage += aux.verificaValufield(cbbEsColaridad, "Escolaridad");
        errorMessage += aux.verificaValufield(cbbHigienePersonal, "Higiene personal");
        if (rbSIActividad.isSelected()) {
            errorMessage += aux.verificaValufield(cbbTipoActividad, "Tipo de actividad");
            errorMessage += aux.verificaTexField(txtFrecuencia, "Frecuencia");
            errorMessage += aux.verificaValufield(cbbVecesAl, "Nivel de frecuencia");
        }
        errorMessage += aux.verificaValufield(cbbPreferenciaSex, "Preferencia sexual");
        errorMessage += aux.verificaTexField(txtParejas, "Numero de parejas");
        errorMessage += aux.verificaValufield(cbbSangre, "Grupo sanguíneo");
        errorMessage += aux.verificaValufield(cbbAlimentacion, "Calidad de la alimentación");
        
        if (errorMessage.length() == 0 ) {
            return true;
        } else {
            aux.alertaError("Campos  Vacíos", "Agregue los siguientes campos:", 
                    errorMessage);
            return false;
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Formato de los comboboxes
        formatoCombobox();
        //formato de radio butons
        radioFormato();
        //formato del button
        formatoButton();
        //formato texfields
        formatoTexfields();
    }    

   
    
}
