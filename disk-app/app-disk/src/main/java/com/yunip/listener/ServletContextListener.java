package com.yunip.listener;

import javax.servlet.ServletContextEvent;

import com.yunip.socket.SocketService;

public class ServletContextListener implements
		javax.servlet.ServletContextListener {
	SocketService service;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		service.closeServer();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		service = new SocketService();
		service.initSocketServer();
	}

}
