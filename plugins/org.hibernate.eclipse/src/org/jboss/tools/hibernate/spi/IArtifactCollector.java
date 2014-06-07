package org.jboss.tools.hibernate.spi;

import java.io.File;
import java.util.Set;

public interface IArtifactCollector {

	Set<String> getFileTypes();
	void formatFiles();
	File[] getFiles(String string);

}
