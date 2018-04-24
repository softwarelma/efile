package com.softwarelma.efile;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p3.disk.EpeDiskFinalList_files_recursive;
import com.sun.prism.Image;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.InMemoryDataProvider;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.server.Extension;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ItemClick;
import com.vaadin.ui.Tree.ItemClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;

public class EfileServer implements Serializable {

	private static final long serialVersionUID = 1L;
	private static EfileServer server = new EfileServer();
	private Map<String, EfileTreeData> mapPathAndTreeData;
	private Button buttonDownload;
	private Extension extension;
	//private String folderPath = "C:/Users/Maria Teresa Manca/Desktop/medcis/MEDCIS_NEW_1";// TODO
	private String folderPath = "C:/Users/Administrator/Desktop/MEDCIS";
	private String defaultFileName;

	public static EfileServer getInstance() throws EpeAppException {
		return EfileServer.server;
	}

	private EfileServer() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("log4j.properties").getFile());
		this.defaultFileName = file.getAbsolutePath();
	}

	// https://vaadin.com/docs/v8/framework/datamodel/datamodel-hierarchical.html
	public void loadPage(UI ui) throws EpeAppException {
		// ui.setContent(new Label("hw"));
		Label title = new Label("MEDCIS PROJECT LAYERS");
		Tree<EfileTreeData> tree = this.newTree();
		VerticalLayout layout = new VerticalLayout();
		this.newDownloadButton();
		layout.addComponent(title);
		layout.addComponents(tree, this.buttonDownload);
		ui.setContent(layout);

	}

	private Tree<EfileTreeData> newTree() throws EpeAppException {
		// 1
		Tree<EfileTreeData> tree = new Tree<>();
		this.addItemListener(tree);

		// 2
		TreeData<EfileTreeData> treeData = new TreeData<>();
		this.addItems(treeData);

		// 3
		this.addDataProvider(tree, treeData);

		return tree;
	}

	private void addItemListener(Tree<EfileTreeData> tree) {
		tree.addItemClickListener(new ItemClickListener<EfileTreeData>() {

			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(ItemClick<EfileTreeData> event) {
				setFileDownloader(event.getItem());
			}

		});
	}

	private void addItems(TreeData<EfileTreeData> treeData) throws EpeAppException {
		this.mapPathAndTreeData = new HashMap<>();
		List<String> listFileOrDir = EpeDiskFinalList_files_recursive.listFilesRecursive(this.folderPath);
		EfileTreeData itemParent, itemChild;
		Map.Entry<String, String> filePathAndName;
		String parent, child;
		File fileChild;

		for (String fileOrDir : listFileOrDir) {
			fileChild = new File(fileOrDir);
			if (!fileChild.isDirectory() && !fileChild.isFile())
				continue;
			filePathAndName = EpeAppUtils.retrieveFilePathAndName(fileOrDir);
			parent = EpeAppUtils.cleanDirName(filePathAndName.getKey());
			child = filePathAndName.getValue();
			itemChild = new EfileTreeData(fileChild.isDirectory(), parent, child);
			itemParent = this.mapPathAndTreeData.get(parent);// could be null
			treeData.addItem(itemParent, itemChild);

			if (fileChild.isDirectory()) {
				fileOrDir = EpeAppUtils.cleanDirName(fileOrDir);
				this.mapPathAndTreeData.put(fileOrDir, itemChild);
			}
		}
	}

	private void addDataProvider(Tree<EfileTreeData> tree, TreeData<EfileTreeData> treeData) {
		InMemoryDataProvider<EfileTreeData> inMemoryDataProvider = new TreeDataProvider<>(treeData);
		tree.setDataProvider(inMemoryDataProvider);
		// tree.expand("...");
	}

	private void setFileDownloader(EfileTreeData treeData) {
		if (treeData.isDir()) {
			this.buttonDownload.setEnabled(false);
			return;
		}

		this.buttonDownload.setEnabled(true);
		Resource res = new FileResource(new File(treeData.getFilePath() + treeData.getFileName()));
		FileDownloader fd = new FileDownloader(res);
		this.buttonDownload.removeExtension(this.extension);
		fd.extend(this.buttonDownload);
		this.extension = fd;
	}

	private void newDownloadButton() {
		this.buttonDownload = new Button("Download");
		this.buttonDownload.setEnabled(false);
		Resource res = new FileResource(new File(this.defaultFileName));
		FileDownloader fd = new FileDownloader(res);
		fd.extend(this.buttonDownload);
		this.extension = fd;
	}

}
