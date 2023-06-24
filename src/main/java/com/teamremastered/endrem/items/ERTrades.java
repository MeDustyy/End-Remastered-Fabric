package com.teamremastered.endrem.items;

import com.teamremastered.endrem.config.ERConfigHandler;
import com.teamremastered.endrem.registry.ERItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.NotNull;

public class ERTrades {

    private static class EREyeTrade implements TradeOffers.Factory {
        int maxPrice = 50;
        int minPrice = 30;

        @Override
        public TradeOffer create(@NotNull Entity entity, Random random){
            int priceEmeralds = random.nextInt(maxPrice - minPrice) + minPrice;
            if (!entity.getWorld().isClient) {
                return new TradeOffer(new ItemStack(Items.EMERALD, priceEmeralds), new ItemStack(Items.RABBIT_FOOT), new ItemStack(ERItems.EVIL_EYE), priceEmeralds, 12, 10, 0.2F);
            }
            return null;
        }
    }

    public static void registerVillagerTrades() {
        if (ERConfigHandler.IS_EVIL_EYE_OBTAINABLE) {
            TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 5, factories -> {
                factories.add(new EREyeTrade());
            });

            TradeOfferHelper.registerWanderingTraderOffers(0, factories -> {
                factories.add(new EREyeTrade());
            });
        }
    }
}
