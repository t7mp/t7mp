package com.googlecode.t7mp;

import com.googlecode.t7mp.scanner.Scanner;

/**
 * 
 * @author jbellmann
 *
 */
public interface ShutdownHook {
    
    void addScanner(Scanner scanner);
    
    void stopScanners();

}
