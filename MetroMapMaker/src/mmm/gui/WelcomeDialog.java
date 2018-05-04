/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.gui;

import static djf.settings.AppStartupConstants.CLOSE_BUTTON_LABEL;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mmm.data.MetroMapData;
import mmm.file.MetroMapFiles;
import mmm.gui.MetroMapWorkspace;

/**
 *
 * @author Manny
 */
public class WelcomeDialog extends Stage {

    static WelcomeDialog singleton = null;

    // HERE ARE THE DIALOG COMPONENTS
    FlowPane rightPane;
    FlowPane rightPane1;
    FlowPane rightPane2;
    FlowPane recentWorks;
    Scene scene;
    Label messageLabel;
    Button closeButton;

    /**
     * Initializes this dialog so that it can be used repeatedly for all kinds
     * of messages. Note this is a singleton design pattern so the constructor
     * is private.
     *
     * @param owner The owner stage of this modal dialoge.
     *
     * @param closeButtonText Text to appear on the close button.
     */
    private WelcomeDialog() {
    }

    /**
     * A static accessor method for getting the singleton object.
     *
     * @return The one singleton dialog of this object type.
     */
    public static WelcomeDialog getWelcomeSingleton() {
        if (singleton == null) {
            singleton = new WelcomeDialog();
        }
        return singleton;
    }

    /**
     * This function fully initializes the singleton dialog for use.
     *
     * @param owner The window above which this dialog will be centered.
     */
    public void init(MetroMapWorkspace workspace, MetroMapData appComponent, MetroMapFiles fileComponent, BorderPane appPane) {

        BorderPane welcome = new BorderPane();
        rightPane = new FlowPane();
        recentWorks = new FlowPane();
        recentWorks.setOrientation(Orientation.VERTICAL);
        recentWorks.setVgap(15);
        recentWorks.setPadding(new Insets(15, 15, 15, 15));
        recentWorks.setPrefWidth(200);
        recentWorks.setStyle("-fx-background-color: white");
        rightPane1 = new FlowPane();
        rightPane2 = new FlowPane();
        rightPane.setOrientation(Orientation.VERTICAL);
        rightPane1.setOrientation(Orientation.VERTICAL);
        rightPane2.setOrientation(Orientation.VERTICAL);
        Text test = new Text("Metro Map Maker\n");
        Text test1 = new Text("                               Manpreet Singh");
        //Color.
        test.setFont(Font.font(null, FontWeight.BOLD, 48));
        test.setStyle("-fx-fill:  linear-gradient(white , #A9A9A9)");
        test1.setFont(Font.font(null, FontWeight.BOLD, 24));
        test1.setStyle("-fx-fill:  linear-gradient(white , #A9A9A9)");
        rightPane.setAlignment(Pos.CENTER_RIGHT);
        //rightPane1.setAlignment(Pos.CENTER_RIGHT);
        //rightPane2.setAlignment(Pos.CENTER_RIGHT);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(25);
        dropShadow.setSpread(0.25);
        dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
        test.setEffect(dropShadow);
        test1.setEffect(dropShadow);
        TextFlow test3 = new TextFlow(test, test1);
        ImageView image = new ImageView(new Image(FILE_PROTOCOL + "images/MetroLogo.png/"));
        
        rightPane.getChildren().add(test3);
        //rightPane1.getChildren().add(test3);
        rightPane.setPrefWidth(600);
        //rightPane1.setStyle("-fx-background-color: #ef0015");
        //#ff5858,#f09819
        rightPane.setStyle("-fx-background-color: linear-gradient(black,darkgray)");
        messageLabel = new Label("Recent Works");
        messageLabel.setFont(Font.font(18));
        recentWorks.getChildren().add(messageLabel);

        File directory = new File("work");

        File[] fileList = directory.listFiles();
        Arrays.sort(fileList, new Comparator<File>() {
            public int compare(File f1, File f2) {
                return -Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            }
        });
        int count = 0;
        if (fileList.length > 8)
            count = 8;
        else
            count = fileList.length;
                
        for (int i = 0; i < count; i++) {
            File f = fileList[i];
            Hyperlink h = new Hyperlink(f.getName());
            h.setFont(Font.font(18));
            recentWorks.getChildren().add(h);
            h.setOnMouseClicked(e -> {
                try {
                    fileComponent.loadData(appComponent, f.getAbsolutePath());
                    workspace.getApp().getGUI().getFileController().setCurrentWorkFile(f);
                    workspace.activateWorkspace(appPane);
                    WelcomeDialog.this.close();
                } catch (IOException ex) {
                    Logger.getLogger(WelcomeDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        Button openNew = new Button("Create New Map");
        openNew.setOnAction(e -> {
            try {
                fileComponent.newRequest(appComponent, workspace.app);
                this.close();
            } catch (IOException ex) {
                Logger.getLogger(WelcomeDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        rightPane.setVgap(100);
        //rightPane.getChildren().addAll(rightPane1,rightPane2);
        //rightPane.getChildren().add(openNew);
        welcome.setRight(rightPane);
        welcome.setLeft(recentWorks);
        // CLOSE BUTTON
        Text close = new Text(CLOSE_BUTTON_LABEL);
        close.setStyle("-fx-fill:  linear-gradient(white , #A9A9A9)");
        closeButton = new Button(close.getText());
       
        closeButton.setOnAction(e -> {
            WelcomeDialog.this.close();
        });
        
        TextFlow buttons = new TextFlow(new Text("\t\t\t\t\t\t\t\t\t "),openNew,new Text("\n\t\t\t\t\t\t\t\t\t\t\t\t  "), closeButton);
        rightPane.getChildren().add(buttons);
        rightPane.getChildren().add(image);
        
        closeButton.setStyle("-fx-background-color: transparent");
        openNew.setStyle("-fx-background-color: transparent");
        closeButton.setFont(Font.font(null, FontWeight.BOLD, 18));
        //closeButton.setStyle("-fx-fill:  linear-gradient(white , #A9A9A9)");
        openNew.setFont(Font.font(null,FontWeight.BOLD,18));
        scene = new Scene(welcome);
        this.initStyle(StageStyle.UNDECORATED);
        this.setScene(scene);
        showAndWait();
    }
}
