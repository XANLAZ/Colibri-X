package org.viento.colibrix.settings;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextCheckbox2Cell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBarView;

import java.util.ArrayList;

import org.viento.colibrix.ColibriXConfig;
import org.viento.colibrix.helpers.PopupHelper;
import org.viento.colibrix.helpers.VoiceEnhancementsHelper;

public class ColibriXChatSettingsActivity extends BaseColibriXSettingsActivity implements NotificationCenter.NotificationCenterDelegate {

    private ActionBarMenuItem resetItem;
    private StickerSizeCell stickerSizeCell;

    private int stickerSizeHeaderRow;
    private int stickerSizeRow;
    private int stickerSize2Row;

    private int chatRow;
    private int ignoreBlockedRow;
    private int hideKeyboardOnChatScrollRow;
    private int tryToOpenAllLinksInIVRow;
    private int disableJumpToNextRow;
    private int disableGreetingStickerRow;
    private int disableMarkdownByDefaultRow;
    private int doubleTapActionRow;
    private int maxRecentStickersRow;
    private int chat2Row;

    private int mediaRow;
    private int hqVoiceMessageRow;
    private int voiceEnhancementsRow;
    private int disablePhotoSideActionRow;
    private int rearVideoMessagesRow;
    private int confirmAVRow;
    private int disableProximityEventsRow;
    private int disableVoiceMessageAutoPlayRow;
    private int autoPauseVideoRow;
    private int media2Row;

