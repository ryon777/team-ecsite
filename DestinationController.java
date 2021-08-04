package jp.co.internous.rainbow.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.rainbow.model.domain.MstDestination;
import jp.co.internous.rainbow.model.domain.MstUser;
import jp.co.internous.rainbow.model.form.DestinationForm;
import jp.co.internous.rainbow.model.mapper.MstDestinationMapper;
import jp.co.internous.rainbow.model.mapper.MstUserMapper;
import jp.co.internous.rainbow.model.session.LoginSession;

@Controller
@RequestMapping("/rainbow/destination")
public class DestinationController {

	@Autowired
	private MstUserMapper userMapper;

	@Autowired
	private LoginSession loginSession;

	@Autowired
	private MstDestinationMapper destinationMapper;

	private Gson gson = new Gson();

	/**
	 * ユーザー情報取得
	 * @param m "user" "loginSession"
	 * @return　"destination"
	 */
	@RequestMapping("/")
	public String index(Model m) {
		MstUser user = userMapper.findByUserNameAndPassword(loginSession.getUserName(),loginSession.getPassword());
		
		m.addAttribute("user", user);
		// page_header.htmlでsessionの変数を表示させているため、loginSessionも画面に送る。
		m.addAttribute("loginSession",loginSession);
		return "destination";

	}

	/**
	 * 登録済みの宛先を削除
	 * @param destinationId
	 * @return IDの有無を返却
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/delete")
	@ResponseBody

public boolean delete(@RequestBody String destinationId) {

		Map<String, String> map = gson.fromJson(destinationId, Map.class);
		String id = map.get("destinationId");

		int result = destinationMapper.logicalDeleteById(Integer.parseInt(id));

		return result > 0;
	}

	/**
	 * 宛先の登録
	 * @param f "userId"
	 * @return "destinationId"
	 */
	@RequestMapping("/register")
	@ResponseBody
	public String register(@RequestBody DestinationForm f) {
		// 宛先を登録
		MstDestination destination = new MstDestination(f);
		int userId = loginSession.getUserId();
		destination.setUserId(userId);
		int count = destinationMapper.insert(destination);

		// 登録した宛先のIDを取得
		Integer id = 0;
		if (count > 0) {
			id = destination.getId();
		}
		return id.toString();
	}
}
