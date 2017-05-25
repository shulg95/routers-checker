package scripts

import com.routers.connections.SshClient
import com.routers.engine.credentials.Credentials
import com.routers.engine.credentials.SSHCredentials
import com.routers.utils.comparators.BitByBitComparator
import com.routers.utils.comparators.ConfigsComparator
import com.routers.utils.comparators.FileComparator

//TODO think over and implement API for scripts
def executeScenario(Map<String, String> routerProps, Map<String, String> tftpServerConfig) {

    String standardPath = tftpServerConfig.get(ConfigKeys.STANDARD_PATH)
    String etalonImagePath = routerProps.get(ConfigKeys.ETALON_IMAGE_PATH)
    String etalonConfigPath = routerProps.get(ConfigKeys.ETALON_CONFIG_PATH)
    Credentials sshCredentials = new SSHCredentials(
            routerProps.get(ConfigKeys.IP_V4),
            Integer.parseInt(routerProps.get(ConfigKeys.PORT)),
            routerProps.get(ConfigKeys.LOGIN),
            routerProps.get(ConfigKeys.PASSWORD))

    SshClient ssh = new SshClient()

    println("Try to connect...")
    ssh.connect(sshCredentials)
    println("Connected")

    println(ssh.send("enable\n", "Password:"))

    println(ssh.send("cisco\n", "Cisco#"))

    ssh.send("terminal length 0\n", "Cisco#")

    println(ssh.send("copy " + routerProps.get(ConfigKeys.SOURCE_IMAGE_PATH) + routerProps.get(ConfigKeys.IMAGE_NAME)
            + " tftp://" + tftpServerConfig.get(ConfigKeys.TFTP_SERVER_ADDRESS) + "/\n", "]?"))
    println(ssh.send("\n", "]?"))
    println("Downloading image...")
    println(ssh.send("\n", "Cisco#"))
    println("Downloaded")

    println(ssh.send("copy running-config"
            + " tftp://" + tftpServerConfig.get(ConfigKeys.TFTP_SERVER_ADDRESS) + "/\n", "]?"))
    println(ssh.send("\n", "]?"))
    println("Downloading config...")
    println(ssh.send("\n", "Cisco#"))
    println("Downloaded")

    ssh.disconnect()

    println("Start image validation...")
    FileComparator imageComparator = new BitByBitComparator()
    boolean theResult = imageComparator.compare(
            new File(standardPath + "\\" + routerProps.get(ConfigKeys.IMAGE_NAME)),
            new File(etalonImagePath + "\\" + routerProps.get(ConfigKeys.IMAGE_NAME)))
    println("Validation result: " + (theResult ? "Ok" : "Invalid"))

    println("Start config validation...")
    FileComparator configComparator = new ConfigsComparator()
    theResult = configComparator.compare(
            new File(standardPath + "\\" + "cisco-confg"),
            new File(etalonConfigPath + "\\" + routerProps.get(ConfigKeys.ETALON_CONFIG_NAME)),
            new File("D:/NetworkDefender/diff.txt"))
    println("Validation result: " + (theResult ? "Ok" : "Invalid"))

    //cleaning standart directory
    File standartDirectory = new File(standardPath);
    File[] files = standartDirectory.listFiles();
    if(files!=null) { //some JVMs return null for empty dirs
        for(File f: files) {
            if(f.isDirectory()) {
                deleteFolder(f);
            } else {
                f.delete();
            }
        }
    }
}