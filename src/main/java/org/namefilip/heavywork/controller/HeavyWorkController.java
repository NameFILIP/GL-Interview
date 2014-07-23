package org.namefilip.heavywork.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.namefilip.heavywork.model.CounterData;
import org.namefilip.heavywork.service.HeavyWorkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* @author Filip Spiridonov
*/
@Controller
@RequestMapping("/counter")
public class HeavyWorkController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String HEAVY_WORK_SERVICE_RESULT = "heavyWorkServiceResult";
	
	@Autowired
	HeavyWorkService heavyWorkService;
	

	/**
	 * Handles the POST request and lets HeavyWorkService to start processing
	 * 
	 * @param counterData - contains data submitted by user
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value="/start", method = RequestMethod.POST)
	public void count(@RequestBody CounterData counterData, HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		// Cancel previous task if exists and running
		cancelCurrentIfExists(request);
		
		logger.info("Starting new counter to " + counterData.getCountTo());
		Future<String> result = heavyWorkService.count(counterData.getCountTo(), session);
		session.setAttribute(HEAVY_WORK_SERVICE_RESULT, result);
	}
	
	/**
	 * If the counter is running it returns a string "Running".
	 * If the task has been successfully completed, it returns the result of the computation.
	 */
	@ResponseBody
	@RequestMapping(value="/status", method = RequestMethod.GET)
	public Map<Boolean, String> status(HttpServletRequest request) {
		
		Map<Boolean, String> resultMap = new HashMap<>();
		HttpSession session = request.getSession();
		
        @SuppressWarnings("unchecked")
        Future<String> result = (Future<String>) session.getAttribute(HEAVY_WORK_SERVICE_RESULT);
        
        if (result == null) {
        	throw new RuntimeException("Counter hasn't been started");
        }
        if (result.isDone()) {
        	logger.info("Counter " + result + " has been finished");
        	try {
        		resultMap.put(true, result.get());
            }
            catch (InterruptedException | ExecutionException e) {
            	throw new RuntimeException("Error has occured", e);
            }
        } else {
        	// Hasn't yet been finished. Return progress status
        	resultMap.put(false, session.getAttribute("status").toString());
        }
        logger.debug("Requested status map is: " + resultMap);
        return resultMap;
	}
	
	/**
	 * API for cancelling current counter
	 */
	@ResponseBody
	@RequestMapping(value="/cancel", method = RequestMethod.GET)
	public void cancel(HttpServletRequest request) {
		cancelCurrentIfExists(request);
	}
	
	
	private void cancelCurrentIfExists(HttpServletRequest request) {
		@SuppressWarnings("unchecked")
        Future<String> current = (Future<String>) request.getSession().getAttribute(HEAVY_WORK_SERVICE_RESULT);
		if (current != null && !current.isDone()) {
			logger.info("Cancelling current counter: " + current);
			current.cancel(true);
		}
		
	}
	
}
