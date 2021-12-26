package com.shan.entity.bean;

public class FileInfo {
    private String name;
    private Boolean isFile;
    private String path;
    private String extension;
    private Long size;

    public String getName() {
        return this.name;
    }

    public Boolean getIsFile() {
        return this.isFile;
    }

    public String getPath() {
        return this.path;
    }

    public String getExtension() {
        return this.extension;
    }

    public Long getSize() {
        return this.size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsFile(Boolean isFile) {
        this.isFile = isFile;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof FileInfo)) {
            return false;
        } else {
            FileInfo other = (FileInfo)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$isFile = this.getIsFile();
                    Object other$isFile = other.getIsFile();
                    if (this$isFile == null) {
                        if (other$isFile == null) {
                            break label71;
                        }
                    } else if (this$isFile.equals(other$isFile)) {
                        break label71;
                    }

                    return false;
                }

                Object this$size = this.getSize();
                Object other$size = other.getSize();
                if (this$size == null) {
                    if (other$size != null) {
                        return false;
                    }
                } else if (!this$size.equals(other$size)) {
                    return false;
                }

                label57: {
                    Object this$name = this.getName();
                    Object other$name = other.getName();
                    if (this$name == null) {
                        if (other$name == null) {
                            break label57;
                        }
                    } else if (this$name.equals(other$name)) {
                        break label57;
                    }

                    return false;
                }

                Object this$path = this.getPath();
                Object other$path = other.getPath();
                if (this$path == null) {
                    if (other$path != null) {
                        return false;
                    }
                } else if (!this$path.equals(other$path)) {
                    return false;
                }

                Object this$extension = this.getExtension();
                Object other$extension = other.getExtension();
                if (this$extension == null) {
                    if (other$extension == null) {
                        return true;
                    }
                } else if (this$extension.equals(other$extension)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof FileInfo;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $isFile = this.getIsFile();
        result = result * 59 + ($isFile == null ? 43 : $isFile.hashCode());
        Object $size = this.getSize();
        result = result * 59 + ($size == null ? 43 : $size.hashCode());
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Object $path = this.getPath();
        result = result * 59 + ($path == null ? 43 : $path.hashCode());
        Object $extension = this.getExtension();
        result = result * 59 + ($extension == null ? 43 : $extension.hashCode());
        return result;
    }

    public String toString() {
        return "FileInfo(name=" + this.getName() + ", isFile=" + this.getIsFile() + ", path=" + this.getPath() + ", extension=" + this.getExtension() + ", size=" + this.getSize() + ")";
    }

    public FileInfo(String name, Boolean isFile, String path, String extension, Long size) {
        this.name = name;
        this.isFile = isFile;
        this.path = path;
        this.extension = extension;
        this.size = size;
    }

    public FileInfo() {
    }
}
