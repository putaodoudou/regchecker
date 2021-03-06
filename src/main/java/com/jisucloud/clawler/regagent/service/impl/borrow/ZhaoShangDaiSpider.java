package com.jisucloud.clawler.regagent.service.impl.borrow;

import com.deep007.goniub.selenium.mitm.AjaxHook;
import com.deep007.goniub.selenium.mitm.ChromeAjaxHookDriver;
import com.deep007.goniub.selenium.mitm.HookTracker;

import com.jisucloud.clawler.regagent.interfaces.PapaSpider;
import com.jisucloud.clawler.regagent.interfaces.PapaSpiderConfig;
import com.jisucloud.clawler.regagent.util.OCRDecode;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import java.util.Map;


import org.openqa.selenium.WebElement;

@Slf4j
@PapaSpiderConfig(
		home = "zhaoshangdai.com", 
		message = "招商贷-中天城投集团股份有限公司旗下核心互联网金融平台；招商贷中国西部领先的互联网金融平台，安全有保障的互联网金融平台，为投资人提供全方位安全、稳定的投资理财服务！", 
		platform = "zhaoshangdai", 
		platformName = "招商贷", 
		tags = { "消费分期" , "p2p", "借贷" }, 
		testTelephones = { "18212345678", "15161509916" })
public class ZhaoShangDaiSpider extends PapaSpider implements AjaxHook{

	private ChromeAjaxHookDriver chromeDriver;
	private boolean vcodeSuc = false;//验证码是否正确
	

	private String getImgCode() {
		for (int i = 0 ; i < 3; i++) {
			try {
				WebElement img = chromeDriver.findElementByCssSelector("#login_validCode");
				chromeDriver.mouseClick(img);
				byte[] body = chromeDriver.screenshot(img);
				return OCRDecode.decodeImageCode(body);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public boolean checkTelephone(String account) {
		try {
			chromeDriver = ChromeAjaxHookDriver.newNoHookInstance(false, true, CHROME_USER_AGENT);
			chromeDriver.get("https://login.zhaoshangdai.com/cas/login?target_type=P2p&loginType=WEB&service=https%3A%2F%2Fwww.zhaoshangdai.com%3A443%2Fuser%2Faccount%2Fdetail.html");smartSleep(2000);
			chromeDriver.findElementByCssSelector("#username").sendKeys(account);
			chromeDriver.findElementByCssSelector("#password").sendKeys("a21123as0a1");
			for (int i = 0; i < 5; i++) {
				WebElement login_imgcode = chromeDriver.findElementById("captcha");
				login_imgcode.clear();
				login_imgcode.sendKeys(getImgCode());
				chromeDriver.findElementByCssSelector("#login_btn").click();smartSleep(3000);
				if (chromeDriver.checkElement("#webmsg")) {
					String webmsg = chromeDriver.findElementById("webmsg").getText();
					if (!webmsg.contains("验证码")) {
						vcodeSuc = true;
					}
					checkTel = webmsg.contains("密码") || webmsg.contains("锁定");
				}
				if (vcodeSuc) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (chromeDriver != null) {
				chromeDriver.quit();
			}
		}
		return checkTel;
	}


	@Override
	public boolean checkEmail(String account) {
		return false;
	}

	@Override
	public Map<String, String> getFields() {
		return null;
	}


	@Override
	public HookTracker getHookTracker() {
		// TODO Auto-generated method stub
		return HookTracker.builder().addUrl("checkExistMobile").isPost().build();
	}

	@Override
	public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		return null;
	}
	boolean checkTel;
	@Override
	public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
		checkTel = contents.getTextContents().contains("true");
	}

}
