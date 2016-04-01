package co.com.core.services.cms.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import co.com.core.commons.converter.cms.BrandTypeUtil;
import co.com.core.dao.cms.BrandTypeDAO;
import co.com.core.domain.cms.BrandType;
import co.com.core.dto.cms.BrandTypeDTO;
import co.com.core.services.cms.IBrandTypeService;

public class BrandTypeServiceImpl implements IBrandTypeService {

	private static final Logger logger = Logger.getLogger(BrandTypeServiceImpl.class);
	BrandTypeDAO brandtypeDAO;
	
	@Override
	public List<BrandTypeDTO> getAll() {
		List<BrandTypeDTO> BrandTypes = new ArrayList<BrandTypeDTO>();
		List<BrandType> entityList = brandtypeDAO.getAll();
		if(entityList!=null && entityList.size() > 0) {
			for(BrandType BrandType : entityList) {
				BrandTypes.add(BrandTypeUtil.getDtoFromEntity(BrandType));
			}
		}
		return BrandTypes;
	}

	@Override
	public void create(BrandTypeDTO dto) {
		brandtypeDAO.create(BrandTypeUtil.getEntityFromDto(dto));
	}

	@Override
	public void delete(BrandTypeDTO dto) {
		brandtypeDAO.delete(BrandTypeUtil.getEntityFromDto(dto));
	}

	@Override
	public void update(BrandTypeDTO dto) {
		brandtypeDAO.update(BrandTypeUtil.getEntityFromDto(dto));
	}

	public BrandTypeDAO getBrandTypeDAO() {
		return brandtypeDAO;
	}

	public void setBrandTypeDAO(BrandTypeDAO BrandTypeDAO) {
		this.brandtypeDAO = BrandTypeDAO;
	}
}