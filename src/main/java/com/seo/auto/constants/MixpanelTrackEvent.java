package main.java.com.seo.auto.constants;

public enum MixpanelTrackEvent {
    USER_CHECKED_IN("User Checked In"),
    USER_INITIATED_POSTING_LINK("User Clicked On Post Link"),
    USER_POSTED_LINK_SUCCESS("User Posted Link Successfully"),
    USER_POSTED_LINK_FAILED("User Posted Link Failed"),
    ;

    private final String description;

    MixpanelTrackEvent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
