package org.namefilip.heavywork.service;

import java.util.concurrent.Future;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

/**
* @author Filip Spiridonov
*/
@Service
public class HeavyWorkServiceImpl implements HeavyWorkService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	@Async
	public Future<String> count(Integer number, HttpSession session) {
		
		logger.debug("Starting counting to " + number);
		
		if (number != null) {
			try {
				for (int i = 0; i < number; i++) {
					int status = (i + 1) * 100 / number;
					session.setAttribute("status", status);
					logger.debug("Task status is: " + status);
					
					Thread.sleep(1000);
				}
			}
			catch (InterruptedException e) {
				logger.error("Error generated", e);
			}
		}
		
		// Answer to the Ultimate Question of Life, The Universe, and Everything
		// (https://answers.yahoo.com/question/index?qid=20090101183349AAtGHAK)
		return new AsyncResult<String>("42");
		
		
		
	}
	
}
