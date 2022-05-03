package org.viento.colibrix.settings;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.core.text.HtmlCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.LanguageDetector;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.BulletinFactory;

import java.util.ArrayList;
import java.util.Locale;

import org.viento.colibrix.ColibriXConfig;
import org.viento.colibrix.helpers.PopupHelper;
import org.viento.colibrix.translator.DeepLTranslator;
import org.viento.colibrix.translator.Translator;

public class ColibriXGeneralSettingsActivity extends BaseColibriXSettingsActivity {

    private final boolean supportLanguageDetector;

    private int connectionRow;
    private int ipv6Row;
    private int connection2Row;

    private int translatorRow;
    private int translatorTypeRow;
    private int deepLFormalityRow;
    private int translationProviderRow;
    private int translationTargetRow;
    private int doNotTranslateRow;
    private int autoTranslateRow;
    private int translator2Row;

    private int notificationRow;
    private int accentAsNotificationColorRow;
    private int silenceNonContactsRow;
    private int notification2Row;
    private int generalRow;
    private int disabledInstantCameraRow;
    private int askBeforeCallRow;
    private int openArchiveOnPullRow;
    private int nameOrderRow;
    private int idTypeRow;
    private int general2Row;

    public ColibriXGeneralSettingsActivity() {
        supportLanguageDetector = LanguageDetector.hasSupport();
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == ipv6Row) {
            ColibriXConfig.toggleIPv6();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.useIPv6);
            }
            for (int a = 0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
                if (UserConfig.getInstance(a).isClientActivated()) {
                    ConnectionsManager.getInstance(a).checkConnection();
                }
            }
        } else if (position == disabledInstantCameraRow) {
            ColibriXConfig.toggleDisabledInstantCamera();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.disableInstantCamera);
            }
        } else if (position == nameOrderRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add(LocaleController.getString("FirstLast", R.string.FirstLast));
            types.add(1);
            arrayList.add(LocaleController.getString("LastFirst", R.string.LastFirst));
            types.add(2);
            PopupHelper.show(arrayList, LocaleController.getString("NameOrder", R.string.NameOrder), types.indexOf(ColibriXConfig.nameOrder), getParentActivity(), view, i -> {
                ColibriXConfig.setNameOrder(types.get(i));
                listAdapter.notifyItemChanged(nameOrderRow);
                parentLayout.rebuildAllFragmentViews(false, false);
            });
        } else if (position == translationProviderRow) {
            final String oldProvider = ColibriXConfig.translationProvider;
            Translator.showTranslationProviderSelector(getParentActivity(), view, param -> {
                if (param) {
                    listAdapter.notifyItemChanged(translationProviderRow);
                } else {
                    listAdapter.notifyItemChanged(translationProviderRow);
                    listAdapter.notifyItemChanged(translationTargetRow);
                }
                if (!oldProvider.equals(ColibriXConfig.translationProvider)) {
                    if (oldProvider.equals(Translator.PROVIDER_DEEPL)) {
                        listAdapter.notifyItemRemoved(deepLFormalityRow);
                        updateRows();
                    } else if (ColibriXConfig.translationProvider.equals(Translator.PROVIDER_DEEPL)) {
                        updateRows();
                        listAdapter.notifyItemInserted(deepLFormalityRow);
                    }
                }
            });
        } else if (position == translationTargetRow) {
            Translator.showTranslationTargetSelector(this, view, () -> {
                listAdapter.notifyItemChanged(translationTargetRow);
                if (getRestrictedLanguages().size() == 1) {
                    listAdapter.notifyItemChanged(doNotTranslateRow);
                }
            });
        } else if (position == deepLFormalityRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add(LocaleController.getString("DeepLFormalityDefault", R.string.DeepLFormalityDefault));
            types.add(DeepLTranslator.FORMALITY_DEFAULT);
            arrayList.add(LocaleController.getString("DeepLFormalityMore", R.string.DeepLFormalityMore));
            types.add(DeepLTranslator.FORMALITY_MORE);
            arrayList.add(LocaleController.getString("DeepLFormalityLess", R.string.DeepLFormalityLess));
            types.add(DeepLTranslator.FORMALITY_LESS);
            PopupHelper.show(arrayList, LocaleController.getString("DeepLFormality", R.string.DeepLFormality), types.indexOf(ColibriXConfig.deepLFormality), getParentActivity(), view, i -> {
                ColibriXConfig.setDeepLFormality(types.get(i));
                listAdapter.notifyItemChanged(deepLFormalityRow);
            });
        } else if (position == openArchiveOnPullRow) {
            ColibriXConfig.toggleOpenArchiveOnPull();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.openArchiveOnPull);
            }
        } else if (position == askBeforeCallRow) {
            ColibriXConfig.toggleAskBeforeCall();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.askBeforeCall);
            }
        } else if (position == idTypeRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add(LocaleController.getString("IdTypeHidden", R.string.IdTypeHidden));
            types.add(ColibriXConfig.ID_TYPE_HIDDEN);
            arrayList.add(LocaleController.getString("IdTypeAPI", R.string.IdTypeAPI));
            types.add(ColibriXConfig.ID_TYPE_API);
            arrayList.add(LocaleController.getString("IdTypeBOTAPI", R.string.IdTypeBOTAPI));
            types.add(ColibriXConfig.ID_TYPE_BOTAPI);
            PopupHelper.show(arrayList, LocaleController.getString("IdType", R.string.IdType), types.indexOf(ColibriXConfig.idType), getParentActivity(), view, i -> {
                ColibriXConfig.setIdType(types.get(i));
                listAdapter.notifyItemChanged(idTypeRow);
                parentLayout.rebuildAllFragmentViews(false, false);
            });
        } else if (position == accentAsNotificationColorRow) {
            ColibriXConfig.toggleAccentAsNotificationColor();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.accentAsNotificationColor);
            }
        } else if (position == silenceNonContactsRow) {
            ColibriXConfig.toggleSilenceNonContacts();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.silenceNonContacts);
            }
        } else if (position == translatorTypeRow) {
            int oldType = ColibriXConfig.transType;
            Translator.showTranslatorTypeSelector(getParentActivity(), view, () -> {
                int newType = ColibriXConfig.transType;
                listAdapter.notifyItemChanged(translatorTypeRow);
                if (oldType != newType) {
                    if (oldType == ColibriXConfig.TRANS_TYPE_EXTERNAL) {
                        updateRows();
                        listAdapter.notifyItemRangeInserted(translationProviderRow, ColibriXConfig.translationProvider.equals(Translator.PROVIDER_DEEPL) ? 5 : 4);
                    } else if (newType == ColibriXConfig.TRANS_TYPE_EXTERNAL) {
                        listAdapter.notifyItemRangeRemoved(translationProviderRow, ColibriXConfig.translationProvider.equals(Translator.PROVIDER_DEEPL) ? 5 : 4);
                        updateRows();
                    }
                }
            });
        } else if (position == doNotTranslateRow) {
            if (!supportLanguageDetector) {
                BulletinFactory.of(this).createErrorBulletinSubtitle(LocaleController.getString("BrokenMLKit", R.string.BrokenMLKit), LocaleController.getString("BrokenMLKitDetail", R.string.BrokenMLKitDetail), null).show();
                return;
            }
            presentFragment(new ColibriXLanguagesSelectActivity(ColibriXLanguagesSelectActivity.TYPE_RESTRICTED, true));
        } else if (position == autoTranslateRow) {
            if (!supportLanguageDetector) {
                BulletinFactory.of(this).createErrorBulletinSubtitle(LocaleController.getString("BrokenMLKit", R.string.BrokenMLKit), LocaleController.getString("BrokenMLKitDetail", R.string.BrokenMLKitDetail), null).show();
                return;
            }
            ColibriXConfig.toggleAutoTranslate();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.autoTranslate);
            }
        }
    }

    @Override
    protected BaseListAdapter createAdapter(Context context) {
        return new ListAdapter(context);
    }

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString("General", R.string.General);
    }

    @Override
    protected void updateRows() {
        super.updateRows();

        connectionRow = addRow("connection");
        ipv6Row = addRow("ipv6");
        connection2Row = addRow();

        translatorRow = addRow("translator");
        translatorTypeRow = addRow("translatorType");
        if (ColibriXConfig.transType != ColibriXConfig.TRANS_TYPE_EXTERNAL) {
            translationProviderRow = addRow("translationProvider");
            deepLFormalityRow = ColibriXConfig.translationProvider.equals(Translator.PROVIDER_DEEPL) ? addRow("deepLFormality") : -1;
            translationTargetRow = addRow("translationTarget");
            doNotTranslateRow = addRow("doNotTranslate");
            autoTranslateRow = addRow("autoTranslate");
        } else {
            translationProviderRow = -1;
            deepLFormalityRow = -1;
            translationTargetRow = -1;
            doNotTranslateRow = -1;
            autoTranslateRow = -1;
        }
        translator2Row = addRow();

        notificationRow = addRow("notification");
        accentAsNotificationColorRow = addRow("accentAsNotificationColor");
        silenceNonContactsRow = addRow("silenceNonContacts");
        notification2Row = addRow();

        generalRow = addRow("general");
        disabledInstantCameraRow = addRow("disabledInstantCamera");
        askBeforeCallRow = addRow("askBeforeCall");
        openArchiveOnPullRow = addRow("openArchiveOnPull");
        nameOrderRow = addRow("nameOrder");
        idTypeRow = addRow("idType");
        general2Row = addRow();
    }

    @Override
    protected String getKey() {
        return "g";
    }

    private ArrayList<String> getRestrictedLanguages() {
        String currentLang = Translator.stripLanguageCode(Translator.getCurrentTranslator().getCurrentTargetLanguage());
        ArrayList<String> langCodes = new ArrayList<>(ColibriXConfig.restrictedLanguages);
        if (!langCodes.contains(currentLang)) {
            langCodes.add(currentLang);
        }
        return langCodes;
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
                    textCell.setCanDisable(true);
                    textCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (position == nameOrderRow) {
                        String value;
                        switch (ColibriXConfig.nameOrder) {
                            case 2:
                                value = LocaleController.getString("LastFirst", R.string.LastFirst);
                                break;
                            case 1:
                            default:
                                value = LocaleController.getString("FirstLast", R.string.FirstLast);
                                break;
                        }
                        textCell.setTextAndValue(LocaleController.getString("NameOrder", R.string.NameOrder), value, true);
                    } else if (position == translationProviderRow) {
                        Pair<ArrayList<String>, ArrayList<String>> providers = Translator.getProviders();
                        ArrayList<String> names = providers.first;
                        ArrayList<String> types = providers.second;
                        if (names == null || types == null) {
                            return;
                        }
                        int index = types.indexOf(ColibriXConfig.translationProvider);
                        if (index < 0) {
                            textCell.setTextAndValue(LocaleController.getString("TranslationProviderShort", R.string.TranslationProviderShort), "", true);
                        } else {
                            String value = names.get(index);
                            textCell.setTextAndValue(LocaleController.getString("TranslationProviderShort", R.string.TranslationProviderShort), value, true);
                        }
                    } else if (position == translationTargetRow) {
                        String language = ColibriXConfig.translationTarget;
                        CharSequence value;
                        if (language.equals("app")) {
                            value = LocaleController.getString("TranslationTargetApp", R.string.TranslationTargetApp);
                        } else {
                            Locale locale = Locale.forLanguageTag(language);
                            if (!TextUtils.isEmpty(locale.getScript())) {
                                value = HtmlCompat.fromHtml(locale.getDisplayScript(), HtmlCompat.FROM_HTML_MODE_LEGACY);
                            } else {
                                value = locale.getDisplayName();
                            }
                        }
                        textCell.setTextAndValue(LocaleController.getString("TranslationTarget", R.string.TranslationTarget), value, true);
                    } else if (position == deepLFormalityRow) {
                        String value;
                        switch (ColibriXConfig.deepLFormality) {
                            case DeepLTranslator.FORMALITY_DEFAULT:
                                value = LocaleController.getString("DeepLFormalityDefault", R.string.DeepLFormalityDefault);
                                break;
                            case DeepLTranslator.FORMALITY_MORE:
                                value = LocaleController.getString("DeepLFormalityMore", R.string.DeepLFormalityMore);
                                break;
                            case DeepLTranslator.FORMALITY_LESS:
                            default:
                                value = LocaleController.getString("DeepLFormalityLess", R.string.DeepLFormalityLess);
                                break;
                        }
                        textCell.setTextAndValue(LocaleController.getString("DeepLFormality", R.string.DeepLFormality), value, true);
                    } else if (position == idTypeRow) {
                        String value;
                        switch (ColibriXConfig.idType) {
                            case ColibriXConfig.ID_TYPE_HIDDEN:
                                value = LocaleController.getString("IdTypeHidden", R.string.IdTypeHidden);
                                break;
                            case ColibriXConfig.ID_TYPE_BOTAPI:
                                value = LocaleController.getString("IdTypeBOTAPI", R.string.IdTypeBOTAPI);
                                break;
                            case ColibriXConfig.ID_TYPE_API:
                            default:
                                value = LocaleController.getString("IdTypeAPI", R.string.IdTypeAPI);
                                break;
                        }
                        textCell.setTextAndValue(LocaleController.getString("IdType", R.string.IdType), value, false);
                    } else if (position == translatorTypeRow) {
                        String value;
                        switch (ColibriXConfig.transType) {
                            case ColibriXConfig.TRANS_TYPE_TG:
                                value = LocaleController.getString("TranslatorTypeTG", R.string.TranslatorTypeTG);
                                break;
                            case ColibriXConfig.TRANS_TYPE_EXTERNAL:
                                value = LocaleController.getString("TranslatorTypeExternal", R.string.TranslatorTypeExternal);
                                break;
                            case ColibriXConfig.TRANS_TYPE_COLIBRIX:
                            default:
                                value = LocaleController.getString("TranslatorTypeColibriX", R.string.TranslatorTypeColibriX);
                                break;
                        }
                        textCell.setTextAndValue(LocaleController.getString("TranslatorType", R.string.TranslatorType), value, position + 1 != translator2Row);
                    } else if (position == doNotTranslateRow) {
                        ArrayList<String> langCodes = getRestrictedLanguages();
                        CharSequence value;
                        if (langCodes.size() == 1) {
                            Locale locale = Locale.forLanguageTag(langCodes.get(0));
                            if (!TextUtils.isEmpty(locale.getScript())) {
                                value = HtmlCompat.fromHtml(locale.getDisplayScript(), HtmlCompat.FROM_HTML_MODE_LEGACY);
                            } else {
                                value = locale.getDisplayName();
                            }
                        } else {
                            value = LocaleController.formatPluralString("Languages", langCodes.size());
                        }
                        textCell.setTextAndValue(LocaleController.getString("DoNotTranslate", R.string.DoNotTranslate), value, true);
                    }
                    break;
                }
                case 3: {
                    TextCheckCell textCell = (TextCheckCell) holder.itemView;
                    textCell.setEnabled(true, null);
                    if (position == ipv6Row) {
                        textCell.setTextAndCheck(LocaleController.getString("IPv6", R.string.IPv6), ColibriXConfig.useIPv6, false);
                    } else if (position == disabledInstantCameraRow) {
                        textCell.setTextAndCheck(LocaleController.getString("DisableInstantCamera", R.string.DisableInstantCamera), ColibriXConfig.disableInstantCamera, true);
                    } else if (position == openArchiveOnPullRow) {
                        textCell.setTextAndCheck(LocaleController.getString("OpenArchiveOnPull", R.string.OpenArchiveOnPull), ColibriXConfig.openArchiveOnPull, true);
                    } else if (position == askBeforeCallRow) {
                        textCell.setTextAndCheck(LocaleController.getString("AskBeforeCalling", R.string.AskBeforeCalling), ColibriXConfig.askBeforeCall, true);
                    } else if (position == accentAsNotificationColorRow) {
                        textCell.setTextAndCheck(LocaleController.getString("AccentAsNotificationColor", R.string.AccentAsNotificationColor), ColibriXConfig.accentAsNotificationColor, true);
                    } else if (position == silenceNonContactsRow) {
                        textCell.setTextAndCheck(LocaleController.getString("SilenceNonContacts", R.string.SilenceNonContacts), ColibriXConfig.silenceNonContacts, false);
                    } else if (position == autoTranslateRow) {
                        textCell.setEnabled(supportLanguageDetector, null);
                        textCell.setTextAndValueAndCheck(LocaleController.getString("AutoTranslate", R.string.AutoTranslate), LocaleController.getString("AutoTranslateAbout", R.string.AutoTranslateAbout), ColibriXConfig.autoTranslate, true, false);
                    }
                    break;
                }
                case 4: {
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == generalRow) {
                        headerCell.setText(LocaleController.getString("General", R.string.General));
                    } else if (position == connectionRow) {
                        headerCell.setText(LocaleController.getString("Connection", R.string.Connection));
                    } else if (position == notificationRow) {
                        headerCell.setText(LocaleController.getString("Notifications", R.string.Notifications));
                    } else if (position == translatorRow) {
                        headerCell.setText(LocaleController.getString("Translator", R.string.Translator));
                    }
                    break;
                }
                case 7: {
                    TextInfoPrivacyCell cell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == general2Row) {
                        cell.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        cell.setText(LocaleController.getString("IdTypeAbout", R.string.IdTypeAbout));
                    } else if (position == notification2Row) {
                        cell.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                        cell.setText(LocaleController.getString("SilenceNonContactsAbout", R.string.SilenceNonContactsAbout));
                    } else if (position == translator2Row) {
                        cell.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                        cell.setText(LocaleController.getString("TranslateMessagesInfo1", R.string.TranslateMessagesInfo1));
                    }
                    break;
                }
                case 9: {
                    DrawerProfilePreviewCell cell = (DrawerProfilePreviewCell) holder.itemView;
                    cell.setUser(getUserConfig().getCurrentUser(), false);
                    break;
                }
            }
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int position = holder.getAdapterPosition();
            if (position == autoTranslateRow || position == doNotTranslateRow) {
                return supportLanguageDetector;
            }
            return super.isEnabled(holder);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == connection2Row) {
                return 1;
            } else if (position == nameOrderRow || position == idTypeRow || position == translatorTypeRow ||
                    (position >= translationProviderRow && position <= doNotTranslateRow)) {
                return 2;
            } else if (position == ipv6Row || position == autoTranslateRow ||
                    (position > generalRow && position < nameOrderRow) ||
                    (position > notificationRow && position < notification2Row)) {
                return 3;
            } else if (position == generalRow || position == connectionRow || position == notificationRow ||
                    position == translatorRow) {
                return 4;
            } else if (position == general2Row || position == notification2Row || position == translator2Row) {
                return 7;
            }
            return 2;
        }
    }
}
