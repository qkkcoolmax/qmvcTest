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
import com.qmvc.exception.TooMuchParamException;
import com.qmvc.kit.MapKit;
import com.qmvc.kit.PrintKit;

/**
 * 閺佺増宓佹惔鎾寸叀鐠囶枎鎷�閻劎琚敍灞藉敶缂冾喖婀猰odel娑擃厾娈戞晶鐐插灩閺�鐓￠惃鍕杽闂勫拑鎷�鏉堟垵婀
 * 潻娆撳櫡鐎圭偟骞囬敍灞芥皑閸嶅繑妲搁敓锟介敓鏂ゆ嫹瀹搞儱鍙跨猾浼欐嫹?
 * 
 * 
 */
public class Db {

	/**
	 * 娣囨繂鐡ㄩ幙宥勭稊
	 * 
	 * @return
	 */
	public static int save(String tableName, QmvcModel model) {
		return save(tableName, model, QmvcConfig.pool.getConnection());
	}

	public static void queryIdforObject(String tableName, QmvcModel model) {
		queryIdForObject(tableName, model, QmvcConfig.pool.getConnection());
	}

	/**
	 * 娣囨繂鐡ㄩ幙宥勭稊
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
			// 閺屻儳婀呯憰涔畁sert閻ㄥ嫯绻栭弶陇顔囪ぐ鏇氱炊閸忋儰绨℃径姘毌娑擃亜寮弫甯礉,閺嬪嫬缂撻敓锟介敓鏂ゆ嫹鐎涙顑佹稉鍙夋殶缂佸嫸绱濋崙鍡楊樃閺�墽鐤嗘潻娆庣昂閸婄》鎷�
			Object values[] = new String[model.getAttrs().size()];

			int i = 0;
			// 鐏忓攲ode娑擃厼绱戦崣鎴嫹?鐠佸墽鐤嗛惃鍕棘閺佹媽娴嗛幑銏″灇String缁鐎烽惃鍕剁礉娴犮儲鏌熸笟璺ㄦ暏閺夈儳绮伴崝顭掓嫹?sql娴肩媴鎷�閸欏倹鏆熼敓锟�
			Map<String, String> strMap = MapKit.toStringMap(model.getAttrs());

			// 鐏忓洤寮弫鎷岊嚢閸戠儤娼甸敍灞剧�闁姵鍨氶敓锟介敓鏂ゆ嫹閸旑煉鎷�sql鐠囶厼褰為敍灞芥倱閺呭倸鐨ｇ�鐐烘閻ㄥ嫸鎷�閺�儳鍙嗛敓锟介敓鏂ゆ嫹閻劍娼垫导鐙呮嫹?閻ㄥ嫭鏆熼幑顕嗘嫹?
			for (String attr : strMap.keySet()) {
				keys = keys + attr + ",";
				wenhao = wenhao + "?,";
				values[i] = strMap.get(attr);
				i++;
			}

			// 閸樼粯甯�敓锟介敓鏂ゆ嫹閿燂拷閿熸枻鎷烽柅妤�娇
			if (keys.endsWith(",")) {
				keys = keys.substring(0, keys.length() - 1);
			}
			// 閸樼粯甯�径姘毉閺夈儳娈戦柅妤�娇
			if (wenhao.endsWith(",")) {
				wenhao = wenhao.substring(0, wenhao.length() - 1);
			}

			// 閹峰吋甯存径鍕З閹够ql鐠囶厼褰為敓锟�
			String sql = "insert into " + tableName + "(" + keys + ")values("
					+ wenhao + ")";

			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			// 閸旑煉鎷�娴肩姴寮敍灞藉弿闁劋浜掔�妤冾儊娑撹尙娈戣ぐ銏犵础
			for (i = 0; i < values.length; i++) {
				ps.setObject(i + 1, values[i]);
				// 娴肩姴寮弰顖欑矤1閿燂拷閿熸枻鎷烽惃鍕舵嫹?
			}
			// 閹笛嗩攽閹垮秳缍�
			result = ps.executeUpdate();
			rs = ps.getGeneratedKeys();

			if (rs.next()) {
				// 閻儱鍙炬禒鍛箒閿燂拷閿熸枻鎷烽敍灞炬櫊閼惧嘲褰囩粭顑跨閿燂拷
				Long id = rs.getLong(1);
				model.set("id", id);
			}

		} catch (Exception e) {

			/**
			 * 这里可以将异常封装成运行时异常，可以转义成不同意义的异常。
			 * 
			 * */
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

