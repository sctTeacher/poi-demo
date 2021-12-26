//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shan.file;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.shan.entity.bean.FileInfo;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
@Component
public class CommonUploader implements ResumeUploader {
    @Value("${spring.uploader.tempdir}")
    private String tempDir;
    @Value("${spring.uploader.isDelTempFile}")
    private Boolean isDelTempFile;

    public Boolean checkChunk(String sharePath, String md5File, Integer chunk) {
        Boolean isExisted = false;
        String filePath = String.format("%s/%s/%s/%s.tmp", sharePath, this.tempDir, md5File, chunk);
        File file = new File(filePath);
        if (file.exists()) {
            isExisted = true;
        }

        return isExisted;
    }

    public Boolean compress(List<String> inputFilename, String zipFilename) {
        Boolean result = false;

        try {
            this.mkDir(zipFilename.substring(0, zipFilename.lastIndexOf("/") + 1));
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));

            try {
                String item;
                String base;
                for(Iterator var5 = inputFilename.iterator(); var5.hasNext(); this.zip(out, item, base)) {
                    item = (String)var5.next();
                    base = "";
                    String[] items = item.split("/");
                    if (item.lastIndexOf("/") + 1 == item.length()) {
                        base = items[items.length - 1] + "/";
                    } else {
                        base = items[items.length - 1];
                    }
                }

                result = true;
            } catch (Throwable var10) {
                try {
                    out.close();
                } catch (Throwable var9) {
                    var10.addSuppressed(var9);
                }

                throw var10;
            }

