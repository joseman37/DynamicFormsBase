package mx.jmgs.dynamicformsbase.struct;

import java.util.Date;

/**
 * Class that holds the template statistics.
 * 
 * @author mgonzalez
 *
 */
public class TemplateStat {
	
	private String template;
	private Date lastModified;
	private String source;
	
	public TemplateStat(String template, Date lastModified, String source) {
		super();
		this.template = template;
		this.lastModified = lastModified;
		this.source = source;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the lastModified
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	

}
