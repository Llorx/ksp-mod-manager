package llorx.kspModManager;

public class Strings {
	public static int MOD_NAME = 0;
	public static int LATEST_DATE = 1;
	public static int NEW_VERSION = 2;
	public static int NEW_UPDATE = 3;
	public static int DOWNLOAD_ERROR = 4;
	public static int DOWNLOAD_MOD = 5;
	public static int INSTALL_QUEUED = 6;
	public static int DOWNLOAD_MM = 7;
	public static int CHECK_MOD_UPDATES = 8;
	public static int NEW_NAME_QUESTION = 9;
	public static int NEW_LINK_QUESTION = 10;
	public static int ERROR_PARSING_LINK = 11;
	public static int LINK_CHANGED_DOWNLOAD_AGAIN = 12;
	public static int LINK_CHANGED_TITLE = 13;
	public static int DELETE_SURE = 14;
	public static int DELETE_MOD_TITLE = 15;
	public static int DELETE_MOD_COMPLETELY_ASK = 16;
	public static int DELETE_COMPLETELY_BUTTON = 17;
	public static int KEEP_VERSION_BUTTON = 18;
	public static int CANCEL_BUTTON = 19;
	public static int ERROR_MM = 20;
	public static int LOG_DIR_EMPTY = 21;
	public static int LOG_DIR_EMPTY_ERROR = 22;
	public static int LOG_OVERWRITTEN_FILES = 23;
	public static int LOG_OVERWRITTEN_FILES_ERROR = 24;
	public static int DOWNLOADING = 25;
	public static int INSTALL_QUEUE = 26;
	public static int ERROR_TEMP = 27;
	public static int ERROR_DOWNLOAD_LINK = 28;
	public static int SELECTION_REMEMBER = 29;
	public static int OVERWRITE_ASK = 30;
	public static int ADD_MOD_TITLE = 31;
	public static int OPEN_README = 32;
	public static int README_FILES_TITLE = 33;
	public static int MARK_INSTALL = 34;
	public static int INSTALL_GAMEDATA = 35;
	public static int INSTALL_AGAIN = 36;
	public static int INSTALLED_COUNT = 37;
	public static int EXTRACTING = 38;
	public static int INSTALLING = 39;
	public static int AFTERINSTALL_1 = 40;
	public static int AFTERINSTALL_2 = 41;
	public static int INSTALL_TITLE = 42;
	public static int CHECKING = 43;
	public static int UPDATED_QUANTITY = 44;
	public static int WARN_VERSION_CHECK = 45;
	public static int ALREADY_ERROR = 46;
	public static int DOWNLOAD_QUEUE = 47;
	public static int ERROR_MOD_URL = 48;
	public static int ERROR_UPDATING_CONFIG = 49;
	public static int MENU_RENAME = 50;
	public static int MENU_OPEN_BROWSER = 51;
	public static int MENU_CHANGE_LINK = 52;
	public static int MENU_REDOWNLOAD = 53;
	public static int MENU_CHECK_UPDATE = 54;
	public static int MENU_UNINSTALL = 55;
	public static int MENU_DISABLED = 56;
	public static int MENU_DOWNLOAD = 57;
	public static int MENU_REMOVE = 58;
	public static int NEW_MANAGER_UPDATE = 59;
	public static int NEW_MANAGER_UPDATE_TITLE = 60;
	public static int UPDATING_TEXT = 61;
	public static int UPDATING_TITLE = 62;
	public static int ERROR_UPDATING_MANAGER = 63;
	public static int ERROR_TITLE = 64;
	public static int BROWSER_BACK = 65;
	public static int LINK_DETECTED_ASK = 66;
	public static int LINK_DETECTED_TITLE = 67;
	public static int LINK_SELECTED_ASK = 68;
	public static int SURE_ASK = 69;
	public static int FILE_NOT_SUPPORTED = 70;
	public static int FILE_NOT_SUPPORTED_TITLE = 71;
	public static int CONFIG_MANAGER_TITLE = 72;
	public static int CHANGE_KSP_FOLDER = 73;
	public static int CHANGE_MM_LINK = 74;
	public static int CHANGE_MM_LINK_TEXT = 75;
	public static int URL_NOT_VALID = 76;
	public static int URL_UPDATED = 77;
	public static int UPDATED_TITLE = 78;
	public static int RESTORE_MM_LINK = 79;
	public static int URL_RESTORED = 80;
	public static int EXCLUDE_FILES = 81;
	public static int CONFIG_WARNING = 82;
	public static int EXCLUDE_MM = 83;
	public static int KSP_FOLDER_ERROR = 84;
	public static int SELECT_KSP_FOLDER = 85;
	public static int SELECT_KSP_FOLDER_TITLE = 86;
	public static int MULTIPLE_LINKS_ASK = 87;
	public static int ONE_LINK_DETECTED = 88;
	public static int NO_LINK_DETECTED = 89;
	public static int INSTALL_FILE_TITLE = 90;
	public static int DOWNLOAD_SELECTED_BUTTON = 91;
	public static int OPEN_BROWSER_DOWNLOAD = 92;
	public static int CHANGE_LANGUAGE = 93;
	public static int SELECT_LANGUAGE_TITLE = 94;
	public static int LANGUAGE_CHANGED_WARN = 95;
	public static int CONFIG_BUTTON = 96;
	public static int CUSTOM_LANGUAGE_INSTRUCTIONS = 97;
	
