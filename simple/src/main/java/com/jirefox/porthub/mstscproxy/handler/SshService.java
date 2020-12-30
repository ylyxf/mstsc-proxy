package com.jirefox.porthub.mstscproxy.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.Session;

@Component
public class SshService {

	protected static final Logger logger = LoggerFactory.getLogger(SshService.class);

	@Value("${host}")
	private String host;

	@Value("${port}")
	private Integer port;

	@Value("${user}")
	private String user;

	@Value("${password}")
	private String password;

	@Value("${remotePorts}")
	private String remotePorts;

	@Value("${proxyHost}")
	private String proxyHost;

	@Value("${proxyPort}")
	private Integer proxyPort;

	private JSch jsch = new JSch();

	public static Session l2rSession = null;

	public String connect(String localHost, Integer localPort) throws JSchException {
		
		Session session = jsch.getSession(user, host, port);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(password);
		session.setServerAliveInterval(30000);
		
		if(!StringUtils.isEmpty(proxyHost)&&!StringUtils.isEmpty(proxyPort)) {
			ProxyHTTP proxyhttp = new  ProxyHTTP (proxyHost,proxyPort);
			session.setProxy(proxyhttp);
		}
		
		
		session.connect(10000);
		List<Integer> remotePorts = listAvailablePorts();
		Integer distRemotePort = null;
		for (Integer remotePort : remotePorts) {
			try {
				session.setPortForwardingR(remotePort, localHost, localPort);
				distRemotePort = remotePort;
				break;
			} catch (Exception e) {
				logger.warn("port " + remotePort + " is used");
			}
		}
		if (distRemotePort != null) {
			if (l2rSession != null) {
				l2rSession.disconnect();
			}
			l2rSession = session;
			return host + ":" + distRemotePort;
		} else {
			throw new RuntimeException("remote ports " + remotePorts + " can't forward.");
		}
	}

	public List<Integer> listAvailablePorts() {
		String availablePorts = this.remotePorts;
		List<Integer> remotePortList = new ArrayList<>();
		String[] portRanges = availablePorts.split(",");
		byte b;
		int i;
		String[] arrayOfString1;
		for (i = (arrayOfString1 = portRanges).length, b = 0; b < i;) {
			String portRange = arrayOfString1[b];
			String[] startEndPort = portRange.split("-");
			try {
				if (startEndPort.length == 1) {
					remotePortList.add(Integer.valueOf(Integer.parseInt(startEndPort[0])));
				} else {
					int startPort = Integer.parseInt(startEndPort[0]);
					int endPort = Integer.parseInt(startEndPort[1]);
					if (startPort < 0 || endPort > 65535)
						throw new RuntimeException(
								"remote ports :" + availablePorts + " is invald.it should be 1-65535.");
					for (int j = startPort; j <= endPort; j++)
						remotePortList.add(Integer.valueOf(j));
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new RuntimeException("can't parse remote ports:" + availablePorts);
			}
			b++;
		}
		return remotePortList;
	}

	/**
	 * 释放资源
	 */
	public static void dispose() {
		if (l2rSession != null) {
			l2rSession.disconnect();
		}
	}

}
