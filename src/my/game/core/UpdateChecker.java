package my.game.core;

import java.io.*;
import java.net.*;

public class UpdateChecker {

	private UpdateChecker() {}
	
	public static void checkForUpdates(String version) {
		try {
			String url = "https://raw.githubusercontent.com/DropDemBits/ELEOS/master/versions.txt";
            URL link = new URL(url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(link.openStream()));
			String inputLine;
			UpdateScreen screen = null;
            
            while((inputLine = reader.readLine()) != null) {
				if(inputLine.contains("---newer---")) break;
				else if(inputLine.contains("#")) continue;
				if(inputLine.contains("distro")) {
					//Usb builds / Sneakernet Builds
					if(version.contains("distro")) {
						int versionNum = Integer.parseInt(version.substring(6));
						int newVersion = Integer.parseInt(inputLine.substring(6));
						if(versionNum >= newVersion) {break;}
						else if(versionNum < newVersion) {
							screen = new UpdateScreen(inputLine, version, true);
							break;
						}
					}
				}else {
					if(version.contains("distro")) {
						screen = new UpdateScreen(inputLine, version, true);
                        break;
					} else {
						boolean important = false;
						if(inputLine.contains("imp:")) {
							important = true;
							inputLine = inputLine.replace("imp:", "");
						}
						int[] cmpVer = getVersionNums(inputLine);
						int[] curVer = getVersionNums(version);
						if(cmpVer[0] > curVer[0] || cmpVer[1] > curVer[1] || (important && (cmpVer[2] > curVer[2] || cmpVer[3] > curVer[3]))) {
							screen = new UpdateScreen(inputLine, version, false);
							break;
						}
					}
				}
			}
			
            if(screen != null) {
                screen.display();
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
		int build = Integer.parseInt(verNums[1].split("b")[1].split("\\.")[0]);
        int subBuild = 0;
        if(verNums.length > 2) subBuild = Integer.parseInt(verNums[2]);
        else subBuild = 0;
		return new int[] {major, minor, build, subBuild};
	}
	
}
