package com.contacts;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uzmap.pkg.uzcore.UZResourcesIDFinder;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.inflate;

public class APIModuleDemo extends UZModule implements View.OnClickListener {

    private static final String TAG = "APIModuleDemo";

    private UZModuleContext mJsCallback;

    RelativeLayout.LayoutParams rlp;
    private View rootview = null;
    private String contactJson;
    int x;
    int y;
    int w;
    int h;

    private Sidebar sidebar;
    private ListView mListView;
    private EditText search;
    private View searchContainer;

    private int sidebarId;
    private int mListViewId;
    private int searchId;
    private int searchContainerId;

    private ContactsAdapter adapter;
    private List<UserInfo> contactList;

    public APIModuleDemo(UZWebView webView) {
        super(webView);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CommonUtil.inputMode(mContext);
            }
        });
    }

    public void jsmethod_hiddenlist(UZModuleContext moduleContext) {
        removeViewFromCurWindow(rootview);
    }

    public void jsmethod_contacts(UZModuleContext moduleContext) {
        mJsCallback = moduleContext;
        x = moduleContext.optInt("X");
        y = moduleContext.optInt("Y");
        w = moduleContext.optInt("W");
        h = moduleContext.optInt("H");
        contactJson = moduleContext.optString("msg");
        rlp = new RelativeLayout.LayoutParams(w, h);
        rlp.leftMargin = x;
        rlp.topMargin = y;
        int LayoutId = UZResourcesIDFinder.getResLayoutID("contacts_activity_contact_list");
        rootview = createItemRootView(null, LayoutId);
        insertViewToCurWindow(rootview, rlp);

    }

    private View createItemRootView(ViewGroup parentView, int layout) {
        View rootView = inflate(getContext(), layout, parentView);
        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View rootView) {
        sidebarId = UZResourcesIDFinder.getResIdID("sidebar");
        mListViewId = UZResourcesIDFinder.getResIdID("list");
        searchId = UZResourcesIDFinder.getResIdID("search");
        searchContainerId = UZResourcesIDFinder.getResIdID("searchContainer");
        sidebar = rootView.findViewById(sidebarId);
        mListView = rootView.findViewById(mListViewId);
        this.search = rootView.findViewById(searchId);
        this.searchContainer = rootView.findViewById(searchContainerId);

        this.searchContainer.setOnClickListener(this);
        this.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CommonUtil.inputMode(mContext);
                if (charSequence != null) {
                    List<UserInfo> list = new ArrayList<UserInfo>();
                    if (!charSequence.toString().equals("")) {
                        for (UserInfo user : contactList) {
                            if (user.getUsername().startsWith(charSequence.toString())) {
                                list.add(user);
                            }
                        }
                        adapter.setDate(list);
                    } else {
                        adapter.setDate(contactList);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        this.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtil.inputMode(mContext);
            }
        });
    }

    private void initData() {
        contactList = loadLocalContact();
        adapter = new ContactsAdapter(mContext, contactList);

        View headView = LayoutInflater.from(mContext).inflate(UZResourcesIDFinder.getResLayoutID("contacts_item_contact_list_header"), null);
        View footerView = LayoutInflater.from(mContext).inflate(UZResourcesIDFinder.getResLayoutID("contacts_item_contact_list_footer"), null);
        TextView tv_total = footerView.findViewById(UZResourcesIDFinder.getResIdID("tv_total"));

        if (contactList.size() > 0) {
            tv_total.setText(contactList.size() + "位联系人");
        }
        mListView.addHeaderView(headView);
        mListView.addFooterView(footerView);
        mListView.setAdapter(adapter);
        sidebar.setListView(mListView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0 || position >= contactList.size() + 1) {
                    return;
                }
                final UserInfo user = contactList.get(position - 1);
                itemClick(user);
            }
        });
    }

    private List<UserInfo> loadLocalContact() {

        List<UserInfo> localContact = new ArrayList<UserInfo>();
        String[] sections = new String[]{"↑", "☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
        List<UserInfo> list = new ArrayList<UserInfo>();
        try {
            JSONObject json = new JSONObject(contactJson);
            JSONArray array = json.getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                UserInfo info = new UserInfo();
                JSONObject object = array.getJSONObject(i);
                info.setUsername(object.optString("name"));
                info.setPhone(object.optString("phone"));
                info.setIcon(object.optString("icon"));
                info.setPinyin(object.optString("pinyin"));
                list.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < sections.length; j++) {
            for (int i = 0; i < list.size(); i++) {
                UserInfo user = list.get(i);
                CommonUtil.setUserInitialLetter(user);
                if (sections[j].equals(user.getInitialLetter())) {
                    localContact.add(user);
                }
            }
        }
        return localContact;
    }

    private void itemClick(UserInfo user) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", user.getUsername());
            json.put("phone", user.getPhone());
            json.put("icon", user.getIcon());
            json.put("pinyin", user.getPinyin());
            mJsCallback.success(json, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
