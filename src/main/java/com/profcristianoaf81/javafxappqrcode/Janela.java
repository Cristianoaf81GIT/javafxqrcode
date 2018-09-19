/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.profcristianoaf81.javafxappqrcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

/**
 *
 * @author cristianoaf81
 */
public final class Janela extends Application{
    private final  AnchorPane Painel;
    private final  TextArea AreaDeTexto;
    private final  Stage palco;
    private final  Scene cena;
    private final RadioButton RB_QRCODE,RB_COD128;
    private final ToggleGroup grupoCheck;
    private final Button BT_gerarCOD,BT_Limpar;
    private final int TAMANHO_QR_COD = 256;
    private FileOutputStream ArquivoSaida;
    private ByteArrayOutputStream saida;
    private BufferedImage entrada;
    private Image ImagemPNG;
    private File saidaCOD128;
    private final String QR_CAMINHO = "file:"
            +System.getProperty("user.home")
            +"/Desktop/QrCode.png";
    private final String COD128_CAMINHO = "file:"
            +System.getProperty("user.home")
            +"/Desktop/COD128.png";
    
    
    
    public Janela(){
    Painel = new AnchorPane();
    Painel.setPrefSize(400, 350);
    
    AreaDeTexto = new TextArea();
    AreaDeTexto.setPrefSize(220, 150);
    AreaDeTexto.setPromptText("Digite Algo Aqui");
    AreaDeTexto.setWrapText(true);
    AreaDeTexto.setLayoutY(30);
    grupoCheck = new ToggleGroup();
    RB_QRCODE = new RadioButton("Código QR");
    RB_QRCODE.setSelected(true);
    RB_QRCODE.setToggleGroup(grupoCheck);
    RB_COD128 = new RadioButton("Código 128");
    RB_COD128.setToggleGroup(grupoCheck);
    BT_gerarCOD = new Button("Gerar Código de Barras");
    BT_Limpar = new Button("Limpar");
    Painel.getChildren().addAll(
            AreaDeTexto,
            RB_QRCODE,
            RB_COD128,
            BT_gerarCOD,
            BT_Limpar);
    
    this.palco = new Stage();
    cena = new Scene(Painel);
    this.palco.setScene(this.cena);
    this.palco.setTitle("Código de Barras em JavaFX");
    this.palco.setResizable(false);
    DefinirEventos();
    
    
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    primaryStage = this.palco;
    primaryStage.show();
    AreaDeTexto.setLayoutX((Painel.getWidth()-AreaDeTexto.getWidth())/2);
    RB_QRCODE.setLayoutY(AreaDeTexto.getHeight()+45);
    RB_QRCODE.setLayoutX((Painel.getWidth())/4);
    RB_COD128.setLayoutY(AreaDeTexto.getHeight()+45);
    RB_COD128.setLayoutX(RB_QRCODE.getLayoutX()+110);
    BT_gerarCOD.setLayoutX((Painel.getWidth()-BT_gerarCOD.getWidth())/2);
    BT_gerarCOD.setLayoutY(RB_COD128.getLayoutY()+50);
    BT_Limpar.setLayoutX((Painel.getWidth()-BT_Limpar.getWidth())/2);
    BT_Limpar.setLayoutY(BT_gerarCOD.getLayoutY()+40);
    }
    
    public static void main(String[] args){
        launch(args);
    }
    
    
   public void DefinirEventos(){
       BT_gerarCOD.setOnAction((ev)->{
           if(RB_QRCODE.isSelected()){
               System.out.println("Código QR Selecionado");
               GerarCodQR();
           }
           if(RB_COD128.isSelected()==true){
               System.out.println("Código 128 selecionado");
               GerarCodigo128();
           }
       });
       BT_Limpar.setOnAction((ActionEvent evt) -> {
           if(!"".equals(AreaDeTexto.getText())){
             AreaDeTexto.setText("");
           }
       });
   }
   
