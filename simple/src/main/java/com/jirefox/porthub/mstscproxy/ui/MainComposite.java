package com.jirefox.porthub.mstscproxy.ui;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jirefox.porthub.mstscproxy.App;
import com.jirefox.porthub.mstscproxy.handler.HubService;

public class MainComposite extends Composite {

	public static Text textLocalConnectCode;

	public static Label lblWarn;

	public static Button btnConnect;

	public MainComposite(Composite parent, int style) {
		super(parent, style);

		Label lblLocalCode = new Label(this, SWT.NONE);
		lblLocalCode.setText("连接码");
		lblLocalCode.setBounds(10, 26, 41, 16);

		textLocalConnectCode = new Text(this, SWT.READ_ONLY);
		textLocalConnectCode.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
		textLocalConnectCode.setText("连接服务器中...");
		textLocalConnectCode.setBounds(57, 26, 240, 16);

		Link link = new Link(this, SWT.NONE);
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Runtime.getRuntime()
							.exec("rundll32 url.dll,FileProtocolHandler https://github.com/ylyxf/mstsc-proxy");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		link.setText("<a>帮助</a>");
		link.setBounds(345, 81, 30, 16);

		lblWarn = new Label(this, SWT.NONE);
		lblWarn.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblWarn.setBounds(10, 81, 203, 12);
		lblWarn.setText("本机3389端口未打开，无法开启远程");
		lblWarn.setVisible(false);

		btnConnect = new Button(this, SWT.NONE);
		btnConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HubService hubService = App.context.getBean(HubService.class);
				hubService.reconnect();// 异步
			}
		});
		btnConnect.setTouchEnabled(true);
		btnConnect.setBounds(303, 21, 72, 22);
		btnConnect.setText("重连");

	}
}
