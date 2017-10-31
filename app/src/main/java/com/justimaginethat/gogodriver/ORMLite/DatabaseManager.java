package com.justimaginethat.gogodriver.ORMLite;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * Created by LION1 on 03-03-2017.
 */
public class DatabaseManager<H extends OrmLiteSqliteOpenHelper> {

	//Step Two....

	private H helper;

	/**
	 * @Name getHelper
	 * @CreatedBy 1266
	 * @CreatedDate Apr 3, 2015
	 * @ModifiedDate Apr 3, 2015
	 * @Purpose Returns the OpenHelper object.
	 */
	@SuppressWarnings("unchecked")
	public H getHelper(Context context) {
		if (helper == null) {
			helper = (H) OpenHelperManager.getHelper(context,
					DatabaseHelper.class);
		}
		return helper;
	}

	/**
	 * @Name releaseHelper
	 * @CreatedBy 1266
	 * @CreatedDate Apr 3, 2015
	 * @ModifiedDate Apr 3, 2015
	 * @Purpose Releases the provided helper object
	 */
	public void releaseHelper(H helper) {
		if (helper != null) {
			OpenHelperManager.releaseHelper();
			helper = null;
		}
	}

}
