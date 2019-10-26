package com.masterlock.ble.app.view.lock.keysafe;

import android.content.Context;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.common.base.Strings;
import com.masterlock.ble.app.C1075R;
import com.masterlock.ble.app.view.lock.keysafe.ChangeSecondaryKeySafeAdapter.GenericViewHolder;
import java.util.LinkedList;
import java.util.List;

public class ChangeSecondaryKeySafeAdapter extends Adapter<GenericViewHolder> {
    /* access modifiers changed from: private */
    public boolean isListEmpty = true;
    private final Context mActivity;
    private RecyclerViewItemInteractionListener mRecyclerViewItemInteractionListener;
    private List<String> secondaryPasscodeList;

    public class GenericViewHolder extends ViewHolder {
        @InjectView(2131296743)
        ImageButton deleteButton;
        @InjectView(2131296426)
        LinearLayout digitContainer1;
        @InjectView(2131296427)
        LinearLayout digitContainer2;
        @InjectView(2131296428)
        LinearLayout digitContainer3;
        @InjectView(2131296429)
        LinearLayout digitContainer4;
        @InjectView(2131296430)
        LinearLayout digitContainer5;
        @InjectView(2131296431)
        LinearLayout digitContainer6;
        @InjectView(2131296432)
        LinearLayout digitContainer7;
        @InjectView(2131296433)
        View digitContainer8;
        private LinkedList<TextView> digitViewList = new LinkedList<>();
        @InjectView(2131296542)
        protected LinearLayout itemLayout;
        private RecyclerViewItemInteractionListener mRecyclerViewItemInteractionListener;
        private String mSecondaryCode;
        @InjectView(2131296800)
        TextView newCodeTxt;

        public GenericViewHolder(View view) {
            super(view);
            ButterKnife.inject((Object) this, view);
            if (ChangeSecondaryKeySafeAdapter.this.isListEmpty) {
                view.setVisibility(8);
            }
            this.digitViewList.clear();
            this.digitViewList.add((TextView) this.digitContainer1.findViewById(C1075R.C1077id.txt_code_digit));
            this.digitViewList.add((TextView) this.digitContainer2.findViewById(C1075R.C1077id.txt_code_digit));
            this.digitViewList.add((TextView) this.digitContainer3.findViewById(C1075R.C1077id.txt_code_digit));
            this.digitViewList.add((TextView) this.digitContainer4.findViewById(C1075R.C1077id.txt_code_digit));
            this.digitViewList.add((TextView) this.digitContainer5.findViewById(C1075R.C1077id.txt_code_digit));
            this.digitViewList.add((TextView) this.digitContainer6.findViewById(C1075R.C1077id.txt_code_digit));
            this.digitViewList.add((TextView) this.digitContainer7.findViewById(C1075R.C1077id.txt_code_digit));
            this.digitViewList.add((TextView) this.digitContainer8.findViewById(C1075R.C1077id.txt_code_digit));
            this.deleteButton.setOnClickListener(new OnClickListener() {
                public final void onClick(View view) {
                    GenericViewHolder.lambda$new$0(GenericViewHolder.this, view);
                }
            });
            view.setOnClickListener(new OnClickListener() {
                public final void onClick(View view) {
                    GenericViewHolder.lambda$new$1(GenericViewHolder.this, view);
                }
            });
        }

        public static /* synthetic */ void lambda$new$0(GenericViewHolder genericViewHolder, View view) {
            RecyclerViewItemInteractionListener recyclerViewItemInteractionListener = genericViewHolder.mRecyclerViewItemInteractionListener;
            if (recyclerViewItemInteractionListener != null) {
                recyclerViewItemInteractionListener.onItemDelete(genericViewHolder.getPosition());
            }
        }

        public static /* synthetic */ void lambda$new$1(GenericViewHolder genericViewHolder, View view) {
            RecyclerViewItemInteractionListener recyclerViewItemInteractionListener = genericViewHolder.mRecyclerViewItemInteractionListener;
            if (recyclerViewItemInteractionListener != null) {
                recyclerViewItemInteractionListener.onItemClick(genericViewHolder.getPosition());
            }
        }

        private void clearItem() {
            for (int i = 0; i < this.digitViewList.size(); i++) {
                ((TextView) this.digitViewList.get(i)).setText("");
                ((TextView) this.digitViewList.get(i)).setVisibility(4);
            }
            this.deleteButton.setVisibility(4);
            this.newCodeTxt.setVisibility(0);
        }

        private void fillItem() {
            String str = this.mSecondaryCode;
            if (str == null || str.isEmpty()) {
                this.newCodeTxt.setVisibility(0);
                this.deleteButton.setVisibility(4);
                clearItem();
                return;
            }
            char[] charArray = this.mSecondaryCode.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                TextView textView = (TextView) this.digitViewList.get(i);
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(charArray[i]);
                textView.setText(sb.toString());
            }
            this.deleteButton.setVisibility(0);
            this.newCodeTxt.setVisibility(4);
        }

        public void bindData(String str, RecyclerViewItemInteractionListener recyclerViewItemInteractionListener) {
            this.mSecondaryCode = str;
            this.mRecyclerViewItemInteractionListener = recyclerViewItemInteractionListener;
            fillItem();
        }
    }

    interface RecyclerViewItemInteractionListener {
        void onItemClick(int i);

        void onItemDelete(int i);
    }

    public int getItemCount() {
        return 5;
    }

    public ChangeSecondaryKeySafeAdapter(Context context, List<String> list, RecyclerViewItemInteractionListener recyclerViewItemInteractionListener) {
        this.mActivity = context;
        this.secondaryPasscodeList = list;
        this.mRecyclerViewItemInteractionListener = recyclerViewItemInteractionListener;
        for (String isNullOrEmpty : this.secondaryPasscodeList) {
            if (!Strings.isNullOrEmpty(isNullOrEmpty)) {
                this.isListEmpty = false;
                return;
            }
        }
    }

    public GenericViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new GenericViewHolder(LayoutInflater.from(this.mActivity).inflate(C1075R.layout.secondary_code_item, viewGroup, false));
    }

    public void onBindViewHolder(GenericViewHolder genericViewHolder, int i) {
        genericViewHolder.bindData((String) this.secondaryPasscodeList.get(i), this.mRecyclerViewItemInteractionListener);
    }
}
