package co.com.core.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import co.com.core.dao.PageDAO;
import co.com.core.domain.Page;


public class PageDAOImpl implements PageDAO{
	
    private SessionFactory sessionFactory;
    private Session session;
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	
	
	@Override
	public List<Page> getAll() {
		List<Page> pages = null;
		try {
			session = this.sessionFactory.openSession();
	        Query query = session.getNamedQuery("Page.findAll");
			pages = query.list();
		} catch(Exception ex) {
			//TODO logger
		} finally {
			session.close();
		}
		return pages;
	}

	@Override
	public void createPage(Page page) {
		try {
			session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.save(page);
			tx.commit();
		} catch(Exception ex) {
			//TODO
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	@Override
	public void update(Page page) {
		try {
			session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
	        session.merge(page);
	        tx.commit();
		} catch(Exception ex) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	@Override
	public void delete(Page page) {
		try {
			session = this.sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query query = session.createQuery("delete Page p where p.pageId = :pageId");
			query.setParameter("pageId", page.getPageId());
			query.executeUpdate();
			tx.commit();
		} catch(Exception ex) {
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}


	@Override
	public List<Page> getPageByURL(String url) {
		List<Page> pages = null;
		try {
			session = this.sessionFactory.openSession();
			StringBuilder hql = new StringBuilder();
//			hql.append("SELECT * FROM page WHERE url LIKE '%").append(url).append("%'");
			hql.append("SELECT p FROM Page p WHERE p.url LIKE :url");
	        Query query = session.createQuery(hql.toString());
	        query.setParameter("url", "%"+url+"%");
	        pages = query.list();
		} catch(Exception ex) {
			System.out.println("ERROR PAGE DAO: " + ex.getMessage());
		} finally {
			session.close();
		}
		
		return pages;
	}

	

}