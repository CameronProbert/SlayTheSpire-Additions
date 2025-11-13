package communitycards.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import communitycards.relics.RelicCombatManual;


public class PatchCombatManual {
    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.AbstractCard",
            method=SpirePatch.CLASS
    )
    public static class PatchCombatManualField {
        public static SpireField<Boolean> inCombatManual = new SpireField<>(() -> false);

        @SpirePatch(
                cls = "com.megacrit.cardcrawl.cards.AbstractCard",
                method = "makeStatEquivalentCopy"
        )
        public static class MakeStatEquivalentCopy {
            public static AbstractCard Postfix(AbstractCard __result, AbstractCard __instance) {
                inCombatManual.set(__result, inCombatManual.get(__instance));
                return __result;
            }
        }
    }

    @SpirePatch(
            clz=AbstractPlayer.class,
            method="bottledCardUpgradeCheck"
    )
    public static class BottledCardUpgradeCheck {
        public static void Postfix(AbstractPlayer __instance, AbstractCard c) {
            if (PatchCombatManualField.inCombatManual.get(c) && __instance.hasRelic(RelicCombatManual.RELIC_ID)) {
                ((RelicCombatManual) __instance.getRelic(RelicCombatManual.RELIC_ID)).setDescriptionAfterLoading();
            }
        }
    }
}
