package communitycards.powers.mark;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import communitycards.powers.BasePower;

import static communitycards.CommunityCards.makeID;

public class KiStrikesPower extends BasePower {
    public static final String POWER_ID = makeID("KiStrikesPower");
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public KiStrikesPower(AbstractCreature owner, int newAmount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, newAmount);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new MarkPower(target, amount),amount));
    }
}
