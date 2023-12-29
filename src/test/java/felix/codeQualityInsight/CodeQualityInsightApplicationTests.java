package felix.codeQualityInsight;

import com.alibaba.fastjson2.JSON;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import felix.codeQualityInsight.model.dto.ProjectPageInfo;
import felix.codeQualityInsight.util.SonarApiClient;
import jakarta.xml.bind.DatatypeConverter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.ssh.jsch.JschConfigSessionFactory;
import org.eclipse.jgit.transport.ssh.jsch.OpenSshConfig;
import org.eclipse.jgit.util.FS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static felix.codeQualityInsight.common.Constant.BASE_DIRECTORY;
import static felix.codeQualityInsight.common.Constant.LOCAL_SECRET_KEY_PATH;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CodeQualityInsightApplicationTests {
	@Autowired
	private SonarApiClient sonarApiClient;
	/**
	 * 测试ssh连接
	 * @throws GitAPIException
	 */
	@Test
	void contextLoads() throws GitAPIException {
		String sshUrl = "git@gitlab.gz.cvte.cn:i_zengrixin/saga-private.git";
		String commitId = "68d18ccbf5605f16a15f8fb66c21a8b6776c6d8c";

			// Set SSH credentials and authentication method
			SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
				@Override
				protected void configure(OpenSshConfig.Host host, Session session) {
					session.setConfig("StrictHostKeyChecking", "ask");
				}

				@Override
				protected JSch createDefaultJSch(FS fs) throws JSchException {
					JSch jSch = super.createDefaultJSch(fs);
					jSch.addIdentity(LOCAL_SECRET_KEY_PATH);
					return jSch;
				}
			};
			Path tempDirectory = Paths.get(BASE_DIRECTORY);
			// Clone or fetch a repository

			try (Git git = Git.cloneRepository()
					.setURI(sshUrl)
					.setTransportConfigCallback(transport -> {
						SshTransport sshTransport = (SshTransport) transport;
						sshTransport.setSshSessionFactory(sshSessionFactory);
					})
					.setDirectory(new File(String.valueOf(tempDirectory)))
					.call()) {
			}
		System.out.println(tempDirectory);
		}


	@Test
	public void getProjects() {
		ProjectPageInfo projects = sonarApiClient.getProjects();
		System.out.println(JSON.toJSONString(projects));
		assertNotNull(projects);
	}

	/**
	 * 对账号和密码进行base64加密后，放到head的Authorization字段作为token
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(DatatypeConverter.printBase64Binary(("admin:qwe12345").getBytes(StandardCharsets.UTF_8)));
	}
}
