package com.jirefox.porthub.mstscproxy.handler;

import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.JSchException;
import com.jirefox.porthub.mstscproxy.ui.MessageTool;

@EnableAsync
@Component
public class HubService {

	@Value("${localHost}")
	private String localHost;

	@Value("${localPort}")
	private Integer localPort;

	@Autowired
	UiService uiService;

	@Autowired
	SshService sshService;

	@Async
	public void reconnect() {
		try {
			if (!isLocalPortAvailable()) {
				return;
			}
			uiService.beforeConnect();
			String connectCode = sshService.connect(localHost, localPort);
			uiService.updateLocalConnectCode(connectCode);
		} catch (Exception e) {
			MessageTool.alert("出错了", e.getMessage());
		} finally {
			uiService.afterConnect();
		}
	}

	private Boolean isLocalPortAvailable() {
		Boolean result = false;
		TelnetClient telnet = new TelnetClient();
		try {
			telnet.connect(localHost, localPort);
			if (!telnet.isAvailable()) {
				uiService.showWarn(true);
				result = false;
			} else {
				uiService.showWarn(false);
				result = true;
			}
			telnet.disconnect();
		} catch (Exception e) {
			MessageTool.alert("出错了", e.getMessage());
			uiService.showWarn(true);
			result = false;
		}
		return result;
	}

}
