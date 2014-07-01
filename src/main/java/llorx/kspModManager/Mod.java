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

import java.util.UUID;

public class Mod implements Serializable {
	static final long serialVersionUID = 2874434645244941775L;
	
	public static final int TYPE_NONE = -1;
	public static final int TYPE_SPACEPORT = 0;
	public static final int TYPE_KSPFORUM = 1;
	public static final int TYPE_JENKINS = 2;
	public static final int TYPE_GITHUB = 3;
	public static final int TYPE_BITBUCKET = 4;
	public static final int TYPE_DROPBOX_FOLDER = 5;
	public static final int TYPE_CURSEFORGE = 6;
	public static final int TYPE_CURSE = 7;
	public static final int TYPE_LINK = 1000;
	
	
	private UUID uniqueId = UUID.randomUUID();
	private String id = "";
	private String name = "";
	private String status = "";
	private String version = "";
	private List<ModFile> installFiles = new ArrayList<ModFile>();
	private String link = "";
	private String downloadLink = "";
	private boolean updatable = true;
	private int type = Mod.TYPE_NONE;
	private boolean installable = false;
	private boolean saved = false;
	
	public boolean isMM = false;
	
	public boolean isValid = false;
	
	private Date lastDate;
	
	public transient String downloadedFile = "";
	public transient boolean nameChanged = false;
	public transient boolean justUpdated = false;
	public transient boolean errorUpdate = false;
	private transient boolean working = false;
	public transient boolean stopWork = false;
	
	// Gets
	public UUID getUniqueId() {
		return uniqueId;
	}
	public String getPrefix() {
		String prefix = "";
		switch(this.getType()) {
			case Mod.TYPE_SPACEPORT:
				prefix = "SpacePort";
				break;
			case Mod.TYPE_KSPFORUM:
				prefix = "KspForum";
				break;
			case Mod.TYPE_JENKINS:
				prefix = "Jenkins";
				break;
			case Mod.TYPE_GITHUB:
				prefix = "GitHub";
				break;
			case Mod.TYPE_BITBUCKET:
				prefix = "BitBucket";
				break;
			case Mod.TYPE_DROPBOX_FOLDER:
				prefix = "Dropbox";
				break;
			case Mod.TYPE_CURSEFORGE:
				prefix = "CurseForge";
				break;
			case Mod.TYPE_CURSE:
				prefix = "Curse.com";
				break;
		}
		return prefix;
	}
	public String getId() {
		return this.getPrefix() + "_" + this.id;
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
	public boolean isInstallable() {
		return this.installable;
	}
	public boolean isSaved() {
		return this.saved;
	}
	public Date getLastDate() {
		return this.lastDate;
	}
	
	// Sets
	public void setUniqueId(UUID uniqueId) {
		this.uniqueId = uniqueId;
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
	public void setInstallable(boolean installable) {
		this.installable = installable;
	}
	public void setSaved(boolean saved) {
		this.saved = saved;
	}
	public void setLastDate(Date date) {
		this.lastDate = date;
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
	public void clearInstalledFiles() {
		this.installFiles = new ArrayList<ModFile>();
	}
	
	public boolean checkVersion() {
		String oldVersion = this.getVersion();
		this.reloadMod(this.getLink());
		return !oldVersion.equals(this.getVersion());
	}
	
	public void setWork(boolean work, Object lock) {
		synchronized(lock) {
			this.working = work;
			if (this.working == false) {
				lock.notify();
			}
		}
	}
	public boolean getWork() {
		return this.working;
	}
	
	public boolean continueWork(Object lock) {
		synchronized(lock) {
			if (this.stopWork == true) {
				lock.notify();
				return false;
			}
			return true;
		}
	}
	public void stopWork(Object lock) {
		synchronized(lock) {
			this.stopWork = true;
			while (this.working == true) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
				}
			}
			this.stopWork = false;
		}
	}
	public void reloadMod(String link) {
		this.reloadMod(this.getName(), link, this.isInstallable());
	}
	public void reloadMod(String name, String link, boolean installable) {
		this.setInstallable(installable);
		this.setLink(link);
		this.setDownloadLink("");
		this.setName(name);
		this.isValid = false;
		try {
			if (link.length() == 0) {
				this.setType(Mod.TYPE_NONE);
			} else {
				Response res = null;
				if (link.indexOf("kerbalspaceport.com/") > -1) {
					this.setType(Mod.TYPE_SPACEPORT);
				} else if (link.indexOf("forum.kerbalspaceprogram.com/threads/") > -1) {
					this.setType(Mod.TYPE_KSPFORUM);
				} else if (link.indexOf("github.com/") > -1) {
					this.setType(Mod.TYPE_GITHUB);
				} else if (link.indexOf("bitbucket.org/") > -1) {
					this.setType(Mod.TYPE_BITBUCKET);
				} else if (link.indexOf("dropbox.com/") > -1) {
					this.setType(Mod.TYPE_DROPBOX_FOLDER);
				} else if (link.indexOf("kerbal.curseforge.com/") > -1) {
					this.setType(Mod.TYPE_CURSEFORGE);
				} else if (link.indexOf("curse.com/") > -1) {
					this.setType(Mod.TYPE_CURSE);
				} else {
					if (Http.fileType(link) == Http.HTML) {
						res = Http.get(link);
						Document doc = res.parse();
						Element el = doc.select("img[src$=search.png]").first();
						if (el != null) {
							el = el.parent();
							if (el != null) {
								String href = el.attr("abs:href");
								if (!href.equals("")) {
									if (href.indexOf("lastSuccessfulBuild") < 0) {
										href = href + "/lastSuccessfulBuild";
									}
									res = Http.get(href + "/api/xml");
									if (res.statusCode() == 200) {
										this.setType(Mod.TYPE_JENKINS);
										link = href;
										this.setLink(link);
									}
								}
							}
						}
					}
					if (this.getType() == Mod.TYPE_NONE) {
						this.setType(Mod.TYPE_LINK);
					}
				}
				ModDataParser.parseModData(this, res);
			}
		} catch (Exception e) {
		}
	}
	public Mod(String name, String link, boolean installable) {
		this.reloadMod(name, link, installable);
		this.setLastDate(new Date());
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