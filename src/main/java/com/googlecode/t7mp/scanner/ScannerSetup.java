package com.googlecode.t7mp.scanner;

import java.io.File;

import com.googlecode.t7mp.AbstractT7Mojo;
import com.googlecode.t7mp.TomcatShutdownHook;

public final class ScannerSetup {

    private ScannerSetup() {
        //hide constructor
    }

    public static void configureScanners(TomcatShutdownHook shutdownHook, AbstractT7Mojo t7Mojo) {
        if (!t7Mojo.isWebProject()) {
            t7Mojo.getLog()
                    .info("Project seems not to be an web-project (packaging 'war'), skip scanner configuration");
            return;
        }
        for (ScannerConfiguration scannerConfiguration : t7Mojo.getScanners()) {
            scannerConfiguration.setRootDirectory(t7Mojo.getWebappSourceDirectory());
            scannerConfiguration.setWebappDirectory(new File(t7Mojo.getCatalinaBase(), "webapps/" + t7Mojo.getBuildFinalName()));
            Scanner scanner = new Scanner(scannerConfiguration, t7Mojo.getLog());
            scanner.start();
            shutdownHook.addScanner(scanner);
        }
        if (t7Mojo.isScanClasses()) {
            ScannerConfiguration scannerConfiguration = new ScannerConfiguration();
            scannerConfiguration.setRootDirectory(t7Mojo.getWebappClassDirectory());
            scannerConfiguration.setWebappDirectory(new File(t7Mojo.getCatalinaBase(), "webapps/" + t7Mojo.getBuildFinalName()
                    + "/WEB-INF/classes"));
            scannerConfiguration.setEndings("%"); // it's all or nothing
            Scanner scanner = new Scanner(scannerConfiguration, t7Mojo.getLog());
            scanner.start();
            shutdownHook.addScanner(scanner);
        }
    }

}
