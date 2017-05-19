package com.routers.engine.credentials;

import com.google.common.base.MoreObjects;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAttribute;

import static com.google.common.base.Preconditions.checkArgument;
import static com.routers.utils.validators.Validators.hostValidator;
import static com.routers.utils.validators.Validators.portValidator;

public abstract class Credentials {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Credentials.class);

    private String host;
    private int port = -1; //if credentials fields are not set they must be invalid
    private String user;
    private String password;

    public Credentials() {}

    public Credentials(String host, int port, String user, String password) {
        checkArgument(hostValidator.validate(host), "Invalid host name: %s", host);
        checkArgument(portValidator.validate(port), "Invalid port number: $s", port);

        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        log.info("{} was created", this);
    }

    @XmlAttribute
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        checkArgument(hostValidator.validate(host), "Invalid host name: %s", host);
        this.host = host;
    }

    @XmlAttribute
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        checkArgument(portValidator.validate(port), "Invalid port number: $s", port);
        this.port = port;
    }

    @XmlAttribute
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @XmlAttribute
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("host", host)
                .add("port", port)
                .add("user", user)
                .toString();
    }
}