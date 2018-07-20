package com.mpeter.xrandomtweaks.xposed.AIMP;

public class AIMPConstants {
    public static final String CLASS_AlbumArts = "com.aimp.player.core.meta.AlbumArts";
        public static final String METHOD_getAlbumArt = "getAlbumArt";

    public static final String CLASS_BaseTrackInfo = "com.aimp.player.core.meta.BaseTrackInfo";

    public static final String AIMP_LOGO_ENCODED = "iVBORw0KGgoAAAANSUhEUgAABAAAAAQACAMAAABIw9uxAAADAFBMVEXu7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u4AAADu7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u7u4yUKpVAAAA/3RSTlN+TQbRT7aPaKtmhCc+H1u6zanixDZ3216mf1e7M3QqEmoLk8bZuOvSnq2G5UNKtW2OZyJAg103GnmnRjT7dQfOE9oKawTLTq8j6vgxmPky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";
}
