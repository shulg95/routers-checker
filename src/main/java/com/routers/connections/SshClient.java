package com.routers.connections;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.routers.engine.credentials.Credentials;
import com.routers.utils.Constants;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.routers.utils.validators.Validators.hostValidator;
import static com.routers.utils.validators.Validators.portValidator;

public class SshClient {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SshClient.class);

//  !TODO These commands are cisco cpecific. Thonk about extention for all vendors
//    private static final String INVALID_COMMAND = "Invalid input detected at '^' marker";
//    private static final String ERROR_IN_EXECUTION = "%Error";

    private static final int SLEEP_TIME = 200;

    private Session session;
    private Channel channel;

    // for SSH
    private JSch shell;
    private OutputStream out;
    private InputStream in;

    public void connect(Credentials credentials) throws IOException, JSchException {

        checkNotNull(credentials);
        checkArgument(hostValidator.validate(credentials.getHost()), "Invalid host name: %s", credentials.getHost());
        checkArgument(portValidator.validate(credentials.getPort()), "Invalid port number: $s", credentials.getPort());

        shell = new JSch();
        log.debug("User {} is trying to open ssh session with {}", credentials.getUser(), credentials.getHost());
        session = shell.getSession(credentials.getUser(), credentials.getHost(), credentials.getPort());
        session.setPassword(credentials.getPassword());

        //!TODO
        // Disabling the StrictHostKeyChecking option will make the connection
        // less secure than having the option enabled, because it will let you
        // connect to remote servers without verifying their SSH host keys.
        session.setConfig("StrictHostKeyChecking", "no");
        session.setTimeout(Constants.SSH_CONNECTION_TIMEOUT);
        session.connect();

        channel = session.openChannel("shell");
        channel.setInputStream(null);
        channel.setOutputStream(null);

        in = channel.getInputStream();
        out = channel.getOutputStream();
        ((ChannelShell) channel).setPtyType("vt102");
        channel.connect();
        log.info("User {} established connection to {}", credentials.getUser(), credentials.getHost());
    }

    public void disconnect() {
        if (channel != null && session.isConnected()) {
            channel.disconnect();
            session.disconnect();
        }
    }

    public String send(String command, String prompt) throws IOException, InterruptedException, JSchException {
        byte[] tmp = new byte[1024];

        out.write(command.getBytes());
        out.flush();
        log.debug("{} was sent to {}", command, out.hashCode());
        String result = "";
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                result = result + (new String(tmp, 0, i));
            }
            //!TODO The problem is that if there's a mistake in prompt the program hangs
            if (isExecutionInterrupted(result, prompt)) break;
            sleep();
        }
        log.debug("{} message was accepted from {}", result, out.hashCode());
        return result;
    }

    private static boolean isExecutionInterrupted(String result, String prompt) throws JSchException {
        if (result.contains(prompt)) {
            return true;
        }
//      !TODO These commands are cisco cpecific. Thonk about extention for all vendors
//        } else if (result.contains(INVALID_COMMAND)) {
//            log.error("Invalid command detected: '" + command + "'");
//            throw new JSchException("Invalid command detected");
//        } else if (result.contains(ERROR_IN_EXECUTION)) {
//            log.error("Error in command execution: '" + command + "'");
//            throw new JSchException("Error in command execution");
//        }
        return false;
    }

    private static void sleep() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException ignored) {}
    }
}
