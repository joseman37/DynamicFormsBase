/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.jmgs.dynamicformsbase.jsf;

import java.io.IOException;
import mx.jmgs.dynamicformsbase.dyna.FormField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.w3c.dom.Document;

@ManagedBean
@SessionScoped // TODO cambiarlo a viewScoped. Utilizar request parameter para especificar el form name.
public class DynaFormController implements Serializable {

    private static List<SelectItem> LANGUAGES = new ArrayList<SelectItem>();

    @ManagedProperty("#{dynamicFormRepository}")
    private DynamicFormRepository forms;

    private DynaFormModel model;

    private String formName;

    public DynaFormController() {

    }

    public DynaFormModel getModel() throws IOException {
        if (model == null) {
            model = new DynaFormModel();

            //Create the DynamicModel according to form name.
            Document doc = forms.getForms().get(formName);
            

            // add rows, labels and editable controls
            // set relationship between label and editable controls to support outputLabel with "for" attribute
            // 1. row
            DynaFormRow row = model.createRegularRow();

            DynaFormLabel label11 = row.addLabel("Author", 1, 1);
            DynaFormControl control12 = row.addControl(new FormField("Author", true), "input", 1, 1);
            label11.setForControl(control12);

            DynaFormLabel label13 = row.addLabel("ISBN", 1, 1);
            DynaFormControl control14 = row.addControl(new FormField("ISBN", true), "input", 1, 1);
            label13.setForControl(control14);

            // 2. row
            row = model.createRegularRow();

            DynaFormLabel label21 = row.addLabel("Title", 1, 1);
            DynaFormControl control22 = row.addControl(new FormField("Title", false), "input", 3, 1);
            label21.setForControl(control22);

            // 3. row
            row = model.createRegularRow();

            DynaFormLabel label31 = row.addLabel("Publisher", 1, 1);
            DynaFormControl control32 = row.addControl(new FormField("Publisher", false), "input", 1, 1);
            label31.setForControl(control32);

            DynaFormLabel label33 = row.addLabel("Published on", 1, 1);
            DynaFormControl control34 = row.addControl(new FormField("Published on", false), "calendar", 1, 1);
            label33.setForControl(control34);

            // 4. row
            row = model.createRegularRow();

            DynaFormLabel label41 = row.addLabel("Language", 1, 1);
            DynaFormControl control42 = row.addControl(new FormField("Language", false), "select", 1, 1);
            label41.setForControl(control42);

            DynaFormLabel label43 = row.addLabel("Description", 1, 2);
            DynaFormControl control44 = row.addControl(new FormField("Description", false), "textarea", 1, 2);
            label43.setForControl(control44);

            // 5. row
            row = model.createRegularRow();

            DynaFormLabel label51 = row.addLabel("Rating", 1, 1);
            DynaFormControl control52 = row.addControl(new FormField("Rating", 3, true), "rating", 1, 1);
            label51.setForControl(control52);
        }
        return model;
    }

    public List<FormField> getBookProperties() {
        if (model == null) {
            return null;
        }
        List<FormField> bookProperties = new ArrayList<FormField>();
        for (DynaFormControl dynaFormControl : model.getControls()) {
            bookProperties.add((FormField) dynaFormControl.getData());
        }
        return bookProperties;
    }

    public String submitForm() {
        FacesMessage.Severity sev = FacesContext.getCurrentInstance().getMaximumSeverity();
        boolean hasErrors = ((sev != null) && (FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0));
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.addCallbackParam("isValid", !hasErrors);
        return null;
    }

    public List<SelectItem> getLanguages() {
        if (LANGUAGES.isEmpty()) {
            LANGUAGES.add(new SelectItem("en", "English"));
            LANGUAGES.add(new SelectItem("de", "German"));
            LANGUAGES.add(new SelectItem("ru", "Russian"));
            LANGUAGES.add(new SelectItem("tr", "Turkish"));
        }

        return LANGUAGES;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        if (!this.formName.equals(formName)) {
            //Create the DynamicModel according to form name.
            model = null;
        }
        this.formName = formName;
    }

    public DynamicFormRepository getForms() {
        return forms;
    }

    public void setForms(DynamicFormRepository forms) {
        this.forms = forms;
    }
}
