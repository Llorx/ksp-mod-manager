package llorx.kspModManager;

import java.io.Serializable;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Mod implements Serializable {
	public static final int TYPE_NONE = -1;
	public static final int TYPE_SPACEPORT = 0;
	public static final int TYPE_KSPFORUM = 1;
	public static final int TYPE_JENKINS = 2;
	public static final int TYPE_GITHUB = 3;
	public static final int TYPE_BITBUCKET = 4;
	public static final int TYPE_DROPBOX_FOLDER = 5;
	public static final int TYPE_LINK = 6;
	
	private String id = "";
	private String name = "";
	private String status = "";
	private String version = "";
	private List<ModFile> installFiles = new ArrayList<ModFile>();
	private String link = "";
	private String downloadLink = "";
	public String downloadedFile = "";
	private boolean updatable = true;
	private int type = Mod.TYPE_NONE;
	public boolean isValid = false;
	
	public boolean nameChanged = false;
	
	public String getId() {
		String prefix = "";
		switch(this.getType()) {
			case Mod.TYPE_SPACEPORT:
				prefix = "sp_";
				break;
			case Mod.TYPE_KSPFORUM:
				prefix = "kf_";
				break;
			case Mod.TYPE_JENKINS:
				prefix = "jk_";
				break;
			case Mod.TYPE_GITHUB:
				prefix = "gh";
				break;
			case Mod.TYPE_BITBUCKET:
				prefix = "bb_";
				break;
			case Mod.TYPE_DROPBOX_FOLDER:
				prefix = "db_";
				break;
		}
		return prefix + this.id;
	}
	public String getUnprefixedId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public String getStatus() {
		return this.status;
	}
	public String getVersion() {
		return this.version;
	}
	public String getLink() {
		return this.link;
	}
	public String getDownloadLink() {
		return this.downloadLink;
	}
	public String getDownloadedFile() {
		return this.downloadedFile;
	}
	public boolean isUpdatable() {
		return this.updatable;
	}
	public int getType() {
		return this.type;
	}
	public List<ModFile> getInstalledFiles() {
		return this.installFiles;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.nameChanged = true;
		this.name = name;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public void setDownloadLink(String downloadLink) {
		this.downloadLink = downloadLink;
	}
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public boolean addInstalledFile(Path path, boolean updated) {
		return addInstalledFile(new ModFile(path, updated));
	}
	
	public boolean addInstalledFile(ModFile file) {
		boolean found = false;
		for (ModFile f: this.installFiles) {
			try {
				if (Files.isSameFile(f.getPath(), file.getPath())) {
					found = true;
					break;
				}
			} catch (Exception e) {
			}
		}
		if (found == false) {
			this.installFiles.add(file);
		}
		return !found;
	}
	
	public Mod(String name, String link) {
		this.setLink(link);
		this.setName(name);
		try {
			Response res = Http.get(link);
			if (res != null) {
				if (link.length() == 0) {
					this.setType(Mod.TYPE_NONE);
				} else {
					if (link.indexOf("kerbalspaceport.com/") > -1) {
						this.setType(Mod.TYPE_SPACEPORT);
					} else if (link.indexOf("forum.kerbalspaceprogram.com/threads/") > -1) {
						this.setType(Mod.TYPE_KSPFORUM);
						int index = link.indexOf("-");
						if (index > -1) {
							link = link.substring(0,index);
							this.setLink(link);
							res = Http.get(link);
						}
					} else if (link.indexOf("github.com/") > -1) {
						this.setType(Mod.TYPE_GITHUB);
						Document doc = res.parse();
						Element linkElement = doc.select("a[class=js-current-repository js-repo-home-link]").first();
						if (linkElement != null) {
							link = linkElement.attr("abs:href") + "/releases/latest";
							this.setLink(link);
							res = Http.get(link);
						}
					} else if (link.indexOf("bitbucket.org/") > -1) {
						this.setType(Mod.TYPE_BITBUCKET);
						Document doc = res.parse();
						Element linkElement = doc.select("a[id=repo-downloads-link]").first();
						if (linkElement != null) {
							link = linkElement.attr("abs:href");
							this.setLink(link);
							res = Http.get(link);
						}
					} else if (link.indexOf("dropbox.com/") > -1) {
						this.setType(Mod.TYPE_DROPBOX_FOLDER);
					} else {
						// Parse link to check if is Jenkins or simply a direct file
					}
					ModDataParser.parseModData(this, res);
				}
			}
		} catch (Exception e) {
		}
	}
};

class ModFile implements Serializable {
	private String path;
	private boolean updated;
	
	public ModFile(Path path, boolean update) {
		this.setPath(path);
		this.setUpdated(updated);
	}
	
	public Path getPath() {
		return Paths.get(this.path);
	}
	
	public boolean isUpdated() {
		return this.updated;
	}
	
	public void setPath(Path path) {
		String p = "";
		try {
			p = path.toFile().getCanonicalPath();
		} catch (Exception e) {
		}
		this.path = p;
	}
	
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
};