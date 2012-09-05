package com.nmbb.oplayer.ui.vitamio;

import io.vov.vitamio.VitamioInstaller;
import android.app.Activity;
import android.content.Intent;

public final class LibsChecker {
	public static final String FROM_ME = "fromVitamioInitActivity";

	public static final boolean checkVitamioLibs(Activity ctx, int msgID, int rawID) {
		new VitamioInstaller(ctx);
		if (!VitamioInstaller.isNativeLibsInited(ctx) && !ctx.getIntent().getBooleanExtra(FROM_ME, false)) {
			Intent i = new Intent();
			i.setClassName(VitamioInstaller.getCompatiblePackage(), "com.nmbb.oplayer.ui.vitamio.InitActivity");
			i.putExtras(ctx.getIntent());
			i.setData(ctx.getIntent().getData());
			i.putExtra("package", ctx.getApplicationInfo().packageName);
			i.putExtra("className", ctx.getClass().getName());
			i.putExtra("EXTRA_MSG", msgID);
			i.putExtra("EXTRA_FILE", rawID);
			ctx.startActivity(i);
			ctx.finish();
			return false;
		}
		return true;
	}
}
