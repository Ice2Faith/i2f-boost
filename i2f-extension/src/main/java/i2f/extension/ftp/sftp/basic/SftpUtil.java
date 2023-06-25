package i2f.extension.ftp.sftp.basic;

import com.jcraft.jsch.*;
import i2f.core.io.file.FileUtil;
import i2f.extension.ftp.sftp.basic.data.ISftpMeta;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * @author ltb
 * @date 2021/11/1
 */
public class SftpUtil implements Closeable{
    private ISftpMeta meta;
    private Session session;
    private ChannelSftp channelSftp;

    public SftpUtil(ISftpMeta meta) {
        setMeta(meta);
    }

    public SftpUtil setMeta(ISftpMeta meta) {
        this.meta = meta;
        return this;
    }

    public SftpUtil login() throws JSchException {
        JSch jsch=new JSch();
        if(meta.getPrivateKey()!=null){
            jsch.addIdentity(meta.getPrivateKey());
        }

        session=jsch.getSession(meta.getUsername(),meta.getHost(),meta.getPort());

        if(meta.getPassword()!=null){
            session.setPassword(meta.getPassword());
        }

        Properties config=new Properties();
        config.put("StrictHostKeyChecking","no");

        Properties cusConfig=meta.getConfig();
        for(Map.Entry<Object,Object> item : cusConfig.entrySet()){
            config.put(item.getKey(),item.getValue());
        }

        session.setConfig(config);
        session.connect();

        Channel channel=session.openChannel("sftp");
        channel.connect();

        channelSftp=(ChannelSftp)channel;

        return this;
    }

    public SftpUtil logout(){
        if(channelSftp!=null){
            if(channelSftp.isConnected()){
                channelSftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
        return this;
    }

    public ChannelShell getChannelShell() throws JSchException {
        Channel shell = session.openChannel("shell");
        shell.connect();
        return (ChannelShell) shell;
    }

    public SftpUtil mkdirs(String serverPath) throws SftpException {
        serverPath = serverPath.replaceAll("\\\\", "/");
        String[] dirs = serverPath.split("\\/");
        StringBuilder builder = new StringBuilder();
        for (String item : dirs) {
            if ("".equals(item)) {
                continue;
            }
            builder.append("/");
            builder.append(item);
            String ppath=builder.toString();
            if(!existDir(ppath)) {
                channelSftp.mkdir(ppath);
            }
        }
        return this;
    }

    public boolean existDir(String path){
        try{
            String bak=channelSftp.pwd();
            channelSftp.cd(path);
            channelSftp.cd(bak);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public SftpUtil upload(String serverPath, String  serverFileName, InputStream is) throws SftpException {
        mkdirs(serverPath);
        channelSftp.cd(serverPath);
        channelSftp.put(is,serverFileName);
        return this;
    }

    public SftpUtil upload(String serverPath, File localFile) throws SftpException, IOException {
        String fileName=localFile.getName();
        InputStream is=new FileInputStream(localFile);
        upload(serverPath,fileName,is);
        is.close();
        return this;
    }

    public OutputStream download(String serverPath,String serverFileName,OutputStream os) throws SftpException {
        if(serverPath!=null && !"".equals(serverPath)){
            channelSftp.cd(serverPath);
        }
        channelSftp.get(serverFileName,os);
        return os;
    }

    public File download(String serverPath,String serverFileName,File localFile) throws IOException, SftpException {
        FileUtil.useParentDir(localFile);
        OutputStream os=new FileOutputStream(localFile);
        download(serverPath,serverFileName,os);
        os.close();
        return localFile;
    }

    public File download2Dir(String serverPath,String serverFileName,File localDir) throws  SftpException {
        FileUtil.useDir(localDir);
        File localFile=new File(localDir,serverFileName);
        try{
            OutputStream os=new FileOutputStream(localFile);
            download(serverPath,serverFileName,os);
            os.close();
        }catch(IOException e){
            throw new SftpException(0,e.getMessage(),e);
        }
        return localFile;
    }

    public SftpUtil delete(String serverPath,String serverFileName) throws SftpException {
        if(serverPath!=null && !"".equals(serverPath)){
            channelSftp.cd(serverPath);
        }
        channelSftp.rm(serverFileName);
        return this;
    }

    public SftpUtil deleteDir(String serverPath) throws SftpException {
        channelSftp.rmdir(serverPath);
        return this;
    }

    public Vector<?> listFiles(String serverPath) throws SftpException {
        return channelSftp.ls(serverPath);
    }

    public String exec(String command) throws IOException {
        ChannelShell channel = null;
        PrintWriter printWriter = null;
        BufferedReader input = null;
        StringBuilder builder = new StringBuilder();
        try {
            channel = getChannelShell();

            InputStreamReader inputStreamReader = new InputStreamReader(channel.getInputStream());
            input = new BufferedReader(inputStreamReader);

            printWriter = new PrintWriter(channel.getOutputStream());
            printWriter.println(command);
            printWriter.println("exit");
            printWriter.flush();
            String line = null;
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (JSchException e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
            if (input != null) {
                input.close();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
        return builder.toString();
    }

    @Override
    public void close() throws IOException {
        logout();
    }
}
