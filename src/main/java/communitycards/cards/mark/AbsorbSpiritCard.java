package communitycards.cards.mark;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import communitycards.actions.mark.AbsorbSpiritAction;
import communitycards.cards.BaseCard;
import communitycards.util.CardStats;

public class AbsorbSpiritCard extends BaseCard {
    public static final String ID = makeID("AbsorbSpiritCard");
    private static final CardStats info = new CardStats(
            CardColor.PURPLE, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    private static final int MARK_TO_APPLY_NUM = 8;
    private static final int MARK_PER_HP_NUM = 3;
    private static final int MARK_PER_HP_NUM_UPGRADE = -1;

    public AbsorbSpiritCard() {
        super(ID, info);
        setMagic(MARK_TO_APPLY_NUM);
        setCustomVar("MARK_PER_HP", MARK_PER_HP_NUM, MARK_PER_HP_NUM_UPGRADE);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new MarkPower(p, magicNumber), magicNumber));
        this.addToBot(new AbsorbSpiritAction(m, p, customVar("MARK_PER_HP")));
    }
}
