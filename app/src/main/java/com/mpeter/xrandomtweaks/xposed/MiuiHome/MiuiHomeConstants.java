package com.mpeter.xrandomtweaks.xposed.MiuiHome;

public class MiuiHomeConstants {
    public static final String CLASS_CellLayout = "com.miui.home.launcher.CellLayout";
        public static final String METHOD_loadGridConfig = "loadGridConfig";
        public static final String METHOD_addView = "addView";
        public static final String METHOD_cellToPoint = "cellToPoint";
        public static final String METHOD_measureChild = "measureChild";
        public static final String METHOD_onLayout = "onLayout";
        public static final String METHOD_onMeasure = "onMeasure";
        public static final String METHOD_getScreenType = "getScreenType";

        public static final String FIELD_mHeightGap = "mHeightGap";
        public static final String FIELD_mCellPaddingTop = "mCellPaddingTop";
        public static final String FIELD_mWidgetCellPaddingTop = "mWidgetCellPaddingTop";
        public static final String FIELD_mCellWorkingHeight = "mCellWorkingHeight";

        public static final String CL_SUBCLASS_LayoutParams = "com.miui.home.launcher.CellLayout$LayoutParams";
            public static final String METHOD_setup = "setup";

    public static final String CLASS_DeviceConfig = "com.miui.home.launcher.DeviceConfig";
        public static final String METHOD_needHideMinusScreen = "needHideMinusScreen";
        public static final String METHOD_getWidgetCellPaddingTop = "getWidgetCellPaddingTop";
        public static final String METHOD_calcGridSize = "calcGridSize";
}
