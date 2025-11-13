package communitycards.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import communitycards.patches.PatchCombatManual;

import java.util.function.Predicate;

import static communitycards.CommunityCards.makeID;

/**
 * <a href="https://www.reddit.com/r/slaythespire/comments/1n0teuj/boss_relic_idea_no_clue_if_its_weak_or_strong/">Credit</a>
 */
public class RelicCombatManual extends BaseRelic implements CustomBottleRelic, CustomSavable<Integer> {
    public static final String RELIC_ID = makeID("CombatManual");

    private boolean cardSelected = true;
    public AbstractCard card = null;

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public RelicCombatManual() {
        super(RELIC_ID, RelicTier.BOSS, LandingSound.FLAT);
    }

    public AbstractCard getCard() {
        return this.card.makeCopy();
    }

    @Override
    public boolean canSpawn() {
        return CardHelper.hasCardType(AbstractCard.CardType.POWER);
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return PatchCombatManual.PatchCombatManualField.inCombatManual::get;
    }

    @Override
    public void onEquip() {
        super.onEquip();
        if (!AbstractDungeon.player.masterDeck.getPurgeableCards().getPowers().isEmpty()) {
            this.cardSelected = false;
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards().getPowers(), 1, this.DESCRIPTIONS[1] + this.name + LocalizedStrings.PERIOD, false, false, false, false);
        }
    }

    @Override
    public void onUnequip() {
        if (card != null) {
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card);
            if (cardInDeck != null) {
                PatchCombatManual.PatchCombatManualField.inCombatManual.set(cardInDeck, false);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.cardSelected = true;
            this.card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(this.card.name, "y") + this.DESCRIPTIONS[3];
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
        }
    }

    @Override
    public void atBattleStartPreDraw() {
        super.atBattleStartPreDraw();
        doAction();
    }

    public void doAction() {
        if (this.card == null || !AbstractDungeon.player.masterDeck.contains(card)) {
            this.card = null;
            return;
        }
        CardGroup drawPile = AbstractDungeon.player.drawPile;
        for (AbstractCard card : drawPile.group) {
            if (card.uuid == this.card.uuid) {
                this.addToTop(new NewQueueCardAction(this.card, (AbstractCreature)null, false, true));
                drawPile.removeCard(card);
                this.flash();
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                return;
            }
        }
    }

    @Override
    public Integer onSave() {
        if (card != null) {
            return AbstractDungeon.player.masterDeck.group.indexOf(card);
        } else {
            return -1;
        }
    }
    @Override
    public void onLoad(Integer cardIndex) {
        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (card != null) {
                PatchCombatManual.PatchCombatManualField.inCombatManual.set(card, true);
                setDescriptionAfterLoading();
            }
        }
    }

    public void setDescriptionAfterLoading() {
        this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(this.card.name, "y") + this.DESCRIPTIONS[3];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
}
