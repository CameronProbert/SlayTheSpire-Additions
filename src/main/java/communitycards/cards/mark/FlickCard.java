package communitycards.cards.mark;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.watcher.TriggerMarksAction;
import com.megacrit.cardcrawl.cards.purple.PressurePoints;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import communitycards.cards.BaseCard;
import communitycards.util.CardStats;

public class FlickCard extends BaseCard {
    public static final String ID = makeID("FlickCard");
    private static final CardStats info = new CardStats(
            CardColor.PURPLE, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    private static final int CARD_DRAW = 1;
    private static final int CARD_DRAW_UPGRADE = 1;

    public FlickCard() {
        super(ID, info);
        setMagic(CARD_DRAW, CARD_DRAW_UPGRADE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new TriggerMarksAction(new PressurePoints()));
        this.addToBot(new DrawCardAction(magicNumber));
    }
}
