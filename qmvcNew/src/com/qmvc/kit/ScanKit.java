package com.qmvc.kit;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import com.qmvc.annotation.Aop;
import com.qmvc.annotation.Controller;
import com.qmvc.annotation.Model;
import com.qmvc.annotation.Service;
import com.qmvc.common.Constant;

/**
 * 鎵弿锟�
 * 
 * @author Administrator
 * 
 */
public class ScanKit {

	/**
	 * 娉ㄦ剰tomcat鍔犺浇webapp涓嬬殑lib涓殑锟�鏂瑰寘浠嶇劧鏄敤鐨剋ebapp鍔犺浇鍣紝鍜寃eb搴旂敤浣跨敤鐨勬槸锟�锟斤拷鍔犺浇鍣
	 * 拷? 姣忎釜搴旂敤鐨剋ebapp鍔犺浇鍣ㄥ姞杞界殑鏄粬鐨剋ebapp涓嬬殑/WEB-INF/涓嬬殑绫伙拷?锟�
	 * 锟斤拷鍖呮嫭classes鍜宭ib涓嬬殑绫伙拷?
	 * 
	 * 
	 * tomcat绗竴娆″緱鍒颁竴涓姹傛椂锛屽紑鍚竴涓嚎绋嬶紙璁剧疆绾跨▼涓婁笅鏂囧姞杞藉櫒涓簑ebapp锟�
	 * 绾跨▼涓細浣跨敤webapp鍔犺浇鍣ㄥ姞杞界浉搴攚ebapp鐨刦ilter锛屽綋鐒惰繖涓猣ilter鏄
	 * 鏋舵彁渚涚殑filter.鑰屾垜浠病锟� 鎶婃鏋跺寘鏀惧埌鍏叡lib涓殑
	 * 锟�閭ｄ箞锟�锟斤拷鍔犺浇杩欎釜filter鐨勫氨纭疄浼氭槸webapp鍔犺浇锟
	 * �鐒跺緦姝ゆ檪绶氱▼涓婁笅鏂囩殑鍔犺級鍣ㄦ噳瑭蹭篃琚玹omcat璁剧疆涓轰簡 瀵瑰簲鐨剋ebapp鍔犺浇锟�
	 * 濡傛灉鎴戯拷?鎶婃鏋跺寘鏀惧埌鍏叡lib涓嬩簡
	 * ,閭ｅ湪浣跨敤webapp鍔犺級妗嗘灦filter鏅傛渻鐧肩従filter宸茬粡琚埗绾у姞杞藉櫒鍔犺浇杩囦簡,锟�锟斤拷鐩存帴浣跨敤.
	 * 璋冪敤鍏剁浉搴旂殑鏂规硶
	 * ,濡傛灉杩欐椂妗嗘灦婧愮爜涓兂瑕佽幏鍙栫敤鎴风殑璧勬簮灏卞彧鑳藉厛閫氶亷绾跨▼涓婁笅鏂囧姞杞藉櫒鏉ヨ幏寰梬ebapp鍔犺級锟�鐒跺悗鍐嶇敤
	 * 閫欙拷?鍔犺級鍣ㄤ締灏嬫壘骞跺姞杓夌敤鎴风殑绫伙紝涓嶇劧鍙兘鎵句笉鍒帮拷?鍥犱负姝ゆ椂鐩存帴浣跨敤class.
	 * forname鍔犺浇椤炰娇鐢ㄧ殑椤炲姞杓夊櫒鏄埗绾у姞杞藉櫒锟�
	 * 
	 * 妗嗘灦鏀惧埌lib鍖呬竴鑸笉浼氬嚭鍟忛锟�
	 * */
	public static void scanClass(Constant constant) {
		// 鎷垮埌classes缁濆璺姴
		/*
		 * System.out.println(DiskFileItemFactory.class.getClassLoader().getResource
		 * (""));
		 */
		// 閫欒！濡傛灉鐩存帴鐢ㄦ鏋舵煇鍊嬮鐨勫姞杓夊櫒渚嗙嵅寰楄矾寰戠殑璇濓紝璺緞鍙兘鏄笉瀵圭殑锛屾嬁鍒扮殑涓嶆槸classpath銆傛壂鎻忓嚭鏉ョ殑绫讳篃灏变笉瀵癸拷?
		String path = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath();
		// 鎷垮疄闄呬笂涓嬮潰鍔犺浇鐢ㄦ埛绫绘椂涔熷簲璇ョ敤webapp鍔犺浇鍣ㄥ姞杞芥墠淇濋櫓銆傚惁鍒欐壘涓嶅埌瀵瑰簲鐨勭敤鎴风被锟�
		// 鍥犵偤class銆俧orname榛樿浣跨敤鐨勮皟鐢ㄤ綅缃殑绫诲姞杞藉櫒鏉ュ姞杞斤拷?

		// tomcat搴旇璁剧疆杩囧綋鍓嶆墽琛岃繖涓柟娉曠殑绾跨▼涓簑ebapp鍔犺浇鍣拷?
		// 杩欓噷锟�锟斤拷浣跨敤绾跨▼涓婁笅鏂囧姞杞藉櫒锛屾嬁鍒皌omcat涓姞杞絯eb搴旂敤鐨勭被鍔犺浇鍣紝杩欐牱鎵嶈兘鑾峰彇鍒版纭殑classpath璺緞锟�
		// String path =
		// ScanKit.class.getClassLoader().getResource("").getPath();
		// 寰楀埌绫荤殑鍏ㄥ悕锟�渚嬪锟�con.everxs.test.TestController.class
		List<String> listClass = FileKit.listClassFileAbsolutePath(path);
		// 鎷垮埌鐢ㄦ埛璺緞涓嬬殑锟�锟斤拷绫荤殑鍚嶅瓧锛堝叏闄愬畾鍚嶏級
		for (String clazzStr : listClass) {
			try {
				// 鎵惧埌杩欎釜绫诲叏鍚嶇О鐨凜lass
				// 棣栨鐢宠珛浣跨敤鐨勬槸鍔犺浇scanClass绫荤殑绫诲姞杞藉櫒锟�
				// 杩欓噷鍏跺疄锟�锟斤拷涔熶娇鐢ㄧ窔绋嬩笂涓嬫枃鍔犺級鍣ㄤ締鍔犺級锟�
				Class clazz = Thread.currentThread().getContextClassLoader()
						.loadClass(clazzStr);
				// Class clazz = Class.forName(clazzStr);
				System.out.println("鍔犺浇锟� + clazz.getName()");

				if (clazz != null) {
					Controller controller = (Controller) clazz
							.getAnnotation(Controller.class);
					Model model = (Model) clazz.getAnnotation(Model.class);
					Service serv = (Service) clazz.getAnnotation(Service.class);
					if (controller != null) {
						constant.setRoute(controller.space(), clazz);
					}
					if (model != null) {
						// filter鍒濆鍖栫殑鏃讹拷?锛屾墽琛岃繖閲岋紝鎵弿鍒扮敤鎴锋墍鏈夌殑model锛屽皢modelclass鍜岃〃鍚嶇殑鏄犲皠鏀剧疆鍒板父閲忎腑锟�
						constant.getTable().setTable(clazz, model.tablename());
					}
					if (serv != null) {
						constant.addClass(clazz);
					}

				}
			} catch (Exception e) {
				System.out.println("鎵句笉鍒扮被鏂囦欢");
			}
		}
	}
}
