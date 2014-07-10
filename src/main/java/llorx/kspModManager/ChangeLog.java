package llorx.kspModManager;

import java.util.LinkedHashSet;

class ChangeLog {
	private static String[][] changes = {
		{
			"Better Changelog output when updating: Will output changes according to the previous version.",
			"Download filename fix: No more empty downloaded files (finally...)",
			"Bandwith/Connections optimizing: Less connections to get download files.",
		},{
			"Minor mediafire download fix.",
		},{
			"Added German language.",
		},{
			"Export modlist to .txt file to show your modlist (Import still not implemented).",
		},{
			"Uninstall fix.",
		},{
			"kerbal-space-parts.com support added.",
		},
	};
	
	public static String get(int oldVersion) {
		LinkedHashSet<String> log = new LinkedHashSet<String>();
		for (int i = oldVersion; i < ChangeLog.changes.length; i++) {
			if (ChangeLog.changes[i] != null) {
				for (int ii = 0; ii < ChangeLog.changes[i].length; ii++) {
					if (ChangeLog.changes[i][ii] != null && ChangeLog.changes[i][ii].length() > 0) {
						log.add(" - " + ChangeLog.changes[i][ii]);
					}
				}
			}
		}
		String logString = "";
		for (String logLine: log) {
			logString = logString + "\n" + logLine;
		}
		return logString;
	}
	
	public static boolean anyChanges(int oldVersion) {
		return getVersion() > oldVersion;
	}
	
	public static int getVersion() {
		return countNotNulls();
	}
	
	private static int countNotNulls() {
		int c = 0;
		for (int i = 0; i < ChangeLog.changes.length; i++) {
			if (ChangeLog.changes[i] != null) {
				c++;
			}
		}
		return c;
	}
}