            out.close();
        } catch (Exception var11) {
            var11.printStackTrace();
            result = false;
        }

        return result;
    }

    public Boolean copy(String fromPath, String tofilePath) {
        Boolean result = false;

        try {
            File fromFile = new File(fromPath);
            File toFile = new File(tofilePath);
            if (fromFile.exists()) {
                if (fromFile.isDirectory()) {
                    FileUtils.copyDirectory(fromFile, toFile);
                } else {
                    FileUtils.copyFile(fromFile, toFile);
                }

                result = toFile.exists();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
            result = false;
        }

        return result;
    }

    public Boolean delete(String path) {
        Boolean result = false;

        try {
            File remoteFile = new File(path);
            if (!remoteFile.exists()) {
                return false;
            }

            if (remoteFile.isFile()) {
                result = remoteFile.delete();
            } else {
                FileUtils.deleteDirectory(remoteFile);
                result = true;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            result = false;
        }

        return result;
    }

    public InputStream download(String fromPath) {
        FileInputStream inputStream = null;

        try {
            File file = new File(fromPath);
            if (file.exists()) {
                inputStream = new FileInputStream(file);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return inputStream;
    }

    public Boolean exists(String path) {
        return (new File(path)).exists();
    }

    public Long getDiskSize(String path) {
        return this.getDiskSizeByte(path) / 1024L;
    }

    private Long getDiskSizeByte(String path) {
        long result = 0L;

        try {
            File file = new File(path);
            if (file.isFile()) {
                result += file.length();
            } else {
                File[] files = file.listFiles();
                File[] var6 = files;
                int var7 = files.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    File f = var6[var8];
                    if (f.isFile()) {
                        result += f.length();
                    }

                    if (f.isDirectory()) {
                        result += this.getDiskSizeByte(f.getPath().substring(f.getPath().indexOf("@") + 1));
                    }
                }
            }
        } catch (Exception var10) {
            var10.printStackTrace();
            return 0L;
        }

        return result;
    }

    public List<FileInfo> listFiles(String path, boolean calculateFolderSize) {
        ArrayList result = Lists.newArrayList();

        try {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                File[] var6 = files;
                int var7 = files.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    File f = var6[var8];
                    Boolean isFile = f.isFile();
                    String filePath = f.getPath();
                    String fileName = isFile ? f.getName() : "";
                    Long size = 0L;
                    if (isFile) {
                        size = f.length() / 1024L;
                    } else if (calculateFolderSize) {
                        size = this.getDiskSize(filePath.substring(filePath.indexOf("@") + 1));
                    }

                    result.add(new FileInfo(f.getName(), isFile, f.getPath(), fileName.substring(fileName.lastIndexOf(".") + 1), size));
                }
            }
        } catch (Exception var14) {
            var14.printStackTrace();
        }

        return result;
    }

    public Boolean merge(String uploadPath, String temPath, String md5File, Integer chunks, String name) {
        Boolean result = false;
        this.mkDir(uploadPath);
        this.mkDir(temPath);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(String.format("%s/%s", uploadPath, name));

            try {
                byte[] buf = new byte[104857600];
                long i = 0L;

                while(true) {
                    if (i >= (long)chunks) {
                        if (this.isDelTempFile) {
                            this.delete(String.format("%s/%s/%s/", temPath, this.tempDir, md5File));
                        }

                        result = true;
                        break;
                    }

                    File file = new File(String.format("%s/%s/%s/%s.tmp", temPath, this.tempDir, md5File, i));
                    FileInputStream inputStream = new FileInputStream(file);

                    try {
                        boolean var13 = false;

                        int len;
                        while((len = inputStream.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, len);
                        }
                    } catch (Throwable var17) {
                        try {
                            inputStream.close();
                        } catch (Throwable var16) {
                            var17.addSuppressed(var16);
                        }

                        throw var17;
                    }

                    inputStream.close();
                    ++i;
                }
            } catch (Throwable var18) {
                try {
                    fileOutputStream.close();
                } catch (Throwable var15) {
                    var18.addSuppressed(var15);
                }

                throw var18;
            }

            fileOutputStream.close();
        } catch (Exception var19) {
            result = false;
        }

        return result;
    }

    public Boolean mkDir(String path) {
        Boolean result = false;

        try {
            File file = new File(path);
            if (!file.exists()) {
                result = file.mkdirs();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
            result = false;
        }

        return result;
    }

    public Boolean move(String fromPath, String toPath) {
        Boolean result = false;

        try {
            Boolean isCopy = this.copy(fromPath, toPath);
            Boolean isDel = false;
            if (isCopy) {
                isDel = this.delete(fromPath);
            }

            result = isCopy && isDel;
        } catch (Exception var6) {
            var6.printStackTrace();
            result = false;
        }

        return result;
    }

    public Boolean rename(String oldName, String newName) {
        Boolean result = false;

        try {
            File oldFile = new File(oldName);
            File newFile = new File(newName);
            oldFile.renameTo(newFile);
            result = newFile.exists();
        } catch (Exception var6) {
            var6.printStackTrace();
            result = false;
        }

        return result;
    }

    public Boolean upload(MultipartFile file, String sharePath, String md5File, Integer chunk) {
        try {
            String path = String.format("%s/%s/%s/", sharePath, this.tempDir, md5File);
            FileUtils.forceMkdir(new File(path));
            String chunkName = chunk == null ? "0.tmp" : chunk.toString().concat(".tmp");
            File savefile = new File(path.concat(chunkName));
            if (!savefile.exists()) {
                savefile.createNewFile();
            }

            file.transferTo(savefile);
        } catch (IOException var8) {
            return false;
        }

        return true;
    }

    private void zip(ZipOutputStream out, String inputFilename, String base) throws Exception {
        File file = new File(inputFilename);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                base = base.length() == 0 ? "" : base;

                for(int i = 0; i < files.length; ++i) {
                    String filePath = files[i].getPath();
                    this.zip(out, filePath.substring(filePath.indexOf("@") + 1), base + files[i].getName());
                }
            } else {
                out.putNextEntry(new ZipEntry(base));
                int len = 0;
                byte[] buf = new byte[104857600];
                FileInputStream inputStream = new FileInputStream(file);
                try {
                    while((len = inputStream.read(buf)) != -1) {
                        out.write(buf, 0, len);
                    }
                } catch (Throwable var11) {
                    try {
                        inputStream.close();
                    } catch (Throwable var10) {
                        var11.addSuppressed(var10);
                    }

                    throw var11;
                }

                inputStream.close();
            }
        }

    }

    public String getTempDir() {
        return this.tempDir;
    }

    public Boolean getIsDelTempFile() {
        return this.isDelTempFile;
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    public void setIsDelTempFile(Boolean isDelTempFile) {
        this.isDelTempFile = isDelTempFile;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CommonUploader)) {
            return false;
        } else {
            CommonUploader other = (CommonUploader)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$isDelTempFile = this.getIsDelTempFile();
                Object other$isDelTempFile = other.getIsDelTempFile();
                if (this$isDelTempFile == null) {
                    if (other$isDelTempFile != null) {
                        return false;
                    }
                } else if (!this$isDelTempFile.equals(other$isDelTempFile)) {
                    return false;
                }

                Object this$tempDir = this.getTempDir();
                Object other$tempDir = other.getTempDir();
                if (this$tempDir == null) {
                    if (other$tempDir != null) {
                        return false;
                    }
                } else if (!this$tempDir.equals(other$tempDir)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof CommonUploader;
    }

    public int hashCode() {
        int PRIME =  59;
        int result = 1;
        Object $isDelTempFile = this.getIsDelTempFile();
         result = result * 59 + ($isDelTempFile == null ? 43 : $isDelTempFile.hashCode());
        Object $tempDir = this.getTempDir();
        result = result * 59 + ($tempDir == null ? 43 : $tempDir.hashCode());
        return result;
    }

    public String toString() {
        return "CommonUploader(tempDir=" + this.getTempDir() + ", isDelTempFile=" + this.getIsDelTempFile() + ")";
    }

    public CommonUploader() {
    }

    public CommonUploader(String tempDir, Boolean isDelTempFile) {
        this.tempDir = tempDir;
        this.isDelTempFile = isDelTempFile;
    }
}
