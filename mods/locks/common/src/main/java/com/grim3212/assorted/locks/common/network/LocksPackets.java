package com.grim3212.assorted.locks.common.network;

import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.platform.services.INetworkHelper;
import com.grim3212.assorted.locks.Constants;
import net.minecraft.resources.Identifier;

public class LocksPackets {

    public static void init() {
        Services.NETWORK.register(new INetworkHelper.MessageHandler<>(resource("set_locked"), SetLockPacket.class, SetLockPacket::encode, SetLockPacket::decode, SetLockPacket::handle, INetworkHelper.MessageBoundSide.SERVER));
    }

    private static Identifier resource(String name) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, name);
    }

}
