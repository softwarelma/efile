package com.softwarelma.efile;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public class EfileTreeData {

    private final boolean dir;

    /**
     * absolute or relative
     */
    private final String filePath;

    /**
     * only the file name, no path involved
     */
    private final String fileName;

    public EfileTreeData(boolean dir, String filePath, String fileName) throws EpeAppException {
        EpeAppUtils.checkNull("filePath", filePath);
        EpeAppUtils.checkNull("fileName", fileName);
        this.dir = dir;
        this.filePath = filePath;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return this.fileName;
    }

    public boolean isDir() {
        return dir;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

}
