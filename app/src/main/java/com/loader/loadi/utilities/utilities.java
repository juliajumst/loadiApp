package com.loader.loadi.utilities;

/**
 * Created by Wendy on 11/5/2016.
 */
public class utilities {
    final public static int SMART = 1;
    final public static int GLOBE = 2;
    final public static int SUN = 3;

    public static int getNetwork(String phone){
        String prefix = phone.substring(0,4);
        switch (prefix){
            case "0905":case "0906":case "0915":case "0916":case "0917":case "0926":case "0927":case "0935":case "0936":case "0945":case "0975":case "0994":case "0995":case "0996":case "0997":case "0976":case "0977":case "0978":case "0979":case "0937":
                return GLOBE;
            case "0813":case "0900":case "0907":case "0908":case "0909":case "0910":case "0911":case "0912":case "0913":case "0914":case "0918":case "0919":case "0920":case "0921":case "0928":case "0929":case "0930":case "0938":case "0939":case "0940":case "0946":case "0947":case "0948":case "0949":case "0950":case "0989":case "0998":case "0999":
                return SMART;
            case "0922":case "0923":case "0924":case "0925":case "0931":case "0932":case "0933":case "0934":case "0942":case "0943":case "0944":
                return SUN;
            default:
                return 0;
        }
    }
}