	public static String[] locales = {"en", "es"};
	public static String[] localeNames = {"English", "Spanish"};
	public static String[][] texts = {
		{ // EN = 0
			
			
	/* START EDITING HERE */
	/* DO NOT REMOVE ANY LINE OR IT WON'T WORK */
			
			
			"Mod name", // Strings.MOD_NAME
			"Latest date", // Strings.LATEST_DATE
			"New version installed", // Strings.NEW_VERSION
			"New update available", // Strings.NEW_UPDATE
			"Error downloading", // Strings.DOWNLOAD_ERROR
			"Download mod", // Strings.DOWNLOAD_MOD
			"Install Queued mods", // Strings.INSTALL_QUEUED
			"Download Module Manager", // Strings.DOWNLOAD_MM
			"Check mod updates", // Strings.CHECK_MOD_UPDATES
			"What is the new name?", // Strings.NEW_NAME_QUESTION
			"What is the new download link?", // Strings.NEW_LINK_QUESTION
			"Error parsing link.", // Strings.ERROR_PARSING_LINK
			"Mod link has changed. Do you want to download it from the new link now?", // Strings.LINK_CHANGED_DOWNLOAD_AGAIN
			"Link changed", // Strings.LINK_CHANGED_TITLE
			"Do you want to remove the mod from the list?", // Strings.DELETE_SURE
			"Delete Mod", // Strings.DELETE_MOD_TITLE
			"Do you want to delete it completely or only uninstall it but keep track of new versions?", // Strings.DELETE_MOD_COMPLETELY_ASK
			"Delete completely", // Strings.DELETE_COMPLETELY_BUTTON
			"Uninstall but keep in list to check versions", // Strings.KEEP_VERSION_BUTTON
			"Cancel", // Strings.CANCEL_BUTTON
			"Error adding Module Manager Mod.", // Strings.ERROR_MM
			"Error removing mod %MODNAME% because these directories were not empty:", // Strings.LOG_DIR_EMPTY
			"Could not remove some directories because they were not empty. Maybe the mod is merged with another mod or simply the mod created a config file on the fly.\nCheck those directories manually AFTER closing this app. You have the list in the log.txt file.", // Strings.LOG_DIR_EMPTY_ERROR
			"Error removing mod %MODNAME% because these files overwrited some other files", // Strings.LOG_OVERWRITTEN_FILES
			"Some files could not be removed because they replaced other mod files. You must check and decide if you remove them manually AFTER closing this app.\nYou have the list in the log.txt file.", // Strings.LOG_OVERWRITTEN_FILES_ERROR
			"Downloading", // Strings.DOWNLOADING
			"Install Queue", // Strings.INSTALL_QUEUE
			"Error accesing temp folder.", // Strings.ERROR_TEMP
			"Error getting download file.", // Strings.ERROR_DOWNLOAD_LINK
			"Remember this selection", // Strings.SELECTION_REMEMBER
			"Do you want to overwrite this file?", // Strings.OVERWRITE_ASK
			"Add new mod", // Strings.ADD_MOD_TITLE
			"Open README file (May have install instructions)", // Strings.OPEN_README
			"Readme files", // Strings.README_FILES_TITLE
			"Mark items to install", // Strings.MARK_INSTALL
			"Install this GameData", // Strings.INSTALL_GAMEDATA
			"Install again", // Strings.INSTALL_AGAIN
			"Installed %NUMBER% files.", // Strings.INSTALLED_COUNT
			"Extracting...", // Strings.EXTRACTING
			"Installing...", // Strings.INSTALLING
			"Found %GDATANUMBER% GameData folders", // Strings.AFTERINSTALL_1
			" and %READMENUMBER% README files.", // Strings.AFTERINSTALL_2
			"Install", // Strings.INSTALL_TITLE
			"Checking...", // Strings.CHECKING
			"Found %UPDATEDCOUNT% mods updated.", // Strings.UPDATED_QUANTITY
			"Do not install, only warn me when there's a new version.", // Strings.WARN_VERSION_CHECK
			"That mod already exists in the mod list under name: %MODNAME%", // Strings.ALREADY_ERROR
			"Download Queue", // Strings.DOWNLOAD_QUEUE
			"Error getting mod %MODNAME%. Check the URL.", // Strings.ERROR_MOD_URL
			"Error updating config. Try again in some minutes.", // Strings.ERROR_UPDATING_CONFIG
			"Rename", // Strings.MENU_RENAME
			"Open mod link in browser", // Strings.MENU_OPEN_BROWSER
			"Change mod link", // Strings.MENU_CHANGE_LINK
			"Redownload", // Strings.MENU_REDOWNLOAD
			"Check update", // Strings.MENU_CHECK_UPDATE
			"Uninstall", // Strings.MENU_UNINSTALL
			"Disabled because of Download/Install operations", // Strings.MENU_DISABLED
			"Download", // Strings.MENU_DOWNLOAD
			"Remove", // Strings.MENU_REMOVE
			"New update found. Install it now?", // Strings.NEW_MANAGER_UPDATE
			"New update", // Strings.NEW_MANAGER_UPDATE_TITLE
			"Updating... do not touch anything", // Strings.UPDATING_TEXT
			"Updating", // Strings.UPDATING_TITLE
			"Error updating. Download LMM manually.", // Strings.ERROR_UPDATING_MANAGER
			"Error", // Strings.ERROR_TITLE
			"Back", // Strings.BROWSER_BACK
			"Detected a %SERVERNAME% link\nDo you want to check version updates directly from there in the future?", // Strings.LINK_DETECTED_ASK
			"Detected link", // Strings.LINK_DETECTED_TITLE
			"Selected:\n%LINKCLICKED%\nAre you sure?", // Strings.LINK_SELECTED_ASK
			"Sure?", // Strings.SURE_ASK
			"This file is not a zip file. Not supported by Mod Manager right now. Select another one.", // Strings.FILE_NOT_SUPPORTED
			"File not supported", // Strings.FILE_NOT_SUPPORTED_TITLE
			"KSP Mod Manager config", // Strings.CONFIG_MANAGER_TITLE
			"Change KSP installation folder", // Strings.CHANGE_KSP_FOLDER
			"Change Module Manager download link", // Strings.CHANGE_MM_LINK
			"Paste the URL to download Module Manager dll", // Strings.CHANGE_MM_LINK_TEXT
			"That URL was not valid.", // Strings.URL_NOT_VALID
			"URL updated.", // Strings.URL_UPDATED
			"Updated", // Strings.UPDATED_TITLE
			"Restore Module Manager download link", // Strings.RESTORE_MM_LINK
			"URL restored.", // Strings.URL_RESTORED
			"Exclude unneeded files (Sources, Readmes, etc...)", // Strings.EXCLUDE_FILES
			"Only change this if you know what you are doing:", // Strings.CONFIG_WARNING
			"Exclude Module Manager dll's bundled in other mods", // Strings.EXCLUDE_MM
			"This does not seems a KSP main installation folder.", // Strings.KSP_FOLDER_ERROR
			"Please, select your KSP main installation folder.", // Strings.SELECT_KSP_FOLDER
			"KSP main folder", // Strings.SELECT_KSP_FOLDER_TITLE
			"Multiple links detected. What file do you want to install?", // Strings.MULTIPLE_LINKS_ASK
			"This file was detected, do you want to install it?", // Strings.ONE_LINK_DETECTED
			"No files detected. Download a file manually from the website.", // Strings.NO_LINK_DETECTED
			"Install file", // Strings.INSTALL_FILE_TITLE
			"Download selected file", // Strings.DOWNLOAD_SELECTED_BUTTON
			"Open website and download file from there", // Strings.OPEN_BROWSER_DOWNLOAD
			"Change Manager Language", // Strings.CHANGE_LANGUAGE
			"Select Language", // Strings.SELECT_LANGUAGE_TITLE
			"Language has changed. Restart the app so changes take effect completely.", // Strings.LANGUAGE_CHANGED_WARN
			"Configuration", // Strings.CONFIG_BUTTON
			"If you want to add your language, follow instructions in the KSP forum thread", // Strings.CUSTOM_LANGUAGE_INSTRUCTIONS
			
			
	/* STOP EDITING HERE. SEND IT TO ME :P */
			
			
		},{ // ES = 1
			"Nombre del mod", // Strings.MOD_NAME
			"Última fecha", // Strings.LATEST_DATE
			"Nueva versión instalada", // Strings.NEW_VERSION
			"Nueva actualización disponible", // Strings.NEW_UPDATE
			"Error descargando", // Strings.DOWNLOAD_ERROR
			"Descargar mod", // Strings.DOWNLOAD_MOD
			"Instalar mods en la cola", // Strings.INSTALL_QUEUED
			"Descargar Module Manager", // Strings.DOWNLOAD_MM
			"Comprobar actualizaciones", // Strings.CHECK_MOD_UPDATES
			"¿Cuál es el nuevo nombre?", // Strings.NEW_NAME_QUESTION
			"¿Cuál es el nuevo enlace?", // Strings.NEW_LINK_QUESTION
			"Error analizando enlace.", // Strings.ERROR_PARSING_LINK
			"El enlace del mod ha cambiado. ¿Quieres descargarlo desde el nuevo enlace ahora?", // Strings.LINK_CHANGED_DOWNLOAD_AGAIN
			"Enlace cambiado", // Strings.LINK_CHANGED_TITLE
			"¿Quieres eliminar el mod de la lista?", // Strings.DELETE_SURE
			"Eliminar mod", // Strings.DELETE_MOD_TITLE
			"¿Quieres erliminarlo completamente o desinstalarlo y dejarlo en la lista para el control de versiones?", // Strings.DELETE_MOD_COMPLETELY_ASK
			"Eliminar completamente", // Strings.DELETE_COMPLETELY_BUTTON
			"Desinstalarlo pero dejarlo en la lista para comprobar versiones", // Strings.KEEP_VERSION_BUTTON
			"Cancelar", // Strings.CANCEL_BUTTON
			"Error añadiendo el mod Module Manager", // Strings.ERROR_MM
			"Error borrando el mod %MODNAME% porque estos directorios no estaban vacíos:", // Strings.LOG_DIR_EMPTY
			"Ha sido imposible borrar algunos directorios porque no estaban vacíos. Es posible que el mod se instalara en la carpeta de otro mod o simplemente el mod ha creado un archivo de configuración mientras jugabas.\nComprueba esos directorios manualmente DESPUÉS de cerrar esta aplicación. Tienes la lista de directorios en el archivo log.txt.", // Strings.LOG_DIR_EMPTY_ERROR
			"Error borrando el mod %MODNAME% porque estos archivos sustituyeron a otros archivos", // Strings.LOG_OVERWRITTEN_FILES
			"Ha sido imposible borrar algunos archivos porque sustituyeron a otros archivos cuando se instalaron. Debes revisarlos manualmente y decidir si los borras DESPUÉS de cerrar esta aplicación.\nTienes la lista de archivos en el archivo log.txt.", // Strings.LOG_OVERWRITTEN_FILES_ERROR
			"Descargando", // Strings.DOWNLOADING
			"Cola de instalación", // Strings.INSTALL_QUEUE
			"Error accediendo al directorio temp.", // Strings.ERROR_TEMP
			"Error recibiendo el archivo de descarga.", // Strings.ERROR_DOWNLOAD_LINK
			"Reecordar esta selección", // Strings.SELECTION_REMEMBER
			"¿Quieres sustituir este archivo?", // Strings.OVERWRITE_ASK
			"Aádir nuevo mod", // Strings.ADD_MOD_TITLE
			"Abrir archivo LEEME (Puede contener instrucciones de instalación)", // Strings.OPEN_README
			"Archivos LEEME", // Strings.README_FILES_TITLE
			"Selecciona los elemetos a instalar", // Strings.MARK_INSTALL
			"Instalar este GameData", // Strings.INSTALL_GAMEDATA
			"Instalar de nuevo", // Strings.INSTALL_AGAIN
			"Instalados %NUMBER% archivos.", // Strings.INSTALLED_COUNT
			"Extrayendo...", // Strings.EXTRACTING
			"Instalando...", // Strings.INSTALLING
			"Encontrados %GDATANUMBER% directorios GameData", // Strings.AFTERINSTALL_1
			" y %READMENUMBER% archivos LEEME.", // Strings.AFTERINSTALL_2
			"Instalar", // Strings.INSTALL_TITLE
			"Comprobando...", // Strings.CHECKING
			"Encontrados %UPDATEDCOUNT% mods actualizados.", // Strings.UPDATED_QUANTITY
			"No instalar, sólo avisarme cuando haya una nueva versión.", // Strings.WARN_VERSION_CHECK
			"Este mod ya existe en la lista bajo el nombre: %MODNAME%", // Strings.ALREADY_ERROR
			"Cola de descarga", // Strings.DOWNLOAD_QUEUE
			"Error recibiendo mod %MODNAME%. Comprueba la URL.", // Strings.ERROR_MOD_URL
			"Error actualizando configuración. Prueba de nuevo en varios minutos.", // Strings.ERROR_UPDATING_CONFIG
			"Renombrar", // Strings.MENU_RENAME
			"Abrir enlace en el navegador", // Strings.MENU_OPEN_BROWSER
			"Cambiar enlance", // Strings.MENU_CHANGE_LINK
			"Redescargar", // Strings.MENU_REDOWNLOAD
			"Comprobar actualización", // Strings.MENU_CHECK_UPDATE
			"Desinstalar", // Strings.MENU_UNINSTALL
			"Desactivado debido a las operaciones de descarga/instalación", // Strings.MENU_DISABLED
			"Descargar", // Strings.MENU_DOWNLOAD
			"Eliminar", // Strings.MENU_REMOVE
			"Nueva actualización encontrada. Instalarla ahora?", // Strings.NEW_MANAGER_UPDATE
			"Nueva actualización", // Strings.NEW_MANAGER_UPDATE_TITLE
			"Actualizando... no toques nada", // Strings.UPDATING_TEXT
			"Actualizando", // Strings.UPDATING_TITLE
			"Error Actualizando. Descarga LMM manualmente.", // Strings.ERROR_UPDATING_MANAGER
			"Error", // Strings.ERROR_TITLE
			"Atrás", // Strings.BROWSER_BACK
			"Detectado un enlace de %SERVERNAME%\nQuieres comprobar desde ahí las nuevas actualizaciones a partir de ahora?", // Strings.LINK_DETECTED_ASK
			"Detectado enlace", // Strings.LINK_DETECTED_TITLE
			"Has seleccionado:\n%LINKCLICKED%\nEstás seguro?", // Strings.LINK_SELECTED_ASK
			"Seguro?", // Strings.SURE_ASK
			"Este archivo no es un archivo zip. No está soportado por LMM por ahora. Selecciona otro.", // Strings.FILE_NOT_SUPPORTED
			"Archivo no soportado", // Strings.FILE_NOT_SUPPORTED_TITLE
			"Configuración del LMM", // Strings.CONFIG_MANAGER_TITLE
			"Cambiar directorio de instalación del KSP", // Strings.CHANGE_KSP_FOLDER
			"Cambiar enlace de descarga del Module Manager", // Strings.CHANGE_MM_LINK
			"Pega la URL donde descargar Module Manager", // Strings.CHANGE_MM_LINK_TEXT
			"Esa URL no era válida.", // Strings.URL_NOT_VALID
			"URL actualizada.", // Strings.URL_UPDATED
			"Actualizada", // Strings.UPDATED_TITLE
			"Restablecer enlace de descarga del Module Manager", // Strings.RESTORE_MM_LINK
			"URL reestablecida.", // Strings.URL_RESTORED
			"Excluir archivos innecesarios (Sources, Readmes, etc...)", // Strings.EXCLUDE_FILES
			"Sólo cambia ésto si sabes lo que haces:", // Strings.CONFIG_WARNING
			"Excluir el dll del Module Manager incluido en otros mods", // Strings.EXCLUDE_MM
			"Ésto no parece un directorio de instalación del KSP.", // Strings.KSP_FOLDER_ERROR
			"Por favor, selecciona el directorio de instalación del KSP.", // Strings.SELECT_KSP_FOLDER
			"Directorio del KSP", // Strings.SELECT_KSP_FOLDER_TITLE
			"Múltiples enlaces detectados. ¿Cuál archivo quieres instalar?", // Strings.MULTIPLE_LINKS_ASK
			"Este archivo fue detectado ¿quieres instalarlo?", // Strings.ONE_LINK_DETECTED
			"No se han detectado archivos. Descarga un archivo manualmente desde la web.", // Strings.NO_LINK_DETECTED
			"Instalar archivo", // Strings.INSTALL_FILE_TITLE
			"Descargar archivo seleccionado", // Strings.DOWNLOAD_SELECTED_BUTTON
			"Abrir la página web y seleccionar un archivo desde ahí", // Strings.OPEN_BROWSER_DOWNLOAD
			"Cambiar idioma del Manager", // Strings.CHANGE_LANGUAGE
			"Selecciona idioma", // Strings.SELECT_LANGUAGE_TITLE
			"Has cambiado el idioma. Reinicia la aplicación para que tenga efecto completamente.", // Strings.LANGUAGE_CHANGED_WARN
			"Configuración", // Strings.CONFIG_BUTTON
			"Si quieres añadir tu idioma, sigue las instrucciones en el post en los foros del KSP", // Strings.CUSTOM_LANGUAGE_INSTRUCTIONS
		},
	};
	
	public static String get(int name) {
		return Strings.texts[ManagerConfig.locale][name];
	}
	
	public static void startUp() {
		int mainListCount = Strings.texts[0].length;
		for (int i = 0; i < Strings.texts.length; i++) {
			if (mainListCount > Strings.texts[i].length) {
				String[] list = new String[mainListCount];
				int ii = 0;
				for (ii = 0; ii < Strings.texts[i].length && Strings.texts[i][ii] != null; ii++) {
					list[ii] = Strings.texts[i][ii];
				}
				for (ii = ii; ii < mainListCount; ii++) {
					list[ii] = Strings.texts[0][ii];
				}
				Strings.texts[i] = list;
			}
			for (int ii = 0; ii < Strings.texts[i].length; ii++) {
				try {
					Strings.texts[i][ii] = new String(Strings.texts[i][ii].getBytes(), "UTF-8");
				} catch (Exception e) {
				}
			}
		}
	}
}