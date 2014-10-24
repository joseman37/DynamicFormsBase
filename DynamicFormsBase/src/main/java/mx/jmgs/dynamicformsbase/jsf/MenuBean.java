package mx.jmgs.dynamicformsbase.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean(name = "menu")
@ApplicationScoped
public class MenuBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Page> pages;
	
	public MenuBean() {
		pages = new ArrayList<>();
		pages.add(new Page("/index.xhtml", "Home"));
		pages.add(new Page("/dynamicForm/form.xhtml", "DynamicForm"));
		pages.add(new Page("/dynamicForm/statistics.xhtml", "Statistics"));
	}

	public List<Page> getPages() {
		return pages;
	}
	
	public static class Page {
		
		private String viewId;
		private String title;
		
		public Page(String viewId, String title) {
			this.title = title;
			this.viewId = viewId;
		}

		/**
		 * @return the viewId
		 */
		public String getViewId() {
			return viewId;
		}

		/**
		 * @param viewId the viewId to set
		 */
		public void setViewId(String viewId) {
			this.viewId = viewId;
		}

		/**
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}
		
		
		
	}
}
