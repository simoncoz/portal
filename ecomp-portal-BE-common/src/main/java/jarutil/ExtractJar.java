/*-
 * ============LICENSE_START==========================================
 * ONAP Portal
 * ===================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * 
 */
package jarutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;


public class ExtractJar {

	public static final int bufferSize = 8192;
	public static final String jarFile = "raptor_upgrade.jar";
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ExtractJar.class);

	public static void main(String[] args) throws Exception {
		if (args.length > 0 && args[0] != null && args[0].length() > 0)
			extractFilesFromJar(args[0]);
		else {
			logger.info("Current Directory is taken as webapp path");
			String currentDir = new File(".").getAbsolutePath();
			extractFilesFromJar(currentDir);
		}
	}

	public static void extractFilesFromJar(String directory) throws IOException {
	
		Class clazz = ExtractJar.class;
		String classContainer = clazz.getProtectionDomain().getCodeSource().getLocation().toString();
		URL jarUrl = clazz.getProtectionDomain().getCodeSource().getLocation();

		try(JarInputStream entryStream = new JarInputStream(jarUrl.openStream())){
			JarEntry entry;
			while (true) {
				entry = entryStream.getNextJarEntry();
				if (entry == null)
					break;
				if (entry.getName().indexOf("jarutil") < 0) {
					logger.info(entry.getName());
					File file = new File(directory, entry.getName());
					if (entry.isDirectory()) {
						if (!file.exists())
							file.mkdirs();
					} else {
						// make directory (some jars don't list dirs)
						File dir = new File(file.getParent());
						if (!dir.exists())
							dir.mkdirs();
						if (file.exists())
							file.delete();
						// Make file
						FileOutputStream fout = new FileOutputStream(file);
						copy(entryStream, fout);
						fout.close();
	
						// touch the file.
						if (entry.getTime() >= 0)
							file.setLastModified(entry.getTime());
					}
	
				}
				entryStream.closeEntry();
			}
			System.out.println("************************************************");
			System.out.println("*                                              *");
			System.out.println("*                                              *");
			System.out.println("*          RAPTOR SETUP COMPLETE.              *");
			System.out.println("*                                              *");
			System.out.println("*         Thank you for upgrading.             *");
			System.out.println("*                                              *");
			System.out.println("************************************************");
		}catch(Exception e) {
			logger.error("Exception in extractFilesFromJar",e);
		}
		
	}

	public static void copy(InputStream in, OutputStream out, long byteCount) throws IOException {
		byte[] buffer = new byte[bufferSize];
		int len = bufferSize;
		if (byteCount >= 0) {
			while (byteCount > 0) {
				if (byteCount < bufferSize)
					len = in.read(buffer, 0, (int) byteCount);
				else
					len = in.read(buffer, 0, bufferSize);
				if (len == -1)
					break;

				byteCount -= len;
				out.write(buffer, 0, len);
			}
		} else {
			while (true) {
				len = in.read(buffer, 0, bufferSize);
				if (len < 0)
					break;
				out.write(buffer, 0, len);
			}
		}
	}

	/* ------------------------------------------------------------------- */
	/**
	 * Copy Reader to Writer for byteCount bytes or until EOF or exception.
	 */
	public static void copy(Reader in, Writer out, long byteCount) throws IOException {
		char[] buffer = new char[bufferSize];
		int len = bufferSize;
		if (byteCount >= 0) {
			while (byteCount > 0) {
				if (byteCount < bufferSize)
					len = in.read(buffer, 0, (int) byteCount);
				else
					len = in.read(buffer, 0, bufferSize);

				if (len == -1)
					break;
				byteCount -= len;
				out.write(buffer, 0, len);
			}
		} else {
			while (true) {
				len = in.read(buffer, 0, bufferSize);
				if (len == -1)
					break;
				out.write(buffer, 0, len);
			}
		}
	}

	/* ------------------------------------------------------------------- */
	/**
	 * Copy Stream in to Stream out until EOF or exception.
	 */
	public static void copy(InputStream in, OutputStream out) throws IOException {
		copy(in, out, -1);
	}

	// Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns false.
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

}
