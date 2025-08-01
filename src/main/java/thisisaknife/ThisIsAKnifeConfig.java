package thisisaknife;

import basemod.EasyConfigPanel;

public class ThisIsAKnifeConfig extends EasyConfigPanel {

    public static boolean enableCardThisIsAKnife = true;
    public static boolean enableRelicBloodMoney = true;

    public ThisIsAKnifeConfig() {
        super(ThisIsAKnife.modID, ThisIsAKnife.makeID("ThisIsAKnifeConfig"));
    }
}
