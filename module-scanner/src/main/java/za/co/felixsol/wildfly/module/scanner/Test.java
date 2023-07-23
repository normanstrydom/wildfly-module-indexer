package za.co.felixsol.wildfly.module.scanner;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		DefaultFileSystemScanner scanner = new DefaultFileSystemScanner("F:/wildfly-29.0.0.Final/modules");
		scanner.execute();
		System.out.println("Done!");
	}

}
