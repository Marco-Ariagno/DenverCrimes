package it.polito.tdp.crimes.model;

import java.util.List;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	
	public Model() {
		dao=new EventsDao();
	}
	
	public List<Event> getAllEvents(){
		return dao.listAllEvents();
	}
	
	public List<String> getAllCategories(){
		return dao.getCategorie();
	}
	
}
