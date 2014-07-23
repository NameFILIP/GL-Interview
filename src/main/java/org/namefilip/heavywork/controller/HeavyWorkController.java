package org.namefilip.heavywork.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.namefilip.heavywork.model.CounterData;
import org.namefilip.heavywork.service.HeavyWorkService;
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
	
//	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String HEAVY_WORK_SERVICE_RESULT = "heavyWorkServiceResult";
	private static final String RUNNING = "Running";
	
	@Autowired
	HeavyWorkService heavyWorkService;
	
	
	/**
	 * 
	 * Handles the POST request and lets HeavyWorkService to start processing
	 * 
	 * @param counterData - contains data submitted by user
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value="/start", method = RequestMethod.POST)
	public void count(@RequestBody CounterData counterData, HttpServletRequest request) {
		Future<String> result = heavyWorkService.count(counterData.getCountTo());
		request.getSession().setAttribute(HEAVY_WORK_SERVICE_RESULT, result);
	}
	
	/**
	 * If the counter is running it returns a string "Running".
	 * If the task has been successfully completed, it returns the result of the computation.
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/status", method = RequestMethod.GET)
	public String status(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Future<String> result = (Future<String>) request.getSession().getAttribute(HEAVY_WORK_SERVICE_RESULT);
        if (result == null) {
        	throw new RuntimeException("Counter hasn't been started");
        }
        if (result.isDone()) {
        	try {
        		return result.get();
            }
            catch (InterruptedException | ExecutionException e) {
            	throw new RuntimeException("Error has occured", e);
            }
        } else {
        	return RUNNING;
        }
	}
	
	
}
