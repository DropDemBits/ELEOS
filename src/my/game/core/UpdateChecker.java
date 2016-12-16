package my.game.core;

import java.io.*;
import java.net.*;

public class UpdateChecker {

	private UpdateChecker() {}
	
	public static void checkForUpdates(String version) {
		//url: https://raw.githubusercontent.com/xDIAMONDSx/xDIAMONDSx.github.io/master/eleos/versions.txt
		try {
			String url = "https://raw.githubusercontent.com/xDIAMONDSx/ELEOS/master/versions.txt";
			//"https://raw.githubusercontent.com/xDIAMONDSx/xDIAMONDSx.github.io/master/eleos/versions.txt";
			URL link = new URL(url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(link.openStream()));
			String inputLine;
			while((inputLine = reader.readLine()) != null) {
				if(inputLine.contains("---newer---")) return;
				else if(inputLine.contains("#"));
				if(inputLine.contains("distro")) {
					//usb builds
					if(version.contains("distro")) {
						int versionNum = Integer.parseInt(version.substring(6));
						int newVersion = Integer.parseInt(inputLine.substring(6));
						if(versionNum >= newVersion) {continue;}
						else if(versionNum < newVersion) {
							new UpdateScreen(inputLine, version);
							return;
						}
					}
				}else {
					if(version.contains("distro")) {
						new UpdateScreen(inputLine, version);
					}else {
						boolean important = false;
						if(inputLine.contains("imp:")) {
							important = true;
							inputLine.replace("imp:", "");
						}
						int[] cmpVer = getVersionNums(inputLine);
						int[] curVer = getVersionNums(inputLine);
						if(cmpVer[0] > curVer[0] || cmpVer[1] > curVer[1] || (important && cmpVer[2] > curVer[2])) {
							new UpdateScreen(inputLine, version);
							return;
						}
					}
				}
			}
			
		}catch(FileNotFoundException nope) {
			//Slience
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static int[] getVersionNums(String in) {
		String[] verNums = in.split("\\.");
		int major = Integer.parseInt(verNums[0]);
		int minor = Integer.parseInt(verNums[1].split("b")[0]);
		int build = Integer.parseInt(verNums[1].split("b")[1]);
		return new int[] {major, minor, build};
	}
	
}
