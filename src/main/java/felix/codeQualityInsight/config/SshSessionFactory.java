package felix.codeQualityInsight.config;

import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;
import org.eclipse.jgit.util.FS;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import static felix.codeQualityInsight.common.Constant.LOCAL_SECRET_KEY_PATH;

/**
 * SSH配置工厂
 */
public class SshSessionFactory extends JschConfigSessionFactory {
    @Override
    protected void configure(OpenSshConfig.Host host, Session session) {
        // 设置 SSH 客户端在连接远程服务器时不进行严格的主机密钥检查
        session.setConfig("StrictHostKeyChecking", "no");
        try {
            //设置连接超时时间
            session.connect(30000);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected JSch createDefaultJSch(FS fs) throws JSchException {
        JSch defaultJSch = super.createDefaultJSch(fs);
        // 添加私钥文件用于身份验证
        defaultJSch.addIdentity(LOCAL_SECRET_KEY_PATH);
        return defaultJSch;
    }

}

