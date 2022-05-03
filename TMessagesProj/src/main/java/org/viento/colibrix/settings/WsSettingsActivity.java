package org.viento.colibrix.settings;

import android.app.assist.AssistContent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;

import java.util.ArrayList;

import org.viento.colibrix.ColibriXConfig;
import org.viento.colibrix.helpers.PopupHelper;

public class WsSettingsActivity extends BaseColibriXSettingsActivity {

    private int descriptionRow;
    private int settingsRow;
    private int enableTLSRow;
    private int localProxyRow;
    private int enableDoHRow;

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == enableTLSRow) {
            ColibriXConfig.toggleWsEnableTLS();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.wsEnableTLS);
            }
            ColibriXConfig.wsReloadConfig();
        } else if (position == localProxyRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(LocaleController.getString("UseProxySocks5", R.string.UseProxySocks5));
            arrayList.add(LocaleController.getString("UseProxyTelegram", R.string.UseProxyTelegram));
            PopupHelper.show(arrayList, LocaleController.getString("WsLocalProxy", R.string.WsLocalProxy), ColibriXConfig.wsUseMTP ? 1 : 0, getParentActivity(), view, i -> {
                ColibriXConfig.setWsUseMTP(i == 1);
                listAdapter.notifyItemChanged(localProxyRow);
                ColibriXConfig.wsReloadConfig();
            });
        } else if (position == enableDoHRow) {
            ColibriXConfig.toggleWsEnableDoH();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.wsUseDoH);
            }
            ColibriXConfig.wsReloadConfig();
        }
    }

    @Override
    protected BaseListAdapter createAdapter(Context context) {
        return new ListAdapter(context);
    }

    @Override
    protected String getActionBarTitle() {
        return ColibriXConfig.WS_ADDRESS;
    }

    @Override
    protected void updateRows() {
        rowCount = 0;

        settingsRow = rowCount++;
        enableTLSRow = rowCount++;
        localProxyRow = rowCount++;
        enableDoHRow = rowCount++;
        descriptionRow = rowCount++;
    }

    @Override
    protected boolean hasWhiteActionBar() {
        return false;
    }

    private class ListAdapter extends BaseListAdapter {

        public ListAdapter(Context context) {
            super(context);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 2: {
                    TextSettingsCell textCell = (TextSettingsCell) holder.itemView;
                    textCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (position == localProxyRow) {
                        String value = ColibriXConfig.wsUseMTP ? LocaleController.getString("UseProxyTelegram", R.string.UseProxyTelegram) : LocaleController.getString("UseProxySocks5", R.string.UseProxySocks5);
                        textCell.setTextAndValue(LocaleController.getString("WsLocalProxy", R.string.WsLocalProxy), value, true);
                    }
                    break;
                }
                case 3: {
                    TextCheckCell textCell = (TextCheckCell) holder.itemView;
                    if (position == enableTLSRow) {
                        textCell.setTextAndCheck(LocaleController.getString("WsEnableTls", R.string.WsEnableTls), ColibriXConfig.wsEnableTLS, true);
                    } else if (position == enableDoHRow) {
                        textCell.setTextAndCheck(LocaleController.getString("WsEnableDoh", R.string.WsEnableDoh), ColibriXConfig.wsUseDoH, false);
                    }
                    break;
                }
                case 4: {
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == settingsRow) {
                        headerCell.setText(LocaleController.getString("Settings", R.string.Settings));
                    }
                    break;
                }
                case 7: {
                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                    cell.setText(getSpannedString("WsDescription", R.string.WsDescription, "https://appsbox.ru/proxy"));
                    cell.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    break;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == descriptionRow) {
                return 7;
            } else if (position == settingsRow) {
                return 4;
            } else if (position == enableTLSRow || position == enableDoHRow) {
                return 3;
            }
            return 2;
        }
    }

    @Override
    public void onProvideAssistContent(AssistContent outContent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            outContent.setWebUri(Uri.parse("https://appsbox.ru/proxy"));
        }
    }
}
