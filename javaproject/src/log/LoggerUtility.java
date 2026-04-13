package log;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Utility class used to generate Log4j logger.
 * 
 * We can generate logs in a text or a html file.
 * 
 * @author Tianxiao.Liu@u-cergy.fr
 */
public class LoggerUtility {
	private static final String TEXT_LOG_CONFIG = "log/log4j-text.properties";
	private static final String HTML_LOG_CONFIG = "log/log4j-html.properties";

	public static Logger getLogger(Class<?> logClass, String logFileType) {
		String configPath;

	    if (logFileType.equals("text")) {
	        configPath = TEXT_LOG_CONFIG;
	    } else if (logFileType.equals("html")) {
	        configPath = HTML_LOG_CONFIG;
	    } else {
	        throw new IllegalArgumentException("Unknown log file type !");
	    }

	    InputStream is = Thread.currentThread()
	        .getContextClassLoader()
	        .getResourceAsStream(configPath);

	    if (is == null) {
	        throw new RuntimeException("Fichier log introuvable : " + configPath);
	    }

	    PropertyConfigurator.configure(is);

	    return Logger.getLogger(logClass.getName());
	}
}