   public void GerarCodQR(){
       if("".equals(AreaDeTexto.getText())){
           Alert aviso =  new Alert(Alert.AlertType.ERROR,
                   "", ButtonType.OK);
           aviso.setTitle("Alerta!");
           aviso.setHeaderText("Não foi possível gerar código QR");
           aviso.setContentText(
                   "O Campo de Texto precisa\n"
                   +"Estar preenchido com algum valor!"
           );
           aviso.showAndWait();
           AreaDeTexto.requestFocus();
       } else {
           System.out.println(AreaDeTexto.getText());
           
           try {
              //gera arquivo na area de trabalho do usuario 
              ArquivoSaida = new FileOutputStream(
                System.getProperty("user.home")+"/Desktop/QrCode.png"
              );
              
              //gera array de bytes para criacao de dados para o 
              //arquivo
              saida = QRCode.from(AreaDeTexto.getText())
                      .to(ImageType.PNG)//extenção de arquivo
                      .withSize(TAMANHO_QR_COD, TAMANHO_QR_COD)//tamanho
                      .withCharset("UTF-8")//codificação utf-8
                      .stream();//gera a saida
              
              ArquivoSaida.write(saida.toByteArray());//grava os dados no arq
              
              ArquivoSaida.close();//fecha o arquivo
              
               Alert avisoSucesso = new Alert(Alert.AlertType.INFORMATION
                       , "", ButtonType.OK);
               avisoSucesso.setTitle("Sucesso!");
               avisoSucesso.setHeaderText(
                       "Arquivo criado com sucesso em:\n"
               );
               avisoSucesso.setContentText(
                    String.valueOf(
                        System.getProperty("user.home")+"/Desktop/"
                               +"QrCode.png"
                ));
               avisoSucesso.showAndWait();
               
              //chamar método para abrir o arquivo recem criado
              AbrirCodigo(QR_CAMINHO,1,AreaDeTexto.getText());
              
           } catch (IOException e) {
               Alert erroArquivo = new Alert(Alert.AlertType.ERROR
                       , "", ButtonType.OK);
               erroArquivo.setTitle("Falha");
               erroArquivo.setHeaderText(
                       "Falha ao Criar Arquivo!\n"
                       +"Caso o problema persista\n"
                       +"entre em contato com administrador do sistema"
               );
               erroArquivo.setContentText(
                       "Causa do Erro : "+e.getMessage()
               );
           }
           
       }   
   }
   
   public void GerarCodigo128(){
        if("".equals(AreaDeTexto.getText())){
           Alert aviso =  new Alert(Alert.AlertType.ERROR,
                   "", ButtonType.OK);
           aviso.setTitle("Alerta!");
           aviso.setHeaderText("Não foi possível gerar código de Barras");
           aviso.setContentText(
                   "O Campo de Texto precisa\n"
                   +"Estar preenchido com algum valor!"
           );
           aviso.showAndWait();
           AreaDeTexto.requestFocus();
       }else{
            try {
                Barcode cod128 = BarcodeFactory
                .createCode128(
                   AreaDeTexto.getText()
                );
            saidaCOD128 = new File(System
                    .getProperty("user.home")
                    +"/Desktop/COD128.png"
            ); 
            BarcodeImageHandler.savePNG(cod128, saidaCOD128);
            Alert avisoSucesso = new Alert(Alert.AlertType.INFORMATION
                       , "", ButtonType.OK);
               avisoSucesso.setTitle("Sucesso!");
               avisoSucesso.setHeaderText(
                       "Arquivo criado com sucesso em:\n"
               );
               avisoSucesso.setContentText(
                    String.valueOf(
                        System.getProperty("user.home")+"/Desktop/"
                               +"COD128.png"
                ));
               avisoSucesso.showAndWait();
                AbrirCodigo(COD128_CAMINHO,2,AreaDeTexto.getText());
            } catch (BarcodeException ex) {
                Logger.getLogger(Janela.class.getName()).log(Level.SEVERE, null, ex);
            } catch(OutputException oe){
            
            }
        
       }
   }
   
   public void AbrirCodigo(String Caminho,int tipoCOD,String texto){
       try {
         ImagemPNG = new Image(Caminho);
         MostrarImg painel = new MostrarImg();
         painel.start(new Stage());
         painel.ExibirIMG(ImagemPNG, tipoCOD,texto);
       } catch (Exception e) {
           System.out.println(e.getMessage());
       }
   }
}
