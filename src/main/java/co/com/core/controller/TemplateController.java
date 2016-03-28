package co.com.core.controller;

import static co.com.core.commons.LoadBundle.geProperty;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import co.com.core.commons.FileUploader;
import co.com.core.commons.LoadBundle;
import co.com.core.commons.SendEmail;
import co.com.core.commons.converter.UserUtil;
import co.com.core.domain.User;
import co.com.core.dto.MenuDTO;
import co.com.core.dto.PageDTO;
import co.com.core.dto.UploadedFileDTO;
import co.com.core.dto.UserDTO;
import co.com.core.services.IMenuService;
import co.com.core.services.IPageService;
import co.com.core.services.IUploadedFileService;

public class TemplateController {

	private IMenuService menuService;
	private IPageService pageService;
	private static final Logger logger = Logger.getLogger(TemplateController.class);
	
	private IUploadedFileService uploadedFileService;
	
	public void validateUserSession() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			String viewId = context.getViewRoot().getViewId();
			if(viewId !=null && !viewId.contains("login")) {
				UserDTO userDto = (UserDTO) context.getExternalContext().getSessionMap().get("user");
				
				if(userDto==null) {
					context.getExternalContext().redirect("/friogan/inicio");
				} else {
					boolean validPage = validateAllowedPage(userDto, viewId);
					if(!validPage) {
						//context.getExternalContext().redirect("/friogan/home/profile.xhtml");
						logger.info("Invalid page access: " + userDto.getFirstName() + "Page: " + viewId);
					}
					logger.info("Valid session: " + userDto.getFirstName() + "Page: " + viewId);
				}
			}

		} catch(Exception ex) {
			logger.error("Exception validating session: " + ex.getMessage());
		}
		
		
	}

	private boolean validateAllowedPage(UserDTO userDto, String viewId) {
		boolean allowedPage = false;
		
		User user = UserUtil.getEntityFromDto(userDto);
		
		List<MenuDTO> menuList = menuService.getUserMenu(user);
		
		PageDTO pageDto = pageService.getPageByURL(viewId);
		
		if(menuList!=null && menuList.size() > 0) {
			if(pageDto!=null) {
				for(MenuDTO menu : menuList) {
					if(menu.getPageId() != null) {
						if(menu.getPageId().getPageId() == pageDto.getPageId()) {
							allowedPage = true;
							logger.info("Allowed page: " + pageDto.getUrl());
						}
					}
				}
			}
		} 
		
		return allowedPage;
	}
	
	public PageDTO getPageFromUrl() {
		PageDTO pageDto = new PageDTO();
		return pageDto;
	}
	
    public void upload(FileUploadEvent event) {  
    	UploadedFile file = event.getFile();
    	String path = LoadBundle.getApplicationProperty("fileUploadPath");
    	boolean success = FileUploader.uploadFile(event, path);
    	FacesContext context = FacesContext.getCurrentInstance();
    	if(success) {
    		UserDTO userDto = (UserDTO) context.getExternalContext().getSessionMap().get("user");
			if(userDto!=null) {
				UploadedFileDTO dto = new UploadedFileDTO();
				dto.setUserId(UserUtil.getEntityFromDto(userDto));
				dto.setCreationDate(new Timestamp(new Date().getTime()));
				dto.setSize((int) file.getSize());
				dto.setName(file.getFileName());
				uploadedFileService.create(dto);
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, geProperty("successfulCreation"), null));
			} else {
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, geProperty("fileUploadError"), null));
				logger.error("Error uploading the file: " + file.getFileName() + " UserDto is null");
			}
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, geProperty("fileUploadSuccess"), null));
    	} else {
    		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, geProperty("fileUploadError"), null));
    	}
    }  
 
    public void sendMail() {
    	SendEmail mailer = new SendEmail();
    	mailer.sendEmail();
    }
    
	public IMenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(IMenuService menuService) {
		this.menuService = menuService;
	}

	public IPageService getPageService() {
		return pageService;
	}

	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}

	public IUploadedFileService getUploadedFileService() {
		return uploadedFileService;
	}

	public void setUploadedFileService(IUploadedFileService uploadedFileService) {
		this.uploadedFileService = uploadedFileService;
	}
	
	
}
