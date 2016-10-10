package io.github.joshuawebb.batterynotificationtweak;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;

public class DisableLowBatteryNotifications implements IXposedHookInitPackageResources {

	private final static String targetPackage = "com.android.systemui";

	@Override
	public void handleInitPackageResources(
			XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
		if (!resparam.packageName.equals(targetPackage))
			return;

		resparam.res.setReplacement(targetPackage, "integer", "config_staminaWarningLevel", -1);
		resparam.res.setReplacement(targetPackage, "integer", "config_staminaCriticalLevel", -1);
	}
}
