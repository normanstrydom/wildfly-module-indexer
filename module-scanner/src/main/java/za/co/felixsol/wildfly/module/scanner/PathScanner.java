package za.co.felixsol.wildfly.module.scanner;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import za.co.felixsol.wildfly.module.scanner.data.WildflyModule;

public interface PathScanner {

	List<Path> listFolders(Path path) throws IOException;

	List<Path> listJars(Path path) throws IOException;

	WildflyModule getModule(Path path);

}
