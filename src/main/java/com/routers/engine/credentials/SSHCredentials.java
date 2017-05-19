package com.routers.engine.credentials;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType
public class SSHCredentials extends Credentials {
    public SSHCredentials() {
    }

    public SSHCredentials(String host, int port, String user, String password) {
        super(host, port, user, password);
    }
}
