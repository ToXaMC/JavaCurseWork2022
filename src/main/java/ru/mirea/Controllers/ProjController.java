package ru.mirea.Controllers;

import javafx.animation.KeyValue;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ru.mirea.MireaApplication;
import ru.mirea.Start;
import ru.mirea.data.User;
import ru.mirea.data.UsersImpl;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjController extends ModelController{

    private int id = 1;

    public UsersImpl usersImpl = (UsersImpl) MireaApplication.ctx.getBean("usersImpl");

    @FXML
    private Label usern;

    @FXML
    private ImageView icon;

    @FXML
    private Pane glavn;

    @FXML
    private Pane news;

    @FXML
    private Pane cont;

    @FXML
    private VBox menu;

    @FXML
    private Pane edit;

    @FXML
    private Pane caps;

    @FXML
    private Pane p_edit;

    @FXML
    private TextField log;

    @FXML
    private PasswordField par;

    private User user;

    @FXML
    private Pane logzan;

    @FXML
    private HBox nav;

    @FXML
    private Pane BN;

    @FXML
    private Pane BC;

    @FXML
    private Pane BM;

    private boolean caps_lock = false;

    private Pane act_pane;

    private int next_id = -1;

    private int node = 0;

    private User rep_us;

    public void init()
    {
        user = usersImpl.getuser(Start.usename);
        if(user != null)
        {
            usern.setText(Start.usename);
            log.setText(Start.usename);
            par.setText(user.getPassword());
            set_ico();
        }
        if(next_id == -1)
        {
            for(Node node : nav.getChildren()) node.setOpacity(0);
        }
        caps_lock = Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK);
        set_caps();
        act_pane = (Pane) Start.roots.get("id_" + id);
        p_edit.getScene().addEventHandler(KeyEvent.KEY_RELEASED, this::caps);
        toMain();
        if(next_id == -1) onNavV();
        super.init();
    }

    public void browse_3535() throws URISyntaxException, IOException {
        Desktop d = Desktop.getDesktop();
        d.browse(new URI("tel:+79152452075"));
    }

    public void browse_0088() throws URISyntaxException, IOException {
        Desktop d = Desktop.getDesktop();
        d.browse(new URI("tel:+79152451530"));
    }

    private void set_ico()
    {
        ico = user.getIcons();
        icon.setImage(new Image(getClass().getResourceAsStream("/img/ls-icon" + (ico + 1) + ".png")));
        switch (ico) {
            case 0 -> ra1.setSelected(true);
            case 1 -> ra2.setSelected(true);
            case 2 -> ra3.setSelected(true);
        }
    }


    private void caps(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.CAPS) {
            caps_lock = !caps_lock;
            set_caps();
        }
    }

    private void set_caps()
    {
        caps.setVisible(caps_lock);
    }

    private void upd_str()
    {
        glavn.setVisible(false);
        news.setVisible(false);
        cont.setVisible(false);
        edit.setVisible(false);
        ernull.setVisible(false);
        erpat.setVisible(false);
        gen.setVisible(false);
        if(erpat.getParent() != null) ((Pane)erpat.getParent()).getChildren().remove(erpat);
        if(ernull.getParent() != null) ((Pane)ernull.getParent()).getChildren().remove(ernull);
        if(gen.getParent() != null) ((Pane)gen.getParent()).getChildren().remove(gen);
        nePN(newEvent(BN));
        nePN(newEvent(BC));
        nePN(newEvent(BM));
    }

    private MouseEvent newEvent(Object obj)
    {
        return new MouseEvent(obj, null, null, 0,0,0,0, null,0,false,false,false,false,false,false,false,false,false,false,false,false,null);
    }

    public void toNews()
    {
        upd_str();
        news.setVisible(true);
        onPN(newEvent(BN));
    }

    public void toContact()
    {
        upd_str();
        cont.setVisible(true);
        onPN(newEvent(BC));
    }

    public void toMain()
    {
        upd_str();
        glavn.setVisible(true);
        onPN(newEvent(BM));
    }

    public void toEdit()
    {
        upd_str();
        edit.setVisible(true);
    }

    public void toEdit(User user)
    {
        upd_str();
        edit.setVisible(true);
        logzan.setVisible(true);
        rep_us = user;
    }

    public void onEnter(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ENTER) onedit();
    }

    private void onNavV()
    {
        List<KeyValue> kv = new ArrayList<>();
        if(node == nav.getChildren().size() - 1) onUser1();
        if(node <= nav.getChildren().size() - 1)
        {
            Node nod = nav.getChildren().get(node);
            onNav(newEvent(nod));
            kv.add(new KeyValue(nod.opacityProperty(), 1.0, inter));
            kv.add(new KeyValue(nod.mouseTransparentProperty(), true, inter));
            nod.mouseTransparentProperty().addListener(this::changed);
        }
        played(kv, 1000);
    }

    private void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
        if(new_val)
        {
            Node nod = nav.getChildren().get(node);
            if(node == nav.getChildren().size() - 1) neUser();
            neNav(newEvent(nod));
            nod.mouseTransparentProperty().removeListener(this::changed);
            nod.setMouseTransparent(false);
            node++;
            onNavV();
        }
    }

    public void onNav(MouseEvent mouseEvent)
    {
        Label label = getLabel(mouseEvent.getSource());
        Glow glow = (Glow) label.getEffect();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(glow.levelProperty(), 1.0, inter));
        played(kv, 200);
    }

    public void neNav(MouseEvent mouseEvent)
    {
        Label label = getLabel(mouseEvent.getSource());
        Glow glow = (Glow) label.getEffect();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(glow.levelProperty(), 0, inter));
        played(kv, 200);
    }

    public void onUser(MouseEvent mouseEvent)
    {
        onNav(mouseEvent);
        onUser1();
    }

    public void onUser1()
    {
        menu.setVisible(true);
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(menu.opacityProperty(), 1, inter));
        kv.add(new KeyValue(menu.layoutYProperty(), 0, inter));
        for(Node node : menu.getChildren()) kv.add(new KeyValue(node.mouseTransparentProperty(), false, inter));
        played(kv, 500);
    }

    public void neUser()
    {
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(menu.layoutYProperty(), -175, inter));
        kv.add(new KeyValue(menu.opacityProperty(), 0, inter));
        kv.add(new KeyValue(menu.visibleProperty(), false, inter));
        for(Node node : menu.getChildren()) kv.add(new KeyValue(node.mouseTransparentProperty(), true, inter));
        played(kv, 500);
    }

    public void onPN(MouseEvent mouseEvent)
    {
        Label label = getLabel(mouseEvent.getSource());
        InnerShadow innerShadow = (InnerShadow) ((DropShadow)((Glow)label.getEffect()).getInput()).getInput();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(innerShadow.chokeProperty(), 0.3, inter));
        played(kv, 100);
    }

    public void nePN(MouseEvent mouseEvent)
    {
        Label label = getLabel(mouseEvent.getSource());
        InnerShadow innerShadow = (InnerShadow) ((DropShadow)((Glow)label.getEffect()).getInput()).getInput();
        List<KeyValue> kv = new ArrayList<>();
        kv.add(new KeyValue(innerShadow.chokeProperty(), 0, inter));
        played(kv, 100);
    }

    private Label getLabel(Object obj)
    {
        if(obj.getClass().getSimpleName().equals("Pane")) {
            Pane pane = (Pane) obj;
            return (Label) pane.getChildren().get(0);
        } else {
            return usern;
        }
    }

    public void onedit()
    {
        boolean stat1 = getstat(log);
        boolean stat2 = getstat(par);
        if(stat1 && stat2)
        {
            System.out.println(log.getText() + " " + usersImpl.getuser(log.getText()));
            if(usersImpl.getuser(log.getText()) != null && !Objects.equals(user.getUsername(), log.getText()))
            {
                logzan.setVisible(true);
                return;
            }
            logzan.setVisible(false);
            if(!ModelController.inet) Start.off_replace.addAll(List.of(Start.usename, log.getText()));
            UsersImpl.map.remove(Start.usename);
            Start.usename = log.getText();
            if(rep_us == null) {
                user.setUsername(log.getText());
                user.setPassword(par.getText());
                user.setIcons(ico);
                usersImpl.addorsave(user);
                usern.setText(Start.usename);
                set_ico();
                toMain();
            } else {
                rep_us.setUsername(log.getText());
                rep_us.setPassword(par.getText());
                rep_us.setIcons(ico);
                usersImpl.addorsave(rep_us);
                usern.setText(Start.usename);
                set_ico();
                toMain();
                neWarn(net_rep);
                rep_us = null;
            }
        }
    }

    public void toBegin() throws IOException {
        Start.usename = null;
        Start.close_project();
        Start.start_scene("/fxml/start.fxml");
        Start.starts();
        StartController startController = Start.loader.getController();
        startController.setPer_rep(per_rep);
        startController.setPer_reg(per_reg);
        if(this.net_rep.isVisible()) startController.onWarn(startController.net_rep);
        if(this.net_reg.isVisible()) startController.onWarn(startController.net_reg);
    }

    public void ranpar()
    {
        ranpar(new PasswordField[]{par});
    }
}
