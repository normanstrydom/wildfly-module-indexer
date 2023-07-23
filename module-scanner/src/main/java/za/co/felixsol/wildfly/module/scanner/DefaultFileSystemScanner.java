package za.co.felixsol.wildfly.module.scanner;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import za.co.felixsol.wildfly.module.scanner.data.WildflyModule;

public class DefaultFileSystemScanner implements PathScanner {

	private Path modulePath;

	public DefaultFileSystemScanner(String modulePath) {
		this.modulePath = Paths.get(modulePath);
		System.out.println(this.modulePath);
	}

	public void execute() throws IOException {
		List<Path> paths = listFolders(modulePath);
		for (Path path : paths) {
			WildflyModule module = getModule(path);
			if (module != null) {
				System.out.println(path);
				List<Path> jars = listJars(path);
				jars.stream().forEach(e -> listZipContents(e.toFile().getAbsolutePath()));
			}
		}
	}

	@Override
	public List<Path> listFolders(Path path) throws IOException {
		List<Path> result = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, new DirectoryStream.Filter<Path>() {

			@Override
			public boolean accept(Path entry) throws IOException {
				return Files.isDirectory(entry);
			}

		})) {
			for (Path dirPath : stream) {
				result.add(dirPath);
				result.addAll(listFolders(dirPath));
			}
		}
		return result;
	}

	@Override
	public List<Path> listJars(Path path) throws IOException {
		List<Path> result = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.jar")) {
			for (Path dirPath : stream) {
				if (!Files.isDirectory(dirPath)) {
					result.add(dirPath);
				}
			}
		}
		return result;
	}

	@Override
	public za.co.felixsol.wildfly.module.scanner.data.WildflyModule getModule(Path path) {
		Path xml = path.resolve("module.xml");
		if (Files.exists(xml, LinkOption.NOFOLLOW_LINKS)) {
			return new WildflyModule();
		}
		return null;
	}

	public static void listZipContents(String zipFilePath) {
		try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
			ZipEntry entry;

			while ((entry = zipInputStream.getNextEntry()) != null) {
				if (!entry.isDirectory()) {
					System.out.println(entry.getName());
				}
				zipInputStream.closeEntry();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
