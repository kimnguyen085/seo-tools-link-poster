package main.java.com.seo.auto.service;

import main.java.com.seo.auto.constants.MixpanelTrackEvent;
import main.java.com.seo.auto.utils.UtilsMeth;
import org.slf4j.LoggerFactory;
import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MessageBuilder;
import com.mixpanel.mixpanelapi.MixpanelAPI;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MixpanelService {

    private static MixpanelService instance;
    private static boolean enabled = true;

    private final Logger logger = LoggerFactory.getLogger(MixpanelService.class);
    private static final String MIXPANEL_API_KEY = "s98sJuFTg+HFhHUBt+vvGhM/cMhM2hncL2DFN5S7N6tFLyNcfbSEEw==";
    private static final String MIXPANEL_EVENT_TIME = "TIME";
    private static final String MIXPANEL_EVENT_NAME = "NAME";
    private static final String MIXPANEL_EVENT_USER_NAME = "USER";

    private static final String MIXPANEL_EVENT_BOT_USAGES = "BOT USAGE";
    private static final String MIXPANEL_EVENT_MAC_ADDRESS = "MAC ADDRESS";

    private String DATE_FORMAT_PATTERN = "EEE, d MMM yyyy HH:mm:ss";

    public void userCheckedInEvent() throws IOException {
        JSONObject props = generateBaseProperties(MixpanelTrackEvent.USER_CHECKED_IN);
        sendEventToMixpanel(props ,MixpanelTrackEvent.USER_CHECKED_IN);
    }

    public void userPostLinkEvent(List<String> botUsages) {
        JSONObject props = generateBaseProperties(MixpanelTrackEvent.USER_INITIATED_POSTING_LINK);
        String botUses = botUsages.stream().collect(Collectors.joining(","));
        props.put(MIXPANEL_EVENT_BOT_USAGES, botUses);
        sendEventToMixpanel(props ,MixpanelTrackEvent.USER_INITIATED_POSTING_LINK);
    }

    public void userSuccessPostedLinkEvent(String botName, String userName) {
        JSONObject props = generateBaseProperties(MixpanelTrackEvent.USER_POSTED_LINK_SUCCESS);
        props.put(MIXPANEL_EVENT_BOT_USAGES, botName);
        props.put(MIXPANEL_EVENT_USER_NAME, userName);
        sendEventToMixpanel(props ,MixpanelTrackEvent.USER_POSTED_LINK_SUCCESS);
    }

    public void userFailedPostedLinkEvent(String botName, String userName) {
        JSONObject props = generateBaseProperties(MixpanelTrackEvent.USER_POSTED_LINK_FAILED);
        props.put(MIXPANEL_EVENT_BOT_USAGES, botName);
        props.put(MIXPANEL_EVENT_USER_NAME, userName);
        sendEventToMixpanel(props ,MixpanelTrackEvent.USER_POSTED_LINK_FAILED);
    }

    private JSONObject generateBaseProperties(MixpanelTrackEvent trackEvent){
        JSONObject props = new JSONObject();
        props.put(MIXPANEL_EVENT_TIME, LocalDateTime.now());
        props.put(MIXPANEL_EVENT_NAME , trackEvent.getDescription());
        props.put(MIXPANEL_EVENT_MAC_ADDRESS, UtilsMeth.getMacAddress());

        return props;
    }

    private void sendEventToMixpanel(JSONObject props, MixpanelTrackEvent trackEvent) {
        if (!enabled)   return;
        MessageBuilder messageBuilder = new MessageBuilder(UtilsMeth.decrypt(MIXPANEL_API_KEY));
        JSONObject sentEvent = messageBuilder.event(UtilsMeth.getMacAddress(),trackEvent.getDescription(), props);
        MixpanelAPI mixpanel = new MixpanelAPI();

        try {
            mixpanel.sendMessage(sentEvent);
        } catch (IOException e) {
            return;
        }
    }

    public void sendEventsToMixpanel(List<JSONObject> messages, String clientId, MixpanelTrackEvent trackEvent) throws IOException {
        if (messages == null || messages.isEmpty()) {
            return;
        }

        if (!enabled)   return;

        MixpanelAPI mixpanel = new MixpanelAPI();
        MessageBuilder messageBuilder = new MessageBuilder(UtilsMeth.decrypt(MIXPANEL_API_KEY));
        ClientDelivery delivery = new ClientDelivery();
        for (JSONObject message : messages) {
            JSONObject sentEvent = messageBuilder.event(clientId,trackEvent.getDescription(), message);
            delivery.addMessage(sentEvent);
        }
        mixpanel.deliver(delivery);
    }

    public static MixpanelService getInstance(){
        if (instance == null) {
            instance = new MixpanelService();
        }
        return instance;
    }
}
