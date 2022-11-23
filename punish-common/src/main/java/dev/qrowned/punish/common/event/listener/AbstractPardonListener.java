package dev.qrowned.punish.common.event.listener;

import dev.qrowned.punish.api.event.EventAdapter;
import dev.qrowned.punish.api.event.impl.PlayerPardonEvent;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class AbstractPardonListener extends EventAdapter<PlayerPardonEvent> {

    protected final MessageHandler<?> messageHandler;
    protected final PunishUserHandler punishUserHandler;
    protected final PunishmentDataHandler punishmentDataHandler;

    @Override
    public void handleReceive(@NotNull PlayerPardonEvent event) {
        this.punishmentDataHandler.invalidate(event.getTarget());

        AbstractPunishUser targetUser = this.punishUserHandler.getUser(event.getTarget());
        AbstractPunishUser executorUser = this.punishUserHandler.getUser(event.getExecutor());

        if (targetUser == null || executorUser == null) return;

        Punishment punishment = event.getPunishment();
        if (punishment.getType().equals(Punishment.Type.BAN)) {
            this.messageHandler.getMessage("punish.unban.notify").broadcast("supremepunish.notify.ban",
                    "%target%", targetUser.getName(),
                    "%executor%", executorUser.getName(),
                    "%reason%", punishment.getPardonReason());
        }
    }

}
