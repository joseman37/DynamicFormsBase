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
import mx.jmgs.dynamicformsbase.dyna.xml.DynamicForm;
import mx.jmgs.dynamicformsbase.dyna.xml.Field;
import mx.jmgs.dynamicformsbase.dyna.xml.FieldSelectItem;
import mx.jmgs.dynamicformsbase.dyna.xml.Row;
import mx.jmgs.dynamicformsbase.dyna.xml.SelectItems;
import org.primefaces.context.RequestContext;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

@ManagedBean
@SessionScoped // TODO cambiarlo a viewScoped. Utilizar request parameter para especificar el form name.
public class DynaFormController implements Serializable {

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
            DynamicForm form = forms.getForms().get(formName);
            
            for(Row xmlRow : form.getRows()) {
                // add rows, labels and editable controls
                // set relationship between label and editable controls to support outputLabel with "for" attribute
                // row
                DynaFormRow row = model.createRegularRow();
                for(Field field : xmlRow.getFields()) {
                    DynaFormLabel label = null;
                    if(field.getLabel() != null) {
                        label = row.addLabel(field.getLabel());
                    }
                    FormField formField = new FormField(field.getId(), field.isRequired());
                    
                    //Get the field's SelectItems
                    SelectItems xmlItems = field.getSelectItems();
                    if(xmlItems != null) {
                        List<SelectItem> items = new ArrayList<>();
                        for(FieldSelectItem item : xmlItems.getSelectItem()) {
                            items.add(new SelectItem(item.getValue(), item.getLabel()));
                        }
                        formField.setSelectItems(items);
                    }
                    
                    //crete the field and label
                    DynaFormControl control = row.addControl(formField,
                            field.getType().value(), field.getColspan() != null ? field.getColspan() : 1,
                            field.getRowspan()!=null? field.getRowspan() : 1);
                     if(label != null) {
                        label.setForControl(control);
                     }
                }
            }
        }
        return model;
    }

    public List<FormField> getUserResponse() {
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

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        if (this.formName!= null && !this.formName.equals(formName)) {
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
