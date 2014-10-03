/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.jmgs.dynamicformsbase.dyna;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;

public class FormField implements Serializable {
 
    private String name;
    private Object value;
    private boolean required;
    /**
     * items para selects
     */
    private List<SelectItem> selectItems;
 
    public FormField(String name, boolean required) {
        this.name = name;
        this.required = required;
    }
 
    public FormField(String name, Object value, boolean required) {
        this(name, value, required, null);
    }
    
    public FormField(String name, Object value, boolean required, List<SelectItem> selectItems) {
        this.name = name;
        this.value = value;
        this.required = required;
        this.selectItems = selectItems;
    }
 
    // getter // setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public Object getFormattedValue() {  
        if (value instanceof Date) {  
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");  
  
            return simpleDateFormat.format(value);  
        }  
  
        return value;  
    }  

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
    
    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }
    
}
