package com.grim3212.assorted.crates.common.network;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.platform.services.INetworkHelper;
import com.grim3212.assorted.crates.Constants;
import net.minecraft.resources.ResourceLocation;

public class CratesPackets {

    public static void init() {
        Services.NETWORK.register(new INetworkHelper.MessageHandler<>(resource("set_all_slots_locked"), SetAllSlotLockPacket.class, SetAllSlotLockPacket::encode, SetAllSlotLockPacket::decode, SetAllSlotLockPacket::handle, INetworkHelper.MessageBoundSide.SERVER));
        Services.NETWORK.register(new INetworkHelper.MessageHandler<>(resource("set_slot_locked"), SetSlotLockPacket.class, SetSlotLockPacket::encode, SetSlotLockPacket::decode, SetSlotLockPacket::handle, INetworkHelper.MessageBoundSide.SERVER));
        Services.NETWORK.register(new INetworkHelper.MessageHandler<>(resource("crate_sync"), SyncCrate.class, SyncCrate::encode, SyncCrate::decode, SyncCrate::handle, INetworkHelper.MessageBoundSide.CLIENT));
    }

    private static ResourceLocation resource(String name) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
    }

}
