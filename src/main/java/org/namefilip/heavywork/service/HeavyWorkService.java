package org.namefilip.heavywork.service;

import java.util.concurrent.Future;

import javax.servlet.http.HttpSession;

/**
* @author Filip Spiridonov
*/
public interface HeavyWorkService {
	
	Future<String> count(Integer number, HttpSession session);
}
