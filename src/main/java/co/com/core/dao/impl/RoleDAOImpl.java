package co.com.core.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import co.com.core.dao.RoleDAO;
import co.com.core.domain.Role;
import co.com.core.domain.UserRole;

public class RoleDAOImpl implements RoleDAO {

	 	private SessionFactory sessionFactory;
	    private Session session;
	    private static final Logger logger = Logger.getLogger(RoleDAOImpl.class);
	    
	    public void setSessionFactory(SessionFactory sessionFactory) {
	        this.sessionFactory = sessionFactory;
	    }
		
		
		@Override
		public List<Role> getAll() {
			List<Role> roles = null;
			try {
				session = this.sessionFactory.openSession();
		        Query query = session.getNamedQuery("Role.findAll");
		        roles = query.list();
			} catch(Exception ex) {
				logger.error("Throwed Exception [RoleDAOImpl.getAll]: " +ex.getMessage());
			} finally {
				session.close();
			}
			return roles;
		}

		@Override
		public void createRole(Role role) {
			try {
				session = this.sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				session.save(role);
				tx.commit();
			} catch(Exception ex) {
				logger.error("Throwed Exception [RoleDAOImpl.createRole]: " +ex.getMessage());
				session.getTransaction().rollback();
			} finally {
				session.close();
			}
		}

		@Override
		public void update(Role role) {
			try {
				session = this.sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
		        session.merge(role);
		        tx.commit();
			} catch(Exception ex) {
				logger.error("Throwed Exception [RoleDAOImpl.update]: " +ex.getMessage());
				session.getTransaction().rollback();
			} finally {
				session.close();
			}
		}
		@Override
		public void delete(Role role) {
			logger.error("ROLE DTO - SERVICE: " + role);
			try {
				session = this.sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				session.delete(role);
				//Query query = session.createQuery("delete Role r where Role.roleId = :roleId");
				//query.setParameter("roleId", role.getRoleId());
				//query.executeUpdate();
				tx.commit();
			} catch(Exception ex) {
				logger.error("Throwed Exception [RoleDAOImpl.delete]: " +ex.getMessage());
				session.getTransaction().rollback();
			} finally {
				session.close();
			}
		}
}
