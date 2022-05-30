package ru.mirea.Controllers;

import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import ru.mirea.Start;
import ru.mirea.data.User;

import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class ModelController{

    @FXML
    public Pane ernull;

    @FXML
    public Pane erpat;

    @FXML
    public Pane gen;

    @FXML
    protected Pane net;

    @FXML
    public Pane net_reg;

    @FXML
    public Pane net_rep;

    @FXML
    protected Label time_pat;

    @FXML
    protected Label time_null;

    @FXML
    protected Label time_gen;

    @FXML
    protected Label time_inet;

    @FXML
    public Label time_reg;

    @FXML
    public Label time_rep;

    @FXML
    protected Label sgerpar;

    @FXML
    protected RadioButton ra1;

    @FXML
    protected RadioButton ra2;

    @FXML
    protected RadioButton ra3;

    @FXML
    protected VBox Notif;

    protected int ico = 0;

    protected String per_reg;

    protected String per_rep;

    protected final Interpolator inter = Interpolator.EASE_BOTH;

    protected final ArrayList<String> list_nonlat = new ArrayList<>();

    protected final ArrayList<String> list_null = new ArrayList<>();

    public static Boolean inet = false;

    public void init()
    {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), this::ping));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void ping(ActionEvent actionEvent)
    {
        if(ModelController.inet != pingh("https://yandex.ru"))
        {
            ModelController.inet = !ModelController.inet;
            if(ModelController.inet) Start.agree_with_bd();
        }
        if(ModelController.inet)
            neWarn(net);
        else {
            time_inet.setText(new SimpleDateFormat("HH:mm").format(new Date()));
            onWarn(net);
        }
    }

    public void rad1()
    {
        ico = 0;
        ra1.setSelected(true);
    }

    public void rad2()
    {
        ico = 1;
        ra2.setSelected(true);
    }

    public void rad3()
    {
        ico = 2;
        ra3.setSelected(true);
    }

    public String getPer_reg() {
        return per_reg;
    }

    public void setPer_reg(String per_reg) {
        this.per_reg = per_reg;
    }

    public String getPer_rep() {
        return per_rep;
    }

    public void setPer_rep(String per_rep) {
        this.per_rep = per_rep;
    }

    public void onNREG() throws IOException {
        if(this.getClass().getSimpleName().equals("StartController"))
        {
            StartController startController = (StartController) this;
            User user = startController.usersImpl.getuser(per_reg);
            startController.toreg(user);
            startController.down();
        } else {
            ProjController projController = (ProjController) this;
            projController.toBegin();
            StartController startController = Start.loader.getController();
            startController.setPer_rep(per_rep);
            startController.setPer_reg(per_reg);
            startController.neWarn(startController.ernull);
            startController.neWarn(startController.erpat);
            startController.neWarn(startController.gen);
            User user = startController.usersImpl.getuser(per_reg);
            System.out.println(user);
            startController.time_reg.setText(new SimpleDateFormat("HH:mm").format(new Date()));
            startController.onWarn(startController.net_reg);
            if(this.net_rep.isVisible()) startController.onWarn(startController.net_rep);
            startController.toreg(user);
            startController.down();
        }
    }

    public void onNREP() throws IOException {
        Start.usename = per_rep;
        if(this.getClass().getSimpleName().equals("StartController"))
        {
            Start.close_start();
            Start.start_scene("/fxml/project.fxml");
            ProjController projController = Start.loader.getController();
            projController.setPer_rep(per_rep);
            projController.setPer_reg(per_reg);
            projController.neWarn(projController.ernull);
            projController.neWarn(projController.erpat);
            projController.neWarn(projController.gen);
            User user = projController.usersImpl.getuser(Start.usename);
            projController.init();
            projController.time_rep.setText(new SimpleDateFormat("HH:mm").format(new Date()));
            projController.onWarn(projController.net_rep);
            if(this.net_reg.isVisible()) projController.onWarn(projController.net_reg);
            projController.toEdit(user);
        } else {
            ProjController projController = (ProjController) this;
            User user = projController.usersImpl.getuser(Start.usename);
            projController.toEdit(user);
        }
    }

    public void neNREG()
    {
        neWarn(net_reg);
        Start.off_reg.remove(per_reg);
    }

    public void neNREP()
    {
        neWarn(net_rep);
    }

    public void textch(KeyEvent keyEvent)
    {
        TextField text = (TextField) keyEvent.getSource();
        if (text.getText().isEmpty())
        {
            if (!list_null.contains(text.getId())) add_null(text.getId());
            if (list_nonlat.contains(text.getId())) rem_nonlat(text.getId());
        } else {
            rem_null(text.getId());
            if (Pattern.matches("[A-Za-z0-9]+", text.getText()))
                rem_nonlat(text.getId());
            else {
                if (!list_nonlat.contains(text.getId())) add_nonlat(text.getId());
            }
        }
    }

    public boolean pingh(String host) {
        try {
            URL siteURL = new URL(host);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1000);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getstat(TextField text)
    {
        if(!list_null.contains(text.getId()) && !list_nonlat.contains(text.getId()))
        {
            if(text.getText().isEmpty()) {
                add_null(text.getId());
                return false;
            }
            return true;
        }
        return false;
    }

    private void add_null(String id)
    {
        list_null.add(id);
        redfield((TextField) Start.roots.get(id), 1);
        onWarn(ernull);
        time_null.setText(new SimpleDateFormat("HH:mm").format(new Date()));
    }

    private void rem_null(String id)
    {
        list_null.remove(id);
        if(list_null.isEmpty()) {
            neWarn(ernull);
            if(!list_nonlat.contains(id)) redfield((TextField) Start.roots.get(id), 0);
        }
    }

    private void add_nonlat(String id)
    {
        list_nonlat.add(id);
        redfield((TextField) Start.roots.get(id), 1);
        onWarn(erpat);
        time_pat.setText(new SimpleDateFormat("HH:mm").format(new Date()));
    }

    private void rem_nonlat(String id)
    {
        list_nonlat.remove(id);
        if(list_nonlat.isEmpty()) {
            neWarn(erpat);
            if (!list_null.contains(id)) redfield((TextField) Start.roots.get(id), 0);
        }
    }

    private void redfield(TextField text, double opacity)
    {
        InnerShadow innerShadow = (InnerShadow) text.getEffect();
        DropShadow dropShadow = (DropShadow) innerShadow.getInput();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(innerShadow.colorProperty(), Color.web("#ff0000",opacity), inter));
        kv.add(new KeyValue(dropShadow.colorProperty(), Color.web("#ff0000",opacity), inter));
        played(kv, 100);
    }

    public void onWarn(Pane pane)
    {
        if(!pane.isVisible()) pane.setVisible(true);
        if(pane.getParent() != Notif){
            if(pane.getParent() != null) ((Pane)pane.getParent()).getChildren().remove(pane);
            pane.setTranslateY(-175);
            pane.setOpacity(0);
            Notif.getChildren().add(pane);
            List<KeyValue> kv = new ArrayList<>();
            kv.add(new KeyValue(pane.translateYProperty(), 0, inter));
            kv.add(new KeyValue(pane.opacityProperty(), 1, inter));
            played(kv, 500);
        }
    }

    public void neWarn(Pane pane)
    {
        if(pane.isVisible()) {
            pane.visibleProperty().addListener(this::changed);
            List<KeyValue> kv = new ArrayList<>();
            kv.add(new KeyValue(pane.translateYProperty(), -175, inter));
            kv.add(new KeyValue(pane.opacityProperty(), 0, inter));
            kv.add(new KeyValue(pane.visibleProperty(), false, inter));
            played(kv, 500);
        }
    }

    private void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
        if(!new_val) {
            Pane pane = (Pane)((BooleanProperty) ov).getBean();
            Notif.getChildren().remove(pane);
            pane.visibleProperty().removeListener(this::changed);
        }
    }

    public void ranpar(PasswordField[] passFields)
    {
        StringBuilder password = new StringBuilder();
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 15; i++){
            password.append(symbols.charAt((int) Math.floor(Math.random() * symbols.length())));
        }
        for(PasswordField text : passFields){
            text.setText(password.toString());
        }
        sgerpar.setText(password.toString());
        onWarn(gen);
        time_gen.setText(new SimpleDateFormat("HH:mm").format(new Date()));
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(password.toString());
        clipboard.setContent(content);
    }

    public void onBut(MouseEvent mouseEvent)
    {
        Button but = (Button) mouseEvent.getSource();
        InnerShadow innerShadow = (InnerShadow) but.getEffect();
        Glow glow = (Glow) innerShadow.getInput();
        Label label = (Label) but.getChildrenUnmodifiable().get(0);
        DropShadow dropShadow = (DropShadow) label.getEffect();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(innerShadow.offsetYProperty(), 0, inter));
        kv.add(new KeyValue(glow.levelProperty(), 0.3, inter));
        kv.add(new KeyValue(dropShadow.colorProperty(), Color.web("#000",0.5), inter));
        played(kv, 100);
    }

    public void neBut(MouseEvent mouseEvent)
    {
        Button but = (Button) mouseEvent.getSource();
        InnerShadow innerShadow = (InnerShadow) but.getEffect();
        Glow glow = (Glow) innerShadow.getInput();
        Label label = (Label) but.getChildrenUnmodifiable().get(0);
        DropShadow dropShadow = (DropShadow) label.getEffect();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(innerShadow.offsetYProperty(), -5, inter));
        kv.add(new KeyValue(glow.levelProperty(), 0, inter));
        kv.add(new KeyValue(dropShadow.colorProperty(), Color.web("#000",1), inter));
        played(kv, 100);
    }

    public void onPRB(MouseEvent mouseEvent)
    {
        Button but = (Button) mouseEvent.getSource();
        InnerShadow innerShadow = (InnerShadow) but.getEffect();
        Glow glow = (Glow) innerShadow.getInput();
        Label label = (Label) but.getChildrenUnmodifiable().get(0);
        DropShadow dropShadow = (DropShadow) label.getEffect();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(innerShadow.offsetYProperty(), 0, inter));
        kv.add(new KeyValue(innerShadow.widthProperty(), 60, inter));
        kv.add(new KeyValue(innerShadow.heightProperty(), 60, inter));
        kv.add(new KeyValue(innerShadow.colorProperty(), Color.web("#000",1), inter));
        kv.add(new KeyValue(glow.levelProperty(), 0, inter));
        kv.add(new KeyValue(dropShadow.colorProperty(), Color.web("#000",0.5), inter));
        played(kv, 100);
    }

    public void nePRB(MouseEvent mouseEvent)
    {
        Button but = (Button) mouseEvent.getSource();
        InnerShadow innerShadow = (InnerShadow) but.getEffect();
        Glow glow = (Glow) innerShadow.getInput();
        Label label = (Label) but.getChildrenUnmodifiable().get(0);
        DropShadow dropShadow = (DropShadow) label.getEffect();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(innerShadow.offsetYProperty(), -5, inter));
        kv.add(new KeyValue(innerShadow.widthProperty(), 0, inter));
        kv.add(new KeyValue(innerShadow.heightProperty(), 1, inter));
        kv.add(new KeyValue(innerShadow.colorProperty(), Color.web("#fff",0.25), inter));
        kv.add(new KeyValue(glow.levelProperty(), 0, inter));
        kv.add(new KeyValue(dropShadow.colorProperty(), Color.web("#000",1), inter));
        played(kv, 100);
    }

    public void onLink(MouseEvent mouseEvent)
    {
        Hyperlink label = (Hyperlink) mouseEvent.getSource();
        Glow glow = (Glow) label.getEffect();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(glow.levelProperty(), 1.0, inter));
        played(kv, 100);
    }

    public void neLink(MouseEvent mouseEvent)
    {
        Hyperlink label = (Hyperlink) mouseEvent.getSource();
        Glow glow = (Glow) label.getEffect();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(glow.levelProperty(), 0, inter));
        played(kv, 100);
    }

    public void onPL(MouseEvent mouseEvent)
    {
        Hyperlink label = (Hyperlink) mouseEvent.getSource();
        Glow glow = (Glow) label.getEffect();
        InnerShadow innerShadow = (InnerShadow) ((DropShadow)glow.getInput()).getInput();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(glow.levelProperty(), 0, inter));
        kv.add(new KeyValue(innerShadow.chokeProperty(), 0.3, inter));
        played(kv, 100);
    }

    public void nePL(MouseEvent mouseEvent)
    {
        Hyperlink label = (Hyperlink) mouseEvent.getSource();
        InnerShadow innerShadow = (InnerShadow) ((DropShadow)((Glow)label.getEffect()).getInput()).getInput();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(innerShadow.chokeProperty(), 0, inter));
        played(kv, 100);
    }

    public void handle1(ActionEvent event)
    {
        Timeline time1 = (Timeline) event.getSource();
        time1.stop();
        time1.getKeyFrames().remove(0, time1.getKeyFrames().size());
        time1 = null;
    }

    public void played(Collection<KeyValue> kv, double time)
    {
        Timeline time1 = new Timeline();
        for(KeyValue key : kv)
        {
            time1.getKeyFrames().add(new KeyFrame(Duration.millis(time), key));
        }
        time1.setOnFinished(this::handle1);
        time1.play();
    }

    public void browse_git() throws URISyntaxException, IOException {
        Desktop d = Desktop.getDesktop();
        d.browse(new URI("https://github.com/Ddudde/Kursach-Java"));
    }
}