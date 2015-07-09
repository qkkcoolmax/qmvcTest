package com.qmvc.kit;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.qmvc.annotation.OnlineController;
import com.qmvc.annotation.OnlineMethod;
import com.qmvc.onlinedoc.Online;

public class OnlineDocKit {

	/**
	*
	*/

	public static List<Online> loadClass(Map<String, Class> map) {
		List<Online> onlineList = new ArrayList<Online>();
		for (String nameSpace : map.keySet()) {
			Class clazz = map.get(nameSpace);
			OnlineController onlineController = (OnlineController) clazz
					.getAnnotation(OnlineController.class);
			if (onlineController != null) {
				Online online = new Online();
				online.setType(1);
				online.setMemo(onlineController.memo());
				online.setUrl(nameSpace);
				onlineList.add(online);
				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					OnlineMethod onlineMethod = method
							.getAnnotation(OnlineMethod.class);
					if (onlineMethod != null) {
						Online o = new Online();
						o.setMemo(onlineMethod.memo());
						o.setMethod(onlineMethod.method());
						o.setParam(onlineMethod.param());
						o.setType(2);
						o.setUrl(nameSpace + "/" + method.getName());
						onlineList.add(o);
					}
				}
			}

		}
		return onlineList;
	}
}
