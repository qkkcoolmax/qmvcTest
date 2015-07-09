package com.qmvc.db;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.qmvc.core.QmvcConfig;
import com.qmvc.core.QmvcModel;
import com.qmvc.kit.MapKit;
import com.qmvc.kit.PrintKit;

/**
 * 鏁版嵁搴撴煡璇拷?鐢ㄧ被锛屽唴缃湪model涓殑澧炲垹鏀规煡鐨勫疄闄咃拷?杈戝湪杩欓噷瀹炵幇锛屽氨鍍忔槸锟�锟斤拷宸ュ叿绫伙拷?
 * 
 * 
 */
public class Db {

	/**
	 * 淇濆瓨鎿嶄綔
	 * 
	 * @return
	 */
	public static int save(String tableName, QmvcModel model) {
		return save(tableName, model, QmvcConfig.pool.getConnection());
	}

	/**
	 * 淇濆瓨鎿嶄綔
	 * 
	 * @return
	 * 
	 *         insert into tableName (xxx,xxx) values ('a','b'); insert into
	 *         tableName (xxx,xxx) values ( ? , ?);
	 * 
	 */
	public static int save(String tableName, QmvcModel model, Connection conn) {
		int result = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String keys = "";
			String wenhao = "";
			// 鏌ョ湅瑕乮nsert鐨勮繖鏉¤褰曚紶鍏ヤ簡澶氬皯涓弬鏁帮紝,鏋勫缓锟�锟斤拷瀛楃涓叉暟缁勶紝鍑嗗鏀剧疆杩欎簺鍊硷拷?
			Object values[] = new String[model.getAttrs().size()];

			int i = 0;
			// 灏唌ode涓紑鍙戯拷?璁剧疆鐨勫弬鏁拌浆鎹㈡垚String绫诲瀷鐨勶紝浠ユ柟渚跨敤鏉ョ粰鍔拷?sql浼狅拷?鍙傛暟锟�
			Map<String, String> strMap = MapKit.toStringMap(model.getAttrs());

			// 灏囧弬鏁拌鍑烘潵锛屾瀯閫犳垚锟�锟斤拷鍔拷?sql璇彞锛屽悓鏅傚皣瀹為檯鐨勶拷?鏀惧叆锟�锟斤拷鐢ㄦ潵浼狅拷?鐨勬暟鎹拷?
			for (String attr : strMap.keySet()) {
				keys = keys + attr + ",";
				wenhao = wenhao + "?,";
				values[i] = strMap.get(attr);
				i++;
			}

			// 鍘绘帀锟�锟斤拷锟�锟斤拷閫楀彿
			if (keys.endsWith(",")) {
				keys = keys.substring(0, keys.length() - 1);
			}
			// 鍘绘帀澶氬嚭鏉ョ殑閫楀彿
			if (wenhao.endsWith(",")) {
				wenhao = wenhao.substring(0, wenhao.length() - 1);
			}

			// 鎷兼帴澶勫姩鎬乻ql璇彞锟�
			String sql = "insert into " + tableName + "(" + keys + ")values("
					+ wenhao + ")";

			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// 鍔拷?浼犲弬锛屽叏閮ㄤ互瀛楃涓茬殑褰㈠紡
			for (i = 0; i < values.length; i++) {
				ps.setObject(i + 1, values[i]);
				// 浼犲弬鏄粠1锟�锟斤拷鐨勶拷?
			}
			// 鎵ц鎿嶄綔
			result = ps.executeUpdate();
			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				// 鐭ュ叾浠呮湁锟�锟斤拷锛屾晠鑾峰彇绗竴锟�
				Long id = rs.getLong(1);
				model.set("id", id);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			// 娉ㄦ剰鍗充娇瑕佸仛浜嬪姟锛屾病鎵ц锟�锟斤拷涔熸槸瑕佸叧闂粨鏋滈泦锛屽０鏄庣殑锛屽彧鏄connection涓嶈兘鏂拷?
			try {
				// 濡傛灉杩炴帴閮ㄤ綅绌烘垨鑰呰繛鎺ユ槸鑷姩鎻愪氦鐨勶紝涔熷氨鏄娌℃湁鎵撶畻鎷挎潵鍋氫簨鍔★紝閭ｄ箞灏辫繕缁欑嚎绋嬫睜锛屽嵆save锟�锟斤拷宸茬粡鑷姩鎻愪氦浜嗭紝鍙互鎶婅繛鎺ヨ繕缁欑嚎绋嬫睜.涓嬫瑕佺敤鍐嶆嬁锟�
				if (conn != null && conn.getAutoCommit()) {
					// 杩欓噷琛ㄧずconn鏄嚜鍔ㄦ彁浜わ紝锟�锟斤拷鍒拌繖閲屼簨鍔″凡缁忕粨鏉燂紝瑕佸皢绾跨▼鐨勪繚闄╃涓殑杩炴帴涔熸竻鐞嗘帀锟�
					conn.close();// 杩欎釜close搴旇琚噸鍐欎簡锛屽苟涓嶆槸鍏抽棴杩炴帴锛岋拷?鏄皢杩炴帴浜よ繕缁欒繛鎺ユ睜锛岃繛鎺ユ睜鍙互灏嗗叾浜ょ粰鍏朵粬浜虹敤锟�
					QmvcConfig.pool.clearConnection();
					// 濡傛灉杩欓噷鐢ㄦ埛蹇樿鍏抽棴浜嗭紝閭ｄ箞寮哄ぇ鐨勪竴鐐圭殑杩炴帴姹犱細鑷鍥炴敹涔呬簡娌＄敤鐨勮繛鎺ワ拷?
				}

				// 濡傛灉涓嶆槸鑷姩鎻愪氦锛岄偅涔堝氨涓嶆墽琛宑lose涔熷氨鏄笉褰掕繕杩欎釜杩炴帴鍏堬拷?
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	public static int delete() {

		return 0;
	}

}
