package dev.qrowned.punish.velocity.listener;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import dev.qrowned.config.message.velocity.VelocityConfigMessage;
import dev.qrowned.config.message.velocity.VelocityMessageService;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.punish.PunishmentReason;
import dev.qrowned.punish.common.event.listener.AbstractConnectionListener;
import dev.qrowned.punish.common.util.DurationFormatter;
import dev.qrowned.punish.velocity.PunishVelocityPlugin;
import org.jetbrains.annotations.NotNull;

public final class VelocityConnectionListener extends AbstractConnectionListener {

    private final PunishmentHandler punishmentHandler;
    private final VelocityMessageService messageService;

    public VelocityConnectionListener(@NotNull PunishVelocityPlugin plugin,
                                      @NotNull PunishmentHandler punishmentHandler,
                                      @NotNull VelocityMessageService messageService) {
        super(plugin);
        this.punishmentHandler = punishmentHandler;
        this.messageService = messageService;
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onLogin(@NotNull LoginEvent event, @NotNull Continuation continuation) {
        Player player = event.getPlayer();

        super.loadUser(player.getUniqueId(), player.getUsername()).thenAcceptAsync(punishUser -> {
            this.punishmentHandler.getActivePunishment(punishUser.getUuid(), Punishment.Type.BAN).thenAcceptAsync(punishmentOptional -> {
                if (punishmentOptional.isPresent()) {
                    Punishment punishment = punishmentOptional.get();
                    PunishmentReason punishmentReason = this.punishmentHandler.getPunishmentReason(punishment.getReason());

                    VelocityConfigMessage banScreenMessage = (VelocityConfigMessage) this.messageService.getMessage("punish.ban.screen");
                    event.setResult(ResultedEvent.ComponentResult.denied(banScreenMessage.parseComponent(
                            "%reason%", punishmentReason == null ? punishment.getReason() : punishmentReason.getDisplayName(),
                            "%end%", DurationFormatter.formatPunishDuration(punishment.getRemainingDuration()),
                            "%id%", Integer.toString(punishment.getId()))));
                }

                continuation.resume();
            });
        });

        super.processJoin(player.getUniqueId(), player.getUsername());
    }

}