			// 濞夈劍鍓伴崡鍏呭▏鐟曚礁浠涙禍瀣閿涘本鐥呴幍褑顢戦敓锟介敓鏂ゆ嫹娑旂喐妲哥憰浣稿彠闂傤厾绮ㄩ弸婊堟肠閿涘苯锛愰弰搴ｆ畱閿涘苯褰ч弰顖濐嚛connection娑撳秷鍏橀弬顓ㄦ嫹?
			try {
				// 婵″倹鐏夋潻鐐村复闁劋缍呯粚鐑樺灗閼板懓绻涢幒銉︽Ц閼奉亜濮╅幓鎰唉閻ㄥ嫸绱濇稊鐔锋皑閺勵垵顕╁▽鈩冩箒閹垫挾鐣婚幏鎸庢降閸嬫矮绨ㄩ崝鈽呯礉闁絼绠炵亸杈箷缂佹瑧鍤庣粙瀣潨閿涘苯宓唖ave閿燂拷閿熸枻鎷峰鑼病閼奉亜濮╅幓鎰唉娴滃棴绱濋崣顖欎簰閹跺﹨绻涢幒銉ㄧ箷缂佹瑧鍤庣粙瀣潨.娑撳顐肩憰浣烘暏閸愬秵瀣侀敓锟�
				if (conn != null && conn.getAutoCommit()) {
					// 鏉╂瑩鍣风悰銊с仛conn閺勵垵鍤滈崝銊﹀絹娴溿倧绱濋敓锟介敓鏂ゆ嫹閸掓媽绻栭柌灞肩皑閸斺�鍑＄紒蹇曠波閺夌噦绱濈憰浣哥殺缁捐法鈻奸惃鍕箽闂勨晝顔堟稉顓犳畱鏉╃偞甯存稊鐔哥閻炲棙甯�敓锟�

					conn.close();// 鏉╂瑤閲渃lose鎼存棁顕氱悮顐﹀櫢閸愭瑤绨￠敍灞借嫙娑撳秵妲搁崗鎶芥４鏉╃偞甯撮敍宀嬫嫹?閺勵垰鐨㈡潻鐐村复娴溿倛绻曠紒娆掔箾閹恒儲鐫滈敍宀冪箾閹恒儲鐫滈崣顖欎簰鐏忓棗鍙炬禍銈囩舶閸忔湹绮禍铏规暏閿燂拷
					QmvcConfig.pool.clearConnection();
					// 婵″倹鐏夋潻娆撳櫡閻劍鍩涜箛妯款唶閸忔娊妫存禍鍡礉闁絼绠炲鍝勩亣閻ㄥ嫪绔撮悙鍦畱鏉╃偞甯村Ч鐘辩窗閼奉亣顢戦崶鐐存暪娑斿懍绨″▽锛勬暏閻ㄥ嫯绻涢幒銉嫹?
				}

				// 婵″倹鐏夋稉宥嗘Ц閼奉亜濮╅幓鎰唉閿涘矂鍋呮稊鍫濇皑娑撳秵澧界悰瀹憀ose娑旂喎姘ㄩ弰顖欑瑝瑜版帟绻曟潻娆庨嚋鏉╃偞甯撮崗鍫嫹?
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return result;
	}

	public static int delete() {

		return 0;
	}
	
	
	

	/**
	 * 
	 * 通过主键查询，返回唯一的对象。
	 * 
	 * */
	private static void queryIdForObject(String tableName, QmvcModel model,
			Connection conn) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSetMetaData rsmt = null;
		try {
			Map<String, String> strMap = MapKit.toStringMap(model.getAttrs());
			if (strMap.size() != 1) {
				throw new TooMuchParamException(
						"model中提供的参数过多，只需要提供对应的主键（id）名称和值");
			}
			String pkeyName = null;
			String pkValue = null;
			for (String key : strMap.keySet()) {
				pkeyName = key;
				pkValue = strMap.get(key);
			}
			String querySql = "select * from " + tableName + "where "
					+ pkeyName + " = " + pkValue;
			ps = conn.prepareStatement(querySql);
			rs = ps.executeQuery();
			rs.last();
			if (rs.getRow() > 1) {
				throw new TooMuchParamException("输入的不是唯一主键，返回行数过多");
			}
			rs.first();
			rsmt = ps.getMetaData();
			int count = rsmt.getColumnCount();

			for (int i = 1; i <= count; i++) {
				model.set(rsmt.getColumnName(i), rs.getObject(i));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
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
			try {
				if (conn != null && conn.getAutoCommit()) {
					conn.close();
					QmvcConfig.pool.clearConnection();//将连接从线程对象中清除，防止误用。
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	
	
	public static int update(String tableName, QmvcModel model, Connection conn) {

		int result = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String keys = "";
			String wenhao = "";
			Object values[] = new String[model.getAttrs().size()];

			int i = 0;
			Map<String, String> strMap = MapKit.toStringMap(model.getAttrs());

			for (String attr : strMap.keySet()) {
				keys = keys + attr + ",";
				wenhao = wenhao + "?,";
				values[i] = strMap.get(attr);
				i++;
			}

			if (keys.endsWith(",")) {
				keys = keys.substring(0, keys.length() - 1);
			}

			if (wenhao.endsWith(",")) {
				wenhao = wenhao.substring(0, wenhao.length() - 1);
			}

			String sql = "insert into " + tableName + "(" + keys + ")values("
					+ wenhao + ")";

			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			for (i = 0; i < values.length; i++) {
				ps.setObject(i + 1, values[i]);

			}

			result = ps.executeUpdate();
			rs = ps.getGeneratedKeys();

			if (rs.next()) {
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
			try {
				if (conn != null && conn.getAutoCommit()) {
					conn.close();
					QmvcConfig.pool.clearConnection();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return result;

	}

}
