package com.tdc.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.wearable.DataMap;

/**
 * <p>Allows to put and get {@link Parcelable} objects into {@link DataMap}</p>
 * <b>USAGE:</b>
 * <p>
 * <b>Store object in DataMap:</b><br/>
 * DataMapParcelableUtils.putParcelable(dataMap, "KEY", myParcelableObject);
 * </p><p>
 * <b>Restore object from DataMap:</b><br/>
 * myParcelableObject = DataMapParcelableUtils.getParcelable(dataMap, "KEY", MyParcelableObject.CREATOR);
 * </p>
 * I do <b>not recommend</b> to use this method - it may fail when the class that implements {@link Parcelable} would be updated. Use it at your own risk.
 * @author Maciej Ciemięga
 */
public class DataMapParcelableUtils {
 
	public static void putParcelable(DataMap dataMap, String key, Parcelable parcelable) {
		final Parcel parcel = Parcel.obtain();
		parcelable.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);
		dataMap.putByteArray(key, parcel.marshall());
		parcel.recycle();
	}
	
	public static <T> T getParcelable(DataMap dataMap, String key, Parcelable.Creator<T> creator) {
		final byte[] byteArray = dataMap.getByteArray(key);
		final Parcel parcel = Parcel.obtain();
		parcel.unmarshall(byteArray, 0, byteArray.length);
		parcel.setDataPosition(0);
		final T object = creator.createFromParcel(parcel);
		parcel.recycle();
		return object;
	}
}