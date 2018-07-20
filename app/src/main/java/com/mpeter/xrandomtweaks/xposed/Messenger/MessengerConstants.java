package com.mpeter.xrandomtweaks.xposed.Messenger;

public class MessengerConstants {
    public static final String CLASS_SoccerActivity = "com.facebook.messaging.soccer.SoccerActivity";
    public static final String METHOD_m94208a_name = "a";
    public static final String METHOD_m94206a_name = "a";
    public static final String METHOD_prepareFields_name = "a";
    public static final String METHOD_m94209b_name = "b"; //Üres before és after hook, de breakpoint
    public static final String METHOD_m94210c_name = "c"; //Talán onCreate?
    public static final String METHOD_r$0_name = "r$0"; //Talán eredmények mentése?
    public static final String METHOD_onBackPressed = "onBackPressed";
    public static final String FIELD_savedHighScore = "w";

    public static final String CLASS_SoccerView = "com.facebook.messaging.soccer.SoccerView";
    public static final String METHOD_setDisplayScore = "setDisplayScore";
    public static final String METHOD_getSaltedHighScore = "d";
    public static final String METHOD_highScoreIsNew = "a";

    public static final String CLASS_ThreadGameData = "com.facebook.messaging.model.threads.ThreadGameData";
    public static final String METHOD_writeToParcel = "writeToParcel";

    public static final String CLASS_FbFragmentActivity = "com.facebook.base.activity.FbFragmentActivity";
    public static final String METHOD_onCreate = "onCreate";

    public static final String CLASS_C20070kh = "X.0kh";
    public static final String METHOD_m9088a = "a"; //construtor

    public static final String CLASS_ThreadKey = "com.facebook.messaging.model.threadkey.ThreadKey";

    public static final String CLASS_X0PR = "X.0PR";
    public static final String CLASS_X0g0 = "X.0g0";

    public static final String CLASS_X1R9 = "X.1R9";
    public static final String METHOD_m19085a = "a";

    public static final String CLASS_CallerContext = "com.facebook.common.callercontext.CallerContext";
    public static final String METHOD_getFromContext = "a";

    public static final String CLASS_MessageDraft = "com.facebook.messaging.model.messages.MessageDraft";

    public static final String CLASS_AnonymousClass00A = "X.00A";
    public static final String CLASS_AnonymousClass0QG = "X.0QG";

    public static final String CLASS_ThreadSummary = "com.facebook.messaging.model.threads.ThreadSummary";

    public static final String CLASS_ThreadSummaryBuilder = "X.ThreadSummaryBuilder";
    public static final String CLASS_Parcel = "android.os.Parcel";
    public static final String CLASS_PostGameScoreParams = "com.facebook.messaging.service.model.PostGameScoreParams";
    public static final String CLASS_PostGameScoreParamsCache = "X.PostGameScoreParamsCache";

    public static final String CLASS_SessionCookie = "com.facebook.auth.credentials.SessionCookie";
    public static final String METHOD_m37126a = "a";

    public static final String CLASS_C16510ex = "X.0ex";

    public static final String CLASS_C91283cG = "X.3cG";
    public static final String METHOD_addCookie = "addCookie";
    public static final String METHOD_clear = "clear";
    public static final String METHOD_getCookies = "getCookies";
    public static final String METHOD_toString = "toString";

    public static final String CLASS_Cookie = "org.apache.http.cookie.Cookie";

    public static final String CLASS_FacebookUser = "com.facebook.ipc.model.FacebookUser";

    public static final String CLASS_StartScreenActivity = "com.facebook.messaging.auth.StartScreenActivity";
    public static final String METHOD_onActivityCreate = "c";

    public static final String CLASS_PasswordCredentialsFragment = "com.facebook.auth.login.ui.PasswordCredentialsFragment";
    public static final String METHOD_m63337a = "a";

    public static final String CLASS_NeuePasswordCredentialsViewGroup = "com.facebook.messaging.auth.NeuePasswordCredentialsViewGroup";
    public static final String METHOD_onAuthSuccess = "onAuthSuccess";
}
