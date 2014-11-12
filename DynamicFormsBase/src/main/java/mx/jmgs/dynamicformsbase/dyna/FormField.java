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
 
    /**
	 * Helps serialization 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
    private Object value;
    private boolean required;
    private String placeholder;
    private String label;
    private Integer minValue;
    private Integer maxValue;
    private Integer minLength;
    private Integer maxLength;
    
    /**
     * items para selects
     */
    private List<SelectItem> selectItems;
 
    public FormField() {
        
    }
    
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

	/**
	 * @return the placeholder
	 */
	public String getPlaceholder() {
		return placeholder;
	}

	/**
	 * @param placeholder the placeholder to set
	 */
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the minValue
	 */
	public Integer getMinValue() {
		return minValue;
	}

	/**
	 * @param min the minValue to set
	 */
	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return the maxValue
	 */
	public Integer getMaxValue() {
		return maxValue;
	}

	/**
	 * @param max the maxValue to set
	 */
	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * @return the minLength
	 */
	public Integer getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the maxLength
	 */
	public Integer getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
    
}
