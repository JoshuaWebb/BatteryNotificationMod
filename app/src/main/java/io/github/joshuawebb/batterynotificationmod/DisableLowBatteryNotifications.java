package io.github.joshuawebb.batterynotificationmod;

import android.content.res.Resources;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;

public class DisableLowBatteryNotifications implements IXposedHookInitPackageResources {

	private final static String targetPackage = "com.android.systemui";

	@Override
	public void handleInitPackageResources(
			XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
		if (!resparam.packageName.equals(targetPackage))
			return;

		String[] configs = new String[] {
			// Xperia devices
			"config_staminaWarningLevel",
			"config_staminaCriticalLevel",

			// Other devices
			"config_lowBatteryCloseWarningLevel",
			"config_lowBatteryWarningLevel",
			"config_criticalBatteryWarningLevel"
		};

		List<String> set = new ArrayList<>();
		List<String> notSet = new ArrayList<>();
		for(String config : configs) {
			if (setReplacement(resparam, config, -1)) {
				set.add(config);
			} else {
				notSet.add(config);
			}
		}

		XposedBridge.log("BatteryMod - set: " + TextUtils.join(", ", set));
		XposedBridge.log("BatteryMod - didn't exist: " + TextUtils.join(", ", notSet));
	}

	private boolean setReplacement(
			XC_InitPackageResources.InitPackageResourcesParam resparam,
			String config,
			int value)
	{
		try {
			resparam.res.setReplacement(targetPackage, "integer", config, value);
			return true;
		}
		catch (Resources.NotFoundException nfe)
		{
			return false;
		}
	}
}
