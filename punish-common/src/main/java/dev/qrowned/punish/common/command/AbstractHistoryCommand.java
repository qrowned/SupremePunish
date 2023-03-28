package dev.qrowned.punish.common.command;

import dev.qrowned.config.message.api.MessageService;
import dev.qrowned.punish.api.command.AbstractPunishCommand;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.punish.PunishmentReason;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.util.DurationFormatter;
import dev.qrowned.punish.common.util.PageUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public abstract class AbstractHistoryCommand<P> extends AbstractPunishCommand<P> {

    private static final int PAGE_SIZE = 3;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    protected final PunishUserHandler userHandler;
    protected final MessageService<P> messageService;
    protected final PunishmentHandler punishmentHandler;

    @Override
    public void execute(P sender, String[] args) {
        if (args.length == 0) {
            this.handleNoSubCommandFound(sender, args);
            return;
        }

        this.userHandler.fetchUser(args[0]).thenAcceptAsync(abstractPunishUser -> {
            if (abstractPunishUser == null) {
                this.messageService.getMessage("punish.user.notExisting").send(sender);
                return;
            }

            UUID uuid = abstractPunishUser.getUuid();
            int page = args.length > 1 && StringUtils.isNumeric(args[1]) ? Integer.parseInt(args[1]) : 1;
            this.getPunishments(uuid, page).thenAcceptAsync(historyMapping -> {
                if (historyMapping.getTotalPunishments() == 0) {
                    this.messageService.getMessage("punish.history.noPunishments").send(sender);
                    return;
                }

                this.messageService.getMessage("punish.history.header").send(sender,
                        "%name%", abstractPunishUser.getName(),
                        "%uuid%", abstractPunishUser.getUuid().toString(),
                        "%page%", Integer.toString(page));

                historyMapping.getPunishments().forEach(punishment -> {
                    AbstractPunishUser executorPunishUser = this.userHandler.getUser(punishment.getExecutor());
                    if (executorPunishUser == null) return;
                    PunishmentReason punishmentReason = this.punishmentHandler.getPunishmentReason(punishment.getReason());

                    if (punishment.isPardon()) {
                        AbstractPunishUser pardonPunishUser = this.userHandler.getUser(punishment.getPardonExecutor());
                        if (pardonPunishUser == null) return;
                        this.messageService.getMessage("punish.history.entry.pardon").send(sender,
                                "%date%", SIMPLE_DATE_FORMAT.format(punishment.getExecutionTime()),
                                "%name%", abstractPunishUser.getName(),
                                "%duration%", DurationFormatter.formatPunishDuration(punishment.getDuration()),
                                "%executor%", executorPunishUser.getName(),
                                "%type%", punishment.getType().name(),
                                "%reason%", punishmentReason == null ? punishment.getReason() : punishmentReason.getDisplayName(),
                                "%pardonReason%", punishment.getPardonReason(),
                                "%pardonExecutor%", pardonPunishUser.getName(),
                                "%pardonDate%", SIMPLE_DATE_FORMAT.format(punishment.getPardonExecutionTime()),
                                "%evidence%", punishment.getEvidence().isEmpty() ? "" : "§7[§e§l" + punishment.getEvidence().get() + "§7]");
                    } else {
                        this.messageService.getMessage("punish.history.entry").send(sender,
                                "%date%", SIMPLE_DATE_FORMAT.format(punishment.getExecutionTime()),
                                "%name%", abstractPunishUser.getName(),
                                "%duration%", DurationFormatter.formatPunishDuration(punishment.getDuration()),
                                "%executor%", executorPunishUser.getName(),
                                "%type%", punishment.getType().name(),
                                "%reason%", punishmentReason == null ? punishment.getReason() : punishmentReason.getDisplayName(),
                                "%evidence%", punishment.getEvidence().isEmpty() ? "" : "§7[§e§l" + punishment.getEvidence().get() + "§7]");
                    }
                });

                if (historyMapping.getRemaining() > 0)
                    this.sendFooterMessage(sender, historyMapping.getRemaining(), page, abstractPunishUser.getName());
            });
        });
    }

    protected abstract void sendFooterMessage(P sender, int remaining, int currentPage, String name);

    private CompletableFuture<HistoryMapping> getPunishments(UUID uuid, int page) {
        return this.punishmentHandler.getPunishments(uuid)
                .thenApplyAsync(punishments -> {
                    List<Punishment> sortedPunishments = punishments.stream()
                            .filter(punishment -> !punishment.getType().equals(Punishment.Type.KICK))
                            .sorted(Comparator.comparingLong(Punishment::getExecutionTime).reversed()).toList();
                    List<Punishment> paginatedList = PageUtils.getPaginatedList(sortedPunishments, page, PAGE_SIZE);
                    int fromIndex = (page - 1) * PAGE_SIZE;
                    final int toIndex = Math.min(fromIndex + PAGE_SIZE, sortedPunishments.size());

                    return new HistoryMapping(sortedPunishments.size(), paginatedList, sortedPunishments.size() - toIndex);
                });
    }

    @Getter
    @AllArgsConstructor
    private static class HistoryMapping {
        private final int totalPunishments;
        private final List<Punishment> punishments;
        private final int remaining;
    }

}
