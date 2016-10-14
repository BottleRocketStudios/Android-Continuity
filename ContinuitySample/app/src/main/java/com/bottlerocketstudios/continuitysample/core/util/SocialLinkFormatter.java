package com.bottlerocketstudios.continuitysample.core.util;

import java.util.Locale;

/**
 * Created on 10/12/16.
 */

public class SocialLinkFormatter {
    private static final String TWITTER_URL_FORMAT = "https://twitter.com/%1$s";
    private static final String YOUTUBE_URL_FORMAT = "https://www.youtube.com/user/%1$s";
    private static final String FACEBOOK_URL_FORMAT = "https://www.facebook.com/%1$s";
    private static final String GOVTRACK_URL_FORMAT = "https://www.govtrack.us/congress/members/%1$s";

    public static String formatTwitterUrl(String twitterId) {
        return String.format(Locale.US, TWITTER_URL_FORMAT, twitterId);
    }

    public static String formatFacebookUrl(String facebookId) {
        return String.format(Locale.US, FACEBOOK_URL_FORMAT, facebookId);
    }

    public static String formatYouTubeUrl(String youTubeId) {
        return String.format(Locale.US, YOUTUBE_URL_FORMAT, youTubeId);
    }

    public static String formatGovtrackUrl(String govtrackId) {
        return String.format(Locale.US, GOVTRACK_URL_FORMAT, govtrackId);
    }
}
