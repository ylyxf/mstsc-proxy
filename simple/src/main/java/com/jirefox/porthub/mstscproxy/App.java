package com.jirefox.porthub.mstscproxy;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.jirefox.porthub.mstscproxy.handler.HubService;
import com.jirefox.porthub.mstscproxy.ui.MainWindow;

@EnableAutoConfiguration
@ComponentScan("com.jirefox.porthub.mstscproxy")
public class App {

	public static final String TITLE = "内网远程控制";

	public static final int VERSION = 2;

	public static ApplicationContext context;

	public static void main(String[] args) {

		Display display = Display.getDefault();
		Shell shell = new MainWindow(display);

		shell.open();
		shell.layout();

		context = SpringApplication.run(App.class, args);

		HubService hubService = context.getBean(HubService.class);
		hubService.reconnect();// 异步

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

	}

}
