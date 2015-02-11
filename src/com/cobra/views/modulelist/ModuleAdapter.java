package com.cobra.views.modulelist;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cobra.appClass;
import com.cobra.api.Cobra.ModuleType;
import com.cobra.api.CobraDataTags.ChannelDataTag;
import com.cobra.api.CobraDataTags.ModuleDataTag;

public class ModuleAdapter extends BaseAdapter {
	private final String TAG;
	private SparseArray<ModuleListItem> modules = null;
	private final Context context;

	public ModuleAdapter(Context context, SparseArray<ModuleListItem> modules) {
		super();
		TAG = ModuleAdapter.class.getName();
		this.context = context;
		this.modules = modules;
		globV = (appClass) context.getApplicationContext();
	}

	appClass globV;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int key = modules.keyAt(position);
		ModuleListItem item = modules.get(key);

		ModuleDataTag module = item.getModuleTag();
		// Boolean active = item.isActive();
		ChannelDataTag channel = item.getChannelTag();

		// appClass.setModuleLogOnDevice("POSITION:" + position +
		// " , Module ID:"+ module.modID);
		// if (convertView != null) {
		// ModuleRow row = (ModuleRow) convertView;
		// row.updateViewData(position, item.getModuleTag(), item.isActive(),
		// item.getChannelTag());
		//
		// ModuleUIRow UIrow = new ModuleUIRow();
		// UIrow.setContent(row.getContent());
		// UIrow.setCuesParent(row.getCuesParent());
		// UIrow.setModuleid(module.modID);
		// UIrow.setParentLayout(row.getParentLayout());
		// UIrow.setPosition(position);
		// UIrow.setTvAddress(row.getTvAddress());
		// UIrow.setTvBattery1(row.getTvBattery1());
		// UIrow.setTvBattery2(row.getTvBattery2());
		// UIrow.setTvChannel(row.getTvChannel());
		// UIrow.setTvCues(row.getTvCues());
		// UIrow.setTvDevice(row.getTvDevice());
		// UIrow.setTvKeyPosition(row.getTvKeyPosition());
		// UIrow.setTvMode(row.getTvMode());
		// UIrow.setTvSignal(row.getTvSignal());
		// globV.setModuleUI_List(UIrow);
		//
		// return row;
		// }

		ModuleRow row = new ModuleRow(position, context, item.getModuleTag(),
				-1, item.getChannelTag(), null);

		ModuleUIRow UIrow = new ModuleUIRow();
		UIrow.setContent(row.getContent());
		UIrow.setCuesParent(row.getCuesParent());
		UIrow.setModuleid(module.modID);

		UIrow.setDevice(module.modType);// - AB, 18M, 36M, 90M
		UIrow.setAddress(module.modID);// - A00, A01, A02, etc.
		UIrow.setKey_Pos(module.keyPos);// - ARM, TEST
		UIrow.setMode(module.armed);// ARM, TEST
		UIrow.setSignal(module.linkQuality);// - -70, -60, -50, etc.
		UIrow.setPower_1(module.batteryLevel1);// 1P & 2P - 0, 1, 2, 3 etc.
		UIrow.setPower_2(module.batteryLevel2);
		UIrow.setParentLayout(row.getParentLayout());
		UIrow.setPosition(position);
		UIrow.setTvAddress(row.getTvAddress());
		UIrow.setTvBattery1(row.getTvBattery1());
		UIrow.setTvBattery2(row.getTvBattery2());
		UIrow.setTvChannel(row.getTvChannel());
		UIrow.setTvCues(row.getTvCues());
		UIrow.setTvDevice(row.getTvDevice());
		UIrow.setTvKeyPosition(row.getTvKeyPosition());
		UIrow.setTvMode(row.getTvMode());
		UIrow.setTvSignal(row.getTvSignal());
		globV.setModuleUI_List(UIrow);
		return row;
	}

	@Override
	public int getCount() {
		return modules.size();
	}

	@Override
	public Object getItem(int position) {
		return modules.get(modules.keyAt(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
