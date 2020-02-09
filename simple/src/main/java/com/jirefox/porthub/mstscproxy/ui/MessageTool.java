package com.jirefox.porthub.mstscproxy.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MessageTool {

	public static Shell shell;

	public static void alert(final String title, final String message) {

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.OK);
				messageBox.setText(title);
				messageBox.setMessage(message);
				messageBox.open();
			}
		});

	}

}
