package cn.yiidii.jdx.support;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.yiidii.jdx.util.ScheduleTaskUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GithubVersionListener implements ITask {

    private final static String POM_RAW_URL = "https://ghproxy.com/https://raw.githubusercontent.com/wangyiidii/jdx/master/back/pom.xml";
    private static boolean needUpgrade = false;
    private static String latestVersion = "";

    private final ScheduleTaskUtil scheduleTaskUtil;

    public JSONObject getVersionInfo() {
        JSONObject result = new JSONObject();
        result.put("appVersion", SpringUtil.getProperty("spring.application.version"));
        result.put("needUpgrade", needUpgrade);
        result.put("latestVersion", latestVersion);
        return result;
    }

    @Override
    public void startTimerTask() {
        scheduleTaskUtil.startCron("SYS_checkUpgrade", () -> this.checkUpgrade(), "0 0/30 * * * ?");
    }

    public void checkUpgrade() {
        String versionStr;
        Integer latestVersion;
        try {
            HttpResponse resp = HttpRequest.get(POM_RAW_URL).execute();
            String body = resp.body();
            JSONObject pomJo = XmlUtil.xmlToBean(XmlUtil.readXML(body), JSONObject.class);
            versionStr = pomJo.getJSONObject("project").getString("version");
            latestVersion = Integer.valueOf(versionStr.replaceAll("[^\\d+]", ""));
        } catch (Exception e) {
            log.error(StrUtil.format("获取Github版本号发生错误: {}", e.getMessage()));
            return;
        }
        Integer appVersion = Integer.valueOf(SpringUtil.getProperty("spring.application.version").replaceAll("[^\\d]", ""));
        if (latestVersion > appVersion) {
            log.info(StrUtil.format("检测到最新版本号: {}, 当前版本号: {}", latestVersion, appVersion));
            needUpgrade = true;
            GithubVersionListener.latestVersion = versionStr;
            scheduleTaskUtil.stopCron("SYS_checkUpgrade");
        }
    }


}
