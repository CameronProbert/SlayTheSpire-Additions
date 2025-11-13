package communitycards.actions.mark;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import com.megacrit.cardcrawl.vfx.combat.FlyingOrbEffect;

public class AbsorbSpiritAction extends AbstractGameAction {
    private final int stealPerXMark;

    public AbsorbSpiritAction(AbstractCreature target, AbstractCreature source, int stealPerXMark) {
        this.setValues(target, source);
        this.actionType = ActionType.DAMAGE;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startDuration;
        this.stealPerXMark = stealPerXMark;
    }

    public void update() {
        if (this.shouldCancelAction()) {
            this.isDone = true;
        } else {
            this.tickDuration();
            if (this.isDone) {
                if (!target.isDying && target.currentHealth > 0 && !target.isEscaping) {
                    if (target.hasPower(MarkPower.POWER_ID)) {
                        int mark = target.getPower(MarkPower.POWER_ID).amount;
                        int damageToDo = mark / stealPerXMark;
                        target.damage(new DamageInfo(source, damageToDo, DamageInfo.DamageType.HP_LOSS));

                        if (target.lastDamageTaken > 0) {
                            for (int j = 0; j < target.lastDamageTaken / 2 && j < 10; ++j) {
                                this.addToBot(new VFXAction(new FlyingOrbEffect(target.hb.cX, target.hb.cY)));
                            }
                            if (!Settings.FAST_MODE) {
                                this.addToBot(new WaitAction(0.3F));
                            }
                            this.addToBot(new HealAction(this.source, this.source, target.lastDamageTaken));
                        }

                    }
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
