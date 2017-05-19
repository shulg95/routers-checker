package com.routers.engine.credentials;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType
public class FTPCredentials extends Credentials {

    private String workingDir;

    public FTPCredentials() {
    }

    public FTPCredentials(String host, int port, String user, String password, String workingDir) {
        super(host, port, user, password);
        this.workingDir = workingDir;
    }

    @XmlAttribute
    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }
}
