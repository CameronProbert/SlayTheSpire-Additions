package communitycards.actions.mark;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ChakraRealignmentAction extends AbstractGameAction {
    private final DamageInfo info;

    public ChakraRealignmentAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startDuration;
    }

    public void update() {
        if (this.shouldCancelAction()) {
            this.isDone = true;
        } else {
            this.tickDuration();
            if (this.isDone) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY, false));
                this.target.damage(this.info);
                if (this.target.lastDamageTaken > 0) {
                    this.addToTop(new ApplyPowerAction(this.target, this.source, new MarkPower(this.target, this.target.lastDamageTaken)));
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                } else {
                    this.addToTop(new WaitAction(0.1F));
                }
            }

        }
    }
}
