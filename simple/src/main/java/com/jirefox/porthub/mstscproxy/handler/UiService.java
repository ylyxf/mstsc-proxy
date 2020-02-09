package com.jirefox.porthub.mstscproxy.handler;

import org.eclipse.swt.widgets.Display;
import org.springframework.stereotype.Component;

import com.jirefox.porthub.mstscproxy.ui.MainComposite;

@Component
public class UiService {

	public void beforeConnect() {

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MainComposite.btnConnect.setEnabled(false);
			}
		});
	}

	public void afterConnect() {

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MainComposite.btnConnect.setEnabled(true);
			}
		});
	}

	public void updateLocalConnectCode(final String localConnectCode) {

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				String currentStatus = localConnectCode == null ? "" : localConnectCode;
				MainComposite.textLocalConnectCode.setText(currentStatus);
			}
		});
	}

	public void showWarn(final boolean show) {

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MainComposite.lblWarn.setVisible(show);
			}
		});
	}

}
