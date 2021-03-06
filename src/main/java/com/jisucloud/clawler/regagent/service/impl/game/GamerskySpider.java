package com.jisucloud.clawler.regagent.service.impl.game;


import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;



@Slf4j
@PapaSpiderConfig(
		home = "gamersky.com", 
		message = "游民星空是国内单机游戏门户网站,提供特色的游戏资讯,大量游戏攻略,经验,评测文章,以及热门游戏资料专题。", 
		platform = "gamersky", 
		platformName = "游民星空", 
		tags = { "游戏" }, 
		testTelephones = { "15700102865", "18212345678" })
public class GamerskySpider extends PapaSpider {

	

	public boolean checkTelephone(String account) {
		try {
			String url = "https://i.gamersky.com/user/verifyphone";
			FormBody formBody = new FormBody
	                .Builder()
	                .add("phone",account)
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Host", "i.gamersky.com")
					.addHeader("Referer", "https://i.gamersky.com/user/register")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			if (response.body().string().contains("已被注册")) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean checkEmail(String account) {
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		return null;
	}

}
