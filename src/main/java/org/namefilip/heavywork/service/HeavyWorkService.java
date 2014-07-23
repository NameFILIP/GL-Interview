package org.namefilip.heavywork.service;

import java.util.concurrent.Future;

/**
* @author Filip Spiridonov
*/
public interface HeavyWorkService {
	
	Future<String> count(Integer number);
}
