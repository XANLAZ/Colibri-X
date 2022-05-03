package org.viento.colibrix.helpers;

import android.net.Uri;
import android.text.TextUtils;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.ui.ActionBar.BaseFragment;

import org.viento.colibrix.settings.BaseColibriXSettingsActivity;
import org.viento.colibrix.settings.ColibriXAppearanceSettings;
import org.viento.colibrix.settings.ColibriXChatSettingsActivity;
import org.viento.colibrix.settings.ColibriXDonateActivity;
import org.viento.colibrix.settings.ColibriXExperimentalSettingsActivity;
import org.viento.colibrix.settings.ColibriXGeneralSettingsActivity;
import org.viento.colibrix.settings.ColibriXPasscodeSettingsActivity;
import org.viento.colibrix.settings.ColibriXSettingsActivity;
import org.viento.colibrix.settings.WsSettingsActivity;

public class SettingsHelper {
    public static void processDeepLink(String link, Callback callback, Runnable unknown) {
        Uri uri = null;
        try {
            uri = Uri.parse(link);
        } catch (Exception e) {
            FileLog.e(e);
        }
        processDeepLink(uri, callback, unknown, false);
    }

    public static void processDeepLink(Uri uri, Callback callback, Runnable unknown, boolean me) {
        if (uri == null) {
            unknown.run();
            return;
        }
        var segments = uri.getPathSegments();
        if (me) {
            if (segments.isEmpty() || segments.size() > 2 || !"colibrixsettings".equals(segments.get(0))) {
                unknown.run();
                return;
            }
        } else if (segments.size() > 1 || !"colibrix".equals(uri.getHost())) {
            unknown.run();
            return;
        }
        BaseColibriXSettingsActivity fragment;
        if (segments.isEmpty() || me && segments.size() == 1) {
            fragment = new ColibriXSettingsActivity();
        } else if (PasscodeHelper.getSettingsKey().equals(segments.get(me ? 1 : 0))) {
            fragment = new ColibriXPasscodeSettingsActivity();
        } else {
            switch (segments.get(me ? 1 : 0)) {
                case "appearance":
                case "a":
                    fragment = new ColibriXAppearanceSettings();
                    break;
                case "chat":
                case "chats":
                case "c":
                    fragment = new ColibriXChatSettingsActivity();
                    break;
                case "donate":
                case "d":
                    fragment = new ColibriXDonateActivity();
                    break;
                case "experimental":
                case "e":
                    fragment = new ColibriXExperimentalSettingsActivity(false, false);
                    break;
                case "general":
                case "g":
                    fragment = new ColibriXGeneralSettingsActivity();
                    break;
                case "ws":
                case "w":
                    fragment = new WsSettingsActivity();
                    break;
                default:
                    unknown.run();
                    return;
            }
        }
        callback.presentFragment(fragment);
        var row = uri.getQueryParameter("r");
        if (TextUtils.isEmpty(row)) {
            row = uri.getQueryParameter("row");
        }
        if (!TextUtils.isEmpty(row)) {
            var rowFinal = row;
            AndroidUtilities.runOnUIThread(() -> fragment.scrollToRow(rowFinal, unknown));
        }

    }

    public interface Callback {
        void presentFragment(BaseFragment fragment);
    }
}
