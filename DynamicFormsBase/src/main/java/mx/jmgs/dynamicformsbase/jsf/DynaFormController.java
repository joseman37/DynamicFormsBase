/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.jmgs.dynamicformsbase.jsf;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import mx.jmgs.dynamicformsbase.dyna.FormField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.xml.bind.JAXBElement;

import mx.jmgs.dynamicformsbase.dyna.xml.Bundle;
import mx.jmgs.dynamicformsbase.dyna.xml.DynamicForm;
import mx.jmgs.dynamicformsbase.dyna.xml.Field;
import mx.jmgs.dynamicformsbase.dyna.xml.FieldSelectItem;
import mx.jmgs.dynamicformsbase.dyna.xml.FormElement;
import mx.jmgs.dynamicformsbase.dyna.xml.FormSeparator;
import mx.jmgs.dynamicformsbase.dyna.xml.Label;
import mx.jmgs.dynamicformsbase.dyna.xml.Row;
import mx.jmgs.dynamicformsbase.util.JsfUtil;

import org.primefaces.context.RequestContext;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

import freemarker.cache.StringTemplateLoader;
import freemarker.ext.beans.ResourceBundleModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@ManagedBean
@SessionScoped // TODO cambiarlo a viewScoped. Utilizar request parameter para especificar el form name.
public class DynaFormController implements Serializable {

	/**
	 * Helps serialization
	 */
	private static final long serialVersionUID = 1L;

	private static final String SEPARATOR_TYPE = "separator";
	
    @ManagedProperty("#{dynamicFormRepository}")
    private DynamicFormRepository forms;

    private DynaFormModel model;

    private String formName;
    /**
     * Used to identify if the locale has changed, if so, the dynamic form model should be updated.
     */
    private Locale lastLocale;

    public DynaFormController() {
    	lastLocale = JsfUtil.getLocale();
    }

	public DynaFormModel getModel() throws IOException {
    	//revisar cuando cambia el lenguaje para restablecer el modelo
    	if(lastLocale != JsfUtil.getLocale()) {
    		model = null;
    	}
        
    	if (model == null) {
            model = new DynaFormModel();

            //Create the DynamicModel according to form name.
            DynamicForm form = forms.getForms().get(formName);
            //fila de labels
            Queue<LabelInfo> queue = new LinkedList<>();
            //fields' hash map
            Map<String, DynaFormControl> controlMap = new HashMap<>();
            
            // read bundles
            HashMap<String, Object> bundles = new HashMap<>();
            ResourceBundle rb = null;
            Locale locale =  JsfUtil.getLocale();
            for(Bundle xmlBundle : form.getBundles()) {
            	rb = ResourceBundle.getBundle(xmlBundle.getBaseName(), locale);
            	bundles.put(xmlBundle.getVar(), rb);
            	
            }
            
            //Read rows
            for(Row xmlRow : form.getRows()) {
                // add rows, labels and editable controls
                // set relationship between label and editable controls to support outputLabel with "for" attribute
                // row
                DynaFormRow row = model.createRegularRow();
                for(JAXBElement<FormElement> obj : xmlRow.getFormElements()) {
                	FormElement element = (FormElement)obj.getValue();
                	if(element instanceof Label) {
                		Label control = (Label)element;
                		//TODO see if we need to clone the data passed to the method getText().
                		String text = processTemplateText(control.getText(), bundles);
                		DynaFormLabel  label = row.addLabel(text, control.getColspan(), control.getRowspan());
                		if(control.getFor()!= null) {
                			queue.add(new LabelInfo(label, control.getFor()));
                		}
                	} else if (element instanceof FormSeparator) {
                		FormSeparator control = (FormSeparator)element;
                		row.addControl(control.getText(), SEPARATOR_TYPE, control.getColspan(), control.getRowspan());
                	} else if (element instanceof Field) {
                		Field field = (Field)element;
                		 FormField formField = new FormField(field.getId(), field.isRequired());
                		//Get the field's SelectItems
                         List<FieldSelectItem> xmlItems = field.getSelectItems();
                         if(xmlItems!= null && !xmlItems.isEmpty()) {
                             List<SelectItem> items = new ArrayList<>();
                             for(FieldSelectItem item : xmlItems) {
                                 items.add(new SelectItem(item.getValue(), item.getLabel()));
                             }
                             formField.setSelectItems(items);
                         }
                         if(field.getPlaceholder() != null) {
	                         String placeholder = processTemplateText(field.getPlaceholder(), bundles);
	                         formField.setPlaceholder(placeholder);
                         }
                       
                         //crete the field.
                         DynaFormControl control = row.addControl(formField,
                                 field.getType().value(), field.getColspan(), field.getRowspan());
                         // Add to the map
                         controlMap.put(field.getId(), control);
                	}
                }
            }
            //Asignar los labels A los fields.
            LabelInfo labelInfo;
            while ((labelInfo = queue.poll()) != null) {
            	DynaFormLabel label = labelInfo.getLabel();
            	label.setForControl(controlMap.get(labelInfo.getForControlId()));
            }
            
        }
        return model;
    }

    public List<FormField> getUserResponse() {
        if (model == null) {
            return null;
        }
        List<FormField> formFields = new ArrayList<FormField>();
		for (DynaFormControl dynaFormControl : model.getControls()) {
			if (dynaFormControl.getData() instanceof FormField) {
				formFields.add((FormField) dynaFormControl.getData());
			}
		}
        return formFields;
    }

    public String submitForm() {
        FacesMessage.Severity sev = FacesContext.getCurrentInstance().getMaximumSeverity();
        boolean hasErrors = ((sev != null) && (FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0));
        RequestContext requestContext = RequestContext.getCurrentInstance();
        //There's an args object available in the scope that will get this value through AJAX
        // and JSON. In json use:
        // args && args.isValid
        requestContext.addCallbackParam("isValid", !hasErrors);
        return null;
    }
    
    /**
     * Process the template of the form. in the specified local.
     * @param templateStr
     * @param data Data for the template. It will be clonned by this method.
     * @return
     */
    public String processTemplateText(String templateStr, HashMap<String, Object> data) {
		try {
			Template template = forms.getTemplate(templateStr);
			
			StringWriter sw = new StringWriter();
			template.process(data, sw);
			return sw.toString();
		} catch (IOException | TemplateException e) {
			throw new RuntimeException("Error while loading template", e);
		} 
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
    
    /**
     * Internal class used to pass the information of label to the creation process queue.
     * @author Jose
     *
     */
    private class LabelInfo {
    	
    	private DynaFormLabel label;
    	
    	private String forControlId;
    	
		public LabelInfo(DynaFormLabel label, String forControlId) {
			super();
			this.label = label;
			this.forControlId = forControlId;
		}
		/**
		 * @return the label
		 */
		public DynaFormLabel getLabel() {
			return label;
		}
		/**
		 * @param label the label to set
		 */
		public void setLabel(DynaFormLabel label) {
			this.label = label;
		}
		/**
		 * @return the forControlId
		 */
		public String getForControlId() {
			return forControlId;
		}
		/**
		 * @param forControlId the forControlId to set
		 */
		public void setForControlId(String forControlId) {
			this.forControlId = forControlId;
		}
    	
    }
}
