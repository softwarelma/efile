package com.softwarelma.efile;

import javax.servlet.annotation.WebServlet;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("efiletheme")
public class EfileMain extends UI {

    private static final long serialVersionUID = 1L;

    public EfileMain() {
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        try {
            EfileServer server = EfileServer.getInstance();
            server.loadPage(this);
        } catch (EpeAppException e) {
        }
    }

    @WebServlet(urlPatterns = "/*", name = "EfileMainServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = EfileMain.class, productionMode = false)
    public static class EfileMainServlet extends VaadinServlet {
        private static final long serialVersionUID = 1L;
    }

}
