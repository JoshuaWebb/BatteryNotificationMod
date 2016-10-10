package io.github.joshuawebb.batterynotificationtweak;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class NoThirtyPercentBatteryNotification implements IXposedHookLoadPackage {

	private final static String targetPackage = "com.android.systemui";

	@Override
	public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
		if (!targetPackage.equals(lpparam.packageName))
			return;

		XposedBridge.log("load package NoThirtyPercentBatteryNotification");

		String targetClassName = targetPackage + ".power.PowerNotificationWarnings";
		final Class<?> notifierClass = XposedHelpers.findClass(targetClassName, lpparam.classLoader);

		XC_MethodHook blockCallHook = new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				super.beforeHookedMethod(param);
				param.setResult(null);
			}
		};

		/**
		 * showWarningNotification  is 30%
		 * showCriticalNotification is 15%
		 *
		 * This version just stops 30%
		 *   -- perhaps another version could configure the thresholds.
		 */
		XposedHelpers.findAndHookMethod(notifierClass, "showWarningNotification", blockCallHook);
	}
}