    private int messageMenuRow;
    private int messageMenu2Row;

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();

        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);

        return true;
    }

    @Override
    public View createView(Context context) {
        View fragmentView = super.createView(context);

        ActionBarMenu menu = actionBar.createMenu();
        resetItem = menu.addItem(0, R.drawable.msg_reset);
        resetItem.setContentDescription(LocaleController.getString("ResetStickerSize", R.string.ResetStickerSize));
        resetItem.setVisibility(ColibriXConfig.stickerSize != 14.0f ? View.VISIBLE : View.GONE);
        resetItem.setTag(null);
        resetItem.setOnClickListener(v -> {
            AndroidUtilities.updateViewVisibilityAnimated(resetItem, false, 0.5f, true);
            ValueAnimator animator = ValueAnimator.ofFloat(ColibriXConfig.stickerSize, 14.0f);
            animator.setDuration(150);
            animator.addUpdateListener(valueAnimator -> {
                ColibriXConfig.setStickerSize((Float) valueAnimator.getAnimatedValue());
                stickerSizeCell.invalidate();
            });
            animator.start();
        });

        return fragmentView;
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == ignoreBlockedRow) {
            ColibriXConfig.toggleIgnoreBlocked();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.ignoreBlocked);
            }
        } else if (position == disablePhotoSideActionRow) {
            ColibriXConfig.toggleDisablePhotoSideAction();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.disablePhotoSideAction);
            }
        } else if (position == hideKeyboardOnChatScrollRow) {
            ColibriXConfig.toggleHideKeyboardOnChatScroll();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.hideKeyboardOnChatScroll);
            }
        } else if (position == rearVideoMessagesRow) {
            ColibriXConfig.toggleRearVideoMessages();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.rearVideoMessages);
            }
        } else if (position == confirmAVRow) {
            ColibriXConfig.toggleConfirmAVMessage();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.confirmAVMessage);
            }
        } else if (position == disableProximityEventsRow) {
            ColibriXConfig.toggleDisableProximityEvents();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.disableProximityEvents);
            }
            showRestartBulletin();
        } else if (position == tryToOpenAllLinksInIVRow) {
            ColibriXConfig.toggleTryToOpenAllLinksInIV();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.tryToOpenAllLinksInIV);
            }
        } else if (position == autoPauseVideoRow) {
            ColibriXConfig.toggleAutoPauseVideo();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.autoPauseVideo);
            }
        } else if (position == disableJumpToNextRow) {
            ColibriXConfig.toggleDisableJumpToNextChannel();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.disableJumpToNextChannel);
            }
        } else if (position == disableGreetingStickerRow) {
            ColibriXConfig.toggleDisableGreetingSticker();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.disableGreetingSticker);
            }
        } else if (position == disableVoiceMessageAutoPlayRow) {
            ColibriXConfig.toggleDisableVoiceMessageAutoPlay();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.disableVoiceMessageAutoPlay);
            }
        } else if (position == doubleTapActionRow) {
            ArrayList<String> arrayList = new ArrayList<>();
            ArrayList<Integer> types = new ArrayList<>();
            arrayList.add(LocaleController.getString("Disable", R.string.Disable));
            types.add(ColibriXConfig.DOUBLE_TAP_ACTION_NONE);
            arrayList.add(LocaleController.getString("Reactions", R.string.Reactions));
            types.add(ColibriXConfig.DOUBLE_TAP_ACTION_REACTION);
            arrayList.add(LocaleController.getString("TranslateMessage", R.string.TranslateMessage));
            types.add(ColibriXConfig.DOUBLE_TAP_ACTION_TRANSLATE);
            arrayList.add(LocaleController.getString("Reply", R.string.Reply));
            types.add(ColibriXConfig.DOUBLE_TAP_ACTION_REPLY);
            arrayList.add(LocaleController.getString("AddToSavedMessages", R.string.AddToSavedMessages));
            types.add(ColibriXConfig.DOUBLE_TAP_ACTION_SAVE);
            arrayList.add(LocaleController.getString("Repeat", R.string.Repeat));
            types.add(ColibriXConfig.DOUBLE_TAP_ACTION_REPEAT);
            arrayList.add(LocaleController.getString("Edit", R.string.Edit));
            types.add(ColibriXConfig.DOUBLE_TAP_ACTION_EDIT);
            PopupHelper.show(arrayList, LocaleController.getString("DoubleTapAction", R.string.DoubleTapAction), types.indexOf(ColibriXConfig.doubleTapAction), getParentActivity(), view, i -> {
                ColibriXConfig.setDoubleTapAction(types.get(i));
                listAdapter.notifyItemChanged(doubleTapActionRow);
            });
        } else if (position == disableMarkdownByDefaultRow) {
            ColibriXConfig.toggleDisableMarkdownByDefault();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.disableMarkdownByDefault);
            }
        } else if (position > messageMenuRow && position < messageMenu2Row) {
            TextCheckbox2Cell cell = ((TextCheckbox2Cell) view);
            int menuPosition = position - messageMenuRow - 1;
            if (menuPosition == 0) {
                ColibriXConfig.toggleShowDeleteDownloadedFile();
                cell.setChecked(ColibriXConfig.showDeleteDownloadedFile);
            } else if (menuPosition == 1) {
                ColibriXConfig.toggleShowNoQuoteForward();
                cell.setChecked(ColibriXConfig.showNoQuoteForward);
            } else if (menuPosition == 2) {
                ColibriXConfig.toggleShowAddToSavedMessages();
                cell.setChecked(ColibriXConfig.showAddToSavedMessages);
            } else if (menuPosition == 3) {
                ColibriXConfig.toggleShowRepeat();
                cell.setChecked(ColibriXConfig.showRepeat);
            } else if (menuPosition == 4) {
                ColibriXConfig.toggleShowViewHistory();
                cell.setChecked(ColibriXConfig.showViewHistory);
            } else if (menuPosition == 5) {
                ColibriXConfig.toggleShowTranslate();
                cell.setChecked(ColibriXConfig.showTranslate);
            } else if (menuPosition == 6) {
                ColibriXConfig.toggleShowReport();
                cell.setChecked(ColibriXConfig.showReport);
            } else if (menuPosition == 7) {
                ColibriXConfig.toggleShowAdminActions();
                cell.setChecked(ColibriXConfig.showAdminActions);
            } else if (menuPosition == 8) {
                ColibriXConfig.toggleShowChangePermissions();
                cell.setChecked(ColibriXConfig.showChangePermissions);
            } else if (menuPosition == 9) {
                ColibriXConfig.toggleShowMessageDetails();
                cell.setChecked(ColibriXConfig.showMessageDetails);
            } else if (menuPosition == 10) {
                ColibriXConfig.toggleShowCopyPhoto();
                cell.setChecked(ColibriXConfig.showCopyPhoto);
            }
        } else if (position == hqVoiceMessageRow) {
            ColibriXConfig.toggleIncreaseVoiceMessageQuality();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.increaseVoiceMessageQuality);
            }
            showRestartBulletin();
        } else if (position == voiceEnhancementsRow) {
            ColibriXConfig.toggleVoiceEnhancements();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(ColibriXConfig.voiceEnhancements);
            }
        } else if (position == maxRecentStickersRow) {
            int[] counts = {20, 30, 40, 50, 80, 100, 120, 150, 180, 200};
            ArrayList<String> types = new ArrayList<>();
            for (int count : counts) {
                if (count <= getMessagesController().maxRecentStickersCount) {
                    types.add(String.valueOf(count));
                }
            }
            PopupHelper.show(types, LocaleController.getString("MaxRecentStickers", R.string.MaxRecentStickers), types.indexOf(String.valueOf(ColibriXConfig.maxRecentStickers)), getParentActivity(), view, i -> {
                ColibriXConfig.setMaxRecentStickers(Integer.parseInt(types.get(i)));
                listAdapter.notifyItemChanged(maxRecentStickersRow);
            });
        }
    }

    @Override
    protected BaseListAdapter createAdapter(Context context) {
        return new ListAdapter(context);
    }

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString("Chat", R.string.Chat);
    }

    @Override
    protected void updateRows() {
        super.updateRows();

        stickerSizeHeaderRow = addRow("stickerSizeHeader");
        stickerSizeRow = addRow("stickerSize");
        stickerSize2Row = addRow();

        chatRow = addRow("chat");
        ignoreBlockedRow = addRow("ignoreBlocked");
        hideKeyboardOnChatScrollRow = addRow("hideKeyboardOnChatScroll");
        tryToOpenAllLinksInIVRow = addRow("tryToOpenAllLinksInIV");
        disableJumpToNextRow = addRow("disableJumpToNext");
        disableGreetingStickerRow = addRow("disableGreetingSticker");
        disableMarkdownByDefaultRow = addRow("disableMarkdownByDefault");
        doubleTapActionRow = addRow("doubleTapAction");
        maxRecentStickersRow = addRow("maxRecentStickers");
        chat2Row = addRow();

        mediaRow = addRow("media");
        hqVoiceMessageRow = addRow("hqVoiceMessage");
        voiceEnhancementsRow = VoiceEnhancementsHelper.isAvailable() ? addRow("voiceEnhancements") : -1;
        disablePhotoSideActionRow = addRow("disablePhotoSideAction");
        rearVideoMessagesRow = addRow("rearVideoMessages");
        confirmAVRow = addRow("confirmAV");
        disableProximityEventsRow = addRow("disableProximityEvents");
        disableVoiceMessageAutoPlayRow = addRow("disableVoiceMessageAutoPlay");
        autoPauseVideoRow = addRow("autoPauseVideo");
        media2Row = addRow();

        messageMenuRow = addRow("messageMenu");
        rowCount += 11;
        messageMenu2Row = addRow();
    }

    @Override
    protected String getKey() {
        return "c";
    }

    @Override
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.emojiLoaded) {
            if (listView != null) {
                listView.invalidateViews();
            }
        }
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
    }

    private class StickerSizeCell extends FrameLayout {

        private final StickerSizePreviewMessagesCell messagesCell;
        private final SeekBarView sizeBar;
        private final int startStickerSize = 2;
        private final int endStickerSize = 20;

        private final TextPaint textPaint;
        private int lastWidth;

        public StickerSizeCell(Context context) {
            super(context);

            setWillNotDraw(false);

            textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            textPaint.setTextSize(AndroidUtilities.dp(16));

            sizeBar = new SeekBarView(context);
            sizeBar.setReportChanges(true);
            sizeBar.setDelegate(new SeekBarView.SeekBarViewDelegate() {
                @Override
                public void onSeekBarDrag(boolean stop, float progress) {
                    sizeBar.getSeekBarAccessibilityDelegate().postAccessibilityEventRunnable(StickerSizeCell.this);
                    ColibriXConfig.setStickerSize(startStickerSize + (endStickerSize - startStickerSize) * progress);
                    StickerSizeCell.this.invalidate();
                    if (resetItem.getVisibility() != VISIBLE) {
                        AndroidUtilities.updateViewVisibilityAnimated(resetItem, true, 0.5f, true);
                    }
                }

                @Override
                public void onSeekBarPressed(boolean pressed) {

                }
            });
            sizeBar.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
            addView(sizeBar, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 38, Gravity.LEFT | Gravity.TOP, 9, 5, 43, 11));

            messagesCell = new StickerSizePreviewMessagesCell(context, parentLayout);
            messagesCell.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
            addView(messagesCell, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.TOP, 0, 53, 0, 0));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            textPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteValueText));
            canvas.drawText(String.valueOf(Math.round(ColibriXConfig.stickerSize)), getMeasuredWidth() - AndroidUtilities.dp(39), AndroidUtilities.dp(28), textPaint);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int width = MeasureSpec.getSize(widthMeasureSpec);
            if (lastWidth != width) {
                sizeBar.setProgress((ColibriXConfig.stickerSize - startStickerSize) / (float) (endStickerSize - startStickerSize));
                lastWidth = width;
            }
        }

        @Override
        public void invalidate() {
            super.invalidate();
            lastWidth = -1;
            messagesCell.invalidate();
            sizeBar.invalidate();
        }

        @Override
        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(event);
            sizeBar.getSeekBarAccessibilityDelegate().onInitializeAccessibilityEvent(this, event);
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            sizeBar.getSeekBarAccessibilityDelegate().onInitializeAccessibilityNodeInfoInternal(this, info);
        }

        @Override
        public boolean performAccessibilityAction(int action, Bundle arguments) {
            return super.performAccessibilityAction(action, arguments) || sizeBar.getSeekBarAccessibilityDelegate().performAccessibilityActionInternal(this, action, arguments);
        }
    }

    private class ListAdapter extends BaseListAdapter {

        public ListAdapter(Context context) {
            super(context);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 1: {
                    if (position == messageMenu2Row) {
                        holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    } else {
                        holder.itemView.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    }
                    break;
                }
                case 2: {
                    TextSettingsCell textCell = (TextSettingsCell) holder.itemView;
                    textCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (position == stickerSizeRow) {
                        textCell.setTextAndValue(LocaleController.getString("StickerSize", R.string.StickerSize), String.valueOf(Math.round(ColibriXConfig.stickerSize)), true);
                    } else if (position == messageMenuRow) {
                        textCell.setText(LocaleController.getString("MessageMenu", R.string.MessageMenu), false);
                    } else if (position == doubleTapActionRow) {
                        String value;
                        switch (ColibriXConfig.doubleTapAction) {
                            case ColibriXConfig.DOUBLE_TAP_ACTION_REACTION:
                                value = LocaleController.getString("Reactions", R.string.Reactions);
                                break;
                            case ColibriXConfig.DOUBLE_TAP_ACTION_TRANSLATE:
                                value = LocaleController.getString("TranslateMessage", R.string.TranslateMessage);
                                break;
                            case ColibriXConfig.DOUBLE_TAP_ACTION_REPLY:
                                value = LocaleController.getString("Reply", R.string.Reply);
                                break;
                            case ColibriXConfig.DOUBLE_TAP_ACTION_SAVE:
                                value = LocaleController.getString("AddToSavedMessages", R.string.AddToSavedMessages);
                                break;
                            case ColibriXConfig.DOUBLE_TAP_ACTION_REPEAT:
                                value = LocaleController.getString("Repeat", R.string.Repeat);
                                break;
                            case ColibriXConfig.DOUBLE_TAP_ACTION_EDIT:
                                value = LocaleController.getString("Edit", R.string.Edit);
                                break;
                            case ColibriXConfig.DOUBLE_TAP_ACTION_NONE:
                            default:
                                value = LocaleController.getString("Disable", R.string.Disable);
                        }
                        textCell.setTextAndValue(LocaleController.getString("DoubleTapAction", R.string.DoubleTapAction), value, true);
                    } else if (position == maxRecentStickersRow) {
                        textCell.setTextAndValue(LocaleController.getString("MaxRecentStickers", R.string.MaxRecentStickers), String.valueOf(ColibriXConfig.maxRecentStickers), false);
                    }
                    break;
                }
                case 3: {
                    TextCheckCell textCell = (TextCheckCell) holder.itemView;
                    textCell.setEnabled(true, null);
                    if (position == ignoreBlockedRow) {
                        textCell.setTextAndValueAndCheck(LocaleController.getString("IgnoreBlocked", R.string.IgnoreBlocked), LocaleController.getString("IgnoreBlockedAbout", R.string.IgnoreBlockedAbout), ColibriXConfig.ignoreBlocked, true, true);
                    } else if (position == disablePhotoSideActionRow) {
                        textCell.setTextAndCheck(LocaleController.getString("DisablePhotoViewerSideAction", R.string.DisablePhotoViewerSideAction), ColibriXConfig.disablePhotoSideAction, true);
                    } else if (position == hideKeyboardOnChatScrollRow) {
                        textCell.setTextAndCheck(LocaleController.getString("HideKeyboardOnChatScroll", R.string.HideKeyboardOnChatScroll), ColibriXConfig.hideKeyboardOnChatScroll, true);
                    } else if (position == rearVideoMessagesRow) {
                        textCell.setTextAndCheck(LocaleController.getString("RearVideoMessages", R.string.RearVideoMessages), ColibriXConfig.rearVideoMessages, true);
                    } else if (position == confirmAVRow) {
                        textCell.setTextAndCheck(LocaleController.getString("ConfirmAVMessage", R.string.ConfirmAVMessage), ColibriXConfig.confirmAVMessage, true);
                    } else if (position == disableProximityEventsRow) {
                        textCell.setTextAndCheck(LocaleController.getString("DisableProximityEvents", R.string.DisableProximityEvents), ColibriXConfig.disableProximityEvents, true);
                    } else if (position == tryToOpenAllLinksInIVRow) {
                        textCell.setTextAndCheck(LocaleController.getString("OpenAllLinksInInstantView", R.string.OpenAllLinksInInstantView), ColibriXConfig.tryToOpenAllLinksInIV, true);
                    } else if (position == autoPauseVideoRow) {
                        textCell.setTextAndValueAndCheck(LocaleController.getString("AutoPauseVideo", R.string.AutoPauseVideo), LocaleController.getString("AutoPauseVideoAbout", R.string.AutoPauseVideoAbout), ColibriXConfig.autoPauseVideo, true, false);
                    } else if (position == disableJumpToNextRow) {
                        textCell.setTextAndCheck(LocaleController.getString("DisableJumpToNextChannel", R.string.DisableJumpToNextChannel), ColibriXConfig.disableJumpToNextChannel, true);
                    } else if (position == disableGreetingStickerRow) {
                        textCell.setTextAndCheck(LocaleController.getString("DisableGreetingSticker", R.string.DisableGreetingSticker), ColibriXConfig.disableGreetingSticker, true);
                    } else if (position == disableVoiceMessageAutoPlayRow) {
                        textCell.setTextAndCheck(LocaleController.getString("DisableVoiceMessagesAutoPlay", R.string.DisableVoiceMessagesAutoPlay), ColibriXConfig.disableVoiceMessageAutoPlay, true);
                    } else if (position == disableMarkdownByDefaultRow) {
                        textCell.setTextAndCheck(LocaleController.getString("DisableMarkdownByDefault", R.string.DisableMarkdownByDefault), ColibriXConfig.disableMarkdownByDefault, true);
                    } else if (position == hqVoiceMessageRow) {
                        textCell.setTextAndCheck(LocaleController.getString("IncreaseVoiceMessageQuality", R.string.IncreaseVoiceMessageQuality), ColibriXConfig.increaseVoiceMessageQuality, true);
                    } else if (position == voiceEnhancementsRow) {
                        textCell.setTextAndValueAndCheck(LocaleController.getString("VoiceEnhancements", R.string.VoiceEnhancements), LocaleController.getString("VoiceEnhancementsAbout", R.string.VoiceEnhancementsAbout), ColibriXConfig.voiceEnhancements, true, true);
                    }
                    break;
                }
                case 4: {
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == chatRow) {
                        headerCell.setText(LocaleController.getString("Chat", R.string.Chat));
                    } else if (position == stickerSizeHeaderRow) {
                        headerCell.setText(LocaleController.getString("StickerSize", R.string.StickerSize));
                    } else if (position == messageMenuRow) {
                        headerCell.setText(LocaleController.getString("MessageMenu", R.string.MessageMenu));
                    } else if (position == mediaRow) {
                        headerCell.setText(LocaleController.getString("SharedMediaTab2", R.string.SharedMediaTab2));
                    }
                    break;
                }
                case 9: {
                    TextCheckbox2Cell cell = (TextCheckbox2Cell) holder.itemView;
                    int menuPosition = position - messageMenuRow - 1;
                    if (menuPosition == 0) {
                        cell.setTextAndCheck(LocaleController.getString("DeleteDownloadedFile", R.string.DeleteDownloadedFile), ColibriXConfig.showDeleteDownloadedFile, true);
                    } else if (menuPosition == 1) {
                        cell.setTextAndCheck(LocaleController.getString("NoQuoteForward", R.string.NoQuoteForward), ColibriXConfig.showNoQuoteForward, true);
                    } else if (menuPosition == 2) {
                        cell.setTextAndCheck(LocaleController.getString("AddToSavedMessages", R.string.AddToSavedMessages), ColibriXConfig.showAddToSavedMessages, true);
                    } else if (menuPosition == 3) {
                        cell.setTextAndCheck(LocaleController.getString("Repeat", R.string.Repeat), ColibriXConfig.showRepeat, true);
                    } else if (menuPosition == 4) {
                        cell.setTextAndCheck(LocaleController.getString("ViewHistory", R.string.ViewHistory), ColibriXConfig.showViewHistory, true);
                    } else if (menuPosition == 5) {
                        cell.setTextAndCheck(LocaleController.getString("TranslateMessage", R.string.TranslateMessage), ColibriXConfig.showTranslate, true);
                    } else if (menuPosition == 6) {
                        cell.setTextAndCheck(LocaleController.getString("ReportChat", R.string.ReportChat), ColibriXConfig.showReport, true);
                    } else if (menuPosition == 7) {
                        cell.setTextAndCheck(LocaleController.getString("EditAdminRights", R.string.EditAdminRights), ColibriXConfig.showAdminActions, true);
                    } else if (menuPosition == 8) {
                        cell.setTextAndCheck(LocaleController.getString("ChangePermissions", R.string.ChangePermissions), ColibriXConfig.showChangePermissions, true);
                    } else if (menuPosition == 9) {
                        cell.setTextAndCheck(LocaleController.getString("MessageDetails", R.string.MessageDetails), ColibriXConfig.showMessageDetails, true);
                    } else if (menuPosition == 10) {
                        cell.setTextAndCheck(LocaleController.getString("CopyPhoto", R.string.CopyPhoto), ColibriXConfig.showCopyPhoto, false);
                    }
                    break;
                }
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == Integer.MAX_VALUE) {
                stickerSizeCell = new StickerSizeCell(mContext);
                stickerSizeCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                stickerSizeCell.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                return new RecyclerListView.Holder(stickerSizeCell);
            } else {
                return super.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == chat2Row || position == stickerSize2Row || position == messageMenu2Row || position == media2Row) {
                return 1;
            } else if (position == doubleTapActionRow || position == maxRecentStickersRow) {
                return 2;
            } else if ((position > chatRow && position < doubleTapActionRow) || (position > mediaRow && position < media2Row)) {
                return 3;
            } else if (position == chatRow || position == stickerSizeHeaderRow || position == messageMenuRow || position == mediaRow) {
                return 4;
            } else if (position > messageMenuRow && position < messageMenu2Row) {
                return 9;
            } else if (position == stickerSizeRow) {
                return Integer.MAX_VALUE;
            }
            return 2;
        }
    }
}
