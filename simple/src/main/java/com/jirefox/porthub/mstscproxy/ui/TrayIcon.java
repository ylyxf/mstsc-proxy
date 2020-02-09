package com.jirefox.porthub.mstscproxy.ui;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.jirefox.porthub.mstscproxy.App;

public class TrayIcon {

	public static TrayItem trayItem;

	public static void init(Display display, final Shell shell) {
		InputStream imageStream = TrayIcon.class.getClassLoader().getResourceAsStream("logo.png");
		ImageData data = new ImageData(imageStream);
		Image image = new Image(display, data);
		final Tray tray = display.getSystemTray();
		trayItem = new TrayItem(tray, SWT.NONE);
		trayItem.setToolTipText(App.TITLE);
		trayItem.setImage(image);

		final Menu trayMenu = new Menu(shell, SWT.POP_UP);
		// 在系统栏图标点击鼠标右键时的事件，弹出系统栏菜单
		trayItem.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent e) {
				trayMenu.setVisible(true);
			}
		});

		trayItem.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event event) {
				shell.setVisible(true);
				shell.setActive();
			}
		});

		MenuItem showMenuItem = new MenuItem(trayMenu, SWT.PUSH);
		showMenuItem.setText("显示主窗口");
		showMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.setVisible(true);
				shell.setActive();
			}
		});

		MenuItem authorMenuItem = new MenuItem(trayMenu, SWT.PUSH);
		authorMenuItem.setText("源代码/更新");
		authorMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler https://github.com/ylyxf/mstsc-proxy");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		MenuItem exitMenuItem = new MenuItem(trayMenu, SWT.PUSH);
		exitMenuItem.setText("退出程序");
		exitMenuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.dispose();
			}
		});
	}

	public static void dispose() {
		trayItem.dispose();
	}

}
