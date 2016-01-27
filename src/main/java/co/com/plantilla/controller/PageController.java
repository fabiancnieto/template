package co.com.plantilla.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import co.com.plantilla.dto.PageDTO;
import co.com.plantilla.services.IPageService;


public class PageController {

	IPageService pageService;
	List<PageDTO> items;
	private PageDTO selected;

	@PostConstruct
	public void init() {
		items = pageService.getAll();
	}

	public void saveNew() {
		try {
			pageService.createPage(selected);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Creación exitosa", "Página"));
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error en creación", "Página"));
		} finally {
			items = pageService.getAll();
		}

	}

	public void delete() {
		if (this.selected != null) {
			try {
				pageService.delete(selected);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Eliminado", "Página"));
			} catch (Exception ex) {
				ex.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error en eliminación", "Página"));
			} finally {
				items = pageService.getAll();
			}
		}
	}

	public void save() {
		if (this.selected != null) {
			try {
				pageService.update(selected);
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Actualizado", "Página"));
			} catch (Exception ex) {
				ex.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Error en actualización", "Página"));
			} finally {
				items = pageService.getAll();
			}
		}
	}

	public void prepareCreate() {
		selected = new PageDTO();
	}
	
	public IPageService getPageService() {
		return pageService;
	}

	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}

	public List<PageDTO> getItems() {
		return items;
	}

	public void setItems(List<PageDTO> items) {
		this.items = items;
	}

	public PageDTO getSelected() {
		return selected;
	}

	public void setSelected(PageDTO selected) {
		this.selected = selected;
	}
}
