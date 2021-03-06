package com.jisucloud.clawler.regagent.service.impl.trip;


import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;

import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;



@Slf4j
@PapaSpiderConfig(
		home = "qunar.com", 
		message = "去哪儿是中国领先的旅游搜索引擎，去哪儿是目前全球最大的中文在线旅行网站，创立于2005年2月，总部在北京。去哪儿网为消费者提供机票、酒店、会场、度假产品的实时搜索，并提供旅游产品团购以及其他旅游信息服务，为旅游行业合作伙伴提供在线技术、移动技术解决方案。", 
		platform = "qunar", 
		platformName = "去哪儿", 
		userActiveness = 0.6f,
		tags = { "旅游" , "酒店" , "机票" , "o2o" }, 
		testTelephones = { "18210538000", "18212345678" })
public class QunarSpider extends PapaSpider {

	

	public boolean checkTelephone(String account) {
		try {
			String url = "https://user.qunar.com/ajax/validator.jsp";
			FormBody formBody = new FormBody
	                .Builder()
	                .add("method", account)
	                .add("prenum", "86")
	                .add("vcode", "null")
	                .build();
			Request request = new Request.Builder().url(url)
					.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0")
					.addHeader("Host", "user.qunar.com")
					.addHeader("Referer", "https://user.qunar.com/passport/register.jsp?ret=https%3A%2F%2Fdujia.qunar.com%2F%3Fex_track%3Dauto_52b3f121")
					.post(formBody)
					.build();
			Response response = okHttpClient.newCall(request).execute();
			if (response.body().string().contains("用户已存在")) {
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
