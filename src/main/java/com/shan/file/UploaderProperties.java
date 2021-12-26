//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shan.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("spring.uploader")
public class UploaderProperties {
    private String type;
    private String account;
    private String pwd;
    private String tempdir;
    private String isDelTempFile;

    public String getType() {
        return this.type;
    }

    public String getAccount() {
        return this.account;
    }

    public String getPwd() {
        return this.pwd;
    }

    public String getTempdir() {
        return this.tempdir;
    }

    public String getIsDelTempFile() {
        return this.isDelTempFile;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setTempdir(String tempdir) {
        this.tempdir = tempdir;
    }

    public void setIsDelTempFile(String isDelTempFile) {
        this.isDelTempFile = isDelTempFile;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof UploaderProperties)) {
            return false;
        } else {
            UploaderProperties other = (UploaderProperties)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$type = this.getType();
                    Object other$type = other.getType();
                    if (this$type == null) {
                        if (other$type == null) {
                            break label71;
                        }
                    } else if (this$type.equals(other$type)) {
                        break label71;
                    }

                    return false;
                }

                Object this$account = this.getAccount();
                Object other$account = other.getAccount();
                if (this$account == null) {
                    if (other$account != null) {
                        return false;
                    }
                } else if (!this$account.equals(other$account)) {
                    return false;
                }

                label57: {
                    Object this$pwd = this.getPwd();
                    Object other$pwd = other.getPwd();
                    if (this$pwd == null) {
                        if (other$pwd == null) {
                            break label57;
                        }
                    } else if (this$pwd.equals(other$pwd)) {
                        break label57;
                    }

                    return false;
                }

                Object this$tempdir = this.getTempdir();
                Object other$tempdir = other.getTempdir();
                if (this$tempdir == null) {
                    if (other$tempdir != null) {
                        return false;
                    }
                } else if (!this$tempdir.equals(other$tempdir)) {
                    return false;
                }

                Object this$isDelTempFile = this.getIsDelTempFile();
                Object other$isDelTempFile = other.getIsDelTempFile();
                if (this$isDelTempFile == null) {
                    if (other$isDelTempFile == null) {
                        return true;
                    }
                } else if (this$isDelTempFile.equals(other$isDelTempFile)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof UploaderProperties;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $type = this.getType();
         result = result * 59 + ($type == null ? 43 : $type.hashCode());
        Object $account = this.getAccount();
        result = result * 59 + ($account == null ? 43 : $account.hashCode());
        Object $pwd = this.getPwd();
        result = result * 59 + ($pwd == null ? 43 : $pwd.hashCode());
        Object $tempdir = this.getTempdir();
        result = result * 59 + ($tempdir == null ? 43 : $tempdir.hashCode());
        Object $isDelTempFile = this.getIsDelTempFile();
        result = result * 59 + ($isDelTempFile == null ? 43 : $isDelTempFile.hashCode());
        return result;
    }

    public String toString() {
        return "UploaderProperties(type=" + this.getType() + ", account=" + this.getAccount() + ", pwd=" + this.getPwd() + ", tempdir=" + this.getTempdir() + ", isDelTempFile=" + this.getIsDelTempFile() + ")";
    }

    public UploaderProperties() {
    }

    public UploaderProperties(String type, String account, String pwd, String tempdir, String isDelTempFile) {
        this.type = type;
        this.account = account;
        this.pwd = pwd;
        this.tempdir = tempdir;
        this.isDelTempFile = isDelTempFile;
    }
}
