package cn.clickvalue.cv2.pages.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.velocity.runtime.parser.node.MathUtils;

import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.components.admin.BasePage;

public class FileBrowser extends BasePage {

	@Persist
	private String baseFolder;

	@Property
	private List<File> dirList = new ArrayList<File>();

	@Property
	private List<File> fileList = new ArrayList<File>();

	@Property
	private File file;

	@SuppressWarnings("unused")
	@Property
	private File dir;

	@Property
	private File base;

	@Persist
	private File current;

	private List<File> checkedFiles = new ArrayList<File>();

	@InjectPage
	private BatchFileUpload batchFileUpload;

	void onActivate(File file) {
		this.current = file;
	}

	void setupRender() {
		dirList.clear();
		fileList.clear();
		base = new File(RealPath.getRealPath(baseFolder));
		if (base.isDirectory()) {
			File[] files = base.listFiles();
			for (File file : files) {
				if (file.isHidden()) {

				} else if (file.isDirectory()) {
					dirList.add(file);
				}
			}
		}
		if (current == null) {
			current = new File(RealPath.getRealPath(baseFolder));
		}
		if (current.isDirectory()) {
			File[] files = current.listFiles();
			Arrays.sort(files, new Comparator<File>() {
				public int compare(File f1, File f2) {
					long sub = f2.lastModified() - f1.lastModified();
					return sub == 0 ? 0 : MathUtils.divide(sub, Math.abs(sub)).intValue();
				}
			});
			for (File file : files) {
				if (file.isHidden()) {

				} else if (file.isFile()) {
					fileList.add(file);
				}
			}
		}
	}

	void onPrepareForSubmit() {
		checkedFiles.clear();
	}

	Object onSubmit() {
		for (File file : checkedFiles) {
			file.delete();
		}
		return this;
	}

	Object onManage() {
		// return resources.createPageLink("admin/batchfileupload", false,
		// RealPath.getPathBaseOnRoot(current.getPath()));
		batchFileUpload.setFolder(RealPath.getPathBaseOnRoot(current.getPath()));
		return batchFileUpload;
	}

	public String getPath() {
		return RealPath.getPathBaseOnRoot(file.getPath());
	}

	public Date getLastModified() {
		return new Date(file.lastModified());
	}

	/**
	 * @return
	 * 
	 */
	public boolean isChecked() {
		return checkedFiles.contains(file);
	}

	/**
	 * @param checked
	 * 
	 */
	public void setChecked(boolean checked) {
		if (checked) {
			if (!checkedFiles.contains(file)) {
				checkedFiles.add(file);
			}
		} else {
			if (checkedFiles.contains(file)) {
				checkedFiles.remove(file);
			}
		}
	}

	public String getBaseFolder() {
		return baseFolder;
	}

	public void setBaseFolder(String baseFolder) {
		this.baseFolder = baseFolder;
	}

	public File getCurrent() {
		return current;
	}

	public void setCurrent(File current) {
		this.current = current;
	}
}
