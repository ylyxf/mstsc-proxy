package com.jirefox.porthub.mstscproxy.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jirefox.porthub.mstscproxy.App;
import com.jirefox.porthub.mstscproxy.handler.SshService;

public class MainWindow extends Shell {

	public MainWindow(Display display) {
		super(display, SWT.TITLE | SWT.CLOSE | SWT.MIN);

		setText(App.TITLE);
		setSize(400, 130);

		// 居中
		Rectangle bounds = display.getPrimaryMonitor().getBounds();
		Rectangle rect = this.getBounds();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		this.setLocation(x, y);

		// 创建内容
		setLayout(new FillLayout(SWT.HORIZONTAL));
		new MainComposite(this, SWT.NONE);

		// 响应事件
		this.addShellListener(new ShellAdapter() {

			// 点击窗口关闭按钮时，并不终止程序
			public void shellClosed(ShellEvent e) {
				e.doit = false; // 消耗掉原本系统来处理的事件
				setVisible(false);// 隐藏窗口
			}
		});

		// 创建TrayIcon
		TrayIcon.init(display, this);

		// 设置全局的提示框
		MessageTool.shell = this;
	}

	@Override
	protected void checkSubclass() {

	}

	@Override
	public void dispose() {
		super.dispose();
		TrayIcon.dispose();
		SWTResourceManager.dispose();
		SshService.dispose();
	}
}
